package net.samagames.hub.cosmetics.jukebox;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;

public class JukeboxManager extends AbstractManager
{
    public JukeboxManager(Hub hub)
    {
        super(hub);
    }

    @Override
    public String getName() { return "JukeboxManager"; }
}
