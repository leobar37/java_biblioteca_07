/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import java.sql.Date;
import models.enums.Sexo;

/**
 *
 * @author USER
 */
public class Person  {

   public static final String[] GENRES = { "Masculino", "Femenino"};


    private String dni,name,direction,phone,lastName;
    private Sexo  sexo;
    private Date dateBirthay;

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Sexo getSexo() {
        return sexo;
    }

    public void setSexo(Sexo sexo) {
        this.sexo = sexo;
    }

    public Date getDateBirthay() {
        return dateBirthay;
    }

    public void setDateBirthay(Date dateBirthay) {
        this.dateBirthay = dateBirthay;
    }
    public static Sexo evaluateSex(String c){
        if(c.equalsIgnoreCase("'F'")){
            return  Sexo.FEMENINO;
        }else {
            return  Sexo.MASCULINO;
        }
    }
    public  int evaluateSex(){
        return  this.sexo == Sexo.FEMENINO ? 1 : 0;
    }
    
    public Sexo getSex() {
        return sexo;
    }

    public void setSex(Sexo sex) {
        this.sexo = sex;
    }
    public  String  getSexLabel(){
        return  this.sexo == Sexo.FEMENINO  ? "Mujer" : "Hombre";
    }

    public Person(String dni, String name, String direction, String phone) {
        this.dni = dni;
        this.name = name;
        this.direction = direction;
        this.phone = phone;
    }

    public String getDni() {
        return dni;
    }

    public String getName() {
        return name;
    }

    public String getDirection() {
        return direction;
    }

    public String getPhone() {
        return phone;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    
}
