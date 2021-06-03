/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import TrackerFom.TrackerForm;

import controllers.UsuarioController;
import globals.Constants;
import java.sql.SQLException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import models.User;
import utils.Dialogs;
import utils.Hash;
import utils.Uiutils;

public class LoguinForm extends javax.swing.JDialog {

    TrackerForm formTracker = new TrackerForm();
    UsuarioController controller = UsuarioController.instance();

    public LoguinForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
        initComponents();
    }

    private void initFields() {
        this.formTracker.addField("usuario", txtUser, String.class, user -> {
            return user.isEmpty();
        }, "El usuario es Requerido");

    }

    public static LoguinForm open() {
        LoguinForm form = new LoguinForm(null, false);
        form.setLocationRelativeTo(null);
        form.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        form.setVisible(true);

        return form;
    }

    private void clean() {
        this.formTracker.cleanForm();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panel = new javax.swing.JPanel();
        loguin = new javax.swing.JPanel();
        form = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CONT_INPUT = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        txtUser = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        CONT_PASSWORD = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtPasssword = new javax.swing.JPasswordField();
        jPanel3 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        image = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        loguin.setPreferredSize(new java.awt.Dimension(436, 50));

        form.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 25));

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Ingreso");
        jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        form.add(jLabel1);

        CONT_INPUT.setPreferredSize(new java.awt.Dimension(300, 70));

        jPanel1.setPreferredSize(new java.awt.Dimension(250, 60));
        jPanel1.setLayout(new java.awt.BorderLayout(0, 10));

        txtUser.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txtUser.setMinimumSize(new java.awt.Dimension(14, 10));
        txtUser.setPreferredSize(new java.awt.Dimension(10, 10));
        txtUser.setRequestFocusEnabled(false);
        jPanel1.add(txtUser, java.awt.BorderLayout.CENTER);

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("usuario");
        jLabel2.setToolTipText("");
        jLabel2.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel2.setIconTextGap(1);
        jLabel2.setPreferredSize(new java.awt.Dimension(20, 16));
        jPanel1.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        CONT_INPUT.add(jPanel1);

        form.add(CONT_INPUT);

        CONT_PASSWORD.setPreferredSize(new java.awt.Dimension(300, 75));
        CONT_PASSWORD.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        jPanel2.setPreferredSize(new java.awt.Dimension(250, 70));
        jPanel2.setLayout(new java.awt.BorderLayout(0, 10));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Contraseña");
        jLabel3.setToolTipText("");
        jLabel3.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel3.setIconTextGap(1);
        jLabel3.setPreferredSize(new java.awt.Dimension(20, 16));
        jPanel2.add(jLabel3, java.awt.BorderLayout.PAGE_START);
        jPanel2.add(txtPasssword, java.awt.BorderLayout.CENTER);

        CONT_PASSWORD.add(jPanel2);

        form.add(CONT_PASSWORD);

        jButton1.setText("Ingresar");
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setPreferredSize(new java.awt.Dimension(100, 35));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton1);

        form.add(jPanel3);

        jLabel5.setText("Olvide mi contraseña");

        javax.swing.GroupLayout loguinLayout = new javax.swing.GroupLayout(loguin);
        loguin.setLayout(loguinLayout);
        loguinLayout.setHorizontalGroup(
            loguinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loguinLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(loguinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loguinLayout.createSequentialGroup()
                        .addComponent(form, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(38, 38, 38))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, loguinLayout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addGap(93, 93, 93))))
        );
        loguinLayout.setVerticalGroup(
            loguinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(loguinLayout.createSequentialGroup()
                .addGap(140, 140, 140)
                .addComponent(form, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32)
                .addComponent(jLabel5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        image.setIcon(new javax.swing.ImageIcon(getClass().getResource("/bgfondo.png"))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        jLabel4.setText("Sistema de Biblioteca");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(image)
                .addContainerGap(33, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 307, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(112, 112, 112))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(50, 50, 50)
                .addComponent(image, javax.swing.GroupLayout.PREFERRED_SIZE, 387, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(64, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelLayout = new javax.swing.GroupLayout(panel);
        panel.setLayout(panelLayout);
        panelLayout.setHorizontalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(loguin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        panelLayout.setVerticalGroup(
            panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLayout.createSequentialGroup()
                .addGroup(panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(loguin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 591, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        if (this.formTracker.isValid()) {
            try {
                //frmPrincipal.open();
                String username = txtUser.getText();
                String passsword = Uiutils.getPassword(txtPasssword);

                Optional<User> validateUser = controller.validateUser(passsword, username);

                if (validateUser.isPresent()) {
                    frmPrincipal.open();
                    this.dispose();

                } else {
                    Dialogs.errorMessage("Credenciales incorrectas");
                }

            } catch (SQLException ex) {
                Logger.getLogger(LoguinForm.class.getName()).log(Level.SEVERE, null, ex);
            }

        } else {
            Dialogs.errorMessage(this.formTracker.getError());
        }

        /*     try {
            String userName= txtUser.getText();
            String password=  Uiutils.getPassword(txtPasssword);
            Optional<User>  useropt = ControllerUser.validateUser(userName, password);
            if(useropt.isPresent()){
                // success  login
            }else{
                // show proper error
            }
            
        } catch (SQLException ex) {
           
            //  display message error bd
            
        }
         */

    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(LoguinForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoguinForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoguinForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoguinForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                LoguinForm dialog = new LoguinForm(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CONT_INPUT;
    private javax.swing.JPanel CONT_PASSWORD;
    private javax.swing.JPanel form;
    private javax.swing.JLabel image;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel loguin;
    private javax.swing.JPanel panel;
    private javax.swing.JPasswordField txtPasssword;
    private javax.swing.JTextField txtUser;
    // End of variables declaration//GEN-END:variables
}
