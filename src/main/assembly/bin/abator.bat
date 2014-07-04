@echo off

set path=.;%JAVA_HOME%\bin
set cp=
for %%i in ("..\libs\*.jar") do call setenv.bat %%i
set classpath=%cp%;.;..\conf\config.properties;
echo %classpath%
java -classpath %classpath% com.github.abator.Main 
