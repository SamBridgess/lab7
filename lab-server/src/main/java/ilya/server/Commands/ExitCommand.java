package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Requests.ServerResponse;

import java.io.IOException;

/**
 * exit command
 */
public class ExitCommand extends Command {
    public ExitCommand() {
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     * @throws IOException
     */
    @Override
    public ServerResponse execute(String[] args, Route route, boolean isFile) throws IOException {
        return new ServerResponse("Exiting...",  false);
    }
}

