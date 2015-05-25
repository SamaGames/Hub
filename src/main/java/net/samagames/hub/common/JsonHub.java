package net.samagames.hub.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.permissionsapi.permissions.PermissionUser;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class JsonHub
{
    private int hubNumber;
    private int connectedPlayers;
    private HashMap<String, Integer> playersDetails;

    public JsonHub(int hubNumber, int connectedPlayers)
    {
        this.hubNumber = hubNumber;
        this.connectedPlayers = connectedPlayers;
        this.playersDetails = new HashMap<>();
    }

    public JsonHub()
    {
        this.playersDetails = new HashMap<>();
    }

    public void addConnectedPlayer(Player player)
    {
        PermissionUser user = SamaGamesAPI.get().getPermissionsManager().getApi().getUser(player.getUniqueId());
        String display = SamaGamesAPI.get().getPermissionsManager().getDisplay(user);

        if (display.length() < 5)
            display += "Joueurs";

        if (this.playersDetails.containsKey(display))
            this.playersDetails.put(display, this.playersDetails.get(display) + 1);
        else
            this.playersDetails.put(display, 1);
    }

    public int getHubNumber()
    {
        return this.hubNumber;
    }

    public void setHubNumber(int hubNumber)
    {
        this.hubNumber = hubNumber;
    }

    public int getConnectedPlayers()
    {
        return this.connectedPlayers;
    }

    public void setConnectedPlayers(int connectedPlayers)
    {
        this.connectedPlayers = connectedPlayers;
    }

    public HashMap<String, Integer> getPlayersDetails()
    {
        return this.playersDetails;
    }
}
