@echo off
call makejar.bat
cd ..

md Release
rem md Release\images

copy bin\*.jar Release
rem copy images\*.* Release\images

