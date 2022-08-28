package ilya.server.ServerUtil;


import ilya.common.Classes.Route;

import java.util.*;
import java.util.stream.Collectors;


public class CollectionManager {
    private ArrayList<Route> collection = new ArrayList<>();
    private final Date collectionCreationDate = new Date();
    private Long maxId = 1L;

    /**
     * creates new CollectionManager
     *
     * @param collection        collection to work with
     */
    public CollectionManager(ArrayList<Route> collection) {
        this.collection = collection;
    }
    public CollectionManager() {
    }

    /**
     * assigns new ID with auto-incrementation
     *
     * @return          returns newly generated ID
     */
    public Long assignNewId() {
        for (Route r : collection) {
            maxId = Math.max(r.getId(), maxId);
        }
        return ++maxId;
    }


    /**
     * clears collection
     */
    public void clearCollection() {
        collection.clear();
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
     * adds new element to collection
     *
     * @param route  element to add
     */
    public void addNewElement(Route route) {
        collection.add(route);
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
     * @return      returns collection
     */
    public ArrayList<Route> getCollection() {
        return collection;
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
