package net.samagames.hub.games;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.packets.hubinfo.GameInfoToHubPacket;
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
                try{
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
                            player.sendMessage(ChatColor.GOLD + "- " + ChatColor.GREEN + "Ajouté à la queue " + ChatColor.GOLD + packet.getGame() +  ChatColor.GREEN + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");
                        }
                        else
                        {
                            player.sendMessage(ChatColor.GOLD + "- " +  ChatColor.RED + "Retiré de la queue " + ChatColor.GOLD + packet.getGame() +  ChatColor.RED + " sur la map " + ChatColor.GOLD + packet.getMap() + ChatColor.GREEN + " !");
                        }
                    }
                }catch(Exception e)
                {
                }
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
