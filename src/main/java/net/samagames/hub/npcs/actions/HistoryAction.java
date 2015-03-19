package net.samagames.hub.npcs.actions;

import net.md_5.bungee.api.ChatColor;
import net.samagames.hub.Hub;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class HistoryAction extends AbstractNPCAction
{
    protected Object[][] history;

    public HistoryAction()
    {
        this.history = new Object[][] {};
    }

    @Override
    public void execute(Player player)
    {
        this.startTalk(player);
        this.doHistory(player);
    }

    protected void doHistory(final Player player)
    {
        final Object[][] text = this.history;

        Bukkit.getScheduler().runTaskAsynchronously(Hub.getInstance(), new Runnable()
        {
            int j = 0;

            @Override
            public void run()
            {
                for (int i = 0; i < text.length; i++)
                {
                    j++;

                    String message = (String) text[i][0];
                    int time = (int) text[i][1];
                    final String finalMessage = ChatColor.DARK_AQUA + "[" + ChatColor.AQUA + (i + 1) + "/" + text.length + ChatColor.DARK_AQUA + "] " + ChatColor.RESET + message;

                    player.sendMessage(finalMessage);
                    historyMessage(player, i);

                    try
                    {
                        Thread.sleep(time * 1000);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    if (j == text.length)
                        historyCallback(player);
                }
            }
        });
    }

    protected void historyMessage(Player player, int messagePos) {}
    protected void historyCallback(Player player)
    {
        this.stopTalk(player);
    }
}
