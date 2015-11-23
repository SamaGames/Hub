package net.samagames.hub.utils;

import net.samagames.hub.Hub;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLAgent
{
    private final Hub instance;
    private final String host;
    private final String database;
    private final String username;
    private final String password;

    public MySQLAgent(Hub instance)
    {
        this.instance = instance;

        this.host = instance.getConfig().getString("sql-host");
        this.database = instance.getConfig().getString("sql-database");
        this.username = instance.getConfig().getString("sql-username");
        this.password = instance.getConfig().getString("sql-password");
    }

    public Connection openMinecraftServerConnection()
    {
        return this.openConnection(this.host, (this.host.contains(":") ? this.host.split(":")[1] : "3306"), this.database, this.username, this.password);
    }

    public Connection openConnection(String host, String port, String db, String user, String password)
    {
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }

        String url = "jdbc:mysql://" + host + ":" + port + "/" + db + "?characterEncoding=UTF-8";

        try
        {
            return DriverManager.getConnection(url, user, password);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return null;
    }
}
