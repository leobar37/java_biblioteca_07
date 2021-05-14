/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;

/**
 *
 * @author USER
 */
public class LabelTitle extends JLabel{
    
    public LabelTitle(String text){
         super(text);
         this.applyStyles();
    }
   
    private void applyStyles(){
         this.setFont(new Font("Arial", Font.BOLD, 25));
         this.setAlignmentX(JLabel.CENTER_ALIGNMENT);
         
    }
    
    
    
    
}
