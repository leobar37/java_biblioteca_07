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
import models.Author;

/**
 *
 * @author USER
 */
public class AuthorController extends BaseController<Author> {

    private static AuthorController controller;

    private AuthorController() {
        super();

    }

    public static AuthorController instance() {
        if (controller != null) {
            return controller;
        } else {
            controller = new AuthorController();

            return controller;
        }
    }

    // CRUD
    public Optional<Author> updateNode(int code, Author node) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("UPDATE public.autor\n" +
"SET" +
"     nombre = ? -- character varying\n" +
"    , apellidos = ? -- character varying\n" +
"    , pais = ? -- character varying NULLABLE\n" +
"    , fechanac = ? -- date NULLABLE\n" +
"WHERE codigoaut = ? returning *");
            pr.setString(1, node.getNombre());
            pr.setString(2, node.getApellidos());
            pr.setString(3, node.getPais());
            pr.setDate(4, node.getFechaNac());
            pr.setInt(5, code);
            return Author.getNodeOfResult(pr.executeQuery(), true);
        });
    }
    public Optional<Author> deleteNode(int code) throws SQLException{
         return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("DELETE FROM\n" +
"--ONLY\n" +
"public.autor\n" +
"WHERE where codigoaut = ? returning *");
            pr.setInt(1, code);
            return Author.getNodeOfResult(pr.executeQuery(), true);
        });  
    }
    
    public Optional<Author> createNode(Author node) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO public.autor (\n" +
"      codigoaut\n" +
"    , nombre\n" +
"    , apellidos\n" +
"    , pais\n" +
"    , fechanac\n" +
") VALUES (\n" +
"      ? -- codigoaut numeric PRIMARY KEY\n" +
"    , ? -- nombre character varying\n" +
"    , ? -- apellidos character varying\n" +
"    , ? -- pais character varying NULLABLE\n" +
"    , ? -- fechanac date NULLABLE\n" +
") returning *");
            pr.setInt(1, this.generateCode());
            pr.setString(2, node.getNombre());
            pr.setString(3, node.getApellidos() );
            pr.setString(4, node.getPais());
            pr.setDate(5, node.getFechaNac());
          
            return Author.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public int generateCode() throws SQLException {
        return bdConnection.secureConnection(conn -> {
            try {
                PreparedStatement pr = conn.prepareStatement("select  COALESCE(Max(codigoaut) , 0) + 1 as codigo from autor");
                ResultSet rs = pr.executeQuery();
                rs.next();
                return rs.getInt("codigo");
            } catch (Exception e) {
                e.printStackTrace();;
                return -1;
            }
        });
    }
    
    
    
    public LinkedList<Author> updateNodes(String query) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            try {
             PreparedStatement pr = conn.prepareStatement("select * from autor " + query);
            LinkedList<Author> list = new LinkedList<Author>();
            ResultSet rs = pr.executeQuery();
            while (rs.next()) {
                list.add(Author.getNodeOfResult(rs).get());
            }
             
            return list;
            } catch (Exception e) {
               e.printStackTrace();
                return null;
            }
        });
    }
    public  Optional<Author> getNode(int code) throws SQLException{
        return  this.updateNodes(String.format("where codigoaut = %s", code)).stream().findFirst();
    }
}
