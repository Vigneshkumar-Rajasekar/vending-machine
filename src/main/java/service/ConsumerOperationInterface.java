package service;

import java.util.List;

public interface ConsumerOperationInterface {

    double getProductPrice(int slotNo) throws IllegalStateException;

    void buyProduct(int slotNo, List<Double> inputCoins) throws Exception;
}
