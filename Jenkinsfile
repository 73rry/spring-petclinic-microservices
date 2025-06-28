pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
        REPOSITORY_PREFIX = "terrytantan"
        SPRING_PROFILES_ACTIVE = "k8s"
        VERSION = "${env.GIT_COMMIT}"  // You can adjust this if you want dynamic versioning
        ENV = "dev"
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Build the project and skip tests
                    sh "mvn clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX}"
                }
            }
        }

        stage('Tag images') {
            steps {
                script {
                    // Run the script to tag images
                    sh "./scripts/tagImages.sh"
                }
            }
        }

        // stage('Login to DockerHub') {
        //     steps {
        //         script {
        //             // Log in to DockerHub using Jenkins credentials
        //             sh "echo ${DOCKER_HUB_CREDENTIALS_PSW} | docker login -u ${DOCKER_HUB_CREDENTIALS_USR} --password-stdin"
        //         }
        //     }
        // }

       stage("Docker login"){
          steps {
                withCredentials([string(credentialsId: 'DockerHubPwd', variable: 'dockerpwd')]) {
                  sh "docker login -u username -p ${dockerpwd}"
                }
            }
       }
        
        stage('Push image to DockerHub') {
            steps {
                script {
                    // Run the script to push images to DockerHub
                    sh "./scripts/pushImages.sh"
                }
            }
        }

        stage('Logout from DockerHub') {
            steps {
                script {
                    // Log out from DockerHub after pushing
                    sh "docker logout"
                }
            }
        }
        
    }
}
