/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

public class User extends Person{
     private String  password,cuestion, rptaCuestion , username;
     private boolean estado;

    public User(String dni, String name, String direction, String phone) {
       super(dni, name, direction, phone);
        
    }
    
  
    public User(String dni, String name, String direction, String phone, String password, String cuestion, String rptaCuestion, boolean  estado) {
        super(dni, name, direction, phone);
        this.password = password;
        this.cuestion = cuestion;
        this.rptaCuestion = rptaCuestion;
        this.estado =  estado;
    }
  
    public String getPassword() {
        return password;
    }

    public String getCuestion() {
        return cuestion;
    }

    public String getRptaCuestion() {
        return rptaCuestion;
    }

    public String getUsername() {
        return username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCuestion(String cuestion) {
        this.cuestion = cuestion;
    }

    public void setRptaCuestion(String rptaCuestion) {
        this.rptaCuestion = rptaCuestion;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

 
}
