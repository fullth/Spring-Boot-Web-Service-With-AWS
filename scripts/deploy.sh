#!/bin/bash

REPOSITORY=/home/ec2-user/app/step2
PROJECT_NAME=fullth-springboot-webservice

echo "> Build 파일을 복사합니다."

cp $REPOSITORY/zip/*.jar $REPOSITORY/

echo "> 현재 구동중인 애플리케이션의 프로세스 아이디를 확인합니다."

CURRENT_PID=$(pgrep -fl fullth-springboot-webservice | grep jar | awk '{print $1}')

echo "현재 구동중인 어플리케이션 pid: $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
    echo "> 현재 구동중인 애플리케이션이 없으므로 종료하지 않습니다."
else
    echo "> kill -15 $CURRENT_PID"
    kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 애플리케이션을 배포합니다."

JAR_NAME=$(ls -tr $REPOSITORY/*.jar | tail -n 1)

echo "> JAR Name: $JAR_NAME"

echo "> $JAR_NAME 에 실행권한을 추가합니다."

chmod +x $JAR_NAME

echo "> $JAR_NAME 실행"

nohup java -jar $JAR_NAME \
    -Dspring.config.location=classpath:/application.properties,classpath:/application-prod.properties,/home/ec2-user/app/application-oauth.properties,/home/ec2-user/app/application-prod-db.properties \
    -Dspring.profiles.active=prod \
    $REPOSITORY/nohup.out 2>&1 &