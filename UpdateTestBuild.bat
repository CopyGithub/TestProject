@echo off
REM 环境变量设置
set workspace=%~dp0
set projectName=UpdateTest
set projectDir=%workspace%%projectName%
REM 依赖包
set androidUtils=%workspace%AndroidUtils
set testutil=%workspace%testutil
set DolphinBaseTest=%workspace%bvt\DolphinBaseTest
REM 清理工作空间
cd /d %workspace%
git clean -dxf
REM 更新依赖包build.xml文件
cd /d %testutil%
call android update lib-project -p ./
cd /d %androidUtils%
call android update lib-project -p ./
cd /d %DolphinBaseTest%
call android update lib-project -p ./
REM 编译apk
cd /d %projectDir%
call android update project -n %projectName% -t android-22 -p ./ -s
call ant -silent debug
pause