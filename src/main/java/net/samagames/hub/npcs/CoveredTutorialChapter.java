package net.samagames.hub.npcs;

import net.samagames.tools.tutorials.TutorialChapter;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
public class CoveredTutorialChapter extends TutorialChapter
{
    private final int coverId;

    public CoveredTutorialChapter(Location location, String title, List<Pair<String, Long>> content, int coverId)
    {
        super(location, title, content, true);

        this.coverId = coverId;
    }

    @Override
    public void teleport(Player player)
    {
        player.playEffect(player.getLocation(), Effect.RECORD_PLAY, this.coverId);

        super.teleport(player);
    }
}
