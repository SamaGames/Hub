package net.samagames.hub.common.managers;

import com.google.gson.JsonObject;
import net.samagames.hub.Hub;
import net.samagames.hub.interactions.AbstractInteractionManager;
import net.samagames.tools.JsonConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;

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
public abstract class AbstractManager implements EntryPoints
{
    protected final Hub hub;
    private String filename;

    public AbstractManager(Hub hub, String filename)
    {
        this(hub);

        this.filename = filename;
    }

    public AbstractManager(Hub hub)
    {
        this.hub = hub;

        if (!(this instanceof AbstractInteractionManager))
            this.hub.getEventBus().registerManager(this);
    }

    public void log(Level level, String message)
    {
        this.hub.getLogger().log(level, "[" + this.getClass().getSimpleName() + "] " + message);
    }

    protected JsonObject reloadConfiguration()
    {
        File configuration = new File(this.hub.getDataFolder(), this.filename);

        if (!configuration.exists())
        {
            try(PrintWriter writer = new PrintWriter(configuration))
            {
                configuration.createNewFile();
                writer.println("{}");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return new JsonConfiguration(configuration).load();
    }
}
