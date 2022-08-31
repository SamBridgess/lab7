package ilya.server.SQL;

import ilya.common.Classes.Route;
import ilya.server.ServerUtil.ElementUpdateMessage;
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

    public void loadFromTable() throws SQLException {
        collection = queryManager.loadFromTable();
    }

    /**
     * adds new element to collection
     *
     * @param route  element to add
     */
    public void addNewElement(Route route, String username) throws SQLException {
        route.setId(queryManager.add(route, username));
        collection.add(route);
    }


    /**
     * clears collection
     */
    public void clearCollection(String username) throws SQLException {
        List<Route> routesToRemove = collection.stream()
                .filter(r -> Objects.equals(r.getOwner(), username))
                .collect(Collectors.toList());
        List<Long> idToRemove = routesToRemove.stream().map(Route::getId).collect(Collectors.toList());
        queryManager.removeByIdList(idToRemove);

        collection.remove(routesToRemove);
    }

    /**
     * removes all objects from collection that are lower than the passed one
     * @param route     passed object
     */
    public void removeAllLower(Route route, String username) throws SQLException {
        List<Route> routesToRemove = collection.stream()
                .filter(r -> new RouteComparator().isLower(r, route))
                .filter(r -> Objects.equals(r.getOwner(), username))
                .collect(Collectors.toList());
        List<Long> idToRemove = routesToRemove.stream().map(Route::getId).collect(Collectors.toList());
        queryManager.removeByIdList(idToRemove);
        collection.remove(routesToRemove);
    }
    /**
     * @return      returns collection
     */
    public ArrayList<Route> getCollection() {
        return collection;
    }

    /**
     * removes route by ID
     *
     * @param id    ID of an element to remove
     * @return      returns whether an element was removed successfully
     */
    public ElementUpdateMessage removeRouteByID(Long id, String username) throws SQLException {
        ElementUpdateMessage elementUpdateMessage = queryManager.removeById(id, username);
        if(elementUpdateMessage.getWasUpdated()){
            collection.removeIf(x -> x.getId() == id);
        }
        return elementUpdateMessage;
    }


    /**
     * updates element in collection
     *
     * @param route  element to update
     */
    public ElementUpdateMessage update(long id, String username, Route route) throws SQLException {
        ElementUpdateMessage elementUpdateMessage = queryManager.update(id, username, route);
        if(elementUpdateMessage.getWasUpdated()) {
            route.setId(elementUpdateMessage.getId());
            collection.removeIf(x -> Objects.equals(x.getId(), route.getId()));
            collection.add(route);
        }
        return elementUpdateMessage;
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
