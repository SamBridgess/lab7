package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.ServerUtil.CollectionManager;

/**
 * remove_first command
 */
public class RemoveFirstCommand extends Command {
    private final CollectionManager manager;
    public RemoveFirstCommand(CollectionManager manager) {
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
        if (manager.removeRouteByIdx(0)) {
            return new ServerResponse("First element removed succesfully", false);
        } else {
            return new ServerResponse("Collection is empty, no first element",  isFile);
        }
    }
}
