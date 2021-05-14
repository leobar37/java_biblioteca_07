/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import config.bdConnection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author USER
 */
public class Dialogs {
    
   public  static void  successMessage(String message){
        JOptionPane.showMessageDialog(null, message);   
   }
   public  static void  errorMessage(String message){
        JOptionPane.showMessageDialog(null, message);   
   }
   public  static int confirmMessage(String message, String title){
       return JOptionPane.showConfirmDialog(null , title,message , JOptionPane.YES_NO_CANCEL_OPTION);
   }

    
}
