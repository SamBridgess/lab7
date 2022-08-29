package ilya.server.ServerUtil;

public class ElementUpdateMessage {
    private String message;
    private boolean wasUpdated;
    public ElementUpdateMessage(String message, boolean wasUpdated) {
        this.message = message;
        this.wasUpdated = wasUpdated;
    }
    public String getMessage() {
        return message;
    }
    public boolean getWasUpdated() {
        return wasUpdated;
    }
}
