# Code Review: address_area_layout Package

## Overview
A Spring REST service that accepts a PDF file upload, renders it to an image, and checks whether an address block exists in the expected area.

---

## Flaws Summary

### Critical Bugs

| # | Flaw | File | Details |
|---|------|------|---------|
| 1 | Resource leak on error | `AddressAreaValidationService` | If `renderer.renderImage()` or `checkAddressBlock()` throws, `document.close()` is never called — PDF document leaks |
| 2 | Wrong pixel comparison | `AddressAreaValidationService` | `pixel == 0` checks for fully transparent black (ARGB `0x00000000`), not visible black text (`0xFF000000`) — logic is incorrect |
| 3 | IOException propagated to client | `AddressAreaValidationController` | `throws IOException` on the endpoint — results in a raw 500 error with stack trace exposed to client |

### Design & Architecture Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 4 | Naïve validation logic | `AddressAreaValidationService` | Returns `true` if *any single pixel* is "black" in the left third — any mark/artifact passes validation |
| 5 | Hardcoded DPI value | `AddressAreaValidationService` | `renderer.renderImage(0, 300)` — 300 DPI hardcoded with no explanation or configurability |
| 6 | Only first page checked | `AddressAreaValidationService` | `renderImage(0, ...)` only renders page 0 — multi-page documents with address on other pages will fail |
| 7 | Address region assumption | `AddressAreaValidationService` | Assumes address block is in the left third of the page — Deutsche Post standard actually specifies exact mm coordinates |
| 8 | No file type validation | `AddressAreaValidationController` | Accepts any uploaded file as "PDF" — no content-type check, no magic bytes verification |
| 9 | No file size limit | `AddressAreaValidationController` | No restriction on upload size — a large file could exhaust memory |
| 10 | Single generic error message | `AddressAreaValidationService` | Only returns "Address block invalid" — no details on what's wrong or where the address should be |

### Poor Practices

| # | Flaw | File | Details |
|---|------|------|---------|
| 11 | No try-with-resources | `AddressAreaValidationService` | `PDDocument` should be in a try-with-resources block to guarantee cleanup |
| 12 | InputStream created unnecessarily | `AddressAreaValidationService` | `PDDocument.load()` can accept `byte[]` directly — the `ByteArrayInputStream` wrapper is unnecessary |
| 13 | Inefficient pixel scan | `AddressAreaValidationService` | Scans pixel-by-pixel in a nested loop — returns on the very first "match", making the validation trivially easy to pass |
| 14 | No logging | `AddressAreaValidationService` | No logging of validation attempts, failures, or document properties |

### Testing Issues

| # | Flaw | File | Details |
|---|------|------|---------|
| 15 | No tests at all | — | No test file exists for this package |

