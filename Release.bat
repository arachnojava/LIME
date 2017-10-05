@echo off
md Release
md Release\images
copy bin\*.jar Release
copy images\*.* Release\images
