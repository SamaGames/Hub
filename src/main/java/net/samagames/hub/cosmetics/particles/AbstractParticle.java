package net.samagames.hub.cosmetics.particles;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.tools.ParticleEffects;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public abstract class AbstractParticle extends AbstractCosmetic
{
    private final ItemStack icon;
    private final ParticleEffects particleEffect;

    public AbstractParticle(String databaseName, String displayName, ItemStack icon, String[] description, ParticleEffects particleEffect)
    {
        super(databaseName);

        this.icon = icon;
        this.particleEffect = particleEffect;

        ItemMeta meta = this.icon.getItemMeta();
        meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD + displayName);

        ArrayList<String> lores = new ArrayList<>();

        for(String str : description)
            lores.add(ChatColor.GRAY + str);

        meta.setLore(lores);
        this.icon.setItemMeta(meta);
    }

    @Override
    public void handleUse(Player player)
    {
        Hub.getInstance().getCosmeticManager().getParticleManager().enableCosmetic(player, this);
        player.sendMessage(ChatColor.GREEN + "Vous voilà dominé par ces magnifiques particules...");
    }

    public ItemStack getIcon()
    {
        return this.icon;
    }

    public ParticleEffects getParticleEffect()
    {
        return this.particleEffect;
    }
}
