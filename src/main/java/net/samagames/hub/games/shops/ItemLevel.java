package net.samagames.hub.games.shops;

class ItemLevel
{
    private final int storageId;
    private final String description;
    private final int cost;

    ItemLevel(int storageId, String description, int cost)
    {
        this.storageId = storageId;
        this.description = description;
        this.cost = cost;
    }

    public int getStorageId()
    {
        return this.storageId;
    }

    public String getDescription()
    {
        return this.description;
    }

    public int getCost()
    {
        return this.cost;
    }
}
