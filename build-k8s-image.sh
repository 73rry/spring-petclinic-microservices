#!/bin/bash

export REPOSITORY_PREFIX="terrytantan"
export SPRING_PROFILES_ACTIVE="k8s"
./mvnw clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX} -Dcontainer.build.extraarg="--push"