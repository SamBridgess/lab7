package ilya.client.ClientUtil;


import ilya.client.IO.IOManager;
import ilya.client.IO.RouteFieldInputManager;
import ilya.common.Classes.Coordinates;
import ilya.common.Classes.Location;
import ilya.common.Classes.Route;
import ilya.common.Exceptions.IncorrectInputException;
import ilya.common.Exceptions.WrongFileFormatException;


/**
 * creates new Routes
 */
public final class RouteCreator {
    private final float noMax = Float.MAX_VALUE;
    private final float noMin = Float.MIN_VALUE;

    private final long yBiggerThan = -673;
    private final float distanceBiggerThan = 1;
    private final IOManager io;


    /**
     * creates new RouteCreator
     *
     * @param io        passed IOManager
     */
    public RouteCreator(IOManager io) {
        this.io = io;
    }

    /**
     * creates new Route
     *
     * @return          returns created Route
     * @throws WrongFileFormatException
     * @throws IncorrectInputException
     */
    public Route createRoute(String owner) throws WrongFileFormatException, IncorrectInputException {
        RouteFieldInputManager inputManager = new RouteFieldInputManager();

        String name = inputManager.validatedLoopInput("Enter route name: ", io, String.class, new RouteValueValidator(new Number[]{0}, noMin, noMax, false));
        int x = inputManager.validatedLoopInput("Enter X coordinate: ", io, Integer.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
        long y = inputManager.validatedLoopInput("Enter Y coordinate: ", io, Long.class, new RouteValueValidator(new Number[]{}, yBiggerThan, noMax, false));
        Coordinates coordinates = new Coordinates(x, y);

        Location from;
        if (!inputManager.askIfNull("From Location", io)) {
            Integer fromX = inputManager.validatedLoopInput("Enter from X coordinate: ", io, Integer.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
            long fromY = inputManager.validatedLoopInput("Enter from Y coordinate: ", io, Long.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
            double fromZ = inputManager.validatedLoopInput("Enter from Z coordinate: ", io, Double.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
            String fromName = inputManager.validatedLoopInput("Enter from name: ", io, String.class, new RouteValueValidator(new Number[]{}, noMin, noMax, true));
            from = new Location(fromX, fromY, fromZ, fromName);
        } else {
            from = null;
        }


        Integer toX = inputManager.validatedLoopInput("Enter to X coordinate: ", io, Integer.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
        long toY = inputManager.validatedLoopInput("Enter to Y coordinate: ", io, Long.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
        double toZ = inputManager.validatedLoopInput("Enter to Z coordinate: ", io, Double.class, new RouteValueValidator(new Number[]{}, noMin, noMax, false));
        String toName = inputManager.validatedLoopInput("Enter to name: ", io, String.class, new RouteValueValidator(new Number[]{}, noMin, noMax, true));
        Location to = new Location(toX, toY, toZ, toName);

        float distance = inputManager.validatedLoopInput("Enter distance: ", io, Float.class, new RouteValueValidator(new Number[]{}, distanceBiggerThan, noMax, false));

        return new Route(0L, name, coordinates, from, to, distance, owner);
    }

}
