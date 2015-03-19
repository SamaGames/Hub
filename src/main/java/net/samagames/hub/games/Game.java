package net.samagames.hub.games;

import org.bukkit.Location;
import org.bukkit.Material;

public class Game
{
    private final String name;
    private final Material icon;
    private final String[] description;
    private final int slotInMainMenu;
    private final Location lobbySpawn;

    //TODO: Sign array

    public Game(String name, Material icon, String[] description, int slotInMainMenu, Location lobbySpawn)
    {
        this.name = name;
        this.icon = icon;
        this.description = description;
        this.slotInMainMenu = slotInMainMenu;
        this.lobbySpawn = lobbySpawn;
    }

    public String getName()
    {
        return this.name;
    }

    public Material getIcon()
    {
        return this.icon;
    }

    public String[] getDescription()
    {
        return this.description;
    }

    public int getSlotInMainMenu()
    {
        return this.slotInMainMenu;
    }

    public Location getLobbySpawn()
    {
        return this.lobbySpawn;
    }
}
