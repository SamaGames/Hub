package net.samagames.hub.events.protection;

import net.samagames.hub.Hub;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_10_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;

public class PlayerProtectionListener implements Listener
{
    private final Hub hub;

    public PlayerProtectionListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerAchievementAwarded(PlayerAchievementAwardedEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerBucketFill(PlayerBucketFillEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event)
    {
        if (!this.canDoAction(event.getPlayer()))
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        if (this.canDoAction(event.getPlayer()))
            return;

        if (event.getItem() != null && (event.getItem().getType() == Material.ENDER_PEARL || event.getItem().getType() == Material.SNOW_BALL || event.getItem().getType() == Material.EGG || isPieceOfArmor(event.getItem())))
        {
            event.setCancelled(true);
            event.getPlayer().updateInventory();
        }

        if (event.getClickedBlock() != null && (event.getClickedBlock().getType() == Material.CHEST || event.getClickedBlock().getType() == Material.ENDER_CHEST))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (event.getTo().getX() > 250 || event.getTo().getX() < -350 || event.getTo().getZ() > 620 || event.getTo().getZ() < -350)
        {
            event.getPlayer().sendMessage(ChatColor.RED + "Mais où allez-vous comme ça ?");
            event.setCancelled(true);

            this.hub.getServer().getScheduler().runTaskLater(this.hub, () ->
            {
                event.getPlayer().teleport(this.hub.getPlayerManager().getSpawn());

                if (event.getPlayer().isFlying())
                    event.getPlayer().setFlying(false);

                if (event.getPlayer().isGliding())
                    ((CraftPlayer)event.getPlayer()).getHandle().setFlag(7, false);
            }, 1);
        }
    }

    private boolean canDoAction(Player player)
    {
        return this.hub.getPlayerManager().canBuild() && player != null && player.isOp() && player.getGameMode() == GameMode.CREATIVE;
    }

    private static boolean isPieceOfArmor(ItemStack stack)
    {
        if (stack.getType() == Material.LEATHER_HELMET || stack.getType() == Material.LEATHER_CHESTPLATE || stack.getType() == Material.LEATHER_LEGGINGS || stack.getType() == Material.LEATHER_BOOTS)
            return true;
        else if (stack.getType() == Material.IRON_HELMET || stack.getType() == Material.IRON_CHESTPLATE || stack.getType() == Material.IRON_LEGGINGS || stack.getType() == Material.IRON_BOOTS)
            return true;
        else if (stack.getType() == Material.CHAINMAIL_HELMET || stack.getType() == Material.CHAINMAIL_CHESTPLATE || stack.getType() == Material.CHAINMAIL_LEGGINGS || stack.getType() == Material.CHAINMAIL_BOOTS)
            return true;
        else if (stack.getType() == Material.GOLD_HELMET || stack.getType() == Material.GOLD_CHESTPLATE || stack.getType() == Material.GOLD_LEGGINGS || stack.getType() == Material.GOLD_BOOTS)
            return true;
        else if (stack.getType() == Material.DIAMOND_HELMET || stack.getType() == Material.DIAMOND_CHESTPLATE || stack.getType() == Material.DIAMOND_LEGGINGS || stack.getType() == Material.DIAMOND_BOOTS)
            return true;

        return false;
    }
}
