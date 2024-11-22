@echo off

rem Remove compiled class files
echo Cleaning up previous .class files...
del /Q *.class
if %ERRORLEVEL% neq 0 (
    echo No .class files to delete.
)

rem Compile all Java files with the SQLite JDBC driver in the classpath
echo Compiling Java files...
javac -cp .;sqlite-jdbc-3.47.0.0.jar *.java
if %ERRORLEVEL% neq 0 (
    echo Compilation failed. Exiting.
    exit /b 1
)

rem Run Perform class
echo Running Perform ...
java Perform
if %ERRORLEVEL% neq 0 (
    echo Perform execution failed.
)
echo --------------
timeout /t 1 >nul

rem Run PerformReadWrite class
echo Running Perform Read ^& Write ...
java PerformReadWrite
if %ERRORLEVEL% neq 0 (
    echo PerformReadWrite execution failed.
)
echo --------------
timeout /t 1 >nul

rem Run PerformJDBC class with SQLite JDBC driver
echo Running Perform SQLite JDBC ...
java -cp .;sqlite-jdbc-3.47.0.0.jar PerformJDBC
if %ERRORLEVEL% neq 0 (
    echo PerformJDBC execution failed.
)

echo Script execution completed.
pause

