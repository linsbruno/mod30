package exception;

public class ElementTypeNotKnowException extends Exception {

    private static final long serialVersionUID = -2268140970978666251L;

    public ElementTypeNotKnowException(String msg) {
        this(msg, null);
    }

    public ElementTypeNotKnowException(String msg, Throwable e) {
        super(msg, e);
    }
}
