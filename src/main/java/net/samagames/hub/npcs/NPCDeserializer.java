package net.samagames.hub.npcs;

import com.google.gson.*;
import net.samagames.hub.npcs.actions.AbstractNPCAction;
import net.samagames.hub.utils.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

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
        UUID owner = UUID.fromString(json.get("owner").getAsString());

        JsonArray jsonPropertiesArmor = json.get("armor").getAsJsonArray();
        ItemStack[] armor = new ItemStack[4];

        for(int i = 0; i < 4; i++)
            armor[i] = ItemStackUtils.strToIs(jsonPropertiesArmor.get(i).getAsString());

        ItemStack itemInHand = ItemStackUtils.strToIs(json.get("item-in-hand").getAsString());

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

        return new NPC(uuid, name, owner, armor, itemInHand, location, action);
    }
}
