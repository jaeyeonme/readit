#!/bin/bash

# 작업 디렉토리를 /home/ec2-user/app으로 변경
cd /home/ec2-user/app

# 환경변수 DOCKER_APP_NAME을 simple-blog로 설정
DOCKER_APP_NAME=simple-blog

# 실행중인 blue가 있는지 확인
# 프로젝트의 실행 중인 컨테이너를 확인하고, 해당 컨테이너가 실행 중인지 여부를 EXIST_BLUE 변수에 저장
EXIST_BLUE=$(sudo docker-compose ps | grep blue-backend | grep Up)

# 배포 시작한 날짜와 시간을 기록
echo "배포 시작일자 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

# green이 실행중이면 blue up
# EXIST_BLUE 변수가 비어있는지 확인
if [ -z "$EXIST_BLUE" ]; then
  echo "blue-backend 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
  sudo docker-compose up -d --build blue-backend

  sleep 30

  echo "green-backend 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
  sudo docker-compose stop green-backend
  sudo docker image prune -af

  echo "green-backend 중단 완료 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
else
  echo "green-backend 배포 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
  sudo docker-compose up -d --build green-backend

  sleep 30

  echo "blue-backend 중단 시작 : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
  sudo docker-compose stop blue-backend
  sudo docker image prune -af

  echo "blue-backend 중단 완료 : $("$(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log
fi
  echo "배포 종료  : $(date +%Y)-$(date +%m)-$(date +%d) $(date +%H):$(date +%M):$(date +%S)" >> /home/ec2-user/deploy.log

  echo "===================== 배포 완료 =====================" >> /home/ec2-user/deploy.log
  echo >> /home/ec2-user/deploy.log
