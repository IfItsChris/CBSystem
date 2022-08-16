package de.wh0ischr1s.startkicksystem.commands;

import de.wh0ischr1s.startkicksystem.utils.Data;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;

            if(Data.startkick != null) {
                if(!Data.startkick.hasVoted(player))  {
                    Data.startkick.incYes(player);
                    player.sendMessage(Data.prefix + "Du hast erfolgreich für §aJa §7gestimmt!");
                } else {
                    player.sendMessage(Data.prefix + "§cDu hast bereits abgestimmt!");
                }
            } else {
                player.sendMessage(Data.prefix + "§cEs läuft momentan kein Startkick!");
            }



        }

        return false;
    }
}
