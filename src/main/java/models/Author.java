/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author USER
 */
public class Author {

    private int codigo;
    private String nombre,apellidos, pais;
    private Date fechaNac;

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }
    
    public String completeName(){
        return this.getNombre() + " " + this.getApellidos();
    }
    
      public static Optional<Author> getNodeOfResult(ResultSet res) throws SQLException{
        try {
          int codarea = res.getInt("codigoaut");
          String nombre = res.getObject("nombre", String.class);
           String apellidos = res.getObject("apellidos", String.class);
           String pais = res.getObject("pais", String.class);
           Date fechNac = res.getObject("fechanac", Date.class);
          
           Author objAuthor =  new Author();
            objAuthor.setNombre(nombre);
            objAuthor.setApellidos(apellidos);
            objAuthor.setPais(pais);
            objAuthor.setFechaNac(fechNac);
            objAuthor.setCodigo(codarea);
           return Optional.of(objAuthor);
        } catch (Exception e) {
            e.printStackTrace();
           return Optional.empty();
        }
    }
     public static Optional<Author> getNodeOfResult(ResultSet res, boolean  resolve) throws SQLException{
        try {
          if(res.next()){
              return Author.getNodeOfResult(res);
          }else{
              return Optional.empty();
          }
        } catch (Exception e) {
            System.err.println("Error obtainer result with resolver");
           return Optional.empty();
        }
  
    }
    
     
}
