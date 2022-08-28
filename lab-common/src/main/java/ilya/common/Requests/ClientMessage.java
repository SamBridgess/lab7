package ilya.common.Requests;

import ilya.common.Classes.Route;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String command;
    private String[] args;
    private Route route;
    private boolean isFile;
    private String username;

    public ClientMessage(String username, String command, String[] args, Route route, boolean isFile) {
        this.username = username;
        this.command = command;
        this.args = args;
        this.route = route;
        this.isFile = isFile;
    }
    public String getUsername() {
        return username;
    }
    public String getCommand() {
        return command;
    }
    public String[] getArgs() {
        return args;
    }
    public Route getRoute() {
        return route;
    }
    public boolean getIsFile() {
        return isFile;
    }

}
