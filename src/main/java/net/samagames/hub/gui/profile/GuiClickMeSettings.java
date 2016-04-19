package net.samagames.hub.gui.profile;

import net.samagames.api.settings.IPlayerSettings;
import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

class GuiClickMeSettings extends GuiSettings
{
    GuiClickMeSettings(Hub hub)
    {
        super(hub, -1);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 45, "Paramètres du ClickMe");
        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        this.drawSetting(player, "ClickMe", new ItemStack(Material.WOOD_BUTTON, 1), 10, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, votre ClickMe",
                ChatColor.GRAY + "sera activé. Les joueurs pourront y accéder en",
                ChatColor.GRAY + "cliquant sur vous dans les hubs ou via la",
                ChatColor.GRAY + "commande " + ChatColor.GOLD + "/click <pseudo>" + ChatColor.GRAY + "."
        }, new GuiSettingsCallback()
        {
            @Override
            public boolean get(IPlayerSettings setting)
            {
                return setting.isClickOnMeActivation();
            }

            @Override
            public void invert(IPlayerSettings setting)
            {
                setting.setClickOnMeActivation(!this.get(setting));
            }
        });

        this.drawSetting(player, "Vue des statistiques", new ItemStack(Material.ENCHANTED_BOOK, 1), 11, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                ChatColor.GRAY + "pourront voir vos statistiques des jeux dans",
                ChatColor.GRAY + "votre ClickMe."
        }, new GuiSettingsCallback()
        {
            @Override
            public boolean get(IPlayerSettings setting)
            {
                return setting.isAllowStatisticOnClick();
            }

            @Override
            public void invert(IPlayerSettings setting)
            {
                setting.setAllowStatisticOnClick(!this.get(setting));
            }
        });

        this.drawSetting(player, "Vue des pièces", new ItemStack(Material.GOLD_INGOT, 1), 12, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                ChatColor.GRAY + "pourront voir votre nombre de pièces dans",
                ChatColor.GRAY + "votre ClickMe."
        }, new GuiSettingsCallback() {
            @Override
            public boolean get(IPlayerSettings setting) {
                return setting.isAllowCoinsOnClick();
            }

            @Override
            public void invert(IPlayerSettings setting) {
                setting.setAllowCoinsOnClick(!get(setting));
            }
        });

        this.drawSetting(player, "Vue des étoiles", new ItemStack(Material.NETHER_STAR, 1), 13, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, les joueurs",
                ChatColor.GRAY + "pourront voir votre nombre d'étoiles dans",
                ChatColor.GRAY + "votre ClickMe."
        }, new GuiSettingsCallback()
        {
            @Override
            public boolean get(IPlayerSettings setting)
            {
                return setting.isAllowStarsOnclick();
            }

            @Override
            public void invert(IPlayerSettings setting)
            {
                setting.setAllowStarsOnclick(!this.get(setting));
            }
        });

        this.drawSetting(player, "Clic gauche", new ItemStack(Material.IRON_SWORD, 1), 16, new String[] {
                ChatColor.GRAY + "Quand cette option est activée, vous",
                ChatColor.GRAY + "accéderez au ClickMe des joueurs en faisant",
                ChatColor.GRAY + "un clic gauche dessus."
        }, new GuiSettingsCallback()
        {
            @Override
            public boolean get(IPlayerSettings setting)
            {
                return setting.isAllowClickOnOther();
            }

            @Override
            public void invert(IPlayerSettings setting)
            {
                setting.setAllowClickOnOther(!this.get(setting));
            }
        });

        this.setSlotData(getBackIcon(), 40, "back");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("back"))
            this.hub.getGuiManager().openGui(player, new GuiSettings(this.hub, 1));
        else
            super.onClick(player, stack, action, clickType);
    }
}
