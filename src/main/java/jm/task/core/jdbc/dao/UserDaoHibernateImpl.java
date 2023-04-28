package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    private SessionFactory factory = Util.getSessionFactory();

    private final static String SQL_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS `pp_1_1_4`.`users` (\n" +
            "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(50) NULL,\n" +
            "  `lastName` VARCHAR(50) NULL,\n" +
            "  `age` TINYINT(3) NOT NULL,\n" +
            "  PRIMARY KEY (`id`))";

    private final static String SQL_DROP_TABLE = "DROP TABLE IF EXISTS pp_1_1_4.users";

    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            Transaction t = session.beginTransaction();
            session.createSQLQuery(SQL_CREATE_TABLE).addEntity(User.class).executeUpdate();
            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при создании новой таблицы User");
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = factory.getCurrentSession()) {
            Transaction t = session.beginTransaction();
            session.createSQLQuery(SQL_DROP_TABLE).executeUpdate();
            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при удалении таблицы users");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            session.save(user);
            transaction.commit();
            System.out.println("User с именем " + name + " добавлен в базу данных");
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при добавлении пользователя в базу данных");
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при удалении пользователя по ID из базы данных");
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        List<User> result = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            result = session.createQuery("FROM User").getResultList();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при получении всех пользователей из базы данных");
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;
        try (Session session = factory.getCurrentSession()) {
            transaction = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("Ошибка при удалении всех данных из базы данных");
        }
    }
}
