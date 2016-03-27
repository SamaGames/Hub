package net.samagames.hub.common.managers;


import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class EventBus implements EntryPoints
{
    private List<AbstractManager> managers;

    public EventBus()
    {
        this.managers = new ArrayList<>();
    }

    public void registerManager(AbstractManager manager)
    {
        this.managers.add(manager);
    }

    @Override
    public void onDisable()
    {
        this.managers.stream().forEach(AbstractManager::onDisable);
    }

    @Override
    public void onLogin(Player player)
    {
        this.managers.stream().forEach(manager -> manager.onLogin(player));
    }

    @Override
    public void onLogout(Player player)
    {
        this.managers.stream().forEach(manager -> manager.onLogout(player));
    }
}
