pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('credential-github')  // GitHub Personal Access Token
        REPOSITORY_PREFIX = "terrytantan"
        SPRING_PROFILES_ACTIVE = "k8s"
        VERSION=latest
        ENV=dev
    }

    stages {
        stage('Build') {
            steps {
                script {
                    sh "mvn clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX}"
                }
            }
        }

        stage('Tag images') {
            steps {
                script {
                    sh "./scripts/tagImages.sh"
                }
            }
        }
        
        stage('Push image to DockerHub') {
            steps {
                script {
                    sh "./scripts/pushImages.sh"
                }
            }
        }
        
    }
}
