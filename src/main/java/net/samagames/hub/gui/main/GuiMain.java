package net.samagames.hub.gui.main;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.tools.GlowEffect;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GuiMain extends AbstractGui
{
    public GuiMain(Hub hub)
    {
        super(hub);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Menu Principal");

        this.setSlotData(ChatColor.GOLD + "Zone " + ChatColor.GREEN + "VIP", Material.DIAMOND, 9, makeButtonLore(new String[] { "Testez les jeux avant tout le monde !" }, false, true), "beta_vip");
        this.setSlotData(ChatColor.GOLD + "Spawn", Material.BED, 18, makeButtonLore(new String[] { "Retournez au spawn grâce à la magie", "de la téléportation céleste !" }, false, true), "spawn");
        this.setSlotData(ChatColor.GOLD + "Parcours du ciel", Material.PACKED_ICE, 26, makeButtonLore(new String[] { "En espérant que vous atteignez le", "paradis..."}, false, true), "parkour");
        this.setSlotData(ChatColor.GOLD + "Informations", Material.EMPTY_MAP, 27, getInformationLore(), "none");
        this.setSlotData(ChatColor.GOLD + "Changer de hub", Material.ENDER_CHEST, 35, makeButtonLore(new String[] { "Cliquez pour ouvrir l'interface" }, true, false), "switch_hub");

        for (String gameIdentifier : this.hub.getGameManager().getGames().keySet())
        {
            AbstractGame game = this.hub.getGameManager().getGameByIdentifier(gameIdentifier);

            if (game.getSlotInMainMenu() >= 0)
            {
                String prefix;

                switch (game.getState())
                {
                    case NEW:
                        prefix = ChatColor.GREEN + "" + ChatColor.BOLD + "NOUVEAU !";
                        break;

                    case POPULAR:
                        prefix = ChatColor.YELLOW + "" + ChatColor.BOLD + "POPULAIRE !";
                        break;

                    case SOON:
                        prefix = ChatColor.RED + "" + ChatColor.BOLD + "BIENTOT !";
                        break;

                    case LOCKED:
                        prefix = ChatColor.GOLD + "" + ChatColor.MAGIC + "aaaaaaaa";
                        break;

                    default:
                        prefix = "";
                        break;
                }

                if (!prefix.equals(""))
                    prefix += " ";

                boolean glow = game.getState() == AbstractGame.State.NEW || game.getState() == AbstractGame.State.POPULAR;

                this.setSlotData(prefix + ChatColor.GOLD + game.getName(), ItemUtils.hideAllAttributes(game.getState() == AbstractGame.State.LOCKED ? new ItemStack(Material.IRON_FENCE, 1) : (glow ? GlowEffect.addGlow(game.getIcon()) : game.getIcon())), game.getSlotInMainMenu(), makeGameLore(game), "game_" + gameIdentifier);
            }
        }

        this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.equals("beta_vip") && SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.beta.vip"))
        {
            player.teleport(this.hub.getGameManager().getGameByIdentifier("beta_vip").getLobbySpawn());
        }
        else if (action.equals("switch_hub"))
        {
            this.hub.getGuiManager().openGui(player, new GuiSwitchHub(this.hub, 1));
        }
        else if (action.equals("spawn"))
        {
            player.teleport(this.hub.getPlayerManager().getSpawn());
        }
        else if (action.equals("parkour"))
        {
            player.teleport(this.hub.getParkourManager().getParkours().get(0).getFail());
        }
        else if (action.startsWith("game"))
        {
            String[] actions = action.split("_");
            AbstractGame game = this.hub.getGameManager().getGameByIdentifier(actions[1]);

            if (game.getState() == AbstractGame.State.LOCKED || game.getState() == AbstractGame.State.SOON)
            {
                player.sendMessage(ChatColor.RED + "Ce jeu n'est pas disponible.");
                return;
            }

            if (clickType == ClickType.LEFT)
            {
                player.teleport(game.getLobbySpawn());
            }
            else if (clickType == ClickType.RIGHT && game.getWebsiteDescriptionURL() != null)
            {
                this.hub.getGuiManager().closeGui(player);
                new FancyMessage(ChatColor.YELLOW + "Cliquez sur ").then("[Accéder]").color(ChatColor.GOLD).style(ChatColor.BOLD).link(game.getWebsiteDescriptionURL()).then(" pour accéder aux règles du jeu.").color(ChatColor.YELLOW).send(player);
            }
        }
    }

    private static String[] makeButtonLore(String[] description, boolean clickOpen, boolean clickTeleport)
    {
        List<String> lore = new ArrayList<>();
        String[] loreArray = new String[] {};

        if (description != null)
        {
            for (String string : description)
                lore.add(ChatColor.GRAY + string);

            if (clickOpen || clickTeleport)
                lore.add("");
        }

        if (clickOpen)
            lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour ouvrir le menu");

        if (clickTeleport)
            lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour être téléporté");

        return lore.toArray(loreArray);
    }

    private static String[] makeGameLore(AbstractGame game)
    {
        List<String> lore = new ArrayList<>();
        String[] loreArray = new String[] {};

        if (game.getState() != AbstractGame.State.LOCKED && game.getState() != AbstractGame.State.SOON)
        {
            lore.add(ChatColor.DARK_GRAY + game.getCategory());
            lore.add("");

            if (game.getDescription() != null)
            {
                for (String line : game.getDescription())
                    lore.add(ChatColor.GRAY + line);

                lore.add("");
            }

            if (game.getDevelopers() != null)
            {
                lore.add(ChatColor.GOLD + "Développeur" + (game.getDevelopers().length > 1 ? "s" : "") + " : ");
                lore.add(ChatColor.GRAY + StringUtils.join(game.getDevelopers(), ", "));
                lore.add("");
            }

            int players = game.getOnlinePlayers();

            lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez gauche pour être téléporté");

            if (game.getWebsiteDescriptionURL() != null)
                lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez droit pour lire les règles");

            lore.add("");
            lore.add(ChatColor.GRAY + "Il y a actuellement " + ChatColor.GOLD + players + ChatColor.GRAY + " joueur" + (players > 1 ? "s" : ""));
            lore.add(ChatColor.GRAY + "sur ce" + (game.isGroup() ? "s" : "") + " jeu" + (game.isGroup() ? "x" : "") + ".");
        }
        else
        {
            lore.add(ChatColor.RED + "Prochainement...");
        }

        return lore.toArray(loreArray);
    }

    private static String[] getInformationLore()
    {
        return new String[] {
                ChatColor.DARK_GRAY + "Site internet : " + ChatColor.GRAY + "https://www.samagames.net",
                ChatColor.DARK_GRAY + "Forum : " + ChatColor.GRAY + "https://www.samagames.net/forum/",
                ChatColor.DARK_GRAY + "Boutique : " + ChatColor.GRAY + "http://shop.samagames.net/",
                "",
                ChatColor.DARK_GRAY + "TeamSpeak : " + ChatColor.GRAY + "ts.samagames.net"
        };
    }
}