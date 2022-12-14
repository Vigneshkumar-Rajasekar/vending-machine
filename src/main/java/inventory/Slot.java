package inventory;

import handler.VendingMachineException;
import handler.VendingMachineSetupException;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Slot class handles the slot and product inventory and its functionality
 *
 * @author Vigneshkumar
 */
public class Slot {

    private final Map<Integer, Double> slotProductPriceMap;
    private final Map<Integer, Integer> slotProductCountMap;
    Queue<Integer> availableSlots;

    /**
     * Constructor with slot count argument to initialize the object
     */
    public Slot(int slotCount) {
        availableSlots = new LinkedList<>();
        for (int slotNo = 1; slotNo <= slotCount; slotNo++) {
            availableSlots.add(slotNo);
        }
        slotProductCountMap = new ConcurrentHashMap<>();
        slotProductPriceMap = new ConcurrentHashMap<>();
    }

    public void setSlotProductPrice(int slotNo, double price) {
        if (isSlotAvailable(slotNo)) {
            slotProductPriceMap.put(slotNo, price);
        } else {
            throw new VendingMachineSetupException("Slot not available");
        }
    }

    public double getSlotProductPrice(int slotNo) {
        return slotProductPriceMap.get(slotNo);
    }

    public void setSlotProductCount(int slotNo, int count) throws VendingMachineException {
        if (!isSlotAvailable(slotNo))
            throw new VendingMachineException("Slot not available");
        if (!isProductPriceExistsForSlot(slotNo))
            throw new VendingMachineException("Product Price not available for the slot");

        slotProductCountMap.put(slotNo, count);
    }

    public void dispenseProductFromSlot(int slotNo) {
        slotProductCountMap.put(slotNo, getSlotProductCount(slotNo) - 1);
    }

    public int getSlotProductCount(int slotNo) {
        return slotProductCountMap.getOrDefault(slotNo, 0);
    }

    private boolean isProductPriceExistsForSlot(int slotNo) {
        return slotProductPriceMap.containsKey(slotNo);
    }

    public boolean isSlotAvailable(int slotNo) {
        return availableSlots.contains(slotNo);
    }
}
