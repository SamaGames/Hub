package net.samagames.hub.events.player;

import net.samagames.api.SamaGamesAPI;
import net.samagames.api.player.PlayerData;
import net.samagames.hub.Hub;
import net.samagames.permissionsbukkit.PermissionsBukkit;
import net.samagames.tools.InventoryUtils;
import net.samagames.tools.Titles;
import org.apache.commons.lang.StringUtils;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
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
import java.util.Map;
import java.util.UUID;

public class PlayerListener implements Listener
{
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event)
    {
        if(event.getPlayer().getLocation().subtract(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.SLIME_BLOCK)
            event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 100, 10));
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

    @EventHandler (priority = EventPriority.LOWEST)
    public void onAsyncChat(AsyncPlayerChatEvent event)
    {
        if (!Hub.getInstance().getChatManager().canChat())
        {
            if (!PermissionsBukkit.hasPermission(event.getPlayer(), "lobby.bypassmute"))
            {
                event.setCancelled(true);
                event.getPlayer().sendMessage(ChatColor.RED + "Le chat est désactivé.");
            }
            else
            {
                event.getPlayer().sendMessage(ChatColor.GOLD + "Attention : chat désactivé");
            }
        }
        else if (Hub.getInstance().getChatManager().getActualSlowDuration() > 0 && !PermissionsBukkit.hasPermission(event.getPlayer(), "slow.cancel"))
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
        else if (StringUtils.containsIgnoreCase(event.getMessage(), "Minechat") || StringUtils.containsIgnoreCase(event.getMessage(), "minecraft connect"))
        {
            event.getPlayer().sendMessage(ChatColor.GOLD + "La publicité d'application de chat Minecraft est censurée.");
            return;
        }

        /**
         * TODO: Add DJ tag to actual DJ
         *
        if (!event.isCancelled())
        {
            PlaylistSong current = Plugin.noteBlockMachine.getCurrentSong();
            if (current != null && current.getPlayedBy().equals(event.getPlayer().getName()))
                event.setFormat(ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + "DJ" + ChatColor.DARK_AQUA + "]" + event.getFormat());
        }
        **/

        if(!Hub.getInstance().getNPCManager().canTalk(event.getPlayer()))
            event.setCancelled(true);

        if(!event.isCancelled())
            for (Player player : Bukkit.getOnlinePlayers())
                if (!Hub.getInstance().getNPCManager().canTalk(player))
                   event.getRecipients().remove(player);
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
    public void onPlayerJoin(final PlayerJoinEvent event)
    {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLaterAsynchronously(Hub.getInstance(), () ->
        {
            if (PermissionsBukkit.hasPermission(player, "lobby.fly"))
                Bukkit.getScheduler().runTask(Hub.getInstance(), () -> player.setAllowFlight(true));
        }, 10L);

        player.setGameMode(GameMode.ADVENTURE);
        InventoryUtils.cleanPlayer(player);
        Hub.getInstance().getPlayerManager().getStaticInventory().setInventoryToPlayer(player);

        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            String lobby = SamaGamesAPI.get().getResource().get("signsarea:" + player.getUniqueId() + ":hub");

            /**
             * TODO: Teleport player to his previous game lobby
             *
            final Region l = Plugin.regionsManager.getLobbyByName(lobby);

            if (l == null || l.getName().equalsIgnoreCase("main") || l.getSpawn() == null)
            {
                teleportPlayer(p, Plugin.spawn);
                return;
            }

            teleportPlayer(p, l.getSpawn());
            **/

            /**
             * TODO: Hider
             *
            Hub.getInstance().hider.onLogin(p);
            **/

            final Player p = event.getPlayer();
            Map<String, String> data = SamaGamesAPI.get().getPlayerManager().getPlayerData(p.getUniqueId()).getValues();

            /**
             * TODO: Put back player's particle on connection [Recreate code needed]
             *
            if (data.get("currentparticle") != null)
                Hub.getInstance().cosmeticsManager.getCosmeticHandler().addFXToPlayer(p, Plugin.cosmeticsManager.getParticle(data.get("currentparticle")));
            **/

            /**
             * TODO: Create back player's pet on connection [Recreate code needed]
             *
            if (data.get("selectedpet") != null)
            {
                Bukkit.getScheduler().runTaskLater(Hub.getInstance(), () ->
                {
                    AbstractPet pet = Plugin.cosmeticsManager.getPet(data.get("selectedpet"));

                    if (pet != null)
                        Plugin.cosmeticsManager.spawnPet(p, pet, data.get("petssettings"));
                }, 40L);
            }
            **/

            /**
             * TODO: Put back player's disguise on connection [Recreate code needed]
             *
            if (data.get("currentdisguise") != null)
            {
                Bukkit.getScheduler().runTaskLaterAsynchronously(Plugin.instance, () -> {
                    AbstractDisguise disguise = Plugin.cosmeticsManager.getDisguise(data.get("currentdisguise"));
                    if (disguise != null) {
                        Disguise d = disguise.getDisguise();
                        if (d instanceof MobDisguise) {
                            d.getWatcher().setCustomNameVisible(true);
                            d.getWatcher().setCustomName(p.getDisplayName());
                        }

                        d.setViewSelfDisguise(Plugin.cosmeticsManager.getStorageHandler().getViewSelfDisguise(p));

                        DisguiseAPI.disguiseToAll(p, d);
                    }
                }, 5L);
            }
            **/

            String chatOnSetting = SamaGamesAPI.get().getSettingsManager().getSetting(event.getPlayer().getUniqueId(), "chaton");
            if (chatOnSetting != null && chatOnSetting.equals("false"))
            {
                Hub.getInstance().getChatManager().disableChat(event.getPlayer());
                event.getPlayer().sendMessage(ChatColor.GOLD + "Vous avez désactivé le chat. Vous ne verrez donc pas les messages des joueurs.");
            }

            /**
             * TODO: Jukebox cosmetics system (Connection event) [Recreate code needed]
             *
            String jukeboxSetting = SettingsManager.getSetting(event.getPlayer().getUniqueId(), "jukeboxenabled");
            if (jukeboxSetting == null || jukeboxSetting.equals("true"))
            {
                Hub.getInstance().getCosmeticsManager().getNoteBlockMachine().addPlayer(p);

                PlaylistSong song = Hub.getInstance().getCosmeticsManager().getNoteBlockMachine().getCurrentSong();

                if (song != null)
                    event.getPlayer().sendMessage(NoteBlockMachine.TAG + ChatColor.YELLOW + "La musique jouée actuellement est "+ChatColor.GOLD + ChatColor.ITALIC + song.getSong().getTitle() + ChatColor.YELLOW +" de "+ChatColor.GOLD + song.getSong().getAuthor() + ChatColor.YELLOW + ", proposée par " + ChatColor.GOLD + song.getPlayedBy());
            }
            else
            {
                Hub.getInstance().getCosmeticsManager().getNoteBlockMachine().disableFor(p.getUniqueId());
            }
            **/

            Titles.sendTabTitle(p, ChatColor.GREEN + "Bienvenue sur mc.samagames.net !", ChatColor.AQUA + "Teamspeak : ts.samagames.net");

            Hub.getInstance().getScoreboardManager().addScoreboardReceiver(player);
            Hub.getInstance().getHologramManager().addReceiver(player);

            if(PermissionsBukkit.hasPermission(player, "lobby.announce"))
                Bukkit.broadcastMessage(player.getDisplayName() + ChatColor.YELLOW + " à rejoint le hub !");

            player.teleport(new Location(Bukkit.getWorlds().get(0), -19, 51, 89));
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onTeleport(final PlayerTeleportEvent event)
    {
        final Player player = event.getPlayer();

        if (player.getVehicle() != null)
        {
            /**
             * TODO: Create this event for the pets (Depends of CosmeticsManager)
             *
            if (Hub.getInstance().getCosmeticsManager().getPetsHandler().hadPet(player))
            {
                Hub.getInstance().getCosmeticsManager().getPetsHandler().removePet(player);
                Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () -> SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("selectedpet"));
            }
            else
            {
                ((CraftEntity) vehicle).getHandle().getWorld().removeEntity(((CraftEntity) vehicle).getHandle());
            }
            **/
        }
    }


    @EventHandler
    public void onEntityDimount(EntityDismountEvent event)
    {
        if (event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getEntity();

            /**
             * TODO: Create this event for the pets (Depends of CosmeticsManager)
             *
            if (Hub.getInstance().getCosmeticsManager().getPetsHandler().hadPet(player))
            {
                Hub.getInstance().getCosmeticsManager().getPetsHandler().removePet(player);
                Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () -> SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId()).remove("selectedpet"));
            }
            **/
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
        {
            Hub.getInstance().getPlayerManager().getStaticInventory().doInteraction(player, item);
        });
    }

    @EventHandler
    public void onPlayerDamaged(final EntityDamageByEntityEvent event)
    {
        event.setCancelled(true);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player)
        {
            final Player player = (Player) event.getDamager();

            Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
            {
                /**
                 * TODO: Include ClickMe
                 *
                if (!ClickMeSettings.ClickMeCanclick.getValue(player.getUniqueId()).equals("true"))
                    return;

                Player target = (Player) event.getEntity();
                ClickMe.generateMenu(target.getName(), player);
                **/
            });

        }
    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) { event.setCancelled(true); }

    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent event)
    {
        if(event.getRightClicked().getType() == EntityType.VILLAGER)
        {
            if(event.getRightClicked().hasMetadata("npc-id"))
            {
                event.setCancelled(true);

                Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
                {
                    if(Hub.getInstance().getNPCManager().hasNPC(UUID.fromString(event.getRightClicked().getMetadata("npc-id").get(0).asString())))
                        if(Hub.getInstance().getNPCManager().canTalk(event.getPlayer()))
                            Hub.getInstance().getNPCManager().getNPCByID(UUID.fromString(event.getRightClicked().getMetadata("npc-id").get(0).asString())).getAction().execute(event.getPlayer());
                });
            }
        }
    }

    private void onPlayerLeave(final Player player)
    {
        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), () ->
        {
            /**
             * TODO: Put in database the previous player game lobby
             *
             Region reg = Hub.getInstance().getRegionManager().getLobbyByLocation(p.getLocation());

             if (reg != null)
             {
             FastJedis.set("signsarea:" + player.getUniqueId() + ":hub", reg.getName());
             FastJedis.expire("signsarea:" + player.getUniqueId() + ":hub", 3 * 60 * 60);
             }
             **/

            PlayerData data = SamaGamesAPI.get().getPlayerManager().getPlayerData(player.getUniqueId());

            /**
             * TODO: Remove player's particles
             *
             if (data.get("currentparticle") != null)
             Hub.getInstance().getCosmeticsManager().getCosmeticHandler().removeFXLocaly(p.getUniqueId());
             **/

            /**
             * TODO: Remove player's pet
             *
             if (data.get("selectedpet") != null)
             Hub.getInstance().getCosmeticsManager().getPetsHandler().removePet(p);
             **/

            if (Hub.getInstance().getPlayerManager().getSelection(player) != null)
                Hub.getInstance().getPlayerManager().removeSelection(player);
        });

        /**
         * TODO: Remove player of hider mode
         *
        Plugin.hider.localHiding.remove(p.getUniqueId());
        **/

        Hub.getInstance().getChatManager().removeChatDisabler(player);

        /**
         * TODO: Remove player from note block machine
         *
        Hub.getInstance().getCosmeticsManager().getNoteBlockMachine().removePlayer(player);
        **/

        Hub.getInstance().getChatManager().unmutePlayer(player);
        Hub.getInstance().getNPCManager().talkFinished(player);
        Hub.getInstance().getScoreboardManager().removeScoreboardReceiver(player);
        Hub.getInstance().getHologramManager().removeReceiver(player);
    }
}
