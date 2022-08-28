package ilya.common.Requests;

import java.io.Serializable;

public class ServerResponse implements Serializable {
    private String responseMessage;
    private boolean wrongScriptFormat;
    public ServerResponse(String responseMessage, boolean wrongScriptFormat) {
        this.responseMessage = responseMessage;
        this.wrongScriptFormat = wrongScriptFormat;
    }
    public String getResponseMessage() {
        return responseMessage;
    }
    public boolean getWrongScriptFormat() {
        return wrongScriptFormat;
    }
}
