package net.samagames.hub.npcs;

import com.google.gson.*;
import net.samagames.hub.utils.ItemStackUtils;

import java.lang.reflect.Type;

public class NPCSerializer implements JsonSerializer<NPC>
{
    @Override
    public JsonElement serialize(NPC npc, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonObject json = new JsonObject();

        json.addProperty("id", npc.getID().toString());
        json.addProperty("name", npc.getName());
        json.addProperty("owner", npc.getOwner().toString());

        JsonArray jsonArmor = new JsonArray();

        for(int i = 0; i < 4; i++)
            jsonArmor.add(new JsonPrimitive(ItemStackUtils.isToStr(npc.getArmor()[i])));

        json.add("armor", jsonArmor);
        json.addProperty("item-in-hand", ItemStackUtils.isToStr(npc.getItemInHand()));

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
