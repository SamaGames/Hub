package net.samagames.hub.common.receivers;

import net.samagames.api.pubsub.IPacketsReceiver;
import net.samagames.hub.Hub;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 08/02/2017
 */
public class EventListener implements IPacketsReceiver
{
    private final Hub hub;

    public EventListener(Hub hub)
    {
        this.hub = hub;
    }

    @Override
    public void receive(String channel, String packet)
    {
        String[] data = packet.split(":");

        String gameCodeName = data[0];
        String template = data[1];
        int coins = Integer.parseInt(data[2]);
        int pearls = Integer.parseInt(data[3]);

        FancyMessage message = new FancyMessage("[Événement] ").color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD)
                .then("Une animation a débuté " + (gameCodeName.equals("hub") ? "sur le " : "en ")).color(ChatColor.LIGHT_PURPLE)
                .then(gameCodeName.equals("hub") ? "Hub " + template : this.hub.getGameManager().getGameByIdentifier(gameCodeName).getName()).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD)
                .then(". Récompenses : ").color(ChatColor.LIGHT_PURPLE);

        if (coins > 0)
            message.then(coins + " pièce" + (coins > 1 ? "s" : "")).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

        if (coins > 0 && pearls > 0)
            message.then(" et ").color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

        if (pearls > 0)
            message.then(pearls + " perle" + (pearls > 1 ? "s" : "")).color(ChatColor.DARK_PURPLE).style(ChatColor.BOLD);

        message.then(". ").color(ChatColor.LIGHT_PURPLE);
        message.then("[Cliquez ici]").color(ChatColor.AQUA).style(ChatColor.BOLD).command(gameCodeName.equals("hub") ? "/hub " + template : "/join " + gameCodeName + " " + template).tooltip(ChatColor.GOLD + "» Clic pour rejoindre");

        this.hub.getServer().getOnlinePlayers().forEach(player ->
        {
            player.sendMessage("");
            message.send(player);
            player.sendMessage("");
        });
    }
}
