pipeline {
  agent any
  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }
  tools {
    jdk 'JDK17'       // Manage Jenkins > Tools'ta verdiğin ad
    maven 'Maven 3'   // Manage Jenkins > Tools'ta verdiğin ad
  }
  parameters {
    choice(name: 'BROWSER', choices: ['chrome','firefox'], description: 'Tarayıcı seç')
    booleanParam(name: 'HEADLESS', defaultValue: true, description: 'Headless çalıştır')
    string(name: 'TAGS', defaultValue: '', description: 'Cucumber @tag filtresi (boş=hepsi)')
  }
  environment {
    MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
  }

  stages {
    stage('Checkout') {
      steps {
        checkout scm
      }
    }

    stage('Build & Test') {
      steps {
        script {
          def args = []
          if (params.TAGS?.trim()) { args += "-Dcucumber.filter.tags=${params.TAGS}" }
          args += "-Dbrowser=${params.BROWSER}"
          args += "-Dheadless=${params.HEADLESS}"

          if (isUnix()) {
            sh "mvn -q -U clean test ${args.join(' ')}"
          } else {
            bat "mvn -q -U clean test ${args.join(' ')}"
          }
        }
      }
      post {
        always {
          // Test raporlarını Jenkins'e yükle
          junit '**/target/surefire-reports/*.xml'
          junit '**/target/failsafe-reports/*.xml'
        }
      }
    }

    stage('Allure Report') {
      when { expression { fileExists('target/allure-results') } }
      steps {
        allure includeProperties: false, results: [[path: 'target/allure-results']]
      }
    }

    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: 'target/**/*.jar, target/**/*.zip', fingerprint: true
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
