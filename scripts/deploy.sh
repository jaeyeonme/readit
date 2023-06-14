#!/bin/bash

echo "> 현재 실행 중인 Docker 컨테이너 pid 확인" >> /home/ec2-user/deploy.log
CURRENT_PID=$(sudo docker container ls -q)

if [ -z $CURRENT_PID ]
then
  echo "> 현재 구동중인 Docker 컨테이너가 없으므로 종료하지 않습니다." >> /home/ec2-user/deploy.log
else
  echo "> sudo docker stop $CURRENT_PID"   # 현재 구동중인 Docker 컨테이너가 있다면 모두 중지
  sudo docker stop $CURRENT_PID
  sleep 5
fi

echo "> Docker image pull from Github Container Registry" >> /home/ec2-user/deploy.log
sudo docker pull ghcr.io/jaeyeonme/readit:latest

echo "> JAR file download from S3" >> /home/ec2-user/deploy.log
aws s3 cp s3://reddit-s3/blog-0.0.1-SNAPSHOT.jar /home/ec2-user/app/

cd /home/ec2-user/app
sudo docker run -d -p 8080:8080 -v $(pwd):/app ghcr.io/jaeyeonme/readit:latest
