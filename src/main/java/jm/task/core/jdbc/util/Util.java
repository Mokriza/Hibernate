package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    private static final String URL = "jdbc:mysql://localhost:3306/pp_1_1_4";
    private static final String USERNAME = "jdbc:mysql://localhost:3306/pp_1_1_4";
    private static final String PASSWORD = "jdbc:mysql://localhost:3306/pp_1_1_4";
    public static Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            System.out.println("Ошибка соединения с базой данных");
        }
        System.out.println("Соединение с БД установлено");
        return connection;
    }


}
