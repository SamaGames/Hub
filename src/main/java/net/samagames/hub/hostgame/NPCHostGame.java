package net.samagames.hub.hostgame;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.HostGameInfoToHubPacket;
import net.samagames.hub.games.signs.GameSign;
import net.samagames.tools.holograms.Hologram;
import net.samagames.tools.npc.nms.CustomNPC;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

/**
 * ╱╲＿＿＿＿＿＿╱╲
 * ▏╭━━╮╭━━╮▕
 * ▏┃＿＿┃┃＿＿┃▕
 * ▏┃＿▉┃┃▉＿┃▕
 * ▏╰━━╯╰━━╯▕
 * ╲╰╰╯╲╱╰╯╯╱  Created by Silvanosky on 27/10/2016
 * ╱╰╯╰╯╰╯╰╯╲
 * ▏▕╰╯╰╯╰╯▏▕
 * ▏▕╯╰╯╰╯╰▏▕
 * ╲╱╲╯╰╯╰╱╲╱
 * ＿＿╱▕▔▔▏╲＿＿
 * ＿＿▔▔＿＿▔▔＿＿
 */
public class NPCHostGame
{
    private final Hub hub;

    private final UUID event;
    private final UUID creator;
    private final String name;
    private final String serverName;
    private final int index;

    private final String templateId;

    private HashMap<UUID, Boolean> clicked = new HashMap<>();

    private CustomNPC npc;

    public NPCHostGame(Hub hub, int index, Location location, HostGameInfoToHubPacket packet)
    {
        this.hub = hub;

        this.event = packet.getEvent();
        this.creator = packet.getCreator();
        this.name = SamaGamesAPI.get().getUUIDTranslator().getName(packet.getCreator());
        this.serverName = packet.getServerName();
        this.templateId = packet.getTemplateId();
        this.index = index;

        hub.getHostGameManager().log(Level.INFO, "Creation NPC HOST uuid:" + this.creator);

        this.npc = SamaGamesAPI.get().getNPCManager().createNPC(location, packet.getCreator(), new String[] {
                ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Nouvel Evenement !"
        });

        this.npc.setCallback((b, player) -> this.connect(player));
        this.update(packet);
    }

    public void connect(Player player)
    {
        if(clicked.containsKey(player.getUniqueId()))
            return;

        clicked.put(player.getUniqueId(), null);
        GameSign.addToQueue(player, templateId);

        //Prevent double click
        Bukkit.getScheduler().runTaskLaterAsynchronously(hub, () -> clicked.remove(player.getUniqueId()), 1L);

        //SamaGamesAPI.get().getPubSub().send("join." + this.serverName, player.getUniqueId().toString());
    }

    public void update(HostGameInfoToHubPacket packet)
    {
        if(!packet.getEvent().equals(this.event) || this.npc == null)
            return;

        Hologram hologram = this.npc.getHologram();
        hologram.change(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Evènement " + this.name,
                ChatColor.GOLD + "" + packet.getTotalPlayerOnServers() + ChatColor.AQUA +  "/" + ChatColor.RED + packet.getPlayerMaxForMap());
    }

    public void remove()
    {
        SamaGamesAPI.get().getNPCManager().removeNPC(this.npc);
    }

    public int getIndex()
    {
        return this.index;
    }
}