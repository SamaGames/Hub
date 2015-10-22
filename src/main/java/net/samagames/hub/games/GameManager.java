package net.samagames.hub.games;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.packets.hubinfo.GameInfoToHubPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueInfosUpdatePacket;
import net.samagames.hub.common.hydroconnect.utils.PacketCallBack;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.sign.GameSign;
import net.samagames.hub.games.type.*;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final HashMap<String, AbstractGame> games;
    private final ArrayList<UUID> playerHided;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();
        this.playerHided = new ArrayList<>();

        this.registerGame(new OneWayGame("beta_vip", "VIP", Material.DIAMOND, new Location(this.hub.getHubWorld(), -221.5D, 203.0D, 31.5D, -90.0F, 0.0F)));

        this.registerGame(new UppervoidGame());
        this.registerGame(new UHCGame());
        this.registerGame(new UHCRunGame());
        this.registerGame(new QuakeGame());
        this.registerGame(new DimensionsGame());
        this.registerGame(new HeroBattleGame());

        ArcadeGame arcadeGame = new ArcadeGame();

        this.registerGame(new ArcadeGame());
        this.registerGame(new BackEndGame("craftmything", "CraftMyThing", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("witherparty", "WitherParty", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("hangovergames", "HangoverGames", arcadeGame.getLobbySpawn()));
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
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.GREEN + "Ajouté à la queue " + ChatColor.GOLD + packet.getGame() +  ChatColor.GREEN + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                        }
                        else
                        {
                            player.playSound(player.getLocation(), Sound.NOTE_PLING, 1.0F, 1.0F);

                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬▬");
                            player.sendMessage(ChatColor.RED + "Retiré de la queue " + ChatColor.GOLD + packet.getGame() + ChatColor.RED + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.RED + " !");
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
                for (GameSign sign : game.getSigns().values())
                    for (Entity entity : this.hub.getHubWorld().getNearbyEntities(sign.getSign().getLocation(), 2.0D, 2.0D, 2.0D))
                        if (entity.getType() == EntityType.PLAYER)
                            if (!toHide.contains(entity.getUniqueId()))
                                toHide.add(entity.getUniqueId());

            for (UUID playerUUID : toHide)
            {
                Player player = Bukkit.getPlayer(playerUUID);

                if (player == null)
                    continue;

                for (Player pPlayer : Bukkit.getOnlinePlayers())
                    pPlayer.hidePlayer(player);
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

                    for (Player pPlayer : Bukkit.getOnlinePlayers())
                        pPlayer.showPlayer(player);
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
