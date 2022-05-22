package persistance;

import model.Agent;
import model.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepositoryORMOrder implements IRepositoryOrder{
    private static SessionFactory sessionFactory = null;

    public RepositoryORMOrder(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Order save(Order entity) {
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
    public List<Order> getAll() {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                List<Order> orders =
                        session.createQuery("from Order", Order.class)
                                .list();
                tx.commit();
                return orders;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public Order delete(Order entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Order order = session.get(Order.class, entity.getID());
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
    public Order update(Order entity, Long aLong) {
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
    public Order findByID(Long aLong) {
        return null;
    }

    @Override
    public Iterable<Order> getAgentOrders(Agent agent) {
        try(Session session = sessionFactory.openSession()){
            try{
                Query<Order> q = session.createQuery(
                        "from Order WHERE agent_id = :agent_id", Order.class);
                q.setParameter("agent_id", agent.getID());
                return q.list();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
