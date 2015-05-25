package net.samagames.hub.cosmetics.gadgets;

import net.minecraft.server.v1_8_R2.EntityItem;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class GadgetManager extends AbstractCosmeticManager<GadgetCosmetic>
{
    private final HashMap<UUID, Integer> cooldowns;
    private final HashMap<UUID, Integer> loopsIds;
    private final ArrayList<Location> blocksUsed;
    public Field ageField;

    public GadgetManager(Hub hub)
    {
        super(hub, new GadgetRegistry());

        this.cooldowns = new HashMap<>();
        this.loopsIds = new HashMap<>();
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
            boolean flag = true;
            boolean flag1 = true;

            try
            {
                AbstractDisplayer displayer = cosmetic.getDisplayerClass().getDeclaredConstructor(Player.class).newInstance(player);

                if (!displayer.canUse()) flag = false;
                if (!this.canUse(player)) flag = false;

                for (Location location : displayer.getBlocksUsed().keySet())
                {
                    if (location.getBlock().getType() != Material.AIR || this.blocksUsed.contains(location))
                        flag1 = false;
                }

                if (flag && flag1)
                    displayer.display();
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
            else if (flag && flag1)
            {
                this.loopsIds.put(player.getUniqueId(), Bukkit.getScheduler().scheduleAsyncRepeatingTask(this.hub, new Runnable() {
                    int timer = cosmetic.getCooldown();

                    @Override
                    public void run()
                    {
                        if (!cooldowns.containsKey(player.getUniqueId()))
                            cooldowns.put(player.getUniqueId(), timer);

                        timer--;
                        cooldowns.put(player.getUniqueId(), timer);

                        if (timer == 0)
                        {
                            cooldowns.remove(player.getUniqueId());
                            callbackLoop(player.getUniqueId());
                        }
                    }
                }, 20L, 20L));
            }
            else if (!flag)
            {
                player.sendMessage(ChatColor.RED + "Vous ne pouvez pas utiliser ce gadget actuellement.");
            }
            else if (!flag1)
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
            Bukkit.getScheduler().cancelTask(this.loopsIds.get(uuid));
            this.loopsIds.remove(uuid);
        }
    }

    public void callbackGadget(AbstractDisplayer displayer)
    {
        displayer.getBlocksUsed().keySet().forEach(this.blocksUsed::remove);
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
        if(this.cooldowns.containsKey(player.getUniqueId()))
        {
            if(!SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.gadgets.cooldownbypass"))
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
            else
            {
                this.cooldowns.remove(player.getUniqueId());
                return true;
            }
        }
        else
        {
            return true;
        }
    }

    @Override
    public String getName() { return "GadgetManager"; }
}
