def call(){
        echo "Inicio CD.goovy"
                        
	stage('downloadNexus') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage downloadNexus"
                        bat "curl -X GET -u admin:admin http://localhost:8081/test-nexus:com#browse/browse:%2Fdevopsusach2020%2FDevOpsUsach2020%2F0.0.1%2FDevOpsUsach2020-0.0.1.jar -O"

                        }

       stage('runJar'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage run"
                        bat "start gradlew bootRun"

                        }
          stage('rest'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage test"
                        bat 'waitfor dir /t 5 2>NUL'
                        }
          stage('nexusCD'){
                        env.TAREA = env.STAGE_NAME
                        echo "Dentro de stage nexus"
		nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'nexusCD', 
		packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', 
		filePath: 'D:\\Diplomado\\talleres\\ejemplo-gradle\\build\\libs\\DevOpsUsach2020-0.0.1.jar']], 
		mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
                        }
}

return this;

