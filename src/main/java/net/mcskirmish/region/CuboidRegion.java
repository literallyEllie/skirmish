package net.mcskirmish.region;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import com.google.common.collect.Lists;

@SerializableAs("CuboidRegion")
public class CuboidRegion extends Region implements ConfigurationSerializable {

    private String worldName;
    private RegionPoint firstCorner;
    private RegionPoint secondCorner;

    public CuboidRegion(Point firstCorner, Point secondCorner) {
        Validate
                .isTrue(firstCorner.getWorldName().equals(secondCorner.getWorldName()),
                        "Both Points are in different Worlds");
        this.worldName = firstCorner.getWorldName();
        this.firstCorner = getLowerCoordinates(firstCorner, secondCorner);
        this.secondCorner = getHigherCoordinates(firstCorner, secondCorner);
    }

    public CuboidRegion(Location firstCorner, Location secondCorner) {
        this.firstCorner = new RegionPoint(this, firstCorner);
        this.secondCorner = new RegionPoint(this, secondCorner);
        this.worldName = this.firstCorner.getWorldName();
        Validate
                .isTrue(worldName.equals(this.secondCorner.getWorldName()),
                        "Both Points are in different Worlds");
    }

    public CuboidRegion(Map<String, Object> serializedCuboidRegion) {
        this.worldName = (String) serializedCuboidRegion.get("world");
        this.firstCorner = new RegionPoint((Map<String, Object>) serializedCuboidRegion.get("firstCorner"));
        this.firstCorner.setRegion(this);
        this.secondCorner = new RegionPoint((Map<String, Object>) serializedCuboidRegion.get("secondCorner"));
        this.secondCorner.setRegion(this);
    }

    private RegionPoint getLowerCoordinates(Point first, Point second) {
        double minX = Math.min(first.getX(), second.getX());
        double minY = Math.min(first.getY(), second.getY());
        double minZ = Math.min(first.getZ(), second.getZ());
        return new RegionPoint(this, minX, minY, minZ);
    }

    private RegionPoint getHigherCoordinates(Point first, Point second) {
        double maxX = Math.max(first.getX(), second.getX());
        double maxY = Math.max(first.getY(), second.getY());
        double maxZ = Math.max(first.getZ(), second.getZ());
        return new RegionPoint(this, maxX, maxY, maxZ);
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    @Override
    public List<RegionPoint> getPoints() {
        return Arrays.asList(getFirstCorner(), getSecondCorner());
    }

    public RegionPoint getFirstCorner() {
        return firstCorner;
    }

    public RegionPoint getSecondCorner() {
        return secondCorner;
    }

    @Override
    public double distance(Point point) {
        double x = getNearestCoordinate(point.getX(), getFirstCorner().getX(), getSecondCorner().getX());
        double y = getNearestCoordinate(point.getY(), getFirstCorner().getY(), getSecondCorner().getY());
        double z = getNearestCoordinate(point.getZ(), getFirstCorner().getZ(), getSecondCorner().getZ());
        return new Point(getWorldName(), x, y, z).distance(point);
    }

    private double getNearestCoordinate(double toCompare, double low, double high) {
        if (toCompare > low) {
            if (toCompare > high) {
                return high;
            } else {
                return toCompare;
            }
        } else {
            return low;
        }
    }

    /**
     * Calculates the distance between two regions by using the other Region's
     * Center. This might be inaccurate if used with unconventional Regions.
     */
    @Override
    public double distance(Region region) {
        Point center = region.getCenter();
        double x = getNearestCoordinate(center.getX(), getFirstCorner().getX(), getSecondCorner().getX());
        double y = getNearestCoordinate(center.getY(), getFirstCorner().getY(), getSecondCorner().getY());
        double z = getNearestCoordinate(center.getZ(), getFirstCorner().getZ(), getSecondCorner().getZ());
        return region.distance(new Point(getWorldName(), x, y, z));
    }

    @Override
    public boolean contains(Point... points) {
        for (Point point : points) {
            if (!contains(point)) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(Point point) {
        if (!point.getWorldName().equals(getWorldName())) {
            return false;
        }
        return getFirstCorner().sub(point).isNegative() && getSecondCorner().sub(point).isPositive();
    }

    @Override
    public boolean contains(Location location) {
        return contains(new Point(location));
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> serializedObject = new HashMap<String, Object>();
        serializedObject.put("world", getWorldName());
        serializedObject.put("firstCorner", getFirstCorner().serialize());
        serializedObject.put("secondCorner", getSecondCorner().serialize());
        return serializedObject;
    }

    @Override
    public RegionPoint getCenter() {
        double x = (getFirstCorner().getX() + getSecondCorner().getX()) / 2;
        double y = (getFirstCorner().getY() + getSecondCorner().getY()) / 2;
        double z = (getFirstCorner().getZ() + getSecondCorner().getZ()) / 2;
        return new RegionPoint(this, x, y, z);
    }

}
