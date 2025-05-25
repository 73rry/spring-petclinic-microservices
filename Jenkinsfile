pipeline {
    agent any

    environment {
        GITHUB_TOKEN = credentials('github-PAT')  // GitHub Personal Access Token
        REPOSITORY_PREFIX = "terrytantan"
        SPRING_PROFILES_ACTIVE = "k8s"
        DOCKER_BUILDKIT = "1"  // Enable Docker BuildKit
        DOCKER_CLI_EXPERIMENTAL = "enabled"  // Enable experimental Docker features
    }

    stages {
        stage('Install Docker Buildx') {
            steps {
                script {
                    // Ensure Docker Buildx is installed (only for environments where it's required)
                    def buildxPath = '/tmp/docker-buildx'  // Using /tmp instead of /usr/local/bin
                    if (!fileExists(buildxPath)) {
                        echo "Installing Docker Buildx"
                        sh """
                            curl -L https://github.com/docker/buildx/releases/download/v0.8.0/buildx-v0.8.0.linux-amd64 -o ${buildxPath}
                            chmod +x ${buildxPath}
                            ln -s ${buildxPath} /usr/local/bin/docker-buildx  # Optionally create symlink
                        """
                    } else {
                        echo "Docker Buildx is already installed."
                    }
                }
            }
        }

        stage('Push') {
            steps {
                script {
                    // Get the commit ID of the latest commit
                    def commitId = sh(script: 'git log -1 --pretty=%H', returnStdout: true).trim()

                    // Output the commit ID and branch name
                    echo "The latest commit ID is: ${commitId}"

                    // Build Docker image using BuildKit and buildx
                    sh """
                        export DOCKER_BUILDKIT=1
                        export DOCKER_CLI_EXPERIMENTAL=enabled
                        ./mvnw clean install -Dmaven.test.skip -P buildDocker -Ddocker.image.prefix=${REPOSITORY_PREFIX} -Dcontainer.build.extraarg="--push"
                    """
                }
            }
        }
    }
}
