package net.mcskirmish.region;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SerializableAs("CylindricalRegion")
public class CylindricalRegion extends Region implements ConfigurationSerializable {

    private String worldName;
    private RegionPoint center;
    private int amplitude;
    private int radius;
    private List<RegionPoint> points;

    public CylindricalRegion(Point center, int height, int radius) {
        this.worldName = center.getWorldName();
        this.center = new RegionPoint(this, center);
        this.amplitude = height / 2;
        this.radius = radius;
    }

    public CylindricalRegion(Location lowerCenter, int height, int radius) {
        this.center = new RegionPoint(this, lowerCenter);
        this.worldName = lowerCenter.getWorld().getName();
        this.amplitude = height / 2;
        this.radius = radius;
    }

    public CylindricalRegion(Point pointOne, Point pointTwo) {
        this.worldName = pointOne.getWorldName();
        this.center = getCenterFromPoints(pointOne, pointTwo);
        this.amplitude = (int) Math.abs(Math.round(pointOne.getY() - pointTwo.getY())) / 2;
        this.radius = (int) Math
                .round(center.distance(new Point(getWorldName(), pointOne.getX(), center.getY(), pointOne.getZ())));
    }

    public CylindricalRegion(Location firstCorner, Location secondCorner) {
        this(new Point(firstCorner), new Point(secondCorner));
    }

    public CylindricalRegion(Map<String, Object> serializedCylindricalRegion) {
        this.worldName = (String) serializedCylindricalRegion.get("world");
        this.center = new RegionPoint((Map<String, Object>) serializedCylindricalRegion.get("center"));
        this.center.setRegion(this);
        this.amplitude = (int) serializedCylindricalRegion.get("amplitude");
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

    /**
     * Generates Points needed for {@link Region#contains(Region)}
     */
    @Override
    public List<RegionPoint> getPoints() {
        if (points == null) {
            points = new ArrayList<RegionPoint>();
            double x = center.getX();
            double y = center.getY();
            double z = center.getZ();
            points.add(new RegionPoint(this, x - radius, y - amplitude, z));
            points.add(new RegionPoint(this, x, y - amplitude, z - radius));
            points.add(new RegionPoint(this, x + radius, y - amplitude, z));
            points.add(new RegionPoint(this, x, y - amplitude, z + radius));
            points.add(new RegionPoint(this, x - radius, y + amplitude, z));
            points.add(new RegionPoint(this, x, y + amplitude, z - radius));
            points.add(new RegionPoint(this, x + radius, y + amplitude, z));
            points.add(new RegionPoint(this, x, y + amplitude, z + radius));
        }
        return points;
    }

    @Override
    public double distance(Point point) {
        // Disregard y-Coordinate
        Point yCenter = new Point(getWorldName(), getCenter().getX(), point.getY(), getCenter().getZ());
        // Calculate Directional Vector inside Cylinder
        Point direction = point.sub(yCenter).div(point.distance(yCenter)).mul(radius);
        // Calculate outer Point of Cylinder
        Point outer = new Point(getWorldName(), yCenter.getX() + direction.getX(),
                getNearestCoordinate(point.getY(), getCenter().getY() - amplitude, getCenter().getY() + amplitude),
                yCenter.getZ() + direction.getZ());
        // Calculate distance from nearest outer Point
        return outer.distance(point);
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
        // Disregard y-Coordinate
        Point yCenter = new Point(getWorldName(), getCenter().getX(), region.getCenter().getY(), getCenter().getZ());
        // Calculate Directional Vector inside Cylinder
        Point direction = region.getCenter().sub(yCenter).div(region.getCenter().distance(yCenter)).mul(radius);
        // Calculate outer Point of Cylinder
        Point outer = new Point(getWorldName(), yCenter.getX() + direction.getX(),
                getNearestCoordinate(region.getCenter().getY(), getCenter().getY() - amplitude,
                        getCenter().getY() + amplitude),
                yCenter.getZ() + direction.getZ());
        return region.distance(outer);
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
        double y = point.getY();
        if (y < getCenter().getY() - amplitude || y > getCenter().getY() + amplitude) {
            return false;
        }
        return new Point(getWorldName(), getCenter().getX(), y, getCenter().getZ()).distance(point) < radius;
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
        serializedObject.put("amplitude", amplitude);
        return serializedObject;
    }

}
