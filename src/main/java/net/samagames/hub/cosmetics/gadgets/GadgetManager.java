package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_9_R2.EntityItem;
import net.minecraft.server.v1_9_R2.NBTTagCompound;
import net.samagames.api.SamaGamesAPI;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.craftbukkit.v1_9_R2.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import javax.lang.model.type.NullType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Level;

public class GadgetManager extends AbstractCosmeticManager<GadgetCosmetic>
{
    public static final Random RANDOM = new Random();
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
    public void enableCosmetic(Player player, GadgetCosmetic cosmetic, ClickType clickType, boolean login, NullType useless)
    {
        player.getInventory().setItem(6, cosmetic.getIcon(player));

        if (!login)
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre gadget a été équipé dans votre barre d'action.");
    }

    @Override
    public void disableCosmetic(Player player, GadgetCosmetic cosmetic, boolean logout, boolean replace, NullType useless)
    {
        if (!logout)
        {
            player.getInventory().setItem(6, null);

            if (!replace)
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Votre gadget a été déséquipé.");
        }
    }

    @Override
    public void update() {}

    @Override
    public boolean restrictToOne()
    {
        return true;
    }

    public void useSelectedCosmetic(Player player, ItemStack cosmeticIcon)
    {
        if (!this.canUse(player))
        {
            int remaining = this.getCooldown(player);
            player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous devez attendre " + remaining + " seconde" + (remaining > 1 ? "s" : "") + " avant de pouvoir ré-utiliser un gadget.");

            return;
        }

        net.minecraft.server.v1_9_R2.ItemStack craftStack = CraftItemStack.asNMSCopy(cosmeticIcon);
        NBTTagCompound tagCompound = craftStack.getTag();
        int gadgetKey = tagCompound.getInt("gadget-key");

        try
        {
            GadgetCosmetic gadget = this.getRegistry().getElementByStorageId(gadgetKey);
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

            if (!hasSpaceToUse || player.getLocation().distanceSquared(this.hub.getPlayerManager().getSpawn()) < 100)
            {
                player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget ici.");
                return;
            }

            player.getInventory().setChestplate(null);
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

        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(displayer.getPlayer().getUniqueId());

        if (permissionsEntity.hasPermission("network.vipplus") && SamaGamesAPI.get().getSettingsManager().getSettings(displayer.getPlayer().getUniqueId()).isElytraActivated())
        {
            displayer.getPlayer().getInventory().setChestplate(new ItemStack(Material.ELYTRA));
            this.hub.getPlayerManager().getStaticInventory().setInventoryToPlayer(displayer.getPlayer());
        }
    }

    public AbstractDisplayer getPlayerGadget(Player player)
    {
        return getPlayerGadget(player.getUniqueId());
    }

    public AbstractDisplayer getPlayerGadget(UUID player)
    {
        if (this.playersGadgets.containsKey(player))
            return this.playersGadgets.get(player);
        else
            return null;
    }

    public int getCooldown(Player player)
    {
        if (this.cooldowns.containsKey(player.getUniqueId()))
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

    public boolean isBlockUsed(Location location)
    {
        for (AbstractDisplayer displayer : this.playersGadgets.values())
            if (displayer.isBlockUsed(location))
                return true;

        return false;
    }

    public boolean isInteracting(Player player)
    {
        for (AbstractDisplayer displayer : this.playersGadgets.values())
            if (displayer.isInteractionsEnabled() && displayer.isInteractingWith(player))
                return true;

        return false;
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
