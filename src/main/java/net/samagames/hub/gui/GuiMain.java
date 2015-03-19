package net.samagames.hub.gui;

import net.samagames.hub.Hub;
import net.samagames.hub.games.Game;
import net.samagames.hub.utils.BungeeUtils;
import net.samagames.hub.utils.EventUtils;
import net.samagames.permissionsbukkit.PermissionsBukkit;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class GuiMain extends Gui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 36, "Menu Principal");

        this.setSlotData(ChatColor.GOLD + "Zone BETA - " + ChatColor.GREEN + "VIP", Material.DIAMOND, 9, this.getLores(new String[] { "Testez les jeux avant tout le monde !" }, false, true, false), "beta_vip");
        this.setSlotData(ChatColor.GOLD + "Statistiques", Material.ENCHANTED_BOOK, 18, this.getLores(new String[] { "Retrouvez vos scores et classements !" }, true, false, false), "stats");
        this.setSlotData(ChatColor.GOLD + "Changer de hub", Material.ENDER_CHEST, 26, this.getLores(null, true, false, false), "switch_hub");

        if(!EventUtils.isCurrentlyEvent())
            this.setSlotData(ChatColor.GOLD + "Évenement", Material.IRON_FENCE, 17, this.getLores(new String[] { "Aucun évenement en cours !" }, false, false, false), "event");
        else
            this.setSlotData(ChatColor.GOLD + "Évenement - " + EventUtils.getCurrentEvent().getName(), Material.CAKE, 17, this.getLores(new String[] { EventUtils.getCurrentEvent().getDescription() }, true, false, false), "event");

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            Game game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if(game.getSlotInMainMenu() != -1)
                this.setSlotData(ChatColor.GOLD + game.getName(), game.getIcon(), game.getSlotInMainMenu(), this.getLores(game.getDescription(), false, true, true), "game_" + gameIdentifier);
        }

        if(PermissionsBukkit.hasPermission(player, "beta.staff"))
            this.setSlotData(ChatColor.GOLD + "Zone BETA - " + ChatColor.BLUE + "Staff", Material.COOKIE, 31, this.getLores(new String[] { "Testez les jeux avant tout le monde !", "Même les VIPs !"}, false, true, false), "beta_staff");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("beta_vip"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn());
        }
        else if(action.equals("beta_staff"))
        {
            player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_staff").getLobbySpawn());
        }
        else if(action.equals("stats"))
        {
            //TODO: Stats gui
        }
        else if(action.equals("switch_hub"))
        {
            //TODO: Switch hub gui
        }
        else if(action.equals("event"))
        {
            if(!EventUtils.isCurrentlyEvent())
                player.sendMessage(ChatColor.RED + "Aucun évenement n'est en cours !");
            else
                BungeeUtils.sendPlayerToServer(player, EventUtils.getCurrentEvent().getServer());
        }
        else if(action.startsWith("game"))
        {
            String[] actions = action.split("_");
            Game game = Hub.getInstance().getGameManager().getGameByIdentifier(actions[1]);

            if(clickType == ClickType.LEFT)
            {
                player.teleport(game.getLobbySpawn());
            }
            else if(clickType == ClickType.RIGHT)
            {
                //TODO: Queue
            }
        }
    }

    private String[] getLores(String[] description, boolean clickOpen, boolean clickTeleport, boolean clickQueue)
    {
        ArrayList<String> lores = new ArrayList<>();
        String[] loresArray = new String[] {};

        if(description != null)
        {
            for (String string : description)
                lores.add(ChatColor.GRAY + string);

            if (clickOpen || clickTeleport || clickQueue)
                lores.add("");
        }

        if(clickOpen)
            lores.add(ChatColor.DARK_GRAY + "▶ Clic droit pour ouvrir le menu");

        if(clickTeleport)
            lores.add(ChatColor.DARK_GRAY + "▶ Clic gauche pour être téléporté");

        if(clickQueue)
            lores.add(ChatColor.DARK_GRAY + "▶ Clic droit pour être mis en file d'attente");

        return lores.toArray(loresArray);
    }
}
