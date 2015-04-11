package net.samagames.hub.games.sign;

import net.samagames.api.signs.SignData;
import net.samagames.hub.Hub;
import net.samagames.hub.games.AbstractGame;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.util.ArrayList;

public class GameSignZone
{
    private final AbstractGame game;
    private final String map;
    private final ArrayList<GameSign> signs;

    public GameSignZone(AbstractGame game, String map, ArrayList<Location> signsLocations)
    {
        this.game = game;
        this.map = map;
        this.signs = new ArrayList<>();

        for(Location location : signsLocations)
        {
            Block block = Hub.getInstance().getHubWorld().getBlockAt(location);

            if(block.getType() == Material.SIGN || block.getType() == Material.SIGN_POST || block.getType() == Material.WALL_SIGN)
                this.signs.add(new GameSign(this, (Sign) Hub.getInstance().getHubWorld().getBlockAt(location).getState()));
        }
    }

    public void updateSign(SignData data)
    {
        if(this.signs.size() >= Integer.valueOf(data.getBungeeName().split("_")[1]))
        {
            this.signs.get((Integer.valueOf(data.getBungeeName().split("_")[1]) - 1)).update(data);
        }
    }

    public GameSign getGameSignByLocation(Location location)
    {
        for(GameSign sign : this.signs)
            if(sign.getSign().getLocation().equals(location))
                return sign;

        return null;
    }

    public AbstractGame getGame()
    {
        return this.game;
    }

    public String getMap()
    {
        return this.map;
    }

    public ArrayList<GameSign> getSigns()
    {
        return this.signs;
    }
}
