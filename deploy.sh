#!/usr/bin/env bash
set -e
docker-compose stop $@
docker-compose rm -f -v $@
docker-compose up -d --build rabbitMq
# Give rabbit mq time to initalize
sleep 5
docker-compose up -d --build $@
# Cleanup the build images
docker image prune -f

