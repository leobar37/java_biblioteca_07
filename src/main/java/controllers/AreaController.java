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
import models.Editorial;
import models.Area;

/**
 *
 * @author USER
 */
public class AreaController extends BaseController<Area> {

    private static AreaController controller;

    private AreaController() {
        super();

    }

    public static AreaController instance() {
        if (controller != null) {
            return controller;
        } else {
            controller = new AreaController();

            return controller;
        }
    }

    // CRUD
    public Optional<Area> updateNode(int code, Area node) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("UPDATE public.area\n"
                    + "SET"
                    + "    nombre = ? -- character varying\n"
                    + "    , vigencia = ? -- boolean\n"
                    + "WHERE codigoare = ?  returning *");
            pr.setString(1, node.getNombre());
            pr.setBoolean(2, node.isVigencia());
            pr.setInt(3, node.getCodArea());

            return Area.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public Optional<Area> deleteNode(int code) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("delete from area where codigoare = ? returning *");
            pr.setInt(1, code);
            return Area.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public Optional<Area> createNode(Area objArea) throws SQLException {
        return bdConnection.secureConnection(conn -> {
            PreparedStatement pr = conn.prepareStatement("INSERT INTO public.area (\n"
                    + "      codigoare\n"
                    + "    , nombre\n"
                    + "    , vigencia\n"
                    + ") VALUES (\n"
                    + "      ? -- codigoare integer PRIMARY KEY\n"
                    + "    , ? -- nombre character varying\n"
                    + "    , ? -- vigencia boolean\n"
                    + ") returning *");
            pr.setInt(1, objArea.getCodArea());
            pr.setString(2, objArea.getNombre());
            pr.setBoolean(3, objArea.isVigencia());

            return Area.getNodeOfResult(pr.executeQuery(), true);
        });
    }

    public int generateCode() throws SQLException {
        return bdConnection.secureConnection(conn -> {
            try {
                PreparedStatement pr = conn.prepareStatement("select  COALESCE(Max(codigoare) , 0) + 1 as codigo from area");
                ResultSet rs = pr.executeQuery();
                rs.next();
                return rs.getObject("codigo", Integer.class);
            } catch (Exception e) {
                e.printStackTrace();;
                return -1;
            }
        });
    }

    public  Optional<Area> getNode(int code) throws SQLException {
        return this.updateNodes(String.format("where codigoare = %s", code)).stream().findFirst();
    }

    public LinkedList<Area> updateNodes(String query) throws SQLException {

        return bdConnection.secureConnection(conn -> {
            try {

                PreparedStatement pr = conn.prepareStatement("select * from area " + query);
                LinkedList<Area> list = new LinkedList<Area>();
                ResultSet rs = pr.executeQuery();
                while (rs.next()) {
                    list.add(Area.getNodeOfResult(rs).get());
                }

                return list;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
