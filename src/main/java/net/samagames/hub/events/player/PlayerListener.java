package net.samagames.hub.events.player;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.games.Status;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.gadgets.displayers.AbstractDisplayer;
import net.samagames.hub.cosmetics.jukebox.JukeboxSong;
import net.samagames.hub.games.AbstractGame;
import net.samagames.hub.games.sign.GameSign;
import net.samagames.hub.gui.profile.GuiClickMe;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.block.Sign;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.Date;
import java.util.UUID;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(event.getFrom().getBlockX() != event.getTo().getBlockX() && event.getFrom().getBlockY() != event.getTo().getBlockY() && event.getFrom().getBlockZ() != event.getTo().getBlockZ())
        {
            if (event.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SLIME_BLOCK)
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 10));
        }
    }

    @EventHandler
    public void onFallIntoVoid(EntityDamageEvent event)
    {
        if (event.getCause() == EntityDamageEvent.DamageCause.VOID)
            if (event.getEntity() instanceof Player)
                event.getEntity().teleport(Hub.getInstance().getPlayerManager().getLobbySpawn());
    }

    @EventHandler
    public void onClick(InventoryInteractEvent event)
    {
        if (!event.getWhoClicked().isOp())
            event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onAsyncChat(AsyncPlayerChatEvent event)
    {
        if (!Hub.getInstance().getChatManager().canChat())
        {
            if (!SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.bypassmute"))
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Le chat est désactivé.");
            }
            else
            {
                event.getPlayer().sendMessage(ChatColor.GOLD + "Attention : chat désactivé");
            }
        }
        else if (Hub.getInstance().getChatManager().getActualSlowDuration() > 0 && !SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.bypassmute"))
        {
            if (!Hub.getInstance().getChatManager().hasPlayerTalked(event.getPlayer()))
            {
                Hub.getInstance().getChatManager().actualizePlayerLastMessage(event.getPlayer());
            }
            else
            {
                Date lastMessage = Hub.getInstance().getChatManager().getLastPlayerMessageDate(event.getPlayer());
                Date actualMessage = new Date(lastMessage.getTime() + (Hub.getInstance().getChatManager().getActualSlowDuration() * 1000));
                Date current = new Date();

                if (actualMessage.after(current))
                {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(ChatColor.RED + "Le chat est actuellement ralenti.");

                    double whenNext = Math.floor((actualMessage.getTime() - current.getTime()) / 1000);
                    event.getPlayer().sendMessage(ChatColor.GOLD + "Prochain message autorisé dans : " + (int) whenNext + " secondes");
                }
                else
                {
                    Hub.getInstance().getChatManager().actualizePlayerLastMessage(event.getPlayer());
                }
            }
        }

        JukeboxSong current = Hub.getInstance().getCosmeticManager().getJukeboxManager().getCurrentSong();

        if (current != null && current.getPlayedBy().equals(event.getPlayer().getName()))
            event.setFormat(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "DJ" + ChatColor.DARK_AQUA + "]" + event.getFormat());

        event.getRecipients().stream().filter(player -> Hub.getInstance().getChatManager().hasChatDisabled(player)).forEach(player -> event.getRecipients().remove(player));

        Bukkit.getOnlinePlayers().stream().filter(player -> StringUtils.containsIgnoreCase(event.getMessage(), player.getName())).forEach(player ->
        {
            event.getRecipients().remove(player);

            if (SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "notifications", true))
                player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.0F);

            String suffixRaw = SamaGamesAPI.get().getPermissionsManager().getApi().getUser(event.getPlayer().getUniqueId()).getProperties().get("suffix");
            ChatColor suffix = ChatColor.getByChar(suffixRaw.charAt(1));

            player.sendMessage(PlayerUtils.getFullyFormattedPlayerName(event.getPlayer()) + suffix + ": " + event.getMessage().replace(player.getName(), ChatColor.GOLD + player.getName() + suffix));
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event)
    {
        this.onPlayerLeave(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerKick(PlayerKickEvent event)
    {
        this.onPlayerLeave(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final AsyncPlayerPreLoginEvent event)
    {
        new ServerStatus(SamaGamesAPI.get().getServerName(), "Hub", "Map", Status.IN_GAME, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).sendToHydro();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        player.setGameMode(GameMode.ADVENTURE);
        player.setWalkSpeed(0.3F);
        player.setFlySpeed(0.2F);
        InventoryUtils.cleanPlayer(player);
        Hub.getInstance().getPlayerManager().getStaticInventory().setInventoryToPlayer(player);

        Hub.getInstance().getScheduledExecutorService().execute(() ->
        {
            Hub.getInstance().getPlayerManager().handleLogin(player);
            Hub.getInstance().getScoreboardManager().addScoreboardReceiver(player);
            Hub.getInstance().getHologramManager().addReceiver(player);

            player.teleport(Hub.getInstance().getPlayerManager().getLobbySpawn());

            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.fly"))
                Bukkit.getScheduler().runTask(Hub.getInstance(), () -> player.setAllowFlight(true));

            if (SamaGamesAPI.get().getPermissionsManager().hasPermission(player, "hub.announce"))
                Bukkit.broadcastMessage(PlayerUtils.getFullyFormattedPlayerName(player) + ChatColor.YELLOW + " a rejoint le hub !");
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(final PlayerTeleportEvent event)
    {
        Player player = event.getPlayer();

        if (player.getVehicle() != null && player.isSneaking())
        {
            if (Hub.getInstance().getCosmeticManager().getPetManager().hadPet(player))
            {
                Hub.getInstance().getCosmeticManager().getPetManager().disableCosmetic(player, false);
            }
        } else if (player.isInsideVehicle())
        {
            final Entity vehicle = player.getVehicle();

            Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
            {
                if (player.getVehicle() == null)
                {
                    ((CraftEntity) vehicle).getHandle().getWorld().removeEntity(((CraftEntity) vehicle).getHandle());
                    Hub.getInstance().getCosmeticManager().getPetManager().disableCosmetic(player, false);
                }
            }, 5);
        }
    }


    @EventHandler
    public void onEntityDismount(EntityDismountEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getEntity();

            if (Hub.getInstance().getCosmeticManager().getPetManager().hadPet(player))
            {
                Hub.getInstance().getCosmeticManager().getPetManager().disableCosmetic(player, false);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent event)
    {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();

        if (item == null)
            return;

        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
                Hub.getInstance().getPlayerManager().getStaticInventory().doInteraction(player, item));
    }

    @EventHandler
    public void onPlayerDamaged(final EntityDamageByEntityEvent event)
    {
        event.setCancelled(true);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getDamager();

            if (Hub.getInstance().getCosmeticManager().getGadgetManager().hasGadget((Player) event.getDamager()))
            {
                AbstractDisplayer displayer = Hub.getInstance().getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getDamager());

                if (displayer.isInteractionsEnabled() && !SamaGamesAPI.get().getSettingsManager().isEnabled(event.getEntity().getUniqueId(), "interactions", true))
                {
                    event.getDamager().sendMessage(ChatColor.RED + "Ce joueur n'accepte pas les intéractions !");
                    return;
                }

                Hub.getInstance().getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getDamager()).handleInteraction(event.getDamager(), event.getEntity());
            } else if (Hub.getInstance().getCosmeticManager().getGadgetManager().hasGadget((Player) event.getEntity()))
            {
                Hub.getInstance().getCosmeticManager().getGadgetManager().getPlayerGadget((Player) event.getEntity()).handleInteraction(event.getDamager(), event.getEntity());
            }

            Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
            {
                Player target = (Player) event.getEntity();

                if (SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "clickme-punch", true))
                {
                    if (!SamaGamesAPI.get().getSettingsManager().isEnabled(target.getUniqueId(), "clickme", true))
                        return;

                    Hub.getInstance().getGuiManager().openGui(player, new GuiClickMe(target));
                }
            });
        } else if (event.getDamager() instanceof Player && !(event.getEntity() instanceof Player))
        {
            if (event.getEntity().hasMetadata("owner-id"))
            {
                UUID uuid = UUID.fromString(event.getEntity().getMetadata("owner-id").get(0).asString());

                if (Hub.getInstance().getCosmeticManager().getGadgetManager().hasGadget(uuid))
                {
                    Hub.getInstance().getCosmeticManager().getGadgetManager().getPlayerGadget(uuid).handleInteraction(event.getDamager(), event.getEntity());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event)
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event) {}

    @EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event)
    {
        if (event.getClickedBlock() != null)
        {
            Material material = event.getClickedBlock().getType();

            if (material == Material.SIGN || material == Material.SIGN_POST || material == Material.WALL_SIGN)
            {
                Sign sign = (Sign) event.getClickedBlock().getState();

                if (sign.hasMetadata("game") && sign.hasMetadata("map"))
                {
                    AbstractGame game = Hub.getInstance().getGameManager().getGameByIdentifier(sign.getMetadata("game").get(0).asString());
                    GameSign gameSign = game.getGameSignByMap(sign.getMetadata("map").get(0).asString());

                    if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.debug.sign"))
                    {
                        if (event.getPlayer().isSneaking())
                        {
                            gameSign.developperClick(event.getPlayer());
                            return;
                        }
                    }

                    gameSign.click(event.getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void onPlayerGameModeChangeEvent(PlayerGameModeChangeEvent event)
    {
        if (SamaGamesAPI.get().getPermissionsManager().hasPermission(event.getPlayer(), "hub.fly"))
            Bukkit.getScheduler().runTask(Hub.getInstance(), () -> event.getPlayer().setAllowFlight(true));
    }

    private void onPlayerLeave(final Player player)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            new ServerStatus(SamaGamesAPI.get().getServerName(), "Hub", "Map", Status.IN_GAME, Bukkit.getOnlinePlayers().size(), Bukkit.getMaxPlayers()).sendToHydro();
            Hub.getInstance().getCosmeticManager().handleLogout(player);
            Hub.getInstance().getPlayerManager().handleLogout(player);
            Hub.getInstance().getChatManager().enableChatFor(player);
            //Hub.getInstance().getNPCManager().talkFinished(player);
            Hub.getInstance().getScoreboardManager().removeScoreboardReceiver(player);
            Hub.getInstance().getHologramManager().removeReceiver(player);
        });
    }
}
