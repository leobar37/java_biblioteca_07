/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package globals;

import java.awt.Dimension;

/**
 *
 * @author USER
 */
// bd 

public  class Constants{
  
    // configuration bd
   public final static String USER = "postgres";
   public  final static String password = "alfk3458";
   public final  static String  bd = "biblioteca";
   public final static String   host = "localhost:5432";
   // alternatives SHA1 - MD5
   public final static String  ALGORTIH_PASSWORD  = "MD5";
   
   public final static Dimension sizeWindow = new Dimension(1100, 700);

   public static final int HEIGHTINPUT = 38;
   public  static final Dimension fieldDimesion =  new Dimension(500 , HEIGHTINPUT + 20);
   private Constants(){}
}
