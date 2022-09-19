package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.IncorrectInputException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * parent of all commands
 */
public abstract class Command {
    public Command() {
    }
    public abstract ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws IOException, WrongFileFormatException, IncorrectInputException, SQLException;
}
