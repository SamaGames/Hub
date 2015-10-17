package net.samagames.hub.commands;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.admin.*;
import net.samagames.hub.commands.moderating.*;
import net.samagames.hub.commands.players.CommandClickMe;
import net.samagames.hub.commands.players.CommandJukebox;
import net.samagames.hub.commands.players.CommandMeh;
import net.samagames.hub.commands.players.CommandWoot;
import net.samagames.hub.common.managers.AbstractManager;

import java.util.logging.Level;

public class CommandManager extends AbstractManager
{
    public CommandManager(Hub hub)
    {
        super(hub);
        this.registerCommands();
    }

    public void registerCommands()
    {
        // Player commands ----------------------------------------------
        this.registerCommand("click", null, CommandClickMe.class);
        this.registerCommand("meh", null, CommandMeh.class);
        this.registerCommand("woot", null, CommandWoot.class);
        this.registerCommand("jukebox", null, CommandJukebox.class);

        // Moderating commands ------------------------------------------
        this.registerCommand("pllock", "hub.jukebox.lock", CommandPlaylistLock.class);
        this.registerCommand("plnext", "hub.jukebox.next", CommandPlaylistNext.class);
        this.registerCommand("plclear", "hub.jukebox.clear", CommandPlaylistClear.class);
        this.registerCommand("slow", "hub.mod.slow", CommandSlow.class);
        this.registerCommand("shutup", "hub.mod.shutup", CommandShutup.class);

        // Admin commands -----------------------------------------------
        //this.registerCommand("npc", "hub.admin.npc", CommandNPC.class);
        this.registerCommand("sign", "hub.admin.sign", CommandSign.class);
        this.registerCommand("anguille", "hub.anguille", CommandAnguille.class);
        this.registerCommand("nbs", "hub.jukebox.nbs", CommandNBS.class);
        this.registerCommand("evacuate", "hub.admin.evacuate", CommandEvacuate.class);

        // Easter Eggs commands -----------------------------------------
    }

    private void registerCommand(String executionTag, String permission, Class<? extends AbstractCommand> command)
    {
        try
        {
            AbstractCommand abCommand = command.newInstance();
            abCommand.setPermission(permission);

            this.hub.getCommand(executionTag).setExecutor(abCommand);
            this.hub.log(this, Level.INFO, "Registered command '" + executionTag + "'");
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            this.hub.log(this, Level.SEVERE, "Failed to register command '" + executionTag + "'!");
        }
    }

    @Override
    public String getName() { return "CommandManager"; }
}
