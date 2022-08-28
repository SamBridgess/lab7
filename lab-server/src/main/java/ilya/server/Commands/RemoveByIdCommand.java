package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.ServerUtil.CollectionManager;

/**
 * remove_by_id command
 */
public class RemoveByIdCommand extends Command {
    private final CollectionManager manager;
    public RemoveByIdCommand(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     * @throws WrongFileFormatException
     */
    @Override
    public ServerResponse execute(String[] args, Route route, boolean isFile) throws WrongFileFormatException {
        if (manager.removeRouteByID(Long.parseLong(args[0]))) {
            return new ServerResponse("Element removed successfully",  false);
        } else {
            return new ServerResponse("There is no element with such ID in collection", isFile);
        }


    }
}
