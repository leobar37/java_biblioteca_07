/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import TrackerFom.TrackerForm;
import TrackerFom.Validations;
import controllers.EditorialController;
import core.tablemodel.LeTableModel;
import java.awt.*;
import javax.swing.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;

import models.Editorial;

import net.miginfocom.swing.MigLayout;

import utils.Dialogs;
import utils.Uiutils;

/**
 *
 * @author USER
 */
public class FrmEditorial extends javax.swing.JFrame {

    JPanel formContainer;

    TrackerForm form = new TrackerForm();

    public static FrmEditorial open() {
        FrmEditorial form = new FrmEditorial();
        form.setLocationRelativeTo(null);
        form.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        form.setVisible(true);
        return form;
    }
    EditorialController editorialController = EditorialController.instance();

    Dimension sizeWindow = new Dimension(650, 250);

    private Editorial currentEditorial = null;

    public FrmEditorial() {
        initComponents();
        this.setSize(new Dimension(sizeWindow.width + 30, sizeWindow.height + 60));
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.setResizable(false);
        this.initCustomComponents();
    }

    private void buiLdField(JComponent left, JComponent right) {
        JPanel panel = new JPanel(new MigLayout("", "[30%][70%]", "[center]"));
        panel.setSize(new Dimension(Uiutils.getPercent(40, sizeWindow.width), 50));
        panel.add(left, "w 20%");
        panel.add(right, "w 80%");
        formContainer.add(panel);
    }

    // codigo
    JTextField textCodigo = new JTextField();
    JLabel lblCodigo = new JLabel("codigo");

    // name
    JTextField txtName = new JTextField();
    JLabel lblNmae = new JLabel("Nombre");

    // Jttoggle
    JToggleButton toggleButton = new JToggleButton();
    JLabel lblVigencia = new JLabel("Vigencia");

    // button
    JButton btnSave = new JButton("Guardar");
    JButton btnClean = new JButton("Limpiar");

    private JPanel buttonSave() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(btnSave);
        panel.add(btnClean);
        this.events();
        return panel;
    }

    private void updateState(Editorial editorial) {
        if (currentEditorial != null) {
            currentEditorial = null;

        } else {
            currentEditorial = editorial;
            this.btnSave.setText("Guardar Cambios");
        }
    }

    private void clean() {
        this.btnSave.setText("Guardar");
        this.form.cleanForm();
        this.updateState(null);
    }

    public void events() {
        // save button
        this.btnSave.addActionListener(event -> {
            Editorial editoria = getValuesForm();
            if (editoria != null) {
                try {

                    if (this.isEditMode()) {
                        Optional<Editorial> editorialopt = this.editorialController.updateNode(this.currentEditorial.getCodigo(), editoria);
                        if (editorialopt.isPresent()) {
                            Dialogs.successMessage("Se ha editado una editorial");
                            this.clean();
                           
                        }
                    } else {
                        Optional<Editorial> editorialopt = this.editorialController.createNode(editoria);
                        if (editorialopt.isPresent()) {
                            Dialogs.successMessage("Se ha creado una nueva editorial");
                            this.updateState(editorialopt.get());
                        }
                    }

                } catch (SQLException ex) {
                    ex.printStackTrace();
                }

            }
        });

        // clean
        this.btnClean.addActionListener(e -> {
            this.clean();
        });

    }

    private Editorial getValuesForm() {
        if (this.form.isValid()) {
            int codigo = (int) this.form.getValue("codigo");
            String name = (String) this.form.getValue("name");
            boolean vigencia = this.toggleButton.isSelected();
            Editorial editorial = new Editorial(name, vigencia, codigo);
            return editorial;

        } else {
            Dialogs.errorMessage(this.form.getError());
            return null;
        }

    }

    private JPanel buildForm() {
        formContainer = new JPanel(new FlowLayout(FlowLayout.CENTER));
        formContainer.setPreferredSize(new Dimension(sizeWindow.width / 2, sizeWindow.height));
        // title

        // codigo
        this.buiLdField(lblCodigo, textCodigo);

        // NAME
        this.buiLdField(lblNmae, txtName);

        // vigencia
        this.buiLdField(lblVigencia, toggleButton);

        // btn save
        formContainer.add(this.buttonSave());

        // tracker form
        form.addField("codigo", textCodigo, Integer.class, val -> val == 0, "El campo codigo es obligaotorio");
        Validations.limitOnlyNumbers(textCodigo, 5);
        form.addField("name", txtName, String.class, val -> Validations.StrinRequied(val), "El campo codigo es obligaotorio");

        return formContainer;
    }

    // table
    JTable table = new JTable();

    private void selectEditoria() {
        int row = table.getSelectedRow();
        LeTableModel<Editorial> m = (LeTableModel<Editorial>) table.getModel();
        this.currentEditorial = m.get(row);
        this.patchValues(currentEditorial);
    }

    private void patchValues(Editorial editorial) {
        this.textCodigo.setText(String.valueOf(editorial.getCodigo()));
        this.txtName.setText(editorial.getNombre());
        this.toggleButton.setSelected(editorial.isVigencia());
        this.btnSave.setText("Guadar cambios");
    }

    private boolean isEditMode() {
        return this.currentEditorial != null;
    }

    private JPanel tableRight() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[left]", "nogrid"));
        panel.setPreferredSize(new Dimension(sizeWindow.width / 2, sizeWindow.height));
        String[] columns = {"Codigo", "Nombre", "vigencia"};

        LeTableModel<Editorial> modelo = new LeTableModel<Editorial>(el -> new Object[]{el.getCodigo(), el.getNombre(), el.isVigencia()});

        modelo.addColumns(Arrays.asList(columns));

        try {
            this.editorialController.updateNodes("").stream().forEach(e -> {
                System.out.println("add row");
                ///table.setModel(model);
                panel.add(table);

                modelo.addRow(e);
            });
            this.table.setSize(new Dimension(450, sizeWindow.height));

            this.table.setModel(modelo);
            // event table

            this.table.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    selectEditoria();
                }
            });
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return panel;
    }

    private void initCustomComponents() {
        JPanel panel = new JPanel(new MigLayout("fillx", "[]rel[]", "[][]"));

        panel.setSize(sizeWindow);

        // add form
        panel.add(this.tableRight(), "cell 1 0");
        panel.add(this.buildForm(), "cell 0 0");
        this.add(panel);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 901, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 654, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(FrmEditorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmEditorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmEditorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmEditorial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FrmEditorial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
