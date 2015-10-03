package net.samagames.hub.common.managers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.StaticInventory;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerManager extends AbstractManager
{
    private HashMap<UUID, Location> selections;
    private ArrayList<UUID> hiders;
    private Location lobbySpawn;
    private StaticInventory staticInventory;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        this.selections = new HashMap<>();
        this.hiders = new ArrayList<>();
        this.staticInventory = new StaticInventory();

        this.lobbySpawn = new Location(this.hub.getHubWorld(), -19.5, 51, 89.5);
        this.canBuild = false;
    }

    public void handleLogin(Player player)
    {
        this.updateSettings(player);
        this.updateHiders(player);
    }

    public void handleLogout(Player player)
    {
        this.removeSelection(player);
        this.removeHider(player);
    }

    public void updateSettings(Player player)
    {
        boolean playerOnSetting = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "players", true);
        boolean chatOnSetting = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "chat", true);
        boolean jukeboxSetting = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "jukebox", true);

        if (!playerOnSetting)
        {
            Bukkit.getOnlinePlayers().stream().filter(p -> !SamaGamesAPI.get().getPermissionsManager().hasPermission(p, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(player.getUniqueId(), p.getUniqueId())).forEach(player::hidePlayer);

            this.addHider(player);
            player.sendMessage(ChatColor.GOLD + "Vous avez désactivé les joueurs. Vous ne verrez donc aucun joueur excepté les membres de l'équipe.");
        }
        else
        {
            Bukkit.getOnlinePlayers().forEach(player::showPlayer);
        }

        if (!chatOnSetting)
        {
            Hub.getInstance().getChatManager().disableChatFor(player);
            player.sendMessage(ChatColor.GOLD + "Vous avez désactivé le chat. Vous ne verrez donc pas les messages des joueurs.");
        }

        if (jukeboxSetting)
        {
            Hub.getInstance().getCosmeticManager().getJukeboxManager().addPlayer(player);

            JukeboxSong song = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();

            if (song != null)
                player.sendMessage(Hub.getInstance().getCosmeticManager().getJukeboxManager().getJukeboxTag() + ChatColor.YELLOW + "La musique jouée actuellement est " + ChatColor.GOLD + ChatColor.ITALIC + song.getSong().getTitle() + ChatColor.YELLOW + " de " + ChatColor.GOLD + song.getSong().getAuthor() + ChatColor.YELLOW + ", proposée par " + ChatColor.GOLD + song.getPlayedBy());
        }
        else
        {
            Hub.getInstance().getCosmeticManager().getJukeboxManager().mute(player, true);
        }
    }

    public void updateHiders(Player newConnected)
    {
        this.hiders.stream().filter(hider -> !hider.equals(newConnected.getUniqueId())).filter(hider -> !SamaGamesAPI.get().getPermissionsManager().hasPermission(newConnected, "hub.announce") && !SamaGamesAPI.get().getFriendsManager().areFriends(newConnected.getUniqueId(), hider)).forEach(hider -> Bukkit.getScheduler().runTask(Hub.getInstance(), () ->
                Bukkit.getPlayer(hider).hidePlayer(newConnected)));
    }

    public void removeSelection(Player player)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            this.selections.remove(player.getUniqueId());
    }

    public void setSelection(Player player, Location selection)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            this.selections.remove(player.getUniqueId());

        this.selections.put(player.getUniqueId(), selection);
    }

    public void addHider(Player player)
    {
        this.hiders.add(player.getUniqueId());
    }

    public void removeHider(Player player)
    {
        if(this.hiders.contains(player.getUniqueId()))
            this.hiders.remove(player.getUniqueId());
    }

    public void setBuildEnabled(boolean flag)
    {
        this.canBuild = flag;
    }

    public Location getSelection(Player player)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            return this.selections.get(player.getUniqueId());
        else
            return null;
    }

    public Location getLobbySpawn() { return this.lobbySpawn; }

    public void setLobbySpawn(Location lobbySpawn)
    {
        this.lobbySpawn = lobbySpawn;
    }

    public StaticInventory getStaticInventory() { return this.staticInventory; }

    public boolean canBuild() { return this.canBuild; }

    @Override
    public String getName() { return "PlayerManager"; }
}
