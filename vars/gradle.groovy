def call(){

                        stage('build test') {
                        env.TAREA = env.STAGE_NAME
                        sh "./gradlew clean build"


                        }
                        stage('sonar') {

                        env.TAREA = env.STAGE_NAME
                        def scannerHome = tool 'sonar_scanner';

                        withSonarQubeEnv('sonar') {
//                                      sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
                        stage('Run'){
                        env.TAREA = env.STAGE_NAME
                        //sh 'nohup gradlew bootrun &'
                        bat "start gradlew bootRun"

                        }

                        stage('Test'){
                        env.TAREA = env.STAGE_NAME
                        sh 'sleep 30'
                        sh "curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"

                        }

                        stage('Nexus'){
                        env.TAREA = env.STAGE_NAME
//                        echo "se simula upload Nexus"
              nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]


                        }

}

return this;

