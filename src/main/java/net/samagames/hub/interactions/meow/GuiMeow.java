package net.samagames.hub.interactions.meow;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
        this.drawFish(player, "vip", 12);
        this.drawFish(player, "vipplus", 14);

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("unlock_"))
        {
            String bonusKey = action.split("_")[1];

            // TODO: Unlocking

            this.parent.playThankYou();
            this.parent.update(player);
        }
        else if (action.equals("unlocked"))
        {
            player.sendMessage(Meow.TAG + ChatColor.RED + "Vous ne pouvez récupérer ce bonus pour le moment.");
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
        }
    }

    private void drawFish(Player player, String bonusKey, int slot)
    {
        Bonus bonus = null; // TODO: Persistance

        if (bonus == null)
            return;

        boolean unlocked = bonus.isUnlocked(player);

        ItemStack stack = new ItemStack(unlocked ? Material.RAW_FISH : Material.COOKED_FISH);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(bonus.getDisplayName());

        List<String> lore = new ArrayList<>();

        if (unlocked)
        {
            lore.add(ChatColor.RED + "Vous ne pourrez récupérer votre");
            lore.add(ChatColor.RED + "bonus seulement dans :");
            lore.add("");

            Timestamp lastUnlock = bonus.getLastUnlock(player);

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(lastUnlock.getNanos());
            calendar.add(Calendar.MONTH, 1);

            long timeInMillis = calendar.getTimeInMillis() - new Date().getTime();

            long days = TimeUnit.MILLISECONDS.toDays(timeInMillis);
            timeInMillis -= TimeUnit.DAYS.toMillis(days);
            long hours = TimeUnit.MILLISECONDS.toHours(timeInMillis);
            timeInMillis -= TimeUnit.HOURS.toMillis(hours);
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timeInMillis);
            timeInMillis -= TimeUnit.MINUTES.toMillis(minutes);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timeInMillis);

            String line = "";

            if (days > 0)
                line += days + " jour" + (days > 1 ? "s" : "") + " ";

            if (hours > 0)
                line += hours + " heure" + (hours > 1 ? "s" : "") + " ";

            if (minutes > 0)
                line += minutes + " minute" + (minutes > 1 ? "s" : "") + " ";

            if (seconds > 0)
                line += seconds + " seconde" + (seconds > 1 ? "s" : "");

            lore.add(ChatColor.RED + line);
        }
        else
        {
            lore.add(ChatColor.GRAY + "Récupérer votre bonus dès");
            lore.add(ChatColor.GRAY + "maintenant !");
        }

        meta.setLore(lore);
        stack.setItemMeta(meta);

        this.setSlotData(stack, slot, unlocked ? "unlocked" : "unlock_" + bonusKey);
    }

    private interface Bonus
    {
        void unlock(Player player);
        Timestamp getLastUnlock(Player player);
        boolean isUnlocked(Player player);
        String getDisplayName();
    }
}
