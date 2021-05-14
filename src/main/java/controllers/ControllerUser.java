package controllers;


import java.util.LinkedList;
import java.util.Optional;

import models.Person;
import models.User;
import config.bdConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import models.enums.Sexo;
import utils.Hash;

public class ControllerUser {

    public static User addUser(User user) throws SQLException {
        // bdConnection.getStatement()
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("insert into users(dni, name, direction , phone, password , cuestion , rptaCuestion , username ) values (?,?,?,?,? , ? , ?,?) returning *");
        statement.setString(1, user.getDni());
        statement.setString(2, user.getName());
        statement.setString(3, user.getDirection());
        statement.setString(4, user.getPhone());
        statement.setString(5, user.getPassword());
        statement.setString(6, user.getCuestion());
        statement.setString(7, user.getRptaCuestion());
        statement.setString(8, user.getUsername());
        ResultSet res = statement.executeQuery();
        return getFirstResult(res).get();
    }

    private static Optional<User> getFirstResult(ResultSet res) throws SQLException {
        while (res.next()) {
            Optional<User> user = getUserOfResultSet(res);
            res.close();
            return user;
        }
        res.close();
        return Optional.empty();

    }

    private static Optional<User> getUserOfResultSet(ResultSet res) throws SQLException {
        String name = res.getObject("name", String.class);
        String dni = res.getObject("dni", String.class);
        String direction = res.getObject("direction", String.class);
        String phone = res.getObject("phone", String.class);
        String cuestion = res.getObject("cuestion", String.class);
        String rpyacuestion = res.getObject("rptaCuestion", String.class);
        String password = res.getObject("password", String.class);
        boolean state =  res.getObject("estado", Boolean.class);
        String sex  = res.getObject("sexo" ,  String.class);
        Sexo sexo = Person.evaluateSex(sex);
        return Optional.of(new User(dni, name, direction, phone, password, cuestion, rpyacuestion ,state));
    }

    public static Optional<User> searchUser(String dni) throws SQLException {
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("select *  from users where dni = ?");
        statement.setString(1, dni);
        ResultSet res = statement.executeQuery();
        return getFirstResult(res);

    }

    public static Optional<User> validateUser(String password, String username) throws SQLException {
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("select * from users where password = ? and username = ?");
        statement.setString(1, Hash.encript(password));
        statement.setString(2, username);
        ResultSet res = statement.executeQuery();
        return getFirstResult(res);
    }

    public static Optional<User> updateUser(String dni, User user) throws SQLException {

        PreparedStatement statement = bdConnection.getConnection().prepareStatement("UPDATE public.users"
                + "SET dni = ? -- character varying PRIMARY KEY"
                + "    , name = ? -- character varying"
                + "    , direction = ? -- character varying NULLABLE"
                + "    , phone = ? -- character varying NULLABLE"
                + "    , password = ? -- character varying NULLABLE"
                + "    , cuestion = ? -- character varying NULLABLE"
                + "    , rptacuestion = ? -- text NULLABLE"
                + "    , username = ? -- character varying NULLABLE"
                + "WHERE id = ? returning *" );
        
        statement.setString(1,  user.getName());
        statement.setString(2,  user.getDirection() );
        statement.setString(3 , user.getPhone());
        statement.setString(4 , user.getPassword());
        statement.setString(5,  user.getCuestion());
        statement.setString(6, user.getRptaCuestion());
        statement.setString(7 , user.getUsername());
        return getFirstResult(statement.executeQuery()); 

    }

    public static LinkedList<User> getUsers() throws SQLException {
        LinkedList<User> users = new LinkedList<User>();
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("select *  from users");

        ResultSet res = statement.executeQuery();
        while (res.next()) {
            Optional<User> userOpt = getUserOfResultSet(res);
            if (userOpt.isPresent()) {
                System.out.println("user present");
                users.add(userOpt.get());
            }
        }
        res.close();
        return users;
    }

}
