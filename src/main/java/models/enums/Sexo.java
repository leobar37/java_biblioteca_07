/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models.enums;

/**
 *
 * @author USER
 */
public enum Sexo {
  MASCULINO('M'),
  FEMENINO('F');
   private final char l;
    Sexo(char sex) {
     this.l = sex;
   }
   
   public char getChar(){
       return  this.l;
   }
  
}
