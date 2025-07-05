def affectedServices = []

pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-PAT')
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-terry-cred')
        REPOSITORY_PREFIX = "terrytantan"
        VERSION = "${env.GIT_COMMIT}"
        ENV = "dev"
    }

    stages {
        stage('Check if PR to main') {
            when {
                expression { return env.CHANGE_ID && env.CHANGE_TARGET == 'main' }
            }
            steps {
                echo "Pull request vào main, tiếp tục kiểm tra!"
            }
        }

        stage('Determine changed services') {
            steps {
                script {
                    def changedFiles
                    if (env.CHANGE_TARGET) {
                        // Ensure the target branch is fetched
                        sh "git fetch origin ${env.CHANGE_TARGET}"
                        
                        sh "git checkout -b target_branch FETCH_HEAD"
                        
                        changedFiles = sh(returnStdout: true, script: "git diff --name-only target_branch...HEAD").trim().split('\n')
                    } else {  
                        changedFiles = sh(returnStdout: true, script: "git diff --name-only HEAD^ HEAD").trim().split('\n')
                    }

                    affectedServices = changedFiles.findAll { file ->
                        file.startsWith('spring-petclinic-')
                    }.collect { file ->
                        return file.split('/')[0]
                    }.unique()

                    echo "Last Commit ID: ${env.GIT_COMMIT}"
                    echo "Affected services: ${affectedServices}"
                    sh "docker system prune -a -f"
                }
            }
        }

        stage('Build affected services') {
            when {
                expression { return affectedServices?.size() > 0 }
            }
            steps {
                script {
                    for (service in affectedServices) {
                        echo "Building ${service} ..."
                        dir(service) {
                            sh "mvn clean package -DskipTests"
                            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, allowEmptyArchive: true
                        }
                        echo "${service} build completed."
                    }
                }
            }
        }
        
        stage('Build docker') {
            when {
                expression { return affectedServices?.size() > 0 }
            }
            steps {
                script {
                    for (service in affectedServices) {
                        echo "Building docker image for ${service} ..."
                        sh "cp ${service}/target/*.jar docker/${service}.jar"
                        
                        sh """
                            docker build -t ${REPOSITORY_PREFIX}/${service}-${ENV} \
                                --build-arg ARTIFACT_NAME=${service} \
                                -f ./docker/Dockerfile ./docker
                        """
                        sh """
                             docker tag ${REPOSITORY_PREFIX}/${service}-${ENV} ${REPOSITORY_PREFIX}/${service}-${ENV}:${VERSION}
                        """

                        sh "rm docker/${service}.jar"
                        sh "docker images"
                        echo "docker ${service} build completed."
                    }
                }
            }
        }
        
        stage('Login to DockerHub') {
            when {
                expression { return affectedServices?.size() > 0 }
            }
            steps {
                script {
                    sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"
                }
            }
        }
       
       stage('Push image to DockerHub') {
            when {
                expression { return affectedServices?.size() > 0 }
            }
            steps {
                script {
                    for (service in affectedServices) {
                        echo "Pushing docker image for ${service} ..."
                        sh """
                             docker push ${REPOSITORY_PREFIX}/${service}-$ENV:${VERSION}
                        """
                    }
                }
            }
        }
    }

    post {
        always {
            echo 'Pipeline completed!'
            sh 'find . -mindepth 1 -exec rm -rf {} +'
            sh "docker logout"
        }
    }
}
