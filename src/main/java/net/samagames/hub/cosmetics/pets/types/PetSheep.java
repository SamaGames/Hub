package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticSheep;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.ColorsConverter;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Sheep;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Dye;

public class PetSheep extends PetCosmetic<Sheep>
{
    public PetSheep(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticSheep.class);
    }

    @Override
    public void onUse(Player player)
    {
        final PetSheep instance = this;

        AbstractGui gui = new AbstractGui()
        {
            @Override
            public void display(Player player)
            {
                this.inventory = Bukkit.getServer().createInventory(null, 18, "Choisissez la couleur du mouton");

                int i = 0;

                for (DyeColor color : DyeColor.values())
                {
                    this.setSlotData(this.inventory, ColorsConverter.dyeToChat(color), makeDye(color), i, null, color.toString());
                    i++;
                }

                this.setSlotData(this.inventory, "Fermer", new ItemStack(Material.EMERALD), 17, null, "close");

                player.openInventory(this.inventory);
            }

            @Override
            public void onClick(Player player, ItemStack stack, String action)
            {
                Hub.getInstance().getGuiManager().closeGui(player);

                if (action.equals("close"))
                    return;

                Hub.getInstance().getCosmeticManager().getPetManager().enableCosmetic(player, instance, action);
            }

            public ItemStack makeDye(DyeColor color)
            {
                Dye dye = new Dye(Material.INK_SACK);
                dye.setColor(color);

                return dye.toItemStack(1);
            }
        };

        Hub.getInstance().getGuiManager().openGui(player, gui);
    }

    @Override
    public void applySettings(Sheep spawned, String settings)
    {
        spawned.setColor(DyeColor.valueOf(settings));
    }

    @Override
    public boolean isOverridingUse()
    {
        return true;
    }
}
