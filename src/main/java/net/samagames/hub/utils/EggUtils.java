package net.samagames.hub.utils;

import net.minecraft.server.v1_9_R1.Items;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

public class EggUtils
{
    public static ItemStack getMonsterEgg(EntityType entityType)
    {
        net.minecraft.server.v1_9_R1.ItemStack egg = new net.minecraft.server.v1_9_R1.ItemStack(Items.SPAWN_EGG, 1);

        NBTTagCompound tag = new NBTTagCompound();

        NBTTagCompound entityTag = new NBTTagCompound();
        entityTag.setString("id", entityType.getName());

        tag.set("EntityTag", entityTag);

        egg.setTag(tag);

        return CraftItemStack.asBukkitCopy(egg);
    }
}
