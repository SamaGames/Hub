package net.samagames.hub.cosmetics.balloons;

import net.samagames.hub.Hub;
import net.samagames.hub.common.players.PlayerManager;
import net.samagames.hub.cosmetics.common.AbstractCosmeticManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.inventory.ClickType;

import javax.lang.model.type.NullType;

/**
 * Created by Rigner for project Hub.
 */
public class BalloonManager extends AbstractCosmeticManager<BalloonCosmetic> implements Listener
{
    public BalloonManager(Hub hub)
    {
        super(hub, new BalloonRegistry(hub));
        this.hub.getServer().getPluginManager().registerEvents(this, this.hub);
    }

    @Override
    public void enableCosmetic(Player player, BalloonCosmetic cosmetic, ClickType clickType, NullType useless)
    {
        cosmetic.spawn(player);
        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Des ballons flottent autour de vous !");
    }

    @Override
    public void disableCosmetic(Player player, BalloonCosmetic cosmetic, boolean logout, NullType useless)
    {
        cosmetic.remove(player);
        player.sendMessage(PlayerManager.COSMETICS_TAG + ChatColor.GREEN + "Vos ballons ont éclatés.");
    }

    @Override
    public void update() { /** Not needed **/ }

    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event)
    {
        if (event.getEntity().getItemStack() != null && event.getEntity().getItemStack().getType() == Material.LEASH)
            event.setCancelled(true);
    }

    @Override
    public boolean restrictToOne()
    {
        return true;
    }
}
