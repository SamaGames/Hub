package net.samagames.hub.npcs;

import net.samagames.tools.tutorials.Tutorial;
import org.bukkit.entity.Player;

import java.util.function.BiConsumer;

public class NPCTutorial extends Tutorial
{
    private BiConsumer<Player, Boolean> consumer;

    @Override
    protected void onTutorialEnds(Player player, boolean interrupted)
    {
        this.consumer.accept(player, interrupted);
    }

    public void onTutorialEnds(BiConsumer<Player, Boolean> consumer)
    {
        this.consumer = consumer;
    }
}
