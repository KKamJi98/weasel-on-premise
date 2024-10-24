pipeline {
    agent any

    tools {
        nodejs 'Nodejs'
    }

    environment {
        AWS_REGION = 'us-east-1'
        AWS_CREDENTIAL = 'weasel-AWS-Credential'
        GIT_URL = 'https://github.com/Team-S5T1/weasel-frontend.git'
        SLACK_CHANNEL = '#alarm-jenkins'
        SLACK_CREDENTIALS_ID = 'weasel-slack-alarm'
        S3_BUCKET = 'weasel-frontend'
        S3_DISTRIBUTION_ID = 'EBDDD040I1O72'
    }

    stages {
        stage('Send message to slack') {
            steps {
                script {
                    slackSend(channel: SLACK_CHANNEL,
                              message: "Jenkins pipeline started: ${env.JOB_NAME} #${env.BUILD_NUMBER} - ${env.BUILD_URL}",
                              attachments: [[
                                  color: '#0000ff',
                                  text: 'Jenkins start!'
                              ]])
                }
            }
        }

        stage('Cloning Git') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/main']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '', url: "${GIT_URL}"]]])
            }
        }

        stage('Build') {
            steps {
                script {
                    sh '''
                        npm install
                        npm run build
                    '''
                }
            }
        }



      stage('Upload to S3') {
            steps {
                withCredentials([[$class: 'AmazonWebServicesCredentialsBinding', credentialsId: "${AWS_CREDENTIAL}"]]) {
                    sh """
                        aws s3 cp dist/ s3://${S3_BUCKET}/ --recursive --region ${AWS_REGION}
                        aws cloudfront create-invalidation --distribution-id ${S3_DISTRIBUTION_ID} --paths "/*" --region us-east-1
                    """
                }
            }
        }

    }    

        post {
            success {
                slackSend(channel: SLACK_CHANNEL,
                        message: "Build succeeded: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        attachments: [[
                            color: '#36a64f',
                            text: 'Build succeeded successfully.'
                        ]])
            }
            failure {
                slackSend(channel: SLACK_CHANNEL,
                        message: "Build failed: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        attachments: [[
                            color: '#ff0000',
                            text: 'Build failed. Please check the details.'
                        ]])
            }
            unstable {
                slackSend(channel: SLACK_CHANNEL,
                        message: "Build unstable: ${env.JOB_NAME} #${env.BUILD_NUMBER}",
                        attachments: [[
                            color: '#f39c12',
                            text: 'Build unstable. Please check the details.'
                        ]])
            }
        }
    
}