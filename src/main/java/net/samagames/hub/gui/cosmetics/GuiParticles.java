package net.samagames.hub.gui.cosmetics;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.particles.ParticleCosmetic;
import net.samagames.hub.cosmetics.particles.ParticleManager;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class GuiParticles extends AbstractGui
{
    @Override
    public void display(Player player)
    {
        int lines = 0;
        int slot = 0;

        for(ParticleCosmetic particle : Hub.getInstance().getCosmeticManager().getParticleManager().getRegistry().getElements().values())
        {
            slot++;

            if(slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        this.inventory = Bukkit.createInventory(null, 9 + (lines * 9) + (9 * 2), "Particules");

        this.update(player);

        player.openInventory(this.inventory);
    }

    @Override
    public void update(Player player)
    {
        int[] baseSlots = { 10, 11, 12, 13, 14, 15, 16 };
        int lines = 0;
        int slot = 0;

        for(ParticleCosmetic particle : Hub.getInstance().getCosmeticManager().getParticleManager().getRegistry().getElements().values())
        {
            this.setSlotData(particle.getIcon(player), (baseSlots[slot] + (lines * 9)), "particle_" + particle.getDatabaseName());

            slot++;

            if(slot == 7)
            {
                slot = 0;
                lines++;
            }
        }

        this.setSlotData(ChatColor.AQUA + "Vous avez " + SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).getStars() + " étoiles", Material.NETHER_STAR, this.inventory.getSize() - 6, null, "none");
        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");
        this.setSlotData(ChatColor.RED + "Supprimer votre effet actuel", Material.FLINT_AND_STEEL, this.inventory.getSize() - 4, null, "delete");
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.startsWith("particle_"))
        {
            String particle = action.split("_")[1];
            ParticleManager manager = Hub.getInstance().getCosmeticManager().getParticleManager();
            manager.enableCosmetic(player, manager.getRegistry().getElementByStorageName(particle));
        }
        else if(action.equals("delete"))
        {
            Hub.getInstance().getCosmeticManager().getParticleManager().disableCosmetic(player, false);
            player.sendMessage(ChatColor.GREEN + "Votre effet disparait dans l'ombre...");
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmetics());
        }
    }
}
