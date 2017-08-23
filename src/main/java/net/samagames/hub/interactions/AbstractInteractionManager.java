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

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
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
