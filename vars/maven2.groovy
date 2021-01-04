def call(){
	echo "Inicio maven2.goovy"
			stage('build') {
			env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        bat 'mvn clean pakage'

			}
                        stage('sonar') {
			echo "Dentro de stage sonar"
                        env.TAREA = env.STAGE_NAME
                        def scannerHome = tool 'sonar_scanner';
                        withSonarQubeEnv('sonar') {
                        	//sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        			bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
                        stage('Run'){
			echo "Dentro de stage run"
                        env.TAREA = env.STAGE_NAME
//                        bat 'nohup mvn spring-boot:run &'
			bat """set JENKINS_NODE_COOKIE=dontKillMe && start /min mvn spring-boot:run """
                        }
                        stage('test'){
			echo "Dentro de stage test"
                        env.TAREA = env.STAGE_NAME
                        bat 'sleep 30'
                        bat "curl -X GET 'http://localhost:8087/rest/mscovid/test?msg=testing'"

                        }
                        stage('nexus'){
			echo "Dentro de stage nexus"
                        env.TAREA = env.STAGE_NAME
//                        echo 'Testing failed!'
//                        currentBuild.result = 'UNSTABLE'
                        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './nada/build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]

                        }
}
return this;

