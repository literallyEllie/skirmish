package net.mcskirmish.region.events;

import net.mcskirmish.region.Region;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.EventException;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

import java.util.function.Consumer;

/**
 * @author zorro
 * <p>
 * Executes Lambda Functions as Listeners, which were registered by
 * {@link Region#registerListener(RegionListenerType, org.bukkit.plugin.Plugin, Consumer)}
 */
public class RegionEventExecutor<EventType extends Event> implements EventExecutor {

    private RegionListenerType<EventType> rlt;
    private Region region;
    private Consumer<EventType> listener;

    public RegionEventExecutor(RegionListenerType<EventType> rlt, Region region, Consumer<EventType> listener) {
        this.rlt = rlt;
        this.region = region;
        this.listener = listener;
    }

    @Override
    public void execute(Listener arg0, Event arg1) throws EventException {
        Location eventLocation = rlt.convertLocation((EventType) arg1);
        if (rlt.checkCondition((EventType) arg1, region) && region.contains(eventLocation)) {
            listener.accept((EventType) arg1);
        }
    }

}
