package ilya.server.ServerUtil;


import ilya.common.Classes.Route;

import java.util.Comparator;

/**
 * Route comparator
 */
public class RouteComparator implements Comparator<Route> {
    /**
     * @param r1        first argument
     * @param r2        second argument
     * @return          returns whether the first argument is lower than the second
     */
    public boolean isLower(Route r1, Route r2) {
        return compare(r1, r2) < 0;
    }


    /**
     * @param r1        first argument
     * @param r2        second argument
     * @return          returns which argument is lower
     */
    @Override
    public int compare(Route r1, Route r2) {
        return Float.compare(r1.getDistance(), r2.getDistance()) == 0 ? (r1.getName().compareTo(r2.getName()) == 0 ? (Long.compare(r1.getId(), r2.getId())) : r1.getName().compareTo(r2.getName())) : Float.compare(r1.getDistance(), r2.getDistance());
    }
}


