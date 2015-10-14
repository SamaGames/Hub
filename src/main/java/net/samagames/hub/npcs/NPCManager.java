package net.samagames.hub.npcs;

import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.techcable.npclib.HumanNPC;
import net.techcable.npclib.NPC;
import net.techcable.npclib.NPCLib;
import net.techcable.npclib.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class NPCManager extends AbstractManager
{
    private final NPCRegistry npcRegistry;
    private final HashMap<UUID, NPCData> npcsDatas;
    private final HashMap<UUID, NPC> npcs;
    private final ArrayList<UUID> talking;

    public NPCManager(Hub hub)
    {
        super(hub);

        this.npcRegistry = NPCLib.getNPCRegistry(hub);
        this.npcsDatas = new HashMap<>();
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
        this.removeNPCS();

        this.npcsDatas.clear();
        this.npcs.clear();

        UUID uuid = UUID.randomUUID();

        this.npcsDatas.put(uuid, new NPCData(uuid, new String[]{
                ChatColor.AQUA + "" + ChatColor.BOLD + "• Tutoriel de Bienvenu •"
        }, UUID.fromString("c59220b1-662f-4aa8-b9d9-72660eb97c10"), null, null, new Location(this.hub.getHubWorld(), -10.5D, 80.0D, -12.5D, -45.0F, 0.0F), "TestAction"));

        this.placeNPCS();
    }

    public void placeNPCS()
    {
        for(NPCData npcData : this.npcsDatas.values())
        {
            HumanNPC npc = this.npcRegistry.createHumanNPC("");
            npc.setProtected(true);
            npc.setShowInTabList(false);
            npc.setSkin(npcData.getOwner());
            npc.faceLocation(npcData.getLocation());
            npc.spawn(npcData.getLocation());

            npc.getEntity().setMetadata("npc-id", new FixedMetadataValue(Hub.getInstance(), npcData.getID()));

            this.npcs.put(npcData.getID(), npc);

            Hub.getInstance().log(this, Level.INFO, "Placed NPC at [" + npcData.getLocation().getBlockX() + "] [" + npcData.getLocation().getBlockY() + "] [" + npcData.getLocation().getBlockZ() + "]");
        }
    }

    public void removeNPCS()
    {
        Bukkit.getWorlds().get(0).getEntities().stream().filter(entity -> entity.getType() == EntityType.VILLAGER).filter(entity -> entity.hasMetadata("npc-id")).forEach(entity ->
        {
            UUID uuid = UUID.fromString(entity.getMetadata("npc-id").get(0).asString());

            if (this.getNPCDataByID(uuid) != null)
                Hub.getInstance().getHologramManager().removeHologram(this.getNPCDataByID(uuid).getHologramID());

            this.npcs.get(uuid).despawn();

            Hub.getInstance().log(this, Level.INFO, "Removed NPC at [" + entity.getLocation().getBlockX() + "] [" + entity.getLocation().getBlockY() + "] [" + entity.getLocation().getBlockZ() + "]");
        });
    }

    public void listNPCS(Player player)
    {
        player.sendMessage(ChatColor.GREEN + "Liste des NPCs :");

        for(NPCData npcData : this.npcsDatas.values())
        {
            player.sendMessage(ChatColor.DARK_GRAY + "{");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npcData.getID().toString() + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npcData.getLocation().getBlockX() + ";" + npcData.getLocation().getBlockY() + ";" + npcData.getLocation().getBlockZ() + ChatColor.DARK_GRAY + "] ");
            player.sendMessage(ChatColor.DARK_GRAY + "[" + ChatColor.GRAY + npcData.getAction().getClass().getSimpleName() + ChatColor.DARK_GRAY + "] ");
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

    public NPCData getNPCDataByID(UUID uuid)
    {
        return this.npcsDatas.get(uuid);
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
