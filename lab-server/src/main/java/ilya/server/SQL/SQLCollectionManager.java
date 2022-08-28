package ilya.server.SQL;

import ilya.common.Classes.Route;
import ilya.server.ServerUtil.RouteComparator;

import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

public class SQLCollectionManager {
    private ArrayList<Route> collection = new ArrayList<>();
    private final Date collectionCreationDate = new Date();
    private QueryManager queryManager;

    /**
     * creates new CollectionManager
     *
     * @param collection        collection to work with
     */
    public SQLCollectionManager(ArrayList<Route> collection, QueryManager queryManager) {
        this.collection = collection;
        this.queryManager = queryManager;
    }
    public SQLCollectionManager() {
    }
    /**
     * adds new element to collection
     *
     * @param route  element to add
     */
    public void addNewElement(Route route, String username) throws SQLException {
        queryManager.add(route, username);
        collection.add(route);
    }


    /**
     * clears collection
     */
    public void clearCollection(String username) throws SQLException {
        queryManager.clearOwned(username);
        collection.clear();
    }

    /**
     * @return      returns collection
     */
    public ArrayList<Route> getCollection() throws SQLException {
        return collection;
    }

    /**
     * removes route by index
     *
     * @param idx   index of an element to remove
     * @return      returns if an element was removed successfully
     */
    public boolean removeRouteByIdx(int idx) {
        if (!collection.isEmpty()) {
            collection.remove(idx);
            return true;
        } else {
            return false;
        }
    }

    /**
     * sorts collection
     */
    public void sortCollection() {
        Collections.sort(collection);
    }

    /**
     * removes route by ID
     *
     * @param id    ID of an element to remove
     * @return      returns whether an element was removed successfully
     */
    public boolean removeRouteByID(Long id) {
        return collection.removeIf(x -> x.getId() == id);
    }

    /**
     * removes all objects from collection that are lower than the passed one
     * @param route     passed object
     */
    public void removeAllLower(Route route) {
        collection.removeIf(value -> new RouteComparator().isLower(value, route));
    }

    /**
     * updates element in collection
     *
     * @param route  element to update
     */
    public boolean update(Route route) {
        if (!collection.removeIf(x -> Objects.equals(x.getId(), route.getId()))) {
            return false;
        }
        collection.add(route);
        return true;
    }

    /**
     * @return          returns a list with all distances, sorted descending
     */
    public List<Float> getDistanceList() {
        List<Float> distanceList = collection.stream().map(Route::getDistance).sorted().collect(Collectors.toList());
        Collections.reverse(distanceList);
        return distanceList;
    }

    /**
     * @return          returns a list with all routes with distance less than given
     */
    public List<Route> getLessThanDistance(float distance) {
        return collection.stream().filter(x -> x.getDistance() < distance).collect(Collectors.toList());
    }

    /**
     * @return      returns information about collection
     */
    public String getInfo() {
        return "Collection class: " + collection.getClass() + "\n"
                + "Creation date: " + collectionCreationDate + "\n"
                + "Collection size: " + collection.size();
    }
}
