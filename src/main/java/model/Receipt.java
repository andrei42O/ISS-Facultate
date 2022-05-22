package model;

import java.time.LocalDateTime;
import java.util.Objects;

public class Receipt extends Entity<Long>{
    private String clientName;
    private String address;
    private LocalDateTime date;
    private Order order;

    public Receipt(String clientName, String address, LocalDateTime date, Order order) {
        this.clientName = clientName;
        this.address = address;
        this.date = date;
        this.order = order;
    }

    public Receipt() {
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(clientName, receipt.clientName) && Objects.equals(address, receipt.address) && Objects.equals(date, receipt.date) && Objects.equals(order, receipt.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, address, date, order);
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "clientName='" + clientName + '\'' +
                ", address='" + address + '\'' +
                ", date=" + date +
                ", order=" + order +
                '}';
    }
}
