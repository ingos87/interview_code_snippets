# International Address Validation - Interview Code Submission

## Overview

I've created a complete implementation of the International Address Validation service based on the user story. The code is written at a junior developer level with **15 intentional flaws** ranging from easy to hard difficulty, designed for a 30-minute senior developer interview.

## Project Structure

```
international_address_validation/
├── Address.java                               (Data model - 27 lines)
├── AddressValidationResult.java              (Result DTO - 39 lines)
├── CountryRule.java                          (Rule configuration - 25 lines)
├── InternationalAddressValidationService.java (Business logic - 108 lines)
├── InternationalAddressValidationController.java (REST endpoints - 60 lines)
└── FLAWS.md                                  (Detailed flaw documentation - 150+ lines)

Tests:
├── InternationalAddressValidationServiceTest.java (11 test cases)
└── AddressValidationResultTest.java               (4 test cases)
```

## Key Features

✅ **Multi-country support** with hardcoded rules for US, CA, DE, GB
✅ **Postal code validation** with format and length checking
✅ **Address field validation** (street, city validation)
✅ **REST API endpoints** for single and batch validation
✅ **Comprehensive test suite** (15 test cases exposing various flaws)

## Flaws by Difficulty (Organized by Interview Timeline)

### 🟢 EASY (Find in first 5 minutes)
1. Missing null check before accessing `address.getPostalCode()`
2. Missing getter methods on result fields (Jackson serialization issue)
3. Public mutable fields in `CountryRule` (no encapsulation)
4. Null check that doesn't return/throw (continues execution after detecting null)
5. Untyped HashMap in controller instead of proper DTO
6. Hardcoded magic values (`50` character limit)

### 🟡 MEDIUM (Find in next 10 minutes)
7. Broken boolean logic in `addViolation()` (backwards validity check)
8. NPE on null postal code fields
9. Static mutable shared state (thread safety issue)
10. Returns mutable internal state directly from getter
11. Missing validation of required fields based on country rules
12. No size limit on batch endpoint (DoS vulnerability)

### 🔴 HARD (Find in last 15 minutes)
13. String comparison using `==` instead of `.equals()` in `getViolationCount()`
14. Regex recompiled on every invocation instead of cached
15. Unsafe type casting without validation

## Interview Scenario

**Setup**: "Here's a basic international address validation service. It needs some fixes before we can use it in production. Walk me through the code and identify what's broken."

**Expected flow**:
1. Junior dev reads through code
2. Finds easy bugs first (null checks, missing getters)
3. As they run tests, more bugs become apparent
4. Discussion on why each flaw exists and how to fix it
5. Conversation naturally leads to architectural improvements

**Talking points during interview**:
- Input validation and null safety
- Thread safety and mutable shared state
- Java best practices (equals vs ==, string comparisons)
- Jackson serialization requirements
- REST API security (batch size limits, DoS prevention)
- Encapsulation and information hiding
- Type safety vs dynamic typing

## How to Use

1. **Run the tests** to see which ones fail:
   ```bash
   ./gradlew test --tests "*InternationalAddressValidation*"
   ```

2. **Ask the candidate to fix the flaws** by identifying and correcting them

3. **Provide hints if needed** by referring to the FLAWS.md document

4. **Discuss fixes** as they implement them

## Business Logic (Already Known)

The candidate should already understand:
- Address validation requirements for different countries
- Postal code format/length rules per country
- Why string validation matters for international shipping
- Basic Spring Boot REST controller patterns

## Quality Assessment Criteria

When reviewing candidate's fixes, look for:
- ✅ Understanding of null safety and defensive programming
- ✅ Knowledge of Java string comparison (equals vs ==)
- ✅ Awareness of thread safety issues
- ✅ Jackson serialization knowledge
- ✅ REST API security considerations
- ✅ Code clarity and maintainability
- ✅ Test-driven approach to validation

## Files Delivered

- **5 Java source files** in `src/main/java/international_address_validation/`
- **2 Java test files** in `src/test/java/international_address_validation/`
- **1 FLAWS.md document** with detailed flaw explanations
- **15+ identified flaws** with discussion points
- **Acceptance criteria tracking** showing what's partially/fully implemented

