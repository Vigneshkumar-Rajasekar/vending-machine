package handler;

/**
 * CoinNotSupportedException class is used to throw coin not supported exception.
 * Internally it extends IllegalArgumentException
 *
 * @author Vigneshkumar
 */
public class CoinNotSupportedException extends IllegalArgumentException {
    public CoinNotSupportedException(String exMsg) {
        super(exMsg);
    }
}
