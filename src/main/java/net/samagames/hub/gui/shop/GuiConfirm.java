package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GuiConfirm extends AbstractGui
{
    private final AbstractGui parent;
    private final ConfirmCallback callback;
    private boolean isInProgress;

    public GuiConfirm(AbstractGui parent, ConfirmCallback callback)
    {
        this.isInProgress = false;
        this.parent = parent;
        this.callback = callback;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 27, "Confirmer l'achat ?");

        this.setSlotData(ChatColor.GREEN + "Oui", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.GREEN.getWoolData()), 11, null, "confirm");
        this.setSlotData(ChatColor.RED + "Annuler", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.RED.getWoolData()), 15, null, "cancel");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        if(action.equals("confirm") && !this.isInProgress)
            Hub.getInstance().getExecutorMonoThread().execute(() -> {
                this.isInProgress = true;
                this.callback.run(parent);
            });
        else
            Hub.getInstance().getGuiManager().openGui(player, this.parent);
    }

    public AbstractGui getParent()
    {
        return parent;
    }



    public interface ConfirmCallback {

        void run(AbstractGui parent);
    }
}
