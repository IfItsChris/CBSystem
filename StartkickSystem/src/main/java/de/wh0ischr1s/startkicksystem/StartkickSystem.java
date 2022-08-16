package de.wh0ischr1s.startkicksystem;

import de.wh0ischr1s.startkicksystem.commands.JaCommand;
import de.wh0ischr1s.startkicksystem.commands.NeinCommand;
import de.wh0ischr1s.startkicksystem.commands.StartkickCommand;
import de.wh0ischr1s.startkicksystem.listener.PlayerPreLoginListener;
import de.wh0ischr1s.startkicksystem.manager.PlayersFileManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class StartkickSystem extends JavaPlugin {

    private static StartkickSystem instance;
    private PlayersFileManager playersFileManager;

    @Override
    public void onEnable() {
        instance = this;
        playersFileManager = new PlayersFileManager("players").load();
        registerConfig();
        registerCommands();
        registerListener();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void registerConfig() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }

    public static StartkickSystem getInstance() {
        return instance;
    }

    public PlayersFileManager getPlayersFile() {
        return playersFileManager;
    }

    private void registerCommands() {
        getCommand("startkick").setExecutor(new StartkickCommand());
        getCommand("ja").setExecutor(new JaCommand());
        getCommand("nein").setExecutor(new NeinCommand());
    }

    private void registerListener() {
        new PlayerPreLoginListener();
    }



}
