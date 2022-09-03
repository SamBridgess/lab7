package ilya.common.Requests;

import ilya.common.Classes.Route;

import java.io.Serializable;

public class ClientMessage implements Serializable {
    private String username;
    private String password;
    private boolean isRegister = false;
    private boolean isLogin = false;
    private String command;
    private String[] args;
    private Route route;
    private boolean isFile;



    public ClientMessage(String username, String password,  String command, String[] args, Route route, boolean isFile) {
        this.username = username;
        this.password = password;
        this.command = command;
        this.args = args;
        this.route = route;
        this.isFile = isFile;
    }
    public ClientMessage(String username, String password, boolean isRegister, boolean isLogin) {
        this.username = username;
        this.password = password;
        this.isRegister = isRegister;
        this.isLogin = isLogin;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public boolean getIsRegister() {
        return isRegister;
    }
    public boolean getIsLogin() {
        return isLogin;
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
