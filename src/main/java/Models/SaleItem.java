package Models;

import lombok.Data;

@Data
public class SaleItem extends BaseEntity{

    private long id;

    private Product product;

    private double price;

    private int count;

    public SaleItem(long id, Product product, double price, int count) {
        this.id = id;
        this.product = product;
        this.price = price;
        this.count = count;
    }
}
