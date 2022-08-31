package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;

import java.sql.SQLException;


/**
 * remove_lower command
 */
public class RemoveLowerCommand extends Command {
    private final SQLCollectionManager manager;
    public RemoveLowerCommand(SQLCollectionManager manager) {
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
        manager.removeAllLower(route, username);
        return new ServerResponse("Elements removed successfully", false);
    }
}
