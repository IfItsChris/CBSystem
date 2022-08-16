package de.wh0ischr1s.startkicksystem.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PlayersFileManager {



    private String filename;
    private File file;
    private FileConfiguration configuration;
    private HashMap<String, Long> startkicked;

    public PlayersFileManager(String filename) {
        this.filename = filename;
    }

    public void save() {
        try {
            configuration.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public PlayersFileManager load() {
        file = new File("plugins//StartkickSystem//" + filename + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        configuration = YamlConfiguration.loadConfiguration(file);
        if(!configuration.isSet("startkicked")) {
            startkicked = new HashMap<>();
            configuration.set("startkicked", new ArrayList<>());
            save();
        } else {
            List<String> startkickedList = configuration.getStringList("startkicked");
            for(String uuid : startkickedList) {
                startkicked.put(uuid, configuration.getLong(uuid + ".end"));
            }
        }
        return this;
    }

    public FileConfiguration getConfig() {
        return configuration;
    }

    public HashMap<String, Long> getStartkicked() {
        return startkicked;
    }

    public boolean isStartkicked(String uuid) {
        return startkicked.containsKey(uuid);
    }

    public void remove(String uuid) {
        startkicked.remove(uuid);
        configuration.set("startkicked", new ArrayList<>(startkicked.keySet()));
        save();
        configuration.set(uuid, null);
        save();
    }

    public void addStartkicked(String uuid) {
        long duration = System.currentTimeMillis() + (1000*60*5);
        startkicked.put(uuid, duration);
        configuration.set("startkicked", new ArrayList<>(startkicked.keySet()));
        save();
        configuration.set(uuid + ".end", duration);
        save();
    }

}
