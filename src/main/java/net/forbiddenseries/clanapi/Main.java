package net.forbiddenseries.clanapi;

import java.net.URISyntaxException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main INSTANCE;
    @Override
    public void onEnable() {
        INSTANCE = this;
        FileConfiguration config = getConfig();
        try {
            SocketClient.setupSocketClient(config.getString("url"), config.getString("token"));
        } catch (URISyntaxException e) {
            getLogger().severe("[WebSocket] URL inválida!");
            System.exit(0);
        }
        getLogger().info("Plugin iniciado.");
    }

    @Override
    public void onDisable() {
        getLogger().info("Plugin desligado.");
    }

    public static Main getInstance() {
        return INSTANCE;
    }
}