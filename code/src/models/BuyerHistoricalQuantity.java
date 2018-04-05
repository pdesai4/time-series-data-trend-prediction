package models;

import java.util.Arrays;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
public class BuyerHistoricalQuantity {
    private int buyerId;
    private int[] consumptionQuantity = new int[15];

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setConsumption(int[] consumptionQuantity) {
        this.consumptionQuantity = consumptionQuantity;
    }

    @Override
    public String toString() {
        return "BuyerHistoricalQuantity{" +
                "buyerId=" + buyerId +
                ", consumption=" + Arrays.toString(consumptionQuantity) +
                '}';
    }
}
