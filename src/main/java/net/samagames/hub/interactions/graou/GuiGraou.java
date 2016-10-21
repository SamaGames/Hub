package net.samagames.hub.interactions.graou;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.ItemUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

class GuiGraou extends AbstractGui
{
    private static final ItemStack PEARL_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNmFlOTUxMTIwMzk0ZjM2OGYyMjUwYjdjM2FkM2ZiMTJjZWE1NWVjMWIyZGI1YTk0ZDFmYjdmZDRiNmZhIn19fQ==");

    private final Graou parent;

    GuiGraou(Hub hub, Graou parent)
    {
        super(hub);
        this.parent = parent;
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
        this.setSlotData(PEARL_HEAD, 10, "pearl_0");
        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("pearl_"))
        {
            this.parent.openBox(player);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }
}
