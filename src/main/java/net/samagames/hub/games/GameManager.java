package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.PacketCallBack;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.GameInfoToHubPacket;
import net.samagames.hub.common.hydroangeas.packets.queues.QueueInfosUpdatePacket;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.hub.games.types.*;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final Map<String, AbstractGame> games;
    private final CopyOnWriteArrayList<UUID> playerHided;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();
        this.playerHided = new CopyOnWriteArrayList<>();

        this.registerGame(new BackEndGame(hub, "beta_vip", "VIP", LocationUtils.str2loc(hub.getConfig().getString("vip-zone")), false));

        this.registerGame(new UppervoidGame(hub));
        this.registerGame(new QuakeGame(hub));
        this.registerGame(new DimensionsGame(hub));
        this.registerGame(new ChunkWarsGame(hub));

        // -----

        UHCZoneGame uhcZoneGame = new UHCZoneGame(hub);

        this.registerGame(uhcZoneGame);
        this.registerGame(new BackEndGame(hub, "uhc", "UHC", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "uhcrun", "UHCRun", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "switchrun", "SwitchRun", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "doublerunner", "DoubleRunner", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "uhcrandom", "UHCRandom", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "randomrun", "RandomRun", uhcZoneGame.getLobbySpawn(), false));
        this.registerGame(new BackEndGame(hub, "ultraflagkeeper", "Run4Flag", uhcZoneGame.getLobbySpawn(), false));

        // -----

        this.registerGame(new TimberManGame(hub));
        this.registerGame(new BomberManGame(hub));
        this.registerGame(new BurnThatChickenGame(hub));
        this.registerGame(new OneTwoThreeSunGame(hub));

        this.registerGame(new BackEndGame(hub, "craftmything", "CraftMyThing", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "witherparty", "WitherParty", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "hangovergames", "HangoverGames", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "pacman", "PacMan", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "flyring", "FlyRing", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "partygames", "PartyGames", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "casino", "Casino", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "samabox", "SamaBox", this.hub.getPlayerManager().getSpawn(), false));
        this.registerGame(new BackEndGame(hub, "agarmc", "AgarMC", this.hub.getPlayerManager().getSpawn(), false));

        // -----

        this.registerGame(new BackEndGame(hub, "event", "Événement", this.hub.getPlayerManager().getSpawn(), false));

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<GameInfoToHubPacket>(GameInfoToHubPacket.class)
        {
            @Override
            public void call(GameInfoToHubPacket packet)
            {
                for (AbstractGame game : games.values())
                {
                    for (List<GameSign> list : game.getSigns().values())
                    {
                        list.stream().filter(sign -> sign.getTemplate().equalsIgnoreCase(packet.getTemplateID())).forEach(sign ->
                        {
                            sign.setPlayerPerGame(packet.getPlayerMaxForMap());
                            sign.setPlayerWaitFor(packet.getPlayerWaitFor());
                            sign.setTotalPlayerOnServers(packet.getTotalPlayerOnServers());
                            sign.update();
                        });
                    }
                }
            }
        });

        hub.getHydroangeasManager().getPacketReceiver().registerCallBack(new PacketCallBack<QueueInfosUpdatePacket>(QueueInfosUpdatePacket.class)
        {
            @Override
            public void call(QueueInfosUpdatePacket packet)
            {
                try
                {
                    Player player = hub.getServer().getPlayer(packet.getPlayer().getUUID());

                    if (!packet.isSuccess() && (packet.getErrorMessage() != null && !packet.getErrorMessage().isEmpty()))
                    {
                        player.sendRawMessage(packet.getErrorMessage());
                        return;
                    }

                    if (player != null)
                    {
                        if (packet.getType().equals(QueueInfosUpdatePacket.Type.ADD))
                        {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 1.5F);



                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.GREEN + "Ajouté à la file d'attente de " + ChatColor.GOLD + packet.getGame() +  ChatColor.GREEN + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                            if (getGameByIdentifier(packet.getGame()).hasResourcesPack())
                            {
                                player.sendMessage(ChatColor.AQUA + "Ce jeu fait l'usage d'un pack de ressources. Vérifiez vos paramètres afin qu'ils soient correctement activés.");
                                player.sendMessage(ChatColor.AQUA + "Si vous n'utilisez pas le pack de ressources avant le début de la partie, vous serez renvoyé au Hub.");
                                player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            }
                        }
                        else if (packet.getType().equals(QueueInfosUpdatePacket.Type.REMOVE))
                        {
                            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_PLING, 1.0F, 0.8F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.RED + "Retiré de la file d'attente de " + ChatColor.GOLD + packet.getGame() + ChatColor.RED + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.RED + " !");
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                        else if (packet.getType().equals(QueueInfosUpdatePacket.Type.INFO) && packet.getMessage() != null)
                        {
                            if (!SamaGamesAPI.get().getSettingsManager().getSettings(player.getUniqueId()).isWaitingLineNotification())
                                return;

                            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 10.0F, 2.0F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                            for (String message : packet.getMessage())
                                player.sendMessage(ChatColor.YELLOW + message.replaceAll("<RESET>", String.valueOf(ChatColor.YELLOW)));

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                    }
                }
                catch (Exception ignored) {}
            }
        });

        this.hub.getServer().getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            List<UUID> toHide = new ArrayList<>();

            for (AbstractGame game : this.getGames().values())
                for (List<GameSign> list : game.getSigns().values())
                    for (GameSign sign : list)
                        this.hub.getWorld().getNearbyEntities(sign.getSign().getLocation(), 1.0D, 1.0D, 1.0D).stream().filter(entity -> entity instanceof Player && !toHide.contains(entity.getUniqueId())).forEach(entity -> toHide.add(entity.getUniqueId()));

            for (UUID playerUUID : toHide)
            {
                Player player = this.hub.getServer().getPlayer(playerUUID);

                if (player == null)
                    continue;

                for (Player pPlayer : this.hub.getServer().getOnlinePlayers())
                    this.hub.getServer().getScheduler().runTask(hub, () -> pPlayer.hidePlayer(player));
            }

            this.playerHided.addAll(toHide);

            for (UUID playerUUID : this.playerHided)
            {
                Player player = this.hub.getServer().getPlayer(playerUUID);

                if (player == null)
                {
                    this.playerHided.remove(playerUUID);
                    continue;
                }

                if (!toHide.contains(playerUUID))
                {
                    this.playerHided.remove(playerUUID);

                    for (Player other : this.hub.getServer().getOnlinePlayers())
                        this.hub.getServer().getScheduler().runTask(hub, () -> other.showPlayer(player));
                }
            }
        }, 20L * 2, 20L * 2);
    }

    @Override
    public void onDisable()
    {
        this.games.values().forEach(AbstractGame::clearSigns);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) {/** Not needed **/ }

    private void registerGame(AbstractGame game)
    {
        if (!this.games.containsKey(game.getCodeName()))
        {
            this.games.put(game.getCodeName(), game);
            this.log(Level.INFO, "Registered game '" + game.getCodeName() + "'");
        }
    }

    public AbstractGame getGameByIdentifier(String identifier)
    {
        if (this.games.containsKey(identifier.toLowerCase()))
            return this.games.get(identifier.toLowerCase());
        else
            return null;
    }

    public Map<String, AbstractGame> getGames()
    {
        return this.games;
    }
}
