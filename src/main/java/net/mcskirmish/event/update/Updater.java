package net.mcskirmish.event.update;

import net.mcskirmish.SkirmishPlugin;
import net.mcskirmish.event.SkirmishEvent;

public class Updater {

    /**
     * Runs every tick and sends events based on time passed between callings.
     *
     * @param plugin plugin instance
     */
    public Updater(SkirmishPlugin plugin) {
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            for (UpdateType update : UpdateType.values()) {
                if (update.hasElapsed()) {
                    SkirmishEvent.callEvent(new UpdateEvent(update));
                }
            }
        }, 0, 1);
    }

}
