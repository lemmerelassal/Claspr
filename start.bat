@echo off
setlocal

:: ── Claspr Dating App - Windows Startup ────────────────
:: Double-click to launch everything

set ROOT=%~dp0
set DB_USER=dating
set DB_PASSWORD=dating123
set DB_NAME=datingdb
set PGPASSWORD=postgres

echo.
echo  ========================================
echo   Claspr - Dating App Launcher
echo  ========================================
echo.

:: ── Check Java ─────────────────────────────────────────
echo [1/5] Checking prerequisites...
where java >nul 2>&1
if %errorlevel% neq 0 (
    echo   [X] Java not found. Install JDK 21 from https://adoptium.net
    goto :fail
)
echo   [OK] Java found

:: ── Check Node ─────────────────────────────────────────
where node >nul 2>&1
if %errorlevel% neq 0 (
    echo   [X] Node.js not found. Install from https://nodejs.org
    goto :fail
)
echo   [OK] Node.js found

:: ── Check PostgreSQL ───────────────────────────────────
where psql >nul 2>&1
if %errorlevel% neq 0 (
    echo   [X] PostgreSQL not found in PATH.
    echo   Add your PostgreSQL bin folder to PATH, e.g.:
    echo   set PATH=%%PATH%%;C:\Program Files\PostgreSQL\18\bin
    goto :fail
)
echo   [OK] PostgreSQL found

:: ── Setup database ─────────────────────────────────────
echo.
echo [2/5] Setting up database...

pg_isready >nul 2>&1
if %errorlevel% neq 0 (
    echo   PostgreSQL not running. Trying to start...
    net start postgresql-x64-18 >nul 2>&1
    timeout /t 3 /nobreak >nul
)

:: Create user (ignore error if exists)
psql -U postgres -c "CREATE USER %DB_USER% WITH PASSWORD '%DB_PASSWORD%';" >nul 2>&1

:: Create database (ignore error if exists)
psql -U postgres -c "CREATE DATABASE %DB_NAME% OWNER %DB_USER%;" >nul 2>&1

echo   [OK] Database ready

:: ── Check JWT keys ─────────────────────────────────────
echo.
echo [3/5] Checking JWT keys...

if exist "%ROOT%backend\src\main\resources\privateKey.pem" goto :keys_ok

where openssl >nul 2>&1
if %errorlevel% neq 0 goto :keys_manual

openssl genrsa -out "%ROOT%backend\src\main\resources\privateKey.pem" 2048 2>nul
openssl rsa -in "%ROOT%backend\src\main\resources\privateKey.pem" -pubout -out "%ROOT%backend\src\main\resources\publicKey.pem" 2>nul
echo   [OK] JWT keys generated
goto :keys_done

:keys_manual
echo   [!] OpenSSL not found. Generate JWT keys manually:
echo       cd backend\src\main\resources
echo       openssl genrsa -out privateKey.pem 2048
echo       openssl rsa -in privateKey.pem -pubout -out publicKey.pem
goto :keys_done

:keys_ok
echo   [OK] JWT keys exist

:keys_done

:: ── Install frontend deps ──────────────────────────────
echo.
echo [4/5] Installing frontend dependencies...

if exist "%ROOT%frontend\node_modules" goto :npm_ok

echo   Running npm install (first time only, may take a minute)...
cd /d "%ROOT%frontend"
call npm install
cd /d "%ROOT%"
goto :npm_done

:npm_ok
echo   [OK] node_modules exists

:npm_done

:: ── Start services ─────────────────────────────────────
echo.
echo [5/5] Starting services...
echo.
echo   Backend:  http://localhost:8080
echo   Frontend: http://localhost:4200
echo.
echo   Demo login: sophie@demo.com / password123
echo.
echo  ========================================
echo   Press Ctrl+C in any window to stop
echo  ========================================
echo.

:: Start backend in new window
start "Claspr Backend" cmd /k "cd /d "%ROOT%backend" && .\gradlew.bat quarkusDev"

:: Wait for backend
echo   Waiting for backend to start...
timeout /t 12 /nobreak >nul

:: Start frontend in new window
start "Claspr Frontend" cmd /k "cd /d "%ROOT%frontend" && npx ng serve --open"

echo.
echo   Both services starting. Browser will open automatically.
echo.
pause
exit /b 0

:fail
echo.
pause
exit /b 1
