package net.samagames.hub.gui.cosmetics.clothes;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.clothes.ClothRegistry;
import net.samagames.hub.cosmetics.clothes.ClothingSet;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.cosmetics.GuiCosmetics;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

class GuiClothingSet extends AbstractGui
{
    private final ClothingSet set;

    GuiClothingSet(Hub hub, ClothingSet set)
    {
        super(hub);

        this.set = set;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, this.set.getName());

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        this.setSlotData(this.set.getSet()[0].getIcon(player), 13, "cosmetic_" + this.set.getSet()[0].getStorageId());
        this.setSlotData(this.set.getSet()[1].getIcon(player), 22, "cosmetic_" + this.set.getSet()[1].getStorageId());
        this.setSlotData(this.set.getSet()[2].getIcon(player), 31, "cosmetic_" + this.set.getSet()[2].getStorageId());
        this.setSlotData(this.set.getSet()[3].getIcon(player), 40, "cosmetic_" + this.set.getSet()[3].getStorageId());

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
        this.setSlotData(ChatColor.RED + "Désactiver vos cosmétiques actuels", Material.FLINT_AND_STEEL, this.inventory.getSize() - 6, null, "delete");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("cosmetic_"))
        {
            int cosmetic = Integer.parseInt(action.split("_")[1]);
            this.hub.getCosmeticManager().getClothManager().enableCosmetic(player, this.hub.getCosmeticManager().getClothManager().getRegistry().getElementByStorageId(cosmetic), clickType);
        }
        else if (action.equals("delete"))
        {
            this.hub.getCosmeticManager().getClothManager().disableCosmetics(player, false);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiClothingSets(this.hub));
        }
    }
}
