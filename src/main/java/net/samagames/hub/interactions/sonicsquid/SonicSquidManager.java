package net.samagames.hub.interactions.sonicsquid;

import com.google.gson.JsonArray;
import net.minecraft.server.v1_9_R1.EntitySquid;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;

public class SonicSquidManager extends AbstractInteractionManager<SonicSquid>
{
    public SonicSquidManager(Hub hub)
    {
        super(hub, "squid");

        this.hub.getEntityManager().registerEntity("SonicSquid", 94, EntitySquid.class, EntitySonicSquid.class);
    }

    @Override
    public void loadConfiguration(JsonArray rootJson)
    {
        this.interactions.add(new SonicSquid(this.hub));
    }
}
