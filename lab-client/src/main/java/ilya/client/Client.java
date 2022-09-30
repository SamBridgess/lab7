package ilya.client;

import ilya.client.IO.IOManager;
import ilya.common.util.AddressValidator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public final class Client {
    private Client() {
    }
    public static void main(String[] args) throws ClassNotFoundException {
        try (IOManager io = new IOManager(new BufferedReader(new InputStreamReader(System.in)), new PrintWriter(System.out, true))) {
            /*
            args = new String[2];
            args[0] = "localhost";
            args[1] = "5555";
            */
            if (!AddressValidator.checkAddress(args)) {
                io.println("Please enter Host, Port, Username and Password correctly!");
                return;
            }
            String host = args[0];
            int port = Integer.parseInt(args[1]);

            ClientWorker clientWorker = new ClientWorker(io, host, port);
            clientWorker.run();
        } catch (IOException e) {
            System.out.println("Unexpected exception!");
        }
    }
}
