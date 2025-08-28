pipeline {
  agent any

  options {
    timestamps()
    buildDiscarder(logRotator(numToKeepStr: '20'))
    // optional: ansiColor('xterm')
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
    string(name: 'WINDOW_SIZE', defaultValue: '1920,1080', description: '★ Chrome pencere boyutu Genişlik,Yükseklik') // ★
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
base_url=${BASE_URL}
kullanici_adi=${EDEVLET_TC}
sifre=${EDEVLET_SIFRE}
EOF
                echo "configuration.properties oluşturuldu (Linux)."
              '''
            } else {
              // Windows: UTF-8 WITHOUT BOM ile yaz (BOM sorununu önler)
              powershell '''
                $ErrorActionPreference = "Stop"
                $res = "src\\test\\resources"
                if (-not (Test-Path $res)) { New-Item -ItemType Directory -Path $res | Out-Null }
                $cfg = Join-Path $res "configuration.properties"

                $content = @"
base_url=$env:BASE_URL
kullanici_adi=$env:EDEVLET_TC
sifre=$env:EDEVLET_SIFRE
"@

                $utf8NoBom = New-Object System.Text.UTF8Encoding($false)
                [System.IO.File]::WriteAllText($cfg, $content, $utf8NoBom)

                Write-Host "configuration.properties oluşturuldu (Windows): $cfg (Boyut: $((Get-Item $cfg).Length) bytes)"
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
          args += "-DbaseUrl=${params.BASE_URL}"
          args += "-DwindowSize=${params.WINDOW_SIZE}"            // ★ yeni parametre Maven'a iletiliyor
          args += "-Dci=true"                                     // ★ opsiyonel: kodda CI’ye özel davranış için

          if (isUnix()) {
            sh  "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          } else {
            bat "mvn -U -B clean test -Dfile.encoding=UTF-8 ${args.join(' ')}"
          }
        }
      }
    }

    stage('Archive Artifacts') {
      steps {
        archiveArtifacts artifacts: '**/target/surefire-reports/*.xml, **/target/xml-report/*.xml, **/target/allure-results/**, logs/**/*, **/target/screenshots/**/*, **/target/*.log, **/target/ci-dump/**/*',
                         fingerprint: true, onlyIfSuccessful: false
      }
    }
  }

  post {
    // All stages bittikten sonra (başarılı/başarısız fark etmez) Allure raporunu yayınla,
    // JUnit sonuçlarını yükle ve workspace'i temizle.
    always {
      script {
        // Muhtemel dizinleri topla (tek/modül çoklu projeler için)
        def results = []
        if (fileExists('target/allure-results')) { results << [path: 'target/allure-results'] }
        if (fileExists('allure-results'))        { results << [path: 'allure-results'] }
        def hits = findFiles(glob: '**/target/allure-results')
        if (hits) { hits.each { results << [path: it.path] } }

        // Tekrarsızlaştır
        results = results.unique { it.path }

        if (results && results.size() > 0) {
          // Eğer Tools altında Allure’ü “Allure” adıyla eklediysen:
          allure commandline: 'Allure', includeProperties: false, results: results, reportBuildPolicy: 'ALWAYS'
          // Tek kurulum varsa 'commandline' parametresi olmadan da çalışır:
          // allure includeProperties: false, results: results, reportBuildPolicy: 'ALWAYS'
        } else {
          echo 'Allure: allure-results bulunamadı.'
        }
      }

      junit testResults: 'target/surefire-reports/*.xml, target/xml-report/*.xml', allowEmptyResults: false
      cleanWs()
    }
  }
}
