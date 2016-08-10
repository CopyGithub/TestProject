@echo off
rem 开启变量延迟
SETLOCAL ENABLEDELAYEDEXPANSION
rem 定义环境变量
set localPath=%~dp0
rem 清理上次build的残留文件
rm -r -f bin
rm -f build.xml
rm -f %localPath%UIAutomation.jar
rem 获取可用的android sdk
for /f %%i in ('grep -i 'target' %localPath%project.properties') do set targetVersion=%%i
set needSDK=%targetVersion:~7%
ls %ANDROID_Home%\platforms\ > sdk.txt
for /f %%i in ('grep -i 'android' sdk.txt') do (
set currentSDK=%%i
if %needSDK%==!currentSDK! (set targetSDK=!needSDK!)
)
if !needSDK!==!targetSDK! (
    echo off
    ) else (
    set targetSDK=!currentSDK!
    )
rem 获取build.xml文件
call android create uitest-project -n UIAutomation -t !targetSDK! -p %localPath%.
rem 编译jar包
call ant build
rm -f sdk.txt
pause