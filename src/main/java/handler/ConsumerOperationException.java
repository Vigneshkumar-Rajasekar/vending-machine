package handler;

public class ConsumerOperationException extends IllegalArgumentException {
    public ConsumerOperationException(String exMsg){
        super(exMsg);
    }
}
