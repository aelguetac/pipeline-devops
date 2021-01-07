def call(){
        echo "Inicio gradle.goovy"
        def cstage = params.stage.split(';')
        for (int i = 0 ; i < cstage.length; i++){
		echo " ejecucion de FOR para  ${cstage[i]}"
      switch("${cstage[i]}"){
      case "build":
                        stage('build') {
//                      when { expression { env.cstage == 'build' }}
//                      when { expression { ${cstage[i]} == 'build' }}
//                        stage('build test') {
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage build"
                        bat "./gradlew clean build"

                        }
        break
        case "sonar":

			stage('sonar') {

                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage sonar"
                        def scannerHome = tool 'sonar_scanner';

                        withSonarQubeEnv('sonar') {
//                     sh "${scannerHome}/bin/sonar-scanner  -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"
        bat "${scannerHome}\\bin\\sonar-scanner -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build"

                                        }

                        }
        break
        case "run":
                        stage('run'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage run"
                        bat "start gradlew bootRun"

                        }
        break
        case "test":

                        stage('test'){
                        env.TAREA = env.STAGE_NAME
			echo "Dentro de stage test"
                        bat 'waitfor dir /t 5 2>NUL'
//			final String url = "http://localhost:8087/rest/mscovid/test?msg=testing"
//                    	final String response = sh(script: "curl -s $url", returnStdout: true).trim()
//                    	echo response
//                        bat 'curl -s http://localhost:8087/rest/mscovid/test?msg=testing'
                        bat 'curl -s -X GET http://localhost:8089'
                        }
        break
        case "nexus":
                        stage('nexus'){
                        env.TAREA = env.STAGE_NAME
//                        echo "se simula upload Nexus"
                        echo "Dentro de stage nexus"
              nexusPublisher nexusInstanceId: 'nexus', nexusRepositoryId: 'test-nexus',
                packages: [[ $class: 'MavenPackage', MavenAssetList: [[classifier: 'RELEASE', extensions: 'jar' ,
                filePath: './build/libs/DevOpsUsach2020-0.0.1.jar']],
                mavenCoordinate: [artifactId: 'DevOpsUsach2020', groupId: 'com.devopsusach2020', packaging : 'jar', version: '0.0.1']]]

                        }
	break
        default:
	stage('ERROR'){
             env.TAREA = env.STAGE_NAME
       	echo "valor ${cstage[i]} no valido, opciones permitidas son: build, sonar, run, test y nexus"
	error("valor ${cstage[i]} no valido, opciones permitidas son: build, sonar, run, test y nexus")
			}
        break

	}
	}
}

return this;

