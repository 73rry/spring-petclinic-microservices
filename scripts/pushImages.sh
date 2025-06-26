#!/bin/bash
docker push ${REPOSITORY_PREFIX}/spring-petclinic-config-server-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-discovery-server-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-api-gateway-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-visits-service-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-vets-service-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-customers-service-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-genai-service-$ENV:${VERSION}
docker push ${REPOSITORY_PREFIX}/spring-petclinic-admin-server-$ENV:${VERSION}
