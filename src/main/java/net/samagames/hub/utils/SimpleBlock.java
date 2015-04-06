package net.samagames.hub.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class SimpleBlock
{
    private Material type;
    private byte data;

    public SimpleBlock(Material type, byte data)
    {
        this.type = type;
        this.data = data;
    }

    public SimpleBlock(Material type, int data)
    {
        this(type, (byte) data);
    }

    public SimpleBlock(Material type)
    {
        this(type, (byte) 0);
    }

    public SimpleBlock(Block block)
    {
        this(block.getType(), block.getData());
    }

    public SimpleBlock(Location location)
    {
        this(location.getBlock());
    }

    public Material getType()
    {
        return this.type;
    }

    public byte getData()
    {
        return this.data;
    }
}
