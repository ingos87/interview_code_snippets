# Interview Guide - International Address Validation

## Quick Start for Interviewer

### Before the Interview
1. Ensure candidate has read the `user_story.md` (acceptance criteria)
2. Give them 5 minutes to review the code structure
3. Tell them: "Find the bugs and fix them. I'll help you discuss each one."

### Interview Timeline (30 minutes)

#### Phase 1: Code Review (0-8 min)
**Ask**: "What do you notice about the code structure?"
- They should find easy bugs: missing getters, null checks, untyped request handler
- Start with `AddressValidationResult.java` and `CountryRule.java`

#### Phase 2: Running Tests (8-15 min)
**Action**: Run `./gradlew test --tests "*InternationalAddressValidation*"`
- Tests will fail, exposing the flaws
- Ask: "Which test failed and why?"
- Work through each failure together

#### Phase 3: Core Logic Issues (15-25 min)
**Focus on the service validation logic**:
1. Thread safety issue in static HashMap
2. Mutable internal state returned from getRules()
3. Missing validation of required fields
4. Regex recompilation performance issue

#### Phase 4: Architecture & Best Practices (25-30 min)
**High-level discussion**:
- "How would you refactor this for production?"
- Input validation strategy
- Error handling approach
- Jackson serialization issues

---

## Common Flaw Discovery Sequence

### What Junior Devs Usually Find First
1. ✅ Missing getters (REST response returns `{}`)
2. ✅ Null reference errors
3. ✅ Hardcoded magic numbers
4. ✅ Public mutable fields

### What Senior Devs Probe Next
5. 🎯 Static shared state (thread safety)
6. 🎯 Mutable reference return (encapsulation violation)
7. 🎯 String comparison with `==` vs `.equals()`
8. 🎯 Regex compilation overhead

### Tricky Issues (Rarely Spotted Immediately)
- Backwards logic in `addViolation()` (valid = violations.size() > 0)
- Type casting without validation in batch endpoint
- Batch size limit (DoS vector)

---

## Key Discussion Points by Flaw

### 1️⃣ Missing Getters Issue
**Why it fails**: Jackson needs getters for JSON serialization
**Learning**: REST API contracts, serialization requirements
**Fix**: Add `@JsonProperty` or proper getter methods

### 2️⃣ Null Check Without Return
**Why it's wrong**: Continues execution after detecting null
**Learning**: Defensive programming, fail-fast principle
**Fix**: Add return statement or throw exception

### 3️⃣ Static Mutable State
**Why it's dangerous**: Multiple service instances share same HashMap
**Learning**: Thread safety, singleton pattern pitfalls
**Fix**: Use instance variable or inject shared service
**Bonus**: Explain `synchronized` or `ConcurrentHashMap`

### 4️⃣ String Comparison with ==
**Why it breaks**: Different String objects (even same content) won't match
**Learning**: Java object equality vs value equality
**Fix**: Use `.equals()` or `.equalsIgnoreCase()`
**Bonus**: Explain String pool and new String()

### 5️⃣ Untyped HashMap in Controller
**Why it's fragile**: Magic keys, no validation, ClassCastException possible
**Learning**: Type safety, DTO pattern, input validation
**Fix**: Create proper `AddressValidationRequest` DTO class

### 6️⃣ No Batch Size Limit
**Why it's dangerous**: DoS attack vector (send 1M addresses = OOM)
**Learning**: API security, rate limiting, resource constraints
**Fix**: Add @Validated with @Size annotation

---

## Red Flags to Listen For

| Flag | What It Means |
|------|---------------|
| "Null checks prevent crashes" | Doesn't understand fail-fast principle |
| "Static is fine, we only create one" | Doesn't think about concurrency |
| "The code works" | Not thinking about thread safety, serialization |
| "I'd use try-catch everywhere" | Confused about exception handling vs prevention |
| "Let me just add a getter" | Missing the encapsulation concern for getRules() |

---

## What You're Really Assessing

🎯 **Technical Depth**
- Understanding of Java fundamentals (String equality, null safety)
- Knowledge of Spring framework (serialization, autowiring)
- Awareness of concurrency issues

🎯 **Problem-Solving**
- Can they trace the bug to its root cause?
- Do they test after fixing?
- Do they consider side effects of their fix?

🎯 **Communication**
- Can they explain the issue clearly?
- Do they ask clarifying questions?
- Can they discuss trade-offs?

🎯 **Professionalism**
- Do they blame the code or analyze it objectively?
- Can they discuss without getting defensive?
- Do they think about maintainability?

---

## If Candidate Gets Stuck

### Hint Level 1 (Subtle)
"What happens if address is null?"

### Hint Level 2 (Directed)
"Look at the validate() method. After checking for null, does it return?"

### Hint Level 3 (Explicit)
"The getters on AddressValidationResult are missing. Jackson can't serialize fields without them."

---

## Possible Extensions (If Time Allows)

✨ **If they fix everything quickly**:
1. "How would you add support for custom validation rules?"
2. "Design the database schema for storing country rules"
3. "How would you handle validation for new countries at runtime?"
4. "What metrics would you track for this service?"

✨ **If they're stuck on a specific flaw**:
1. Focus deep on that issue
2. Discuss why it matters in production
3. Explore different solutions
4. Talk about when to optimize vs when to keep it simple

---

## Expected Completion Times

| Difficulty | Should Fix By | Notes |
|-----------|---|---|
| Easy flaws | 8-10 min | Missing getters, null checks |
| Medium flaws | 20 min | Logic errors, mutable state |
| Hard flaws | 25+ min | String comparison, type safety |
| All flaws + fixes | 30+ min | Need follow-up interview |

---

## Questions to Ask During Review

**After each fix:**
1. "Why was this a bug?"
2. "How would you test this fix?"
3. "Would this break anything else?"
4. "Is this the best solution, or just the first one?"

**If they implement a feature:**
1. "Is this secure?"
2. "Can this scale?"
3. "How maintainable is this code?"
4. "How would another developer understand this?"

