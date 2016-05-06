package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class GadgetCosmetic extends AbstractCosmetic
{
    private final Class<? extends AbstractDisplayer> clazz;
    private final int cooldown;

    GadgetCosmetic(Hub hub, long storageId, String displayName, ItemStack icon, int stars, CosmeticRarity rarity, CosmeticAccessibility accessibility, String[] description, Class<? extends AbstractDisplayer> clazz, int cooldown)
    {
        super(hub, storageId, displayName, icon, stars, rarity, accessibility, description);

        this.clazz = clazz;
        this.cooldown = cooldown;
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack stack = super.getIcon(player);
        net.minecraft.server.v1_9_R1.ItemStack craftStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound tagCompound = craftStack.getTag();

        if (tagCompound == null)
            tagCompound = new NBTTagCompound();

        tagCompound.setLong("gadget-key", this.getStorageId());

        craftStack.setTag(tagCompound);

        return CraftItemStack.asBukkitCopy(craftStack);
    }

    public Class<? extends AbstractDisplayer> getDisplayerClass()
    {
        return this.clazz;
    }

    public int getCooldown()
    {
        return this.cooldown;
    }
}
