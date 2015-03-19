package net.samagames.hub.common.managers;

import net.samagames.hub.Hub;
import net.samagames.hub.common.StaticInventory;
import net.samagames.hub.utils.Selection;
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

        this.canBuild = false;
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
