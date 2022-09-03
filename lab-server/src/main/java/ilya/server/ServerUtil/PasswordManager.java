package ilya.server.ServerUtil;

import ilya.server.SQL.QueryManager;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class PasswordManager {
    private static String getHash(String password) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] bytes = sha1.digest(password.getBytes());
        BigInteger bi = new BigInteger(1, bytes);
        return String.format("%0" + (bytes.length << 1) + "X", bi);
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
