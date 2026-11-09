# OIBSIP Android tasks

Five Android Studio projects for the Oasis Infobyte internship work. Each app has its own folder. Open one folder at a time in Android Studio.

## Pick an app

1. [Task 1 Unit Converter](Task%201%20Unit%20Converter/README.md)
2. [Task 2 To Do Login](Task%202%20To%20Do%20Login/README.md)
3. [Task 3 Calculator](Task%203%20Calculator/README.md)
4. [Task 4 Quiz](Task%204%20Quiz/README.md)
5. [Task 5 Stopwatch](Task%205%20Stopwatch/README.md)

## Run one on your phone or emulator

In Android Studio, choose **Open** and select the folder for the app you want. Wait for Gradle sync. Choose an Android phone or emulator, then press **Run**. Android Studio may ask you to install SDK Platform 35. Let it install, then run again.

Each folder is a separate project. You do not need to open the whole repository as one Android app.

## What is in each folder

Task 1 converts length, mass, volume and temperature.

Task 2 has account registration, sign in and a private task list stored in SQLite. Passwords are salted and hashed with PBKDF2.

Task 3 handles the four basic arithmetic operations, decimals, clear, backspace and division by zero.

Task 4 has ten general knowledge questions, shuffled order, answer feedback and a score screen.

Task 5 has start, pause, reset and lap controls. It uses Android's monotonic clock and keeps its time when the app is briefly backgrounded.

## Before you hand it in

Run each app in Android Studio and check the assignment list in that app's README. Add your name to the submission where the portal asks for it. The assignment file gives two different due dates, 15 November 2026 and 15 December 2026. Check with the organizer which date is correct.

The browser preview is at [index.html](index.html). It is only a visual demo. The Android projects are the actual assignment apps.
