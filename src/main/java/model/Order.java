package model;

import java.util.HashMap;
import java.util.List;

public class Order extends Entity<Long>{
    private HashMap<Product, Integer> products;

    public Order() {
        products = new HashMap<>();
    }

    public HashMap<Product, Integer> getProducts() {
        return products;
    }

    public void addProduct(Product p, Integer quantity){
        if(products.containsKey(p)) {
            var oldVal = products.get(p);
            products.replace(p, oldVal, oldVal + quantity);
        }
    }
}
