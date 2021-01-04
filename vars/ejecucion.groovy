
def call(){
  
pipeline {
    agent any

        parameters { 
		choice(name: 'herramienta', choices : ['gradle','maven'], description :'')
		string(name: 'stage' , defaultValue: '', description : '')
	}
	
        stages {
                stage('Pipeline') {
                        steps {
                                script {
                                        env.TAREA = ''
                                        env.JOB = 'env.JOB_NAME'
                                        //def cadena = "hola ${params.buildtool}"
                                        //def cadena = "{env.TAREA}"
                                        //def mensajes = "Build status ${buildStatus}: [Alejandro Elgueta] [${params.herramienta}] Ejecucion exitosa"
                                        //def mensajef = "Build status ${buildStatus}: [Alejandro Elgueta] [${params.herramienta}] Ejecucion fallida en stage [${TAREA}]"
                                        echo "buildtool usada :" + params.herramienta
					echo "stage a ejecutar :" + params.stage
                                        if (params.herramienta == 'gradle' ){
                                                //def ejecucion = load 'gradle.groovy'
						if (params.stage ==''){
						string(name: 'stage' , defaultValue: 'buid;sonar;run;test;nexus', description : '')
                                                gradle.call()
						} else {
						gradle.call()
						}

                                        } else {
                                                //def ejecucion = load 'maven.groovy'
						 if (params.stage ==''){
                                                string(name: 'stage' , defaultValue: 'buid;sonar;run;test;nexus', description : '')
                                                maven.call()
						} else {
						maven.call()
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
