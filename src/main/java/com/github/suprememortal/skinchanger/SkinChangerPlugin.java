package com.github.suprememortal.skinchanger;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.network.protocol.PlayerSkinPacket;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.service.ServicePriority;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class SkinChangerPlugin extends PluginBase implements Listener, SkinChanger {
    private final Map<UUID, Skin> originalSkins = new HashMap<>();
    private Path pluginDir;

    @Override
    public void onEnable() {
        pluginDir = getDataFolder().toPath();

        if (Files.notExists(pluginDir)) {
            try {
                Files.createDirectories(pluginDir);
            } catch (IOException e) {
                throw new AssertionError("Could not create directory");
            }
        }

        getServer().getPluginManager().registerEvents(this, this);

        getServer().getCommandMap().register("skinchanger", new SkinChangerCommand(this));
        getServer().getCommandMap().register("skinchanger", new ResetSkinCommand(this));

        getServer().getServiceManager().register(SkinChanger.class, this, this, ServicePriority.HIGHEST);
    }

    @Override
    public void changeSkin(Player player, String name) throws SkinChangeException {
        Skin skin = new Skin();

        Path skinFolderPath = pluginDir.resolve(name);
        Path skinGeometryPath = skinFolderPath.resolve("geometry.json");
        Path skinPath = skinFolderPath.resolve("skin.png");

        if (Files.notExists(skinFolderPath) || !Files.isDirectory(skinFolderPath) ||
                Files.notExists(skinGeometryPath) || !Files.isRegularFile(skinGeometryPath) ||
                Files.notExists(skinPath) || !Files.isRegularFile(skinPath)) {
            throw new SkinChangeException("Skin does not exist");
        }

        String geometry;
        BufferedImage skinData;
        try {
            geometry = new String(Files.readAllBytes(skinGeometryPath), StandardCharsets.UTF_8);
            skinData = ImageIO.read(skinPath.toFile());
        } catch (IOException e) {
            throw new SkinChangeException("Error loading data", e);
        }

        skin.setGeometryData(geometry);
        skin.setGeometryName("geometry." + name);
        skin.setSkinData(skinData);
        skin.setSkinId(name);
        skin.setPremium(true);

        Skin oldSkin = player.getSkin();

        player.setSkin(skin);

        PlayerSkinPacket packet = new PlayerSkinPacket();
        packet.skin = skin;
        packet.newSkinName = name;
        packet.oldSkinName = oldSkin.getSkinId();
        packet.uuid = player.getUniqueId();

        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), packet);
    }

    private static final DirectoriesFilter DIRECTORIES_FILTER = new DirectoriesFilter();

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onLogin(PlayerLoginEvent event) {
        originalSkins.put(event.getPlayer().getUniqueId(), event.getPlayer().getSkin());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        originalSkins.remove(event.getPlayer().getUniqueId());
    }

    @Override
    public boolean resetSkin(Player player) {
        Skin skin = originalSkins.get(player.getUniqueId());
        if (skin != null) {
            Skin oldSkin = player.getSkin();
            player.setSkin(skin);

            PlayerSkinPacket packet = new PlayerSkinPacket();
            packet.skin = skin;
            packet.newSkinName = skin.getSkinId();
            packet.oldSkinName = oldSkin.getSkinId();
            packet.uuid = player.getUniqueId();

            Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), packet);
            return true;
        }
        return false;
    }

    public String[] getAvailableSkins() {
        List<String> skins = new ArrayList<>();
        try (DirectoryStream<Path> ds = Files.newDirectoryStream(this.pluginDir, DIRECTORIES_FILTER)) {
            for (Path path : ds) {
                skins.add(path.getFileName().toString());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return skins.toArray(new String[0]);
    }

    private static class DirectoriesFilter implements DirectoryStream.Filter<Path> {
        @Override
        public boolean accept(Path entry) throws IOException {
            return Files.isDirectory(entry);
        }
    }
}
