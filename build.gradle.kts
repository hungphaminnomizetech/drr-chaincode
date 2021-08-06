import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.jetbrains.kotlin.noarg.gradle.NoArgExtension

plugins {
	kotlin("jvm") version "1.5.10"

	// need this to generate no-arg constructors for data classes marked
	// with certain annotations. See NoArgExtension section below for details
	id ("org.jetbrains.kotlin.plugin.noarg") version "1.5.10"

	// need this to create a fat jar, which is used by the JVM running chaincode
	id ("com.github.johnrengelman.shadow") version "7.0.0"
}

// specify the list of annotations, which when marked on data classes
// will signal the kotlin compiler to generate no-arg constructors
configure<NoArgExtension> {
	annotations(listOf("org.hyperledger.fabric.contract.annotation.DataType"))
}

java.sourceCompatibility = JavaVersion.VERSION_11

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven("https://jitpack.io")
}

dependencies {
	implementation( "org.hyperledger.fabric-chaincode-java:fabric-chaincode-shim:2.3.0")
	implementation( "com.fasterxml.jackson.core:jackson-databind:2.11.0")

	//Log4j Kotlin Logging Dependencies
	implementation( "org.apache.logging.log4j:log4j-api-kotlin:1.0.0")
	implementation ("org.apache.logging.log4j:log4j-api:2.11.1")
	implementation ("org.apache.logging.log4j:log4j-core:2.11.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "11"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

// custom task to create the fat jar
tasks {
	named<ShadowJar>("shadowJar") {
		archiveBaseName.set("chaincode")
		archiveClassifier.set("")
		manifest {
			attributes(mapOf("Main-Class" to "org.hyperledger.fabric.contract.ContractRouter"))
		}
	}
}

// dependency graph modification to build the fat jar every time we build this code
tasks {
	build {
		dependsOn(shadowJar)
	}
}