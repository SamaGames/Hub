package net.samagames.hub.gui.main;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;

public class GuiMain extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Menu Principal");

        this.setSlotData(ChatColor.GOLD + "Zone BETA - " + ChatColor.GREEN + "VIP", Material.DIAMOND, 9, this.getLores(new String[] { "Testez les jeux avant tout le monde !" }, false, true), "beta_vip");
        this.setSlotData(ChatColor.GOLD + "Spawn", Material.BED, 18, this.getLores(null, false, true), "spawn");
        this.setSlotData(ChatColor.GOLD + "Parkour du Ciel", Material.PACKED_ICE, 26, this.getLores(null, false, true), "parkour");
        this.setSlotData(ChatColor.GOLD + "Informations", Material.EMPTY_MAP, 27, this.getInformationLores(), "none");
        this.setSlotData(ChatColor.GOLD + "Changer de hub", Material.ENDER_CHEST, 35, this.getLores(null, true, false), "switch_hub");
        this.setSlotData(ChatColor.GOLD + "Évenement", Material.IRON_FENCE, 17, this.getLores(new String[] { "Aucun évenement en cours !" }, false, false), "event");

        for(String gameIdentifier : Hub.getInstance().getGameManager().getGames().keySet())
        {
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(gameIdentifier);

            if(game.getSlotInMainMenu() != -1)
            {
                ArrayList<String> description = new ArrayList<>(Arrays.asList(game.getDescription()));
                String[] developpers = game.getDeveloppers();

                if (developpers != null)
                {
                    description.add("");
                    description.add(ChatColor.GOLD + "Développeur" + (developpers.length > 1 ? "s" : "") + " : " + ChatColor.GRAY + StringUtils.join(developpers, ", "));
                }

                this.setSlotData((game.isNew() ? ChatColor.GREEN + "" + ChatColor.BOLD + "NOUVEAU ! " + ChatColor.RESET : "") + ChatColor.GOLD + game.getName(), game.getIcon(), game.getSlotInMainMenu(), this.getLores(description.toArray(new String[description.size()]), false, true), "game_" + gameIdentifier);
            }
        }

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("beta_vip"))
        {
            if(SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.beta.vip"))
            {
                player.teleport(Hub.getInstance().getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn());
            }
        }
        else if(action.equals("switch_hub"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiSwitchHub(1));
        }
        else if(action.equals("spawn"))
        {
            player.teleport(Hub.getInstance().getPlayerManager().getLobbySpawn());
        }
        else if(action.equals("parkour"))
        {
            player.teleport(Hub.getInstance().getParkourManager().getParkours().get(0).getSpawn());
        }
        else if(action.startsWith("game"))
        {
            String[] actions = action.split("_");
            AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(actions[1]);

            if(!game.isLocked())
                player.teleport(game.getLobbySpawn());
            else
                player.sendMessage(ChatColor.RED + "Ce jeu n'est pas disponible !");
        }
    }

    private String[] getLores(String[] description, boolean clickOpen, boolean clickTeleport)
    {
        ArrayList<String> lores = new ArrayList<>();
        String[] loresArray = new String[] {};

        if(description != null)
        {
            for (String string : description)
                lores.add(ChatColor.GRAY + string);

            if (clickOpen || clickTeleport)
                lores.add("");
        }

        if(clickOpen)
            lores.add(ChatColor.DARK_GRAY + "▶ Clique pour ouvrir le menu");

        if(clickTeleport)
            lores.add(ChatColor.DARK_GRAY + "▶ Clique pour être téléporté");

        return lores.toArray(loresArray);
    }

    private String[] getInformationLores()
    {
        return new String[] {
                ChatColor.DARK_GRAY + "Site internet : " + ChatColor.GRAY + "http://samagames.net",
                ChatColor.DARK_GRAY + "Forum : " + ChatColor.GRAY + "http://samagames.net/forum/",
                ChatColor.DARK_GRAY + "Boutique : " + ChatColor.GRAY + "http://samagames.net/boutique/",
                "",
                ChatColor.DARK_GRAY + "TeamSpeak : " + ChatColor.GRAY + "ts.samagames.net"
        };
    }
}
