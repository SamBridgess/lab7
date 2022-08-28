package ilya.client.ClientUtil;

import java.util.Arrays;

public final class CommandSplitter {
    private CommandSplitter() {

    }
    public static String getCommand(String line) {
        String[] words = line.trim().split("\\s++");
        return words[0];
    }
    public static String[] getArgs(String line) {
        String[] words = line.trim().split("\\s++");
        return Arrays.copyOfRange(words, 1, words.length);
    }
}
