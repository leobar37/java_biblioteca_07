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
import jdk.nashorn.internal.runtime.options.Option;
import models.Editorial;

/**
 *
 * @author USER
 */
public class EditorialController extends BaseController<Editorial>{

    private static EditorialController controller;
    
    private EditorialController(){
        super();
       
    }
    public static EditorialController instance(){
        if(controller != null){
            return controller;
        }else{
            controller = new EditorialController();
            
            return controller;
        }
    }
    
    private Optional<Editorial> getNodeOfResult(ResultSet res) throws SQLException{
        try {
              int codig = res.getObject("codigoedi", Integer.class);
         String nombre =  res.getObject("nombre", String.class);
         boolean vigencia =  res.getObject("vigencia", Boolean.class);
        
         Editorial edi = new Editorial(nombre, vigencia, codig);
         
         return Optional.of(edi);
       
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error en obtener Resultado");
           return Optional.empty();
        }
  
    }
    private Optional<Editorial> getNodeOfResult(ResultSet res, boolean  resolve) throws SQLException{
        try {
          if(res.next()){
              return this.getNodeOfResult(res);
          }else{
              return Optional.empty();
          }
        } catch (Exception e) {
            System.err.println("Error obtainer result with resolver");
           return Optional.empty();
        }
  
    }
  
    
   // CRUD
    
   public Optional<Editorial> updateNode(int code , Editorial node) throws SQLException{
       return bdConnection.secureConnection( conn -> {
              PreparedStatement pr =  conn.prepareStatement("UPDATE public.editorial\n" +
                                                          "SET \n" +
                                                           " nombre = ? -- character varying\n" +
                                                         ", vigencia = ? -- boolean\n" +
                                                          "WHERE codigoedi = ? returning *");
               pr.setString(1, node.getNombre());
               pr.setBoolean(2, node.isVigencia());
               pr.setInt(3, node.getCodigo());       
               
           return this.getNodeOfResult(pr.executeQuery() , true);
       });
   }
    
   public Optional<Editorial>   createNode(Editorial editorial) throws SQLException{
          return  bdConnection.secureConnection( conn -> {
              PreparedStatement pr =  conn.prepareStatement("INSERT INTO public.editorial (\n" +
"      codigoedi\n" +
"    , nombre\n" +
"    , vigencia\n" +
") VALUES (\n" +
"      ? -- codigoedi integer PRIMARY KEY\n" +
"    , ? -- nombre character varying\n" +
"    , ? -- vigencia boolean\n" +
") returning *");
               pr.setInt(1, editorial.getCodigo());
               pr.setString(2, editorial.getNombre());
               pr.setBoolean(3, editorial.isVigencia());
               
           return this.getNodeOfResult(pr.executeQuery() , true);
       });
       
   }
   
   public LinkedList<Editorial> updateNodes()  throws SQLException{
        return bdConnection.secureConnection(conn -> {
          PreparedStatement pr = conn.prepareStatement("select * from editorial");
          LinkedList<Editorial> list = new LinkedList<Editorial>();
           ResultSet rs = pr.executeQuery();
           while(rs.next()){
               list.add(getNodeOfResult(rs).get());
           }
         
          return list;
        }); 
   }
   
    
}
