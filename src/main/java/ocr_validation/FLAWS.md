# OCR Validation — Flaws & Discussion Points

## User Story

> As a mailing service customer, I want the system to check whether the address can be reliably recognized by OCR so that I can detect problems that might prevent automated processing.

---

## Critical Bugs

| # | File | Location | Description |
|---|------|----------|-------------|
| 1 | `OcrValidationService.java` | `validateAddress()` | **NullPointerException after null check** — when `addressText` is null, it prints a message but does not return or throw. Execution continues into `generateHash(addressText)` which calls `input.getBytes()` on null. |
| 2 | `OcrResult.java` | `getErrorCount()` / `getWarningCount()` | **String comparison with `==`** — severity strings created with `new String(...)` in `analyzeCharacters()` will never match the literals `"ERROR"` / `"WARNING"` via `==`. These methods always return 0. |
| 3 | `OcrResult.java` | `addIssue()` | **Broken confidence calculation** — subtracts `1.0 / issues.size()` on each addition. With 1 issue: `1.0 - 1.0 = 0.0`. With 2 issues: `0.0 - 0.5 = -0.5`. Confidence goes negative, which is nonsensical. |
| 4 | `OcrResult.java` | `isReliable()` | **Floating-point equality with `==`** — comparing a double to `0.85` with `==` is almost never true due to floating-point representation. |
| 5 | `OcrValidationService.java` | `simulateOcrConfidence()` | **Characters 'Q' and 'q' are never flagged** — their confidence values (0.8 and 0.78) exceed `WARNING_THRESHOLD` (0.75), so `analyzeCharacters()` skips them despite being in `PROBLEMATIC_CHARS`. |

---

## Resource Leaks

| # | File | Location | Description |
|---|------|----------|-------------|
| 6 | `OcrValidationService.java` | `storeResult()` | **FileWriter never closed** — if the write succeeds, the writer is never flushed or closed. Data may be lost and file handles leak. |
| 7 | `OcrValidationService.java` | `convertToImage()` | **Graphics2D never disposed** — `g.dispose()` is never called, leaking native graphics resources. |

---

## Design & Architecture Issues

| # | File | Location | Description |
|---|------|----------|-------------|
| 8 | `OcrValidationService.java` | `resultCache` | **Static mutable shared state** — the cache is a plain `HashMap` accessed from a static context with no synchronization. Not thread-safe; concurrent requests cause race conditions. |
| 9 | `OcrValidationService.java` | `resultCache` | **Unbounded cache** — entries are never evicted. Over time this causes an OutOfMemoryError. |
| 10 | `OcrValidationController.java` | `service` field | **Manual instantiation bypasses Spring DI** — `new OcrValidationService()` means Spring does not manage the bean. `@Service` annotation is useless. |
| 11 | `OcrValidationController.java` | `clearCache()` | **GET endpoint with side effects** — clearing the cache mutates state, violating HTTP semantics. No authentication or authorization. |
| 12 | `OcrValidationController.java` | `validate()` | **Untyped request body** — uses `HashMap<String, String>` instead of a proper DTO. No input validation. |
| 13 | `OcrValidationController.java` | `validateBatch()` | **No size limit on batch** — accepts an unbounded array; attackers can cause OOM or CPU exhaustion. |
| 14 | `OcrResult.java` | all fields | **Package-private fields, no getters** — Jackson cannot serialize these fields by default. REST responses will be empty JSON objects. |
| 15 | `OcrValidationService.java` | `getCache()` | **Internal state exposed** — returns the mutable map directly. Callers can corrupt internal state. |

---

## Poor Practices

| # | File | Location | Description |
|---|------|----------|-------------|
| 16 | `OcrValidationService.java` | `storeResult()` | **Hardcoded file path** (`/tmp/ocr-results/`) — not configurable, OS-specific, directory may not exist. |
| 17 | `OcrValidationService.java` | `storeResult()` catch | **Swallowed exception** — prints to stdout instead of logging or propagating. Failures are invisible. |
| 18 | `OcrValidationService.java` | `generateHash()` | **Uses MD5** — cryptographically broken hash algorithm. Also swallows `NoSuchAlgorithmException` and returns a constant `"no-hash"`. |
| 19 | `OcrValidationService.java` | `generateHash()` | **Broken hex encoding** — `Integer.toHexString(b & 0xff)` does not zero-pad single-digit values, producing inconsistent hash lengths. |
| 20 | `OcrResult.java` | `timestamp` | **Non-standard date format** — `new Date().toString()` produces locale-dependent, non-parseable output instead of ISO-8601. |
| 21 | `OcrValidationService.java` | `convertToImage()` | **Fake OCR** — renders text as an image but never actually performs character recognition on the image. The "analysis" works on the original string, defeating the stated purpose. |
| 22 | `CharacterIssue.java` | all fields | **Public mutable fields** — no encapsulation. Any caller can modify issue data after creation. |
| 23 | `OcrValidationService.java` | `analyzeCharacters()` | **`new String("ERROR")`** — allocates unnecessary String objects that break `==` comparison in `OcrResult`. |
| 24 | `OcrValidationController.java` | `getResult()` | **Raw RuntimeException with user input** — leaks the hash value in the exception message; no proper HTTP status code. |

---

## Testing Issues

| # | File | Location | Description |
|---|------|----------|-------------|
| 25 | `OcrValidationServiceTest.java` | `testProblematicCharacters()` | **Tautological assertion** — `assertTrue(result.issues.size() >= 0)` is always true regardless of behavior. |
| 26 | `OcrValidationServiceTest.java` | `testConfidence()` | **Absurdly wide bounds** — asserts confidence is between -10.0 and 1.0, which proves nothing useful. |
| 27 | `OcrValidationServiceTest.java` | `testNullAddress()` | **Empty test** — creates a service but never calls anything or asserts. The null-handling bug goes undetected. |
| 28 | `OcrValidationServiceTest.java` | `testCaching()` | **Tests implementation detail** — `assertSame` verifies caching behavior, but the shared mutable reference means modifications to one result corrupt the other. |
| 29 | `OcrValidationServiceTest.java` | all tests | **Direct field access** — tests access package-private fields, coupling them to internal representation. |

---

## Acceptance Criteria Coverage

| Criterion | Met? | Notes |
|-----------|------|-------|
| Converts address area into an image | ⚠️ Partially | Creates a `BufferedImage` but never uses it for recognition. |
| Attempts to recognize address using OCR | ❌ No | No OCR library is used. Analysis operates on the original string. |
| Reports when characters cannot be reliably recognized | ⚠️ Partially | Reports issues, but confidence calculation is broken (goes negative). |
| Identifies potentially problematic characters | ⚠️ Partially | Some characters in `PROBLEMATIC_CHARS` (Q, q) are never actually flagged due to threshold logic. |
| Provides a confidence value for the OCR result | ❌ Broken | Confidence formula is mathematically incorrect and `isReliable()` uses broken float comparison. |

