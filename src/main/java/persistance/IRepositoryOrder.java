package persistance;

import model.Agent;
import model.Order;

public interface IRepositoryOrder extends IRepository<Long, Order> {
    Iterable<Order> getAgentOrders(Agent agent);
}
