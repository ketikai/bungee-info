val minecraftVersion = "1.12.2"

group = "${rootProject.group}.bungee"

repositories {
    maven {
        name = "spigotmc-public"
        url = uri("https://hub.spigotmc.org/nexus/content/groups/public/")
    }
    maven {
        name = "spigotmc-snapshots"
        url = uri("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    }
}

dependencies {
    compileOnly("net.md-5:bungeecord-api:1.12-SNAPSHOT")

    shadow(project(":modules:${rootProject.name}-tags"))
    shadow(project(":modules:${rootProject.name}-protocol")) {
        isTransitive = false
    }
}

tasks.processResources {
    includeEmptyDirs = false
    val props = mapOf(
        "name" to rootProject.name,
        "version" to version,
    )
    filesMatching(listOf("plugin.yml")) {
        expand(props)
    }
}