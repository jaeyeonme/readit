#!/bin/bash

DOCKER_APP_NAME=readit
IMAGE_NAME=ghcr.io/jaeyeonme/readit:latest

# Docker 이미지 pull
sudo docker pull $IMAGE_NAME

# 실행중인 blue가 있는지
EXIST_BLUE=$(sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml ps | grep Up)

# green이 실행중이면 blue up
if [ -z "$EXIST_BLUE" ]; then
    echo "blue up"
    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml up -d --build

    sleep 30

    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml down
    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml rm -f # green 컨테이너 삭제
    sudo docker image prune -af # 사용하지 않는 이미지 삭제

# blue가 실행중이면 green up
else
    echo "green up"
    sudo docker-compose -p ${DOCKER_APP_NAME}-green -f docker-compose.green.yml up -d --build

    sleep 30

    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml down
    sudo docker-compose -p ${DOCKER_APP_NAME}-blue -f docker-compose.blue.yml rm -f # blue 컨테이너 삭제
    sudo docker image prune -af
fi
