package net.samagames.hub.games;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.packets.hubinfo.GameInfoToHubPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueAddPlayerPacket;
import net.samagames.hub.common.hydroconnect.packets.queues.QueueInfosUpdatePacket;
import net.samagames.hub.common.hydroconnect.utils.PacketCallBack;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.type.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Level;

public class GameManager extends AbstractManager
{
    private final HashMap<String, AbstractGame> games;

    public GameManager(Hub hub)
    {
        super(hub);

        this.games = new HashMap<>();

        /** Private zones **/

        this.registerGame(new OneWayGame("beta_vip", "VIP", Material.DIAMOND, new Location(this.hub.getHubWorld(), 2003.5, 55, 70.5, -180, 0)));
        this.registerGame(new OneWayGame("beta_staff", "Staff", Material.COOKIE, new Location(this.hub.getHubWorld(), -2016.5, 51, 92.5, -180, 0)));


        /** Games **/

        this.registerGame(new UpperVoidGame());
        this.registerGame(new UHCGame());
        this.registerGame(new UHCRunGame());
        this.registerGame(new QuakeGame());
        this.registerGame(new DimensionsGame());
        this.registerGame(new HeroBattleGame());
        this.registerGame(new SplatoonGame());


        /** Arcade games **/

        ArcadeGame arcadeGame = new ArcadeGame();

        this.registerGame(new ArcadeGame());
        this.registerGame(new BackEndGame("craftmything", "CraftMyThing", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("witherparty", "WitherParty", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("hangovergames", "HangoverGames", arcadeGame.getLobbySpawn()));
        this.registerGame(new BackEndGame("agarmc", "AgarMC", arcadeGame.getLobbySpawn()));

        hub.getHydroManager().getPacketReceiver().registerCallBack(new PacketCallBack<GameInfoToHubPacket>(GameInfoToHubPacket.class)
        {
            @Override
            public void call(GameInfoToHubPacket packet)
            {
                for (AbstractGame game : games.values())
                {
                    game.getSigns().values().stream().filter(sign -> sign.getTemplate().equalsIgnoreCase(packet.getTemplateID())).forEach(sign ->
                    {
                        sign.setPlayerWaitFor(packet.getPlayerWaitFor());
                        sign.setTotalPlayerOnServers(packet.getTotalPlayerOnServers());
                        sign.update();
                    });
                }
            }
        });

        hub.getHydroManager().getPacketReceiver().registerCallBack(new PacketCallBack<QueueAddPlayerPacket>(QueueAddPlayerPacket.class)
        {
            @Override
            public void call(QueueAddPlayerPacket packet)
            {
                Player player = Bukkit.getPlayer(packet.getPlayer().getUUID());

                if(player != null)
                {
                    player.sendMessage(ChatColor.GREEN + "Vous avez été ajouté à la queue " + ChatColor.GOLD + games.get(packet.getGame()).getName() + " : " + packet.getMap() + ChatColor.GREEN + " !");
                }
            }
        });

        hub.getHydroManager().getPacketReceiver().registerCallBack(new PacketCallBack<QueueInfosUpdatePacket>(QueueInfosUpdatePacket.class)
        {
            @Override
            public void call(QueueInfosUpdatePacket packet)
            {
                Player player = Bukkit.getPlayer(packet.getPlayer().getUUID());

                if (player == null || packet.isSuccess() || packet.getErrorMessage() == null)
                    return;

                player.sendRawMessage(packet.getErrorMessage());
            }
        });
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
