package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * print_ascending command
 */
public class PrintAscendingCommand extends Command {
    private final SQLCollectionManager manager;
    public PrintAscendingCommand(SQLCollectionManager manager) {
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
        ArrayList<Route> listCopy = new ArrayList<>(manager.getCollection());
        Collections.sort(listCopy);

        String message = "";
        for (Route r : listCopy) {
            message = message + r + '\n';
        }
        return new ServerResponse(message, false);
    }
}
