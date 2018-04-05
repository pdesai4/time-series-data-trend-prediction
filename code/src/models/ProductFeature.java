package models;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
public class ProductFeature {
    private int productId;
    private int attribute_1;
    private int attribute_2;
    private double originalPrice;

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setAttribute_1(int attribute_1) {
        this.attribute_1 = attribute_1;
    }

    public void setAttribute_2(int attribute_2) {
        this.attribute_2 = attribute_2;
    }

    public void setOriginalPrice(double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "ProductFeature{" +
                "productId=" + productId +
                ", attribute_1=" + attribute_1 +
                ", attribute_2=" + attribute_2 +
                ", originalPrice=" + originalPrice +
                '}';
    }
}
