# Code Review: configurable_rules Package

## User Story
> As a system administrator I want postal validation rules to be configurable so that changes in Deutsche Post regulations can be applied without modifying application code.

---

## Flaws Summary

### Critical Bugs

| # | Flaw | File | Details |
|---|------|------|---------|
| 1 | NPE after null check | `PostalValidationService` | Checks `if (rules == null)` and adds error, but continues to iterate `rules` unconditionally on the next line |
| 2 | NPE on missing request params | `PostalValidationService` | No null checks on `postalCode`, `street`, `city`, `recipient` — any missing key in the HashMap causes NPE |
| 3 | NumberFormatException | `PostalValidationService` | `Integer.parseInt(postalCode)` in `POSTAL_CODE_RANGE` rule with no guard for non-numeric input |
| 4 | Resource leak | `RuleConfigurationService` | `BufferedReader` is never closed in `loadRules()` |

### Design & Architecture Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 5 | Static mutable state | `RuleConfigurationService` | `static List<PostalRule>` shared across all instances — not thread-safe |
| 6 | Hardcoded file path | `RuleConfigurationService` | `/etc/postal-rules/rules.csv` — not configurable, not portable |
| 7 | GET with side effects | `PostalRuleController` | `/rules/reload` endpoint mutates state via GET — violates HTTP semantics |
| 8 | Untyped request body | `PostalRuleController` | Uses `HashMap<String, String>` instead of a proper DTO |
| 9 | No access control | `PostalRuleController` | Reload/admin endpoints exposed without authentication |

### Poor Practices

| # | Flaw | File | Details |
|---|------|------|---------|
| 10 | Swallowed exceptions | `RuleConfigurationService` | Catches generic `Exception`, prints to stdout — no logging framework |
| 11 | Only one previous version | `RuleConfigurationService` | `previousRules` overwritten each reload — no real version history/traceability |
| 12 | Version from first rule only | `PostalValidationService` | `appliedRuleVersion` takes version from `rules.get(0)` — fragile assumption |
| 13 | Package-private fields, no getters | `RuleValidationResult` | DTO fields not accessible for serialization or external use |
| 14 | No thread safety | Multiple | Concurrent reload and read can cause `ConcurrentModificationException` |

### Testing Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 15 | Test depends on missing file | `PostalValidationServiceTest` | `RuleConfigurationService` won't find `/etc/postal-rules/rules.csv` — test passes vacuously (no rules = no violations = valid) |

---

## Acceptance Criteria vs. Implementation

| Criterion | Met? | Issue |
|-----------|------|-------|
| Rules can be modified without recompiling | Partially | Reads from CSV, but path is hardcoded and error handling is broken |
| Rule changes become effective after deployment | Partially | Reload endpoint exists but uses GET, no atomicity, no health check |
| Previous rule versions remain traceable | ❌ | Only one previous version stored in memory, lost on restart |

