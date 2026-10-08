@echo off
rem 最小可用的 Maven Wrapper 启动脚本（Windows）
setlocal
set "BASEDIR=%~dp0"
if "%JAVA_HOME%"=="" (
  set "JAVACMD=java"
) else (
  set "JAVACMD=%JAVA_HOME%\bin\java.exe"
)
"%JAVACMD%" -Dmaven.multiModuleProjectDirectory="%BASEDIR:~0,-1%" -cp "%BASEDIR%.mvn\wrapper\maven-wrapper.jar" org.apache.maven.wrapper.MavenWrapperMain %*
endlocal