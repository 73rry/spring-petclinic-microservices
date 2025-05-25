pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('credential-github')  // GitHub Personal Access Token
        REPOSITORY_PREFIX = "thanguyen165"
        SPRING_PROFILES_ACTIVE = "k8s"
    }

    stages {
        stage('Hello world') {
            steps {
                echo "hello world"
            }
        }

        }
    }
}
