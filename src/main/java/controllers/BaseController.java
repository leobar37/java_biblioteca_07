/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.LinkedList;

/**
 *
 * @author USER
 */
public class BaseController<T> {

    LinkedList<T> nodes;
    
   protected static  BaseController controller;

    BaseController() {
        this.nodes = new LinkedList<T>();
    }
   
    public LinkedList<T> getNodes() {

        return this.nodes;
    }

}
