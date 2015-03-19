package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteBlockPlayerMain {

    public static NoteBlockPlayerMain plugin;
    public HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
    public HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

    public NoteBlockPlayerMain() {
        plugin = this;
    }

    public static boolean isReceivingSong(Player p) {
        return ((plugin.playingSongs.get(p.getName()) != null) && (!plugin.playingSongs.get(p.getName()).isEmpty()));
    }

    public static void stopPlaying(Player p) {
        if (plugin.playingSongs.get(p.getName()) == null) {
            return;
        }
        for (SongPlayer s : plugin.playingSongs.get(p.getName())) {
            s.removePlayer(p);
        }
    }

    public static void setPlayerVolume(Player p, byte volume) {
        plugin.playerVolume.put(p.getName(), volume);
    }

    public static byte getPlayerVolume(Player p) {
        Byte b = plugin.playerVolume.get(p.getName());
        if (b == null) {
            b = 100;
            plugin.playerVolume.put(p.getName(), b);
        }
        return b;
    }
}
