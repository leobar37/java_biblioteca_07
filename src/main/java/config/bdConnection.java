package config;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author USER
 */
import java.sql.*;

import java.util.Properties;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;
import globals.Constants;

public class bdConnection {

    private static Connection conn = null;

    private static String getUrl(String bd, String host) {
        String url = String.format("jdbc:postgresql://%s/%s", host, bd);
        return url;
    }

    private bdConnection() {
        //
    }
    // Deprecated
    public static Connection getConnection() {
        Properties properties = new Properties();
        properties.setProperty("user", Constants.USER);
        properties.setProperty("password", Constants.password);
        try {
            bdConnection.registerDriver();
            bdConnection.conn = DriverManager.getConnection(bdConnection.getUrl(Constants.bd, Constants.host), properties);
        } catch (SQLException ex) {
            Logger.getLogger(bdConnection.class.getName()).log(Level.SEVERE, "Error connection bd", ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(bdConnection.class.getName()).log(Level.SEVERE, "class not found", ex);
        }finally{
            bdConnection.conn.close();
        }
        return bdConnection.conn;
    }

    public static <T> T secureConnection(ConnectionCallback<T> call) throws SQLException {
        T value = call.callback(getConnection());
        getConnection().close();
        return value;
    }

    public static ResultSet fastQueries(String query) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement st = conn.prepareStatement(query);
            return st.executeQuery();
        });
    }
    
    public static boolean multiplesQueriesTransaction(String... queries) throws SQLException {
        return bdConnection.transaction(conn -> {

            try {
                for (String query : queries) {
                    bdConnection.fastQueries(query);
                }
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }

        });
    }

    public static <T> T transaction(ConnectionCallback<T> call) throws SQLException {
        Connection con = getConnection();
        try {
            con.setAutoCommit(false);
            T value = call.callback(con);
            con.commit();
            return value;
        } catch (SQLException e) {
            e.printStackTrace();
            con.rollback();
            throw new SQLException(e);
        }

    }

    public static Statement getStatement() {
        try {
            return bdConnection.getConnection().createStatement();
        } catch (SQLException ex) {
            Logger.getLogger(bdConnection.class.getName()).log(Level.SEVERE, "Error create statement", ex);
        }
        return null;
    }

    private static void registerDriver() throws ClassNotFoundException {
        Class.forName("org.postgresql.Driver");
    }

}
