@rem ##########################################################################
@rem  Gradle startup script for Windows (self-bootstrapping)
@rem ##########################################################################
@if "%DEBUG%"=="" @echo off
setlocal

set DIRNAME=%~dp0
set APP_HOME=%DIRNAME%

set WRAPPER_JAR=%APP_HOME%gradle\wrapper\gradle-wrapper.jar
set WRAPPER_URL=https://services.gradle.org/distributions/gradle-8.5-bin.zip
set WRAPPER_PROPS=%APP_HOME%gradle\wrapper\gradle-wrapper.properties

if not exist "%APP_HOME%gradle\wrapper" mkdir "%APP_HOME%gradle\wrapper"

@rem Download gradle-wrapper.jar if missing
if not exist "%WRAPPER_JAR%" (
    echo Downloading Gradle wrapper...
    powershell -Command "& { [Net.ServicePointManager]::SecurityProtocol = [Net.SecurityProtocolType]::Tls12; Invoke-WebRequest -Uri 'https://raw.githubusercontent.com/gradle/gradle/v8.5.0/gradle/wrapper/gradle-wrapper.jar' -OutFile '%WRAPPER_JAR%' }" 2>nul
    if not exist "%WRAPPER_JAR%" (
        echo.
        echo ERROR: Could not download gradle-wrapper.jar automatically.
        echo Please install Gradle 8.5 from https://gradle.org/install/
        echo Then run: gradle wrapper --gradle-version 8.5
        echo.
        exit /b 1
    )
    echo Gradle wrapper downloaded successfully.
)

set JAVA_EXE=java.exe
%JAVA_EXE% -classpath "%WRAPPER_JAR%" org.gradle.wrapper.GradleWrapperMain %*

endlocal
