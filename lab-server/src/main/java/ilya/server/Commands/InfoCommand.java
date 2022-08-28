package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;

/**
 * info command
 */
public class InfoCommand extends Command {
    private final SQLCollectionManager manager;
    public InfoCommand(SQLCollectionManager manager) {
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
        return new ServerResponse(manager.getInfo(), false);
    }
}
