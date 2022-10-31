package inventory;

import handler.CoinNotSupportedException;
import handler.VendingMachineException;

import java.util.*;

public class Coins {
    private final Map<Double, Integer> coinsCountMap;

    public Coins(List<Double> coins) {
        coinsCountMap = new TreeMap<>(Collections.reverseOrder());
        for (double coin : coins) {
            coinsCountMap.put(coin, 0);
        }
    }

    public int getCoinsCountForCoinType(double coinType) {
        return coinsCountMap.get(coinType);
    }

    public void setCoinsCountInMap(double coinType, int count) {
        if (isCoinTypeAvailable(coinType)) {
            coinsCountMap.put(coinType, count);
        } else {
            throw new CoinNotSupportedException("Coin Type does not match with the available coin types");
        }
    }

    public void addCoinsToMachine(List<Double> inputCoins) {
        for(double coin : inputCoins){
            addCoin(coin);
        }
    }

    public void removeCoinsFromMachine(List<Double> inputCoins) {
        for(double coin : inputCoins){
            removeCoin(coin);
        }
    }

    public void addCoin(double coinType){
        if (isCoinTypeAvailable(coinType)) {
            coinsCountMap.put(coinType, getCoinsCountForCoinType(coinType)+1);
        }
    }

    public void removeCoin(double coinType){
        if (isCoinTypeAvailable(coinType)) {
            coinsCountMap.put(coinType, getCoinsCountForCoinType(coinType)-1);
        }
    }

    public boolean isCoinTypeAvailable(double coin) {
        return coinsCountMap.containsKey(coin);
    }

    public boolean validateInputCoins(List<Double> coins) {
        boolean validateCoin = false;
        for (double coin : coins) {
            isCoinTypeAvailable(coin);
        }
        return validateCoin;
    }

    public boolean isCoinsChangeAvailable(double changeAmount) {
        for (Map.Entry<Double, Integer> coinEntry : coinsCountMap.entrySet()) {
            for(int i = 0; i < coinEntry.getValue(); i++){
                if(changeAmount > coinEntry.getKey()
                        && getCoinsCountForCoinType(coinEntry.getKey()) > 0){
                    changeAmount -= coinEntry.getKey();
                } else {
                    break;
                }
            }
            if(changeAmount == 0)
                break;
        }
        return (changeAmount > 0) ? false : true;
    }

    public synchronized List<Double> getCoinsChange(double changeAmount) {
        List<Double> changeCoins = new ArrayList<>();
        for (Map.Entry<Double, Integer> coinEntry : coinsCountMap.entrySet()) {
            for(int i = 0; i < coinEntry.getValue(); i++){
                if(changeAmount >= coinEntry.getKey()
                        && getCoinsCountForCoinType(coinEntry.getKey()) > 0){
                    changeCoins.add(coinEntry.getKey());
                    //coinsCountMap.put(coinEntry.getKey(), getCoinsCountForCoinType(coinEntry.getKey())-1);
                    removeCoin(coinEntry.getKey());
                    changeAmount -= coinEntry.getKey();
                } else { break; }
            }
            if(changeAmount == 0)
                break;
        }
        return changeCoins;
    }

    public List<Map<Double, Integer>> getCoinInventory() {
        return Arrays.asList(coinsCountMap);
    }
}
