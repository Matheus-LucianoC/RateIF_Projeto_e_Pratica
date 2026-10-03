@echo off
setlocal
cd /d "%~dp0mobile"
echo ========================================
echo RateIF - Mobile Expo
echo ========================================
if not exist node_modules (
  echo Instalando dependencias do mobile...
  call npm install
  if errorlevel 1 (
    echo Falha ao instalar dependencias.
    pause
    exit /b 1
  )
)
call npm start
endlocal
