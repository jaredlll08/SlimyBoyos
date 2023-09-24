#!/usr/bin/env groovy

def branchName = "1.20.2";

pipeline {
    agent any
    tools {
        jdk "jdk-17.0.1"
    }

    environment {
        modrinth_token = credentials('modrinth_token')
        curseforgeApiToken = credentials('curseforge_token')
        discordCFWebhook = credentials('discord_cf_webhook')
        versionTrackerKey = credentials('version_tracker_key')
        versionTrackerAPI = credentials('version_tracker_api')
    }

    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }

        stage('Publish') {
            stages {
                stage('Updating Version') {
                    when {
                        branch branchName
                    }
                    steps {
                        script {
                            if (sh(script: "git log -1 --pretty=%B | fgrep -i -e '[skip deploy]' -e '[skip-deploy]'", returnStatus: true) == 0) {
                                echo 'Skipping Update Version due to [skip deploy]'
                            } else {
                                echo 'Updating Version'
                                sh './gradlew updateVersionTracker'
                            }
                        }

                    }
                }

                stage('Deploying to Maven') {
                    when {
                        branch branchName
                    }
                    steps {
                        echo 'Deploying to Maven'
                        sh './gradlew publish'
                    }
                }

                stage('Deploying to CurseForge') {
                    when {
                        branch branchName
                    }
                    steps {
                        script {
                            if (sh(script: "git log -1 --pretty=%B | fgrep -i -e '[skip deploy]' -e '[skip-deploy]'", returnStatus: true) == 0) {
                                echo 'Skipping CurseForge due to [skip deploy]'
                            } else {
                                echo 'Deploying to CurseForge'
                                sh './gradlew publishCurseForge modrinth postDiscord'
                            }
                        }

                    }
                }

            }
        }
    }

    options {
        disableConcurrentBuilds()
    }
}
