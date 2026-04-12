@echo off
:: ── Claspr — Reset Database ────────────────────────────
:: Drops and recreates the database, clears uploaded photos

echo.
echo  ========================================
echo   Claspr — Database Reset
echo  ========================================
echo.
echo  This will DELETE all data (users, matches, messages, photos).
echo.

set /p CONFIRM="Are you sure? (y/N): "
if /i not "%CONFIRM%"=="y" (
    echo  Cancelled.
    pause
    exit /b 0
)

echo.
echo  Resetting database...
psql -U postgres -c "DROP DATABASE IF EXISTS datingdb;" 2>nul
psql -U postgres -c "CREATE DATABASE datingdb OWNER dating;" 2>nul

echo  Clearing uploaded photos...
if exist "%~dp0backend\uploads" (
    rmdir /s /q "%~dp0backend\uploads" 2>nul
)

echo.
echo  [OK] Database reset complete.
echo  Restart the backend to re-seed demo data.
echo  Clear browser: localStorage.clear() in console
echo  Login: sophie@demo.com / password123
echo.
pause
