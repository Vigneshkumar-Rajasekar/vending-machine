package service;

public interface VendingMachineInterface {

    void setItemCountForSlot(int slotNo, int count) throws IllegalStateException;

    int getItemCountForSlot(int slotNo);

    void setPricePerItemForSlot(int slotNo, double price) throws IllegalArgumentException;

    double getPricePerItemForSlot(int slotNo);

    void setCoinsCountForCoinType(double coinType, int count) throws IllegalArgumentException;

    int getCoinsCountForCoinType(double coinType);
}
