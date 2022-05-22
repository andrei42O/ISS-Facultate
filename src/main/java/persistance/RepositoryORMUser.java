package persistance;

import model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RepositoryORMUser implements IRepositoryDBUsers {

    private final SessionFactory sessionFactory;

    public RepositoryORMUser(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User save(User entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                session.save(entity);
                tx.commit();
                return entity;
            }
            catch(Exception e){
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public List<User> getAll() {
        try(Session session = sessionFactory.openSession()){
            try{
                return session.createQuery("FROM User", User.class)
                    .list();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    @Override
    public User delete(User entity) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                User _user =
                        session.createQuery(String.format("from User where id = %s", entity.getID()), User.class)
                                    .setMaxResults(1)
                                    .uniqueResult();

                session.delete(_user);
                tx.commit();
                return _user;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public User update(User entity, Long id) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                User user = session.load(User.class, id);
                user.setJob(entity.getJob());
                user.setName(entity.getName());
                user.setPassword(entity.getPassword());
                user.setUsername(entity.getUsername());
                session.update(user);
                tx.commit();
                return user;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public User findByID(Long aLong) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                User user = session.get(User.class, aLong);
                tx.commit();
                return user;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

    @Override
    public User findByUsername(String username) {
        try(Session session = sessionFactory.openSession()){
            Transaction tx = null;
            try{
                tx = session.beginTransaction();
                Query<User> query = session.createQuery("from User where username = :username", User.class);
                query.setParameter("username", username);
                User user = query.uniqueResult();
                tx.commit();
                return user;
            } catch (Exception e) {
                e.printStackTrace();
                if(tx != null)
                    tx.rollback();
                return null;
            }
        }
    }

}
