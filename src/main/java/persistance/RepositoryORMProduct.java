package persistance;

import model.Product;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;

public class RepositoryORMProduct implements IRepositoryProduct{
    private static SessionFactory sessionFactory = null;

    public RepositoryORMProduct(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Product save(Product entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return entity;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public List<Product> getAll() {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                List<Product> products =
                        session.createQuery("from Product", Product.class)
                                .list();
                return products;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public Product delete(Product entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Product product = session.get(Product.class, entity.getID());
                session.delete(product);
                tx.commit();
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Product update(Product entity, Long aLong) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Product product = session.get(Product.class, aLong);
                product.setDetails(entity.getDetails());
                product.setPrice(entity.getPrice());
                product.setName(entity.getName());
                product.setStock(entity.getStock());
                session.update(product);
                tx.commit();
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Product findByID(Long aLong) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Product product = session.get(Product.class, aLong);
                tx.commit();
                return product;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }
}
