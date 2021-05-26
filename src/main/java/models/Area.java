/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

/**
 *
 * @author USER
 */
public class Area {
 
   private int codArea;
   private String nombre;
   private boolean vigencia;

    public Area(int codArea, String nombre, boolean vigencia) {
        this.codArea = codArea;
        this.nombre = nombre;
        this.vigencia = vigencia;
    }

    public int getCodArea() {
        return codArea;
    }

    public void setCodArea(int codArea) {
        this.codArea = codArea;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

     
    public static Optional<Area> getNodeOfResult(ResultSet res) throws SQLException{
        try {
          int codarea = res.getObject("codigoare", Integer.class);
          String nombre = res.getObject("nombre", String.class);
          boolean vigencia = res.getObject("vigencia", Boolean.class);
          Area  objArea = new Area(codarea, nombre, vigencia);
          return Optional.of(objArea);
        } catch (Exception e) {
           return Optional.empty();
        }
    }
     public static Optional<Area> getNodeOfResult(ResultSet res, boolean  resolve) throws SQLException{
        try {
          if(res.next()){
              return Area.getNodeOfResult(res);
          }else{
              return Optional.empty();
          }
        } catch (Exception e) {
            System.err.println("Error obtainer result with resolver");
           return Optional.empty();
        }
  
    }
   
}
