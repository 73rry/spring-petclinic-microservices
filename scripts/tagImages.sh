#!/bin/bash
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-config-server ${REPOSITORY_PREFIX}/spring-petclinic-config-server-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-discovery-server ${REPOSITORY_PREFIX}/spring-petclinic-discovery-server-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-api-gateway ${REPOSITORY_PREFIX}/spring-petclinic-api-gateway-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-visits-service ${REPOSITORY_PREFIX}/spring-petclinic-visits-service-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-vets-service ${REPOSITORY_PREFIX}/spring-petclinic-vets-service-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-customers-service ${REPOSITORY_PREFIX}/spring-petclinic-customers-service-$ENV:${VERSION}
docker tag ${REPOSITORY_PREFIX}/spring-petclinic-admin-server ${REPOSITORY_PREFIX}/spring-petclinic-admin-server-$ENV:${VERSION}
