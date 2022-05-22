import Utils.SessionFactoryUtils;
import model.*;
import persistance.RepositoryORMOrder;
import persistance.RepositoryORMProduct;
import persistance.RepositoryORMUser;

public class Test {
    public static void main(String[] args) {
        RepositoryORMUser repoUser = new RepositoryORMUser(SessionFactoryUtils.getSessionFactory());
        RepositoryORMProduct repoProduct = new RepositoryORMProduct(SessionFactoryUtils.getSessionFactory());
        Order order = new Order((Agent)repoUser.findByUsername("test"));
        RepositoryORMOrder repoOrder = new RepositoryORMOrder(SessionFactoryUtils.getSessionFactory());
        order = repoOrder.save(order);
        order.modifyQuantity(new OrderProduct(repoProduct.findByID(1L), 20L));
        repoOrder.update(order, order.getID());
        order.modifyQuantity(new OrderProduct(repoProduct.findByID(2L), 30L));
        order.modifyQuantity(new OrderProduct(repoProduct.findByID(1L), 50L));
        repoOrder.update(order, order.getID());
        repoOrder.getAll().forEach(ord -> {
            System.out.println(ord.getProducts());
        });
    }
}
