package persistance;

import model.Agent;
import model.Order;
import model.Receipt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepositoryORMReceipt implements IRepositoryReceipt{
    private static SessionFactory sessionFactory = null;

    public RepositoryORMReceipt(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Receipt save(Receipt entity) {
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
    public List<Receipt> getAll() {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                List<Receipt> receipts =
                        session.createQuery("from Receipt", Receipt.class)
                                .list();
                tx.commit();
                return receipts;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Receipt delete(Receipt entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Receipt order = session.get(Receipt.class, entity.getID());
                session.delete(order);
                tx.commit();
                return order;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Receipt update(Receipt entity, Long aLong) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.update(entity);
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
    public Receipt findByID(Long aLong) {
        return null;
    }
}
