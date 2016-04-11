package net.samagames.hub.gui.shop;

import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.gui.AbstractGui;
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

    public GuiConfirm(Hub hub, AbstractGui parent, ConfirmCallback callback)
    {
        super(hub);

        this.isInProgress = false;
        this.parent = parent;
        this.callback = callback;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 27, "Confirmer l'achat ?");

        this.setSlotData(ChatColor.GREEN + "Oui", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.GREEN.getWoolData()), 11, null, "confirm");
        this.setSlotData(ChatColor.RED + "Annuler", new ItemStack(Material.STAINED_GLASS, 1, DyeColor.RED.getWoolData()), 15, null, "cancel");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        if(action.equals("confirm"))
        {
            if (this.isInProgress)
            {
                player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Merci de patienter pendant le traitement de votre commande...");
                return;
            }

            this.hub.getExecutorMonoThread().execute(() ->
            {
                this.isInProgress = true;
                this.callback.run(this.parent);
            });
        }
        else
        {
            if (this.isInProgress)
            {
                player.sendMessage(PlayerManager.SHOPPING_TAG + ChatColor.RED + "Merci de patienter pendant le traitement de votre commande...");
                return;
            }

            this.hub.getGuiManager().openGui(player, this.parent);
        }
    }

    @FunctionalInterface
    public interface ConfirmCallback
    {
        void run(AbstractGui parent);
    }
}
