package ilya.client.ClientUtil;

public class CommandRules {
    private Integer numberOfArgs;
    private boolean requiresNewRoute = false;

    public CommandRules(Integer numberOfArgs) {
        this.numberOfArgs = numberOfArgs;
    }
    public CommandRules(Integer numberOfArgs, boolean requiresNewRoute) {
        this.numberOfArgs = numberOfArgs;
        this.requiresNewRoute = requiresNewRoute;
    }

    public Integer getNumberOfArgs() {
        return numberOfArgs;
    }
    public boolean getRequiresNewRoute() {
        return requiresNewRoute;
    }
}
