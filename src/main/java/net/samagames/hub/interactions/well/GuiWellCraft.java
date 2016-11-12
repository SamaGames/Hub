package net.samagames.hub.interactions.well;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 12/11/2016
 */
public class GuiWellCraft extends AbstractGui
{
    private static final ItemStack CONFIRM;
    private static final ItemStack HELP;

    private final Well parent;
    private final int[] numbers;
    private final int[] expectedNumbers;

    GuiWellCraft(Hub hub, Well parent)
    {
        super(hub);

        this.parent = parent;
        this.numbers = new int[4];
        this.expectedNumbers = generateRandomNumbers();
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Créer une perle");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        this.setSlotData(makeRemainingPowders(), 13, "none");

        int[] slots = {20, 21, 23, 24};

        for (int i = 0; i < 4; i++)
            this.setSlotData(makeNumberSlot(i), slots[i], "slot_" + i);

        this.setSlotData(CONFIRM, 31, "confirm");
        this.setSlotData(HELP, this.inventory.getSize() - 6, "back");
        this.setSlotData(getBackIcon(), this.inventory.getSize() - 4, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("slot_"))
        {
            int slot = Integer.parseInt(action.split("_")[1]);

            if (clickType == ClickType.LEFT && this.getRemainingPowders() > 0)
            {
                this.numbers[slot]++;
            }
            else if (clickType == ClickType.RIGHT && this.numbers[slot] > 0)
            {
                this.numbers[slot]--;
            }

            this.update(player);
        }
        else if (action.equals("confirm"))
        {

        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiWell(this.hub, this.parent));
        }
    }

    @Override
    public void onClose(Player player)
    {
        this.parent.stop(player);
    }

    private ItemStack makeRemainingPowders()
    {
        int remaining = this.getRemainingPowders();

        ItemStack stack = new ItemStack(Material.SUGAR, remaining);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.YELLOW + "" + remaining + " poussières d'\u272F");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Poussières restantes");
        lore.add("");
        lore.add(ChatColor.GRAY + "Vous devez encore placer " + ChatColor.AQUA + remaining);
        lore.add(ChatColor.AQUA + "poussières d'\u272F" + ChatColor.GRAY + " avant de");
        lore.add(ChatColor.GRAY + "pouvoir valider.");

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private ItemStack makeNumberSlot(int slot)
    {
        ItemStack stack = new ItemStack(Material.SUGAR, this.numbers[slot]);
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.AQUA + "" + this.numbers[slot] + " poussière" + (this.numbers[slot] > 1 ? "s" : "") + " d'\u272F");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Emplacement n°" + (slot + 1));
        lore.add("");
        lore.add(ChatColor.GRAY + "Vous avez placé " + ChatColor.AQUA + "" + this.numbers[slot] + "poussière" + (this.numbers[slot] > 1 ? "s" : ""));
        lore.add(ChatColor.AQUA + "d'\u272F" + ChatColor.GRAY + " dans cet emplacement.");
        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "\u25B6 Clic gauche pour ajouter");
        lore.add(ChatColor.DARK_GRAY + "\u25B6 Clic droit pour retirer");

        meta.setLore(lore);

        stack.setItemMeta(meta);

        return stack;
    }

    private int getRemainingPowders()
    {
        return 64 - IntStream.of(this.numbers).sum();
    }

    private static int[] generateRandomNumbers()
    {
        int[] parts = new int[4];
        int n = 10;
        int sum = 0;

        Random random = new Random();

        for (int i = 0; i < 4; i++)
            parts[i] = random.nextInt(100);

        for (int i = 0; i < 4; i++)
        {
            parts[i] = (int) Math.floor(parts[i] * n / 100 + 10);
            sum += parts[i];
        }

        int diff = 64 - sum;
        int i = 0;

        while (diff != 0)
        {
            parts[i] += diff > 0 ? 1 : -1;
            diff += diff > 0 ? -1 : 1;
            i = (i + 1) % 4;
        }

        return parts;
    }

    static
    {
        CONFIRM = new ItemStack(Material.STAINED_GLASS, 1, DyeColor.GREEN.getWoolData());

        ItemMeta confirmMeta = CONFIRM.getItemMeta();
        confirmMeta.setDisplayName(ChatColor.GREEN + "Valider le placement");

        List<String> confirmLore = new ArrayList<>();
        confirmLore.add(ChatColor.RED + "Attention, Cette action est irréversible.");

        confirmMeta.setLore(confirmLore);

        CONFIRM.setItemMeta(confirmMeta);

        // ---

        HELP = new ItemStack(Material.WRITTEN_BOOK, 1);

        ItemMeta helpMeta = CONFIRM.getItemMeta();
        helpMeta.setDisplayName(ChatColor.YELLOW + "Aide");

        List<String> helpLore = new ArrayList<>();
        helpLore.add(ChatColor.DARK_GRAY + "Commence ça marche ?");
        helpLore.add("");
        helpLore.add(ChatColor.GRAY + "Vous devez placer un total de " + ChatColor.AQUA + "64");
        helpLore.add(ChatColor.AQUA + "poussières d'\u272F " + ChatColor.GRAY + "dans " + ChatColor.YELLOW + "4" + ChatColor.GRAY + " emplacements");
        helpLore.add(ChatColor.GRAY + "différents.");
        helpLore.add("");
        helpLore.add(ChatColor.GRAY + "A la manière d'une lotterie, plus vous");
        helpLore.add(ChatColor.GRAY + "vous rapprochez des nombres aléatoires,");
        helpLore.add(ChatColor.GRAY + "plus le niveau de votre perle sera élevé.");
        helpLore.add("");
        helpLore.add(ChatColor.YELLOW + "Note : " + ChatColor.GRAY + "Les numéros sont aléatoires et");
        helpLore.add(ChatColor.GRAY + "ne sont pas éloignés les uns des autres.");

        helpMeta.setLore(helpLore);

        HELP.setItemMeta(helpMeta);
    }
}