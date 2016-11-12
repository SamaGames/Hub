package net.samagames.hub.gui.main;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.RulesBook;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

class GuiRulesBooks extends AbstractGui
{
    private final List<ItemStack> books;

    GuiRulesBooks(Hub hub, AbstractGame game)
    {
        super(hub);

        this.books = new ArrayList<>();

        if (game.getRulesBooks() != null)
            for (RulesBook book : game.getRulesBooks())
                this.books.add(book.toItemStack());
    }

    @Override
    public void display(Player player)
    {
        int[] slots = new int[] { 10, 11, 12, 13, 14, 15, 16, 17 };
        int slot = 0;
        int lines = 1;

        for (ItemStack book : this.books)
        {
            slot++;

            if (slot == slots.length)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = this.hub.getServer().createInventory(null, 9 + (9 * lines) + (9 * 2), "Livre" + (this.books.size() > 1 ? "s" : "") + " de règles");

        slot = 0;
        lines = 0;

        for (int i = 0; i < this.books.size(); i++)
        {
            this.setSlotData(this.books.get(i), slots[slot] + (9 * lines), "rule_" + i);
            slot++;

            if (slot == slots.length)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.equals("back"))
        {
            this.hub.getGuiManager().openGui(player, new GuiMain(this.hub));
        }
        else if (action.startsWith("rule_"))
        {
            this.hub.getGuiManager().closeGui(player);

            ItemStack rulesBook = this.books.get(Integer.parseInt(action.replace("rule_", "")));
            ItemUtils.openWrittenBook(rulesBook, player);
        }
    }
}
