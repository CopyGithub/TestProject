@echo off
rem 开启变量延迟
SETLOCAL ENABLEDELAYEDEXPANSION
REM 设置环境变量
set workspace=%~dp0
set bvtCase=%workspace%bvt
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
REM 循环编译apk
cd /d %bvtCase%
mkdir %bvtCase%\target
for /d %%i in (BVT_*) do (
  rem 获取工程名
  set projectName=%%i
  echo "start compile '!projectName!'==================================="
  cd /d %bvtCase%/!projectName!
  call android update project -n !projectName! -t android-22 -p ./ -s
  call ant -silent debug
  copy bin\!projectName!-debug.apk %bvtCase%\target\!projectName!.apk
)
pause