package net.samagames.hub.bar;

import net.samagames.hub.Hub;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Random;

public class JukeboxMessage extends BarMessage
{
    public JukeboxMessage()
    {
        super("", 8);
    }

    @Override
    public String getMessage()
    {
        if(Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong() != null)
        {
            String message = this.getBaseMessage();

            message = message.replaceAll("&NOTE&", this.randomColor() + "");
            message = message.replaceAll("&TEXT&", this.randomColor() + "");
            message = message.replaceAll("&IMPORTANT&", this.randomColor() + "");

            return message;
        }
        else
        {
            return ChatColor.BOLD + "" + ChatColor.RED + "♫ " + ChatColor.BOLD + ChatColor.YELLOW + "Aucune musique jouée actuellement" + ChatColor.BOLD + "" + ChatColor.RED + " ♪";
        }
    }

    public String getBaseMessage()
    {
        return ChatColor.BOLD + "&NOTE&♫ " + ChatColor.BOLD + "&TEXT&Musique actuelle : " + ChatColor.BOLD + "&IMPORTANT&" + Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong().getSong().getTitle() + ChatColor.BOLD + "&TEXT& proposée par " + ChatColor.BOLD + "&IMPORTANT&" + Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong().getPlayedBy() + ChatColor.BOLD + "&NOTE& ♪";
    }

    public ChatColor randomColor()
    {
        ArrayList<ChatColor> colors = new ArrayList<>();

        for(ChatColor color : ChatColor.values())
            if(!color.isFormat())
                if(color != ChatColor.RESET)
                    colors.add(color);

        return colors.get(new Random().nextInt(colors.size()));
    }
}
