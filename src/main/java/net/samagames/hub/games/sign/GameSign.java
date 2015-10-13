package net.samagames.hub.games.sign;

import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_8_R3.*;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class GameSign
{
    private final Sign sign;
    private final AbstractGame game;
    private final String map;
    private final ChatColor color;
    private final String template;
    private final BukkitTask updateTask;

    private int scrollIndex = 0;
    private int scrollVector = +1;
    private String scrolledMapName;

    private int playerWaitFor;
    private int totalPlayerOnServers;

    public GameSign(AbstractGame game, String map, ChatColor color, String template, Sign sign)
    {
        this.sign = sign;
        this.game = game;
        this.map = map;
        this.color = color;
        this.template = template;

        this.sign.setMetadata("game", new FixedMetadataValue(Hub.getInstance(), game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(Hub.getInstance(), map));

        Hub.getInstance().getScheduledExecutorService().scheduleAtFixedRate(() -> scrollMapName(), 1000, 500, TimeUnit.MILLISECONDS);

        this.updateTask = Bukkit.getScheduler().runTaskTimerAsynchronously(Hub.getInstance(), this::update, 20L, 20L);
    }

    public void update()
    {
        if(this.game.isMaintenance())
        {
            this.sign.setLine(0, "");
            this.sign.setLine(1, ChatColor.DARK_RED + "Jeu en");
            this.sign.setLine(2, ChatColor.DARK_RED + "maintenance !");
            this.sign.setLine(3, "");

            updateSign();
            return;
        }

        String mapLine = this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, playerWaitFor + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, totalPlayerOnServers + "" + ChatColor.RESET + " en jeu");

        updateSign();
    }

    public void updateSign()
    {
        WorldServer worldServer = ((CraftWorld) sign.getWorld()).getHandle();

        IChatBaseComponent[] lines = new IChatBaseComponent[]
                {
                        new ChatComponentText(sign.getLine(0)),
                        new ChatComponentText(sign.getLine(1)),
                        new ChatComponentText(sign.getLine(2)),
                        new ChatComponentText(sign.getLine(3))
                };
        PacketPlayOutUpdateSign packet = new PacketPlayOutUpdateSign(worldServer, new BlockPosition(sign.getX(), sign.getY(), sign.getZ()), lines);

        sign.getWorld().getNearbyEntities(sign.getLocation(), 15, 15, 15).stream().filter(entity -> entity instanceof Player).forEach(entity -> {
            Player player = (Player) entity;

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
        });
    }

    public void updateMapName()
    {
        this.sign.setLine(1, this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «");
        updateSign();
    }

    public void scrollMapName()
    {
        if(map.length() <= 10)
        {
            scrolledMapName = map;
            return;
        }

        int start = scrollIndex;
        int end = scrollIndex+10;

        if(end > map.length())
        {
            scrollVector = -1;
            scrollIndex = map.length()-10;
            return;
        }
        if(start < 0)
        {
            scrollVector = 1;
            scrollIndex = 0;
            return;
        }

        scrolledMapName = map.substring(start, end);
        scrollIndex += scrollVector;
        updateMapName();
    }

    public void click(Player player)
    {
        if(this.game.isMaintenance())
        {
            player.sendMessage(ChatColor.RED + "Ce jeu est actuellement en maintenance.");
            return;
        }

        UUID partyUUID = SamaGamesAPI.get().getPartiesManager().getPlayerParty(player.getUniqueId());

        if(partyUUID == null)
        {
            Hub.getInstance().getHydroManager().addPlayerToQueue(player.getUniqueId(), this.template);
        }
        else
        {
            if(!SamaGamesAPI.get().getPartiesManager().getLeader(partyUUID).equals(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "Vous n'êtes pas le leader, vous ne pouvez pas ajouter votre partie dans une queue.");
                return;
            }

            Hub.getInstance().getHydroManager().addPartyToQueue(player.getUniqueId(), partyUUID, this.template);
        }
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Template : " + template);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + playerWaitFor);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + totalPlayerOnServers);
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public String getTemplate()
    {
        return template;
    }

    public String getMap()
    {
        return map;
    }

    public ChatColor getColor()
    {
        return color;
    }

    public void setPlayerWaitFor(int playerWaitFor)
    {
        this.playerWaitFor = playerWaitFor;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers)
    {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }

    public void onDelete()
    {
        updateTask.cancel();
    }
}
