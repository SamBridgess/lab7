package ilya.common.Requests;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private String responseMessage;
    private boolean operationSucces;
    private boolean wrongScriptFormat;
    public ServerResponse(String responseMessage, boolean wrongScriptFormat) {
        this.responseMessage = responseMessage;
        this.wrongScriptFormat = wrongScriptFormat;
    }
    public ServerResponse(boolean operationSucces) {
        this.operationSucces = operationSucces;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public boolean getOperationSucces() {
        return operationSucces;
    }
    public boolean getWrongScriptFormat() {
        return wrongScriptFormat;
    }
}
