package net.samagames.hub.games.leaderboards;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import org.bukkit.Location;

import java.util.List;

public class RotatingLeaderboard extends HubLeaderboard
{
    private List<RotatingLeaderboardFrame> frames;
    private static int currentFrame = 0;

    public RotatingLeaderboard(Hub hub, Location sign, List<HubLeaderBoardStand> stands, List<RotatingLeaderboardFrame> frames)
    {
        this.hub = hub;
        this.sign = sign;
        this.stands = stands;
        this.frames = frames;
    }

    public static void increment()
    {
        RotatingLeaderboard.currentFrame++;
    }

    @Override
    public void refresh()
    {
        super.refresh();
    }

    @Override
    protected GamesNames getGame()
    {
        return this.frames.get(RotatingLeaderboard.currentFrame % this.frames.size()).game;
    }

    @Override
    protected String getDisplayName()
    {
        return this.frames.get(RotatingLeaderboard.currentFrame % this.frames.size()).displayName;
    }

    @Override
    protected String getStatName()
    {
        return this.frames.get(RotatingLeaderboard.currentFrame % this.frames.size()).statName;
    }

    @Override
    protected String getGameName()
    {
        return this.frames.get(RotatingLeaderboard.currentFrame % this.frames.size()).gameName;
    }

    public static class RotatingLeaderboardFrame
    {
        private GamesNames game;
        private String gameName;
        private String displayName;
        private String statName;

        public RotatingLeaderboardFrame(GamesNames game, String gameName, String displayName, String statName)
        {
            this.game = game;
            this.gameName = gameName;
            this.displayName = displayName;
            this.statName = statName;
        }
    }
}
