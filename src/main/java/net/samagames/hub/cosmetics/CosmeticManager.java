package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.balloons.BalloonManager;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.disguises.DisguiseManager;
import net.samagames.hub.cosmetics.gadgets.GadgetManager;
import net.samagames.hub.cosmetics.jukebox.JukeboxManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import net.samagames.hub.cosmetics.pets.PetManager;
import org.bukkit.entity.Player;

public class CosmeticManager extends AbstractManager
{
    private DisguiseManager disguiseManager;
    private JukeboxManager jukeboxManager;
    private PetManager petManager;
    private GadgetManager gadgetManager;
    private ParticleManager particleManager;
    private BalloonManager balloonManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player)
    {
        this.jukeboxManager.onLogin(player);

        this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
        {
            this.disguiseManager.restoreCosmetic(player);
            this.petManager.restoreCosmetic(player);
            this.gadgetManager.restoreCosmetic(player);
            this.particleManager.restoreCosmetic(player);
            this.balloonManager.restoreCosmetic(player);
        }, 20L);
    }

    @Override
    public void onLogout(Player player)
    {
        this.jukeboxManager.onLogout(player);

        this.disguiseManager.disableCosmetic(player, true);
        this.jukeboxManager.disableCosmetic(player, true);
        this.petManager.disableCosmetic(player, true);
        this.gadgetManager.disableCosmetic(player, true);
        this.particleManager.disableCosmetic(player, true);
        this.balloonManager.disableCosmetic(player, true);
    }

    public DisguiseManager getDisguiseManager()
    {
        return this.disguiseManager;
    }

    public JukeboxManager getJukeboxManager()
    {
        return this.jukeboxManager;
    }

    public PetManager getPetManager()
    {
        return this.petManager;
    }

    public GadgetManager getGadgetManager()
    {
        return this.gadgetManager;
    }

    public ParticleManager getParticleManager()
    {
        return this.particleManager;
    }

    public BalloonManager getBalloonManager()
    {
        return balloonManager;
    }

    public boolean isEquipped(Player player, AbstractCosmetic cosmetic)
    {
        boolean equipped = false;

        if (this.disguiseManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.disguiseManager.getEquippedCosmetic(player)) > 0)
            equipped = true;
        else if (this.petManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.petManager.getEquippedCosmetic(player)) > 0)
            equipped = true;
        else if (this.gadgetManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.gadgetManager.getEquippedCosmetic(player)) > 0)
            equipped = true;
        else if (this.particleManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.particleManager.getEquippedCosmetic(player)) > 0)
            equipped = true;
        else if (this.balloonManager.getEquippedCosmetic(player) != null && cosmetic.compareTo(this.balloonManager.getEquippedCosmetic(player)) > 0)
            equipped = true;

        return equipped;
    }

    public void init()
    {
        this.disguiseManager = new DisguiseManager(this.hub);
        this.jukeboxManager = new JukeboxManager(this.hub);
        this.petManager = new PetManager(this.hub);
        this.gadgetManager = new GadgetManager(this.hub);
        this.particleManager = new ParticleManager(this.hub);
        this.balloonManager = new BalloonManager(this.hub);
    }
}
