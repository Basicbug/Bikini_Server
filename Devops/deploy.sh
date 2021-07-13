#!/bin/bash

WORKSPACE=/home/ec2-user/workspace
REPOSIOTRY=Bikini_Server
PROJECT_NAME=bikini

cd $WORKSPACE/$REPOSIOTRY

echo "Refresh code base ..."

git pull

echo "Build Start ..."

./gradlew build

echo "Copy Build Result ..."

cp ./build/libs/*.jar $WORKSPACE/deploy/

echo "Check running application pid"

CURRENT_PID=$(pgrep -f $PROJECT_NAME.*.jar)

echo "Current running application pid is : $CURRENT_PID"

if [ -z $CURRENT_PID ]; then
    echo "There is no running application"
else
    echo "Kill running application $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "Deploy application"

JAR_NAME=$(ls $WORKSPACE/deploy/ | grep $PROJECT_NAME | tail -n 1)

echo "Jar file name : $JAR_NAME"

nohup java -jar -Dspring.profiles.active=prod $WORKSPACE/deploy/$JAR_NAME &
