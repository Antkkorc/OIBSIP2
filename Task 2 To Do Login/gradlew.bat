@echo off
set APP_HOME=%~dp0
java -classpath "%APP_HOME%gradle\wrapper\gradle-wrapper.jar" -Dorg.gradle.appname=gradlew org.gradle.wrapper.GradleWrapperMain %*
