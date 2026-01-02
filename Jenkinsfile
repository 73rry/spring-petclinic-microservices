def servicesList = ['spring-petclinic-admin-server', 'spring-petclinic-api-gateway', 'spring-petclinic-config-server', 'spring-petclinic-customers-service', 'spring-petclinic-discovery-server', 'spring-petclinic-genai-service', 'spring-petclinic-vets-service', 'spring-petclinic-visits-service']
def affectedServices = []

pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-PAT')
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-PAT')
        REPOSITORY_PREFIX = "matadora04"
        VERSION = "${env.GIT_COMMIT}"
        ENV = "dev"
    }

    stages {
        stage('Check for Git Tag') {
            steps {
                script {
                    // Kiểm tra nếu build được kích hoạt bởi một Git tag
                    if (env.GIT_TAG) {
                        // Lấy commit ID và git tag
                        def commitId = env.GIT_COMMIT
                        def gitTag = env.GIT_TAG

                        echo "Git Tag detected: ${gitTag}. Changing ENV to 'staging' and checking Docker images."

                        // Kiểm tra nếu tất cả images `${REPOSITORY_PREFIX}/${service}-dev:${commitId}` đã tồn tại
                        def allImagesExist = true
                        for (service in servicesList) {
                            echo "Checking if image ${image} exists..."
                            def image = "${REPOSITORY_PREFIX}/${service}-${ENV}:${commitId}"
                            
                            // Kiểm tra image tồn tại trên Docker Hub
                            def imageExists = sh(script: "docker pull ${image}", returnStatus: true) == 0
                            
                            if (!imageExists) {
                                echo "Image ${image} does not exist, skipping this pipeline."
                                allImagesExist = false
                                break  // Dừng nếu có bất kỳ image nào không tồn tại
                            }
                        }

                        // Nếu tất cả images đã tồn tại, tạo image mới và push lên Docker Hub
                        if (allImagesExist) {
                            echo "All images exist. Creating new images with tag ${gitTag} and pushing to DockerHub."
                            ENV = gitTag  // Đổi ENV thành tên tag
                            VERSION = gitTag  // Đổi VERSION thành Git tag

                            // Đăng nhập vào DockerHub
                            sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"

                            // Tạo và push image mới cho từng service
                            for (service in servicesList) {
                                def imageDev = "${REPOSITORY_PREFIX}/${service}-dev:${commitId}"
                                def imageStaging = "${REPOSITORY_PREFIX}/${service}-staging:${gitTag}"

                                // Tạo image mới với tag 'staging'
                                sh "docker tag ${imageDev} ${imageStaging}"
                                echo "Pushing image ${imageStaging} to DockerHub"
                                sh "docker push ${imageStaging}"
                            }

                            // Đăng xuất khỏi DockerHub
                            sh "docker logout"

                            // Dừng pipeline sau khi push images
                            echo "Images pushed successfully. Stopping the pipeline."
                            return  // Dừng pipeline sau khi hoàn tất
                        } else {
                            echo "Not all required images exist. Stopping pipeline."
                            return  // Dừng pipeline nếu một trong các image không tồn tại
                        }
                    }
                }
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

        stage('SonarQube Analysis') {
            when {
                expression { return affectedServices?.size() > 0 }
            }
            steps {
                script {
                    for (service in affectedServices) {
                        // Sử dụng block 'withSonarQubeEnv' để Jenkins tự động nạp Token và URL
                        withSonarQubeEnv('sonarqube') { 
                            dir(service) {
                                sh "mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar " +
                                "-Dsonar.projectKey=spring-petclinic-${service} " +
                                "-Dsonar.projectName=spring-petclinic-${service}"
                            }
                        }
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
