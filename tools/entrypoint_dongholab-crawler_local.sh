#!/bin/sh
SERVICE_NAME=$SERVICE_NAME

echo $SERVICE_NAME
echo $SPRING_PROFILES_ACTIVE
echo $TAG

JAVA_OPTS="-server -Xms2g -Xmx2g -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -Dspring.profiles.active=alpha -XX:ParallelGCThreads=3 -XX:+DisableExplicitGC -XX:+UseStringDeduplication -Dfile.encoding=UTF8 -Dsun.net.inetaddr.ttl=0 -Dtag=$TAG"
java $JAVA_OPTS -jar ./dongholab-consumer.jar
