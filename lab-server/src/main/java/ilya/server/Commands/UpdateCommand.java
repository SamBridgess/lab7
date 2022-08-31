package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.ElementUpdateMessage;

import java.sql.SQLException;


/**
 * update command
 */
public class UpdateCommand extends Command {
    private final SQLCollectionManager manager;
    public UpdateCommand(SQLCollectionManager manager) {
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
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws WrongFileFormatException, CtrlDException, SQLException {
        ElementUpdateMessage elementUpdateMessage = manager.update(Long.parseLong(args[0]), username, route);
        if(elementUpdateMessage.getWasUpdated()) {
            return new ServerResponse(elementUpdateMessage.getMessage(),  false);
        }
        return new ServerResponse(elementUpdateMessage.getMessage(),  isFile);

    }
}
