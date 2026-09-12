# International Address Validation — Flaws & Discussion Points

## User Story

> As an international mailing customer, I want addresses to be validated according to the destination country's conventions so that international shipments are processed correctly.

---

## Acceptance Criteria

- The system supports multiple countries.
- Validation rules depend on the destination country.
- Unsupported countries are reported.

---

## Flaws by Difficulty Level

### EASY FLAWS (Should find in first 5 minutes)

| # | Flaw | File | Location | Description |
|---|------|------|----------|-------------|
| 1 | **Missing null checks before use** | `AddressValidationResult` | `timestamp` initialization | `new Date().toString()` produces non-ISO format, non-parseable output |
| 2 | **Missing field getters** | `AddressValidationResult` | All fields | `valid`, `violations`, `timestamp` are private with no getters. Jackson cannot serialize these fields. REST endpoint returns empty JSON `{}` |
| 3 | **Public mutable fields** | `CountryRule` | All fields | No encapsulation. Any caller can modify `countryCode`, `countryName`, `postalCodePattern` after creation, corrupting validation logic for all future validations |
| 4 | **Null checks that don't return** | `InternationalAddressValidationService` | `validateAddress()` | Checks `if (countryCode == null)` and prints message but does **not** return. Continues to call `rules.get(countryCode)` with null, causing NPE |
| 5 | **Untyped request handling** | `InternationalAddressValidationController` | `validateAddress()` | Uses `HashMap<String, String>` with magic keys. No validation that keys exist. No input validation on extracted values |
| 6 | **Hardcoded values** | `InternationalAddressValidationService` | `getMaxAddressLineLength()` | Returns hardcoded `50` instead of using configurable constant or property |

### MEDIUM FLAWS (Should find in next 10 minutes)

| # | Flaw | File | Location | Description |
|---|------|------|----------|-------------|
| 7 | **Broken boolean logic** | `AddressValidationResult` | `addViolation()` | `valid = valid - (violations.size() > 0 ? 1 : 0) > 0` is nonsensical. Attempts to subtract from boolean. `valid` starts as `true` and becomes wrong after first violation |
| 8 | **NPE after null check** | `InternationalAddressValidationService` | `validatePostalCode()` | No null check before calling `postalCode.length()`. If `address.getPostalCode()` returns null, will throw NPE |
| 9 | **Static mutable shared state** | `InternationalAddressValidationService` | `rules` field | Static HashMap is not thread-safe. Multiple service instances share same map. Race condition if rules are modified concurrently |
| 10 | **Returns mutable internal state** | `InternationalAddressValidationService` | `getRules()` | Returns reference to internal `rules` map directly. Callers can add/remove countries, corrupting validation for all future validations |
| 11 | **Missing validation logic** | `InternationalAddressValidationService` | `validateFields()` | `requiredFields` array is defined in rules but never validated. Should check that street, city, postal are all present and non-empty |
| 12 | **No size limit on batch** | `InternationalAddressValidationController` | `validateBatch()` | Accepts unbounded list of addresses. Attacker can send 1 million addresses causing OOM or CPU exhaustion. No rate limiting |

### HARD FLAWS (Should find in last 15 minutes)

| # | Flaw | File | Location | Description |
|---|------|------|----------|-------------|
| 13 | **String comparison with ==** | `AddressValidationResult` | `getViolationCount()` | Uses `severity == "CRITICAL"` instead of `.equals()`. Strings created with `new String(...)` never match literals via `==`. Method always returns 0 |
| 14 | **Regex recompiled every call** | `InternationalAddressValidationService` | `validatePostalCode()` | `Pattern.matches()` compiles regex on every invocation. Should pre-compile patterns or use cached Pattern objects |
| 15 | **Type casting without validation** | `InternationalAddressValidationController` | `validateBatch()` | Casts `Object` to `List<Map<String, String>>` without checking type. ClassCastException if wrong type sent |

---

## Root Cause Analysis

### Architectural Issues

- **Lack of input validation**: Controller accepts untyped HashMap and casts without checking
- **Mutable shared state**: Static rules map causes thread-safety issues
- **Poor encapsulation**: Public fields allow corruption of validation rules
- **Missing null-safety**: Multiple methods call methods on potentially-null objects

### Common Anti-Patterns

1. **Printing errors instead of throwing/logging**: Silently continues after detecting null input
2. **Broken control flow**: Detects errors but doesn't return/throw
3. **Type unsafety**: HashMap + casting instead of strong typing
4. **Resource inefficiency**: Regex recompiled on every match
5. **Poor serialization support**: Fields missing getters, Jackson can't serialize response

---

## Acceptance Criteria Coverage

| Criterion | Met? | Notes |
|-----------|------|-------|
| System supports multiple countries | ⚠️ Partially | Countries hardcoded; only 4 pre-loaded. Adding new countries requires code changes. |
| Validation rules depend on destination country | ⚠️ Partially | Rules exist but validation logic has bugs. Required fields not validated. |
| Unsupported countries are reported | ⚠️ Partially | Throws RuntimeException but NPE occurs before reaching this if input is null. |
| REST API is usable | ❌ No | Response returns empty JSON `{}` due to missing getters on result fields. |

