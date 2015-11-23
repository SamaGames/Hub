package net.samagames.hub.games.sign;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.hub.games.AbstractGame;
import net.samagames.tools.LocationUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public class SignManager extends AbstractManager
{
    public SignManager(Hub hub)
    {
        super(hub);

        this.reloadList();
    }

    public void reloadList()
    {
        this.hub.log(this, Level.INFO, "Reloading game sign list...");

        this.hub.getGameManager().getGames().values().forEach(net.samagames.hub.games.AbstractGame::clearSigns);

        try (Connection sql = this.hub.getMySQLAgent().openMinecraftServerConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sg_signs");
            ResultSet result = pStatement.executeQuery();

            while(result.next())
            {
                String game = result.getString("game");

                AbstractGame gameObject = this.hub.getGameManager().getGameByIdentifier(game);

                if(gameObject == null)
                {
                    this.hub.log(this, Level.SEVERE, "Wanted to register a game sign withing an unknown game!");
                    continue;
                }

                String map = result.getString("map");
                ChatColor color = ChatColor.valueOf(result.getString("color"));
                String template = result.getString("template");
                Location location = LocationUtils.str2loc(result.getString("location"));
                boolean maintenance = result.getBoolean("maintenance");

                Block block = Hub.getInstance().getHubWorld().getBlockAt(location);

                if(!(block.getState() instanceof Sign))
                {
                    this.hub.log(this, Level.SEVERE, "Sign block for game '" + game + "' and map '" + map + "' is not a sign in the world!");
                    continue;
                }

                gameObject.addSignForMap(map.replace("_", " "), (Sign) block.getState(), template, color);
                gameObject.getGameSignByTemplate(template).setMaintenance(maintenance);

                this.hub.log(this, Level.INFO, "Registered sign zone for the game '" + game + "' and the map '" + map + "'!");
            }

            sql.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        this.hub.log(this, Level.INFO, "Reloaded game sign list.");
    }

    @Override
    public String getName() { return "SignManager"; }
}
