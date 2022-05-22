package observer;

import model.Receipt;

public class ReceiptGenerated implements Event{
    private Receipt receipt = null;

    public ReceiptGenerated(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public Object getObject() {
        return receipt;
    }

    @Override
    public void setObject(Object o) {
        this.receipt = (Receipt)o;
    }
}
