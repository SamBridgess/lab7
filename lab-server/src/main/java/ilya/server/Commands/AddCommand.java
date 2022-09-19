package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.IncorrectInputException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;

import java.sql.SQLException;

/**
 * add command
 */
public class AddCommand extends Command {
    private final SQLCollectionManager manager;
    public AddCommand(SQLCollectionManager manager) {
        this.manager = manager;
    }
    /**
     * executes command with arguments
     * @param args      arguments
     * @param route     potential new element
     * @throws WrongFileFormatException
     * @throws IncorrectInputException
     */
    @Override
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws WrongFileFormatException, IncorrectInputException, SQLException {
        manager.addNewElement(route, username);

        return new ServerResponse("Element added successfully",  false);
    }
}
