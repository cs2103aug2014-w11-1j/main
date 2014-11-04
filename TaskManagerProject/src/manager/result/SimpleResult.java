package manager.result;

/**
 * A simple result that carries no additional information.<br>
 * Only carries the result type.
 * 
 */
//@author A0065475X
public class SimpleResult implements Result {
    private final Type type;
    
    public SimpleResult(Type type) {
        this.type = type;
    }

    @Override
    public Type getType() {
        return type;
    }
}
