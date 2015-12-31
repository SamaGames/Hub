package net.samagames.hub.games;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.packets.hubinfo.GameInfoToHubPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueInfosUpdatePacket;
import net.samagames.hub.common.hydroconnect.utils.PacketCallBack;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.sign.GameSign;
import net.samagames.hub.games.type.*;

import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final HashMap<String, AbstractGame> games;
    private final CopyOnWriteArrayList<UUID> playerHided;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();
        this.playerHided = new CopyOnWriteArrayList<>();

        this.registerGame(new OneWayGame("beta_vip", "VIP", Material.DIAMOND, new Location(this.hub.getHubWorld(), -221.5D, 203.0D, 31.5D, -90.0F, 0.0F)));
        this.registerGame(new OneWayGame("beta_staff", "Staff", Material.COOKIE, new Location(this.hub.getHubWorld(), -243.5D, 194.0D, 18.5D, 128.0F, 10.0F)));

        this.registerGame(new UppervoidGame());
        this.registerGame(new QuakeGame());
        this.registerGame(new DimensionsGame());
        this.registerGame(new HeroBattleGame());

        // -----

        UHCZoneGame uhcZoneGame = new UHCZoneGame();

        DisplayedStat killsStat = new DisplayedStat("kills", "Joueurs tués", new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal()));
        DisplayedStat deathsStat = new DisplayedStat("deaths", "Morts", Material.BONE);

        this.registerGame(uhcZoneGame);
        this.registerGame(new BackEndGame("uhc", "UHC", uhcZoneGame.getLobbySpawn(), killsStat, deathsStat));
        this.registerGame(new BackEndGame("uhcrun", "UHCRun", uhcZoneGame.getLobbySpawn(), killsStat, deathsStat));
        this.registerGame(new BackEndGame("switchrun", "SwitchRun", uhcZoneGame.getLobbySpawn(), killsStat, deathsStat));
        this.registerGame(new BackEndGame("doublerunner", "DoubleRunner", uhcZoneGame.getLobbySpawn(), killsStat, deathsStat));

        // -----

        ArcadeGame arcadeGame = new ArcadeGame();

        this.registerGame(arcadeGame);
        this.registerGame(new BackEndGame("craftmything", "CraftMyThing", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("witherparty", "WitherParty", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("hangovergames", "HangoverGames", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("pacman", "PacMan", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("burnthatchicken", "BurnThatChicken", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("timberman", "Timberman", arcadeGame.getLobbySpawn()));

        // -----

        this.registerGame(new AgarMCGame());
        this.registerGame(new BackEndGame("event", "Événement", Hub.getInstance().getPlayerManager().getLobbySpawn()));

        hub.getHydroManager().getPacketReceiver().registerCallBack(new PacketCallBack<GameInfoToHubPacket>(GameInfoToHubPacket.class)
        {
            @Override
            public void call(GameInfoToHubPacket packet)
            {
                for (AbstractGame game : games.values())
                {
                    game.getSigns().values().stream().filter(sign -> sign.getTemplate().equalsIgnoreCase(packet.getTemplateID())).forEach(sign ->
                    {
                        sign.setPlayerPerGame(packet.getPlayerMaxForMap());
                        sign.setPlayerWaitFor(packet.getPlayerWaitFor());
                        sign.setTotalPlayerOnServers(packet.getTotalPlayerOnServers());
                        sign.update();
                    });
                }
            }
        });

        hub.getHydroManager().getPacketReceiver().registerCallBack(new PacketCallBack<QueueInfosUpdatePacket>(QueueInfosUpdatePacket.class)
        {
            @Override
            public void call(QueueInfosUpdatePacket packet)
            {
                try
                {
                    Player player = Bukkit.getPlayer(packet.getPlayer().getUUID());

                    if (!packet.isSuccess() && (packet.getErrorMessage() != null && !packet.getErrorMessage().isEmpty()))
                    {
                        player.sendRawMessage(packet.getErrorMessage());
                        return;
                    }

                    if(player != null)
                    {
                        if(packet.getType().equals(QueueInfosUpdatePacket.Type.ADD))
                        {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.5F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.GREEN + "Ajouté à la file d'attente de " + ChatColor.GOLD + packet.getGame() +  ChatColor.GREEN + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                        else if(packet.getType().equals(QueueInfosUpdatePacket.Type.REMOVE))
                        {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 0.8F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.RED + "Retiré de la file d'attente de " + ChatColor.GOLD + packet.getGame() + ChatColor.RED + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.RED + " !");
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                        else if(packet.getType().equals(QueueInfosUpdatePacket.Type.INFO) && packet.getMessage() != null)
                        {
                        	if (!SamaGamesAPI.get().getSettingsManager().isEnabled(player.getUniqueId(), "queuenotifications", true))
                        		return;

                            player.playSound(player.getLocation(), Sound.VILLAGER_HAGGLE, 10.0F, 2.0F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");

                            for(String message : packet.getMessage())
                                player.sendMessage(ChatColor.YELLOW + message.replaceAll("<RESET>", String.valueOf(ChatColor.YELLOW)));

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                    }
                }
                catch(Exception ignored) {}
            }
        });

        Bukkit.getScheduler().runTaskTimerAsynchronously(hub, () ->
        {
            ArrayList<UUID> toHide = new ArrayList<>();

            for (AbstractGame game : this.getGames().values())
            {
                for (GameSign sign : game.getSigns().values())
                {
                    for (Entity entity : this.hub.getHubWorld().getNearbyEntities(sign.getSign().getLocation(), 2.0D, 2.0D, 2.0D))
                    {
                        if (entity instanceof Player)
                        {
                            if (!toHide.contains(entity.getUniqueId()))
                            {
                                toHide.add(entity.getUniqueId());
                            }
                        }
                    }
                }
            }

            for (UUID playerUUID : toHide)
            {
                Player player = Bukkit.getPlayer(playerUUID);

                if (player == null)
                    continue;

                for (final Player pPlayer : Bukkit.getOnlinePlayers())
                {
                    Bukkit.getScheduler().runTask(hub, () -> pPlayer.hidePlayer(player));
                }
            }

            this.playerHided.addAll(toHide);

            for (UUID playerUUID : this.playerHided)
            {
                Player player = Bukkit.getPlayer(playerUUID);

                if (player == null)
                {
                    this.playerHided.remove(playerUUID);
                    continue;
                }

                if (!toHide.contains(playerUUID))
                {
                    this.playerHided.remove(playerUUID);

                    for (final Player pPlayer : Bukkit.getOnlinePlayers())
                    {
                        Bukkit.getScheduler().runTask(hub, () -> pPlayer.showPlayer(player));
                    }
                }
            }
        }, 20L * 2, 20L * 2);
    }

    public void registerGame(AbstractGame game)
    {
        if(!this.games.containsKey(game.getCodeName()))
        {
            this.games.put(game.getCodeName(), game);
            this.hub.log(this, Level.INFO, "Registered game '" + game.getCodeName() + "'");
        }
    }

    public AbstractGame getGameByIdentifier(String identifier)
    {
        if(this.games.containsKey(identifier.toLowerCase()))
            return this.games.get(identifier.toLowerCase());
        else
            return null;
    }

    public HashMap<String, AbstractGame> getGames()
    {
        return this.games;
    }

    @Override
    public String getName() { return "GameManager"; }
}
