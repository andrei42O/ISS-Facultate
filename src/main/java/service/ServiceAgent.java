package service;

import model.*;
import observer.*;
import persistance.IRepositoryOrder;
import persistance.IRepositoryProduct;
import persistance.IRepositoryReceipt;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ServiceAgent extends BasicThreadSafeObservableExample  implements IService{
    private IRepositoryProduct repositoryProduct = null;
    private IRepositoryOrder repositoryOrder = null;
    private IRepositoryReceipt repositoryReceipt = null;
    private Agent loggedAgent = null;

    public ServiceAgent() {
    }

    public Agent getLoggedAgent() {
        return loggedAgent;
    }

    public ServiceAgent(Agent loggedAgent) {
        this.loggedAgent = loggedAgent;
    }

    public void setRepositoryProduct(IRepositoryProduct repoProduct) {
        this.repositoryProduct = repoProduct;

    }

    public void setLoggedAgent(Agent loggedAgent) {
        this.loggedAgent = loggedAgent;
    }

    public void setRepositoryOrder(IRepositoryOrder repoOrder) {
        this.repositoryOrder = repoOrder;
    }

    public void setRepositoryReceipt(IRepositoryReceipt repositoryReceipt) {
        this.repositoryReceipt = repositoryReceipt;
    }

    public Iterable<Product> getAllProducts() {
        return repositoryProduct.getAll();
    }

    public Iterable<Order> getOrders() {
        return repositoryOrder.getAgentOrders(this.loggedAgent);
    }

    public Order createOrder() {
        Order order = new Order(loggedAgent);
        repositoryOrder.save(order);
        return order;
    }

    public Product searchProduct(Long id) {
        return repositoryProduct.findByID(id);
    }

    public Order updateOrder(Order order) {
        Order ret = repositoryOrder.update(order, order.getID());
        if(ret == null)
            throw new RuntimeException("The order couldn't be updated!");
        return ret;
    }

    public Order deleteOrder(Order item) {
        return repositoryOrder.delete(item);
    }

    public Receipt placeOder(Order order, String clientName, String address){
        Receipt receipt = new Receipt(clientName, address, LocalDateTime.now(), order);
        if(repositoryReceipt.save(receipt) != null){
            order.setPlaced(true);
            if(repositoryOrder.update(order, order.getID()) == null){
                repositoryReceipt.delete(receipt);
                return null;
            }
            updateProducts(order);
            notifyObservers(new ReceiptGenerated(receipt));
            return receipt;
        }
        return null;
    }

    private void updateProducts(Order order) {
        ArrayList<OrderProduct> notAvailable = new ArrayList<>();
        order.getProducts().forEach(o -> {
            if(repositoryProduct.findByID(o.getProduct().getID()).getStock() < o.getQuantity()){
                notAvailable.add(o);
            }
        });
        if(!notAvailable.isEmpty()){
            notifyObservers(new NotAvailableProducts(notAvailable));
        }
        order.getProducts().forEach(o -> {
            Product p = repositoryProduct.findByID(o.getProduct().getID());
            p.setStock(p.getStock() - o.getQuantity());
            repositoryProduct.update(p, p.getID());
            notifyObservers(new ProductQuantityUpdated(p));
        });

    }
}
