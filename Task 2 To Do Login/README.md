# Task 2: To Do App with Login

Register an account, sign in and keep a separate task list for that account. Tasks are stored in SQLite on the device. Passwords are stored as salted PBKDF2 hashes, not as readable text.

## Run it

Open this folder in Android Studio. Wait for Gradle sync, select an emulator or Android phone, then press Run. Use a test email and a password you do not use elsewhere.

## Check it

Register, sign out and sign back in. Try the wrong password. Add a task with notes, mark it complete, reopen it and delete it. Register a second account and check that its list is separate. An empty account should show a message instead of a blank screen.

This is a classroom app. Its login is stored on one device and is not a replacement for a production account service. Removing the app removes its database.

## Android references

[SQLiteOpenHelper](https://developer.android.com/reference/android/database/sqlite/SQLiteOpenHelper) and [Android cryptography guidance](https://developer.android.com/privacy-and-security/cryptography).
