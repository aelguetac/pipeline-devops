def call(){
        echo "Inicio ci.goovy"
                        
	stage('buildAndTest') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        bat "./gradlew clean build"

                        }

	stage('sonar') {

                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage sonar"
                        def scannerHome = tool 'sonar_scanner';
                        withSonarQubeEnv('sonar') {
        		bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
                        			}

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
          stage('nexusCI'){
                        env.TAREA = env.STAGE_NAME
                        echo "Dentro de stage nexus"
		nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'nexusCI', 
		packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', 
		filePath: 'D:\\Diplomado\\talleres\\ejemplo-gradle\\build\\libs\\DevOpsUsach2020-0.0.1.jar']], 
		mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
                        }
}

return this;

