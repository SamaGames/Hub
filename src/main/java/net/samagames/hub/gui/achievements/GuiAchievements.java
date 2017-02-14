package net.samagames.hub.gui.achievements;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.achievements.Achievement;
import net.samagames.api.achievements.AchievementCategory;
import net.samagames.api.achievements.AchievementProgress;
import net.samagames.api.achievements.IncrementationAchievement;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.profile.GuiProfile;
import net.samagames.tools.ItemUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class GuiAchievements extends AbstractGui
{
    private static Table<Integer, Integer, List<Triple<Integer, Integer, Boolean>>> CACHE;

    private static final ItemStack LOCKED_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvZDhjNTNiY2U4YWU1OGRjNjkyNDkzNDgxOTA5YjcwZTExYWI3ZTk0MjJkOWQ4NzYzNTEyM2QwNzZjNzEzM2UifX19");
    private static final ItemStack UNLOCKED_BLUE_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvMTI2ZTM0NjI4N2EyMWRiZmNhNWI1OGMxNDJkOGQ1NzEyYmRjODRmNWI3NWQ0MzE0ZWQyYTgzYjIyMmVmZmEifX19");
    private static final ItemStack UNLOCKED_GOLD_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYWM3ZjdiNzJmYzNlNzMzODI4ZmNjY2MwY2E4Mjc4YWNhMjYzM2FhMzNhMjMxYzkzYTY4MmQxNGFjNTRhYTBjNCJ9fX0=");
    private static final DateFormat DATE_FORMATTER = new SimpleDateFormat("EEEE d MMMM yyyy à HH:mm", Locale.FRENCH);

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
        this.inventory = this.hub.getServer().createInventory(null, 54, "Objectifs" + (this.category != null ? " (Page " + (this.page + 1) + ")" : ""));

        int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
        int lines = 0;
        int slot = 0;

        for (AchievementCategory category : SamaGamesAPI.get().getAchievementManager().getAchievementsCategories())
        {
            if (category.getParent() == this.category)
            {
                ItemStack itemStack = category.getIcon().clone();

                ItemMeta itemMeta = itemStack.getItemMeta();
                itemMeta.setDisplayName(ChatColor.GOLD + ChatColor.translateAlternateColorCodes('&', category.getDisplayName()));

                List<String> lore = new ArrayList<>();

                for (String line : category.getDescription())
                    lore.add(ChatColor.GRAY + line);

                lore.add("");
                lore.add(ChatColor.GREEN + "Objectifs : " + ChatColor.BOLD + ChatColor.YELLOW + "?/?");
                lore.add("");
                lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour parcourir");

                itemMeta.setLore(lore);
                itemStack.setItemMeta(itemMeta);

                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

                this.setSlotData(itemStack, (baseSlots[slot] + (lines * 9)), "category_" + category.getID());

                slot++;

                if (slot == 7)
                {
                    slot = 0;
                    lines++;
                }
            }
        }

        if (this.category != null && CACHE.containsRow(this.category.getID()))
        {
            for (Triple<Integer, Integer, Boolean> achievementPair : CACHE.get(this.category.getID(), this.page))
            {
                Achievement achievement = SamaGamesAPI.get().getAchievementManager().getAchievementByID(achievementPair.getMiddle());

                boolean unlocked = achievement.isUnlocked(player.getUniqueId());

                ItemStack itemStack = (unlocked ? (achievementPair.getRight() ? UNLOCKED_GOLD_HEAD : UNLOCKED_BLUE_HEAD) : LOCKED_HEAD).clone();

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

                    lore.add(ChatColor.DARK_GRAY + "Vous avez débloqué cet objectif");
                    lore.add(ChatColor.DARK_GRAY + "le : " + ChatColor.GRAY + WordUtils.capitalize(DATE_FORMATTER.format(unlockDate)) + ChatColor.DARK_GRAY + ".");
                }
                else
                {
                    if (achievement instanceof IncrementationAchievement)
                    {
                        lore.add(ChatColor.DARK_GRAY + "Progression : " + ChatColor.GRAY + (progress == null ? 0 : progress.getProgress()) + "/" + ((IncrementationAchievement) achievement).getObjective());
                        lore.add("");
                    }

                    lore.add(ChatColor.DARK_GRAY + "Cet objectif n'est pas encore");
                    lore.add(ChatColor.DARK_GRAY + "débloqué.");
                }

                itemMeta.setLore(lore);
                itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
                itemStack.setItemMeta(itemMeta);

                this.setSlotData(itemStack, achievementPair.getLeft(), "none");
            }
        }

        if (this.category != null && this.page > 0)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if (this.category != null && CACHE.contains(this.category.getID(), this.page + 1))
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 2) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");

        this.setSlotData(AbstractGui.getBackIcon(), 49, "back");

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

    public static void createCache()
    {
        CACHE = HashBasedTable.create();

        /*
         * This is the base slots
         */
        int[] slots = { 36, 37, 38, 39, 40, 41, 42, 43, 44 };

        Map<Integer, List<Integer>> achievementParents = new HashMap<>();

        /*
         * We group the achievements with their category
         */
        SamaGamesAPI.get().getAchievementManager().getAchievements().forEach(achievement ->
        {
            if (!achievementParents.containsKey(achievement.getParentCategoryID().getID()))
                achievementParents.put(achievement.getParentCategoryID().getID(), new ArrayList<>());

            achievementParents.get(achievement.getParentCategoryID().getID()).add(achievement.getID());
        });

        /*
         * We iterate the categories
         */
        for (int categoryId : achievementParents.keySet())
        {
            List<List<Integer>> families = new ArrayList<>();

            // We copy the list to edit it in these loops
            CopyOnWriteArrayList<Integer> remaining = new CopyOnWriteArrayList<>(achievementParents.get(categoryId));

            int page = 0;
            int slotIndex = 0;
            int slot = slots[slotIndex];

            /*
             * We iterate the copied list
             */
            for (int achievementId : remaining)
            {
                /*
                 * If we have deleted this achievement id from the remaining list
                 * we continue the loop
                 */
                if (!remaining.contains(achievementId))
                    continue;

                Achievement achievement = SamaGamesAPI.get().getAchievementManager().getAchievementByID(achievementId);

                /*
                 * We are creating a string who contains the description of the achievement
                 * with only alphabetic characters
                 */
                String concatenated = Arrays.toString(achievement.getDescription());
                String cleared = concatenated.replaceAll("[^A-Za-z]+", "");

                ArrayList<Integer> family = new ArrayList<>();
                family.add(achievementId);

                /*
                 * We iterate the achievements around this one to find similarities in their
                 * description. It's permit to group them in a family
                 */
                for (int testAchievementId = achievementId - 2; testAchievementId < achievementId + 5; testAchievementId++)
                {
                    if (testAchievementId == achievementId)
                        continue;

                    Achievement remainingAchievement = SamaGamesAPI.get().getAchievementManager().getAchievementByID(testAchievementId);

                    /*
                     * A family can only contains incrementation achievements
                     */
                    if (remainingAchievement instanceof IncrementationAchievement)
                    {
                        /*
                         * We are creating a string who contains the description of the achievement
                         * with only alphabetic characters
                         */
                        String remainingAchievementConcatenated = Arrays.toString(remainingAchievement.getDescription());
                        String remainingAchievementCleared = remainingAchievementConcatenated.replaceAll("[^A-Za-z]+", "");

                        /*
                         * We calculate the Jaro Winkler similarity to determinate if the description of
                         * this around achievement is similar. If yes, we add this achievement to the
                         * family
                         */
                        if (StringUtils.getJaroWinklerDistance(cleared, remainingAchievementCleared) > 0.85D)
                        {
                            family.add(testAchievementId);
                            remaining.remove(new Integer(testAchievementId));
                        }
                    }
                }

                remaining.remove(new Integer(achievementId));
                families.add(family);
            }

            /*
             * We sort the families to have the 'big ones' at the beginning
             */
            Collections.sort(families, (o1, o2) ->
            {
                if ((((List) o1).size() > 1 && ((List) o2).size() > 1) || (((List) o1).size() == ((List) o2).size()))
                    return 0;
                else if (((List) o1).size() > ((List) o2).size())
                    return -1;
                else
                    return 1;
            });

            /*
             * Now we are creating columns. Columns are a list of slot<->achievement_id
             * who are stored in the cache to create easily the GUI's
             */
            List<Pair<List<Integer>, Boolean>> columns = new ArrayList<>();
            List<Integer> independentActualColumn = new ArrayList<>();
            boolean wasBig = false;

            /*
             * We iterate the families
             */
            for (List<Integer> family : families)
            {
                /*
                 * If the family contains more than 1 member, this is a 'big' family
                 */
                if (family.size() > 1)
                {
                    /*
                     * The descriptions contains a number who represent the amount of the
                     * action we have to repeat. With this, we can sort this amount to sort
                     * in the positive order
                     */
                    Collections.sort(family, (o1, o2) ->
                    {
                        String o1Concatenated = Arrays.toString(SamaGamesAPI.get().getAchievementManager().getAchievementByID((Integer) o1).getDescription());
                        String o2Concatenated = Arrays.toString(SamaGamesAPI.get().getAchievementManager().getAchievementByID((Integer) o2).getDescription());

                        if (Integer.parseInt(o1Concatenated.replaceAll("[\\D]", "")) < Integer.parseInt(o2Concatenated.replaceAll("[\\D]", "")))
                            return -1;
                        else
                            return 1;
                    });

                    wasBig = true;
                    columns.add(Pair.of(family, true));
                }
                else
                {
                    /*
                     * If the last iterated family was a 'big' one, we put a blank column
                     * to separate them in the GUI's
                     */
                    if (wasBig)
                    {
                        columns.add(Pair.of(null, null));
                        wasBig = false;
                    }

                    independentActualColumn.add(family.get(0));

                    /*
                     * A column can handle at least 5 items. When we hit this number, we
                     * merge the 'temporary' column into a separate one
                     */
                    if (independentActualColumn.size() == 5)
                    {
                        columns.add(Pair.of(independentActualColumn, false));
                        independentActualColumn = new ArrayList<>();
                    }
                }
            }

            /*
             * If we don't hit the 5 items, we can miss achievements so we
             * merge them in a column
             */
            if (!independentActualColumn.isEmpty())
                columns.add(Pair.of(independentActualColumn, false));

            /*
             * It's time to iterate the families and to attribute a inventory
             * slot to an achievement id
             */
            for (Pair<List<Integer>, Boolean> columnPair : columns)
            {
                /*
                 * If the sides of the Pair are both null, it's because it's an empty
                 * column we created to separate the 'big' columns from the 'individuals'
                 */
                if (columnPair.getLeft() == null && columnPair.getRight() == null)
                {
                    /*
                     * But, if this empty column has to be placed at the beginning or at
                     * this ending of the GUI, it's not necessary to have one
                     */
                    if (slotIndex == 0 || slotIndex == slots.length - 1)
                        continue;
                }
                else
                {
                    /*
                     * We iterate the members of the family
                     */
                    for (int achievementId : columnPair.getLeft())
                    {
                        /*
                         * If we don't have a list according to this category id and
                         * this page, we create one
                         */
                        if (!CACHE.contains(categoryId, page))
                            CACHE.put(categoryId, page, new ArrayList<>());

                        /*
                         * We put the data in the cache. If the family according to the columns
                         * was a 'big' one, we set the third parameter of the Triple to true.
                         */
                        CACHE.get(categoryId, page).add(Triple.of(slot, achievementId, columnPair.getRight()));

                        /*
                         * We decrease the slot to put the next achievement at the top of this
                         * one
                         */
                        slot -= 9;
                    }
                }

                slotIndex++;

                /*
                 * If the next index is out the array, we increment the page and we put
                 * back the index to 0
                 */
                if (slotIndex == slots.length)
                {
                    slotIndex = 0;
                    page++;
                }

                slot = slots[slotIndex];
            }
        }

        /*
         * Now the cache is ordered. End of this algorithm.
         */
    }
}
