package controller;

import impl.VendingMachine;

import java.util.Arrays;

public class VendingMachineSimulator {

    public void doProcess() {

       try{
           VendingMachine vendingMachine =
                   new VendingMachine(10, Arrays.asList(0.10, 0.20, 0.50, 1.0));

           vendingMachine.setPricePerItemForSlot(1, 0.30);
           vendingMachine.setItemCountForSlot(1, 5);
           vendingMachine.setCoinsCountForCoinType(0.10, 10);
           vendingMachine.buyProduct(1,Arrays.asList(0.50,2.0,0.10,1.0));
           //vendingMachine.buyProduct(1,Arrays.asList(0.10));
       } catch(Exception e) {
           e.printStackTrace();
       }
    }

    public static void main(String[] args) {
        VendingMachineSimulator simulator = new VendingMachineSimulator();
        simulator.doProcess();

    }
}
