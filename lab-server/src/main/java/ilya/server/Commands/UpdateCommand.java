package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.ServerUtil.CollectionManager;


/**
 * update command
 */
public class UpdateCommand extends Command {
    private final CollectionManager manager;
    public UpdateCommand(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     * @throws WrongFileFormatException
     * @throws CtrlDException
     */
    @Override
    public ServerResponse execute(String[] args, Route route, boolean isFile) throws WrongFileFormatException, CtrlDException {
        route.setId(Long.valueOf(args[0]));
        if (manager.update(route)) {
            return new ServerResponse("Updated element successfully",  false);
        } else {
            return new ServerResponse("There is no object with such ID in the collection!",  isFile);
        }
    }
}
