package model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class OrderProduct implements Serializable {
    @Serial
    private static final long serialVersionUID = 6529685098267751237L;

    private Product product;
    private Long quantity;

    public OrderProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public OrderProduct() {

    }


    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderProduct that = (OrderProduct) o;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }

    @Override
    public String toString() {
        return String.format(
                "#%s| %s -> %s",
                product.getID(), product.getName(), quantity
                );
    }
}
