import handler.CoinNotSupportedException;
import handler.ConsumerOperationException;
import handler.VendingMachineException;
import handler.VendingMachineSetupException;
import impl.VendingMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


public class VendingMachineJUnitTest {

    VendingMachine vendingMachine;

    @BeforeEach
    void setUp() {
        vendingMachine = new VendingMachine(10, Arrays.asList(0.10, 0.20, 0.50, 1.0));
    }

    @Test
    @DisplayName("Initializing VendingMachine with 0 slot parameter")
    void testInitializingVendingMachineWithZeroSlot() {
        Exception exception = assertThrows(
                VendingMachineException.class, () ->
                        new VendingMachine(0, null)
        );

        assertEquals("No. of slots not defined", exception.getMessage());
    }

    @Test
    @DisplayName("Initializing VendingMachine with null coin type parameter")
    void testInitializingVendingMachineWithNullCoinType() {
        Exception exception = assertThrows(
                VendingMachineException.class, () ->
                        new VendingMachine(10, null)
        );

        assertEquals("Coin Types are not defined", exception.getMessage());

    }

    @Test
    @DisplayName("Setting product count for a slot with no price")
    void testSettingProductForSlotWithNoPrice() {
        Exception exception = assertThrows(
                VendingMachineException.class, () ->
                        vendingMachine.setItemCountForSlot(1, 10)
        );
        assertEquals("Product Price not available for the slot", exception.getMessage());
    }

    @Test
    @DisplayName("Setting product price for invalid slot")
    void testSettingPoductPriceForInvalidSlot() {
        Exception exception = assertThrows(
                VendingMachineSetupException.class, () ->
                        vendingMachine.setPricePerItemForSlot(11, 1.30)
        );
        assertEquals("Slot not available", exception.getMessage());
    }

    @Test
    @DisplayName("Setting coin count for unsupported coin type")
    void testSettingCountForUnsupportedCoinType() {
        Exception exception = assertThrows(
                CoinNotSupportedException.class, () ->
                        vendingMachine.setCoinsCountForCoinType(2.0, 10)
        );
        assertEquals("Coin Type does not match with the available coin types",
                exception.getMessage());
    }

    @Test
    @DisplayName("Buying product from invalid slot")
    void testBuyProductFromInvalidSlot() {
        Exception exception = assertThrows(
                ConsumerOperationException.class, () ->
                        vendingMachine.buyProduct(13, Arrays.asList(0.50, 2.0, 0.10, 1.0))
        );
        assertEquals("Slot not available", exception.getMessage());
    }

    @Test
    @DisplayName("Buying product without inserting coins")
    void testBuyProductWithNoCoinsInserted() {
        Exception exception = assertThrows(
                ConsumerOperationException.class, () ->
                        vendingMachine.buyProduct(1, null)
        );
        assertEquals("No coins inserted for purchase", exception.getMessage());
    }

    @Test
    @DisplayName("Buying product by inserting unsupported coin types")
    void testBuyProductWithUnsupportedCoins() {
        Exception exception = assertThrows(
                ConsumerOperationException.class, () ->
                        vendingMachine.buyProduct(1, Arrays.asList(10.0))
        );
        assertEquals("Input coins are not supported by the machine", exception.getMessage());
    }

    @Test
    @DisplayName("Buying product which doesn't have inventory")
    void testBuySoldOutProduct() {
        Exception exception = assertThrows(
                VendingMachineException.class, () ->
                        vendingMachine.buyProduct(1, Arrays.asList(0.50, 2.0, 0.10, 1.0))
        );
        assertEquals("Product Sold Out", exception.getMessage());
    }

    @Test
    @Disabled
    @DisplayName("Buying product with insufficient coins inserted")
    void testBuyProductWithInsufficientFund() {
        vendingMachine.setPricePerItemForSlot(1, 0.30);
        vendingMachine.setItemCountForSlot(1, 2);
        Exception exception = assertThrows(
                ConsumerOperationException.class, () ->
                        vendingMachine.buyProduct(1, Arrays.asList(0.10, 0.10))
        );
        assertEquals("Insufficient fund to purchase the product", exception.getMessage());
    }

    @Test
    @DisplayName("Testing vending machine with no change state")
    void testVendingMachineNoChangeState() {
        vendingMachine.setPricePerItemForSlot(1, 0.30);
        vendingMachine.setItemCountForSlot(1, 2);
        Exception exception = assertThrows(
                VendingMachineException.class, () ->
                        vendingMachine.buyProduct(1, Arrays.asList(0.50))
        );
        assertEquals("No change available in the machine", exception.getMessage());
    }

    @Test
    @DisplayName("Buying product with proper inputs")
    void testBuyProduct() {
        try {
            vendingMachine.setPricePerItemForSlot(1, 0.30);
            vendingMachine.setItemCountForSlot(1, 2);
            vendingMachine.setCoinsCountForCoinType(0.10, 10);
            String dispenseMsg = vendingMachine.buyProduct(1, Arrays.asList(0.50));
            StringBuffer expectedDispenseMessage = new StringBuffer()
                    .append("Product Dispensed Successfully.\n")
                    .append("Please collet change : ")
                    .append("[0.1, 0.1]");

            assertEquals(expectedDispenseMessage.toString(), dispenseMsg);
        } catch (Exception e) {

        }
    }
}
