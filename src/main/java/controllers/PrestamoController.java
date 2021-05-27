/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import ca.krasnay.sqlbuilder.DeleteBuilder;
import ca.krasnay.sqlbuilder.SelectBuilder;
import config.bdConnection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Optional;
import models.Area;
import models.Prestamo;

/**
 *
 * @author USER
 */
public class PrestamoController extends BaseController<Prestamo> {

    private static PrestamoController controller;

    private PrestamoController() {
        super();

    }

    public static PrestamoController instance() {
        if (controller != null) {
            return controller;
        } else {
            controller = new PrestamoController();

            return controller;
        }
    }

    // CRUD
    public Optional<Prestamo> uodateNode(int code, Prestamo prestamo) throws SQLException {
        return bdConnection.transaction(conn -> {
            PreparedStatement pr = conn.prepareStatement("UPDATE public.prestamo\n"
                    + "SET"
                    + "     fechapre = ? -- date\n"
                    + "    , estado = ? -- boolean\n"
                    + "    , estadomora = ? -- boolean\n"
                    + "    , estadomulta = ? -- boolean\n"
                    + "    , dnilector = ? -- character\n"
                    + "WHERE codigopre = ?  returning *");
            int n = 1;

            pr.setDate(n++, prestamo.getFechapre());
            pr.setBoolean(n++, prestamo.isEstado());
            pr.setBoolean(n++, prestamo.isEstadomora());
            pr.setBoolean(n++, prestamo.isEstadomulta());
            pr.setString(n++, prestamo.getDnilector());
            pr.setInt(n++, prestamo.getCodigopre());
            ResultSet resCreatePrestamo = pr.executeQuery();
            // Prestamo.getNodeOfResult(pr.executeQuery(), true);
            // inser books 

            String deleteDetails = new DeleteBuilder("detalle_prestamo").where(String.format("codigopre = %d", code)).toString();
            System.out.println(deleteDetails);
            int respDelete = conn.prepareStatement(deleteDetails).executeUpdate();
             if(respDelete > 0){
                 System.out.println("Delete details succes");
             }
           
            for (Prestamo.LibroView book : prestamo.getDetallePrestamo()) {

              
                    pr = conn.prepareStatement("INSERT INTO public.detalle_prestamo (\n"
                            + "      codigopre\n"
                            + "    , isbn\n"
                            + "    , estadodev\n"
                            + ") VALUES (\n"
                            + "      ? -- codigopre integer PRIMARY KEY\n"
                            + "    , ? -- isbn character varying PRIMARY KEY\n"
                            + "    , ? -- estadodev boolean\n"
                            + ")");
                    n = 1;

                    pr.setInt(n++, prestamo.getCodigopre());
                    pr.setString(n++, book.libro.getIsbn());
                    pr.setBoolean(n++, book.estadoDev);

                    if (pr.executeUpdate() > 0) {
                        System.out.println("Detalle creado correctamente");
                    }
            }

            return Prestamo.getNodeOfResult(resCreatePrestamo, true);
        });
    }

    public Optional<Prestamo> deleteNode(int code) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("delete from area where codigoare = ? returning *");
            pr.setInt(1, code);
            return Prestamo.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public Optional<Prestamo> createNode(Prestamo prestamo) throws SQLException {
        return bdConnection.transaction(conn -> {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO public.prestamo (\n"
                    + "      codigopre\n"
                    + "    , fechapre\n"
                    + "    , estado\n"
                    + "    , estadomora\n"
                    + "    , estadomulta\n"
                    + "    , dnilector\n"
                    + ") VALUES (\n"
                    + "      ? -- codigopre integer PRIMARY KEY\n"
                    + "    , ? -- fechapre date\n"
                    + "    , ? -- estado boolean\n"
                    + "    , ? -- estadomora boolean\n"
                    + "    , ? -- estadomulta boolean\n"
                    + "    , ? -- dnilector character\n"
                    + ") returning *");
            int n = 1;
            pr.setInt(n++, prestamo.getCodigopre());
            pr.setDate(n++, prestamo.getFechapre());
            pr.setBoolean(n++, prestamo.isEstado());
            pr.setBoolean(n++, prestamo.isEstadomora());
            pr.setBoolean(n++, prestamo.isEstadomulta());
            pr.setString(n++, prestamo.getDnilector());
            ResultSet resCreatePrestamo = pr.executeQuery();
            // Prestamo.getNodeOfResult(pr.executeQuery(), true);
            // inser books 
            for (Prestamo.LibroView book : prestamo.getDetallePrestamo()) {
                pr = conn.prepareStatement("INSERT INTO public.detalle_prestamo (\n"
                        + "      codigopre\n"
                        + "    , isbn\n"
                        + "    , estadodev\n"
                        + ") VALUES (\n"
                        + "      ? -- codigopre integer PRIMARY KEY\n"
                        + "    , ? -- isbn character varying PRIMARY KEY\n"
                        + "    , ? -- estadodev boolean\n"
                        + ")");
                n = 1;
                pr.setInt(n++, prestamo.getCodigopre());
                pr.setString(n++, book.libro.getIsbn());
                pr.setBoolean(n++, book.estadoDev);

                if (pr.executeUpdate() > 0) {
                    System.out.println("Detalle creado correctamente");
                }

            }

            return Prestamo.getNodeOfResult(resCreatePrestamo, true);
        });
    }

    public int generateCode() throws SQLException {
        return bdConnection.secureConnection(conn -> {
            try {
                PreparedStatement pr = conn.prepareStatement("select  COALESCE(Max(codigopre) , 0) + 1 as codigo from prestamo");
                ResultSet rs = pr.executeQuery();
                rs.next();
                return rs.getObject("codigo", Integer.class);
            } catch (Exception e) {
                e.printStackTrace();;
                return -1;
            }
        });
    }

    public Optional<Prestamo> getNode(int code) throws SQLException {
        return this.updateNodes(String.format("where codigopre = %d", code)).stream().findFirst();
    }

    public LinkedList<Prestamo> updateNodes(String query) throws SQLException {

        return bdConnection.secureConnection(conn -> {
            try {

                PreparedStatement pr = conn.prepareStatement("select * from prestamo " + query);
                LinkedList<Prestamo> list = new LinkedList<Prestamo>();
                ResultSet rs = pr.executeQuery();
                while (rs.next()) {
                    list.add(Prestamo.getNodeOfResult(rs).get());
                }

                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }
}
