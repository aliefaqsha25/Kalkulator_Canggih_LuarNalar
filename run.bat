@echo off
REM Kompilasi lalu jalankan. Pemakaian: run.bat [cli|gui]
if not exist out mkdir out
javac -encoding UTF-8 -d out src\main\java\com\kalkulator\*.java src\main\java\com\kalkulator\core\*.java src\main\java\com\kalkulator\ui\*.java src\main\java\com\kalkulator\cli\*.java src\main\java\com\kalkulator\gui\*.java
if errorlevel 1 exit /b 1
java -cp out com.kalkulator.Main %*
