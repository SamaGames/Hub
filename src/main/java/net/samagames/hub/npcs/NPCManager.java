package net.samagames.hub.npcs;

import net.minecraft.server.v1_8_R1.World;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.libs.com.google.gson.Gson;
import org.bukkit.craftbukkit.libs.com.google.gson.GsonBuilder;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class NPCManager extends AbstractManager
{
    private HashMap<UUID, NPC> npcs;
    private ArrayList<UUID> talking;
    private boolean debug;

    public NPCManager(Hub hub)
    {
        super(hub);

        this.debug = false;
        this.npcs = new HashMap<>();
        this.talking = new ArrayList<>();
        this.reloadNPCS();
    }

    public void reloadNPCS()
    {
        String baseKey = "lobby:npcs";
        Map<String, String> array = SamaGamesAPI.get().getResource().hgetAll(baseKey);

        this.removeNPCS();
        this.npcs.clear();

        for(String npcJson : array.values())
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(NPC.class, new NPCDeserializer());
            Gson gson = gsonBuilder.create();
            NPC npc = gson.fromJson(npcJson, NPC.class);

            this.npcs.put(npc.getID(), npc);
            Hub.getInstance().log(this, Level.INFO, "Registered NPC '" + npc.getID().toString() + "'");
        }

        this.placeNPCS();
    }

    public void addNPC(String name, Villager.Profession profession, Location location, String actionClassName)
    {
        UUID uuid = UUID.randomUUID();
        NPC npc = new NPC(uuid, ChatColor.translateAlternateColorCodes('&', name), profession, location, actionClassName);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(NPC.class, new NPCSerializer());
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        String json = gson.toJson(npc);

        SamaGamesAPI.get().getResource().hset("lobby:npcs", uuid.toString(), json);
    }

    public void removeNPC(UUID uuid)
    {
        SamaGamesAPI.get().getResource().hdel("lobby:npcs", uuid.toString());
    }

    public void placeNPCS()
    {
        for(NPC npc : this.npcs.values())
        {
            World craftbukkitWorld = ((CraftWorld) npc.getLocation().getWorld()).getHandle();

            CustomEntityVillager villager = new CustomEntityVillager(craftbukkitWorld);
            craftbukkitWorld.addEntity(villager, CreatureSpawnEvent.SpawnReason.CUSTOM);
            villager.getBukkitEntity().teleport(npc.getLocation());

            Hologram hologram;

            if(this.debug)
                hologram = new Hologram(ChatColor.GOLD + "" + ChatColor.BOLD + npc.getName() + " [" + npc.getID().toString() + "]");
            else
                hologram = new Hologram(ChatColor.GOLD + "" + ChatColor.BOLD + npc.getName());

            // TODO
            //hologram.setLocation(npc.getLocation().clone().add(0.0D, 1.75D, 0.0D));
            //hologram.generateLines();

            npc.setHologram(hologram);

            ((Villager) villager.getBukkitEntity()).setProfession(npc.getProfession());
            villager.getBukkitEntity().setMetadata("npc-id", new FixedMetadataValue(Hub.getInstance(), npc.getID()));

            Hub.getInstance().log(this, Level.INFO, "Placed NPC & NPC Title Skull at [" + npc.getLocation().getBlockX() + "] [" + npc.getLocation().getBlockY() + "] [" + npc.getLocation().getBlockZ() + "]");
        }
    }

    public void removeNPCS()
    {
        for(Entity entity : Bukkit.getWorlds().get(0).getEntities())
        {
            if(entity.getType() == EntityType.VILLAGER)
            {
                if(entity.hasMetadata("npc-id"))
                {
                    Hub.getInstance().log(this, Level.INFO, "Removed NPC at [" + entity.getLocation().getBlockX() + "] [" + entity.getLocation().getBlockY() + "] [" + entity.getLocation().getBlockZ() + "]");
                    entity.remove();
                }
            }
        }
    }

    public void listNPCS(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des NPCs :");

        for(NPC npc : this.npcs.values())
        {
            StringBuilder npcInfos = new StringBuilder();
            npcInfos.append(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY).append(npc.getID().toString()).append(ChatColor.DARK_GRAY + "] ");
            npcInfos.append(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY).append(npc.getName()).append(ChatColor.DARK_GRAY + "] ");
            npcInfos.append(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY).append(npc.getLocation().getBlockX() + ";" + npc.getLocation().getBlockY() + ";" + npc.getLocation().getBlockZ()).append(ChatColor.DARK_GRAY + "] ");
            npcInfos.append(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY).append(npc.getAction().getClass().getSimpleName()).append(ChatColor.DARK_GRAY + "] ");

            player.sendMessage(npcInfos.toString());
        }
    }

    public void talk(Player player)
    {
        this.talking.add(player.getUniqueId());
    }

    public void talkFinished(Player player)
    {
        this.talking.remove(player.getUniqueId());
    }

    public void toggleDebug(boolean debug)
    {
        this.debug = debug;
        this.reloadNPCS();
    }

    public NPC getNPCByID(UUID uuid)
    {
        return this.npcs.get(uuid);
    }

    public boolean canTalk(Player player)
    {
        return !this.talking.contains(player.getUniqueId());
    }

    public boolean hasNPC(UUID uuid)
    {
        return this.npcs.containsKey(uuid);
    }

    @Override
    public String getName() { return "NPCManager"; }
}
