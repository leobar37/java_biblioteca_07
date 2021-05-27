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
import models.Libro;

/**
 *
 * @author USER
 */
public class LibroController extends BaseController<Libro> {

    private static LibroController controller;

    private LibroController() {
        super();

    }

    public static LibroController instance() {
        if (controller != null) {
            return controller;
        } else {
            controller = new LibroController();

            return controller;
        }
    }

    // CRUD
    public Optional<Libro> updateNode(String code, Libro node) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("UPDATE public.libro\n" +
"SET" +
"     titulo = ? -- character varying\n" +
"    , fechapub = ? -- date NULLABLE\n" +
"    , edicion = ? -- integer\n" +
"    , codigoedi = ? -- integer\n" +
"    , codigoare = ? -- integer\n" +
"WHERE isbn = ? returning *");
        pr.setString(1, node.getTitulo());
        pr.setInt(2, node.getEdicion());
        pr.setInt(3, node.getCodigoEdi());
        pr.setInt(4, node.getCodigoArea());
        pr.setString(5, node.getIsbn());
            return Libro.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public Optional<Libro> deleteNode(String isbn) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("DELETE FROM\n" +
"--ONLY\n" +
"public.libro\n" +
"WHERE isbn = ?");
            pr.setString(1, isbn);
            return Libro.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public Optional<Libro> createNode(Libro node) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO public.libro (\n" +
"      isbn\n" +
"    , titulo\n" +
"    , fechapub\n" +
"    , edicion\n" +
"    , codigoedi\n" +
"    , codigoare\n" +
"    , codautor\n" +
") VALUES (\n" +
"      ? -- isbn character varying PRIMARY KEY\n" +
"    , ? -- titulo character varying\n" +
"    , ? -- fechapub date NULLABLE\n" +
"    , ? -- edicion integer\n" +
"    , ? -- codigoedi integer\n" +
"    , ? -- codigoare integer\n" +
"    , ? -- codautor integer\n" +
") returning *");
            pr.setString(1, node.getIsbn());
            pr.setString(2, node.getTitulo());
            pr.setDate(3, node.getFechaPub());
            pr.setInt(4, node.getEdicion());
            pr.setInt(5, node.getCodigoEdi());
            pr.setInt(6, node.getCodigoArea());
            pr.setInt(7, node.getCodAuthor());
            return Libro.getNodeOfResult(pr.executeQuery(), true);
        });
    }

   
    public  Optional<Libro> getNode(String code) throws SQLException {
        return this.updateNodes(String.format("where isbn = %s", code)).stream().findFirst();
    }

    public LinkedList<Libro> updateNodes(String query) throws SQLException {

        return bdConnection.secureConnection(conn -> {
            try {

                PreparedStatement pr = conn.prepareStatement("select * from libro " + query);
                LinkedList<Libro> list = new LinkedList<Libro>();
                ResultSet rs = pr.executeQuery();
                while (rs.next()) {
                    list.add(Libro.getNodeOfResult(rs).get());
                }

                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
