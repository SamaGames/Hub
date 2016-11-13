package net.samagames.hub.events;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.api.permissions.IPermissionsEntity;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.hub.gui.profile.GuiClickMe;
import net.samagames.hub.utils.RestrictedVersion;
import net.samagames.hub.utils.ServerStatus;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class PlayerListener implements Listener
{
    private final Hub hub;

    public PlayerListener(Hub hub)
    {
        this.hub = hub;
    }

    @EventHandler
    public void onPlayerLogin(PlayerJoinEvent event)
    {
        this.hub.getEventBus().onLogin(event.getPlayer());
        new ServerStatus(SamaGamesAPI.get().getServerName(), "Hub", "Map", Status.IN_GAME, this.hub.getServer().getOnlinePlayers().size(), this.hub.getServer().getMaxPlayers()).sendToHydro();
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event)
    {
        this.hub.getEventBus().onLogout(event.getPlayer());
        new ServerStatus(SamaGamesAPI.get().getServerName(), "Hub", "Map", Status.IN_GAME, this.hub.getServer().getOnlinePlayers().size(), this.hub.getServer().getMaxPlayers()).sendToHydro();
    }

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event)
    {
        this.hub.getChatManager().onAsyncPlayerChat(event);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
        event.setCancelled(true);

        if (event.getEntity().getType() != EntityType.PLAYER)
            return;

        if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
        {
            if (this.hub.getParkourManager().getPlayerParkour(event.getEntity().getUniqueId()) != null)
                this.hub.getParkourManager().getPlayerParkour(event.getEntity().getUniqueId()).failPlayer((Player) event.getEntity());
            else
                event.getEntity().teleport(this.hub.getPlayerManager().getSpawn());
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event)
    {
        if (!event.getWhoClicked().isOp())
            event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item == null)
            return;

        this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () -> this.hub.getPlayerManager().getStaticInventory().doInteraction(player, item));
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null)
        {
            Material material = event.getClickedBlock().getType();

            if (material == Material.SIGN || material == Material.SIGN_POST || material == Material.WALL_SIGN)
            {
                Sign sign = (Sign) event.getClickedBlock().getState();

                if (sign.hasMetadata("game") && sign.hasMetadata("map"))
                {
                    AbstractGame game = this.hub.getGameManager().getGameByIdentifier(sign.getMetadata("game").get(0).asString());
                    GameSign gameSign = game.getGameSignsByMap(sign.getMetadata("map").get(0).asString()).get(0);

                    if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.debug.sign") && event.getPlayer().isSneaking())
                    {
                        gameSign.developperClick(event.getPlayer());
                        return;
                    }

                    gameSign.click(event.getPlayer());
                    return;
                }
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event)
    {
        if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.fly"))
            this.hub.getServer().getScheduler().runTask(this.hub, () -> event.getPlayer().setAllowFlight(true));
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if (!event.getPlayer().getLocation().subtract(0, 0.1, 0).getBlock().getType().equals(Material.AIR)
            && event.getPlayer().getInventory().getChestplate() != null
            && event.getPlayer().getInventory().getChestplate().getType() == Material.ELYTRA)
        {
            this.checkElytra(event.getPlayer());
        }

        this.hub.getTaskManager().getPlayersAwayFromKeyboardTask().resetPlayer(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerGlide(EntityToggleGlideEvent event)
    {
        if (!(event.getEntity() instanceof Player))
            return;

        if (!SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getEntity(), "network.vipplus"))
            if (((Player) event.getEntity()).isFlying() && ((Player) event.getEntity()).getInventory().getChestplate().getType() == Material.ELYTRA)
                ((Player) event.getEntity()).getInventory().setChestplate(null);

        if (event.isGliding() && RestrictedVersion.isLoggedInPost19((Player) event.getEntity()))
        {
            ItemStack stack = new ItemStack(Material.FEATHER);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "Ventilateur" + ChatColor.RESET + "" + ChatColor.GRAY + " (Clic-droit)");
            stack.setItemMeta(meta);
            stack.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            ((Player) event.getEntity()).getInventory().setItem(3, stack);
        }
        else
        {
            ((Player) event.getEntity()).getInventory().setItem(3, new ItemStack(Material.AIR));
        }
    }

    @EventHandler
    public void onPlayerToggleFlight(PlayerToggleFlightEvent event)
    {
        this.onPlayerGlide(new EntityToggleGlideEvent(event.getPlayer(), false));
    }

    @EventHandler
    public void onPlayerDamaged(final EntityDamageByEntityEvent event)
    {
        event.setCancelled(true);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getDamager();

            if (this.hub.getCosmeticManager().getGadgetManager().hasGadget((Player) event.getDamager()))
            {
                AbstractDisplayer displayer = this.hub.getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getDamager());

                if (displayer.isInteractionsEnabled() && !SamaGamesAPI.get().getSettingsManager().getSettings(event.getEntity().getUniqueId()).isOtherPlayerInteraction())
                {
                    if (this.hub.getPlayerManager().isBusy((Player) event.getEntity()))
                        event.getDamager().sendMessage(ChatColor.RED + "Ce joueur est actuellement occupé.");
                    else
                        event.getDamager().sendMessage(ChatColor.RED + "Ce joueur n'accepte pas les intéractions !");

                    return;
                }

                this.hub.getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getDamager()).handleInteraction(event.getDamager(), event.getEntity());
                return;
            }
            else if (this.hub.getCosmeticManager().getGadgetManager().hasGadget((Player) event.getEntity()))
            {
                this.hub.getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getEntity()).handleInteraction(event.getDamager(), event.getEntity());
                return;
            }

            this.hub.getServer().getScheduler().runTaskAsynchronously(this.hub, () ->
            {
                Player target = (Player) event.getEntity();

                if (SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isAllowClickOnOther())
                {
                    if (!SamaGamesAPI.get().getSettingsManager().getSettings(target.getUniqueId()).isClickOnMeActivation())
                        return;

                    this.hub.getGuiManager().openGui(player, new GuiClickMe(this.hub, target));
                }
            });
        }
        else if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
        {
            if (event.getEntity().hasMetadata("owner-id"))
            {
                UUID uuid = UUID.fromString(event.getEntity().getMetadata("owner-id").get(0).asString());

                if (this.hub.getCosmeticManager().getGadgetManager().hasGadget(uuid))
                    this.hub.getCosmeticManager().getGadgetManager().getPlayerGadget(uuid).handleInteraction(event.getDamager(), event.getEntity());
            }
        }
    }

    private boolean checkElytra(Player player)
    {
        IPermissionsEntity permissionsEntity = SamaGamesAPI.get().getPermissionsManager().getPlayer(player.getUniqueId());

        if (permissionsEntity.getGroupId() < 3 || !SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isElytraActivated())
        {
            player.getInventory().setChestplate(new ItemStack(Material.AIR));
            return false;
        }

        return true;
    }
}
