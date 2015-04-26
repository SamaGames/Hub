package net.samagames.hub.games.sign;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.signs.SignData;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.FireworkUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

public class GameSign
{
    private final Sign sign;
    private SignData lastData;
    private boolean hasPacman;

    public GameSign(GameSignZone zone, Sign sign)
    {
        this.sign = sign;
        this.sign.setMetadata("game", new FixedMetadataValue(Hub.getInstance(), zone.getGame().getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(Hub.getInstance(), zone.getMap()));
    }

    public void update(SignData data)
    {
        if(Hub.getInstance().getSignManager().isPacmanEnabled())
        {
            return;
        }
        else if(Hub.getInstance().getSignManager().isMaintenance())
        {
            this.sign.setLine(0, "");
            this.sign.setLine(1, ChatColor.DARK_RED + "Jeu en");
            this.sign.setLine(2, ChatColor.DARK_RED + "maintenance !");
            this.sign.setLine(3, "");

            Bukkit.getScheduler().runTask(Hub.getInstance(), this.sign::update);
            return;
        }

        this.lastData = data;
        this.lastData.display(this.sign);
    }

    public void click(Player player)
    {
        if(Hub.getInstance().getSignManager().isPacmanEnabled())
        {
            if(this.hasPacman)
            {
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).creditStars(5, "MACOUM!", false);
                Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Pacman a été attrapé par " + ChatColor.RED + ChatColor.BOLD + player.getName() + ChatColor.GOLD + ChatColor.BOLD + " !");
                FireworkUtils.launchfw(this.sign.getLocation(), FireworkEffect.builder().with(FireworkEffect.Type.STAR).withColor(Color.YELLOW).withFade(Color.ORANGE).withTrail().withFlicker().build());
                Hub.getInstance().getSignManager().stopPacman();
            }
            else
            {
                player.sendMessage(ChatColor.GOLD + "Macoum macoum macoum...");
            }

            return;
        }

        if(this.lastData == null)
        {
            return;
        }

        String[] serverNameParts = this.lastData.getBungeeName().split("_");

        player.sendMessage(ChatColor.GREEN + "Vous avez été envoyé vers le serveur " + serverNameParts[0] + " " + serverNameParts[1] + " !");
        SamaGamesAPI.get().getProxyDataManager().getProxiedPlayer(player.getUniqueId()).connect(this.lastData.getBungeeName());
    }

    public void setPacman(boolean flag)
    {
        this.hasPacman = flag;
    }

    public Sign getSign()
    {
        return this.sign;
    }
}
