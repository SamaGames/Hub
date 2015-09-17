package net.samagames.hub.gui.profile.stats;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.DisplayedStat;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GuiGameStats extends AbstractGui
{
    private final String name;
    private final UUID uuid;
    private final AbstractGame game;

    public GuiGameStats(String name, UUID uuid, AbstractGame game)
    {
        this.name = name;
        this.uuid = uuid;
        this.game = game;
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Statistiques de " + this.name);
        int slot = 0;

        for(DisplayedStat stat : this.game.getDisplayedStats())
        {
            this.setSlotData(stat.getIcon(), slot, "stat_" + stat.getDatabaseName());
            slot++;
        }

        this.setSlotData(GuiUtils.getBackItem(), 8, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("stat_"))
        {
            String[] sp  = action.split("_");
            String stat = sp[1];
            if (sp.length > 2)
                stat = sp[1] + "_" +  sp[2];

            Hub.getInstance().getGuiManager().openGui(player, new GuiGameStat(this.name, this.uuid, this.game, stat));
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiStats(this.name, this.uuid, false));
        }
    }
}
