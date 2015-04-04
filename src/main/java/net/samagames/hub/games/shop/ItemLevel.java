package net.samagames.hub.games.shop;

public class ItemLevel
{
    private final int cost;
    private final String description;
    private final String databaseStorageName;

    public ItemLevel(int cost, String description, String databaseStorageName)
    {
        this.cost = cost;
        this.description = description;
        this.databaseStorageName = databaseStorageName;
    }

    public int getCost()
    {
        return this.cost;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getDatabaseStorageName()
    {
        return this.databaseStorageName;
    }
}
