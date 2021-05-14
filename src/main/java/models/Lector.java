/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import models.enums.Sexo;

/**
 *
 * @author USER
 */
public class Lector extends Person{
    
    private boolean  vigencia;

    public boolean isVigencia() {
        return vigencia;
    }

    public void setVigencia(boolean vigencia) {
        this.vigencia = vigencia;
    }

    
    public Lector(String dni, String name, String direction, String phone) {
        super(dni, name, direction, phone);
    }

    public  String getLabelVigencia(){
        return  this.isVigencia() == true ? "Activo" : "inactivo";
    }


    
}
