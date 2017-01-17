package net.samagames.hub.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.function.BiConsumer;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 15/01/2017
 */
class ClothCosmetic extends AbstractCosmetic
{
    public enum ArmorSlot
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

    public ClothCosmetic(Hub hub, int storageId, ArmorSlot slot, ItemStack piece) throws Exception
    {
        super(hub, "Habit", storageId);

        this.slot = slot;
        this.piece = piece;
    }

    @Override
    public ItemStack getIcon(Player player)
    {
        ItemStack stack = super.getIcon(player);

        if (!this.isOwned(player))
        {
            ItemMeta meta = stack.getItemMeta();

            List<String> lore = meta.getLore();
            lore.add("");
            lore.add(ChatColor.YELLOW + "Clic droit pour prévisualiser");

            meta.setLore(lore);
            stack.setItemMeta(meta);
        }

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
