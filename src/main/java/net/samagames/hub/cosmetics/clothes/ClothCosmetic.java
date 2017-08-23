package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;

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
public class ClothCosmetic extends AbstractCosmetic
{
    enum ArmorSlot
    {
        HELMET((player, stack) -> player.getInventory().setHelmet(stack)),
        CHESTPLATE((player, stack) -> player.getInventory().setChestplate(stack)),
        LEGGINGS((player, stack) -> player.getInventory().setLeggings(stack)),
        BOOTS((player, stack) -> player.getInventory().setBoots(stack));

        private final BiConsumer<Player, ItemStack> consumer;

        ArmorSlot(BiConsumer<Player, ItemStack> consumer)
        {
            this.consumer = consumer;
        }

        public void equip(Player player, ItemStack stack)
        {
            this.consumer.accept(player, stack);
        }
    }

    private final ArmorSlot slot;
    private final ItemStack piece;

    ClothCosmetic(Hub hub, int storageId, ArmorSlot slot) throws Exception
    {
        super(hub, "Habit", storageId);

        this.slot = slot;
        this.piece = this.getIcon().clone();
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack stack = super.getIcon(player);
        ItemMeta meta = stack.getItemMeta();

        List<String> lore = meta.getLore();
        lore.add("");
        lore.add(ChatColor.YELLOW + "Clic droit pour prévisualiser");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }

    public ArmorSlot getSlot()
    {
        return this.slot;
    }

    public ItemStack getPiece()
    {
        return this.piece;
    }
}
