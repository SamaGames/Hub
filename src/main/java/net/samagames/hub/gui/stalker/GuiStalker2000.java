package net.samagames.hub.gui.stalker;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import net.samagames.tools.BungeeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Map;

public class GuiStalker2000 extends AbstractGui
{
    private final int page;
    private boolean teleportEnabled;

    public GuiStalker2000(int page)
    {
        this.page = page;
        this.teleportEnabled = false;
    }

    @Override
    public void display(Player player)
    {
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        Map<String, String> famous = jedis.hgetAll("famouslocations");
        jedis.close();

        this.inventory = Bukkit.createInventory(null, 54, "Stalker 2000");
        this.teleportEnabled = SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.stalker.teleport");

        boolean canSeeServer = SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.stalker.view");

        int[] baseSlots = { 10, 11, 12, 13, 14, 15, 16 };
        int line = 1;
        int slot = 0;

        int i = 0;
        boolean more = false;

        for(String famousData : famous.values())
        {
            if (i < (7 * (this.page - 1)))
            {
                i++;
                continue;
            }
            else if (i > ((7 * (this.page - 1) + (baseSlots.length * 3))))
            {
                more = true;
                break;
            }

            String formattedUsername = famousData.split("::")[0];
            String username = formattedUsername.split(" ")[1];
            String server = famousData.split("::")[1];

            ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
            SkullMeta headMeta = (SkullMeta) head.getItemMeta();
            headMeta.setOwner(username);
            headMeta.setDisplayName(formattedUsername);

            ArrayList<String> lores = new ArrayList<>();

            if (canSeeServer)
            {
                String formattedServer = server;

                if (server.length() == 2)
                {
                    formattedServer = ChatColor.GRAY + server.split("_")[0] + " " + server.split("_")[1];
                }


                lores.add(ChatColor.GOLD + "Actuellement sur : " + formattedServer);
                lores.add("");

                if (this.teleportEnabled)
                {
                    lores.add(ChatColor.GRAY + "▶ Clique pour rejoindre le joueur");
                }
                else
                {
                    lores.add(ChatColor.RED + "Devenez " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" + ChatColor.RED + " pour");
                    lores.add(ChatColor.RED + "afficher le serveur du joueur.");
                }
            }
            else
            {
                lores.add(ChatColor.GOLD + "Actuellement sur : " + ChatColor.GRAY + ChatColor.MAGIC + "zzz");
                lores.add("");
                lores.add(ChatColor.RED + "Devenez " + ChatColor.GREEN + "VIP" + ChatColor.RED + " pour");
                lores.add(ChatColor.RED + "afficher le serveur du joueur.");
            }

            headMeta.setLore(lores);
            head.setItemMeta(headMeta);

            this.setSlotData(head, baseSlots[slot] * line, "server:" + username);
            slot++;

            if (slot == baseSlots.length)
            {
                slot = 0;
                line++;
            }

            i++;
        }

        boolean followEnabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "tracker-follow", false);

        this.setSlotData(ChatColor.GOLD + "Suivi", Material.LEASH, this.inventory.getSize() - 6, new String[] {
                ChatColor.GRAY + "Etre informé de la connexion",
                ChatColor.GRAY + "d'un " + ChatColor.GOLD + "Coupaing" + ChatColor.GRAY + " via un message",
                ChatColor.GRAY + "envoyé dans le chat.",
                "",
                (canSeeServer ? followEnabled ? ChatColor.GREEN + "Cliquez pour désactiver" : ChatColor.RED + "Cliquez pour activer" : ChatColor.RED + "Fonction réservée aux " + ChatColor.GREEN + "VIP")
        }, "follow");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        this.setSlotData(ChatColor.GOLD + "Amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), this.inventory.getSize() - 4, new String[] {
                ChatColor.GRAY + "Cliquez pour savoir où sont",
                ChatColor.GRAY + "situés vos amis."
        }, "friends");

        if(this.page > 1)
            this.setSlotData(ChatColor.YELLOW + "« Page " + (this.page - 1), Material.PAPER, this.inventory.getSize() - 9, null, "page_back");

        if(more)
            this.setSlotData(ChatColor.YELLOW + "Page " + (this.page + 1) + " »", Material.PAPER, this.inventory.getSize() - 1, null, "page_next");

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        boolean canSeeServer = SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.stalker.view");
        boolean followEnabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "tracker-follow", false);

        this.setSlotData(ChatColor.GOLD + "Suivi", Material.LEASH, this.inventory.getSize() - 6, new String[] {
                ChatColor.GRAY + "Etre informé de la connexion",
                ChatColor.GRAY + "d'un " + ChatColor.GOLD + "Coupaing" + ChatColor.GRAY + " via un message",
                ChatColor.GRAY + "envoyé dans le chat.",
                "",
                (canSeeServer ? followEnabled ? ChatColor.GREEN + "Cliquez pour désactiver" : ChatColor.RED + "Cliquez pour activer" : ChatColor.RED + "Fonction réservée aux " + ChatColor.GREEN + "VIP" + ChatColor.RED + "s")
        }, "follow");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        this.setSlotData(ChatColor.GOLD + "Amis", new ItemStack(Material.RAW_FISH, 1, (short) 3), this.inventory.getSize() - 4, new String[] {
                ChatColor.GRAY + "Cliquez pour savoir où sont",
                ChatColor.GRAY + "situés vos amis."
        }, "friends");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("server:"))
        {
            String server = action.split(":")[1];

            if(this.teleportEnabled)
            {
                BungeeUtils.sendPlayerToServer(player, server);
            }
            else
            {
                player.sendMessage(ChatColor.RED + "Cette fonctionnalité n'est réservée qu'au " + ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" + ChatColor.RED + " !");
            }
        }
        else if(action.equals("follow"))
        {
            Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
            {
                boolean followEnabled = SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "tracker-follow", false);
                SamaGamesAPI.get().getSettingsManager().setSetting(player.getUniqueId(), "tracker-follow", String.valueOf(!followEnabled));

                this.update(player);
            });
        }
        else if(action.equals("friends"))
        {
            //TODO Fix friends gui
            //Hub.getInstance().getGuiManager().openGui(player, new GuiStalkerFriends2000());
        }
        else if(action.equals("page_back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiStalker2000((this.page - 1)));
        }
        else if(action.equals("page_next"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiStalker2000((this.page + 1)));
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
    }
}