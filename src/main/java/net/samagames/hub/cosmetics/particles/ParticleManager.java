package net.samagames.hub.cosmetics.particles;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.tools.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class ParticleManager extends AbstractCosmeticManager<ParticleCosmetic>
{
    private HashMap<UUID, ParticleEffect> playersParticleEffect;

    public ParticleManager(Hub hub)
    {
        super(hub, new ParticleRegistry());
        this.playersParticleEffect = new HashMap<>();
    }

    @Override
    public void enableCosmetic(Player player, ParticleCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            this.playersParticleEffect.put(player.getUniqueId(), cosmetic.getParticleEffect());
            SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).set("cosmetics.particles.current", cosmetic.getDatabaseName());
            player.sendMessage(ChatColor.GREEN + "Vous voilà noyé sous les particules...");
        }
        else
        {
            cosmetic.buy(player);
        }
    }

    @Override
    public void disableCosmetic(Player player, boolean logout)
    {
        if (this.playersParticleEffect.containsKey(player.getUniqueId()))
            this.playersParticleEffect.remove(player.getUniqueId());

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
    public void update()
    {
        try
        {
            for(UUID uuid : this.playersParticleEffect.keySet())
            {
                Player player = Bukkit.getPlayer(uuid);

                if(player == null)
                    continue;

                this.playersParticleEffect.get(uuid).display(0.5F, 0.5F, 0.5F, 0.25F, 2, player.getLocation(), 100.0D);
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
