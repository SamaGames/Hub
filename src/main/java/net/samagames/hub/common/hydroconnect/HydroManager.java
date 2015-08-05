package net.samagames.hub.common.hydroconnect;

import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroconnect.connection.ConnectionManager;

/**
 * This file is a part of the SamaGames Project CodeBase
 * This code is absolutely confidential.
 * Created by Geekpower14 on 17/07/2015.
 * (C) Copyright Elydra Network 2014 & 2015
 * All rights reserved.
 */
public class HydroManager {

    private Hub plugin;

    private ConnectionManager connectionManager;

    public HydroManager(Hub plugin)
    {

        this.plugin = plugin;

        connectionManager = new ConnectionManager(plugin);
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
