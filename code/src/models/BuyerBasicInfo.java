package models;

/**
 * Data Mining Project, Spring 2017
 * Created by Priyanka Desai
 * B-number: B00658664
 */

public class BuyerBasicInfo {

    private int buyerId;
    private long registrationTime;
    private int sellerLevel;
    private int buyerLevel;
    private int age;
    private int gender;

    public void setBuyerId(int buyerId) {
        this.buyerId = buyerId;
    }

    public void setRegistrationTime(long registrationTime) {
        this.registrationTime = registrationTime;
    }

    public void setSellerLevel(int sellerLevel) {
        this.sellerLevel = sellerLevel;
    }

    public void setBuyerLevel(int buyerLevel) {
        this.buyerLevel = buyerLevel;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "BuyerBasicInfo{" +
                "buyerId=" + buyerId +
                ", registrationTime=" + registrationTime +
                ", sellerLevel=" + sellerLevel +
                ", buyerLevel=" + buyerLevel +
                ", age=" + age +
                ", gender=" + gender +
                '}';
    }
}
