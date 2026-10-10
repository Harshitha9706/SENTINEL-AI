@echo off
setlocal
cd /d "%~dp0ai-decision-service"

REM Configure JDK and Maven paths if bundled in tools
if exist "%~dp0tools\apache-maven-3.9.9\bin\mvn.cmd" (
    set "PATH=%~dp0tools\apache-maven-3.9.9\bin;%PATH%"
)
for /d %%D in ("%~dp0tools\jdk*" "%~dp0tools\amazon-corretto*") do (
    if exist "%%D\bin\java.exe" (
        set "JAVA_HOME=%%D"
        set "PATH=%%D\bin;%PATH%"
    )
)

echo ===================================================
echo Starting SENTINEL AI - AI Decision Service (:8081)
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%

if exist "%~dp0ai-decision-service\target\ai-decision-service-1.0.0.jar" (
    java -jar "%~dp0ai-decision-service\target\ai-decision-service-1.0.0.jar"
) else (
    mvn spring-boot:run
)
pause
