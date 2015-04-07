package net.samagames.hub.common.managers;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.StaticInventory;
import net.samagames.hub.cosmetics.jukebox.JukeboxPlaylist;
import net.samagames.tools.Selection;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager extends AbstractManager
{
    private HashMap<UUID, Selection> selections;
    private Location lobbySpawn;
    private StaticInventory staticInventory;

    private boolean canBuild;

    public PlayerManager(Hub hub)
    {
        super(hub);

        this.selections = new HashMap<>();
        this.staticInventory = new StaticInventory();

        this.lobbySpawn = new Location(this.hub.getHubWorld(), -19.5, 51, 89.5);
        this.canBuild = false;
    }

    public void updateSettings(Player player)
    {
        String chatOnSetting = SamaGamesAPI.get().getSettingsManager().getSetting(player.getUniqueId(), "chat");
        String jukeboxSetting = SamaGamesAPI.get().getSettingsManager().getSetting(player.getUniqueId(), "jukebox");

        if (chatOnSetting != null && chatOnSetting.equals("false"))
        {
            Hub.getInstance().getChatManager().disableChat(player);
            player.sendMessage(ChatColor.GOLD + "Vous avez désactivé le chat. Vous ne verrez donc pas les messages des joueurs.");
        }

        if (jukeboxSetting != null && jukeboxSetting.equals("true"))
        {
            Hub.getInstance().getCosmeticManager().getJukeboxManager().addPlayer(player);

            JukeboxPlaylist song = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();

            if (song != null)
                player.sendMessage(Hub.getInstance().getCosmeticManager().getJukeboxManager().getJukeboxTag() + ChatColor.YELLOW + "La musique jouée actuellement est " + ChatColor.GOLD + ChatColor.ITALIC + song.getSong().getTitle() + ChatColor.YELLOW + " de " + ChatColor.GOLD + song.getSong().getAuthor() + ChatColor.YELLOW + ", proposée par " + ChatColor.GOLD + song.getPlayedBy());
        }
        else
        {
            Hub.getInstance().getCosmeticManager().getJukeboxManager().mute(player, true);
        }
    }

    public void removeSelection(Player player)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            this.selections.remove(player.getUniqueId());
    }

    public void setSelection(Player player, Selection selection)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            this.selections.remove(player.getUniqueId());

        this.selections.put(player.getUniqueId(), selection);
    }

    public void setLobbySpawn(Location lobbySpawn)
    {
        this.lobbySpawn = lobbySpawn;
    }

    public void setBuildEnabled(boolean flag)
    {
        this.canBuild = flag;
    }

    public Selection getSelection(Player player)
    {
        if(this.selections.containsKey(player.getUniqueId()))
            return this.selections.get(player.getUniqueId());
        else
            return null;
    }

    public Location getLobbySpawn() { return this.lobbySpawn; }
    public StaticInventory getStaticInventory() { return this.staticInventory; }

    public boolean canBuild() { return this.canBuild; }

    @Override
    public String getName() { return "PlayerManager"; }
}
