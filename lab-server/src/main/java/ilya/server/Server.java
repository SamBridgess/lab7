package ilya.server;

import ilya.common.util.AddressValidator;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.SQL.QueryManager;
import org.postgresql.util.PSQLException;
import java.io.*;
import java.net.BindException;
import java.nio.channels.ServerSocketChannel;
import java.sql.SQLException;
import java.util.*;

public final class Server {
    private Server() {
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            /*
            args = new String[4];
            args[0] = "5555";
            args[1] = "postgres";
            args[2] = "123123";
            args[3] = "jdbc:postgresql://127.0.0.1:5432/TestBase";
            */

            if (args.length != 4) {
                System.out.println("Please enter arguments correctly!");
                return;
            }
            if (!AddressValidator.checkPort(args)) {
                System.out.println("Please enter Port correctly!");
                return;
            }

            QueryManager queryManager = new QueryManager(args[1], args[2], args[3]);
            SQLCollectionManager manager = new SQLCollectionManager(new ArrayList<>(), queryManager);
            int port = Integer.parseInt(args[0]);

            ServerWorker serverWorker = new ServerWorker(queryManager, manager, port);
            serverWorker.run(serverSocketChannel);
        } catch (BindException e) {
            System.out.println("This port is already in use, try another!");
        } catch (PSQLException e) {
            System.out.println("Could not log into data base! Make sure arguments are correct");
        }
    }
}
