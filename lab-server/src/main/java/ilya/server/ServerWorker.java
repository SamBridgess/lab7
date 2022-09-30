package ilya.server;

import ilya.common.Classes.Route;
import ilya.common.Exceptions.IncorrectInputException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ClientMessage;
import ilya.common.Requests.ServerResponse;
import ilya.server.Commands.*;
import ilya.server.SQL.QueryManager;
import ilya.server.SQL.SQLCollectionManager;
import ilya.server.ServerUtil.PasswordManager;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ServerWorker {
    private QueryManager queryManager;
    private SQLCollectionManager manager;
    private HashMap<String, Command> commands;
    private Selector selector;
    private ExecutorService readRequestsPool = Executors.newCachedThreadPool();
    private ExecutorService requestHandlerPool = new ForkJoinPool();
    private ExecutorService responseSendPool = Executors.newCachedThreadPool();
    private int port;
    public ServerWorker(QueryManager queryManager, SQLCollectionManager manager, int port) throws IOException {
        this.selector = Selector.open();
        this.queryManager = queryManager;
        this.manager = manager;
        this.commands = createCommandsMap(manager);
        this.port = port;
    }

    public void run(ServerSocketChannel serverSocketChannel) throws SQLException, IOException {
        queryManager.createUsersTable();
        queryManager.createDataTable();
        manager.loadFromTable();
        System.out.println("Data from table:");
        for (Route route : manager.getCollection()) {
            System.out.println(route);
        }
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
    }
    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(selector, SelectionKey.OP_READ);
        System.out.println("User accepted: " + channel.socket().getRemoteSocketAddress());
    }
    private ClientMessage receive(SelectionKey key) throws IOException, ClassNotFoundException {
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

    private ServerResponse handleRequest(ClientMessage clientMessage) throws SQLException, NoSuchAlgorithmException, WrongFileFormatException, IncorrectInputException, IOException {
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
    private void sendResponse(SelectionKey key, ServerResponse serverResponse) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();

        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(b);

        objectOutputStream.writeObject(serverResponse);

        channel.write(ByteBuffer.wrap(b.toByteArray()));
        System.out.println("Packet sent!");
    }
    private HashMap<String, Command> createCommandsMap(SQLCollectionManager collectionManager) {
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
