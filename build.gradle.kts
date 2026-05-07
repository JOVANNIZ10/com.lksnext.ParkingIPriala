plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.google.services) apply false
    id("org.sonarqube") version "7.3.0.8198"
}

sonar {
    properties {
        property("sonar.projectKey", "JOVANNIZ10_com.lksnext.ParkingIPriala")
        property("sonar.organization", "jovanniz10")
        property("sonar.host.url", "https://sonarcloud.io")
    }
}