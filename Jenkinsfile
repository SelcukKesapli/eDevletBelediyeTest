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
    string(name: 'BASE_URL', defaultValue: 'https://www.turkiye.gov.tr/', description: 'Ana URL')
  }

  environment {
    MAVEN_OPTS = '-Dmaven.test.failure.ignore=false'
  }

  stages {

    stage('Checkout') {
      steps { checkout scm }
    }

    stage('Config Dosyası Oluştur') {
      steps {
        withCredentials([
          string(credentialsId: 'EDEVLET_TC',    variable: 'EDEVLET_TC'),
          string(credentialsId: 'EDEVLET_SIFRE', variable: 'EDEVLET_SIFRE')
        ]) {
          script {
            if (isUnix()) {
              sh '''
                set -e
                mkdir -p src/test/resources
                cat > src/test/resources/configuration.properties <<EOF
kullanici_adi=${EDEVLET_TC}
sifre=${EDEVLET_SIFRE}
EOF
                echo "configuration.properties oluşturuldu."
              '''
            } else {
              powershell '''
                $ErrorActionPreference = "Stop"
                $res = "src\\test\\resources"
                if (-not (Test-Path $res)) { New-Item -ItemType Directory -Path $res | Out-Null }
                $cfg = Join-Path $res "configuration.properties"
@"
kullanici_adi=$env:EDEVLET_TC
sifre=$env:EDEVLET_SIFRE
"@ | Out-File -FilePath $cfg -Encoding UTF8 -Force
                Write-Host "configuration.properties oluşturuldu: $cfg (Boyut: $((Get-Item $cfg).Length) bytes)"
              '''
            }
          }
        }
      }
    }

    stage('Build & Test') {
      steps {
        script {
          def args = []
          if (params.TAGS?.trim()) { args += "-Dcucumber.filter.tags=${params.TAGS}" }
          args += "-Dbrowser=${params.BROWSER}"
          args += "-Dheadless=${params.HEADLESS}"
          args += "-DbaseUrl=${params.BASE_URL}"   // TEK KAYNAK

          if (isUnix()) {
            sh "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          } else {
            bat "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          }
        }
      }
    }

    stage('Allure Report') {
      when { expression { currentBuild.currentResult == 'SUCCESS' } }
      steps {
        script {
          def hits = findFiles(glob: '**/target/allure-results')
          if (hits) {
            def inputs = hits.collect { [path: it.path] }
            allure includeProperties: false, results: inputs
          } else {
            echo 'Allure: allure-results bulunamadı.'
          }
        }
      }
    }

    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: '**/target/surefire-reports/*.xml, **/target/xml-report/*.xml, logs/**/*, **/target/screenshots/**/*, **/target/*.log',
                         fingerprint: true, onlyIfSuccessful: false
      }
    }
  }

  post {
    always {
      // Test sonuçlarını her durumda yayınla (başarısızlıkta bile)
      junit testResults: 'target/surefire-reports/*.xml, target/xml-report/*.xml', allowEmptyResults: false
      cleanWs()
    }
  }
}
