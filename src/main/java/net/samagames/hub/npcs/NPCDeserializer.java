package net.samagames.hub.npcs;

import com.google.gson.*;
import net.samagames.hub.npcs.actions.AbstractNPCAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Villager;

import java.lang.reflect.Type;
import java.util.UUID;

public class NPCDeserializer implements JsonDeserializer<NPC>
{
    @Override
    public NPC deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException
    {
        JsonObject json = jsonElement.getAsJsonObject();
        UUID uuid = UUID.fromString(json.get("id").getAsString());
        String name = json.get("name").getAsString();
        Villager.Profession profession = Villager.Profession.valueOf(json.get("profession").getAsString());

        JsonObject jsonLocation = json.get("location").getAsJsonObject();
        Location location = new Location(Bukkit.getWorlds().get(0), jsonLocation.get("x").getAsInt(), jsonLocation.get("y").getAsInt(), jsonLocation.get("z").getAsInt(), jsonLocation.get("yaw").getAsFloat(), jsonLocation.get("pitch").getAsFloat());

        AbstractNPCAction action = null;

        try
        {
            Class actionClass = Class.forName(AbstractNPCAction.class.getPackage().getName() + "." + json.get("action").toString().replaceAll("\"", ""));

            if(actionClass != null)
            {
                action = (AbstractNPCAction) actionClass.newInstance();
            }
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e)
        {
            e.printStackTrace();
            return null;
        }

        return new NPC(uuid, name, profession, location, action);
    }
}
