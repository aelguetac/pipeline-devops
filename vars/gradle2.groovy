def call(){
        echo "Inicio gradle2.goovy"
                        stage('build') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        sh "./gradlew clean build"

                        }

			stage('sonar') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage sonar"
                        def scannerHome = tool 'sonar_scanner';
                        withSonarQubeEnv('sonar') {
//                     sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
                        stage('run'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage run"
                        bat "start gradlew bootRun"

                        }

                        stage('test'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage test"
                        bat 'sleep 30'
			final String url = "http://localhost:8087/rest/mscovid/test?msg=testing"
                    	final String response = sh(script: "curl -s $url", returnStdout: true).trim()
                    	echo response
//                        sh "curl -X GET 'http://localhost:8087/rest/mscovid/test?msg=testing'"

                        }
                        stage('nexus'){
                        env.TAREA = env.STAGE_NAME
                        echo "Dentro de stage nexus"
              nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]

                        }

}

return this;

