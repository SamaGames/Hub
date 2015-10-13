package net.samagames.hub.gui.profile.stats;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.gui.profile.GuiClickMe;
import net.samagames.hub.gui.profile.GuiProfile;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class GuiStats extends AbstractGui
{
    private final String name;
    private final UUID uuid;
    private final boolean fromClickMe;

    public GuiStats(String name, UUID uuid, boolean fromClickMe)
    {
        this.name = name;
        this.uuid = uuid;
        this.fromClickMe = fromClickMe;
    }

    public GuiStats(Player player)
    {
        this(player.getName(), player.getUniqueId(), false);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 9, "Statistiques de " + this.name);
        int slot = 0;

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            if(game.getDisplayedStats() != null && !game.getDisplayedStats().isEmpty())
            {
                this.setSlotData(ChatColor.GOLD + game.getGuiName(), game.getIcon(), slot, null, "game_" + game.getCodeName());
                slot++;
            }
        }

        this.setSlotData(GuiUtils.getBackItem(), 8, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("game_"))
        {
            String game = action.split("_")[1];
            Hub.getInstance().getGuiManager().openGui(player, new GuiGameStats(this.name, this.uuid, Hub.getInstance().getGameManager().getGameByIdentifier(game)));
        }
        else if(action.equals("back"))
        {
            if(this.fromClickMe)
                Hub.getInstance().getGuiManager().openGui(player, new GuiClickMe(this.name, this.uuid));
            else
                Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
    }
}
