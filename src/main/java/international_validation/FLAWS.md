# International Address Validation — Flaws & Discussion Points

## User Story

> As an international mailing customer, I want addresses to be validated according to the destination country's conventions so that international shipments are processed correctly.

---

## Acceptance Criteria

- The system supports multiple countries.
- Validation rules depend on the destination country.
- Unsupported countries are reported.

---

## Critical Bugs

| # | File | Location | Description |
|---|------|----------|-------------|
| 1 | `InternationalValidationService.java` | `validateAddress()` | **NullPointerException after null check** — when `countryCode` or `address` is null, only prints a message but continues execution, leading to NPE when accessing null values. |
| 2 | `InternationalValidationResult.java` | `addViolation()` | **Broken valid state logic** — `valid = valid - (violations.size() > 0 ? 1 : 0) > 0` is nonsensical. Tries to subtract a boolean from a boolean; valid starts as `true` (1) and becomes falsy after first violation in wrong way. |
| 3 | `InternationalValidationResult.java` | `getViolationCount()` | **String comparison with `==`** — severity strings created with `new String(...)` will never match the literal `"CRITICAL"` via `==`. Method always returns 0. |
| 4 | `InternationalValidationService.java` | `validateAddressParts()` | **Off-by-one array indexing** — splits address into parts but logic for checking state presence is fragile. Different country rules have different part orderings but no validation. |
| 5 | `InternationalValidationService.java` | `validatePostalCode()` | **No null check before array access** — if address has no commas, `parts.length == 0` and `parts[parts.length - 1]` would cause ArrayIndexOutOfBoundsException. |

---

## Resource Leaks

| # | File | Location | Description |
|---|------|----------|-------------|
| 6 | `InternationalValidationService.java` | `storeResult()` | **FileWriter never closed** — if the write succeeds, the writer is never flushed or closed. Data may be lost and file handles leak. |

---

## Design & Architecture Issues

| # | File | Location | Description |
|---|------|----------|-------------|
| 7 | `InternationalValidationService.java` | `ruleCache` | **Static mutable shared state** — rule cache is a static `HashMap` accessed from instance methods. Race condition if multiple service instances are created. Not thread-safe. |
| 8 | `InternationalValidationController.java` | `validate()` | **Untyped request body** — uses `HashMap<String, String>` instead of a proper DTO. No input validation on extracted values. |
| 9 | `InternationalValidationController.java` | `validateBatch()` | **No size limit on batch** — accepts an unbounded array; attackers can cause OOM or CPU exhaustion by sending 1 million addresses. |
| 10 | `InternationalValidationResult.java` | all fields | **Package-private fields, no getters** — Jackson cannot serialize these fields by default. REST responses will be empty JSON objects. |
| 11 | `CountryRule.java` | all fields | **Public mutable fields** — no encapsulation. Any caller can modify rule data after creation, corrupting validation logic. |
| 12 | `InternationalValidationService.java` | `getRules()` | **Returns mutable map directly** — callers can corrupt internal rule state. Adding or removing countries affects all future validations. |

---

## Poor Practices

| # | File | Location | Description |
|---|------|----------|-------------|
| 13 | `InternationalValidationService.java` | `storeResult()` | **Hardcoded file path** (`/tmp/international-results/`) — not configurable, OS-specific, directory may not exist. |
| 14 | `InternationalValidationService.java` | `storeResult()` catch | **Swallowed exception** — prints to stdout instead of logging or propagating. Failures are invisible. |
| 15 | `InternationalValidationService.java` | `initializeRules()` | **Hardcoded country rules** — should be externalized to configuration file or database. Adding new countries requires code changes. |
| 16 | `InternationalValidationResult.java` | `timestamp` | **Non-standard date format** — `new Date().toString()` produces locale-dependent, non-parseable output instead of ISO-8601. |
| 17 | `InternationalValidationController.java` | `validateBatch()` | **Fragile HashMap extraction** — assumes specific keys and structure. No validation that country and addresses arrays exist. |
| 18 | `InternationalValidationService.java` | `validateAddress()` | **No return on null check** — even after detecting null input, method continues and attempts to use null values. |
| 19 | `InternationalValidationResult.java` | `isValid()` | **Redundant logic** — both `addViolation()` and `isValid()` try to set/check validity, but the methods are inconsistent. |

---

## Testing Issues

| # | File | Location | Description |
|---|------|----------|-------------|
| 20 | `InternationalValidationServiceTest.java` | `testUSAddressValidation()` | **Tautological assertion** — `assertTrue(result.violations.size() >= 0)` is always true regardless of behavior. |
| 21 | `InternationalValidationServiceTest.java` | `testNullCountry()` | **Empty test** — calls the method but never asserts anything. The null-handling bug goes undetected. |
| 22 | `InternationalValidationServiceTest.java` | `testViolationCount()` | **Absurdly wide bounds** — asserts violations are between -10 and 100, which proves nothing useful. |
| 23 | `InternationalValidationServiceTest.java` | all tests | **Direct field access** — tests access package-private fields, coupling them to internal representation. |

---

## Acceptance Criteria Coverage

| Criterion | Met? | Notes |
|-----------|------|-------|
| System supports multiple countries | ⚠️ Partially | Countries hardcoded in code; only 4 countries pre-loaded. Adding new countries requires code changes. |
| Validation rules depend on destination country | ⚠️ Partially | Rules exist but validation logic is fragile and doesn't properly check all rule properties. |
| Unsupported countries are reported | ✅ Yes | Throws RuntimeException but leaks country code in message; no proper HTTP status code. |
| Country information is available | ⚠️ Partially | Rules are hardcoded and not externalized. Cannot be updated without redeployment. |


