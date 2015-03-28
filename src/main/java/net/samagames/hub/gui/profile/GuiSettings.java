package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.Gui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiSettings extends Gui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Paramètres");
        this.update(player);

        player.openInventory(this.inventory);
    }

    public void update(Player player)
    {
        this.drawSetting(player, "players", "Joueurs", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()), 10, new String[] {
                ChatColor.GRAY + "Cette option sert à cacher les joueurs",
                ChatColor.GRAY + "dans le hub pour une meilleure visibilité."
        });

        this.drawSetting(player, "chat", "Chat", new ItemStack(Material.BOOK_AND_QUILL, 1), 11, new String[] {
                ChatColor.GRAY + "Cette option sert à vous bloquer l'arrivée",
                ChatColor.GRAY + "de messages dans le chat."
        });

        this.drawSetting(player, "private-messages", "Messages privés", new ItemStack(Material.PAPER, 1), 12, new String[] {
                ChatColor.GRAY + "Cette option vous permet de ne pas recevoir",
                ChatColor.GRAY + "de messages privés de la part des joueurs,",
                ChatColor.GRAY + "excepté vos amis."
        });

        this.drawSetting(player, "friend-requests", "Demandes d'amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), 13, new String[] {
                ChatColor.GRAY + "Cette option vous permet de ne pas recevoir",
                ChatColor.GRAY + "de demandes d'amis de la part des joueurs."
        });

        this.drawSetting(player, "party-requests", "Demandes de groupe", new ItemStack(Material.LEASH, 1), 14, new String[] {
                ChatColor.GRAY + "Cette option vous permet de ne pas recevoir",
                ChatColor.GRAY + "de demandes de groupe de la part des joueurs,",
                ChatColor.GRAY + "excepté vos amis."
        });

        this.drawSetting(player, "jukebox", "Jukebox", new ItemStack(Material.JUKEBOX, 1), 15, new String[] {
                ChatColor.GRAY + "Cette option vous permet de ne pas entendre",
                ChatColor.GRAY + "la musique du Jukebox dans les hubs."
        });

        this.drawSetting(player, "clickme", "ClickMe", new ItemStack(Material.WOOD_BUTTON, 1), 16, new String[] {
                ChatColor.GRAY + "En cliquant, vous accéderez à un menu",
                ChatColor.GRAY + "avec les différents paramètres du ClickMe.",
                ChatColor.GRAY + "Avec celui-ci vous pourrez accéder aux statistiques",
                ChatColor.GRAY + "des joueurs en faisant un clic-gauche sur ceux-ci."
        });

        this.setSlotData(GuiUtils.getBackItem(), 40, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("setting_"))
        {
            if(action.equals("setting_clickme"))
            {
                return;
            }

            boolean enabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), action.split("_")[1], false);

            if(enabled)
                SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), action.split("_")[1], "false");
            else
                SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), action.split("_")[1], "true");

            this.update(player);
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
    }

    private void drawSetting(Player player, String name, String displayName, ItemStack icon, int slot, String[] description)
    {
        boolean enabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), name, false);
        ChatColor titleColor = (enabled ? ChatColor.GREEN : ChatColor.RED);
        ItemStack glassBlock = new ItemStack(Material.STAINED_GLASS, 1, (enabled ? DyeColor.GREEN.getData() : DyeColor.RED.getData()));

        this.setSlotData(titleColor + displayName, icon, slot, description, "setting_" + name);
        this.setSlotData(titleColor + (enabled ? "Activé" : "Désactivé"), glassBlock, slot + 9, null, "setting_" + name);
    }
}
