package net.samagames.hub.cosmetics.suits;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class SuitInventory extends AbstractGui {

    private static final short colors[] = {14, 0, 0, 0, 0};
    private final SuitManager manager;

    public SuitInventory(Hub hub, SuitManager manager) {
        super(hub);
        this.manager = manager;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Habits");

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        int lines = 0;
        int slot = 0;

        for (SuitCosmetic cosmetic : this.manager.getRegistry().getElements().values())
        {
            if(!cosmetic.canView(player))
                continue;

            this.setSlotData(cosmetic.getIcon(player), lines * 3 + 2 + slot * 9, "cosmetic_" + cosmetic.getKey());

            int owned = cosmetic.getOwnedParts(player);
            this.setSlotData(new ItemStack(Material.STAINED_GLASS_PANE, owned, colors[owned]), lines * 3 + 3 + slot * 9, "none");

            slot++;

            if (slot == 6)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(getStarsIcon(player), 17, "none");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
    }
}
