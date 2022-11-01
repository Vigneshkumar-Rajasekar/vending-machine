package handler;

/**
 * VendingMachineException class is used to throw vending machine state exception.
 * Internally it extends IllegalStateException
 *
 * @author Vigneshkumar
 */
public class VendingMachineException extends IllegalStateException {
    public VendingMachineException(String exMsg) {
        super(exMsg);
    }
}
