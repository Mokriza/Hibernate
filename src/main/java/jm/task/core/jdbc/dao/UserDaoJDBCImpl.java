package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection = Util.getConnection();

    private final static String SQL_REMOVE_USER = "DELETE FROM pp_1_1_4.users WHERE ID=?";
    private final static String SQL_SAVE_USER = "INSERT INTO PP_1_1_4.users (name, lastname, age) VALUES (?, ?, ?)";

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS pp_1_1_4.users (\n" +
                    "                    id INT not NULL AUTO_INCREMENT,  \n" +
                    "                    name VARCHAR(50),  \n" +
                    "                    lastName VARCHAR (50),\n" +
                    "                    age TINYINT(3) not NULL,\n" +
                    "                    PRIMARY KEY (id))");
        } catch (SQLException e) {
            System.out.println("При cоздании таблицы пользователей произошло исключение");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DROP TABLE IF EXISTS pp_1_1_4.users");
        } catch (SQLException e) {
            System.out.println("При удалении таблицы пользователей произошла ошибка");
        }

    }

    public void saveUser(String name, String lastName, byte age) {

        try (PreparedStatement ps = connection.prepareStatement(SQL_SAVE_USER);) {
            ps.setString(1, name);
            ps.setString(2, lastName);
            ps.setByte(3, age);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при сохранении в БД нового пользователя");
        }
        System.out.println("User с именем " + name + " добавлен в базу данных");
    }

    public void removeUserById(long id) {
        try (PreparedStatement ps = connection.prepareStatement(SQL_REMOVE_USER)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении пользователя из БД по ID");
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery("select id, name, lastname, age from pp_1_1_4.users");
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getLong("id"));
                user.setName(resultSet.getString("name"));
                user.setLastName(resultSet.getString("lastname"));
                user.setAge(resultSet.getByte("age"));

                result.add(user);
            }
            System.out.println(result);
        } catch (SQLException e) {
            System.out.println("Ошибка при получении списка всех пользователей из БД");
        }
        return result;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM pp_1_1_4.users");
        } catch (SQLException e) {
            System.out.println("При очистке таблицы пользователей произошла ошибка");
        }
    }
}
