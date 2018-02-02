@echo on
set javahome = %JAVA_HOME%
set "path=%JAVA_HOME%\bin"
set "classpath=%JAVA_HOME%\jre\lib"


"%path%\java"  -Xms256m -Xmx512m -jar socket-server-0.0.1-SNAPSHOT.jar

echo on