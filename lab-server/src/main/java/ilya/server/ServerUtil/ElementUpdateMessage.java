package ilya.server.ServerUtil;

public class ElementUpdateMessage {
    private String message;
    private boolean wasUpdated;
    private Long id = 0L;

    public ElementUpdateMessage(String message, boolean wasUpdated) {
        this.message = message;
        this.wasUpdated = wasUpdated;
    }
    public ElementUpdateMessage(String message, boolean wasUpdated, Long id) {
        this.message = message;
        this.wasUpdated = wasUpdated;
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public boolean getWasUpdated() {
        return wasUpdated;
    }
    public Long getId() {
        return id;
    }
}
