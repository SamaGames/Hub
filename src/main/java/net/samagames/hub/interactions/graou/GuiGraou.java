package net.samagames.hub.interactions.graou;

import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

class GuiGraou extends AbstractGui
{
    private final Graou parent;
    private final Map<UUID, Pearl> pearls;

    GuiGraou(Hub hub, Graou parent)
    {
        super(hub);

        this.parent = parent;
        this.pearls = new HashMap<>();
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Graou");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        this.pearls.clear();

        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for (Pearl pearl : this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()))
        {
            this.setSlotData(PearlLogic.getIcon(pearl), (baseSlots[slot] + (lines * 9)), "pearl_" + pearl.getUUID());

            slot++;

            if (slot == 7)
            {
                slot = 0;
                lines++;
            }

            this.pearls.put(pearl.getUUID(), pearl);
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("pearl_"))
        {
            this.parent.openBox(player, this.pearls.get(UUID.fromString(action.split("_")[1])));
            this.hub.getGuiManager().closeGui(player);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }
}
