@echo off

echo Calling MHFramework.bat...
call MHFramework.bat

rem copy ..\bin\*.class .
copy .\Scripts\mainClass.txt .\bin

cd bin

c:\"Program Files (x86)"\Java\jdk1.6.0_22\bin\jar cvfm LIME.jar mainClass.txt *.class mhframework
pause
