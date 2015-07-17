package net.samagames.hub.cosmetics.gadgets.displayers;

import net.samagames.hub.utils.SimpleBlock;
import net.samagames.tools.DirectionUtils;
import org.bukkit.DyeColor;
import org.bukkit.Location;
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
        Location randomizedLocation = this.baseLocation.add(random.nextInt(80) - 40, 120, random.nextInt(80) - 40);


    }

    public HashMap<Location, SimpleBlock> createPortalFrame(Location basePortalLocation, DyeColor portalColor, boolean axisX, boolean exit)
    {
        HashMap<Location, SimpleBlock> blocks = new HashMap<>();

        if(exit)
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

    @Override
    public boolean canUse()
    {
        return true;
    }
}
