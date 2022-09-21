package ilya.server;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.IncorrectInputException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ClientMessage;
import ilya.common.Requests.ServerResponse;
import ilya.common.util.AddressValidator;
import ilya.server.Commands.*;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.SQL.QueryManager;
import ilya.server.ServerUtil.PasswordManager;
import org.postgresql.util.PSQLException;

import java.io.*;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
public final class Server {
    private static String bdUsername;
    private static String bdPassword;
    private static String bdUrl;

    private static HashMap<String, Command> commands;
    private static QueryManager queryManager;
    private static SQLCollectionManager manager;

    private static Selector selector;
    private static ExecutorService readRequestsPool = Executors.newCachedThreadPool();
    private static ExecutorService requestHandlerPool = new ForkJoinPool();
    private static ExecutorService responseSendPool = Executors.newCachedThreadPool();
    //private static Set<SocketChannel> session;

    private Server() {
    }
    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        try (ServerSocketChannel serverSocketChannel = ServerSocketChannel.open()) {
          /* args = new String[4];
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
            bdUsername = args[1];
            bdPassword = args[2];
            bdUrl = args[3];

            queryManager = new QueryManager(bdUsername, bdPassword, bdUrl);
            queryManager.createUsersTable();
            queryManager.createDataTable();
            manager = new SQLCollectionManager(new ArrayList<>(), queryManager);
            manager.loadFromTable();
            commands = createCommandsMap(manager);

            System.out.println("Data from table:");
            for (Route route : manager.getCollection()) {
                System.out.println(route);
            }

            int port = Integer.parseInt(args[0]);
            selector = Selector.open();

            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server is working on " + InetAddress.getLocalHost() + ": " + port);
            while (true) {
                try {
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
                            key.cancel();
                            readRequestsPool.submit(() -> {
                                try {
                                    ClientMessage clientMessage = receive(key);
                                    if (clientMessage == null) {
                                        return;
                                    }

                                    requestHandlerPool.submit(() -> {
                                        try {
                                            ServerResponse response = handleRequest(clientMessage);
                                            responseSendPool.submit(() -> {
                                                try {
                                                    sendResponse(key, response);
                                                    key.channel().close();
                                                    System.out.println("Exchange finished: " + ((SocketChannel) key.channel()).socket().getRemoteSocketAddress());
                                                } catch (IOException e) {
                                                    e.printStackTrace();
                                                }
                                            });
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                } catch (IOException | ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                } catch (StreamCorruptedException e) {
                    System.out.println("Unsupported packet received!");
                } catch (Exception e) {
                    System.out.println("Exception detected, server is still working");
                }
            }
        } catch (BindException e) {
            System.out.println("This port is already in use, try another!");
        } catch (PSQLException e) {
            System.out.println("Could not log into data base! Make sure arguments are correct");
        }
    }
    private static void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("User accepted: " + channel.socket().getRemoteSocketAddress());
    }
    private static ClientMessage receive(SelectionKey key) throws IOException, ClassNotFoundException {
        final int bufferSize = 65536;

        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(bufferSize);

        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            System.out.println("Exchange finished unexpectedly: " + channel.socket().getRemoteSocketAddress() + "\n");
            channel.close();
            key.cancel();
            return null;
        }
        byteBuffer.flip();
        ObjectInputStream inputStream = new ObjectInputStream(new ByteArrayInputStream(byteBuffer.array()));

        ClientMessage clientMessage = (ClientMessage) inputStream.readObject();

        byteBuffer.clear();
        System.out.println("Packet received!");
        return clientMessage;
    }

    private static ServerResponse handleRequest(ClientMessage clientMessage) throws SQLException, NoSuchAlgorithmException, WrongFileFormatException, IncorrectInputException, IOException {
        String username = clientMessage.getUsername();
        String password = clientMessage.getPassword();
        boolean isRegister = clientMessage.getIsRegister();
        boolean isLogin = clientMessage.getIsLogin();
        if (isRegister) {
            return new ServerResponse(PasswordManager.registerUser(username, password, queryManager));
        } else if (isLogin) {
            return new ServerResponse(PasswordManager.login(username, password, queryManager));
        }

        String command = clientMessage.getCommand();
        String[] arguments = clientMessage.getArgs();
        Route route = clientMessage.getRoute();
        boolean isFile = clientMessage.getIsFile();

        return commands.get(command).execute(username, arguments, route, isFile);
    }
    private static void sendResponse(SelectionKey key, ServerResponse serverResponse) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(b);

        objectOutputStream.writeObject(serverResponse);

        channel.write(ByteBuffer.wrap(b.toByteArray()));
        System.out.println("Packet sent!");
    }
    private static HashMap<String, Command> createCommandsMap(SQLCollectionManager collectionManager) {
        HashMap<String, Command> commandsMap = new HashMap<>();
        commandsMap.put("help", new HelpCommand());
        commandsMap.put("info", new InfoCommand(collectionManager));
        commandsMap.put("show", new ShowCommand(collectionManager));
        commandsMap.put("add", new AddCommand(collectionManager));
        commandsMap.put("update", new UpdateCommand(collectionManager));
        commandsMap.put("remove_by_id", new RemoveByIdCommand(collectionManager));
        commandsMap.put("clear", new ClearCommand(collectionManager));
        commandsMap.put("remove_lower", new RemoveLowerCommand(collectionManager));
        commandsMap.put("filter_less_than_distance", new FilterLessThanDistanceCommand(collectionManager));
        commandsMap.put("print_ascending", new PrintAscendingCommand(collectionManager));
        commandsMap.put("print_field_descending_distance", new PrintFieldDescendingDistanceCommand(collectionManager));
        return commandsMap;
    }
}
