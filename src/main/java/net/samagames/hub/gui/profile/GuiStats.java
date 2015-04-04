package net.samagames.hub.gui.profile;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GuiStats extends AbstractGui
{
    private final String name;
    private final UUID uuid;
    private final boolean fromClickMe;

    public GuiStats(String name, UUID uuid, boolean fromClickMe)
    {
        this.name = name;
        this.uuid = uuid;
        this.fromClickMe = fromClickMe;
    }

    public GuiStats(Player player)
    {
        this(player.getName(), player.getUniqueId(), false);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Statistiques de " + this.name);

        this.setSlotData(GuiUtils.getBackItem(), 8, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
        {
            if(this.fromClickMe)
                Hub.getInstance().getGuiManager().openGui(player, new GuiClickMe(this.name, this.uuid));
            else
                Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
    }
}
