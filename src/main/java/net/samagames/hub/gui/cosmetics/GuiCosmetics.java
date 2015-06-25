package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.disguises.DisguiseCosmetic;
import net.samagames.hub.cosmetics.gadgets.GadgetCosmetic;
import net.samagames.hub.cosmetics.particles.ParticleCosmetic;
import net.samagames.hub.cosmetics.pets.PetCosmetic;
import net.samagames.hub.gui.AbstractGui;
import net.samagames.hub.utils.GuiUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class GuiCosmetics extends AbstractGui
{
    private final ArrayList<Integer> slots;

    public GuiCosmetics()
    {
        this.slots = new ArrayList<>();

        for(int i = 1; i <= 4; i++)
            this.slots.add(i);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = Bukkit.createInventory(null, 54, "Caverne aux trésors");

        this.randomIcon(1, ChatColor.DARK_AQUA + "●" + ChatColor.AQUA + " Humeurs " + ChatColor.DARK_AQUA + "●", Material.BLAZE_POWDER, new String[]{
                ChatColor.GRAY + "Montrez-nous comment vous vous sentez :)"
        }, DyeColor.LIGHT_BLUE, "particles");

        this.randomIcon(2, ChatColor.DARK_GREEN + "▲" + ChatColor.GREEN + " Montures " + ChatColor.DARK_GREEN + "▲", Material.SADDLE, new String[]{
                ChatColor.GRAY + "Un fidèle compagnon qui vous suit",
                ChatColor.GRAY + "où que vous soyez."
        }, DyeColor.GREEN, "pets");

        this.randomIcon(6, ChatColor.DARK_RED + "◼" + ChatColor.RED + " Déguisements " + ChatColor.DARK_RED + "◼", Material.SKULL_ITEM, new String[]{
                ChatColor.GRAY + "Entrez dans la peau d'un autre",
                ChatColor.GRAY + "personnage !"
        }, DyeColor.RED, "disguises");

        this.randomIcon(7, ChatColor.GOLD + "★" + ChatColor.YELLOW + " Gadgets " + ChatColor.GOLD + "★", Material.FIREWORK, new String[]{
                ChatColor.GRAY + "Animez le serveur en exposant vos",
                ChatColor.GRAY + "nombreux gadgets venant du futur !"
        }, DyeColor.YELLOW, "gadgets");

        this.setSlotData(ChatColor.DARK_PURPLE + "◢" + ChatColor.LIGHT_PURPLE + " Jukebox " + ChatColor.DARK_PURPLE + "◣", Material.JUKEBOX, 31, new String[]{
                ChatColor.GRAY + "Devenez un véritable SamaDJ !"
        }, "jukebox");
        this.drawLineOfGlass(31, DyeColor.PURPLE, "jukebox");

        this.setSlotData(GuiUtils.getBackItem(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        if(action.equals("particles"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmeticsCategory<ParticleCosmetic>("Particules", Hub.getInstance().getCosmeticManager().getParticleManager(), true));
        }
        else if(action.equals("pets"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmeticsCategory<PetCosmetic>("Montures", Hub.getInstance().getCosmeticManager().getPetManager(), true));
        }
        else if(action.equals("disguises"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmeticsCategory<DisguiseCosmetic>("Déguisements", Hub.getInstance().getCosmeticManager().getDisguiseManager(), true));
        }
        else if(action.equals("gadgets"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiCosmeticsCategory<GadgetCosmetic>("Gadgets", Hub.getInstance().getCosmeticManager().getGadgetManager(), false));
        }
        else if(action.equals("jukebox"))
        {
            Hub.getInstance().getGuiManager().openGui(player, new GuiJukebox());
        }
        else if(action.equals("back"))
        {
            Hub.getInstance().getGuiManager().closeGui(player);
        }
    }

    @Override
    public void onClose(Player player)
    {
        player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1.0F, 1.0F);
    }

    private void randomIcon(int base, String displayName, Material material, String[] description, DyeColor color, String action)
    {
        Random random = new Random();
        int randomized = random.nextInt(this.slots.size());
        int slot = base + (this.slots.get(randomized) * 9);

        this.slots.remove(randomized);

        this.setSlotData(displayName, material, slot, description, action);
        this.drawLineOfGlass(slot, color, action);
    }

    private void drawLineOfGlass(int baseSlot, DyeColor color, String action)
    {
        int slot = baseSlot - 9;

        while(slot >= 0)
        {
            if(this.inventory.getItem(slot) == null)
                this.setSlotData(ChatColor.GRAY + "", new ItemStack(Material.STAINED_GLASS_PANE, 1, color.getData()), slot, null, action);

            slot -= 9;
        }
    }
}
