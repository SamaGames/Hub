package net.samagames.hub.cosmetics.particles;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.tools.ParticleEffects;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class ParticleManager extends AbstractCosmeticManager<AbstractParticle>
{
    private HashMap<UUID, ParticleEffects> playersParticleEffect;

    public ParticleManager(Hub hub)
    {
        super(hub, new ParticleRegistry());
        this.playersParticleEffect = new HashMap<>();
    }

    @Override
    public void enableCosmetic(Player player, AbstractParticle particle)
    {
        this.playersParticleEffect.put(player.getUniqueId(), particle.getParticleEffect());
        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.particles.current", particle.getDatabaseName());
    }

    @Override
    public void disableCosmetic(Player player)
    {
        if(this.playersParticleEffect.containsKey(player.getUniqueId()))
            this.playersParticleEffect.remove(player.getUniqueId());

        SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("cosmetics.particles.current");
    }

    @Override
    public void restoreCosmetic(Player player)
    {
        String value = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).get("cosmetics.particles.current");

        if(value != null)
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void update()
    {
        try
        {
            for(UUID uuid : this.playersParticleEffect.keySet())
            {
                Player player = Bukkit.getPlayer(uuid);

                for(Player serverPlayer : Bukkit.getOnlinePlayers())
                    this.playersParticleEffect.get(uuid).sendToPlayer(serverPlayer, player.getLocation(), 0.5F, 0.5F, 0.5F, 0.25F, 2);
            }
        }
        catch (Exception e)
        {
            Hub.getInstance().log(this, Level.SEVERE, "An exception occured when the update process!");
            e.printStackTrace();
        }
    }

    @Override
    public String getName() { return "ParticleManager"; }
}
