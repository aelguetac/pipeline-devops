def call(){
	echo "Inicio maven.goovy"
	def cstage = params.stage.split(';')
	for (int i = 0 ; i < cstage.length; i++){
							echo " el primer for para ${cstage[i]}"
	switch("${cstage[i]}"){
	case "build":
			stage('build') {
//                      when { expression { "env.cstage == 'build' }}
//			when { expression { ${cstage[i]} == 'build' }}
//			when { expression { "${params.stage}" == "build" }}
			env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        bat 'mvn clean pakage'

			}
        break
	case "sonar":
                        stage('sonar') {
			echo "Dentro de stage sonar"
                        env.TAREA = env.STAGE_NAME
                        def scannerHome = tool 'sonar_scanner';

                        withSonarQubeEnv('sonar') {
                                      sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
	break
	case "run":
                        stage('Run'){
			echo "Dentro de stage run"
                        env.TAREA = env.STAGE_NAME
                        bat 'mvn spring-boot:run | at now + 1 minutes'

                        }
	break
	case "test":
                        stage('test'){
			echo "Dentro de stage test"
                        env.TAREA = env.STAGE_NAME
                        bat 'sleep 30'
                        bat "curl -X GET 'http://localhost:8087/rest/mscovid/test?msg=testing'"

                        }
	break
	case "nexus":
                        stage('nexus'){
			echo "Dentro de stage nexus"
                        env.TAREA = env.STAGE_NAME
                        echo 'Testing failed!'
                        currentBuild.result = 'UNSTABLE'
                        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './nada/build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]
	break

                        }

	}	
	}
}
return this;

