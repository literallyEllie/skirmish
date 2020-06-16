package net.mcskirmish.region.events;

import net.mcskirmish.region.Region;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

import java.util.function.Function;

public final class RegionListenerType<EventType extends Event> {

    /**
     * Listeners get called upon a Player entering a specified Region.<br>
     * PlayerMoveEvents may cripple Server performance if used excessively with many
     * Players.
     */
    public static RegionListenerType<PlayerMoveEvent> ENTER_REGION =
            new RegionListenerType<PlayerMoveEvent>(PlayerMoveEvent.class, event -> event.getTo(),
                    obj -> !((Region) obj[1]).contains(((PlayerMoveEvent) obj[0]).getFrom()));
    /**
     * Listeners get called upon a Player leaving a specified Region.<br>
     * PlayerMoveEvents may cripple Server performance if used excessively with many
     * Players.
     */
    public static RegionListenerType<PlayerMoveEvent> LEAVE_REGION =
            new RegionListenerType<PlayerMoveEvent>(PlayerMoveEvent.class, event -> event.getFrom(),
                    obj -> !((Region) obj[1]).contains(((PlayerMoveEvent) obj[0]).getTo()));

    /**
     * Listeners get called upon a Player interacting with something within a
     * specified Region.
     */
    public static RegionListenerType<PlayerInteractEvent> INTERACT_REGION =
            new RegionListenerType<PlayerInteractEvent>(PlayerInteractEvent.class,
                    event -> event.getClickedBlock().getLocation());

    public static RegionListenerType<PlayerInteractEntityEvent> INTERACT_ENTITY_REGION =
            new RegionListenerType<PlayerInteractEntityEvent>(PlayerInteractEntityEvent.class,
                    event -> event.getRightClicked().getLocation());

    public static RegionListenerType<PlayerDeathEvent> PLAYER_DEATH_REGION =
            new RegionListenerType<PlayerDeathEvent>(PlayerDeathEvent.class,
                    event -> event.getEntity().getLocation());

    public static RegionListenerType<PlayerDropItemEvent> PLAYER_DROP_ITEM_REGION =
            new RegionListenerType<PlayerDropItemEvent>(PlayerDropItemEvent.class,
                    event -> event.getPlayer().getLocation());

    public static RegionListenerType<PlayerPickupItemEvent> PLAYER_PICKUP_ITEM_REGION =
            new RegionListenerType<PlayerPickupItemEvent>(PlayerPickupItemEvent.class,
                    event -> event.getPlayer().getLocation());

    public static RegionListenerType<PlayerBucketEvent> PLAYER_BUCKET_REGION =
            new RegionListenerType<PlayerBucketEvent>(PlayerBucketEvent.class,
                    event -> event.getPlayer().getLocation());

    private Class<EventType> eventClass;
    private Function<EventType, Location> locationConverter;
    private Function<Object[], Boolean> conditionCheck;

    public RegionListenerType(Class<EventType> eventClass, Function<EventType, Location> locationConverter) {
        this.eventClass = eventClass;
        this.locationConverter = locationConverter;
    }

    public RegionListenerType(Class<EventType> eventClass, Function<EventType, Location> locationConverter,
                              Function<Object[], Boolean> conditionCheck) {
        this(eventClass, locationConverter);
        this.conditionCheck = conditionCheck;
    }

    public Class<EventType> getEventClass() {
        return eventClass;
    }

    public Location convertLocation(EventType event) {
        return locationConverter.apply(event);
    }

    public boolean checkCondition(EventType event, Region region) {
        if (conditionCheck == null) {
            return true;
        }
        return conditionCheck.apply(new Object[]{event, region});
    }

}
