package net.samagames.hub.gui.achievements;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.AchievementCategory;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.Reflection;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class GuiAchievements extends AbstractGui
{
    private static final ItemStack HEAD_1 = GuiAchievements.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM3ZjdiNzJmYzNlNzMzODI4ZmNjY2MwY2E4Mjc4YWNhMjYzM2FhMzNhMjMxYzkzYTY4MmQxNGFjNTRhYTBjNCJ9fX0=");
    private static final ItemStack HEAD_2 = GuiAchievements.getCustomSkull("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhjNTNiY2U4YWU1OGRjNjkyNDkzNDgxOTA5YjcwZTExYWI3ZTk0MjJkOWQ4NzYzNTEyM2QwNzZjNzEzM2UifX19");

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
        this.inventory = this.hub.getServer().createInventory(null, 54, "Objectifs");

        this.setSlotData(AbstractGui.getBackIcon(), 50, "back");

        List<ItemStack> itemStackList = new ArrayList<>();
        List<AchievementCategory> categories = new ArrayList<>();

        SamaGamesAPI samaGamesAPI = SamaGamesAPI.get();
        samaGamesAPI.getAchievementManager().getAchievementsCategories().stream().filter(category -> category.getParent() == this.category).forEach(category ->
        {
            ItemStack itemStack = category.getIcon().clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(category.getDescription()));
            itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', category.getDisplayName()));
            itemStack.setItemMeta(itemMeta);
            itemStackList.add(itemStack);
            categories.add(category);
        });

        samaGamesAPI.getAchievementManager().getAchievements().stream().filter(achievement -> achievement.getParentCategoryID() == this.category).forEach(achievement ->
        {
            boolean unlocked = achievement.isUnlocked(player.getUniqueId());
            ItemStack itemStack = unlocked ? HEAD_1 : HEAD_2;
            itemStack = itemStack.clone();
            ItemMeta itemMeta = itemStack.getItemMeta();
            itemMeta.setLore(Arrays.asList(achievement.getDescription()));
            itemMeta.setDisplayName((unlocked ? ChatColor.GREEN : ChatColor.RED) + ChatColor.translateAlternateColorCodes('&', achievement.getDisplayName()));
            itemStack.setItemMeta(itemMeta);
            itemStackList.add(itemStack);
        });

        if (this.page > 0)
            this.setSlotData(GuiAchievements.getPageIcon(false), 53, "previous");

        for (int i = 0; i < this.page; i++)
            for (int j = 0; j < 28; j++)
            {
                if (itemStackList.size() > 0)
                    itemStackList.remove(0);
                if (categories.size() > 0)
                    categories.remove(0);
            }

        if (itemStackList.size() > 28)
            this.setSlotData(GuiAchievements.getPageIcon(true), 53, "next");

        while (itemStackList.size() > 28)
            itemStackList.remove(28);

        for (int i = 0; i < itemStackList.size(); i++)
            this.setSlotData(itemStackList.get(i), 10 + i % 7 + i / 7 * 9, i < categories.size() ? "category_" + categories.get(i).getID() : "");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action)
    {
        switch (action)
        {
            case "next":
                SamaGamesAPI.get().getGuiManager().openGui(player, new GuiAchievements(this.hub, this.category, this.page + 1));
                break ;
            case "previous":
                SamaGamesAPI.get().getGuiManager().openGui(player, new GuiAchievements(this.hub, this.category, this.page - 1));
                break ;
            default:
                if (action.startsWith("category_"))
                {
                    Integer id = Integer.parseInt(action.substring(9));
                    AchievementCategory category = SamaGamesAPI.get().getAchievementManager().getAchievementCategoryByID(id);
                    SamaGamesAPI.get().getGuiManager().openGui(player, new GuiAchievements(this.hub, category, 0));
                }
        }
    }

    private static ItemStack getCustomSkull(String url)
    {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        PropertyMap propertyMap = profile.getProperties();
        if (propertyMap == null)
            throw new IllegalStateException("Profile doesn't contain a property map");
        byte[] encodedData = url.getBytes();
        propertyMap.put("textures", new Property("textures", new String(encodedData)));
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        ItemMeta headMeta = head.getItemMeta();
        try
        {
            Reflection.setValue(headMeta, "profile", profile);
        }
        catch (NoSuchFieldException | IllegalAccessException e)
        {
            e.printStackTrace();
        }
        //Reflections.getField(headMetaClass, "profile", GameProfile.class).set(headMeta, profile);
        head.setItemMeta(headMeta);
        return head;
    }

    private static ItemStack getPageIcon(boolean next)
    {
        ItemStack itemStack = new ItemStack(Material.ARROW);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("Page " + (next ? "suivante" : "précédente"));
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
