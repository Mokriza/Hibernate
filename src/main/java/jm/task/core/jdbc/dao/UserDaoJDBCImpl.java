package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String sqlCreateUsersTable = "CREATE TABLE IF NOT EXISTS pp_1_1_4.users (\n" +
                    "                    id INT not NULL AUTO_INCREMENT,  \n" +
                    "                    name VARCHAR(50),  \n" +
                    "                    lastName VARCHAR (50),\n" +
                    "                    age TINYINT(3) not NULL,\n" +
                    "                    PRIMARY KEY (id))";
            statement.execute(sqlCreateUsersTable);
        } catch (SQLException e) {
            System.out.println("При cоздании таблицы пользователей произошло исключение");
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String sqlDropUsersTable = "DROP TABLE IF EXISTS pp_1_1_4.users";
            statement.execute(sqlDropUsersTable);
        } catch (SQLException e) {
            System.out.println("При удалении таблицы пользователей произошла ошибка");
        }

    }

    public void saveUser(String name, String lastName, byte age) {
        String sql = "INSERT INTO PP_1_1_4.users (name, lastname, age) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql);) {
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
        String sql = "DELETE FROM pp_1_1_4.users WHERE ID=?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Ошибка при удалении пользователя из БД по ID");
        }
    }

    public List<User> getAllUsers() {
        List<User> result = new ArrayList<>();
        String sql = "select id, name, lastname, age from pp_1_1_4.users";

        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
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
            String sqlDropUsersTable = "DELETE FROM pp_1_1_4.users";
            statement.execute(sqlDropUsersTable);
        } catch (SQLException e) {
            System.out.println("При очистке таблицы пользователей произошла ошибка");
        }
    }
}
