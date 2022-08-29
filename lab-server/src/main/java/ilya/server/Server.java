package ilya.server;


import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ClientMessage;
import ilya.common.Requests.ServerResponse;
import ilya.common.util.AddressValidator;
import ilya.server.Commands.*;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.SQL.QueryManager;

import java.io.*;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.sql.SQLException;
import java.util.*;

public final class Server {
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "123123";
    private static final String DB_URL = "jdbc:postgresql://127.0.0.1:5432/TestBase";

    private static Selector selector;
    private static InetSocketAddress inetSocketAddress;
    private static Set<SocketChannel> session;
    private Server() {
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, CtrlDException, WrongFileFormatException, SQLException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
            args = new String[1];
            args[0] = "5555";


            QueryManager sqlManager = new QueryManager(DB_USERNAME, DB_PASSWORD, DB_URL);
            sqlManager.CreateTable();

            if (!AddressValidator.checkPort(args)) {
                System.out.println("Please enter Port correctly!");
                return;
            }

            //todo String collectionPath = "Collection.xml";
            //todo CollectionManager manager = XmlParser.convertXmlToCollection(collectionPath);
            //CollectionManager manager = new CollectionManager(new ArrayList<>());
            SQLCollectionManager manager = new SQLCollectionManager(new ArrayList<>(), sqlManager);
            HashMap<String, Command> commands = createCommandsMap(manager);

            int port = Integer.parseInt(args[0]);
            inetSocketAddress = new InetSocketAddress(port);
            selector = Selector.open();
            session = new HashSet<>();

            serverSocketChannel.bind(inetSocketAddress);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server is working on port " + port);
            while (true) {
                selector.select();
                Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                    } else if (key.isReadable()) {
                        ClientMessage clientMessage = receive(key);
                        if (clientMessage == null) {
                            continue;
                        }

                        String username = clientMessage.getUsername();
                        String command = clientMessage.getCommand();
                        String[] arguments = clientMessage.getArgs();
                        Route route = clientMessage.getRoute();
                        boolean isFile = clientMessage.getIsFile();

                        //sqlManager.add(route);

                        ServerResponse serverResponse = commands.get(command).execute(username, arguments, route, isFile);

                        sendResponse(key, serverResponse);
                    }
                }
                //todo XmlParser.convertCollectionToXml(manager, collectionPath);
            }
        } catch (BindException e) {
            System.out.println("This port is already in use, try another!");
        }
    }
    private static void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        session.add(channel);
        System.out.println("User accepted: " + channel.socket().getRemoteSocketAddress());
    }
    private static ClientMessage receive(SelectionKey key) throws IOException, ClassNotFoundException {
        final int bufferSize = 65536;

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            session.remove(channel);
            System.out.println("Exchanged finished: " + channel.socket().getRemoteSocketAddress() + "\n");
            channel.close();
            key.cancel();
            return null;
        }
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteBuffer.array()));

        ClientMessage clientMessage = (ClientMessage) inputStream.readObject();

        byteBuffer.clear();
        System.out.println("Packet received!");
        return clientMessage;
    }
    private static void sendResponse(SelectionKey key, ServerResponse serverResponse) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(b);

        objectOutputStream.writeObject(serverResponse);

        channel.write(ByteBuffer.wrap(b.toByteArray()));
        System.out.println("Packet sent!");
    }
    private static HashMap<String, Command> createCommandsMap(SQLCollectionManager manager) {
        HashMap<String, Command> commands = new HashMap<>();
        commands.put("help", new HelpCommand());
        commands.put("info", new InfoCommand(manager));
        commands.put("show", new ShowCommand(manager));
        commands.put("add", new AddCommand(manager));
        commands.put("update", new UpdateCommand(manager));
        commands.put("remove_by_id", new RemoveByIdCommand(manager));
        commands.put("clear", new ClearCommand(manager));
        //commands.put("remove_first", new RemoveFirstCommand(manager));
        //commands.put("remove_lower", new RemoveLowerCommand(manager));
        commands.put("sort", new SortCommand(manager));
        commands.put("filter_less_than_distance", new FilterLessThanDistanceCommand(manager));
        commands.put("print_ascending", new PrintAscendingCommand(manager));
        commands.put("print_field_descending_distance", new PrintFieldDescendingDistanceCommand(manager));
        return commands;
    }
}
