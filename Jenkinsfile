pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-PAT')  // GitHub Personal Access Token
        REPOSITORY_PREFIX = "thanguyen165"
        SPRING_PROFILES_ACTIVE = "k8s"
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-PAT')
    }

    stages {
        stage('Login') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }

        
        stage('Push') {
            steps {
                script {
                    // Get the commit ID of the latest commit
                    def commitId = sh(script: 'git log -1 --pretty=%H', returnStdout: true).trim()

                    // Output the commit ID and branch name
                    echo "The latest commit ID is: ${commitId}"
                    
                    // Build Docker image using BuildKit (buildx)
                    sh """
                        ./mvnw clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX} -Dcontainer.build.extraarg="--push"
                    """
                }
            }
        }
    }
}
