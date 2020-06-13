package net.mcskirmish.region;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("SphericalRegion")
public class SphericalRegion extends Region implements ConfigurationSerializable {

    private String worldName;
    private RegionPoint center;
    private int radius;
    private List<RegionPoint> points;

    public SphericalRegion(Point center, int radius) {
        this.worldName = center.getWorldName();
        this.center = new RegionPoint(this, center);
        this.radius = radius;
    }

    public SphericalRegion(Location lowerCenter, int radius) {
        this.center = new RegionPoint(this, lowerCenter);
        this.worldName = lowerCenter.getWorld().getName();
        this.radius = radius;
    }

    public SphericalRegion(Point pointOne, Point pointTwo) {
        this.worldName = pointOne.getWorldName();
        this.center = getCenterFromPoints(pointOne, pointTwo);
        this.radius = (int) Math.abs(Math.round(pointOne.getY() - pointTwo.getY())) / 2;
    }

    public SphericalRegion(Location firstCorner, Location secondCorner) {
        this(new Point(firstCorner), new Point(secondCorner));
    }

    public SphericalRegion(Map<String, Object> serializedCylindricalRegion) {
        this.worldName = (String) serializedCylindricalRegion.get("world");
        this.center = new RegionPoint((Map<String, Object>) serializedCylindricalRegion.get("center"));
        this.center.setRegion(this);
        this.radius = (int) serializedCylindricalRegion.get("radius");
    }

    private RegionPoint getCenterFromPoints(Point pointOne, Point pointTwo) {
        int x = Math.round((int) (pointOne.getX() + pointTwo.getX()) / 2);
        int y = Math.round((int) (pointOne.getY() + pointTwo.getY()) / 2);
        int z = Math.round((int) (pointOne.getZ() + pointTwo.getZ()) / 2);
        return new RegionPoint(this, x, y, z);
    }

    @Override
    public String getWorldName() {
        return worldName;
    }

    /** Generates Points needed for {@link Region#contains(Region)} */
    @Override
    public List<RegionPoint> getPoints() {
        if (points == null) {
            points = new ArrayList<RegionPoint>();
            double x = center.getX();
            double y = center.getY();
            double z = center.getZ();
            points.add(new RegionPoint(this, x - radius, y - radius, z - radius));
            points.add(new RegionPoint(this, x - radius, y - radius, z + radius));
            points.add(new RegionPoint(this, x - radius, y + radius, z - radius));
            points.add(new RegionPoint(this, x - radius, y + radius, z + radius));
            points.add(new RegionPoint(this, x + radius, y - radius, z - radius));
            points.add(new RegionPoint(this, x + radius, y - radius, z + radius));
            points.add(new RegionPoint(this, x + radius, y + radius, z - radius));
            points.add(new RegionPoint(this, x + radius, y + radius, z + radius));
        }
        return points;
    }

    @Override
    public double distance(Point point) {
        return getCenter().distance(point) - radius;
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

    @Override
    public double distance(Region region) {
        double dist = region.getCenter().distance(getCenter());
        Point nearestPoint = region.getCenter().sub(getCenter()).div(dist).mul(radius);
        return region.distance(nearestPoint);
    }

    @Override
    public boolean contains(Point... points) {
        for (Point point : points) {
            if (!contains(point))
                return false;
        }
        return true;
    }

    public boolean contains(Point point) {
        if (!point.getWorldName().equals(getWorldName())) {
            return false;
        }
        return getCenter().distance(point) <= radius;
    }

    @Override
    public boolean contains(Location location) {
        return contains(new Point(location));
    }

    @Override
    public RegionPoint getCenter() {
        return center;
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> serializedObject = new HashMap<String, Object>();
        serializedObject.put("world", getWorldName());
        serializedObject.put("center", getCenter().serialize());
        serializedObject.put("radius", radius);
        return serializedObject;
    }

}
