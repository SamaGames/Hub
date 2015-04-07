package net.samagames.hub.gui.profile;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiSettings extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 45, "Paramètres");
        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        this.drawSetting(player, "players", "Joueurs", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()), 10, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, vous verrez",
                ChatColor.GRAY + "les joueurs autour de vous dans le hub."
        });

        this.drawSetting(player, "chat", "Chat", new ItemStack(Material.BOOK_AND_QUILL, 1), 11, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                ChatColor.GRAY + "voir les messages des joueurs dans le chat."
        });

        this.drawSetting(player, "private-messages", "Messages privés", new ItemStack(Material.PAPER, 1), 12, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                ChatColor.GRAY + "receoir des messages privés de la part des",
                ChatColor.GRAY + "joueurs. Vos amis pourront quand même vous",
                ChatColor.GRAY + "envoyer des messages."
        });

        this.drawSetting(player, "friend-requests", "Demandes d'amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), 13, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                ChatColor.GRAY + "pourront vous envoyer des demandes d'amis."
        });

        this.drawSetting(player, "party-requests", "Demandes de groupe", new ItemStack(Material.LEASH, 1), 14, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                ChatColor.GRAY + "pourront vous envoyer des demandes de groupe.",
                ChatColor.GRAY + "Vos amis pourront quand même vous inviter",
                ChatColor.GRAY + "dans un groupe."
        });

        this.drawSetting(player, "jukebox", "Jukebox", new ItemStack(Material.JUKEBOX, 1), 15, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, vous",
                ChatColor.GRAY + "entenderez la musique du Jukebox dans",
                ChatColor.GRAY + "les hubs."
        });

        this.drawSetting(player, "clickme", "ClickMe", new ItemStack(Material.WOOD_BUTTON, 1), 16, new String[] {
                ChatColor.GRAY + "En cliquant, vous accéderez à un menu",
                ChatColor.GRAY + "avec les différents paramètres du ClickMe.",
                ChatColor.GRAY + "Avec celui-ci vous pourrez accéder aux",
                ChatColor.GRAY + "statistiques des joueurs en faisant un",
                ChatColor.GRAY + "clic-gauche sur ceux-ci."
        });

        this.setSlotData(GuiUtils.getBackItem(), 40, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("setting_"))
        {
            if(action.equals("setting_clickme") && this.getSlot(action) == 16)
            {
                Hub.getInstance().getGuiManager().openGui(player, new GuiClickMeSettings());
                return;
            }

            boolean enabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), action.split("_")[1], true);

            if(enabled)
                SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), action.split("_")[1], "false");
            else
                SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), action.split("_")[1], "true");

            Hub.getInstance().getPlayerManager().updateSettings(player);
            this.update(player);
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiProfile());
        }
    }

    protected void drawSetting(Player player, String name, String displayName, ItemStack icon, int slot, String[] description)
    {
        boolean enabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), name, true);
        ChatColor titleColor = (enabled ? ChatColor.GREEN : ChatColor.RED);
        ItemStack glassBlock = new ItemStack(Material.STAINED_GLASS, 1, (enabled ? DyeColor.GREEN.getData() : DyeColor.RED.getData()));

        this.setSlotData(titleColor + displayName, icon, slot, description, "setting_" + name);
        this.setSlotData(titleColor + (enabled ? "Activé" : "Désactivé"), glassBlock, slot + 9, null, "setting_" + name);
    }
}
