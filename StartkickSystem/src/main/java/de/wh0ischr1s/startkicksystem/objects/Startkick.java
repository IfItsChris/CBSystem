package de.wh0ischr1s.startkicksystem.objects;

import de.wh0ischr1s.startkicksystem.StartkickSystem;
import de.wh0ischr1s.startkicksystem.manager.PlayersFileManager;
import de.wh0ischr1s.startkicksystem.utils.Data;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.UUID;

public class Startkick {

    private Player source;
    private Player target;

    private UUID sourceUUID;
    private UUID targetUUID;

    private int yes = 0;
    private int no = 0;
    private int countdown = 15;

    private BukkitTask task;

    private String reason;

    private ArrayList<Player> voted;

    public Startkick(Player source, Player target) {
        this.source = source;
        this.target = target;
        this.sourceUUID = source.getUniqueId();
        this.targetUUID = target.getUniqueId();
        voted = new ArrayList<>();
    }

    public Startkick setReason(String reason) {
        this.reason = reason;
        return this;
    }

    public Startkick incYes(Player player) {
        yes++;
        return this;
    }

    public Startkick incNo(Player player) {
        no++;
        voted.add(player);
        return this;
    }

    public boolean hasVoted(Player player) {
        return voted.contains(player);
    }

    public boolean willKicked() {
        return yes > no;
    }

    public boolean draw() {
        return yes == no;
    }

    private void broadcastMessage(String[] message) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            for (String s : message) {
                all.sendMessage(s);
            }
        }
    }

    private void broadcastComponent(TextComponent message) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.spigot().sendMessage(message);
        }
    }

    private void broadcastSound(Sound sound) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            all.playSound(all.getLocation(), sound, 1, 0);
        }
    }

    private void startkick() {
        PlayersFileManager manager = StartkickSystem.getInstance().getPlayersFile();
        if (target != null)
            target.kickPlayer("§cDu wurdest aufgrund von einem Startkick 5 Minuten von dem Server ausgeschlossen!");
        manager.addStartkicked(targetUUID.toString());
    }

    public Startkick start() {


        TextComponent jaMessage = new TextComponent(Data.prefix + "§7[§a/ja§7]");
        jaMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/ja"));
        jaMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a/ja").create()));

        TextComponent neinMessage = new TextComponent(Data.prefix + "§7[§c/nein§7]");
        neinMessage.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/nein"));
        neinMessage.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§c/nein").create()));


        broadcastMessage(new String[]{
                " ",
                " ",
                " ",
                "§8__________________________________________________________________",
                " ",
                Data.prefix + "Es wurde eine §cStartkick §7Abstimmung gestartet",
                Data.prefix + "§aVon:       §7" + source.getName(),
                Data.prefix + "§cAn:        §7" + target.getName(),
                Data.prefix + "§8Grund:     §7" + reason,
                Data.prefix + "Ihr habt §a15 Sekunden §7Zeit §7abzustimmen"});
        broadcastComponent(jaMessage);
        broadcastComponent(neinMessage);
        broadcastMessage(new String[]{
                " ",
                "§8__________________________________________________________________",
                " ",
                " ",
                " ",});
        broadcastSound(Sound.ENTITY_LIGHTNING_BOLT_THUNDER);
        task = Bukkit.getScheduler().runTaskTimer(StartkickSystem.getInstance(), new Runnable() {
            @Override
            public void run() {
                if (countdown > 0) {
                    //broadcastMessage("Timer endet in " + countdown + " Sekunden");
                    switch (countdown) {
                        case 15:
                        case 10:
                        case 5:
                        case 3:
                        case 2:
                        case 1:
                            broadcastMessage(new String[]{Data.prefix + "Timer endet in " + countdown + " Sekunde(n)"});
                            broadcastSound(Sound.BLOCK_NOTE_BLOCK_BIT);
                            break;
                    }
                    countdown--;
                } else {
                    //broadcastMessage("Fertig");
                    broadcastMessage(new String[]{
                            " ",
                            " ",
                            " ",
                            "§8__________________________________________________________________",
                            Data.prefix + "Die Abstimmung ist beendet",
                            Data.prefix + "Es haben §a" + yes + " §7 fur §aJa §7und §c" + no + " §7für §c nein §7gestimmt",


                    });

                    if (draw()) {
                        broadcastMessage(new String[]{
                                Data.prefix + "Es ist ein §r unentschieden. §7Der Spieler wurde §cnicht gekickt",
                                "§8__________________________________________________________________",
                                " ",
                                " ",
                                " "
                        });
                    }
                    if (willKicked()) {
                        startkick();
                        broadcastMessage(new String[]{
                                Data.prefix + "Der Spieler wurde für 5 Minuten §aausgeschlossen",
                                "§8__________________________________________________________________",
                                " ",
                                " ",
                                " "
                        });
                    } else {
                        broadcastMessage(new String[]{
                                Data.prefix + "§7Der Spieler wurde §cnicht gekickt",
                                "§8__________________________________________________________________",
                                " ",
                                " ",
                                " "
                        });

                    }
                    Data.startkick = null;
                    stopTask();
                }
            }
        }, 0, 20L);

        return this;
    }

    private void stopTask() {
        task.cancel();
    }

}
