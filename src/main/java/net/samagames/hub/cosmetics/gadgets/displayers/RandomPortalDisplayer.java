package net.samagames.hub.cosmetics.gadgets.displayers;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class RandomPortalDisplayer extends AbstractDisplayer
{
    public RandomPortalDisplayer(Player player)
    {
        super(player);

        Location location = player.getLocation().getBlock().getLocation();
        float angleDegree = player.getLocation().getYaw();
        if(angleDegree >= 45 && angleDegree <= 135)
        {
            angleDegree = 90;
        }else if(angleDegree >= 135 && angleDegree <= 225)
        {
            angleDegree = 180;
        }else if(angleDegree >= 225 && angleDegree <= 315)
        {
            angleDegree = 270;
        }else{
            angleDegree = 0;
        }
        double angleRadian = Math.toRadians(angleDegree);
        location.add(Math.cos(angleRadian) * 3,0, Math.sin(angleRadian) * 3);


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
