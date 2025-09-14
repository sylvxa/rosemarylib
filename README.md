# Rosemary

![A re-tinted Minecraft rose bush](src/main/resources/assets/rosemarylib/icon.png)

Rosemary is a simple server-side library used for some of my mods. It currently adds:

- Utility functions for Persistent State
- Dialog GUIs
- Simple serialization based config

## Usage

See the help article for [Modrinth Maven](https://support.modrinth.com/en/articles/8801191-modrinth-maven)

```
repositories {
    exclusiveContent {
        forRepository {
            maven {
                name = "Modrinth"
                url = "https://api.modrinth.com/maven"
            }
        }
        
        filter {
            includeGroup "maven.modrinth"
        }
    }
}

dependencies {
    modImplementation "maven.modrinth:rosemarylib:1.0.0"
}
```