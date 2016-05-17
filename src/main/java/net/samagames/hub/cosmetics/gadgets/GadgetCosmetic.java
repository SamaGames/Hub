package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

class GadgetCosmetic extends AbstractCosmetic
{
    private final Class<? extends AbstractDisplayer> clazz;
    private final int cooldown;

    GadgetCosmetic(Hub hub, int storageId, Class<? extends AbstractDisplayer> clazz, int cooldown) throws Exception
    {
        super(hub, storageId);

        this.clazz = clazz;
        this.cooldown = cooldown;
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack stack = super.getIcon(player);
        net.minecraft.server.v1_9_R2.ItemStack craftStack = CraftItemStack.asNMSCopy(stack);

        NBTTagCompound tagCompound = craftStack.getTag();

        if (tagCompound == null)
            tagCompound = new NBTTagCompound();

        tagCompound.setInt("gadget-key", this.getStorageId());

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
