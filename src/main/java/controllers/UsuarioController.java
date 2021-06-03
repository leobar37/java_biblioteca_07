/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import config.bdConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;
import models.Area;
import models.User;
import utils.Hash;
import view.frmPrincipal;

/**
 *
 * @author USER
 */
public class UsuarioController extends BaseController<Area> {

    private static UsuarioController controller;

    private UsuarioController() {
        super();
    }
    private User user = null;

    public static UsuarioController instance() {
        if (controller != null) {
            return controller;
        } else {
            controller = new UsuarioController();

            return controller;
        }
    }
    /// add user 

    public User addUser(User user) throws SQLException {
        // bdConnection.getStatement()
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("insert into users(dni, name, direction , phone, password , cuestion , rptaCuestion , username ) values (?,?,?,?,? , ? , ?,?) returning *");
        statement.setString(1, user.getDni());
        statement.setString(2, user.getName());
        statement.setString(3, user.getDirection());
        statement.setString(4, user.getPhone());
        statement.setString(5, Hash.encript(user.getPassword()));
        statement.setString(6, user.getCuestion());
        statement.setString(7, user.getRptaCuestion());
        statement.setString(8, user.getUsername());
        ResultSet res = statement.executeQuery();

        return getFirstResult(res).get();
    }

    private Optional<User> getFirstResult(ResultSet res) throws SQLException {
        while (res.next()) {
            Optional<User> user = getUserOfResultSet(res);
            res.close();
            return user;
        }
        res.close();
        return Optional.empty();

    }

    private Optional<User> getUserOfResultSet(ResultSet res) throws SQLException {
        String name = res.getObject("name", String.class);
        String dni = res.getObject("dni", String.class);
        String direction = res.getObject("direction", String.class);
        String phone = res.getObject("phone", String.class);
        String cuestion = res.getObject("cuestion", String.class);
        String rpyacuestion = res.getObject("rptaCuestion", String.class);
        String password = res.getObject("password", String.class);
        // boolean estado = res.getObject("estado", Boolean.class);
        // User user = new User(String dni, String name, String direction, String phone);    
        User user = new User(dni, name, direction, phone);
        user.setPassword(password);
        user.setCuestion(cuestion);
        user.setRptaCuestion(rpyacuestion);
//return Optional.of(new );

        return Optional.of(user);
    }

    public Optional<User> searchUser(String dni) throws SQLException {
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("select *  from users where dni = ?");
        statement.setString(1, dni);
        ResultSet res = statement.executeQuery();
        return getFirstResult(res);

    }

    public Optional<User> validateUser(String password, String username) throws SQLException {
        PreparedStatement statement = bdConnection.getConnection().prepareStatement("select * from users where username = ?");
        statement.setString(1, username);
        ResultSet res = statement.executeQuery();
        Optional<User> user = getFirstResult(res);

        if (user.isPresent()) {
            if (!Hash.compareHash(password, user.get().getPassword())) {
                return Optional.empty();
            }
            this.user = user.get();
        }
        return user;
    }

    public Optional<User> updateUser(String dni, User user) throws SQLException {

        PreparedStatement statement = bdConnection.getConnection().prepareStatement("UPDATE public.users"
                + "SET dni = ? -- character varying PRIMARY KEY"
                + "    , name = ? -- character varying"
                + "    , direction = ? -- character varying NULLABLE"
                + "    , phone = ? -- character varying NULLABLE"
                + "    , password = ? -- character varying NULLABLE"
                + "    , cuestion = ? -- character varying NULLABLE"
                + "    , rptacuestion = ? -- text NULLABLE"
                + "    , username = ? -- character varying NULLABLE"
                + "WHERE condition returning *");

        statement.setString(1, user.getName());
        statement.setString(2, user.getDirection());
        statement.setString(3, user.getPhone());
        statement.setString(4, user.getPassword());
        statement.setString(5, user.getCuestion());
        statement.setString(6, user.getRptaCuestion());
        statement.setString(7, user.getUsername());
        return getFirstResult(statement.executeQuery());

    }

    public LinkedList<User> getUsers() throws SQLException {
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
