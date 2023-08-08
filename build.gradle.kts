/**
 * Only using buildscript here so we can handle branching logic of TakDev plugin coordinates
 */
buildscript {
  dependencies {
    val isPipeline = properties["takrepo.url"] != null
    if (isPipeline) {
      classpath("com.atakmap.gradle:atak-gradle-takdev:2.+")
    } else {
      val takDevVersion = libs.versions.takdev.get()
      classpath("com.github.jonapoul:atak-gradle-takdev:$takDevVersion")
    }
  }
}

plugins {
  alias(libs.plugins.agp.app) apply false
  alias(libs.plugins.agp.lib) apply false
  alias(libs.plugins.detekt) apply false
  alias(libs.plugins.kotlin.android) apply false
  alias(libs.plugins.kotlin.serialization) apply false
  alias(libs.plugins.ktlint) apply false
  alias(libs.plugins.kover) apply false
  alias(libs.plugins.licensee) apply false
  alias(libs.plugins.spotless) apply false

  alias(libs.plugins.doctor) // configured below

  id("convention-properties")
}

configurations.all {
  resolutionStrategy {
    dependencySubstitution {
      substitute(module("net.sf.proguard:proguard-gradle"))
        .using(module(libs.plugin.proguard.get().toString()))
    }
  }
}

extra.apply {
  val urlKey = "takrepo.url"
  set("isDevKitEnabled", { project.getProperty(urlKey, null) != null })
  set("takrepoUrl", project.getProperty(urlKey, "http://localhost/"))
  set("takrepoUser", project.getProperty("takrepo.user", "invalid"))
  set("takrepoPassword", project.getProperty("takrepo.password", "invalid"))
  set("takdevPlugin", project.getProperty("takrepo.plugin", "${rootDir}/../../atak-gradle-takdev.jar"))
  set("takdevConTestEnable", false)
  set("ATAK_VERSION", libs.versions.atak.get())
  set("PLUGIN_VERSION", libs.versions.pluginVersionName.get())
}

doctor {
  javaHome {
    ensureJavaHomeMatches.set(false)
    ensureJavaHomeIsSet.set(true)
    failOnError.set(true)
  }
}
