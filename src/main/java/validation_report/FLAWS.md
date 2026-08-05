# Code Review: validation_report Package

## User Story
> As a mailing service customer I want to receive a detailed validation report so that I understand why an address is not machine-readable.

---

## Flaws Summary

### Critical Bugs

| # | Flaw | File | Details |
|---|------|------|---------|
| 1 | String comparison with `==` | `ValidationReport` | `getErrors()` and `getRecommendations()` use `==` instead of `.equals()` — will silently return empty lists |
| 2 | NPE after null check | `ReportGeneratorService` | Checks `if (street == null)` then immediately calls `street.length()` on the next conditional — same for `city` and `recipient` |
| 3 | `isValid()` contradicts `status` | `ValidationReport` | `isValid()` checks `violations.size() == 0`, but `status` is set independently in `addViolation()` — inconsistent state possible |

### Design & Architecture Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 4 | Public fields, no encapsulation | `Violation`, `ValidationReport` | Fields are public/package-private — no control over access or mutation |
| 5 | Static mutable HashMap as "storage" | `ReportGeneratorService` | In-memory, not thread-safe, lost on restart — doesn't fulfill "stored for later reference" |
| 6 | Severity as magic strings | Throughout | `"ERROR"` and `"RECOMMENDATION"` are raw strings — no enum, easy to typo |
| 7 | Untyped request body | `ReportController` | Uses `HashMap<String, String>` instead of a typed DTO |
| 8 | Raw RuntimeException for 404 | `ReportController` | Throws `RuntimeException` when report not found — no proper HTTP status handling |
| 9 | No serialization support | `ValidationReport` | Package-private fields won't be serialized by Jackson without annotations or public getters |

### Poor Practices

| # | Flaw | File | Details |
|---|------|------|---------|
| 10 | No `addressId` validation | `ReportGeneratorService` | `addressId` can be null — map key would be null |
| 11 | No pagination on `getAllReports()` | `ReportController` | Returns all reports in memory — unbounded response |
| 12 | Status set in two places | `ValidationReport` + `ReportGeneratorService` | `addViolation()` sets "INVALID", generator sets "VALID" at end — unclear ownership |
| 13 | `timestamp` on Violation but unused | `Violation` | Field is set but never exposed or used in any logic |

### Testing Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 14 | Tests access internal fields directly | `ReportGeneratorServiceTest` | `report.status` and `report.violations.size()` — tightly coupled to implementation |
| 15 | No test for the `==` bug | `ReportGeneratorServiceTest` | Tests never call `getErrors()` or `getRecommendations()` so the `==` bug goes undetected |

---

## Acceptance Criteria vs. Implementation

| Criterion | Met? | Issue |
|-----------|------|-------|
| Every violation is explained | ✅ | Each violation has a message |
| Violations reference the corresponding postal rule | ✅ | `ruleReference` field present (e.g. "DP-001") |
| Report distinguishes between errors and recommendations | ❌ | `getErrors()` / `getRecommendations()` use `==` for string comparison — always return empty lists |
| Report can be stored for later reference | ❌ | In-memory static HashMap — lost on restart, no persistence |

