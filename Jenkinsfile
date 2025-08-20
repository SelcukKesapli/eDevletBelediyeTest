pipeline {
  agent any
  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '20'))
  }
  tools {
    jdk 'JDK17'
    maven 'Maven 3'
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
            sh "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          } else {
            bat "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          }
        }
      }
      post {
        always {
          // Surefire + Runner junit raporları
          junit testResults: 'target/surefire-reports/*.xml, target/xml-report/*.xml', allowEmptyResults: false
        }
      }
    }

    stage('Allure Report') {
      steps {
        script {
          // workspace altında tüm allure-results klasörlerini bul
          def hits = findFiles(glob: '**/target/allure-results')
          if (hits && hits.size() > 0) {
            def inputs = hits.collect { [path: it.path] }
            allure includeProperties: false, results: inputs
          } else {
            echo 'Allure: allure-results bulunamadı, rapor üretilmedi.'
          }
        }
      }
    }

    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: '**/target/surefire-reports/*.xml, **/target/xml-report/*.xml, logs/**/*, **/target/screenshots/**/*, **/target/*.log', fingerprint: true, onlyIfSuccessful: false
      }
    }
  }

  post {
    always {
      cleanWs()
    }
  }
}
