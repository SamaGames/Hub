package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_9_R1.EntityItem;
import net.minecraft.server.v1_9_R1.NBTTagCompound;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.lang.model.type.NullType;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.logging.Level;

public class GadgetManager extends AbstractCosmeticManager
{
    public static final Field AGE_FIELD;

    private final Map<UUID, Integer> cooldowns;
    private final Map<UUID, AbstractDisplayer> playersGadgets;
    private final List<Location> blocksUsed;


    public GadgetManager(Hub hub)
    {
        super(hub, new GadgetRegistry(hub));

        this.cooldowns = new HashMap<>();
        this.playersGadgets = new HashMap<>();
        this.blocksUsed = new ArrayList<>();
    }

    @Override
    public void enableCosmetic(Player player, AbstractCosmetic cosmetic, NullType useless)
    {
        player.getInventory().setItem(6, cosmetic.getIcon(player));
        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre gadget a été équipé dans votre barre d'action.");
    }

    @Override
    public void disableCosmetic(Player player, boolean logout, NullType useless)
    {
        if (!logout)
        {
            player.getInventory().setItem(6, null);
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre gadget a été déséquipé.");
        }
    }

    @Override
    public void restoreCosmetic(Player player) {}

    @Override
    public void update() {}

    public void useSelectedCosmetic(Player player, ItemStack cosmeticIcon)
    {
        if (!this.canUse(player))
        {
            int remaining = this.getCooldown(player);
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous devez attendre " + remaining + " seconde" + (remaining > 1 ? "s" : "") + " avant de pouvoir ré-utiliser un gadget.");

            return;
        }

        net.minecraft.server.v1_9_R1.ItemStack craftStack = CraftItemStack.asNMSCopy(cosmeticIcon);
        NBTTagCompound tagCompound = craftStack.getTag();
        String gadgetKey = tagCompound.getString("gadget-key");

        try
        {
            GadgetCosmetic gadget = (GadgetCosmetic) this.getRegistry().getElementByStorageName(gadgetKey);
            AbstractDisplayer displayer = gadget.getDisplayerClass().getDeclaredConstructor(Hub.class, Player.class).newInstance(this.hub, player);

            if (!displayer.canUse())
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget actuellement.");
                return;
            }

            boolean hasSpaceToUse = true;

            for (Location location : displayer.getBlocksUsed().keySet())
                if (location.getBlock().getType() != Material.AIR || this.blocksUsed.contains(location) || location.getBlock().getRelative(BlockFace.DOWN).getType() != Material.AIR)
                    hasSpaceToUse = false;

            if (!hasSpaceToUse)
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget ici.");
                return;
            }

            displayer.display();

            this.playersGadgets.put(player.getUniqueId(), displayer);

            new BukkitRunnable()
            {
                int timer = gadget.getCooldown();

                @Override
                public void run()
                {
                    this.timer--;

                    if (this.timer == 0)
                    {
                        cooldowns.remove(player.getUniqueId());
                        this.cancel();
                    }
                    else
                    {
                        cooldowns.put(player.getUniqueId(), this.timer);
                    }
                }
            }.runTaskTimerAsynchronously(this.hub, 20L, 20L);

            this.hub.getGuiManager().closeGui(player);
        }
        catch (ReflectiveOperationException ex)
        {
            this.hub.getCosmeticManager().log(Level.SEVERE, "An error occured when a displayer starts!");
        }
    }

    public void callbackGadget(AbstractDisplayer displayer)
    {
        displayer.getBlocksUsed().keySet().forEach(this.blocksUsed::remove);
        this.playersGadgets.remove(displayer.getPlayer().getUniqueId());
    }

    public AbstractDisplayer getPlayerGadget(Player player)
    {
        return getPlayerGadget(player.getUniqueId());
    }

    public AbstractDisplayer getPlayerGadget(UUID player)
    {
        if(this.playersGadgets.containsKey(player))
            return this.playersGadgets.get(player);
        else
            return null;
    }

    public int getCooldown(Player player)
    {
        if(this.cooldowns.containsKey(player.getUniqueId()))
            return this.cooldowns.get(player.getUniqueId());
        else
            return 0;
    }

    public boolean canUse(Player player)
    {
        return !this.hasGadget(player) && !this.cooldowns.containsKey(player.getUniqueId());

    }

    public boolean hasGadget(Player player)
    {
        return this.hasGadget(player.getUniqueId());
    }

    public boolean hasGadget(UUID player)
    {
        return this.playersGadgets.containsKey(player);
    }

    static
    {
        Field ageField = null;

        try
        {
            ageField = EntityItem.class.getDeclaredField("age");
            ageField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            e.printStackTrace();
        }

        AGE_FIELD = ageField;
    }
}
