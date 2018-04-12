package ua.epam.spring.hometask.domain;

/**
 * Created by Oleksii_Kovetskyi on 4/12/2018.
 */
public class Discount implements Comparable<Discount> {

    private String discountType;
    private byte percent;

    public Discount() {
    }

    public Discount(String discountType, byte percent) {
        this.discountType = discountType;
        this.percent = percent;
    }

    public String getDiscountType() {
        return discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public byte getPercent() {
        return percent;
    }

    public void setPercent(byte percent) {
        this.percent = percent;
    }

    @Override
    public int compareTo(Discount o) {
        return this.percent - o.percent;
    }
}
