package net.samagames.hub.cosmetics.gadgets.displayers;

import de.slikey.effectlib.effect.HelixEffect;
import de.slikey.effectlib.util.ParticleEffect;
import net.samagames.hub.Hub;
import net.samagames.hub.utils.SimpleBlock;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Random;

public class StargateDisplayer extends AbstractDisplayer
{
    private final Location basePortalLocation;
    private HelixEffect helixEffect;
    private BukkitTask portalTask;

    private HashMap<Location, SimpleBlock> portals;

    public StargateDisplayer(Player player)
    {
        super(player);

        this.basePortalLocation = this.baseLocation.add(3.0D, 0.0D, 0.0D).getBlock().getLocation();
        this.addBlocksToUse(this.createPortalFrame(this.basePortalLocation, DyeColor.ORANGE, false));

        this.portals = this.createPortal(this.basePortalLocation);
        this.addBlocksToUse(this.portals);

        Random random = new Random();
        Location randomizedLocation = this.baseLocation.add(random.nextInt(120) - 60, 255, random.nextInt(120) - 60);

        this.addBlocksToUse(this.createPortalFrame(randomizedLocation, DyeColor.BLUE, true));
    }

    public HashMap<Location, SimpleBlock> createPortalFrame(Location basePortalLocation, DyeColor portalColor, boolean exit)
    {
        HashMap<Location, SimpleBlock> blocks = new HashMap<>();

        if(exit)
        {
            blocks.put(basePortalLocation, new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(2.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(3.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(4.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(5.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(6.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(6.0D, 0.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(6.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(5.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(4.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(3.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(2.0D, 0.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
        }
        else
        {
            blocks.put(basePortalLocation, new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 3.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 6.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 6.0D, 0.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 6.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 3.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 2.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STAINED_CLAY, portalColor.getDyeData()));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(1.0D, 0.0D, 0.0D).add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(0.0D, 0.0D, 2.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
            blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 0.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 1.0D), new SimpleBlock(Material.STEP, 7));
            blocks.put(basePortalLocation.clone().add(1.0D, 0.0D, 2.0D), new SimpleBlock(Material.QUARTZ_BLOCK, 1));
        }

        return blocks;
    }

    public HashMap<Location, SimpleBlock> createPortal(Location basePortalLocation)
    {
        HashMap<Location, SimpleBlock> blocks = new HashMap<>();

        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 1.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 2.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 3.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 2.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 1.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 4.0D, 2.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().subtract(0.0D, 0.0D, 1.0D).add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 0.0D), new SimpleBlock(Material.AIR, 1));
        blocks.put(basePortalLocation.clone().add(0.0D, 5.0D, 1.0D), new SimpleBlock(Material.AIR, 1));

        return blocks;
    }

    @Override
    public void display()
    {
        for (Location block : this.blocksUsed.keySet())
        {
            block.getBlock().setType(this.blocksUsed.get(block).getType());
            block.getBlock().setData(this.blocksUsed.get(block).getData());
        }

        this.player.getLocation().setYaw(-90.0F);

        Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
        {
            for (Location block : this.portals.keySet())
            {
                block.getBlock().setType(Material.PORTAL);
                block.getBlock().setData((byte) 2);
            }

            this.helixEffect = new HelixEffect(Hub.getInstance().getCosmeticManager().getParticleManager().getEffectManager());
            this.helixEffect.particle = ParticleEffect.FIREWORKS_SPARK;
            this.helixEffect.radius = 12;
            this.helixEffect.setLocation(this.basePortalLocation);
            this.helixEffect.infinite();
            this.helixEffect.start();
        }, 20L * 2);

        Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
        {
            this.end();
            this.restore();
            this.portalTask.cancel();
            this.helixEffect.cancel();
        }, 20L * 15);
    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {

    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
