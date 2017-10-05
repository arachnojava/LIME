@echo off
call MHFramework.bat


copy ..bin\*.class .
copy ..\mainClass.txt .

c:\Progra~1\Java\jdk1.6.0_12\bin\jar cvfm LIME.jar mainClass.txt *.class mhframework
pause
