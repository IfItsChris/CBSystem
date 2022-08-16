package de.wh0ischr1s.startkicksystem.commands;

import de.wh0ischr1s.startkicksystem.objects.Startkick;
import de.wh0ischr1s.startkicksystem.utils.Data;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StartkickCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(Data.startkick != null) {
                player.sendMessage(Data.prefix + "Es läuft momentan noch ein Startkick. Bitte warte bis dieser vorbei ist.");
                return false;
            }

            if(args.length < 2) {
                player.sendMessage(Data.prefix + "§c/startkick <Spieler> <Grund>");
            }

            if(args.length >= 2) {

                player.sendMessage(String.valueOf(args.length));

                player.sendMessage(Data.prefix + "Startkick startet");

                Player target = Bukkit.getPlayer(args[0]);

                if(target != null) {

                    StringBuilder reason = new StringBuilder();

                    for(int i = 1; i < args.length; i++) {
                        reason.append(args[i]).append(" ");
                    }

                    Data.startkick = new Startkick(player, target).setReason(reason.toString()).start();
                } else {
                    player.sendMessage(Data.prefix + "§cDieser Spieler ist nicht online.");
                    return false;
                }
            }

        }

        return false;
    }
}
