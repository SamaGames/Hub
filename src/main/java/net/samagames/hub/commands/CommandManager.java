package net.samagames.hub.commands;

import net.samagames.hub.Hub;
import net.samagames.hub.commands.admin.*;
import net.samagames.hub.commands.moderating.*;
import net.samagames.hub.commands.players.*;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;

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
