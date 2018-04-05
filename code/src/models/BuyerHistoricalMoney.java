package models;

import java.util.Arrays;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
public class BuyerHistoricalMoney {
    private int buyerId;
    private double[] consumptionAmount = new double[15];

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setConsumptionAmount(double[] consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    @Override
    public String toString() {
        return "BuyerHistoricalMoney{" +
                "buyerId=" + buyerId +
                ", consumptionAmount=" + Arrays.toString(consumptionAmount) +
                '}';
    }
}
