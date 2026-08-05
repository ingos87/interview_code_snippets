# Code Review: machine_readability Package

## Overview
A Spring REST service that validates postal addresses for machine readability (Deutsche Post standards).

---

## Flaws Summary

### Critical Bugs

| # | Flaw | File | Details |
|---|------|------|---------|
| 1 | NPE after null check | `AddressValidationService` | Checks `if (address == null)` and adds error, but does **not** return — continues to call `address.getStreet()` on line 18, causing `NullPointerException` |
| 2 | NPE on null fields | `AddressValidationService` | No null checks on individual fields — `getStreet()`, `getPostalCode()`, `getCity()`, `getCountry()` could all be null |

### Design & Architecture Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 3 | No getters on result DTO | `ValidationResult` | `valid` and `errors` are private with no getters — Jackson cannot serialize the response, REST endpoint returns empty JSON `{}` |
| 4 | No setters on Address | `Address` | Only getters — deserialization via `@RequestBody` will fail (no default constructor either, Jackson needs either setters or `@JsonCreator`) |
| 5 | Error message references package name | `AddressValidationService` | `"machine_readability.Address must not be null"` — internal package name leaked to API consumers |

### Poor Practices

| # | Flaw | File | Details |
|---|------|------|---------|
| 6 | Hardcoded validation rules | `AddressValidationService` | Max street length (50), postal code pattern (`\\d{5}`), supported countries all hardcoded — not configurable |
| 7 | Magic numbers | `AddressValidationService` | `50` for street length with no named constant or explanation |
| 8 | Regex compiled on every call | `AddressValidationService` | `address.getPostalCode().matches("\\d{5}")` recompiles the regex pattern on each invocation |
| 9 | Inconsistent country handling | `AddressValidationService` | Accepts both "DE" and "GERMANY" — mixing ISO codes and full names |

### Testing Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 10 | No assertion on validity | `AddressValidationServiceTest` | Only asserts `result != null` — never checks if the address is actually valid or what errors are returned |
| 11 | Single test case | `AddressValidationServiceTest` | Only one happy-path test — no edge cases, no invalid addresses, no null input test |
| 12 | Uses `assert` keyword | `AddressValidationServiceTest` | Uses `assert result != null` instead of JUnit's `assertNotNull()` — may be disabled at runtime |
| 13 | No test for null address | `AddressValidationServiceTest` | The critical NPE bug (flaw #1) is never exercised |

