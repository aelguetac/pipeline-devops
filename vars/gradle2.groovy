def call(){
                        stage('build') {
//                      when { expression { env.cstage == 'build' }}
//                      when { expression { ${cstage[i]} == 'build' }}
//                        stage('build test') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        bat "./gradlew clean build"

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
                        bat 'waitfor dir /t 5 2>NUL'
//			final String url = "http://localhost:8087/rest/mscovid/test?msg=testing"
//                    	final String response = sh(script: "curl -s $url", returnStdout: true).trim()
//                    	echo response
//                        bat 'curl.exe -s -X http://localhost:8087/rest/mscovid/test?msg=testing'
//                        bat 'curl.exe -s -X GET http://localhost:9000'
//			bat 'curl -s -o /dev/null -w "%{http_code}" http://localhost:9000/'
//			bat 'curl --silent --fail http://httpstat.us/200 > /dev/null; echo $?'
                        }
                        stage('nexus'){
                        env.TAREA = env.STAGE_NAME
                        echo "Dentro de stage nexus"
//              nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
//                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
//                filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']],
//                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]
		nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus', 
		packages: [[$class: 'MavenPackage', mavenAssetList: [[classifier: '', extension: 'jar', 
		filePath: 'D:\\Diplomado\\talleres\\ejemplo-gradle\\build\\libs\\DevOpsUsach2020-0.0.1.jar']], 
		mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging: 'jar', version: '0.0.1']]]
                        }

}

return this;

