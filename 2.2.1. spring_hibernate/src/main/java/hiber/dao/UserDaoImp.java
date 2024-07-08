package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.resource.transaction.spi.TransactionStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


@Repository
public class UserDaoImp implements UserDao {

   private final Logger logger;

   public UserDaoImp() {
      logger = Logger.getLogger(this.getClass().getName());
   }

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void add(User user) {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         Long id = (Long)session.save(user);
//         if (user.getCar() != null) {
//            Car car = user.getCar();
//            car.setId(id);
//            session.save(car);
//         }
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE && transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
         }
         logger.log(Level.WARNING, "Error while adding to users table (hibernate): " + e.getMessage());
      }
   }

   @Override
   public List<User> listUsers() {
      Transaction transaction = null;
      List<User> users = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         users = session.createQuery("from User", User.class).list();
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE && transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
         }
         logger.log(Level.WARNING, "Error while getting all users (hibernate): " + e.getMessage());
      }
      return users;
   }


   @Override
   public void cleanUsersTable() {
      Transaction transaction = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         String sql = "SET FOREIGN_KEY_CHECKS = 0;";
         session.createSQLQuery(sql).executeUpdate();
         sql = "TRUNCATE TABLE cars;";
         session.createSQLQuery(sql).addEntity(Car.class).executeUpdate();
         sql = "TRUNCATE TABLE users;";
         session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE && transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
         }
         logger.log(Level.WARNING, "Error while truncating users table (hibernate): " + e.getMessage());
      }
   }

   @Override
   public User findByCar(String model, int series) {
      Transaction transaction = null;
      User user = null;
      try (Session session = sessionFactory.openSession()) {
         transaction = session.beginTransaction();
         var query = session.createQuery("from User as user " +
                 "join user.car as car " +
                 "with car.series = :series AND car.model = :model ");
         query.setParameter("series", series);
         query.setParameter("model", model);
         query.setMaxResults(1);
         Object[] u = (Object[]) query.uniqueResult();
         user = (User) u[0];
//         user = (User)u.get(0)[0];
//         var u = query.addEntity(User.class).uniqueResult();
         transaction.commit();
      } catch (Exception e) {
         if (transaction != null && transaction.getStatus() == TransactionStatus.ACTIVE && transaction.getStatus() == TransactionStatus.MARKED_ROLLBACK) {
            transaction.rollback();
         }
         logger.log(Level.WARNING, "Error while getting user by car (hibernate): " + e.getMessage());
      }
      return user;
   }
}
