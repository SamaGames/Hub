package net.samagames.hub.interactions.graou;

import net.samagames.api.games.pearls.Pearl;
import net.samagames.hub.Hub;
import net.samagames.hub.cosmetics.common.AbstractCosmetic;
import net.samagames.hub.cosmetics.common.CosmeticRarity;
import net.samagames.tools.ItemUtils;
import net.samagames.tools.chat.fanciful.FancyMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *                )\._.,--....,'``.
 * .b--.        /;   _.. \   _\  (`._ ,.
 * `=,-,-'~~~   `----(,_..'--(,_..'`-.;.'
 *
 * Created by Jérémy L. (BlueSlime) on 21/10/2016
 */
class PearlLogic
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

    private class CosmeticList extends ArrayList<AbstractCosmetic>
    {
        public List<AbstractCosmetic> getByRarity(CosmeticRarity cosmeticRarity)
        {
            List<AbstractCosmetic> list = new ArrayList<>();

            for (AbstractCosmetic cosmetic : this)
                if (cosmetic.getRarity() == cosmeticRarity)
                    list.add(cosmetic);

            return list;
        }
    }

    private static final ItemStack PEARL_HEAD = ItemUtils.getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvN2QxNmFlOTUxMTIwMzk0ZjM2OGYyMjUwYjdjM2FkM2ZiMTJjZWE1NWVjMWIyZGI1YTk0ZDFmYjdmZDRiNmZhIn19fQ==");

    private final Hub hub;
    private final CosmeticList cosmetics;

    PearlLogic(Hub hub)
    {
        this.hub = hub;

        this.cosmetics = new CosmeticList();
        this.cosmetics.addAll(hub.getCosmeticManager().getDisguiseManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getParticleManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getPetManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getJukeboxManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getBalloonManager().getRegistry().getElements().values());
        this.cosmetics.addAll(hub.getCosmeticManager().getGadgetManager().getRegistry().getElements().values());

        Collections.shuffle(this.cosmetics);
    }

    public AbstractCosmetic unlockRandomizedCosmetic(Player player, Pearl pearl, Location openingLocation)
    {
        Collections.shuffle(this.cosmetics);

        CosmeticRarity rarity = Star.getByCount(pearl.getStars()).getRandomizedRarity();
        List<AbstractCosmetic> cosmeticsSelected = this.cosmetics.getByRarity(rarity);

        AbstractCosmetic cosmeticSelected = cosmeticsSelected.get(new Random().nextInt(cosmeticsSelected.size()));

        new FancyMessage("\u272F ").color(ChatColor.GOLD)
                .then("Vous avez trouvé ").color(ChatColor.YELLOW)
                .then(cosmeticSelected.getIcon().getItemMeta().getDisplayName()).tooltip(cosmeticSelected.getIcon().getItemMeta().getLore())
                .then(" dans le cadeau !").color(ChatColor.YELLOW)
                .then(" \u272F").color(ChatColor.GOLD)
                .send(player);

        if (cosmeticSelected.isOwned(player))
        {
            player.sendMessage(ChatColor.GOLD + "\u272F " + ChatColor.YELLOW + "Malheureusement, vous possédiez déjà ce cosmétique..." + ChatColor.GOLD + " \u272F");
        }
        else
        {
            if (cosmeticSelected.getRarity() == CosmeticRarity.EPIC || cosmeticSelected.getRarity() == CosmeticRarity.LEGENDARY)
            {
                FancyMessage globalMessage = new FancyMessage("\u272F " + player.getName()).color(ChatColor.GOLD)
                        .then(" a trouvé ").color(ChatColor.YELLOW)
                        .then(cosmeticSelected.getIcon().getItemMeta().getDisplayName()).tooltip(cosmeticSelected.getIcon().getItemMeta().getLore())
                        .then(" dans un cadeau !").color(ChatColor.YELLOW)
                        .then(" \u272F").color(ChatColor.GOLD);

                this.hub.getServer().getOnlinePlayers().stream().filter(p -> p.getUniqueId() != player.getUniqueId()).forEach(globalMessage::send);

                if (cosmeticSelected.getRarity() == CosmeticRarity.LEGENDARY)
                {
                    openingLocation.getWorld().strikeLightningEffect(openingLocation);
                    this.hub.getServer().getOnlinePlayers().forEach(p -> p.playSound(p.getLocation(), Sound.ENTITY_ENDERDRAGON_GROWL, 1.0F, 1.0F));
                }
            }

            cosmeticSelected.buy(player, true);
        }

        return cosmeticSelected;
    }

    public ItemStack getIcon(Pearl pearl)
    {
        ItemStack stack = PEARL_HEAD.clone();

        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Perle");

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.DARK_GRAY + "Niveau " + pearl.getStars());
        lore.add("");
        lore.add(ChatColor.GRAY + "Cette perle si brillante et si");
        lore.add(ChatColor.GRAY + "mystérieuse en fait envier plus");
        lore.add(ChatColor.GRAY + "d'un. " + ChatColor.GOLD + "Graou" + ChatColor.GRAY + " en fait collection");
        lore.add(ChatColor.GRAY + "et vous l'échangera contre un");
        lore.add(ChatColor.GRAY + "cosmétique à la hauteur du niveau");
        lore.add(ChatColor.GRAY + "de cette perle.");
        lore.add("");

        if (pearl.getStars() > 3)
            lore.add(ChatColor.DARK_GRAY + "Nécéssite : " + (pearl.getStars() == 5 ? ChatColor.AQUA + "VIP" + ChatColor.LIGHT_PURPLE + "+" : ChatColor.GREEN + "VIP"));

        lore.add(ChatColor.DARK_GRAY + "Expire dans : " + ChatColor.WHITE + pearl.getExpirationInDays() + " jour" + (pearl.getExpirationInDays() > 1 ? "s" : ""));

        lore.add("");
        lore.add(ChatColor.DARK_GRAY + "\u25B6 Cliquez pour échanger");

        meta.setLore(lore);
        stack.setItemMeta(meta);

        return stack;
    }
}
