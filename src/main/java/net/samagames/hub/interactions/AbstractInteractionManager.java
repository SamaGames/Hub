package net.samagames.hub.interactions;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.JsonConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractInteractionManager<T extends AbstractInteraction> extends AbstractManager
{
    protected final List<T> interactions;

    public AbstractInteractionManager(Hub hub, String name)
    {
        super(hub);

        this.interactions = new ArrayList<>();

        File yodelsConfigurationFile = new File(hub.getDataFolder(), "interactions" + File.separator + name + ".json");

        if (!yodelsConfigurationFile.exists())
        {
            try
            {
                yodelsConfigurationFile.createNewFile();

                PrintWriter writer = new PrintWriter(yodelsConfigurationFile);
                writer.println("{ \"" + name + "\": [] }");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        JsonConfiguration yodelsConfiguration = new JsonConfiguration(yodelsConfigurationFile);
        JsonObject jsonRoot = yodelsConfiguration.load();

        if (jsonRoot == null)
            return;

        this.loadConfiguration(jsonRoot.getAsJsonArray(name));
    }

    public abstract void loadConfiguration(JsonArray rootJson);

    @Override
    public void onDisable()
    {
        this.interactions.forEach(AbstractInteraction::onDisable);
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player)
    {
        this.interactions.stream().filter(interaction -> interaction.hasPlayer(player)).forEach(interaction -> interaction.stop(player));
    }

    public List<T> getInteractions()
    {
        return this.interactions;
    }

    public boolean hasPlayer(Player player)
    {
        return this.interactions.stream().filter((interaction) -> interaction.hasPlayer(player)).findFirst().orElse(null) != null;
    }
}
