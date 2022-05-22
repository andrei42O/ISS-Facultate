package observer;

import model.OrderProduct;
import model.Receipt;

import java.util.ArrayList;

public class NotAvailableProducts implements Event {
    ArrayList<OrderProduct> obj;

    public NotAvailableProducts(ArrayList<OrderProduct> obj) {
        this.obj = obj;
    }

    @Override
    public Object getObject() {
        return obj;
    }

    @Override
    public void setObject(Object o) {

    }
}
