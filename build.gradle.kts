import org.gradle.jvm.tasks.Jar
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  kotlin("jvm") version "1.4.32"
  id("org.jetbrains.compose") version "0.4.0-build182"
}

repositories {
  mavenCentral()
  maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
  api(compose.materialIconsExtended)
  implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile> {
  kotlinOptions.jvmTarget = "11"
  kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
}

val main = "com.squareup.dashboard.MainKt"

compose.desktop {
  application {
    mainClass = main
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "dashboard"
      packageVersion = "1.0.0"
    }
  }
}

tasks {
  register("fatJar", Jar::class.java) {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    manifest {
      attributes("Main-Class" to main)
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    from(sourceSets.main.get().output)
  }
}

tasks {
  "build" {
    dependsOn("fatJar")
  }
}
