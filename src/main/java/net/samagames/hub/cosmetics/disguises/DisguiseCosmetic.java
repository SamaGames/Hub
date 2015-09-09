package net.samagames.hub.cosmetics.disguises;

import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.inventory.ItemStack;

public class DisguiseCosmetic extends AbstractCosmetic
{
    private final DisguiseType disguise;

    public DisguiseCosmetic(String key, String displayName, ItemStack icon, String[] description, DisguiseType disguise)
    {
        super("disguise", key, displayName, icon, description);
        this.disguise = disguise;
    }

    public DisguiseType getDisguiseType()
    {
        return this.disguise;
    }

    @Override
    public BaseComponent getBuyResponse()
    {
        TextComponent txt = new TextComponent("Disque acheté ! Re-cliquez pour l'ajouter à la liste d'attente.");
        txt.setColor(net.md_5.bungee.api.ChatColor.GREEN);
        return txt;
    }
}
