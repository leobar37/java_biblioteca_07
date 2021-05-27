/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import config.ConnectionCallback;
import config.bdConnection;


import java.sql.Connection;
import java.util.LinkedList;
import java.util.List;
import models.Lector;
import models.Person;
import models.enums.Sexo;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author USER
 */
public class ReaderController {

    LinkedList<Lector> lectors = new LinkedList<Lector>();
    
    //get lector of rs
    private Optional<Lector> getLectorRest(ResultSet res) throws SQLException{
        String dni = res.getObject("dnilector", String.class);
        String name =  res.getObject("nombre", String.class);
        String apellidos = res.getObject("apellidos", String.class);
        Date fechaNac =  res.getObject("fechanac", Date.class );
        String direction = res.getObject("direccion", String.class);
        String phone = res.getObject("telefono", String.class);
        String sex =  res.getObject("sexo" ,String.class);
        Sexo sexo = Person.evaluateSex(sex);
        Lector lector = new Lector(dni, name,direction, phone);
        lector.setLastName(apellidos);
        lector.setDateBirthay(fechaNac);
        lector.setSex(sexo);
        return Optional.of(lector);
    }
    private Optional<Lector> getLectorRest(ResultSet res, boolean resolve) throws SQLException{
        while (res.next()){
            try {
                Optional<Lector> s = this.getLectorRest(res);
                res.close();
                return  s;
            }catch (NullPointerException ex){
                return  Optional.empty();
            }
        }
        res.close();
        return  Optional.empty();
    }

    //add lector
    public Optional<Lector> addLector(Lector lector) throws SQLException {
         /*
         * FIXME:
         * */
        // this code should improve this:  https://examples.javacodegeeks.com/core-java/util/concurrent/atomic/atomicreference/java-atomicreference-example/
        ConnectionCallback<Lector> cc = (Connection connection) -> {
              PreparedStatement pr = connection.prepareStatement("INSERT INTO public.lector (\n" +
                      "      dnilector\n" +
                      "    , nombre\n" +
                      "    , apellidos\n" +
                      "    , fechanac\n" +
                      "    , sexo\n" +
                      "    , vigencia\n" +
                      "    , direccion\n" +
                      "    , telefono\n" +
                      ") VALUES (\n" +
                      "      ? -- dnilector character PRIMARY KEY\n" +
                      "    , ? -- nombre character varying\n" +
                      "    , ? -- apellidos character varying\n" +
                      "    , ? -- fechanac date\n" +
                      "    , ? -- sexo character\n" +
                      "    , ? -- vigencia boolean\n" +
                      "    , ? -- direccion text NULLABLE\n" +
                      "    , ? -- telefono character varying\n" +
                      ") returning *");
              
              pr.setString(1 , lector.getDni());
              pr.setString(2 , lector.getName());
              pr.setString(3, lector.getLastName());
              pr.setDate(4  , lector.getDateBirthay());
              pr.setString(5 , String.valueOf(lector.getSex().getChar()));
              pr.setBoolean(6 , lector.isVigencia());
              pr.setString(7 , lector.getDirection());
              pr.setString(8 , lector.getPhone());
              ResultSet res = pr.executeQuery();
              res.next();
              Lector lectorRe = this.getLectorRest(res).get();
              res.close();
              return lectorRe;
        };
       Lector bdLector = bdConnection.secureConnection(cc);

        return  Optional.of(bdLector);
    }
    //edit lector

    // delete lector
   public Optional<Lector> getLector(String dni) throws SQLException{
       return  bdConnection.secureConnection(conn -> {
            PreparedStatement statement = conn.prepareStatement("select * from lector where dnilector = ?");
            statement.setString(1 , dni);
            ResultSet re = statement.executeQuery();
            return  this.getLectorRest(re,  true);
        });
   }

   // delete Reader
    public  Optional<Lector> deleteLector(String dni) throws  SQLException{
      return bdConnection.secureConnection(conn -> {
         try {
             PreparedStatement  statement = conn.prepareStatement("delete from lector where dnilector = ? returning *");
             statement.setString(1 , dni);
             return  this.getLectorRest(statement.executeQuery() , true);
         }catch(SQLException ex) {
             System.err.println(ex.getMessage());
             return Optional.empty();
        }
      });
    }
    public Optional<Lector> modifyLector(Lector lector) throws SQLException{
        return  bdConnection.secureConnection(conn -> {
            PreparedStatement statement = conn.prepareStatement("UPDATE public.lector\n" +
                    "SET" +
                    "     nombre = ? -- character varying\n" +
                    "    , apellidos = ? -- character varying\n" +
                    "    , fechanac = ? -- date\n" +
                    "    , sexo = ? -- character\n" +
                    "    , vigencia = ? -- boolean\n" +
                    "    , direccion = ? -- text NULLABLE\n" +
                    "    , telefono = ? -- character varying\n" +
                    "WHERE dnilector = ? returning *");

            statement.setString(1 , lector.getName());
            statement.setString(2, lector.getLastName());
            statement.setDate(3  , lector.getDateBirthay());
            statement.setString(4 , String.valueOf(lector.getSex().getChar()));
            statement.setBoolean(5 , lector.isVigencia());
            statement.setString(6 , lector.getDirection());
            statement.setString(7 , lector.getPhone());
            statement.setString(8 , lector.getDni());

            return this.getLectorRest(statement.executeQuery() , true);
        });
    }

    // getLectors
    public LinkedList<Lector> getLectors() throws SQLException{
        lectors = new LinkedList<Lector>();
       /*
       * Article: https://www.baeldung.com/java-void-type
       * */
       ConnectionCallback<LinkedList<Lector>>  callConnection = (Connection conn) -> {
          LinkedList<Lector> lectors2  = new LinkedList<Lector>();
               PreparedStatement statement = conn.prepareStatement("select * from lector");
               ResultSet rs = statement.executeQuery();
               while(rs.next()){

                   lectors2.add(this.getLectorRest(rs).get());
               }
               rs.close();
               return lectors2;
        };
       try {
           this.lectors =  bdConnection.secureConnection(callConnection);
       }catch (SQLException ex){
           System.err.println(ex.getMessage());
       }
        System.out.println(lectors.size());
        return lectors;
    }
    
       // getLectors
    public LinkedList<Lector> getLectors(String query) throws SQLException{
        lectors = new LinkedList<Lector>();
       /*
       * Article: https://www.baeldung.com/java-void-type
       * */
       ConnectionCallback<LinkedList<Lector>>  callConnection = (Connection conn) -> {
          LinkedList<Lector> lectors2  = new LinkedList<Lector>();
               PreparedStatement statement = conn.prepareStatement("select * from lector " +  query);
               ResultSet rs = statement.executeQuery();
               while(rs.next()){

                   lectors2.add(this.getLectorRest(rs).get());
               }
               rs.close();
               return lectors2;
        };
       try {
           this.lectors =  bdConnection.secureConnection(callConnection);
       }catch (SQLException ex){
           System.err.println(ex.getMessage());
       }
        System.out.println(lectors.size());
        return lectors;
    }
    
}
