/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import ca.krasnay.sqlbuilder.SelectBuilder;
import config.bdConnection;
import controllers.AreaController;
import controllers.AuthorController;
import controllers.EditorialController;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

/**
 *
 * @author USER
 */


public class Prestamo {
    public static class LibroView{
       public Libro libro;
       public boolean estadoDev;
       public int  cod_prestamo;
    };
    private int codigopre;
    private Date fechapre;
    private boolean estado;
    private boolean estadomora;
    private boolean estadomulta;
    private String dnilector;
    // list -> libro estado de devolucion
    private ArrayList<LibroView>  detallePrestamo = new ArrayList<LibroView>();

    public ArrayList<LibroView> getDetallePrestamo() {
        return detallePrestamo;
    }

    public void setDetallePrestamo(ArrayList<LibroView> detallePrestamo) {
        this.detallePrestamo = detallePrestamo;
    }
    
    public void addNewdDetailView(LibroView  node){
        node.cod_prestamo=  this.getCodigopre();
         this.detallePrestamo.add(node);
    }
    public void removeDetailView(LibroView  node){
         this.detallePrestamo.removeIf(node2 -> {
             return node2.libro.getIsbn().equalsIgnoreCase(node.libro.getIsbn());
         });
    }
    
    public boolean  exitLibroinDetail(Libro libro){
       
        return detallePrestamo.stream().filter(node -> node.libro.getIsbn().equalsIgnoreCase(libro.getIsbn())).toArray().length> 0;
    }
    
    
    public int getCodigopre() {
        return codigopre;
    }

    public void setCodigopre(int codigopre) {
        this.codigopre = codigopre;
    }

    public Date getFechapre() {
        return fechapre;
    }

    public void setFechapre(Date fechapre) {
        this.fechapre = fechapre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isEstadomora() {
        return estadomora;
    }

    public void setEstadomora(boolean estadomora) {
        this.estadomora = estadomora;
    }

    public boolean isEstadomulta() {
        return estadomulta;
    }

    public void setEstadomulta(boolean estadomulta) {
        this.estadomulta = estadomulta;
    }

    public String getDnilector() {
        return dnilector;
    }

    public void setDnilector(String dnilector) {
        this.dnilector = dnilector;
    }
    
       public static Optional<Prestamo> getNodeOfResult(ResultSet res) throws SQLException {
        try {
             int codigopre  = res.getInt("codigopre");
             Date fechapre = res.getDate("fechapre");
             boolean estado =  res.getBoolean("estado");
             boolean estadomora = res.getBoolean("estadomora");
             boolean estadomulta =  res.getBoolean("estadomulta");
             String dnil_lector = res.getString("dnilector");
               
             
             // librries
           String query = new  SelectBuilder().column("codigopre").column("estadodev").column("isbn")
                                               .from("detalle_prestamo")
                                               .where(String.format("codigopre = %d", codigopre)).toString();
             System.out.println("execute query");
          ArrayList<LibroView> libros_prestamo =  bdConnection.transaction(connection -> {
              try {
                ResultSet respose = connection.prepareStatement(query).executeQuery();
                ArrayList<LibroView> details = new ArrayList<LibroView>();
                  System.out.println("callback");
                  System.out.println(query);
                while(respose.next()){
                    boolean estadodev =  respose.getBoolean("estadodev");
                  
                    String isbn  = respose.getString("isbn");
                    String selectBook = new SelectBuilder().column("*").from("libro").where(String.format("isbn = '%s'", isbn)).toString();
                    System.out.println(selectBook);
                    ResultSet respLibro =   connection.prepareStatement(selectBook).executeQuery();
                    // obtain boox
                    Optional<Libro> optLibro =  Libro.getNodeOfResult(respLibro, true);
                    
                    Prestamo.LibroView viewLibro = new LibroView();
                    
                    viewLibro.cod_prestamo = codigopre;
                    viewLibro.libro =  optLibro.get();
                    viewLibro.estadoDev = estadodev;
          
                    details.add(viewLibro);
                }
                
                return details;
              } catch (SQLException e) {
                  e.printStackTrace();
               return null;
              }
            });
           
          Prestamo pr = new Prestamo();
          pr.setCodigopre(codigopre);
          pr.setFechapre(fechapre);
          pr.setEstado(estado);
          pr.setEstadomora(estadomora);
          pr.setEstadomulta(estadomulta);
          pr.setDnilector(dnil_lector);
          pr.setDetallePrestamo(libros_prestamo);
           
            
             return Optional.of(pr);
            
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Prestamo> getNodeOfResult(ResultSet res, boolean resolve) throws SQLException {
        try {
            if (res.next()) {
                return Prestamo.getNodeOfResult(res);
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
