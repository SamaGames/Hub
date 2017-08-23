package net.samagames.hub.common.refresh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
class CacheList extends ConcurrentHashMap<Integer, JsonHub>
{
    private final Map<Integer, Integer> timeouts;

    CacheList()
    {
        this.timeouts = new ConcurrentHashMap<>();
    }

    @Override
    public JsonHub put(Integer hubNumber, JsonHub jsonHub)
    {
        if (this.containsKey(hubNumber))
            this.remove(hubNumber);

        if (this.timeouts.containsKey(hubNumber))
            this.timeouts.remove(hubNumber);

        this.timeouts.put(hubNumber, 10);

        return super.put(hubNumber, jsonHub);
    }

    public void update()
    {
        List<Integer> toRemove = new ArrayList<>();

        for (int hubNumber : this.keySet())
        {
            if (!this.timeouts.containsKey(hubNumber))
            {
                toRemove.add(hubNumber);
            }
            else
            {
                int newValue = this.timeouts.get(hubNumber) - 1;

                if (newValue <= 0)
                {
                    this.timeouts.remove(hubNumber);
                    toRemove.add(hubNumber);
                }
                else
                {
                    this.timeouts.remove(hubNumber);
                    this.timeouts.put(hubNumber, newValue);
                }
            }
        }

        toRemove.forEach(this::remove);
    }
}
