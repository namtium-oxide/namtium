@echo off

rmdir /s /q tmp
if %errorlevel% lss 0 goto end
xcopy original_files tmp\files /s /e /i
if %errorlevel% lss 0 goto end
java -jar libs\gzipr.jar --verbose --delete --extension gz tmp\files
if %errorlevel% lss 0 goto end
java -cp libs Filelist original_files > tmp\filelist
if %errorlevel% lss 0 goto end
java -jar libs\gzipr.jar --verbose --delete --extension gz tmp\filelist
if %errorlevel% lss 0 goto end
rmdir /s /q ..\files
if %errorlevel% lss 0 goto end
xcopy tmp .. /s /e /i /y
if %errorlevel% lss 0 goto end
rmdir /s /q tmp

:end
pause
