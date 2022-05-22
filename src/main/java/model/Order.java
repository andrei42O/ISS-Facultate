package model;

import java.util.HashSet;
import java.util.Set;

public class Order extends Entity<Long>{
    private Set<OrderProduct> products;
    private Agent agent;
    private Boolean placed = false;

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Order() {
    }

    public Order(Agent agent){
        this.products = new HashSet<>();
        this.agent = agent;
    }

    public Boolean getPlaced() {
        return placed;
    }

    public void setPlaced(Boolean placed) {
        this.placed = placed;
    }

    public Set<OrderProduct> getProducts() {
        return products;
    }

    public void modifyQuantity(OrderProduct p){
        products.remove(p);
        if(p.getQuantity() != 0)
            products.add(p);
    }

    public void setProducts(Set<OrderProduct> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return String.format(
                "Order #%s",
                getID());
    }
}
