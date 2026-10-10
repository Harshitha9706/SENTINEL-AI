@echo off
setlocal
cd /d "%~dp0incident-service"

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
echo Starting SENTINEL AI - Incident Management Service (:8080)
echo ===================================================
echo JAVA_HOME: %JAVA_HOME%

if exist "%~dp0incident-service\target\incident-service-1.0.0.jar" (
    java -jar "%~dp0incident-service\target\incident-service-1.0.0.jar" --spring.profiles.active=local
) else (
    mvn spring-boot:run -Dspring-boot.run.profiles=local
)
pause
