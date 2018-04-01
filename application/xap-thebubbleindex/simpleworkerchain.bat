SET bubbleindexStandaloneFolder=C:\Users\ipower\workspace\thebubbleindex\
SET projectDirectoryFolder=C:\Users\ipower\workspace\xap-thebubbleindex\
SET gigaSpaceBinDirectoryFolder=C:\Users\ipower\Downloads\gigaspaces-xap-open-12.2.1-ga-b18100\bin\

REM first make sure the main standalone app is install into your repository
start cmd.exe /k mvn clean install -f %bubbleindexStandaloneFolder%pom.xml -DskipTests=true
ping 127.0.0.1 -n 12 > nul

REM first make sure all jars are created with mvn clean package...
start cmd.exe /k mvn clean install -f %projectDirectoryFolder%pom.xml -DskipTests=true
ping 127.0.0.1 -n 20 > nul

REM each command is run with a delay of n seconds to ensure the space has time to start successfully

REM now start up lookup service
start cmd.exe /k %gigaSpaceBinDirectoryFolder%lookup-service.bat
ping 127.0.0.1 -n 10 > nul

set JAVA_OPTIONS=-server -Xms2g -Xmx4g -Xmn300m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=60 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxPermSize=256m -XX:+ExplicitGCInvokesConcurrent -XX:+UseCompressedOops

REM now start up pendingTaskSpace
start cmd.exe /k %gigaSpaceBinDirectoryFolder%space-instance.bat -name pendingTaskSpace -cluster schema=partitioned total_members=5,0
ping 127.0.0.1 -n 12 > nul

REM now start up completedTaskSpace
start cmd.exe /k %gigaSpaceBinDirectoryFolder%space-instance.bat -name completedTaskSpace -cluster schema=partitioned total_members=5,0
ping 127.0.0.1 -n 12 > nul

set JAVA_OPTIONS=-server

REM now start up the bubbleindex-task-worker
start cmd.exe /k %gigaSpaceBinDirectoryFolder%pu-instance.bat -path %projectDirectoryFolder%bubbleindex-task-worker\target\bubbleindex-task-worker.jar
ping 127.0.0.1 -n 12 > nul

REM now start up the bubbleindex-task-finalizer
start cmd.exe /k %gigaSpaceBinDirectoryFolder%pu-instance.bat -path %projectDirectoryFolder%bubbleindex-task-finalizer\target\bubbleindex-task-finalizer.jar
ping 127.0.0.1 -n 12 > nul

REM next run the feeder or client application, in this case GigaspaceTest2
start cmd.exe /k mvn clean compile assembly:single -f %bubbleindexStandaloneFolder%pom.xml -DskipTests=true
ping 127.0.0.1 -n 12 > nul