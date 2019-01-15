# SkinChanger

## Usage

### Adding Skins

Create a folder insider the `SkinChanger` plugin folder with the name of your skin e.g. `my.custom.skin`.

Add your `geometry.json` and `skin.png` to the folder making sure that the geometry name is the same your chosen skin name.

```json
{
  "my.custom.skin": {
    "bones": [
      {
        "name": "body",
        "pivot": [ 0.0, 24.0, 0.0 ],
        "cubes": [
          {
            "origin": [ -4.0, 12.0, -2.0 ],
            "size": [ 8, 12, 4 ],
            "uv": [ 16, 16 ]
          }
        ]
      }
    ]
  }
}
```


### Changing Skins

```java
    public void onEnable() {
        RegisteredServiceProvider<SkinChanger> provider = getServer().getServiceManager().getProvider(SkinChanger.class);
        
        if (provider == null || provider.getProvider() == null) {
            this.getServer().getPluginManager().disablePlugin(this);
        }
        
        SkinChanger skinChanger = provider.getProvider();
    }
```
```java
    skinChanger.changeSkin(player, "my.custom.skin")
```
```java
    skinChanger.resetSkin(player);
```

## Dependency

### Maven
```xml
    <repositories>
        <repository>
            <id>nukkitx-repo</id>
            <url>https://repo.nukkitx.com/snapshot/</url>
        </repository>
    </repositories>

    <dependencies>
        <dependency>
            <groupId>com.github.suprememortal</groupId>
            <artifactId>skinchanger</artifactId>
            <version>1.0.0-SNAPSHOT</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>
```