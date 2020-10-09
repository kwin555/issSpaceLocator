plugins {
  java
  jacoco
  pmd
  application
}

application {
    mainClassName = "locator.ISSInit"
}

repositories {
    mavenCentral()
    maven ( url = "<https://repo1.maven.org/maven2/>")
}


dependencies {
  testCompile("org.junit.jupiter:junit-jupiter-api:5.2.0")
  testRuntime("org.junit.jupiter:junit-jupiter-engine:5.2.0")
  testRuntime("org.junit.platform:junit-platform-console:1.2.0")
  testImplementation("org.hamcrest:hamcrest:2.1")
  compile(group = "net.iakovlev", name = "timeshape", version = "2018d.6")
  compile(group = "com.google.code.geocoder-java", name = "geocoder-java", version = "0.16")
  compile(group = "org.json", name = "json", version = "20190722")
  compile(group = "org.slf4j", name = "slf4j-jdk14", version = "1.7.25")
  compile(group = "com.fasterxml.jackson.core", name = "jackson-databind", version="2.10.0.pr3")
  compile(group = "com.google.code.gson", name = "gson", version = "2.8.5")
  testCompile(group = "org.mockito", name = "mockito-core", version = "2.1.0")
}

 
sourceSets {
  main {
    java.srcDirs("ISSLocator/src")
  }
  test {
    java.srcDirs("ISSLocator/tests")
  }
}

tasks {
    val treatWarningsAsError =
            listOf("-Xlint:unchecked", "-Xlint:deprecation", "-Werror")

    getByName<JavaCompile>("compileJava") {
        options.compilerArgs = treatWarningsAsError
    }

    getByName<JavaCompile>("compileTestJava") {
        options.compilerArgs = treatWarningsAsError
    }

    getByName<JacocoReport>("jacocoTestReport") {
        afterEvaluate {
            setClassDirectories(files(classDirectories.files.map {
                fileTree(it) { exclude("**/**/ISSInit.java")}
            }))
        }
    }


}

val test by tasks.getting(Test::class) {
	useJUnitPlatform {}
}

pmd {
    ruleSets = listOf()
    ruleSetFiles = files("../conf/pmd/ruleset.xml")
}                                                
 
defaultTasks("clean", "test", "jacocoTestReport", "pmdMain")