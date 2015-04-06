package net.samagames.hub.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public class CosmeticManager extends AbstractManager
{
    private ParticleManager particleManager;

    public CosmeticManager(Hub hub)
    {
        super(hub);

        this.particleManager = new ParticleManager(hub);

        Bukkit.getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            this.particleManager.update();
        }, 1L, 1L);

        hub.log(this, Level.INFO, "Registered cosmetics and started loop!");
    }

    public void handleLogin(Player player)
    {
        this.particleManager.restoreCosmetic(player);
    }

    public void handleLogout(Player player)
    {
        this.particleManager.disableCosmetic(player, true);
    }

    public ParticleManager getParticleManager() { return this.particleManager; }

    @Override
    public String getName() { return "CosmeticManager"; }
}
