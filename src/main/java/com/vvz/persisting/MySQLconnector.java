package com.vvz.persisting;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static com.vvz.persisting.ConfigLoader.*;


public class MySQLconnector {
    final static Logger logger = Logger.getLogger(MySQLconnector.class);

    public MySQLconnector() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Invalid driver", e);
        }
    }

    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(urlDB, userDB, passwordDB);
        } catch (SQLException e) {
            logger.error("Cannot plug in...", e);
        }
        return connection;
    }

    public void resetDB(Connection connection) {
        try (Statement statement = connection.createStatement()) {
            statement.execute("CREATE SCHEMA IF NOT EXISTS `contacts` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin");
            statement.execute("CREATE TABLE IF NOT EXISTS `contacts`.`users` (\n" +
                    "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                    "  `name` VARCHAR(45) NULL,\n" +
                    "  `lastname` VARCHAR(45) NULL,\n" +
                    "  `phone` VARCHAR(45) NULL,\n" +
                    "  PRIMARY KEY (`id`))\n" +
                    "ENGINE = InnoDB\n" +
                    "DEFAULT CHARACTER SET = utf8\n" +
                    "COLLATE = utf8_bin");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
