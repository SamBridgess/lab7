package ilya.server.ServerUtil;

import ilya.server.SQL.QueryManager;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public final class PasswordManager {
    private PasswordManager() {
    }
    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] digest = sha256.digest(password.getBytes());
        StringBuilder sb = new StringBuilder();

        for (byte b : digest) {
            sb.append(String.format("%02x", b & 0xff));
        }
        return sb.toString();
    }
    public static boolean registerUser(String username, String password, QueryManager queryManager) throws NoSuchAlgorithmException, SQLException {
        String hash = getHash(password);
        return queryManager.register(username, hash);
    }
    public static boolean login(String username, String password, QueryManager queryManager) throws NoSuchAlgorithmException, SQLException {
        String hash = getHash(password);
        return queryManager.login(username, hash);
    }
}
