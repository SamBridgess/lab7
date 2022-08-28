package ilya.common.Classes;

import java.io.Serializable;
import java.util.Objects;

/**
 * describes location by x, y and z coordinates and given name
 */

public class Location implements Serializable {
    private Integer x;
    private long y;
    private double z;
    private String name;

    /**
     * creates new Location
     *
     * @param x         x coordinate (cannot be null)
     * @param y         y coordinate
     * @param z         z coordinate
     * @param name      name of location (can be null)
     */
    public Location(Integer x, long y, double z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    public Location() {
    }

    public Integer getX() {
        return x;
    }
    public void setX(Integer x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }
    public void setY(long y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }
    public void setZ(double z) {
        this.z = z;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return          returns string with information about each field
     */
    @Override
    public String toString() {
        return "Location{"
                + "x=" + x
                + ", y=" + y
                + ", z=" + z
                + ", name='" + name + '\''
                + '}';
    }

    /**
     * @param o         the object to compare with
     * @return          returns true or false based on objects equality
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return y == location.y && Double.compare(location.z, z) == 0 && Objects.equals(x, location.x) && Objects.equals(name, location.name);
    }

    /**
     * @return          returns hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y, z, name);
    }
}
