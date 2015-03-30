package net.samagames.hub.common;

import net.samagames.api.SamaGamesAPI;
import net.samagames.permissionsapi.permissions.PermissionUser;
import net.samagames.permissionsbukkit.PermissionsBukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class JsonHub
{
    private final int lobbyNumber;
    private int connectedPlayers;
    private HashMap<String, Integer> playersDetails;

    public JsonHub(int lobbyNumber, int connectedPlayers)
    {
        this.lobbyNumber = lobbyNumber;
        this.connectedPlayers = connectedPlayers;
        this.playersDetails = new HashMap<>();
    }

    public void addConnectedPlayer(Player player)
    {
        PermissionUser user = PermissionsBukkit.getApi().getUser(player.getUniqueId());
        String display = PermissionsBukkit.getDisplay(user);

        if (display.length() < 5)
            display += "Joueurs";

        if (this.playersDetails.containsKey(display))
            this.playersDetails.put(display, this.playersDetails.get(display) + 1);
        else
            this.playersDetails.put(display, 1);
    }

    public int getLobbyNumber()
    {
        return this.lobbyNumber;
    }

    public int getConnectedPlayers()
    {
        return this.connectedPlayers;
    }

    public HashMap<String, Integer> getPlayersDetails()
    {
        return this.playersDetails;
    }
}
