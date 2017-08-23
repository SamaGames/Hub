package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
class GadgetCosmetic extends AbstractCosmetic
{
    private final Class<? extends AbstractDisplayer> clazz;
    private final int cooldown;

    GadgetCosmetic(Hub hub, int storageId, Class<? extends AbstractDisplayer> clazz, int cooldown) throws Exception
    {
        super(hub, "Gadget", storageId);

        this.clazz = clazz;
        this.cooldown = cooldown;
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack stack = super.getIcon(player);
        net.minecraft.server.v1_12_R1.ItemStack craftStack = CraftItemStack.asNMSCopy(stack);

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
