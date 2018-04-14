#!/bin/bash

bubbleindexStandaloneFolder=/home/ipower/Downloads/gigaspace-bubbleindex/thebubbleindex
projectDirectoryFolder=/home/ipower/Downloads/gigaspace-bubbleindex/xap-thebubbleindex
gigaSpaceBinDirectoryFolder=/home/ipower/Downloads/gigaspaces-xap-open-12.2.1-ga-b18100/bin
xapHome=/home/ipower/Downloads/gigaspaces-xap-open-12.2.1-ga-b18100/
export VERBOSE=true

#export JAVA_HOME=/home/jdk

# first make sure the main standalone app is install into your repository
mvn clean install -f $bubbleindexStandaloneFolder/pom.xml -DskipTests=true

# first make sure all jars are created with mvn clean package...
mvn clean install -f $projectDirectoryFolder/pom.xml -DskipTests=true

# each command is run with a delay of n seconds to ensure the space has time to start successfully

export XAP_HOME=$xapHome

# now start up lookup service
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/lookup-service.sh; bash"
sleep 10

export JAVA_OPTIONS="-server -Xms2g -Xmx4g -Xmn300m -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -XX:CMSInitiatingOccupancyFraction=60 -XX:+UseCMSInitiatingOccupancyOnly -XX:MaxPermSize=256m -XX:+ExplicitGCInvokesConcurrent -XX:+UseCompressedOops"

# now start up pendingTaskSpace
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/space-instance.sh -name pendingTaskSpace -cluster schema=partitioned total_members=5,0; bash"
sleep 12

# now start up completedTaskSpace
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/space-instance.sh -name completedTaskSpace -cluster schema=partitioned total_members=5,0; bash"
sleep 12

export JAVA_OPTIONS="-server"

# now start up the bubbleindex-task-worker
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/pu-instance.sh -path $projectDirectoryFolder/bubbleindex-task-worker/target/bubbleindex-task-worker.jar; bash"
sleep 12

# now start up the bubbleindex-task-finalizer
gnome-terminal -x sh -c "$gigaSpaceBinDirectoryFolder/pu-instance.sh -path $projectDirectoryFolder/bubbleindex-task-finalizer/target/bubbleindex-task-finalizer.jar; bash"
sleep 12

# next run the feeder or client application
mvn clean compile assembly:single -f $bubbleindexStandaloneFolder/pom.xml -DskipTests=true

