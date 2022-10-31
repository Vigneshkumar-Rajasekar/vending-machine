package impl;

import handler.ConsumerOperationException;
import handler.VendingMachineException;
import inventory.Coins;
import inventory.Slot;
import service.ConsumerOperationInterface;
import service.VendingMachineInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class VendingMachine
        implements VendingMachineInterface, ConsumerOperationInterface {

    private final Coins coins;
    private final Slot slot;
    private final List<Double> invaidCoins;
    private final List<Double> coinChangeList;
    private volatile double insertedAmount;
    private ConcurrentHashMap<Double, Integer> coinTypesCountHash;
    private HashMap<Integer, Double> pricePerProductHash;


    public VendingMachine(int noOfSlots, List<Double> acceptedCoinTypes) {
        slot = new Slot(noOfSlots);
        coins = new Coins(acceptedCoinTypes);
        invaidCoins = new ArrayList<>();
        coinChangeList = new ArrayList<>();
    }

    @Override
    public void setItemCountForSlot(int slotNo, int count) throws IllegalStateException {
        slot.setSlotProductCount(slotNo, count);
    }

    @Override
    public int getItemCountForSlot(int slotNo) {
        return slot.getSlotProductCount(slotNo);
    }

    @Override
    public void setPricePerItemForSlot(int slotNo, double price) throws IllegalArgumentException {
        slot.setSlotProductPrice(slotNo, price);
    }

    @Override
    public double getPricePerItemForSlot(int slotNo) {
        return slot.getSlotProductPrice(slotNo);
    }

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

    @Override
    public void buyProduct(int slotNo, List<Double> inputCoins) throws Exception {
        try{
            showPurchasePreview(slotNo,inputCoins);
            coins.addCoinsToMachine(inputCoins);
            if (validateConsumerInput(slotNo, inputCoins)
                    && validateProductAndPrice(slotNo)) {
                List<Double> changeCoins = dispenseProduct(slotNo);
                printPurchaseDetails(changeCoins);
            }
        } catch(Exception e){
            coins.removeCoinsFromMachine(inputCoins);
            e.printStackTrace();
        }
    }

    private boolean validateConsumerInput(int slotNo, List<Double> inputCoins) throws Exception {
        if (!slot.isSlotAvailable(slotNo))
            throw new ConsumerOperationException("Slot not available");
        if (inputCoins == null || inputCoins.size() == 0)
            throw new ConsumerOperationException("No coins inserted for purchase");
        if (!checkInputCoins(inputCoins))
            throw new ConsumerOperationException("Input coins are not supported by the machine");
        return true;
    }

    private boolean validateProductAndPrice(int slotNo) {
        if (getItemCountForSlot(slotNo) == 0)
            throw new VendingMachineException("Product Sold Out");
        if (insertedAmount < getProductPrice(slotNo))
            throw new ConsumerOperationException("Insufficient fund to purchase the product");
        double balanceAmount = insertedAmount - getProductPrice(slotNo);
        if (coins.isCoinsChangeAvailable(balanceAmount))
            throw new VendingMachineException("No change available in the machine");
        return true;
    }

    private List<Double> dispenseProduct(int slotNo) {
        slot.dispenseProductFromSlot(slotNo);
        double balanceAmount = insertedAmount - getProductPrice(slotNo);
        return coins.getCoinsChange(balanceAmount);
    }

    private boolean checkInputCoins(List<Double> inputCoins) {
        boolean validCoin = false;
        for (double coin : inputCoins) {
            if (coins.isCoinTypeAvailable(coin)) {
                insertedAmount += coin;
                if (!validCoin)
                    validCoin = true;
            } else {
                invaidCoins.add(coin);
            }
        }
        return validCoin;
    }

    private void showPurchasePreview(int slotNo, List<Double> coins) {
        System.out.println(" *******************************");
        System.out.println("        PURCHASE PREVIEW        ");
        System.out.println(" *******************************");
        System.out.println("Selected Product Slot : "+slotNo);
        System.out.println("Price of the Product : "+getProductPrice(slotNo));
        System.out.println("Coins Inserted : "+ Arrays.toString(coins.toArray()));
        System.out.println("MACHINE WALLET : "+ this.coins.getCoinInventory());
    }

    private void printPurchaseDetails(List<Double> coins) {
        System.out.println(" *******************************");
        System.out.println("        PURCHASE DETAILS        ");
        System.out.println(" *******************************");
        System.out.println("Please collect the product from the vending machine");
        System.out.println("Please collect the change : "+ Arrays.toString(coins.toArray()));
        if(invaidCoins.size() > 0){
            System.out.println("Ejecting Invalid Coins : "+ Arrays.toString(invaidCoins.toArray()));
        }
        System.out.println("MACHINE WALLET : "+ this.coins.getCoinInventory());
    }
}
