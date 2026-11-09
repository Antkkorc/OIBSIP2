# OIBSIP2 — Fieldnotes Android Kit

A native Android Studio project containing five independent assignment tools: **Unit Converter**, **To-do with account login**, **Calculator**, **General Knowledge Quiz**, and **Stopwatch with laps**. The brief requires at least three; this repository aims to implement all five. Built with Java and XML views, using Android's built-in SQLite APIs and no internet permission.

## Design thesis

- **User/context:** an Android-development intern presenting five small, graded utilities in one clearly documented portfolio repo.
- **One job:** make each assignment criterion easy to find, use, and verify without disguising the work as a generic productivity app.
- **Mood:** instrumented, editorial, quietly confident.
- **References:** Braun calculator interfaces (function-first legibility) and Things 3 (calm list hierarchy and useful empty states).

**Structure:** a field kit rather than a single-purpose app: a compact studio index routes into five separate tools. **Signature details:** numbered task markers; warm paper surfaces, ink text, and persimmon accent; plainspoken feedback/status copy. **Type:** humanist sans UI with restrained serif display headings. **Accent:** persimmon `#D65B3F`.

The source brief contradicted itself: it both bans and requests liquid glass, pills, gradients, and Inter/Poppins. This project deliberately follows the more specific anti-default studio brief: no glass, purple/indigo, blob backgrounds, emoji icons, uniform cards, or generic productivity slogans. Motion is limited to modest state feedback; reduced-motion preferences are respected where practical.

## Apps and behavior

1. **Unit Converter:** length, mass, volume, and temperature categories; validated numeric input; source/target selection; swap; precise conversions and clear results.
2. **To-do + login:** local account registration/login; salted PBKDF2-HMAC-SHA256 password hashes (never plaintext or MD5); per-account SQLite tasks; add notes, complete, delete, empty state, and logout.
3. **Calculator:** decimal arithmetic, operator precedence, clear/backspace, malformed-input and divide-by-zero handling.
4. **Quiz:** 10 locally bundled questions, randomized order, immediate answer feedback, score, and restart.
5. **Stopwatch:** monotonic elapsed-time tracking, start/pause/reset/laps; state is retained across activity pause/resume while the process lives.

## Open in Android Studio

1. Clone or download this repository and open the **repository root** (`OIBSIP2`) in Android Studio.
2. Install Android SDK Platform 35 and a JDK 17-compatible Android Studio/Gradle environment if prompted; allow Gradle sync to finish.
3. Run the `app` configuration on an emulator or Android device (API 23+).
4. Select one of the five tools from the home screen. The to-do demo allows registering a local account; use a test email and a unique password.

No API keys or internet access are required. App database contents are local to the installation; uninstalling the app removes them. Do not reuse a real password for a classroom demo. PBKDF2 is educational local-app protection, not a substitute for a production authentication service, hardware-backed key management, or server-side security review.

## Suggested verification checklist

- Converter: empty input; invalid text; negative value; same-unit conversion; Celsius ↔ Fahrenheit; very large/small values; switch category and confirm units update.
- To-do: register/login/logout; try wrong password; verify hashes in SQLite are not the password; add blank/long task and notes; complete/reopen; delete; log in as a second account and confirm tasks are isolated; empty account.
- Calculator: `2 + 3 × 4`, decimals, repeated operators, leading decimal, backspace, clear, `9 ÷ 0`, rapid taps.
- Quiz: answer correct and incorrect choices; verify feedback, score totals, final result, and restart; check randomized order.
- Stopwatch: start/pause/resume/reset, laps, rotate/pause/resume, and confirm time does not jump when the activity is briefly backgrounded.

## Submission notes

The attachment lists two conflicting deadlines (15 November 2026 and 15 December 2026). Confirm the authoritative deadline and submission form with the internship organizer. The stated filename format is `YourName_TaskNumber`; add your own name only where required by the submission portal. Keep the public repository link in your submission and include a short screen-recorded demo if requested. Do not claim certification or evaluation before the organizer confirms it.

## Build status

See [`BUILD_STATUS.md`](BUILD_STATUS.md) for what was compiled and tested. This is a learning assignment; run the verification checklist on a real emulator/device before submission.
