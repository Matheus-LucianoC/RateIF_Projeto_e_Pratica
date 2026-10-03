@echo off
setlocal
cd /d "%~dp0"
echo ========================================
echo RateIF - Backend Spring Boot
echo ========================================
echo.
java -version >nul 2>&1
if errorlevel 1 (
  echo Java nao foi encontrado no PATH.
  echo Instale Java 17 ou superior e tente novamente.
  pause
  exit /b 1
)
call mvnw.cmd clean spring-boot:run
if errorlevel 1 (
  echo.
  echo O backend foi encerrado com erro.
  pause
)
endlocal
