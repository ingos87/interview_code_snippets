# Code Review: challenge Package

## Overview
This package contains coding challenge exercises demonstrating bad practices in Java and Kotlin, plus lambda and Optional exercises.

---

## BadCode.java — Flaws

| # | Flaw | Details |
|---|------|---------|
| 1 | **OutOfMemoryError** | `new long[Integer.MAX_VALUE][Integer.MAX_VALUE]` attempts to allocate ~9.2 exabytes — instant OOM crash |
| 2 | **SQL Injection** | `someSql()` concatenates user input directly into the query string — trivial to exploit (e.g. `' OR 1=1 --`) |
| 3 | **Resource leak** | `Connection` and `Statement` are never closed — connection pool exhaustion |
| 4 | **Swallowed exception** | `catch (SQLException e)` block is empty — failures are silently ignored |
| 5 | **Returns null** | Returns `null` on error instead of using Optional or throwing — callers will NPE |
| 6 | **Hardcoded connection string** | Database URL embedded in source code — not configurable, potential credential exposure |
| 7 | **No PreparedStatement** | Uses `Statement` instead of `PreparedStatement` — enables the SQL injection |
| 8 | **Secrets in query** | Selects a column called `secret` — suggests sensitive data handling with no access control |

---

## BadCodeKotlin.kt — Flaws

| # | Flaw | Details |
|---|------|---------|
| 1 | **OutOfMemoryError** | `Array(Int.MAX_VALUE) { LongArray(Int.MAX_VALUE) }` — same OOM issue as Java version |
| 2 | **SQL Injection via string template** | `"... username = '$username' ..."` — Kotlin string interpolation doesn't prevent injection |
| 3 | **Resource leak** | Connection and Statement never closed — no `use {}` block |
| 4 | **Swallowed exception** | Empty `catch (e: SQLException)` block |
| 5 | **Returns null** | Returns `null` instead of idiomatic Kotlin error handling |
| 6 | **Misleading import** | `import java.sql.DriverManager.println` — imports `println` from DriverManager, confusing |

---

## Lambdas.java — Flaws

| # | Flaw | Details |
|---|------|---------|
| 1 | **No lambdas used** | `simpleLambda()` uses an imperative for-loop despite the class name suggesting lambda usage |
| 2 | **String concatenation in loop** | `result += ", " + ...` creates a new String object each iteration — O(n²) |
| 3 | **Leading comma** | Result starts with `", DHL, UPS..."` — formatting bug |
| 4 | **Hardcoded return value** | `lambdas()` returns `32` with a comment — the actual stream/lambda implementation is missing |
| 5 | **Incomplete exercise** | `lambdas()` method body is unimplemented — no actual logic |

---

## Optionals.java — Flaws

| # | Flaw | Details |
|---|------|---------|
| 1 | **Unconditional `.get()`** | `optional.get()` called without checking — bad practice even when known present (use `orElseThrow()`) |
| 2 | **Side effects only** | `simple()` uses `System.out.println` — no testable return values |
| 3 | **Incomplete implementation** | `check()` method body is empty — exercise left unfinished |
| 4 | **Optional as parameter** | `check(Optional<String>)` takes Optional as a method parameter — anti-pattern per Java best practices |

