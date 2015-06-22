package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CosmeticManager extends AbstractManager
{
    private ParticleManager particleManager;
    private GadgetManager gadgetManager;
    private JukeboxManager jukeboxManager;
    private DisguiseManager disguiseManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.particleManager = new ParticleManager(hub);
        this.gadgetManager = new GadgetManager(hub);
        this.jukeboxManager = new JukeboxManager(hub);
        this.disguiseManager = new DisguiseManager(hub);

        hub.log(this, Level.INFO, "Registered cosmetics and started loop!");
    }

    public void handleLogin(Player player)
    {
        this.particleManager.restoreCosmetic(player);
        this.disguiseManager.restoreCosmetic(player);
    }

    public void handleLogout(Player player)
    {
        this.particleManager.disableCosmetic(player, true);

        this.jukeboxManager.mute(player, false);
        this.jukeboxManager.removePlayer(player);
    }

    public ParticleManager getParticleManager() { return this.particleManager; }
    public GadgetManager getGadgetManager() { return this.gadgetManager; }
    public JukeboxManager getJukeboxManager() { return this.jukeboxManager; }
    public DisguiseManager getDisguiseManager() { return this.disguiseManager; }

    @Override
    public String getName() { return "CosmeticManager"; }
}
