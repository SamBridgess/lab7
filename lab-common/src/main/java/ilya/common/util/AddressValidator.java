package ilya.common.util;

import java.net.InetSocketAddress;

public final class AddressValidator {
    private static final int MIN_PORT = 1;
    private static final int MAX_PORT = 65535;
    private AddressValidator() {

    }
    private static boolean checkPort(String str) {
        try {
            final int port = Integer.parseInt(str);
            return port >= MIN_PORT && port <= MAX_PORT;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean checkPort(String[] args) {
        if (args.length != 1) {
            return false;
        }
        try {
            final int port = Integer.parseInt(args[0]);
            return port >= MIN_PORT && port <= MAX_PORT;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean checkAddress(String[] args) {
        if (args.length != 2) {
            return false;
        } else {
            String host = args[0];
            String port = args[1];
            if (checkPort(port)) {
                InetSocketAddress address = new InetSocketAddress(host, Integer.parseInt(port));
                return !address.isUnresolved();
            } else {
                return false;
            }
        }
    }
}
