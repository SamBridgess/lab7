package ilya.client;


import ilya.client.ClientUtil.*;
import ilya.client.IO.IOManager;
import ilya.common.Classes.Route;
import ilya.common.Exceptions.CtrlDException;
import ilya.common.Exceptions.WrongFileFormatException;
import ilya.common.Requests.ClientMessage;
import ilya.common.Requests.ServerResponse;
import ilya.common.util.AddressValidator;

import java.io.*;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.HashMap;

public final class Client {
    private static String host;
    private static int port;

    private Client() {
    }
    public static void main(String[] args) throws ClassNotFoundException {
        try (IOManager io = new IOManager(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out, true))) {
            args = new String[2];
            args[0] = "localhost";
            args[1] = "5555";
            String username = "SomeUser";

            if (!AddressValidator.checkAddress(args)) {
                io.println("Please enter Host and Port correctly!");
                return;
            }
            host = args[0];
            port = Integer.parseInt(args[1]);

            HashMap<String, CommandRules> commandsInfo = createCommandsInfo();
            while (io.getContinueExecutionFlag()) {
                try {
                    if (!io.getIsFile()) {
                        io.print(">>> ");
                    }
                    String s = io.getNextLine();
                    if (s.isEmpty()) {
                        continue;
                    }
                    String command = CommandSplitter.getCommand(s);
                    String[] arguments = CommandSplitter.getArgs(s);

                    if (LineValidator.checkLine(command, arguments, commandsInfo, io)) {
                        if ("exit".equals(command)) {
                            io.println("Exiting...");
                            return;
                        }
                        if ("execute_script".equals(command)) {
                            ScriptManager.addScript(io, arguments[0]);
                        } else {
                            Route route = null;
                            if (commandsInfo.get(command).getRequiresNewRoute()) {
                                route = new RouteCreator(io).createRoute();
                            }

                            ClientMessage clientMessage = new ClientMessage(command, arguments, route, io.getIsFile());
                            ServerResponse serverResponse = sendRequest(clientMessage);
                            io.println(serverResponse.getResponseMessage());

                            if (serverResponse.getWrongScriptFormat()) {
                                throw new WrongFileFormatException();
                            }
                            while (io.isLastFileExecuted()) {
                                io.println(io.getFileStack().peek().getName() + " executed successfully");
                                io.popStacks();
                            }
                        }
                    } else {
                        io.println("Invalid arguments");
                        if (io.getIsFile()) {
                            throw new WrongFileFormatException();
                        }
                    }
                } catch (CtrlDException e) {
                    io.clearStacks();
                    io.println("ctrl + D detected! Exiting program...");
                    return;
                } catch (WrongFileFormatException e) {
                    io.clearStacks();
                    io.println("Can't execute script(s) further! Wrong file(s) format");
                } catch (ConnectException | SocketTimeoutException e) {
                    io.clearStacks();
                    io.println("Server is currently unavailable!");
                }
            }


        } catch (IOException e) {
            System.out.println("Unexpected exception!");
        }
    }

    private static ServerResponse sendRequest(ClientMessage clientMessage) throws IOException, ClassNotFoundException {
        final int timeout = 1000;
        final int bufferSize = 65536;
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeout);

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(bytes);
        objectOutputStream.writeObject(clientMessage);
        socket.getOutputStream().write(bytes.toByteArray());
        objectOutputStream.close();

        byte[] b = new byte[bufferSize];
        socket.getInputStream().read(b);
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(b));

        ServerResponse serverResponse = (ServerResponse) objectInputStream.readObject();

        objectOutputStream.close();
        socket.close();
        objectInputStream.close();
        return serverResponse;
    }
    private static HashMap<String, CommandRules> createCommandsInfo() {
        HashMap<String, CommandRules> commandsInfo = new HashMap<>();
        commandsInfo.put("help", new CommandRules(0));
        commandsInfo.put("info", new CommandRules(0));
        commandsInfo.put("show", new CommandRules(0));
        commandsInfo.put("add", new CommandRules(0, true));
        commandsInfo.put("update", new CommandRules(1, true));
        commandsInfo.put("remove_by_id", new CommandRules(1));
        commandsInfo.put("clear", new CommandRules(0));
        commandsInfo.put("save", new CommandRules(0));
        commandsInfo.put("execute_script", new CommandRules(1));
        commandsInfo.put("exit", new CommandRules(0));
        commandsInfo.put("remove_first", new CommandRules(0));
        commandsInfo.put("remove_lower", new CommandRules(0, true));
        commandsInfo.put("sort", new CommandRules(0));
        commandsInfo.put("filter_less_than_distance", new CommandRules(1));
        commandsInfo.put("print_ascending", new CommandRules(0));
        commandsInfo.put("print_field_descending_distance", new CommandRules(0));
        return commandsInfo;
    }
}
