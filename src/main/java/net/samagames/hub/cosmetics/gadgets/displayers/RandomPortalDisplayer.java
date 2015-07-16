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
        double angleRadian = Math.toRadians(player.getLocation().getYaw());
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
