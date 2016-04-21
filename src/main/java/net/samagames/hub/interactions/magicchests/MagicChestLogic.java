package net.samagames.hub.interactions.magicchests;

import net.samagames.hub.Hub;

import java.util.Random;

class MagicChestLogic
{
    private final Hub hub;
    private final Random random;

    MagicChestLogic(Hub hub)
    {
        this.hub = hub;
        this.random = new Random();
    }
}
