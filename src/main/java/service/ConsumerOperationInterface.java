package service;

import java.util.List;

/**
 * ConsumerOperationInterface interface has consumer operation method definitions
 *
 * @author Vigneshkumar
 */
public interface ConsumerOperationInterface {

    double getProductPrice(int slotNo) throws IllegalStateException;

    String buyProduct(int slotNo, List<Double> inputCoins) throws Exception;
}
