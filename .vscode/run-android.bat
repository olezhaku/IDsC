@echo off

set "PACKAGE=com.olezhaku.idsc"
set "ACTIVITY=.MainActivity"
set "APK=app\build\outputs\apk\debug\app-debug.apk"

echo [1/4] Build debug APK
call gradlew.bat assembleDebug
if errorlevel 1 exit /b 1

echo [2/4] Install APK
adb install -r "%APK%"
if errorlevel 1 exit /b 1

echo [3/4] Restart app
adb shell am force-stop %PACKAGE%

echo [4/4] Start app
adb shell am start -n %PACKAGE%/%ACTIVITY%

echo Done.