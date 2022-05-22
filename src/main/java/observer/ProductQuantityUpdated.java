package observer;

import model.Product;

public class ProductQuantityUpdated implements Event {
    private Product p;

    public ProductQuantityUpdated(Product p) {
        this.p = p;
    }

    @Override
    public Object getObject() {
        return p;
    }

    @Override
    public void setObject(Object o) {
        this.p = (Product)o;
    }
}
