package net.samagames.hub.games.signs;

import net.minecraft.server.v1_12_R1.*;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.parties.IParty;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public class GameSign
{
    private final Hub hub;
    private final AbstractGame game;
    private final String map;
    private final ChatColor color;
    private final String template;
    private final RestrictedVersion restrictedVersion;
    private final Sign sign;
    private final BukkitTask updateTask;
    private boolean isMaintenance;
    private boolean isSoon;

    private int scrollIndex = 0;
    private int scrollVector = +1;
    private String scrolledMapName;

    private int playerPerGame;
    private int playerWaitFor;
    private int totalPlayerOnServers;

    public GameSign(Hub hub, AbstractGame game, String map, ChatColor color, String template, RestrictedVersion restrictedVersion, Sign sign)
    {
        this.hub = hub;
        this.game = game;
        this.map = map;
        this.color = color;
        this.template = template;
        this.restrictedVersion = restrictedVersion;
        this.sign = sign;

        this.sign.setMetadata("game", new FixedMetadataValue(hub, game.getCodeName()));
        this.sign.setMetadata("map", new FixedMetadataValue(hub, map));

        hub.getScheduledExecutorService().scheduleAtFixedRate(this::scrollMapName, 500, 500, TimeUnit.MILLISECONDS);

        this.updateTask = hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, this::update, 20L, 20L);
    }

    public void onDelete()
    {
        this.updateTask.cancel();
    }

    public void update()
    {
        if (this.isSoon)
        {
            this.sign.setLine(0, "-*--*-");
            this.sign.setLine(1, "Prochainement");
            this.sign.setLine(2, "...");
            this.sign.setLine(3, "-*--*-");

            this.updateSign();
            return;
        }

        if (this.isMaintenance)
        {
            this.sign.setLine(0, "");
            this.sign.setLine(1, ChatColor.DARK_RED + "Jeu en");
            this.sign.setLine(2, ChatColor.DARK_RED + "maintenance !");
            this.sign.setLine(3, "");

            this.updateSign();
            return;
        }

        String mapLine = this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «";

        this.sign.setLine(0, this.game.getName());
        this.sign.setLine(1, mapLine);
        this.sign.setLine(2, this.playerWaitFor + "" + ChatColor.RESET + " en attente");
        this.sign.setLine(3, this.totalPlayerOnServers + "" + ChatColor.RESET + " en jeu");

        this.updateSign();
    }

    public void updateSign()
    {
        IChatBaseComponent[] lines = new IChatBaseComponent[] {
                new ChatComponentText(this.sign.getLine(0)),
                new ChatComponentText(this.sign.getLine(1)),
                new ChatComponentText(this.sign.getLine(2)),
                new ChatComponentText(this.sign.getLine(3))
        };

        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", "Sign");
        nbt.setInt("x", this.sign.getLocation().getBlockX());
        nbt.setInt("y", this.sign.getLocation().getBlockY());
        nbt.setInt("z", this.sign.getLocation().getBlockZ());

        for (int i = 0; i < 4; ++i)
            nbt.setString("Text" + (i + 1), IChatBaseComponent.ChatSerializer.a(lines[i]));

        PacketPlayOutTileEntityData packet = new PacketPlayOutTileEntityData(new BlockPosition(this.sign.getX(), this.sign.getY(), this.sign.getZ()), 9, nbt);

        this.sign.getWorld().getNearbyEntities(this.sign.getLocation(), 30, 30, 30).stream().filter(entity -> entity instanceof Player).forEach(entity -> ((CraftPlayer) entity).getHandle().playerConnection.sendPacket(packet));
    }

    public void updateMapName()
    {
        this.sign.setLine(1, this.color + "» " + ChatColor.BOLD + this.scrolledMapName + ChatColor.RESET + this.color + " «");
        this.updateSign();
    }

    public void scrollMapName()
    {
        if (this.isMaintenance || this.isSoon)
            return;

        if (this.map.length() <= 10)
        {
            this.scrolledMapName = this.map;
            return;
        }

        int start = this.scrollIndex;
        int end = this.scrollIndex + 10;

        if (end > this.map.length())
        {
            this.scrollVector = -1;
            this.scrollIndex = this.map.length() - 10;
            return;
        }
        if (start < 0)
        {
            this.scrollVector = 1;
            this.scrollIndex = 0;
            return;
        }

        this.scrolledMapName = this.map.substring(start, end);
        this.scrollIndex += this.scrollVector;

        this.updateMapName();
    }

    public void click(Player player)
    {
        if (this.isSoon)
        {
            player.sendMessage(ChatColor.RED + "Ce jeu n'est pas encore disponible.");
            return;
        }
        else if (this.isMaintenance)
        {
            player.sendMessage(ChatColor.RED + "Ce jeu est actuellement en maintenance.");
            return;
        }

        if (this.restrictedVersion != null && !this.restrictedVersion.canAccess(player))
        {
            player.sendMessage(ChatColor.RED + "Vous ne pouvez pas jouer à ce jeu avec votre version de Minecraft actuelle !");

            if (this.restrictedVersion.getOperator() == RestrictedVersion.Operator.EQUALS)
            {
                player.sendMessage(ChatColor.RED + "Veuillez utiliser la version " + this.restrictedVersion.getLiteralVersion() + " pour pouvoir jouer.");
            }
            else if (this.restrictedVersion.getOperator() == RestrictedVersion.Operator.GREATER_OR_EQUALS)
            {
                player.sendMessage(ChatColor.RED + "Veuillez utiliser une version supérieure ou égale à la version " + this.restrictedVersion.getLiteralVersion() + " pour pouvoir jouer.");
            }
            else if (this.restrictedVersion.getOperator() == RestrictedVersion.Operator.LESS_OR_EQUALS)
            {
                player.sendMessage(ChatColor.RED + "Veuillez utiliser une version inférieure ou égale à la version " + this.restrictedVersion.getLiteralVersion() + " pour pouvoir jouer.");
            }

            return;
        }

        if (this.game.isPlayerFirstGame(SamaGamesAPI.get().getStatsManager().getPlayerStats(player.getUniqueId())) && this.game.getWebsiteDescriptionURL() != null)
            this.game.showRulesWarning(player);

        addToQueue(player, template);
    }

    public static void addToQueue(Player player, String template)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            IParty party = SamaGamesAPI.get().getPartiesManager().getPartyForPlayer(player.getUniqueId());

            if (party == null)
            {
                Hub.getInstance().getHydroangeasManager().addPlayerToQueue(player.getUniqueId(), template);
            }
            else
            {
                if (!party.getLeader().equals(player.getUniqueId()))
                {
                    player.sendMessage(ChatColor.RED + "Vous n'êtes pas le leader de votre partie, vous ne pouvez donc pas l'ajouter dans une file d'attente.");
                    return;
                }

                Hub.getInstance().getHydroangeasManager().addPartyToQueue(player.getUniqueId(), party.getParty(), template);
            }
        });
    }

    public void developperClick(Player player)
    {
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
        player.sendMessage(ChatColor.GOLD + "Informations du panneau de jeu :");
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Template : " + ChatColor.GREEN + this.template);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Jeu : " + ChatColor.GREEN + this.game.getCodeName());
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Map : " + ChatColor.GREEN + this.map);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Version restreinte : " + ChatColor.GREEN + (this.restrictedVersion == null ? "Aucune" : this.restrictedVersion.getOperator().getSymbol() + this.restrictedVersion.getLiteralVersion()));
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs par jeu : " + ChatColor.GREEN + this.playerPerGame);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en attente : " + ChatColor.GREEN + this.playerWaitFor);
        player.sendMessage(ChatColor.GOLD + "> " + ChatColor.AQUA + "Joueurs en jeu : " + ChatColor.GREEN + this.totalPlayerOnServers);
        player.sendMessage(ChatColor.GOLD + "----------------------------------------");
    }

    public void setPlayerPerGame(int playerPerGame)
    {
        this.playerPerGame = playerPerGame;
    }

    public void setPlayerWaitFor(int playerWaitFor)
    {
        this.playerWaitFor = playerWaitFor;
    }

    public void setTotalPlayerOnServers(int totalPlayerOnServers)
    {
        this.totalPlayerOnServers = totalPlayerOnServers;
    }

    public void setMaintenance(boolean isMaintenance)
    {
        this.isMaintenance = isMaintenance;
        this.update();
    }

    public Sign getSign()
    {
        return this.sign;
    }

    public String getTemplate()
    {
        return this.template;
    }

    public String getMap()
    {
        return this.map;
    }

    public ChatColor getColor()
    {
        return this.color;
    }

    public int getTotalPlayerOnServers()
    {
        return this.totalPlayerOnServers;
    }

    public void setSoon(boolean isSoon)
    {
        this.isSoon = isSoon;
        this.update();
    }
}
