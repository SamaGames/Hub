package net.samagames.hub.games.sign;

import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;

public class Pacman
{
    private final int taskId;
    private final ArrayList<GameSign> signs;

    private GameSign actualSign;
    private int index;
    private int position;

    public Pacman(Player launcher)
    {
        Bukkit.broadcastMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Ouvrez les yeux ! Le Pacman a été libéré par " + ChatColor.RED + ChatColor.BOLD + launcher.getName() + ChatColor.GOLD + ChatColor.BOLD + " !");

        this.signs = new ArrayList<>();

        for(AbstractGame game : Hub.getInstance().getGameManager().getGames().values())
        {
            for(GameSignZone zone : game.getSignZones().values())
            {
                this.signs.addAll(zone.getSigns().stream().collect(Collectors.toList()));
            }
        }
        
        this.generateTerrain();
        this.taskId = Bukkit.getScheduler().scheduleAsyncRepeatingTask(Hub.getInstance(), this::update, 5L, 5L);
    }

    public void stop()
    {
        Bukkit.getScheduler().cancelTask(this.taskId);

        for(GameSign sign : this.signs)
        {
            sign.getSign().setLine(1, "");
            Bukkit.getScheduler().runTask(Hub.getInstance(), sign.getSign()::update);
        }
    }

    public void update()
    {
        if(this.actualSign == null)
        {
            this.actualSign = this.randomSign();
            this.actualSign.setPacman(true);
            this.position = 1;
        }
        else if(this.position == 14)
        {
            this.generateSign(this.actualSign);
            this.actualSign.setPacman(false);
            this.actualSign = this.randomSign();
            this.actualSign.setPacman(true);
            this.position = 1;

            if(new Random().nextInt(200) < 10)
                this.actualSign.getSign().getLocation().getWorld().strikeLightning(this.actualSign.getSign().getLocation());
        }

        String line = ". . . . . . .";
        String begin = ChatColor.BLACK + line.substring(0, this.position);
        String end = line.substring(this.position);
        
        begin = ChatColor.GRAY + begin.substring(0, begin.length() - 1) + ChatColor.DARK_RED + ChatColor.BOLD + "<" + ChatColor.BLACK;

        line = begin + end;

        this.position++;
        this.actualSign.getSign().setLine(1, line);
        Bukkit.getScheduler().runTask(Hub.getInstance(), this.actualSign.getSign()::update);
    }

    public void generateTerrain()
    {
        this.signs.forEach(this::generateSign);
    }

    public void generateSign(GameSign sign)
    {
        sign.getSign().setLine(1, ChatColor.BLACK + ". . . . . . .");
        Bukkit.getScheduler().runTask(Hub.getInstance(), sign.getSign()::update);
    }

    public GameSign randomSign()
    {
        Random random = new Random();
        this.index = random.nextInt(this.signs.size());
        return this.signs.get(this.index);
    }
}
