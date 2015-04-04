package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;

public class CosmeticManager extends AbstractManager
{
    private JukeboxManager jukeboxManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.jukeboxManager = new JukeboxManager(hub);
    }

    @Override
    public String getName() { return "CosmeticManager"; }
}
