package ilya.server.Commands;

import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;
import ilya.server.ServerUtil.CollectionManager;

/**
 * sort command
 */
public class SortCommand extends Command {
    private final CollectionManager manager;
    public SortCommand(CollectionManager manager) {
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
        manager.sortCollection();

        return new ServerResponse("Collection sorted successfully",  false);
    }
}
