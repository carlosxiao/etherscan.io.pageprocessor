#!/usr/bin/env bash

##
# usage: startup.sh prod 25 1 1560
##

#startup for application
ACTIVE_PROFILE=$1
if [ -z $1 ]; then
    echo "active environment not specified use default: dev"
    ACTIVE_PROFILE="dev"
fi

PAGE_SIZE=$2
START_PAGE=$3
TOTAL_PAGE=$4
THREAD_COUNT=$5
INTERVAL_SECONDS=$6

if [ -z $JAVA_HOME ]; then
  echo "ERROR: JAVA_HOME is not found in your Environment."
  exit 1
fi
echo "Using JAVA_HOME: $JAVA_HOME"

# the path of the application installed.
APP_HOME=$(cd "`dirname $0`/.."; pwd)
echo "Using APP_HOME: $APP_HOME"

# add jar to classpath
CP=$CLASSPATH
for JAR in $APP_HOME/lib/*.jar
do
    CP=$CP:$JAR
done

echo "CLASSPATH: $CP"

MAX_HEAP=2G

JAVA_OPTS="-server -Xmx$MAX_HEAP -Xms$MAX_HEAP -Xmn256m -Xss512k -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled -XX:LargePageSizeInBytes=128m -XX:+UseFastAccessorMethods -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 -Duser.timezone=GMT+8"
## -Detherscan.startPage=1
nohup $JAVA_HOME/bin/java -DAPP_HOME=$APP_HOME -Dspring.profiles.active=$ACTIVE_PROFILE -Detherscan.startPage=$START_PAGE -Detherscan.pageSize=$PAGE_SIZE -Detherscan.totalPage=$TOTAL_PAGE -Detherscan.threadCount=$THREAD_COUNT -Detherscan.intervalSeconds=$INTERVAL_SECONDS -cp $CP $JAVA_OPTS -Xloggc:$APP_HOME/logs/gc.log org.springframework.boot.loader.JarLauncher --server.tomcat.basedir=$APP_HOME/logs --server.tomcat.access-log-enabled=true --server.tomcat.accesslog.directory=$APP_HOME/logs --server.tomcat.access-log-pattern='%{X-FORWARDED-FOR}i %l %u %t "%r" %s %b %D %q "%{User-Agent}i" %T' > /dev/null 2>&1 &
