package net.samagames.hub.common.casino;

import net.samagames.hub.Hub;
import org.bukkit.entity.Player;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 28/01/2017
 */
public abstract class CasinoIntegration
{
    protected final Hub instance;

    public CasinoIntegration(Hub instance)
    {
        this.instance = instance;
    }

    public abstract void onDisable();

    public abstract void handleJoin(Player player);
    public abstract void handleLeave(Player player, boolean disconnect);

    public abstract boolean isAvailable();
}
