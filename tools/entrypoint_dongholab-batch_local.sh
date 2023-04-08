#!/bin/sh
SERVICE=${1:-API}
JOB_NAME=$2
SERVICE_NAME=$SERVICE_NAME

echo $SERVICE_NAME
echo $SPRING_PROFILES_ACTIVE


JAVA_OPTS="-server -Xms2g -Xmx2g -XX:+UseG1GC -Dspring.profiles.active=${SPRING_PROFILES_ACTIVE} -Dfile.encoding=UTF8 -Dsun.net.inetaddr.ttl=0 -Dspring.batch.job.names=$JOB_NAME -Dtag=$TAG"
java $JAVA_OPTS -jar ./dongholab-batch.jar

