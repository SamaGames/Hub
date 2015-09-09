package net.samagames.hub.cosmetics.particles;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class ParticleManager extends AbstractCosmeticManager<ParticleCosmetic>
{
    private final HashMap<UUID, Effect> playersParticleEffect;
    private final EffectManager effectManager;

    public ParticleManager(Hub hub)
    {
        super(hub, new ParticleRegistry());

        this.playersParticleEffect = new HashMap<>();
        this.effectManager = new EffectManager(hub.getEffectLib());
    }

    @Override
    public void enableCosmetic(Player player, ParticleCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            clearEffect(player.getUniqueId());

            try
            {
                Effect particleEffectObject = cosmetic.getParticleEffect().getConstructor(EffectManager.class).newInstance(this.effectManager);
                particleEffectObject.setEntity(player);
                particleEffectObject.infinite();
                particleEffectObject.start();

                this.playersParticleEffect.put(player.getUniqueId(), particleEffectObject);
                SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.particles.current", cosmetic.getKey());
                player.sendMessage(ChatColor.GREEN + "Vous voilà noyé sous les particules...");
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
            {
                this.hub.log(this, Level.SEVERE, "Can't create EntityEffect object to " + player.getName() + "'s particle effect!");
                e.printStackTrace();
            }
        }
        else
        {
            cosmetic.buy(player);
        }
    }

    @Override
    public void disableCosmetic(Player player, boolean logout)
    {
        clearEffect(player.getUniqueId());

        if (!logout)
        {
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("cosmetics.particles.current");
            player.sendMessage(ChatColor.GREEN + "Votre effet disparait dans l'ombre...");
        }
    }

    @Override
    public void restoreCosmetic(Player player)
    {
        String value = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).get("cosmetics.particles.current");

        if(value != null)
            this.enableCosmetic(player, this.getRegistry().getElementByStorageName(value));
    }

    @Override
    public void update() {}

    public void clearEffect(UUID player)
    {
        if (this.playersParticleEffect.containsKey(player))
        {
            this.playersParticleEffect.get(player).cancel();
            this.playersParticleEffect.remove(player);
        }
    }

    public EffectManager getEffectManager()
    {
        return this.effectManager;
    }

    @Override
    public String getName() { return "ParticleManager"; }
}
