package net.samagames.hub.cosmetics.pets.types;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.cosmetics.pets.nms.CosmeticMagmaCube;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.MagmaCube;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PetMagmaCube extends PetCosmetic<MagmaCube>
{
    public PetMagmaCube(String databaseName, String displayName, ItemStack icon, String[] description)
    {
        super(databaseName, displayName, icon, description, CosmeticMagmaCube.class);
    }

    @Override
    public void onUse(Player player)
    {
        final PetMagmaCube instance = this;

        AbstractGui gui = new AbstractGui()
        {
            @Override
            public void display(Player player)
            {
                this.inventory = Bukkit.getServer().createInventory(null, 9, "Choisissez la taille du Slime");

                int i = 0;

                while (i < 3)
                {
                    this.setSlotData(this.inventory, "Taille " + (i + 1), new ItemStack(Material.MAGMA_CREAM, i + 1), i, null, "" + i);
                    i++;
                }

                this.setSlotData(this.inventory, "Fermer", new ItemStack(Material.EMERALD), 8, null, "close");

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
        };

        Hub.getInstance().getGuiManager().openGui(player, gui);
    }

    @Override
    public void applySettings(MagmaCube spawned, String settings)
    {
        spawned.setSize(Integer.parseInt(settings) + 1);
    }

    @Override
    public boolean isOverridingUse()
    {
        return true;
    }
}
