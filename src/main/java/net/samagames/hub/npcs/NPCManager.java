package net.samagames.hub.npcs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.minecraft.server.v1_8_R3.World;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import redis.clients.jedis.Jedis;

import java.util.*;
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

    @Override
    public void onServerClose()
    {
        this.removeNPCS();
    }

    public void reloadNPCS()
    {
        String baseKey = "hub:npcs";
        Jedis jedis = SamaGamesAPI.get().getBungeeResource();
        Map<String, String> array = jedis.hgetAll(baseKey);
        jedis.close();
        this.removeNPCS();
        this.npcs.clear();

        for(String npcJson : array.values())
        {
            GsonBuilder gsonBuilder = new GsonBuilder();
            gsonBuilder.registerTypeAdapter(NPC.class, new NPCDeserializer());
            Gson gson = gsonBuilder.create();
            NPC npc = gson.fromJson(npcJson, NPC.class);

            this.npcs.put(npc.getID(), npc);
            Hub.getInstance().log(this, Level.INFO, "Registered NPC '" + npc.getID() + "'");
        }

        this.placeNPCS();
    }

    public void placeNPCS()
    {
        for(NPC npc : this.npcs.values())
        {
            World craftbukkitWorld = ((CraftWorld) npc.getLocation().getWorld()).getHandle();

            CustomEntityNPC entityNPC = new CustomEntityNPC(npc, npc.getLocation());
            craftbukkitWorld.addEntity(entityNPC, CreatureSpawnEvent.SpawnReason.CUSTOM);
            entityNPC.getBukkitEntity().teleport(npc.getLocation());

            ArrayList<String> lines = new ArrayList<>();
            lines.addAll(Arrays.asList(npc.getName().split("@")));

            if(this.debug)
                lines.add(ChatColor.RED + "[" + npc.getID() + "]");

            String[] linesArray = new String[] {};
            Hologram hologram = new Hologram(lines.toArray(linesArray));

            hologram.setLocation(npc.getLocation().clone().add(0.0D, 2.0D, 0.0D));
            npc.setHologramID(Hub.getInstance().getHologramManager().registerHologram(hologram));

            entityNPC.getBukkitEntity().setMetadata("npc-id", new FixedMetadataValue(Hub.getInstance(), npc.getID()));

            Hub.getInstance().log(this, Level.INFO, "Placed NPC at [" + npc.getLocation().getBlockX() + "] [" + npc.getLocation().getBlockY() + "] [" + npc.getLocation().getBlockZ() + "]");
        }
    }

    public void removeNPCS()
    {
        Bukkit.getWorlds().get(0).getEntities().stream().filter(entity -> entity.getType() == EntityType.VILLAGER).filter(entity -> entity.hasMetadata("npc-id")).forEach(entity ->
        {
            if (this.getNPCByID(UUID.fromString(entity.getMetadata("npc-id").get(0).asString())) != null)
                Hub.getInstance().getHologramManager().removeHologram(this.getNPCByID(UUID.fromString(entity.getMetadata("npc-id").get(0).asString())).getHologramID());

            Hub.getInstance().log(this, Level.INFO, "Removed NPC at [" + entity.getLocation().getBlockX() + "] [" + entity.getLocation().getBlockY() + "] [" + entity.getLocation().getBlockZ() + "]");
            entity.remove();
        });
    }

    public void listNPCS(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des NPCs :");

        for(NPC npc : this.npcs.values())
        {
            player.sendMessage(ChatColor.DARK_GRAY + "{");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npc.getID().toString() + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + ChatColor.stripColor(npc.getName()) + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npc.getLocation().getBlockX() + ";" + npc.getLocation().getBlockY() + ";" + npc.getLocation().getBlockZ() + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npc.getAction().getClass().getSimpleName() + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "}");
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
