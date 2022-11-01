package handler;

/**
 * ConsumerOperationException class is used to throw consumer operation exception.
 * Internally it extends IllegalArgumentException
 *
 * @author Vigneshkumar
 */
public class ConsumerOperationException extends IllegalArgumentException {
    public ConsumerOperationException(String exMsg) {
        super(exMsg);
    }
}
