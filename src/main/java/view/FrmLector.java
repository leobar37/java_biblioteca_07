/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import controllers.ReaderController;

import java.awt.*;

import java.util.LinkedList;

import java.util.function.Consumer;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JTable;


import core.tablemodel.LeTableModel;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import models.Lector;

import net.miginfocom.layout.*;
import net.miginfocom.swing.MigLayout;
import ui.LabelTitle;
import ui.PanerlMantenainceLector;
import globals.Constants;

/**
 *
 * @author USER
 */
public class FrmLector extends javax.swing.JFrame {
    
   
    static FrmLector open(){
       FrmLector lector = new FrmLector();
       lector.setLocationRelativeTo(null);
       lector.setVisible(true);
       return lector;
    }
    
    @FunctionalInterface
    interface  callBacModelTable{
        public void callback(LeTableModel<Lector>  table);
    }
    /**
     * Creates new form FrmLector
     */
  
    JPanel container;

    // right side
    JTable table;

    private ReaderController readerController =  new ReaderController();
    public FrmLector() {

        initComponents();
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        this.instanceCustomComponents();
    }

    private void instanceCustomComponents() {

        this.setSize(Constants.sizeWindow);
        container = new JPanel(new GridLayout( 1 , 2 , 10 , 10));

        container.setSize(new Dimension(Constants.sizeWindow.width - 40 ,  Constants.sizeWindow.height - 80));
        // panel lector
       
        PanerlMantenainceLector panelLector = new PanerlMantenainceLector(this.readerController);
        Consumer<Void>  updateLectors = (e) ->{
            this.addLectosInTable();
        };
        // when lectors have been updated
        panelLector.updateLectorsConsummer = updateLectors;

        panelLector.setPreferredSize(new Dimension(panelLector.getWidth(), Constants.sizeWindow.height));

        container.add(panelLector );

        container.add(this.panelRight());
// add panel
        this.add(container);

        //panel.set
    }

    private  void instanceTable(callBacModelTable callback){
     try {
         String[] columns = { "Dni" , "Nombre" , "Apellido", "Sexo" , "Teléfono", "Vigencia"};

         LeTableModel<Lector> tableModel = new LeTableModel<Lector >( lector -> {
             Object[]  row = {  lector.getDni() , lector.getName() , lector.getLastName() , lector.getSexLabel() , lector.getPhone() ,lector.getLabelVigencia()};
             return  row;
         });
        
         tableModel.addColumns(Arrays.asList(columns));
         
         callback.callback(tableModel);
        
         
         if(table != null){
             table.setModel(tableModel);
         }

     }catch (Exception ex){
         ex.printStackTrace();
     }

    }
    private void addLectosInTable(){
        try {
            LinkedList<Lector> lectors = this.readerController.getLectors();
            System.out.println("lectors obtian");
            instanceTable((model)->{
                System.out.println(" call here");
                lectors.forEach( lector -> {
                     model.addRow(lector);
                });
            });
        }catch (Exception ex){
             // lauch erro here
            System.err.println();

        }
    }
    private JPanel panelRight() {
        MigLayout layout =  new MigLayout("", "[100%]" ,"[][]");

        JPanel panelRight = new JPanel(layout);

        // title
        LabelTitle title = new LabelTitle("Lista de lectores");
        panelRight.add(title , new CC().width("100%").alignX("center").wrap().gapX("10" , "0"));

        table = new JTable();


         this.addLectosInTable();
       


        table.setPreferredSize(new Dimension(600, Constants.sizeWindow.height));

        panelRight.add(table ,  new CC().gapY("10px", "50px"));
        
       return  panelRight;

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1108, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 670, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FrmLector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLector.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmLector().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
