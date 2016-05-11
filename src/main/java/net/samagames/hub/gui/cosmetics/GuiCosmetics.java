package net.samagames.hub.gui.cosmetics;

import net.samagames.hub.Hub;
import net.samagames.hub.gui.AbstractGui;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GuiCosmetics extends AbstractGui
{
    private final List<Integer> slots;

    public GuiCosmetics(Hub hub)
    {
        super(hub);

        this.slots = new ArrayList<>();

        for(int i = 1; i <= 4; i++)
            this.slots.add(i);
    }

    @Override
    public void display(Player player)
    {
        this.inventory = this.hub.getServer().createInventory(null, 54, "Caverne aux trésors");

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

        this.randomIcon(8, ChatColor.DARK_BLUE + "" + ChatColor.BLUE + "Ballons" + ChatColor.DARK_BLUE + "", Material.CLAY_BALL, new String[]{
                ChatColor.GRAY + "Des petits ballons au dessus de votre tête !"
        }, DyeColor.BLUE, "balloons");

        this.setSlotData(ChatColor.DARK_PURPLE + "◢" + ChatColor.LIGHT_PURPLE + " Jukebox " + ChatColor.DARK_PURPLE + "◣", Material.JUKEBOX, 31, new String[]{
                ChatColor.GRAY + "Devenez un véritable SamaDJ !"
        }, "jukebox");
        this.drawLineOfGlass(31, DyeColor.PURPLE, "jukebox");

        this.setSlotData(getBackIcon(), this.inventory.getSize() - 5, "back");

        player.openInventory(this.inventory);
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_OPEN, 1.0F, 1.0F);
    }

    @Override
    public void onClick(Player player, ItemStack stack, String action, ClickType clickType)
    {
        switch (action)
        {
            case "particles":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Particules", this.hub.getCosmeticManager().getParticleManager(), true));
                break;

            case "pets":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Montures", this.hub.getCosmeticManager().getPetManager(), true));
                break;

            case "disguises":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Déguisements", this.hub.getCosmeticManager().getDisguiseManager(), true));
                break;

            case "gadgets":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Gadgets", this.hub.getCosmeticManager().getGadgetManager(), true));
                break;

            case "jukebox":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Jukebox", this.hub.getCosmeticManager().getJukeboxManager(), false));
                break;

            case "balloons":
                this.hub.getGuiManager().openGui(player, new GuiCosmeticsCategory<>(this.hub, "Ballons", this.hub.getCosmeticManager().getBalloonManager(), true));
                break;

            case "back":
                this.hub.getGuiManager().closeGui(player);
                break;
        }
    }

    @Override
    public void onClose(Player player)
    {
        player.playSound(player.getLocation(), Sound.BLOCK_CHEST_CLOSE, 1.0F, 1.0F);
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
