package models;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
public class TradeInfoTraining {
    private int productId;
    private int buyerId;
    private int tradeTime;
    private int tradeQuantity;
    private double tradePrice;

    public void setTradePrice(double tradePrice) {
        this.tradePrice = tradePrice;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public int getTradeQuantity() {
        return tradeQuantity;
    }

    public void setTradeQuantity(int tradeQuantity) {
        this.tradeQuantity = tradeQuantity;
    }

    public int getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(int tradeTime) {
        this.tradeTime = tradeTime;
    }

    @Override
    public String toString() {
        return "TradeInfoTraining{" +
                "productId=" + productId +
                ", buyerId=" + buyerId +
                ", tradeTime=" + tradeTime +
                ", tradeQuantity=" + tradeQuantity +
                ", tradePrice=" + tradePrice +
                '}';
    }
}
