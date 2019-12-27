package com.vvz.persisting;

import com.vvz.model.User;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import com.vvz.persisting.DAO;
import com.vvz.persisting.MySQLconnector;

import java.sql.*;

import static org.junit.Assert.assertEquals;

public class DAOTesting {

    private DAO dao;

    @Before
    public void setUp() throws SQLException {
        ResultSet resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);
        Mockito.when(resultSet.getInt("id")).thenReturn(1);
        Mockito.when(resultSet.getString("name")).thenReturn("test");
        Mockito.when(resultSet.getString("lastname")).thenReturn("test1");
        Mockito.when(resultSet.getString("phone")).thenReturn("test2");

        Statement statement = Mockito.mock(Statement.class);
        Mockito.when(statement.execute("CREATE SCHEMA IF NOT EXISTS " +
                "`contacts` DEFAULT CHARACTER SET utf8 COLLATE utf8_bin")).thenReturn(true);
        Mockito.when(statement.execute("CREATE TABLE IF NOT EXISTS `contacts`.`users` (\n" +
                "  `id` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NULL,\n" +
                "  `lastname` VARCHAR(45) NULL,\n" +
                "  `phone` VARCHAR(45) NULL,\n" +
                "  PRIMARY KEY (`id`))\n" +
                "ENGINE = InnoDB\n" +
                "DEFAULT CHARACTER SET = utf8\n" +
                "COLLATE = utf8_bin")).thenReturn(true);

        PreparedStatement preparedStatement = Mockito.mock(PreparedStatement.class);
        Mockito.when(preparedStatement.executeQuery()).thenReturn(resultSet);
        Mockito.when(preparedStatement.executeUpdate()).thenReturn(1);

        Connection connection = Mockito.mock(Connection.class);
        Mockito.when(connection.createStatement()).thenReturn(statement);
        Mockito.when(connection.prepareStatement("SELECT * FROM contacts.users WHERE id = ?")).thenReturn(preparedStatement);
        Mockito.when(connection.prepareStatement("UPDATE contacts.users " +
                "SET name = ?, lastname = ?, phone = ?  WHERE id = ?")).thenReturn(preparedStatement);
        Mockito.when(connection.prepareStatement("INSERT INTO contacts.users " +
                "(name, lastname, phone) values (?, ?, ?)")).thenReturn(preparedStatement);
        Mockito.when(connection.prepareStatement("SELECT * FROM contacts.users")).thenReturn(preparedStatement);
        Mockito.when(connection.prepareStatement("DELETE FROM contacts.users WHERE id = ?")).thenReturn(preparedStatement);

        MySQLconnector mySQLconnector = Mockito.mock(MySQLconnector.class);
        Mockito.when(mySQLconnector.getConnection()).thenReturn(connection);
        dao = new DAO(mySQLconnector);
    }

    @Test
    public void testGetUser() {
        User user = new User();
        user.setId(1);
        user = dao.getUser(user);

        assertEquals(1, user.getId());
        assertEquals("test", user.getName());
        assertEquals("test1", user.getLastname());
        assertEquals("test2", user.getPhone());
    }

    @Test
    public void testAddUser() {
        User user = new User();
        user.setId(0);
        dao.addUser(user);

        assertEquals(0, dao.getUser(user).getId());
        assertEquals(null, dao.getUser(user).getName());
        assertEquals(null, dao.getUser(user).getLastname());
        assertEquals(null, dao.getUser(user).getPhone());
    }

    @Test
    public void testDeleteUser() {
        User user = new User();
        user.setId(1);
        dao.deleteUser(user);
    }

    @Test
    public void testGetAllUsers() {
        User user = new User();
        user.setId(1);

        assertEquals(1, dao.getAllUsers().size());
    }


}
