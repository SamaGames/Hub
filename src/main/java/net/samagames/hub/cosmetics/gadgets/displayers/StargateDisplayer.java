package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.DirectionUtils;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Random;

public class StargateDisplayer extends AbstractDisplayer
{
    private final Location basePortalLocation;
    private final boolean axisX;

    public StargateDisplayer(Player player)
    {
        super(player);

        this.basePortalLocation = this.baseLocation.getBlock().getLocation();
        float angleDegree = this.baseLocation.getYaw();

        if(angleDegree >= 45 && angleDegree <= 135)
            angleDegree = 90;
        else if(angleDegree >= 135 && angleDegree <= 225)
            angleDegree = 180;
        else if(angleDegree >= 225 && angleDegree <= 315)
            angleDegree = 270;
        else
            angleDegree = 0;

        double angleRadian = Math.toRadians(angleDegree);

        this.basePortalLocation.add(Math.cos(angleRadian) * 3,0, Math.sin(angleRadian) * 3);
        this.axisX = (DirectionUtils.getPlayerDirection(this.player) == DirectionUtils.Directions.NORTH || DirectionUtils.getPlayerDirection(this.player) == DirectionUtils.Directions.SOUTH);

        this.addBlocksToUse(this.createPortalFrame(this.basePortalLocation, DyeColor.ORANGE, this.axisX, false));

        Random random = new Random();
        Location randomizedLocation = this.baseLocation.add(random.nextInt(80) - 40, 100, random.nextInt(80) - 40);

        boolean done = false;

        for(int i = 0; i < 5; i++)
        {
            if(this.canPlaceExitAt(randomizedLocation))
            {
                done = true;
                break;
            }

            randomizedLocation = this.baseLocation.add(random.nextInt(80) - 40, 100, random.nextInt(80) - 40);
        }

        if(!done)
        {

        }
    }

    public HashMap<Location, SimpleBlock> createPortalFrame(Location basePortalLocation, DyeColor portalColor, boolean axisX, boolean horizontally)
    {
        HashMap<Location, SimpleBlock> blocks = new HashMap<>();

        if(horizontally)
        {

        }
        else
        {
            if(axisX)
            {

            }
            else
            {

            }
        }

        return blocks;
    }

    @Override
    public void display()
    {

    }

    @Override
    public void handleInteraction(Entity who, Entity with)
    {

    }

    public boolean canPlaceExitAt(Location randomizedLocation)
    {
        boolean flag = true;

        int firstX = randomizedLocation.clone().subtract(3.0D, 0.0D, 0.0D).getBlockX();
        int secondX = randomizedLocation.clone().add(3.0D, 0.0D, 0.0D).getBlockX();
        int firstZ = randomizedLocation.clone().getBlockZ();
        int secondZ = randomizedLocation.clone().add(0.0D, 0.0D, 6.0D).getBlockZ();

        for(int x = Math.min(firstX, secondX); x <= Math.max(firstX, secondX); x++)
        {
            for(int z = Math.min(firstZ, secondZ); z <= Math.max(firstZ, secondZ); z++)
            {
                Location blockLocation = new Location(randomizedLocation.getWorld(), x, randomizedLocation.getY(), z);

                if(blockLocation.getBlock().getType() != Material.AIR)
                    flag = false;
            }
        }

        return flag;
    }

    @Override
    public boolean canUse()
    {
        return true;
    }
}
