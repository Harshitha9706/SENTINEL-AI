@echo off
echo Starting SENTINEL AI Microservices Platform...
start "SENTINEL AI Decision Service (:8081)" cmd /k "%~dp0run-ai-service.bat"
timeout /t 3 /nobreak >nul
start "SENTINEL Incident Service (:8080)" cmd /k "%~dp0run-incident-service.bat"
echo Services starting in dedicated windows.
echo Web UI will be accessible at: http://localhost:8080
