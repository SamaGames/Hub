package net.samagames.hub.hostgame;

import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.common.hydroangeas.packets.hubinfos.HostGameInfoToHubPacket;
import net.samagames.tools.holograms.Hologram;
import net.samagames.tools.npc.nms.CustomNPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;

import java.util.UUID;

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
public class NPCHostGame {

    private UUID event;

    private UUID creator;
    private String name;

    private CustomNPC npc;

    public NPCHostGame(Location location, HostGameInfoToHubPacket packet)
    {

        this.event = packet.getEvent();
        this.creator = packet.getCreator();
        this.name = SamaGamesAPI.get().getUUIDTranslator().getName(packet.getCreator());

        npc = SamaGamesAPI.get().getNPCManager().createNPC(location, packet.getCreator(),
                new String[]{
                        ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Nouvel Evenement !"
        });
        update(packet);
    }

    public void update(HostGameInfoToHubPacket packet)
    {
        if(!packet.getEvent().equals(event))
            return;

        Hologram hologram = npc.getHologram();
        hologram.change(
                new String[]{
                        ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Evenement " + name,
                        ChatColor.GOLD + "" + packet.getTotalPlayerOnServers() + ChatColor.AQUA +  "/" + ChatColor.RED + packet.getPlayerMaxForMap()
                });
    }

    public void remove()
    {
        SamaGamesAPI.get().getNPCManager().removeNPC(npc);
    }
}
