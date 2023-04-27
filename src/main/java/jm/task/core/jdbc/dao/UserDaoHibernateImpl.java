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
        Session session = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();

            session.createSQLQuery(SQL_CREATE_TABLE).addEntity(User.class).executeUpdate();

            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при создании новой таблицы User");
        } finally {
            session.close();
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();

            session.createSQLQuery(SQL_DROP_TABLE).executeUpdate();

            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при удалении таблицы users");
        } finally {
            session.close();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();

            User user = new User(name, lastName, age);
            session.save(user);
            t.commit();

            System.out.println("User с именем " + name + " добавлен в базу данных");

        } catch (HibernateException e) {
            System.out.println("Ошибка при добавлении пользователя в базу данных");
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();

            User user = session.get(User.class, id);
            session.delete(user);

            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при удалении пользователя по ID из базы данных");
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Session session = null;
        List<User> result = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();
            result = session.createQuery("FROM User").getResultList();
            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при получении всех пользователей из базы данных");
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        Session session = null;
        try {
            session = factory.getCurrentSession();
            Transaction t = session.beginTransaction();
            session.createQuery("delete from User").executeUpdate();
            t.commit();
        } catch (HibernateException e) {
            System.out.println("Ошибка при удалении всех данных из базы данных");
        } finally {
            session.close();
        }
    }
}
