pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-PAT')  // GitHub Personal Access Token
        REPOSITORY_PREFIX="terrytantan"
        SPRING_PROFILES_ACTIVE="k8s"
    }

    stages {
        stage('Push') {
            steps {
                script {
                    // Get the commit ID of the latest commit
                    def commitId = sh(script: 'git log -1 --pretty=%H', returnStdout: true).trim() 

                    // Output the commit ID and branch name
                    echo "The latest commit ID is: ${commitId}"
                    sh """
                        ./mvnw clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX} -Dcontainer.build.extraarg="--push"
                    """ 
                    
                }
            }
        }
    }
}
