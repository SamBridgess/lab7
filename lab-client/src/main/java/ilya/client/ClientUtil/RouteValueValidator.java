package ilya.client.ClientUtil;

import java.math.BigDecimal;

/**
 * contains field's value restrictions
 */
public class RouteValueValidator {
    private final Number[] banned;
    private final float biggerThan;
    private final float lessThan;
    private final boolean canBeNull;

    /**
     * creates new Value validator and sets restrictions
     * @param banned        array with forbidden values
     * @param biggerThan    field must be greater than this number
     * @param lessThan      field must be lower than this number
     * @param canBeNull     defines whether a filed can be null
     */
    public RouteValueValidator(Number[] banned, float biggerThan, float lessThan, boolean canBeNull) {
        this.banned = banned;
        this.biggerThan = biggerThan;
        this.lessThan = lessThan;
        this.canBeNull = canBeNull;
    }

    /**
     * @param a             first number
     * @param b             second number
     * @return              returns the result of comparing two numbers
     */
    private int compare(Object a, Object b) {
        return new BigDecimal(a.toString()).compareTo(new BigDecimal(b.toString()));
    }

    /**
     * @param n             object to validate
     * @param clazz         the class of the object to validate
     * @param <T>
     * @return              returns whether passed object fulfils the rules
     */
    public <T> boolean validate(Object n, Class<T> clazz) {
        if (n == null) {
            return canBeNull;
        }
        if (clazz == String.class) {
            return true;
        }
        boolean isBanned = false;
        for (Object ban : banned) {
            if (compare(n, ban) == 0) {
                isBanned = true;
                break;
            }
        }
        return (!isBanned
                & (compare(n, biggerThan) == 1)
                & (compare(n, lessThan) == -1));
    }
}
