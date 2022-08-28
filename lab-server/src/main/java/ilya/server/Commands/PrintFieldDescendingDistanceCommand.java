package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;

/**
 * print_field_descending_distance command
 */
public class PrintFieldDescendingDistanceCommand extends Command {
    private final SQLCollectionManager manager;
    public PrintFieldDescendingDistanceCommand(SQLCollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     */
    @Override
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) {
        String message = "";
        for (Float f : manager.getDistanceList()) {
            message = message + f + '\n';
        }
        return new ServerResponse(message, false);
    }
}
