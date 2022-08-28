package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;

import java.sql.SQLException;

/**
 * sort command
 */
public class SortCommand extends Command {
    private final SQLCollectionManager manager;
    public SortCommand(SQLCollectionManager manager) {
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
        manager.sortCollection();
        return new ServerResponse("Collection sorted successfully",  false);
    }
}
