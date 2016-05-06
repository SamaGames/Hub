package net.samagames.hub.games.shops;

public class ItemLevel
{
    private final int cost;
    private final String description;
    private final long storageId;

    public ItemLevel(int cost, String description, long storageId)
    {
        this.cost = cost;
        this.description = description;
        this.storageId = storageId;
    }

    public int getCost()
    {
        return this.cost;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getStorageId()
    {
        return this.storageId;
    }
}
