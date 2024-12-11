@echo off
echo Ayyooo fam, Pahan's Ticketing System boutta be lit no cap fr fr...
echo ================================================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if errorlevel 1 (
    echo Nahh fam you trippin - where's Java at? 
    echo My g you need Java 17 or later to run this fire app no cap.
    echo Run setup.bat rn if you ain't done it yet, I gotchu fam!
    pause
    exit /b 1
)

REM Check if Maven is installed
mvn -v >nul 2>&1
if errorlevel 1 (
    echo Sheeeesh Maven ain't here! 
    echo Ayo you need Maven 3.6 or later to make this ting work innit.
    echo Run setup.bat if you ain't already, your boy Pahan's got your back fr fr!
    pause
    exit /b 1
)

REM Build and run the application
echo On god we finna build this real quick no cap...
call mvn clean compile
if errorlevel 1 (
    echo Nahhh fam that's an L - build's acting mad sus rn.
    echo DM your boy if you need help fixing this ting!
    pause
    exit /b 1
)

echo.
echo YURRR we lit! System's boutta go crazy...
call mvn javafx:run
if errorlevel 1 (
    echo Deadass? System said nah fr fr. 
    echo But we move tho - run setup.bat again or hit up your boy Pahan!
    pause
    exit /b 1
)

echo.
echo Aight bet, we done here! Press any key to dip...
pause 