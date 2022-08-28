package ilya.server.Commands;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ServerResponse;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.CollectionManager;

import java.sql.SQLException;


/**
 * update command
 */
public class UpdateCommand extends Command {
    private final SQLCollectionManager manager;
    public UpdateCommand(SQLCollectionManager manager) {
        this.manager = manager;
    }

    /**
     * executes command with arguments
     *
     * @param args      arguments
     * @param route     potential new element
     * @throws WrongFileFormatException
     * @throws CtrlDException
     */
    @Override
    public ServerResponse execute(String username, String[] args, Route route, boolean isFile) throws WrongFileFormatException, CtrlDException, SQLException {
        if(manager.update(Long.parseLong(args[0]), username, route)) {
            return new ServerResponse("Updated element successfully",  false);
        } else {
            return new ServerResponse("There was a problem with updating the object",  isFile);
        }
        //todo нужно добавить конкретную причину ошибки(такой id отстувует \ нет прав)

       /* route.setId(Long.valueOf(args[0]));
        if (manager.update(route)) {
            return new ServerResponse("Updated element successfully",  false);
        } else {
            return new ServerResponse("There is no object with such ID in the collection!",  isFile);
        }*/
    }
}
