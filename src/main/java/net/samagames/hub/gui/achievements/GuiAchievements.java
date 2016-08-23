package net.samagames.hub.gui.achievements;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.AchievementCategory;
import net.samagames.api.achievements.AchievementProgress;
import net.samagames.api.achievements.IncrementationAchievement;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.profile.GuiProfile;
import net.samagames.tools.ItemUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class GuiAchievements extends AbstractGui
{
    private static final ItemStack LOCKED_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhjNTNiY2U4YWU1OGRjNjkyNDkzNDgxOTA5YjcwZTExYWI3ZTk0MjJkOWQ4NzYzNTEyM2QwNzZjNzEzM2UifX19");
    private static final ItemStack UNLOCKED_BLUE_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI2ZTM0NjI4N2EyMWRiZmNhNWI1OGMxNDJkOGQ1NzEyYmRjODRmNWI3NWQ0MzE0ZWQyYTgzYjIyMmVmZmEifX19");
    private static final ItemStack UNLOCKED_GOLD_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM3ZjdiNzJmYzNlNzMzODI4ZmNjY2MwY2E4Mjc4YWNhMjYzM2FhMzNhMjMxYzkzYTY4MmQxNGFjNTRhYTBjNCJ9fX0=");
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm");

    private AchievementCategory category;
    private int page;

    public GuiAchievements(Hub hub, AchievementCategory category, int page)
    {
        super(hub);

        this.category = category;
        this.page = page;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Objectifs (Page " + (this.page + 1) + ")");

        this.setSlotData(AbstractGui.getBackIcon(), 49, "back");

        List<ItemStack> itemStackList = new ArrayList<>();
        List<AchievementCategory> categories = new ArrayList<>();

        SamaGamesAPI.get().getAchievementManager().getAchievementsCategories().stream().filter(category -> category.getParent() == this.category).forEach(category ->
        {
            ItemStack itemStack = category.getIcon().clone();

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', category.getDisplayName()));

            List<String> lore = new ArrayList<>();
            lore.add("");

            for (String line : category.getDescription())
                lore.add(ChatColor.GRAY + line);

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);

            itemStackList.add(itemStack);
            categories.add(category);
        });

        SamaGamesAPI.get().getAchievementManager().getAchievements().stream().filter(achievement -> achievement.getParentCategoryID() == this.category).forEach(achievement ->
        {
            boolean unlocked = achievement.isUnlocked(player.getUniqueId());

            ItemStack itemStack = (unlocked ? UNLOCKED_GOLD_HEAD : LOCKED_HEAD).clone();

            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setDisplayName((unlocked ? ChatColor.GREEN : ChatColor.RED) + ChatColor.translateAlternateColorCodes('&', achievement.getDisplayName()));

            List<String> lore = new ArrayList<>();
            lore.add("");

            for (String line : achievement.getDescription())
                lore.add(ChatColor.GRAY + line);

            lore.add("");

            AchievementProgress progress = achievement.getProgress(player.getUniqueId());

            if (unlocked)
            {
                Date unlockDate = new Date();
                unlockDate.setTime(progress.getUnlockTime().getTime());

                lore.add(ChatColor.DARK_GRAY + "Vous avez débloqué cet objectif le :");
                lore.add(ChatColor.DARK_GRAY + DATE_FORMATTER.format(unlockDate));
            }
            else if (!(achievement instanceof IncrementationAchievement))
            {
                lore.add(ChatColor.DARK_GRAY + "Cet objectif n'est pas encore débloqué.");
            }
            else
            {
                int target = (progress == null ? ((IncrementationAchievement) achievement).getObjective() : (((IncrementationAchievement) achievement).getObjective() - progress.getProgress()));

                lore.add(ChatColor.DARK_GRAY + "Vous devez effectuer cette action encore");
                lore.add(ChatColor.DARK_GRAY + String.valueOf(target) + " fois pour débloquer cet objectif.");
            }

            itemMeta.setLore(lore);

            itemStack.setItemMeta(itemMeta);
            itemStackList.add(itemStack);
        });

        for (int i = 0; i < this.page; i++)
        {
            for (int j = 0; j < 28; j++)
            {
                if (!itemStackList.isEmpty())
                    itemStackList.remove(0);

                if (!categories.isEmpty())
                    categories.remove(0);
            }
        }

        if (this.page > 0)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if (itemStackList.size() > 28)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 2) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");

        while (itemStackList.size() > 28)
            itemStackList.remove(28);

        for (int i = 0; i < itemStackList.size(); i++)
            this.setSlotData(itemStackList.get(i), 10 + i % 7 + i / 7 * 9, i < categories.size() ? "category_" + categories.get(i).getID() : "");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        if (action.startsWith("category_"))
        {
            int id = Integer.parseInt(action.substring(9));
            this.hub.getGuiManager().openGui(player, new GuiAchievements(this.hub, SamaGamesAPI.get().getAchievementManager().getAchievementCategoryByID(id), 0));
        }
        else if (action.equals("page_back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiAchievements(this.hub, this.category, this.page - 1));
        }
        else if (action.equals("page_next"))
        {
            this.hub.getGuiManager().openGui(player, new GuiAchievements(this.hub, this.category, this.page + 1));
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, this.category == null ? new GuiProfile(this.hub) : new GuiAchievements(this.hub, this.category.getParent(), 0));
        }
    }
}
