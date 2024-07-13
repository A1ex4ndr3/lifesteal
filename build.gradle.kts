plugins {
    java
}

group = "fr.alex4ndr3"
version = "1.1"

repositories {
    mavenCentral()
    mavenLocal()
    maven { url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/") }
    maven { url = uri("https://maven.enginehub.org/repo/") }
    maven { url = uri("https://repo.codemc.org/repository/maven-public/") }
    maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    maven { url = uri("https://ci.athion.net/plugin/repository/everything/") } // FAWE repository
    maven { url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/") } // Sonatype OSS for Adventure dependencies
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.21-R0.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.6")
    implementation(platform("com.intellectualsites.bom:bom-newest:1.46")) // Ref: https://github.com/IntellectualSites/bom implementation(platform("com.intellectualsites.bom:bom-newest:1.46")) // Ref: https://github.com/IntellectualSites/bom
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Core") {
        exclude(group = "com.sk89q.worldedit", module = "worldedit-core")
    }
    compileOnly("com.fastasyncworldedit:FastAsyncWorldEdit-Bukkit") {
        exclude(group = "com.sk89q.worldedit", module = "worldedit-bukkit")
    }
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.11-SNAPSHOT")
    compileOnly("net.kyori:adventure-text-minimessage:4.10.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks.compileJava {
    options.encoding = "UTF-8"
}

tasks.compileTestJava {
    options.encoding = "UTF-8"
}

tasks.register<Copy>("copyJar") {
    dependsOn(tasks.jar)
    from(tasks.jar.get().archiveFile)
    into("./server/plugins")
    doLast {
        println("JAR file copied to ./server/plugins")
    }
}

tasks.named("build") {
    dependsOn(tasks.named("copyJar"))
}
