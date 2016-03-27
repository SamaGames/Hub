package net.samagames.hub.common.tasks;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import org.bukkit.entity.Player;

public class TaskManager extends AbstractManager
{
    private final CirclesTask circlesTask;

    public TaskManager(Hub hub)
    {
        super(hub);

        this.circlesTask = new CirclesTask(hub);
    }

    @Override
    public void onDisable()
    {
        this.circlesTask.cancel();
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    public CirclesTask getCirclesTask()
    {
        return this.circlesTask;
    }
}
