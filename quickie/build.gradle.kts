plugins {
  id(Plugins.Android.library)
  id(Plugins.Kotlin.android)
  id(Plugins.Kotlin.parcelize)
  id(Plugins.Kotlin.dokka) version Versions.dokka
  `maven-publish`
  signing
}

android {
  compileSdkVersion(Versions.androidCompileSdk)
  defaultConfig.minSdkVersion(Versions.androidMinSdk)
  resourcePrefix = project.name
  buildFeatures {
    viewBinding = true
    buildConfig = false
  }
  flavorDimensions("mlkit")
  productFlavors {
    create("bundled").dimension("mlkit")
    create("unbundled").dimension("mlkit")
  }
  sourceSets {
    getByName("main").java.srcDirs("src/main/kotlin")
    getByName("bundled").java.srcDirs("src/bundled/kotlin")
    getByName("unbundled").java.srcDirs("src/unbundled/kotlin")
  }
}

repositories {
  google()
  mavenCentral()
  jcenter {
    content {
      includeGroupByRegex("org\\.jetbrains.*")
    }
  }
}

val bundledImplementation by configurations
val unbundledImplementation by configurations
dependencies {
  implementation(Deps.AndroidX.activity)
  implementation(Deps.AndroidX.fragment)
  implementation(Deps.AndroidX.appcompat)
  implementation(Deps.AndroidX.core)

  implementation(Deps.AndroidX.camera)
  implementation(Deps.AndroidX.cameraLifecycle)
  implementation(Deps.AndroidX.cameraPreview)

  bundledImplementation(Deps.MLKit.barcodeScanning)
  unbundledImplementation(Deps.MLKit.barcodeScanningGms)
}

group = "io.github.g00fy2.quickie"
version = "0.7.0"

tasks.register<Jar>("androidJavadocJar") {
  archiveClassifier.set("javadoc")
  from("$buildDir/dokka/javadoc")
  dependsOn("dokkaJavadoc")
}

tasks.register<Jar>("androidSourcesJar") {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
  publishing {
    publications {
      create<MavenPublication>("bundledRelease") {
        from(components["bundledRelease"])
        commonConfig("quickie-bundled")
      }
      create<MavenPublication>("unbundledRelease") {
        from(components["unbundledRelease"])
        commonConfig("quickie-unbundled")
      }
    }
    repositories {
      maven {
        name = "sonatype"
        url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
        credentials {
          username = project.findStringProperty("sonatypeUsername")
          password = project.findStringProperty("sonatypePassword")
        }
      }
    }
  }
}

signing {
  project.findStringProperty("signing.keyId")
  project.findStringProperty("signing.password")
  project.findStringProperty("signing.secretKeyRingFile")
  sign(publishing.publications)
}

fun MavenPublication.commonConfig(artifactName: String) {
  artifactId = artifactName
  artifact(tasks.named("androidJavadocJar"))
  artifact(tasks.named("androidSourcesJar"))
  pom {
    name.set(artifactName)
    description.set("Android QR code scanner library")
    url.set("https://github.com/G00fY2/Quickie")
    licenses {
      license {
        name.set("MIT License")
        url.set("https://opensource.org/licenses/MIT")
      }
    }
    developers {
      developer {
        id.set("g00fy2")
        name.set("Thomas Wirth")
        email.set("twirth.development@gmail.com")
      }
    }
    scm {
      connection.set("https://github.com/G00fY2/Quickie.git")
      developerConnection.set("https://github.com/G00fY2/Quickie.git")
      url.set("https://github.com/G00fY2/Quickie")
    }
  }
}

fun Project.findStringProperty(propertyName: String): String? {
  return findProperty(propertyName) as String? ?: {
    logger.error("$propertyName missing in gradle.properties")
    null
  }()
}