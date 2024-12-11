@echo off
setlocal enabledelayedexpansion

echo Yo fam, welcome to Pahan's Ticketing System setup no cap fr fr!
echo ================================================================
echo.

REM Create directories
set "INSTALL_DIR=%USERPROFILE%\TicketingSystem"
set "TOOLS_DIR=%INSTALL_DIR%\tools"
mkdir "%INSTALL_DIR%" 2>nul
mkdir "%TOOLS_DIR%" 2>nul

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Ayo we ain't got Java 17 installed yet sheeeesh!
    echo Don't trip tho, your boy Pahan's bout to bless you with that download fr fr...
    echo.
    
    REM Download AdoptOpenJDK 17
    powershell -Command "& {Invoke-WebRequest -Uri 'https://github.com/adoptium/temurin17-binaries/releases/download/jdk-17.0.9%%2B9/OpenJDK17U-jdk_x64_windows_hotspot_17.0.9_9.msi' -OutFile '%TOOLS_DIR%\java17.msi'}"
    
    echo On god we finna install Java 17 rn no cap!
    msiexec /i "%TOOLS_DIR%\java17.msi" /quiet
    
    REM Set JAVA_HOME
    for /f "tokens=2*" %%a in ('reg query "HKLM\SOFTWARE\JavaSoft\JDK\17" /v JavaHome 2^>nul') do set "JAVA_HOME=%%b"
    setx JAVA_HOME "%JAVA_HOME%" /M
    setx PATH "%PATH%;%JAVA_HOME%\bin" /M
    
    echo YURRR Java 17 installed bussin fr fr!
    echo.
) else (
    echo Java already installed and vibin no cap!
    echo.
)

REM Check if Maven is installed
mvn -v >nul 2>&1
if errorlevel 1 (
    echo Nah fam we need that Maven fr fr!
    echo Dw tho, Pahan's got the sauce incoming...
    echo.
    
    REM Download Maven
    powershell -Command "& {Invoke-WebRequest -Uri 'https://dlcdn.apache.org/maven/maven-3/3.9.5/binaries/apache-maven-3.9.5-bin.zip' -OutFile '%TOOLS_DIR%\maven.zip'}"
    
    echo Bussin out that Maven real quick...
    powershell -Command "& {Expand-Archive -Path '%TOOLS_DIR%\maven.zip' -DestinationPath '%TOOLS_DIR%' -Force}"
    
    REM Set MAVEN_HOME and update PATH
    set "MAVEN_HOME=%TOOLS_DIR%\apache-maven-3.9.5"
    setx MAVEN_HOME "%MAVEN_HOME%" /M
    setx PATH "%PATH%;%MAVEN_HOME%\bin" /M
    
    echo No cap Maven installed and it's straight bussin!
    echo.
) else (
    echo Maven already vibin in the system fr fr!
    echo.
)

REM Copy application files
echo Finna copy them files real quick no cap...
xcopy /E /I /Y ".\*" "%INSTALL_DIR%\app"

REM Create desktop shortcut
echo Ayo lemme bless your desktop with that shortcut rn!
set "SHORTCUT=%USERPROFILE%\Desktop\Ticketing System.lnk"
set "VBS=%TEMP%\CreateShortcut.vbs"

echo Set oWS = WScript.CreateObject("WScript.Shell") > "%VBS%"
echo sLinkFile = "%SHORTCUT%" >> "%VBS%"
echo Set oLink = oWS.CreateShortcut(sLinkFile) >> "%VBS%"
echo oLink.TargetPath = "%INSTALL_DIR%\app\run.bat" >> "%VBS%"
echo oLink.WorkingDirectory = "%INSTALL_DIR%\app" >> "%VBS%"
echo oLink.Description = "Real-Time Event Ticketing System" >> "%VBS%"
echo oLink.Save >> "%VBS%"
cscript //nologo "%VBS%"
del "%VBS%"

echo.
echo SHEEEESH we done here fam!
echo The app's chillin at: %INSTALL_DIR%
echo Dropped that shortcut on your desktop no cap fr fr!
echo.
echo Restart that command prompt real quick to get them new environment variables bussin!
echo.

REM Ask if user wants to run the application
set /p RUNAPP="Ayo you tryna run this fire app rn? (Y/N): "
if /i "%RUNAPP%"=="Y" (
    echo.
    echo Say less fam, we finna start this ting!
    cd "%INSTALL_DIR%\app"
    call run.bat
)

pause 