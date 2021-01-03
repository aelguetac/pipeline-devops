def call(){
	echo "Inicio maven.goovy"
	def cstage = params.stage.split(';')
	for (int i = 0 ; i < cstage.length; i++){

//	switch("cstage"){
//	case "build":
			stage('build') {
//                      when { expression { env.cstage == 'build' }}
			when { expression { ${cstage[i]} == 'build' }}
			env.TAREA = env.STAGE_NAME
                        sh "./mavenw clean build"

			}
//        break
//	case "sonar":
                        stage('sonar') {
                        env.TAREA = env.STAGE_NAME
                        def scannerHome = tool 'sonar_scanner';

                        withSonarQubeEnv('sonar') {
//                                      sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
//	break
//	case "run":
                        stage('Run'){
                        env.TAREA = env.STAGE_NAME
                        sh 'nohup mvn spring-boot:run &'
                        }
//	break
//	case "test":
                        stage('Test'){
                        env.TAREA = env.STAGE_NAME
                        sh 'sleep 30'
                        sh "curl -X GET 'http://localhost:8087/rest/mscovid/test?msg=testing'"

                        }
//	break
//	case "nexus":
                        stage('Nexus'){
                        env.TAREA = env.STAGE_NAME
                        echo 'Testing failed!'
                        currentBuild.result = 'UNSTABLE'
                        nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './nada/build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]
//	break

                        }

		
	}
}
return this;

