package com.vvz.persisting;



import com.vvz.model.User;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

@ManagedBean(name="dao", eager = true)
@ApplicationScoped
public class DAO {

    public static Connection connection;

    public DAO(){
        MySQLconnector mySQLconnector = new MySQLconnector();
        connection = mySQLconnector.getConnection();
        mySQLconnector.resetDB(connection);
    }

    public DAO(MySQLconnector mySQLconnector) {
        connection = mySQLconnector.getConnection();
        mySQLconnector.resetDB(connection);
    }

    public void addUser(User user) {
        boolean userExists = false;
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contacts.users WHERE id = ?")) {
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userExists = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (userExists) {
            try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE contacts.users " +
                    "SET name = ?, lastname = ?, phone = ?  WHERE id = ?")) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getPhone());
                preparedStatement.setInt(4, user.getId());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO contacts.users " +
                    "(name, lastname, phone) values (?, ?, ?)")) {
                preparedStatement.setString(1, user.getName());
                preparedStatement.setString(2, user.getLastname());
                preparedStatement.setString(3, user.getPhone());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<User> getAllUsers() {
        LinkedList<User> usersList = new LinkedList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contacts.users")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setName(resultSet.getString("name"));
                user.setLastname(resultSet.getString("lastname"));
                user.setPhone(resultSet.getString("phone"));
                usersList.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usersList;
    }

    public User getUser(User user) {
        User receiveUser = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM contacts.users WHERE id = ?")) {
            preparedStatement.setInt(1, user.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                receiveUser.setId(resultSet.getInt("id"));
                receiveUser.setName(resultSet.getString("name"));
                receiveUser.setLastname(resultSet.getString("lastname"));
                receiveUser.setPhone(resultSet.getString("phone"));
                return receiveUser;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return receiveUser;
    }

    public void deleteUser(User user) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM contacts.users WHERE id = ?")) {
            preparedStatement.setInt(1, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
