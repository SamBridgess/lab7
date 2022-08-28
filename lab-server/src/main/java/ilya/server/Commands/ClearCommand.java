package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;

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
    public ServerResponse execute(String[] args, Route route, boolean isFile) throws SQLException {
        manager.clearCollection();

        return new ServerResponse("Collection cleared successfully",  false);
    }
}
