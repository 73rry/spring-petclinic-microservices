#!/bin/bash

export REPOSITORY_PREFIX="terrytantan"
export SPRING_PROFILES_ACTIVE="k8s"
export VERSION=$(date +%Y%m%d%H%M%S)
export ENV=dev
mvn clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX} 
./scripts/tagImages.sh
./scripts/pushImages.sh