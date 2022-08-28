package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.ServerUtil.CollectionManager;

/**
 * print_field_descending_distance command
 */
public class PrintFieldDescendingDistanceCommand extends Command {
    private final CollectionManager manager;
    public PrintFieldDescendingDistanceCommand(CollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     */
    @Override
    public ServerResponse execute(String[] args, Route route, boolean isFile) {
        String message = "";
        for (Float f : manager.getDistanceList()) {
            message = message + f + '\n';
        }
        return new ServerResponse(message, false);
    }
}
