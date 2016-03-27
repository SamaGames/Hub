package net.samagames.hub.events;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.PlayerInventory;

public class GuiListener implements Listener
{
    private final Hub hub;

    public GuiListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event)
    {
        if (event.getWhoClicked() instanceof Player)
        {
            Player player = (Player) event.getWhoClicked();
            AbstractGui gui = (AbstractGui) this.hub.getGuiManager().getPlayerGui(player);

            if(event.getClickedInventory() instanceof PlayerInventory)
            {
                event.setCancelled(true);
                this.hub.getPlayerManager().getStaticInventory().doInteraction(player, event.getCurrentItem());
                return;
            }

            if (gui != null)
            {
                event.setCancelled(true);

                String action = gui.getAction(event.getSlot());

                if (action != null)
                    gui.onClick(player, event.getCurrentItem(), action, event.getClick());
            }
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event)
    {
        if (this.hub.getGuiManager().getPlayerGui(event.getPlayer()) != null)
            this.hub.getGuiManager().removeClosedGui((Player) event.getPlayer());
    }
}
