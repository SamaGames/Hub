package net.samagames.hub.utils;

import net.samagames.api.SamaGamesAPI;
import net.samagames.core.api.player.Promo;
import redis.clients.jedis.ShardedJedis;

public class PromoUtils
{
    public static Promo getCurrentPromo()
    {
        ShardedJedis jedis = SamaGamesAPI.get().getResource();
        String prom = jedis.get("coins:currentpromo");
        jedis.close();

        if (prom == null)
            return null;
        else
            return new Promo(prom);
    }
}
