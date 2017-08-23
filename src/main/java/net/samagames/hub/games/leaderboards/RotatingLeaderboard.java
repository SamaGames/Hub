package net.samagames.hub.games.leaderboards;

import net.samagames.api.games.GamesNames;
import net.samagames.hub.Hub;
import org.bukkit.Location;

import java.util.List;

/*
 * This file is part of Hub.
 *
 * Hub is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Hub is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Hub.  If not, see <http://www.gnu.org/licenses/>.
 */
public class RotatingLeaderboard extends HubLeaderboard
{
    private List<RotatingLeaderboardFrame> frames;
    private static int currentFrame = 0;
    private int currentFrameSave;

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
        this.currentFrameSave = RotatingLeaderboard.currentFrame;
        super.refresh();
    }

    @Override
    protected GamesNames getGame()
    {
        return this.frames.get(this.currentFrameSave % this.frames.size()).game;
    }

    @Override
    protected String getDisplayName()
    {
        return this.frames.get(this.currentFrameSave % this.frames.size()).displayName;
    }

    @Override
    protected String getStatName()
    {
        return this.frames.get(this.currentFrameSave % this.frames.size()).statName;
    }

    @Override
    protected String getGameName()
    {
        return this.frames.get(this.currentFrameSave % this.frames.size()).gameName;
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
