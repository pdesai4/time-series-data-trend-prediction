package models;

import java.util.Arrays;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */
public class ProductDistributionTrainingSet {
    private int productId;
    private int[] quantitiesOfKeyProduct = new int[118];

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public void setQuantitiesOfKeyProduct(int[] quantitiesOfKeyProduct) {
        this.quantitiesOfKeyProduct = quantitiesOfKeyProduct;
    }

    public int getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return "ProductDistributionTrainingSet{" +
                "productId=" + productId +
                ", quantitiesOfKeyProduct=" + Arrays.toString(quantitiesOfKeyProduct) +
                '}';
    }
}
