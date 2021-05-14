/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author USER
 */
@FunctionalInterface
public interface ConnectionCallback<T> {
    public T callback(Connection connection) throws SQLException;
}