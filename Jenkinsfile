pipeline {
    agent any

    environment {
        REPO_URL = "http://${env.MAVEN_URL}/repository/maven-snapshots/"
        REPO_ID = "snapshots"
        PROJECT_NAME = "${env.JOB_NAME}"
        DIR_API = "${env.DIR_KEY}-api"
        DIR_SERVICE = "${env.DIR_KEY}-service"
        DIR_SIMULATOR = "${env.DIR_KEY}-simulator"
        DIR_CLIENT = "${env.DIR_KEY}-client"
        IMAGE_NAME = "${env.REGISTRY_URL}/${PROJECT_NAME}:${env.BUILD_NUMBER}"
        BUILD_NUMBER = "${env.BUILD_NUMBER}"
    }

    parameters {
        choice(choices: [true, false], description: '是否发布API', name: 'DEPLOY_API')
        choice(choices: ['service', 'simulator', 'client'], description: '发布服务类型', name: 'DEPLOY_TYPE')
    }

    tools {
        maven 'M3'
    }

    stages {
        stage('构建并发布') {
            when { expression { params.DEPLOY_API == "true" } }
            steps {
                script {
                    dir(DIR_API) {
                        sh '''
                            echo '============================== 构建并发布 =============================='
                            mvn clean deploy -DBUILD_NUMBER=${BUILD_NUMBER}' -DaltDeploymentRepository=${REPO_ID}::default::${REPO_URL}
                        '''
                    }
                }
            }
        }
        stage('构建服务镜像') {
            when { expression { params.DEPLOY_TYPE == "service" } }
            steps {
                script {
                    sh '''
                        echo '============================== 构建镜像 =============================='
                        docker build -t ${IMAGE_NAME} -f ../Dockerfile ./${DIR_SERVICE}/
                    '''
                }
            }
        }
        stage('构建模拟器镜像') {
            when { expression { params.DEPLOY_TYPE == "simulator" } }
            steps {
                script {
                    sh '''
                        echo '============================== 构建镜像 =============================='
                        docker build -t ${IMAGE_NAME} -f ../Dockerfile ./${DIR_SIMULATOR}/
                    '''
                }
            }
        }
        stage('构建客户端镜像') {
            when { expression { params.DEPLOY_TYPE == "client" } }
            steps {
                script {
                    sh '''
                        echo '============================== 构建镜像 =============================='
                        docker build -t ${IMAGE_NAME} -f ../Dockerfile ./${DIR_CLIENT}/
                    '''
                }
            }
        }
        stage('上传镜像') {
            steps {
                sh '''
                    echo '============================== 上传镜像 =============================='
                    docker push ${IMAGE_NAME}
                '''
            }
        }
        stage('运行镜像') {
            steps {
                sh '''
                    echo '============================== 运行镜像 =============================='
                    if [ -n \"\$(docker ps -q -f name=${PROJECT_NAME})" ]; then
                        docker stop ${PROJECT_NAME}
                    fi
                    if [ -n \"\$(docker ps -aq -f name=${PROJECT_NAME})" ]; then
                        docker rm ${PROJECT_NAME}
                    fi
                    docker pull ${IMAGE_NAME}
                    docker run -d --name ${PROJECT_NAME} ${IMAGE_NAME}
                    sleep 10
                    docker logs ${PROJECT_NAME}
                '''
            }
        }
    }
}