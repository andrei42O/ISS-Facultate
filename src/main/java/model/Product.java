package model;

import java.util.Objects;

public class Product extends Entity<Long>{
    private String name;
    private Long stock;
    private Double price;
    private String details;

    public Product(String name, Long quantity, Double price) {
        this.name = name;
        this.stock = quantity;
        this.price = price;
    }

    public Product(String name, Long quantity, Double price, String details) {
        this.name = name;
        this.stock = quantity;
        this.price = price;
        this.details = details;
    }

    public Product() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Product product = (Product) o;
        return Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(details, product.details);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, stock, price, details);
    }

    @Override
    public String toString() {
        return String.format(
                "#%s -> %s | stock: %s | price: %s | details: %s",
                getID(), getName(), getStock(), getPrice(), getDetails()
        );
    }
}
