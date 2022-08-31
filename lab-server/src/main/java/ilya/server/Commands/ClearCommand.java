package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;


import java.sql.SQLException;

/**
 * clear command
 */
public class ClearCommand extends Command {
    private final SQLCollectionManager manager;
    public ClearCommand(SQLCollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     */
    @Override
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws SQLException {
        manager.clearCollection(username);

        return new ServerResponse("All owned element removed",  false);
    }
}
