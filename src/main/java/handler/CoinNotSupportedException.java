package handler;

public class CoinNotSupportedException extends IllegalArgumentException {
    public CoinNotSupportedException(String exMsg){
        super(exMsg);
    }
}
