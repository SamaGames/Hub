package net.samagames.hub.npcs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class NPCSerializer implements JsonSerializer<NPC>
{
    @Override
    public JsonElement serialize(NPC npc, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject json = new JsonObject();

        json.addProperty("id", npc.getID().toString());
        json.addProperty("name", npc.getName());
        json.addProperty("profession", npc.getProfession().name());

        JsonObject jsonLocation = new JsonObject();
        jsonLocation.addProperty("x", npc.getLocation().getBlockX());
        jsonLocation.addProperty("y", npc.getLocation().getBlockY());
        jsonLocation.addProperty("z", npc.getLocation().getBlockZ());
        jsonLocation.addProperty("yaw", npc.getLocation().getYaw());
        jsonLocation.addProperty("pitch", npc.getLocation().getPitch());

        json.add("location", jsonLocation);
        json.addProperty("action", npc.getAction().getClass().getSimpleName());

        return json;
    }
}
