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
    private final int page;

    public GuiSettings(int page)
    {
        this.page = page;
    }

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
        if (this.page == 0)
        {
            this.drawSetting(player, "players", "Joueurs", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()), 10, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, vous verrez",
                    ChatColor.GRAY + "les joueurs autour de vous dans le hub. Dans le cas",
                    ChatColor.GRAY + "contraire, vous verrez seulement le " + ChatColor.GOLD + "Staff" + ChatColor.GRAY + ",",
                    ChatColor.GRAY + "les " + ChatColor.GOLD + "Coupaings" + ChatColor.GRAY + " et vos " + ChatColor.GOLD + "amis" + ChatColor.GRAY + "."
            });

            this.drawSetting(player, "chat", "Chat", new ItemStack(Material.BOOK_AND_QUILL, 1), 11, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "voir les messages des joueurs dans le chat."
            });

            this.drawSetting(player, "private-messages", "Messages privés", new ItemStack(Material.PAPER, 1), 12, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "recevoir des messages privés de la part des",
                    ChatColor.GRAY + "joueurs. Vos amis pourront quand même vous",
                    ChatColor.GRAY + "envoyer des messages."
            });

            this.drawSetting(player, "notifications", "Notifications", new ItemStack(Material.MAP, 1), 13, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, vous pourrez",
                    ChatColor.GRAY + "recevoir un signal sonore lorsqu'un joueur",
                    ChatColor.GRAY + "écrit votre nom dans le chat.",
            });

            this.drawSetting(player, "friend-requests", "Demandes d'amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), 14, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                    ChatColor.GRAY + "pourront vous envoyer des demandes d'amis."
            });

            this.drawSetting(player, "party-requests", "Demandes de groupe", new ItemStack(Material.LEASH, 1), 15, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                    ChatColor.GRAY + "pourront vous envoyer des demandes de groupe.",
                    ChatColor.GRAY + "Vos amis pourront quand même vous inviter",
                    ChatColor.GRAY + "dans un groupe."
            });

            this.drawSetting(player, "jukebox", "Jukebox", new ItemStack(Material.JUKEBOX, 1), 16, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, vous",
                    ChatColor.GRAY + "entenderez la musique du Jukebox dans",
                    ChatColor.GRAY + "les hubs."
            });
        }
        else if (this.page == 1)
        {
            this.drawSetting(player, "interactions", "Intéractions", new ItemStack(Material.COOKIE, 1), 10, new String[] {
                    ChatColor.GRAY + "Quand cette option est activée, les",
                    ChatColor.GRAY + "intéractions seront possibles avec votre",
                    ChatColor.GRAY + "joueur, comme par exemple celles avec les",
                    ChatColor.GRAY + "gadgets. Seuls vos " + ChatColor.GOLD + "amis" + ChatColor.GRAY + " pourront tout de même",
                    ChatColor.GRAY + "intéragir avec vous."
            });

            this.drawSetting(player, "clickme", "ClickMe", new ItemStack(Material.WOOD_BUTTON, 1), 16, new String[]{
                    ChatColor.GRAY + "En cliquant, vous accéderez à un menu",
                    ChatColor.GRAY + "avec les différents paramètres du ClickMe.",
                    ChatColor.GRAY + "Avec celui-ci vous pourrez accéder aux",
                    ChatColor.GRAY + "statistiques des joueurs en faisant un",
                    ChatColor.GRAY + "clic-gauche sur ceux-ci."
            });
        }

        if(this.page == 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.REDSTONE, this.inventory.getSize() - 9, null, "page_back");

        if(this.page == 0)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.REDSTONE, this.inventory.getSize() - 1, null, "page_next");

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

            SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), action.split("_")[1], String.valueOf(!enabled), () ->
            {
                Hub.getInstance().getPlayerManager().updateSettings(player, (action.equals("setting_jukebox")), false);
                this.update(player);
            });
        }
        else if(action.equals("page_back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiSettings((this.page - 1)));
        }
        else if(action.equals("page_next"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiSettings((this.page + 1)));
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
