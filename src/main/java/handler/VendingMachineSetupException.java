package handler;

/**
 * VendingMachineSetupException class is used to throw vending machine inventory setup exception.
 * Internally it extends IllegalArgumentException
 *
 * @author Vigneshkumar
 */
public class VendingMachineSetupException extends IllegalArgumentException {
    public VendingMachineSetupException(String exMsg) {
        super(exMsg);
    }
}