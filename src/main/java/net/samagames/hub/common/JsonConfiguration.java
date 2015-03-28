package net.samagames.hub.common;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonArray;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonObject;
import org.bukkit.craftbukkit.libs.com.google.gson.JsonParser;

import java.io.*;
import java.util.logging.Level;

public class JsonConfiguration
{
    private final File configurationFile;
    private JsonObject configuration;

    public JsonConfiguration(File configurationFile)
    {
        this.configurationFile = configurationFile;
        this.reload();
    }

    public JsonConfiguration(String path)
    {
        this(new File(path));
    }

    public void reload()
    {
        try
        {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.configurationFile), "utf-8"));
            StringBuilder builder = new StringBuilder();
            String currentLine;

            while((currentLine = br.readLine()) != null)
            {
                builder.append(currentLine);
            }

            br.close();

            this.configuration = new JsonParser().parse(builder.toString()).getAsJsonObject();
        }
        catch (IOException ex)
        {
            Bukkit.getLogger().log(Level.SEVERE, "Can't load file '" + this.configurationFile.getName() + "'");
        }
    }

    public void save()
    {
        Gson gson = new Gson();

        try
        {
            FileWriter writer = new FileWriter(this.configurationFile);
            writer.write(gson.toJson(this.configuration));
            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void setString(String key, String value)
    {
        this.configuration.addProperty(key, value);
    }

    public void setInt(String key, int value)
    {
        this.configuration.addProperty(key, value);
    }

    public void setBoolean(String key, boolean value)
    {
        this.configuration.addProperty(key, value);
    }

    public void setChar(String key, char value)
    {
        this.configuration.addProperty(key, value);
    }

    public void setJsonObject(String key, JsonObject value)
    {
        this.configuration.add(key, value);
    }

    public void setJsonArray(String key, JsonArray value)
    {
        this.configuration.add(key, value);
    }

    public String getString(String key)
    {
        return this.configuration.get(key).getAsString();
    }

    public int getInt(String key)
    {
        return this.configuration.get(key).getAsInt();
    }

    public boolean getBoolean(String key)
    {
        return this.configuration.get(key).getAsBoolean();
    }

    public char getChar(String key)
    {
        return this.configuration.get(key).getAsCharacter();
    }

    public JsonObject getJsonObject(String key)
    {
        return this.configuration.get(key).getAsJsonObject();
    }

    public JsonArray getJsonArray(String key)
    {
        return this.configuration.get(key).getAsJsonArray();
    }

    public JsonObject getConfiguration()
    {
        return this.configuration;
    }
}
