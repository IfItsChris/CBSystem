package de.wh0ischr1s.startkicksystem.listener;

import de.wh0ischr1s.startkicksystem.StartkickSystem;
import de.wh0ischr1s.startkicksystem.manager.PlayersFileManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

public class PlayerPreLoginListener implements Listener {

    private PlayersFileManager manager;

    public PlayerPreLoginListener() {
        manager = StartkickSystem.getInstance().getPlayersFile();
        StartkickSystem.getInstance().getServer().getPluginManager().registerEvents(this, StartkickSystem.getInstance());
    }

    @EventHandler
    public void onPreLogin(AsyncPlayerPreLoginEvent event) {


        String uuid = event.getUniqueId().toString();

        if (manager.isStartkicked(uuid)) {
            long duration = manager.getStartkicked().get(uuid);
            long current = System.currentTimeMillis();

            if (current < duration) {
                event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, "§cDu bist noch aufgrund eines Startkicks gebannt.\nBitte versuche es später nochmal.");
            } else {
                manager.remove(uuid);
            }

        }

    }

}
