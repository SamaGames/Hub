package com.xxmicloxx.NoteBlockAPI;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class NoteBlockPlayerMain
{
    public static NoteBlockPlayerMain instance;
    public HashMap<String, ArrayList<SongPlayer>> playingSongs = new HashMap<String, ArrayList<SongPlayer>>();
    public HashMap<String, Byte> playerVolume = new HashMap<String, Byte>();

    public NoteBlockPlayerMain()
    {
        instance = this;
    }

    public static boolean isReceivingSong(Player p)
    {
        return ((instance.playingSongs.get(p.getName()) != null) && (!instance.playingSongs.get(p.getName()).isEmpty()));
    }

    public static void stopPlaying(Player p)
    {
        if (instance.playingSongs.get(p.getName()) == null)
            return;

        for (SongPlayer s : instance.playingSongs.get(p.getName()))
            s.removePlayer(p);
    }

    public static void setPlayerVolume(Player p, byte volume)
    {
        instance.playerVolume.put(p.getName(), volume);
    }

    public static byte getPlayerVolume(Player p)
    {
        Byte b = instance.playerVolume.get(p.getName());

        if (b == null)
        {
            b = 100;
            instance.playerVolume.put(p.getName(), b);
        }

        return b;
    }
}
