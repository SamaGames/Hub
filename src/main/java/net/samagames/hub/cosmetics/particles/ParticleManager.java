package net.samagames.hub.cosmetics.particles;

import de.slikey.effectlib.Effect;
import de.slikey.effectlib.EffectManager;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class ParticleManager extends AbstractCosmeticManager<ParticleCosmetic>
{
    private final Map<UUID, Effect> playersParticleEffect;
    private final EffectManager effectManager;

    public ParticleManager(Hub hub)
    {
        super(hub, new ParticleRegistry(hub));

        this.playersParticleEffect = new HashMap<>();
        this.effectManager = new EffectManager(hub.getEffectLib());
    }

    @Override
    public void enableCosmetic(Player player, ParticleCosmetic cosmetic, ClickType clickType, NullType useless)
    {
        try
        {
            Effect particleEffectObject = cosmetic.getParticleEffect().getConstructor(EffectManager.class).newInstance(this.effectManager);
            particleEffectObject.setEntity(player);
            particleEffectObject.infinite();
            particleEffectObject.start();

            this.playersParticleEffect.put(player.getUniqueId(), particleEffectObject);
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vous voilà noyé sous les particules...");
        }
        catch (ReflectiveOperationException e)
        {
            this.hub.getCosmeticManager().log(Level.SEVERE, "Can't create EntityEffect object to " + player.getName() + "'s particle effect!");
        }
    }

    @Override
    public void disableCosmetic(Player player, ParticleCosmetic cosmetic, boolean logout, NullType useless)
    {
        if (this.playersParticleEffect.containsKey(player))
        {
            this.playersParticleEffect.get(player).cancel();
            this.playersParticleEffect.remove(player);
        }

        if (!logout)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre effet disparait dans l'ombre...");
    }

    @Override
    public void update() {}

    @Override
    public boolean restrictToOne()
    {
        return true;
    }

    public EffectManager getEffectManager()
    {
        return this.effectManager;
    }
}
