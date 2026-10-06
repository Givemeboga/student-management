pipeline {
    agent any
    tools {
        jdk 'JDK17'
        maven 'M2_HOME'
    }
    triggers {
        pollSCM('H/5 * * * *')
    }
    environment {
        IMAGE = 'givemeboga/student-management'
        TAG   = "${env.BUILD_NUMBER}"
    }
    stages {
        stage('Commit') {
            steps {
                git branch: 'main', url: 'https://github.com/Givemeboga/student-management.git'
                sh 'git log -1 --pretty=format:"Commit %h | Auteur : %an | Message : %s"'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }
        stage('Test unitaire') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Docker Build') {
            steps {
                sh 'docker build -t $IMAGE:$TAG -t $IMAGE:latest .'
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhubcredentials',
                        usernameVariable: 'DH_USER', passwordVariable: 'DH_TOKEN')]) {
                    sh 'echo $DH_TOKEN | docker login -u $DH_USER --password-stdin'
                }
                sh 'docker push $IMAGE:$TAG'
                sh 'docker push $IMAGE:latest'
            }
        }
    }
    post {
        always {
            sh 'docker logout || true'
        }
        success {
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
            echo 'Pipeline réussi : jar archivé et image Docker publiée.'
        }
        failure {
            echo 'Pipeline en échec : consultez la console et le rapport de tests.'
        }
    }
}
