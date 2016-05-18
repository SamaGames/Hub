package net.samagames.hub.common.refresh;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CacheList extends ArrayList<JsonHub>
{
    private final Map<Integer, Integer> timeouts;

    CacheList()
    {
        this.timeouts = new HashMap<>();
    }

    @Override
    public boolean add(JsonHub jsonHub)
    {
        if (this.contains(jsonHub))
            this.remove(jsonHub);

        if (this.timeouts.containsKey(jsonHub.getHubNumber()))
            this.timeouts.remove(jsonHub.getHubNumber());

        this.timeouts.put(jsonHub.getHubNumber(), 5);

        return super.add(jsonHub);
    }

    public void update()
    {
        List<JsonHub> toRemove = new ArrayList<>();

        for (JsonHub hub : this)
        {
            if (!this.timeouts.containsKey(hub.getHubNumber()))
            {
                toRemove.add(hub);
            }
            else
            {
                int newValue = this.timeouts.get(hub.getHubNumber()) - 1;

                if (newValue <= 0)
                {
                    toRemove.add(hub);
                }
                else
                {
                    this.timeouts.remove(hub.getHubNumber());
                    this.timeouts.put(hub.getHubNumber(), newValue);
                }
            }
        }

        this.removeAll(toRemove);
    }
}
