package net.samagames.hub.interactions.meow;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

class GuiMeow extends AbstractGui
{
    private final Meow parent;

    GuiMeow(Hub hub, Meow parent)
    {
        super(hub);
        this.parent = parent;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Meow");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        for (Bonus bonus : MeowManager.getBonus())
            this.setSlotData(bonus.getIcon(player.getUniqueId()), bonus.getSlot(), bonus.isAbleFor(player.getUniqueId()) ? "bonus_" + bonus.getId() : "taken");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("bonus_"))
        {
            int bonusId = Integer.parseInt(action.split("_")[1]);

            Bonus bonus = MeowManager.getBonusById(bonusId);

            if (bonus == null)
                return;

            bonus.take(player.getUniqueId());

            this.parent.playThankYou();
            this.parent.update(player);

            this.hub.getScoreboardManager().update(player);
            this.update(player);
        }
        else if (action.equals("taken"))
        {
            player.sendMessage(Meow.TAG + ChatColor.RED + "Vous ne pouvez récupérer ce bonus pour le moment.");
            player.playSound(player.getLocation(), Sound.ENTITY_CAT_HISS, 1.0F, 1.0F);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }
}
