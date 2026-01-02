#!/bin/bash
# Install Java 17 (required for Maven Spring Boot 3.4.1)
echo "[2/6] Installing Java 17..."
sudo apt-get install -y openjdk-17-jdk
java -version

# Install Maven
echo "[3/6] Installing Maven..."
sudo apt-get install -y maven
mvn -version

# Install Docker (WITHOUT version pinning to avoid the error)
echo "[4/6] Installing Docker..."
# Add Docker's official GPG key:
sudo apt update
sudo apt install ca-certificates curl
sudo install -m 0755 -d /etc/apt/keyrings
sudo curl -fsSL https://download.docker.com/linux/debian/gpg -o /etc/apt/keyrings/docker.asc
sudo chmod a+r /etc/apt/keyrings/docker.asc

# Add the repository to Apt sources:
sudo tee /etc/apt/sources.list.d/docker.sources <<EOF
Types: deb
URIs: https://download.docker.com/linux/debian
Suites: $(. /etc/os-release && echo "$VERSION_CODENAME")
Components: stable
Signed-By: /etc/apt/keyrings/docker.asc
EOF

sudo apt update

sudo apt-get install containerd.io=2.2.0-2~debian.12~bookworm
sudo apt install docker-ce docker-ce-cli docker-buildx-plugin docker-compose-plugin

# Install Jenkins
echo "[5/6] Installing Jenkins..."
# Add Jenkins GPG key
curl -fsSL https://pkg.jenkins.io/debian-stable/jenkins.io-2023.key | sudo tee \
  /usr/share/keyrings/jenkins-keyring.asc > /dev/null

# Add Jenkins repository
echo deb [signed-by=/usr/share/keyrings/jenkins-keyring.asc] \
  https://pkg.jenkins.io/debian-stable binary/ | sudo tee \
  /etc/apt/sources.list.d/jenkins.list > /dev/null

sudo apt-get update
sudo apt-get install -y jenkins

# Add Jenkins user to Docker group
echo "[6/6] Configuring permissions..."
sudo usermod -aG docker jenkins
sudo usermod -aG docker $USER

# Start and enable services
sudo systemctl enable docker
sudo systemctl start docker
sudo systemctl enable jenkins
sudo systemctl start jenkins


sudo apt install nodejs npm
npm install snyk -g
