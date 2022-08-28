package ilya.common.Classes;

import java.io.Serializable;
import java.util.Objects;

/**
 * describes route coordinates by x and y coordinates
 */
public class Coordinates implements Serializable {
    private int x;
    private long y;

    /**
     * creates new Coordinates
     *
     * @param x         x coordinate
     * @param y         y coordinate (must be greater than -673)
     */
    public Coordinates(int x, long y) {
        this.x = x;
        this.y = y;
    }
    public Coordinates() {
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }
    public void setY(long y) {
        this.y = y;
    }

    /**
     * @return          returns string with information about each field
     */
    @Override
    public String toString() {
        return "Coordinates{"
                + "x=" + x
                + ", y=" + y
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
        Coordinates that = (Coordinates) o;
        return x == that.x && y == that.y;
    }
    /**
     * @return          returns hashcode
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
