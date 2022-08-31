package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;

import ilya.server.ServerUtil.ElementUpdateMessage;

import java.sql.SQLException;

/**
 * remove_by_id command
 */
public class RemoveByIdCommand extends Command {
    private final SQLCollectionManager manager;
    public RemoveByIdCommand(SQLCollectionManager manager) {
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
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws WrongFileFormatException, SQLException {
        ElementUpdateMessage elementUpdateMessage = manager.removeRouteByID(Long.parseLong(args[0]), username);
        if (elementUpdateMessage.getWasUpdated()) {
            return new ServerResponse(elementUpdateMessage.getMessage(),  false);
        }
        return new ServerResponse(elementUpdateMessage.getMessage(), isFile);

    }
}
