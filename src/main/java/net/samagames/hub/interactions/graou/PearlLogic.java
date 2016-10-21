package net.samagames.hub.interactions.graou;

import net.samagames.hub.cosmetics.common.CosmeticRarity;

import java.util.Random;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 * <p>
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
public class PearlLogic
{
    private enum Star
    {
        ONE(80, 20, 0),
        TWO(65, 30, 5),
        THREE(32, 45, 20),
        FOUR(5, 30, 50),
        FIVE(0, 20, 60)
        ;

        private final int commonPercentage;
        private final int rarePercentage;
        private final int epicPercentage;

        Star(int commonPercentage, int rarePercentage, int epicPercentage)
        {
            this.commonPercentage = commonPercentage;
            this.rarePercentage = rarePercentage;
            this.epicPercentage = epicPercentage;
        }

        /**
         * Return a randomized cosmetic rarity calculated with
         * the percentages.
         *
         * Note: We don't need a legendary percentage because of
         * the total of the percentage have to be equals to 100
         * (obvious).
         *
         * @return A randomized cosmetic rarity
         */
        public CosmeticRarity getRandomizedRarity()
        {
            int random = new Random().nextInt(100);

            if (random <= this.commonPercentage)
                return CosmeticRarity.COMMON;
            else if (random <= this.commonPercentage + this.rarePercentage)
                return CosmeticRarity.RARE;
            else if (random <= this.commonPercentage + this.rarePercentage + this.epicPercentage)
                return CosmeticRarity.EPIC;
            else
                return CosmeticRarity.LEGENDARY;
        }

        public static Star getByCount(int stars)
        {
            if (stars == 1)
                return ONE;
            else if (stars == 2)
                return TWO;
            else if (stars == 3)
                return THREE;
            else if (stars == 4)
                return FOUR;
            else
                return FIVE;
        }
    }
}
