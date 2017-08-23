package net.samagames.hub.commands;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.admin.*;
import net.samagames.hub.commands.animating.CommandEvent;
import net.samagames.hub.commands.moderating.*;
import net.samagames.hub.commands.players.*;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
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
public class CommandManager extends AbstractManager
{
    public CommandManager(Hub hub)
    {
        super(hub);
        this.registerCommands();
    }

    @Override
    public void onDisable() { /** Not needed **/ }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    private void registerCommands()
    {
        // Player commands ----------------------------------------------
        this.registerCommand("click", null, CommandClickme.class);
        this.registerCommand("meh", null, CommandMeh.class);
        this.registerCommand("woot", null, CommandWoot.class);
        this.registerCommand("jukebox", null, CommandJukebox.class);
        this.registerCommand("join", null, CommandJoin.class);
        this.registerCommand("fly", null, CommandFly.class);

        // Animating commands
        this.registerCommand("event", "hub.animating.event", CommandEvent.class);

        // Moderating commands ------------------------------------------
        this.registerCommand("pllock", "hub.jukebox.lock", CommandPlaylistLock.class);
        this.registerCommand("plnext", "hub.jukebox.next", CommandPlaylistNext.class);
        this.registerCommand("plclear", "hub.jukebox.clear", CommandPlaylistClear.class);
        this.registerCommand("slow", "hub.mod.slow", CommandSlow.class);
        this.registerCommand("shutup", "hub.mod.shutup", CommandShutup.class);

        // Admin commands -----------------------------------------------
        this.registerCommand("sign", "hub.admin.sign", CommandSign.class);
        this.registerCommand("anguille", "hub.anguille", CommandAnguille.class);
        this.registerCommand("nbs", "hub.jukebox.nbs", CommandNBS.class);
        this.registerCommand("evacuate", "hub.admin.evacuate", CommandEvacuate.class);
        this.registerCommand("pearl", "hub.admin.pearl", CommandPearl.class);

        // Easter Eggs commands -----------------------------------------
    }

    private void registerCommand(String executionTag, String permission, Class<? extends AbstractCommand> command)
    {
        try
        {
            AbstractCommand abCommand = command.getDeclaredConstructor(Hub.class).newInstance(this.hub);
            abCommand.setPermission(permission);

            this.hub.getCommand(executionTag).setExecutor(abCommand);
            this.log(Level.INFO, "Registered command '" + executionTag + "'");
        }
        catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
        {
            this.log(Level.SEVERE, "Failed to register command '" + executionTag + "'!");
        }
    }
}
