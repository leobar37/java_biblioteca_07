/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import controllers.AreaController;
import controllers.AuthorController;
import controllers.EditorialController;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author USER
 */
public class Libro {

    private String isbn;
    private String titulo;
    private Date fechaPub;
    private int edicion;
    private int codigoEdi;
    private int codigoArea;
    private int codAuthor;
    private Author author;

    public int getCodAuthor() {
        return codAuthor;
    }

    public void setCodAuthor(int codAuthor) {
        this.codAuthor = codAuthor;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }
    private Editorial editorial;
    private Area area;

    public Editorial getEditorial() {
        return editorial;
    }

    public void setEditorial(Editorial editorial) {
        this.editorial = editorial;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(Date fechaPub) {
        this.fechaPub = fechaPub;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

 

    public int getCodigoEdi() {
        return codigoEdi;
    }

    public void setCodigoEdi(int codigoEdi) {
        this.codigoEdi = codigoEdi;
    }

    public int getCodigoArea() {
        return codigoArea;
    }

    public void setCodigoArea(int codigoArea) {
        this.codigoArea = codigoArea;
    }
    

    public static Optional<Libro> getNodeOfResult(ResultSet res) throws SQLException {
        try {
            String isbn = res.getString("isbn");
            String titulo = res.getString("titulo");
            Date fechSub = res.getDate("fechapub");
            int edicion =  res.getInt("edicion");
            int codigoEditorial = res.getInt("codigoedi");
            int codigoArea = res.getInt("codigoare");
            int codAuthor = res.getInt("codautor");
             Libro obLibro = new Libro();
             obLibro.setIsbn(isbn);
             
           Optional<Area>  optArea =  AreaController.instance().getNode(codigoArea);
           
             obLibro.setCodigoArea(codigoArea);
             obLibro.setArea(optArea.get());
             obLibro.setFechaPub(fechSub);
             obLibro.setTitulo(titulo);
            
             Optional<Editorial>  editorial =  EditorialController.instance().getNode(codigoEditorial);
             obLibro.setCodigoEdi(codigoEditorial);
             
             obLibro.setEditorial(editorial.get());
             
             Optional<Author>  author = AuthorController.instance().getNode(codAuthor);
              obLibro.setAuthor(author.get());
              obLibro.setCodAuthor(codAuthor);
             
             obLibro.setEdicion(edicion);
             return Optional.of(obLibro);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Libro> getNodeOfResult(ResultSet res, boolean resolve) throws SQLException {
        try {
            if (res.next()) {
                return Libro.getNodeOfResult(res);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error obtainer result with resolver");
            return Optional.empty();
        }

    }

}
