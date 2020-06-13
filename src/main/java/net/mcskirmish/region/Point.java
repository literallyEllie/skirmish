package net.mcskirmish.region;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

public class Point implements ConfigurationSerializable {

    private String world;
    private final double x, y, z;

    public Point(Map<String, Object> serializedPoint) {
        world = (String) serializedPoint.get("world");
        x = (double) serializedPoint.get("x");
        y = (double) serializedPoint.get("y");
        z = (double) serializedPoint.get("z");
    }

    public Point(Location loc) {
        this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
    }

    public Point(World world, double x, double y, double z) {
        this(world.getName(), x, y, z);
    }

    public Point(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public String getWorldName() {
        return world;
    }

    public World getWorld() {
        return Bukkit.getWorld(getWorldName());
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public Location toLocation() {
        return new Location(getWorld(), getX(), getY(), getZ());
    }

    /**
     * Calculates the distance of both Points to each other
     * 
     * @exception IllegalArgumentException If otherPoint is outside of this Point's
     *                                     world.
     * @param otherPoint
     * @return
     */
    public double distance(Point otherPoint) {
        if (!otherPoint.getWorldName().equals(getWorldName())) {
            throw new IllegalArgumentException("Both Points are in different Worlds");
        }
        Point difference = sub(otherPoint);
        return Math
                .sqrt(Math.pow(difference.getX(), 2) + Math.pow(difference.getY(), 2)
                        + Math.pow(difference.getZ(), 2));
    }

    public Point sub(Point otherPoint) {
        if (!otherPoint.getWorldName().equals(getWorldName())) {
            throw new IllegalArgumentException("Both Points are in different Worlds");
        }
        return new Point(getWorldName(), getX() - otherPoint.getX(), getY() - otherPoint.getY(),
                getZ() - otherPoint.getZ());
    }

    public boolean isPositive() {
        return getX() >= 0 && getY() >= 0 && getZ() >= 0;
    }

    public boolean isNegative() {
        return getX() <= 0 && getY() <= 0 && getZ() <= 0;
    }

    public Point add(Point otherPoint) {
        if (!otherPoint.getWorldName().equals(getWorldName())) {
            throw new IllegalArgumentException("Both Points are in different Worlds");
        }
        return new Point(getWorldName(), getX() + otherPoint.getX(), getY() + otherPoint.getY(),
                getZ() + otherPoint.getZ());
    }

    public Point mul(double mul) {
        return new Point(getWorldName(), getX() * mul, getY() * mul, getZ() * mul);
    }

    public Point div(double div) {
        return new Point(getWorldName(), getX() / div, getY() / div, getZ() / div);
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
