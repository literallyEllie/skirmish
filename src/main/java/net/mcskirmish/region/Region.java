package net.mcskirmish.region;

import net.mcskirmish.region.events.RegionEventExecutor;
import net.mcskirmish.region.events.RegionListenerType;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Region {

    private String regionName;

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public World getWorld() {
        return Bukkit.getWorld(getWorldName());
    }

    public abstract String getWorldName();

    public abstract List<RegionPoint> getPoints();

    public abstract double distance(Point point);

    public double distance(Location location) {
        return distance(new Point(location));
    }

    public abstract double distance(Region region);

    public abstract boolean contains(Point... points);

    public abstract boolean contains(Location location);

    public boolean contains(Region otherRegion) {
        return contains(otherRegion.getPoints().toArray(new Point[]{}));
    }

    public boolean isPlayerInside(Player player) {
        return contains(player.getLocation());
    }

    public abstract RegionPoint getCenter();

    /**
     * Registers a custom Region Listener with {@link RegionEventExecutor} as
     * EventExecutor
     *
     * @param rlt Type of Region Listener (e.g.
     *            {@linkplain RegionListenerType#ENTER_REGION})
     */
    public <EventType extends Event> void registerListener(RegionListenerType<EventType> rlt, Plugin plugin,
                                                           Consumer<EventType> listener) {
        Bukkit
                .getPluginManager()
                .registerEvent(rlt.getEventClass(), null, EventPriority.NORMAL,
                        new RegionEventExecutor<EventType>(rlt, this, listener), plugin);
    }

    /**
     * Force serializability for all types of regions.
     */
    public abstract Map<String, Object> serialize();

}

class RegionPoint extends Point implements ConfigurationSerializable {

    private Region parentRegion;

    public RegionPoint(Map<String, Object> serializedPoint) {
        super((String) null, (int) serializedPoint.get("x"), (int) serializedPoint.get("y"),
                (int) serializedPoint.get("z"));
    }

    public RegionPoint(Region parentRegion, Location loc) {
        this(parentRegion, loc.getX(), loc.getY(), loc.getZ());
        Validate
                .isTrue(loc.getWorld().getName().equals(getWorldName()),
                        "RegionPoint is not part of parent regions World");
    }

    public RegionPoint(Region parentRegion, double x, double y, double z) {
        super((String) null, x, y, z);
        setRegion(parentRegion);
    }

    public RegionPoint(Region parentRegion, Point point) {
        this(parentRegion, point.getX(), point.getY(), point.getZ());
        Validate
                .isTrue(point.getWorldName().equals(parentRegion.getWorldName()),
                        "RegionPoint is not part of parent regions World");
    }

    public Region getRegion() {
        return parentRegion;
    }

    protected void setRegion(Region region) {
        this.parentRegion = region;
    }

    @Override
    public String getWorldName() {
        return parentRegion.getWorldName();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<String, Object>();
        serialized.put("world", getWorldName());
        serialized.put("x", getX());
        serialized.put("y", getY());
        serialized.put("z", getZ());
        return serialized;
    }

}