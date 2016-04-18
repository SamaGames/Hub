package net.samagames.hub.events;

import com.dsh105.echopet.compat.api.event.PetInteractEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PetListener implements Listener
{
    @EventHandler
    public void onPetInteract(PetInteractEvent event)
    {
        if (event.getPlayer().getUniqueId().equals(event.getPet().getOwnerUUID()))
            if (event.getAction() == PetInteractEvent.Action.RIGHT_CLICK && !event.getPet().isOwnerRiding())
                event.getPet().ownerRidePet(true);
    }
}
