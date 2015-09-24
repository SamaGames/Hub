package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import net.samagames.hub.cosmetics.pets.PetManager;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CosmeticManager extends AbstractManager
{
    private final ParticleManager particleManager;
    private final GadgetManager gadgetManager;
    private final JukeboxManager jukeboxManager;
    private final DisguiseManager disguiseManager;
    private final PetManager petManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.particleManager = new ParticleManager(hub);
        this.gadgetManager = new GadgetManager(hub);
        this.jukeboxManager = new JukeboxManager(hub);
        this.disguiseManager = new DisguiseManager(hub);
        this.petManager = new PetManager(hub);

        hub.log(this, Level.INFO, "Registered cosmetics and started loops!");
    }

    public void handleLogin(Player player)
    {
        this.particleManager.restoreCosmetic(player);
        this.disguiseManager.restoreCosmetic(player);
    }

    public void restorePet(Player player)
    {
        this.petManager.restoreCosmetic(player);
    }

    public void handleLogout(Player player)
    {
        this.particleManager.disableCosmetic(player, true);
        this.disguiseManager.disableCosmetic(player, true);
        this.petManager.disableCosmetic(player, true);

        this.jukeboxManager.mute(player, false);
        this.jukeboxManager.removePlayer(player);
    }

    public ParticleManager getParticleManager() { return this.particleManager; }
    public GadgetManager getGadgetManager() { return this.gadgetManager; }
    public JukeboxManager getJukeboxManager() { return this.jukeboxManager; }
    public DisguiseManager getDisguiseManager() { return this.disguiseManager; }
    public PetManager getPetManager() { return this.petManager; }

    @Override
    public String getName() { return "CosmeticManager"; }
}
