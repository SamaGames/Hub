package net.samagames.hub.common.refresh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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
