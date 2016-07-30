package net.samagames.hub.games.leaderboards;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import org.bukkit.Location;

import java.util.List;

public class RotatingLeaderboard extends HubLeaderboard
{
    private List<RotatingLeaderboardFrame> frames;
    private int currentFrame;

    public RotatingLeaderboard(Hub hub, Location sign, List<HubLeaderBoardStand> stands, List<RotatingLeaderboardFrame> frames)
    {
        this.hub = hub;
        this.sign = sign;
        this.stands = stands;
        this.frames = frames;
        this.currentFrame = 0;
    }

    @Override
    public void refresh()
    {
        this.currentFrame++;
        this.currentFrame %= this.frames.size();
        super.refresh();
    }

    @Override
    protected GamesNames getGame()
    {
        return this.frames.get(this.currentFrame).game;
    }

    @Override
    protected String getDisplayName()
    {
        return this.frames.get(this.currentFrame).displayName;
    }

    @Override
    protected String getStatName()
    {
        return this.frames.get(this.currentFrame).statName;
    }

    @Override
    protected String getGameName()
    {
        return this.frames.get(this.currentFrame).gameName;
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
