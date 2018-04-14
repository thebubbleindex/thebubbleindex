#!/bin/bash

bubbleindexStandaloneFolder=/home/ipower/Downloads/gigaspace-bubbleindex/thebubbleindex
projectDirectoryFolder=/home/ipower/Downloads/gigaspace-bubbleindex/xap-thebubbleindex
gigaSpaceBinDirectoryFolder=/home/ipower/Downloads/gigaspaces-xap-open-12.3.0-ga-b19000/bin
xapHome=/home/ipower/Downloads/gigaspaces-xap-open-12.3.0-ga-b19000/
export VERBOSE=true

#export JAVA_HOME=/jdk

# may need to install the yeppp dependency if first time installing
#mvn install:install-file -Dfile=$bubbleindexStandaloneFolder/src/main/resources/yeppp-bundle.jar -DgroupId=yeppp -DartifactId=yeppp-java -Dversion=1.0 -Dpackaging=jar

# first make sure the main standalone app is install into your repository
mvn clean install -f $bubbleindexStandaloneFolder/pom.xml -DskipTests=true

# first make sure all jars are created with mvn clean package...
mvn clean install -f $projectDirectoryFolder/pom.xml -DskipTests=true

# each command is run with a delay of n seconds to ensure the space has time to start successfully

export XAP_HOME=$xapHome

# now start up lookup service, this doesn't work in 12.3 no way to start lus?
#gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/xap pu run null --lus; bash"
 
#sleep 10

export JAVA_OPTIONS="-server -Xms2g -Xmx4g -Xmn300m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=60 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxPermSize=256m -XX:+ExplicitGCInvokesConcurrent -XX:+UseCompressedOops"

# now start up pendingTaskSpace
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/xap space run pendingTaskSpace --partitions=5 --lus; bash"
sleep 12

# now start up completedTaskSpace
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/xap space run completedTaskSpace --partitions=5; bash"
sleep 12

export JAVA_OPTIONS="-server"

# now start up the bubbleindex-task-worker
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/xap pu run $projectDirectoryFolder/bubbleindex-task-worker/target/bubbleindex-task-worker.jar; bash"
sleep 12

# now start up the bubbleindex-task-finalizer
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/xap pu run $projectDirectoryFolder/bubbleindex-task-finalizer/target/bubbleindex-task-finalizer.jar; bash"
sleep 12

# next run the feeder or client application
mvn clean compile assembly:single -f $bubbleindexStandaloneFolder/pom.xml -DskipTests=true

