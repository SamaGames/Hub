package net.samagames.hub.interactions.graou;

import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

class GuiGraou extends AbstractGui
{
    private static final ItemStack NONE;

    private final Graou parent;
    private final Map<UUID, Pearl> pearls;

    GuiGraou(Hub hub, Graou parent)
    {
        super(hub);

        this.parent = parent;
        this.pearls = new HashMap<>();
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Graou");

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
        {
            this.update(player);
            this.hub.getServer().getScheduler().runTask(this.hub, () -> player.openInventory(this.inventory));
        });
    }

    @Override
    public void update(Player player)
    {
        this.pearls.clear();

        if (!this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()).isEmpty())
        {
            int[] baseSlots = {10, 11, 12, 13, 14, 15, 16};
            int lines = 0;
            int slot = 0;

            for (Pearl pearl : this.hub.getInteractionManager().getGraouManager().getPlayerPearls(player.getUniqueId()))
            {
                if (this.pearls.size() == 24)
                    break;

                this.setSlotData(this.hub.getInteractionManager().getGraouManager().getPearlLogic().getIcon(pearl), baseSlots[slot] + (lines * 9), "pearl_" + pearl.getUUID());

                slot++;

                if (slot == 7)
                {
                    slot = 0;
                    lines++;
                }

                this.pearls.put(pearl.getUUID(), pearl);
            }
        }
        else
        {
            this.setSlotData(NONE, 22, "none");
        }

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");
    }

    @Override
    public void onClose(Player player)
    {
        this.parent.stop(player);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if (action.startsWith("pearl_"))
        {
            this.parent.openBox(player, this.pearls.get(UUID.fromString(action.split("_")[1])));
            this.hub.getGuiManager().closeGui(player);
        }
        else if (action.equals("back"))
        {
            this.hub.getGuiManager().closeGui(player);
            this.parent.stop(player);
        }
    }

    static
    {
        NONE = new ItemStack(Material.IRON_FENCE, 1);
        ItemMeta meta = NONE.getItemMeta();
        meta.setDisplayName(ChatColor.RED + "Vous n'avez aucune perle :(");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Jouez à nos différents jeux pour");
        lore.add(ChatColor.GRAY + "obtenir des perles.");

        NONE.setItemMeta(meta);
    }
}
