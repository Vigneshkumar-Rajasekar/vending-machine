package impl;

import handler.ConsumerOperationException;
import handler.VendingMachineException;
import inventory.Coins;
import inventory.Slot;
import service.ConsumerOperationInterface;
import service.VendingMachineInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * VendingMachine class handles the functionality of vending machine and consumer operations
 *
 * @author Vigneshkumar
 */

public class VendingMachine
        implements VendingMachineInterface, ConsumerOperationInterface {

    private final Coins coins;
    private final Slot slot;
    private List<Double> invaidCoins;
    private volatile double insertedAmount;

    /**
     * Constructor which initialize the coin and slot objects internally
     *
     * @param noOfSlots         an integer value to create slots
     * @param acceptedCoinTypes to specify the vending machine to accept a list of coin types
     */
    public VendingMachine(int noOfSlots, List<Double> acceptedCoinTypes) {
        if (noOfSlots <= 0)
            throw new VendingMachineException("No. of slots not defined");
        if (acceptedCoinTypes == null || acceptedCoinTypes.size() <= 0)
            throw new VendingMachineException("Coin Types are not defined");

        slot = new Slot(noOfSlots);
        coins = new Coins(acceptedCoinTypes);
    }

    /**
     * Overridden method to set the item count for the slot
     *
     * @param slotNo an unique identification which represents a slot location
     * @param count  allocating the number of items for the specified slot
     */
    @Override
    public void setItemCountForSlot(int slotNo, int count) throws IllegalStateException {
        slot.setSlotProductCount(slotNo, count);
    }

    @Override
    public int getItemCountForSlot(int slotNo) {
        return slot.getSlotProductCount(slotNo);
    }

    /**
     * Overridden method to set the price per item for the slot
     *
     * @param slotNo an unique identification which represents a slot location
     * @param price  allocating the price for the items in the specified slot
     */
    @Override
    public void setPricePerItemForSlot(int slotNo, double price) throws IllegalArgumentException {
        slot.setSlotProductPrice(slotNo, price);
    }

    @Override
    public double getPricePerItemForSlot(int slotNo) {
        return slot.getSlotProductPrice(slotNo);
    }

    /**
     * Overridden method to set number of coins loaded for the specified coin type
     *
     * @param coinType represents the value of the coin which is used as coinType as well
     * @param count    allocating the count of coins for the specified coin type
     */
    @Override
    public void setCoinsCountForCoinType(double coinType, int count) throws IllegalArgumentException {
        coins.setCoinsCountInMap(coinType, count);
    }

    @Override
    public int getCoinsCountForCoinType(double coinType) {
        return coins.getCoinsCountForCoinType(coinType);
    }

    @Override
    public double getProductPrice(int slotNo) throws IllegalStateException {
        return slot.getSlotProductPrice(slotNo);
    }

    /**
     * Overridden method to buy product by choosing a slot and providing coins
     *
     * @param slotNo     an unique identification which represents a slot location
     *                   from where the product need to be picked
     * @param inputCoins collection of coins inserted by the user
     */
    @Override
    public String buyProduct(int slotNo, List<Double> inputCoins) throws Exception {
        List<Double> changeCoins = null;
        try {
            if (validateConsumerInput(slotNo, inputCoins)
                    && validateProductAndPrice(slotNo)) {
                showPurchasePreview(slotNo, inputCoins);
                coins.addCoinsToMachine(inputCoins);
                changeCoins = dispenseProduct(slotNo);
                printPurchaseDetails(changeCoins);
            }
            return new StringBuffer()
                    .append("Product Dispensed Successfully.\n")
                    .append("Please collet change : ")
                    .append((changeCoins != null) ? Arrays.toString(changeCoins.toArray()) : "")
                    .toString();
        } catch (Exception e) {
            coins.removeCoinsFromMachine(inputCoins);
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Method used to validate the consumer inputs
     * Throws exception if the choosen slot is invalid
     * Throws exception if the input coins are empty
     * Throws exception if the input coins are not supported by the vending machine
     */
    private boolean validateConsumerInput(int slotNo, List<Double> inputCoins) throws Exception {
        if (!slot.isSlotAvailable(slotNo))
            throw new ConsumerOperationException("Slot not available");
        if (inputCoins == null || inputCoins.size() == 0)
            throw new ConsumerOperationException("No coins inserted for purchase");
        if (!checkInputCoins(inputCoins))
            throw new ConsumerOperationException("Input coins are not supported by the machine");
        return true;
    }

    /**
     * Method used to validate the vending machine functionality with respect to the consumer's input
     * Throws exception if the choosen product is sold out
     * Throws exception if the input fund is not sufficient for the choosen product
     * Throws exception if the vending machine doesn't have proper change to return
     */
    private boolean validateProductAndPrice(int slotNo) {
        if (getItemCountForSlot(slotNo) == 0)
            throw new VendingMachineException("Product Sold Out");
        if (insertedAmount < getProductPrice(slotNo))
            throw new ConsumerOperationException("Insufficient fund to purchase the product");
        double balanceAmount = insertedAmount - getProductPrice(slotNo);
        if (!coins.isCoinsChangeAvailable(balanceAmount))
            throw new VendingMachineException("No change available in the machine");
        return true;
    }

    /**
     * Method used to dispense a product by revising slot and coin inventory
     */
    private List<Double> dispenseProduct(int slotNo) {
        slot.dispenseProductFromSlot(slotNo);
        double balanceAmount = insertedAmount - getProductPrice(slotNo);
        return coins.getCoinsChange(balanceAmount);
    }

    /**
     * Method used to check whether the coins inserted by the user are valid
     * Provided an additional functionality to collect the invalid coins in collection
     */
    private boolean checkInputCoins(List<Double> inputCoins) {
        invaidCoins = new ArrayList<>();
        for (double coin : inputCoins) {
            if (coins.isCoinTypeAvailable(coin)) {
                insertedAmount += coin;
            } else {
                invaidCoins.add(coin);
            }
        }
        return inputCoins.size() != invaidCoins.size();
    }

    private void showPurchasePreview(int slotNo, List<Double> coins) {
        System.out.println(" *******************************");
        System.out.println("        PURCHASE PREVIEW        ");
        System.out.println(" *******************************");
        System.out.println("Selected Product Slot : " + slotNo);
        System.out.println("Price of the Product : " + getProductPrice(slotNo));
        System.out.println("Coins Inserted : " + Arrays.toString(coins.toArray()));
        System.out.println("MACHINE WALLET : " + this.coins.getCoinInventory());
    }

    private void printPurchaseDetails(List<Double> coins) {
        System.out.println(" *******************************");
        System.out.println("        PURCHASE DETAILS        ");
        System.out.println(" *******************************");
        System.out.println("Please collect the product from the vending machine");
        System.out.println("Please collect the change : " + Arrays.toString(coins.toArray()));
        if (invaidCoins.size() > 0) {
            System.out.println("Ejecting Invalid Coins : " + Arrays.toString(invaidCoins.toArray()));
        }
        System.out.println("MACHINE WALLET : " + this.coins.getCoinInventory());
    }
}
