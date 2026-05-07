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
        property("sonar.sources", "app/src/main/java,app/src/main/kotlin")
        property("sonar.tests", "app/src/test/java,app/src/test/kotlin")
        property("sonar.java.binaries", "app/build/intermediates/classes/debug")
        property("sonar.kotlin.binaries", "app/build/intermediates/classes/debug")
        property("sonar.android.lint.report", "app/build/reports/lint-results-debug.xml")
        property("sonar.junit.reportPaths", "app/build/test-results/debugUnitTest/")
        property("sonar.coverage.jacoco.xmlReportPaths", "app/build/reports/jacoco/debug/jacoco.xml")
    }
}