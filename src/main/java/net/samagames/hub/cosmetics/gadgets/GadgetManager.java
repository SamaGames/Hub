package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_8_R3.EntityItem;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class GadgetManager extends AbstractCosmeticManager<GadgetCosmetic>
{
    private final HashMap<UUID, Integer> cooldowns;
    private final HashMap<UUID, BukkitTask> loopsIds;
    private final HashMap<UUID, AbstractDisplayer> playersGadgets;
    private final ArrayList<Location> blocksUsed;
    public Field ageField;

    public GadgetManager(Hub hub)
    {
        super(hub, new GadgetRegistry());

        this.cooldowns = new HashMap<>();
        this.loopsIds = new HashMap<>();
        this.playersGadgets = new HashMap<>();
        this.blocksUsed = new ArrayList<>();

        this.patch();
    }

    public void patch()
    {
        this.hub.log(this, Level.INFO, "Patching server classes...");

        try
        {
            this.ageField = EntityItem.class.getDeclaredField("age");
            this.ageField.setAccessible(true);
        }
        catch (NoSuchFieldException e)
        {
            this.hub.log(this, Level.SEVERE, "Failed to patch a server class!");
            e.printStackTrace();
        }

        this.hub.log(this, Level.INFO, "Classes patched.");
    }

    @Override
    public void enableCosmetic(Player player, GadgetCosmetic cosmetic)
    {
        if (cosmetic.isOwned(player))
        {
            boolean hasRightToUse = true;
            boolean hasSpaceToUse = true;

            try
            {
                AbstractDisplayer displayer = cosmetic.getDisplayerClass().getDeclaredConstructor(Player.class).newInstance(player);

                hasRightToUse = displayer.canUse() && canUse(player);

                for (Location location : displayer.getBlocksUsed().keySet())
                {
                    if (location.getBlock().getType() != Material.AIR || this.blocksUsed.contains(location))
                        hasSpaceToUse = false;
                }

                if (hasRightToUse && hasSpaceToUse)
                {
                    displayer.display();
                    this.playersGadgets.put(player.getUniqueId(), displayer);

                    this.loopsIds.put(player.getUniqueId(), Bukkit.getScheduler().runTaskTimerAsynchronously(this.hub, new Runnable() {
                        int timer = cosmetic.getCooldown();

                        @Override
                        public void run() {
                            if (!cooldowns.containsKey(player.getUniqueId()))
                                cooldowns.put(player.getUniqueId(), timer);

                            timer--;
                            cooldowns.put(player.getUniqueId(), timer);

                            if (timer == 0) {
                                cooldowns.remove(player.getUniqueId());
                                callbackLoop(player.getUniqueId());
                            }
                        }
                    }, 20L, 20L));

                    Hub.getInstance().getGuiManager().closeGui(player);
                    return;
                }
            }
            catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex)
            {
                this.hub.log(this, Level.SEVERE, "An error occured when a displayer starts!");
                ex.printStackTrace();
            }

            if (!this.canUse(player))
            {
                player.sendMessage(ChatColor.RED + "Vous pourrez utiliser un gadget que dans " + this.getCooldown(player) + " secondes.");
            }
            else if (!hasRightToUse)
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget actuellement.");
            }
            else if (!hasSpaceToUse)
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget ici.");
            }
        }
        else
        {
            cosmetic.buy(player);
        }
    }

    @Override
    public void disableCosmetic(Player player, boolean logout) {}

    @Override
    public void restoreCosmetic(Player player) {}

    @Override
    public void update() {}

    public void callbackLoop(UUID uuid)
    {
        if(this.loopsIds.containsKey(uuid))
        {
            try{
                this.loopsIds.get(uuid).cancel();
            }catch(Exception e)
            {
                //In case of nullpointer
                e.printStackTrace();
            }
            this.loopsIds.remove(uuid);
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
        {
            return this.cooldowns.get(player.getUniqueId());
        }
        else
        {
            return 0;
        }
    }

    public boolean canUse(Player player)
    {
        if(this.hasGadget(player))
            return false;

        if(this.cooldowns.containsKey(player.getUniqueId()))
        {
            if(SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.gadgets.cooldownbypass"))
            {
                this.cooldowns.remove(player.getUniqueId());
                return true;
            }
            else
            {
                if (this.cooldowns.get(player.getUniqueId()) <= 0)
                {
                    this.cooldowns.remove(player.getUniqueId());
                    return true;
                }
                else
                {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean hasGadget(Player player)
    {
        return this.hasGadget(player.getUniqueId());
    }

    public boolean hasGadget(UUID player)
    {
        return this.playersGadgets.containsKey(player);
    }

    @Override
    public String getName() { return "GadgetManager"; }
}
