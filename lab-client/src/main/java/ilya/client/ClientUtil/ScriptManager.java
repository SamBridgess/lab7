package ilya.client.ClientUtil;

import ilya.client.IO.IOManager;
import ilya.common.Exceptions.WrongFileFormatException;

import java.io.File;
import java.io.IOException;

public final class ScriptManager {
    private ScriptManager() {

    }
    public static void addScript(IOManager io, String path) throws WrongFileFormatException, IOException {
        File file = new File(path);
        if (!file.exists()) {
            io.println("File \"" + file.getName() + "\" not found!");
        } else {
            if (!io.addFileToFileStack(file)) {
                io.println("Recursion detected!");
                throw new WrongFileFormatException();
            }
            io.fillExecutionStack(file);
            io.println("Starting to execute " + file.getName());
        }
    }
}
