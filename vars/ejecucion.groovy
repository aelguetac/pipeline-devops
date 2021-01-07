
def call(){
  
pipeline {
    agent any

        parameters {
		choice(name: 'herramienta', choices : ['gradle','maven'], description :'')
		choice(name: 'cicd', choices : ['ci','cd'], description :'')
		//message { se deben ingresar uno o mas de estos stage: buid;sonar;run;test;nexus, si no se ingresa ningun stage se ejecutaran todos }
	//	string(name: 'stage' , defaultValue: '', description : '')
	}
	
        stages {
                stage('Pipeline') {
                        steps {
                                script {
					bat 'set'
                                        env.TAREA = ''
                                        env.JOB = 'env.JOB_NAME'
                                        //def cadena = "hola ${params.buildtool}"
                                        echo "buildtool usada :" + params.herramienta
					echo "integracion o entrega continua :" + params.cicd
                                        if (params.herramienta == 'gradle' ){
						figlet params.herramienta
						//if (params.cicd == ci || findText(textFinders: [textFinder(regexp: '*feature*|*develop*', fileset : "env.GIT_BRANCH", alsoCheckConsoleOutput: true)])){
						if (params.cicd == 'ci' || env.GIT_BRANCH == "*feature*" || env.GIT_BRANCH == "*develop" ){
						figlet params.cicd
						cig.call()
						} else {
						echo " se usa CD"
						figlet params.cicd
                                                cdg.call()
					//	} else {
					//	gradle.call()
						}

                                        } else {
                                                //def ejecucion = load 'maven.groovy'
						figlet params.herramienta
						if (params.cicd == 'ci' || env.GIT_BRANCH == "*feature*" || env.GIT_BRANCH == "*develop" ){
						figlet params.cicd
						cim.call()
						} else {
						echo "se usa CD"
						figlet params.cicd
                                                cdm.call()
					//	} else {
					//	maven.call()
						}
                                        }

                        }
                        }
                }
        }
                post {

                        success {
                                //println env.TAREA
                                //println env.JOB_NAME
                                //println params.buildtool

                                //def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
                                //steps {
                                //      script {
                        //      mensajes = "Build status ${buildStatus}: [Alejandro Elgueta] [${params.herramienta}] Ejecucion exitosa"
//      Build Success: [Nombre Alumno][Nombre Job][buildTool] Ejecución exitosa.
//      Build Failure: [Nombre Alumno][Nombre Job][buildTool] Ejecución fallida en stage [Stage]
        slackSend color: 'good', message: "Build status SUCCESS: [Alejandro Elgueta] [${env.JOB_NAME}}] [${params.herramienta}] Ejecucion exitosa\nURL : [${env.JENKINS_URL}/job/${env.JOB_NAME}]", teamDomain: 'devops-usach-2020', tokenCredentialId: 'slack'
                                //slackSend color: 'good', message: "${mensajes}", teamDomain: 'devops-usach-2020', tokenCredentialId: 'slack'
                        //}}
                        }
                        failure {
                        //        steps {
                        //        script {
        slackSend color: 'danger', message: "Build status FAILURE: [Alejandro Elgueta] [${env.JOB_NAME}] [${params.herramienta}] Ejecucion fallida en stage [${TAREA}]\nURL : [${env.JENKINS_URL}/job/${env.JOB_NAME}]", teamDomain: 'devops-usach-2020', tokenCredentialId: 'slack'
                        //      mensajef = "Build status ${buildStatus}: [Alejandro Elgueta] [${env.JOB_NAME}] [${params.herramienta}] Ejecucion fallida en stage [${TAREA}]"
                                //println env.TAREA." "env.JOB_NAME
                        //      slackSend color: 'danger', message: "${mensajef}", teamDomain: 'devops-usach-2020', tokenCredentialId: 'slack'
                        //      }}
                        }
                }


}

}

return this;
