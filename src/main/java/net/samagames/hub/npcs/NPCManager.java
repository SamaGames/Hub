package net.samagames.hub.npcs;

import com.google.gson.JsonObject;
import net.samagames.api.SamaGamesAPI;
import net.samagames.hub.Hub;
import net.samagames.hub.common.managers.AbstractManager;
import net.samagames.tools.JsonConfiguration;
import net.samagames.tools.LocationUtils;
import net.samagames.tools.npc.nms.CustomNPC;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class NPCManager extends AbstractManager
{
    private static final UUID AURELIEN_SAMA_UUID = UUID.fromString("29b2b527-1b59-45df-b7b0-d5ab20d8731a");

    private final List<CustomNPC> npcs;

    public NPCManager(Hub hub)
    {
        super(hub);

        this.npcs = new ArrayList<>();

        File configuration = this.reloadConfiguration();

        JsonConfiguration npcsConfig = new JsonConfiguration(configuration);
        JsonObject jsonRoot = npcsConfig.load();

        if (jsonRoot == null)
            return;

        CustomNPC welcomeTutorialNPC = SamaGamesAPI.get().getNPCManager().createNPC(LocationUtils.str2loc(jsonRoot.get("welcome-tutorial").getAsString()), AURELIEN_SAMA_UUID, new String[] {
                ChatColor.GOLD + "" + ChatColor.BOLD + "Tutoriel de Bienvenue",
                ChatColor.YELLOW + "" + ChatColor.BOLD + "CLIC DROIT"
        }).setCallback(new WelcomeTutorialNPCAction(hub));

        this.npcs.add(welcomeTutorialNPC);
        this.log(Level.INFO, "Registered 'Welcome tutorial' NPC!");
    }

    @Override
    public void onDisable()
    {
        this.npcs.forEach(npc -> SamaGamesAPI.get().getNPCManager().removeNPC(npc.getName()));
        this.npcs.clear();
    }

    @Override
    public void onLogin(Player player) { /** Not needed **/ }

    @Override
    public void onLogout(Player player) { /** Not needed **/ }

    private File reloadConfiguration()
    {
        File configuration = new File(this.hub.getDataFolder(), "npcs.json");

        if (!configuration.exists())
        {
            try
            {
                configuration.createNewFile();

                PrintWriter writer = new PrintWriter(configuration);
                writer.println("{}");
                writer.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        return configuration;
    }
}
