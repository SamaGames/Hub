package net.samagames.hub.cosmetics.suits;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticRegistry;
import net.samagames.hub.cosmetics.common.CosmeticAccessibility;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

class SuitRegistry extends AbstractCosmeticRegistry<SuitCosmetic> {

    SuitRegistry(Hub hub) {
        super(hub);
    }

    @Override
    public void register() {
        SuitCosmetic pigSuit = new SuitCosmetic(this.hub, "pig", "Cochonou", CosmeticAccessibility.ALL,
                new String[] {

                }, new SuitCosmetic.SuitElement[]{
                    new SuitCosmetic.SuitElement(450, CosmeticRarity.RARE, makeHead("MHF_Pig")),
                new SuitCosmetic.SuitElement(300, CosmeticRarity.COMMON, makeArmor(new ItemStack(Material.LEATHER_CHESTPLATE), 0xFF99D7)),
                new SuitCosmetic.SuitElement(250, CosmeticRarity.COMMON, makeArmor(new ItemStack(Material.LEATHER_LEGGINGS), 0xFF99D7)),
                new SuitCosmetic.SuitElement(200, CosmeticRarity.COMMON, makeArmor(new ItemStack(Material.LEATHER_BOOTS), 0xFF99D7))
                });
    }

    private ItemStack makeArmor(ItemStack itemStack, int color) {
        LeatherArmorMeta meta = (LeatherArmorMeta)itemStack.getItemMeta();
        meta.setColor(Color.fromRGB(color));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    private ItemStack makeHead(String username) {
        ItemStack itemStack = new ItemStack(Material.SKULL, 1, (short)3);
        SkullMeta meta = (SkullMeta)itemStack.getItemMeta();
        meta.setOwner(username);
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
