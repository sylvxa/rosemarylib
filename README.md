# Rosemary

![A re-tinted Minecraft rose bush](src/main/resources/assets/rosemarylib/icon.png)

Rosemary is a simple server-side library used for some of my mods. It currently adds:

- Utility functions for `SavedData`
- Dialog GUIs
- Simple serialization based config

## Usage

See `src/testmod` for examples on how features are used

Add the maven repository and implementation

I ask that you don't JiJ/include this library in your mod unless you have a good reason to do so. Point users to the [Modrinth](https://modrinth.com/mod/rosemarylib) page (or atleast add it as a dependency)

```
repositories {
    maven {
        name "sylvielol"
        url "https://maven.sylvie.lol/releases"
    }
}

dependencies {
    // Versions can be found at https://maven.sylvie.lol/#/releases/lol/sylvie/rosemarylib or on Modrinth
    implementation "lol.sylvie:rosemarylib:1.1.0-mc26.1"
}
```