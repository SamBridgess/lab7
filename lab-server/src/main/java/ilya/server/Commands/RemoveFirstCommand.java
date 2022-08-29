package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;
import ilya.server.ServerUtil.ElementUpdateMessage;

import java.sql.SQLException;

/**
 * remove_first command
 */
public class RemoveFirstCommand extends Command {
    private final SQLCollectionManager manager;
    public RemoveFirstCommand(SQLCollectionManager manager) {
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
        ElementUpdateMessage elementUpdateMessage = manager.removeFirst(username);
        if(elementUpdateMessage.getWasUpdated()) {
            return new ServerResponse(elementUpdateMessage.getMessage(), false);
        }
        return new ServerResponse(elementUpdateMessage.getMessage(), isFile);
    }
}
