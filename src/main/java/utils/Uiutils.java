/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import javax.swing.JFrame;
import javax.swing.JPasswordField;
import javax.swing.JTable;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author USER
 */
public class Uiutils {
  
   public static boolean  compareFieldsPassword(JPasswordField  field1 , JPasswordField  field2){
        return new String(field1.getPassword()).equals(new String(field2.getPassword()));
   }
   
   public  static  String getPassword(JPasswordField  field){
    return new String(field.getPassword());
   }
   
   public static void openWindow(JFrame wind){
       wind.setLocationRelativeTo(null);
       wind.setVisible(true);
   }
     
   public  static int getPercent(int percent , int width){
       return (int) Math.floor(percent * width / 100);
   }
   public static void setColumnWidths(JTable table, int... widths) {
       TableColumnModel columnModel = table.getColumnModel();
    for (int i = 0; i < widths.length; i++) {
        if (i < columnModel.getColumnCount()) {
            columnModel.getColumn(i).setMaxWidth(widths[i]);
        }
        else break;
    }
}
}
