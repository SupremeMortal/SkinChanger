pipeline {
    agent any

    tools {
        maven 'Maven 3'
        jdk 'Java 8'
    }

    options {
        buildDiscarder(logRotator(artifactNumToKeepStr: '1'))
    }

    stages {
        stage ('Build') {
            steps {
                sh 'mvn clean package'
            }
            post {
                success {
                    archiveArtifacts artifacts: 'target/SkinChanger.jar', fingerprint: true
                }
            }
        }

        stage ('Deploy') {
            steps {
                sh 'mvn javadoc:jar source:jar deploy -DskipTests'
            }
        }
    }
}