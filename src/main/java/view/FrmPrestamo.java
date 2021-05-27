/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import TrackerFom.TrackerForm;
import TrackerFom.Validations;
import controllers.LibroController;
import controllers.PrestamoController;
import controllers.ReaderController;
import core.tablemodel.LeTableModel;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import models.Lector;
import models.Libro;
import models.Prestamo;
import utils.Dialogs;
import utils.Uiutils;

/**
 *
 * @author USER
 */
public class FrmPrestamo extends javax.swing.JDialog {

    /**
     * Creates new form FrmPrestamo
     */
    private Prestamo currentNode = null;
    private TrackerForm formTracker = new TrackerForm();
    private InitCamps initCamps;

    private Lector selectLecotor = null;

    private LibroController libroController = LibroController.instance();

    private PrestamoController controller = PrestamoController.instance();

    private ReaderController readerController = new ReaderController();
    
    boolean editMode = false;

    public FrmPrestamo(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.initCamps = new InitCamps();
        
        try {
            this.changueState(this.controller.getNode(8).get());
        } catch (SQLException ex) {
            Logger.getLogger(FrmPrestamo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static FrmPrestamo open() {
        FrmPrestamo frm = new FrmPrestamo(null, false);
        frm.setLocationRelativeTo(null);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
        return frm;
    }

    private boolean isEditMode() {
        return !this.btnSave.getText().equalsIgnoreCase("guardar");
    }

    private void changueState(Prestamo pr) {
        if (pr != null) {
            this.currentNode = pr;
            
            this.btnSave.setText("Guardar cambios");
            this.editMode =  true;
            this.initCamps.fillDetails();
            txtDniLector.setText(pr.getDnilector());
            this.displayLector();
            
        } else {
             this.editMode =  false;
            this.initCamps = new InitCamps();
            this.btnSave.setText("Guardar");
            this.selectLecotor =  null;
            txtIInfoLector.setText("");
        }
    }

    private void clean() {
        this.currentNode = new Prestamo();
        try {
            int code = controller.generateCode();
            txtNumPrestamo.setText(String.valueOf(code));
            this.currentNode.setCodigopre(code);

        } catch (SQLException ex) {
            Logger.getLogger(FrmPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            Dialogs.bderrorMessage();
        }
    }

    class InitCamps {

        public InitCamps() {
            this.initFields();
            this.fillLibros("");

            formTracker.cleanForm();
            clean();
            this.fillDetails();
            Validations.limitOnlyNumbers(txtDniLector, 8);

        }

        public void fillLibros(String query) {
            /*
             private String isbn;
    private String titulo;
    private Date fechaPub;
    private int edicion;
    private int codigoEdi;
    private int codigoArea;
             */
            Object[] columns = {"Isbn", "Titulo", "Edicion", "Editorial", "Area"};
            LeTableModel<Libro> modelo = new LeTableModel<Libro>(node -> {
                return new Object[]{node.getIsbn(), node.getTitulo(), node.getEdicion(), node.getEditorial().getNombre(), node.getArea().getNombre()};
            });
            modelo.addColumns(Arrays.asList(columns));
            try {
                libroController.updateNodes("").forEach(node -> {
                    modelo.addRow(node);
                });
                tableLibros.setModel(modelo);
                Uiutils.setColumnWidths(tableLibros, 60, 270, 80, 120, 80);
            } catch (SQLException ex) {
                Dialogs.bderrorMessage();
            }
        }

        private void initFields() {
            /*
             [] fecha -> poner fecha actual
             [] generar el numero de prestamo
             [] listar libros
             */
            formTracker.addField("fechaPrestamo", fechaPrestamo, Date.class);
            formTracker.addField("dniLector", txtDniLector, String.class);

        }

        public void fillDetails() {
            Object[] columns = {"ISBN", "Titulo"};

            LeTableModel<Prestamo.LibroView> modelo = new LeTableModel<Prestamo.LibroView>(node -> {

                return new Object[]{node.libro.getIsbn(), node.libro.getTitulo()};
            });
            modelo.addColumns(Arrays.asList(columns));

            try {
                currentNode.getDetallePrestamo().forEach(detail -> {
                    modelo.addRow(detail);
                });
                tableDetail.setModel(modelo);
            } catch (Exception e) {
                e.printStackTrace();
                Dialogs.bderrorMessage();
            }

        }

    }

    private Prestamo getValues() {
        /*
          PreparedStatement pr = conn.prepareStatement("INSERT INTO public.prestamo (\n"
                    + "      codigopre\n"
                    + "    , fechapre\n"
                    + "    , estado\n"
                    + "    , estadomora\n"
                    + "    , estadomulta\n"
                    + "    , dnilector\n"
                    + ") VALUES (\n"
                    + "      ? -- codigopre integer PRIMARY KEY\n"
                    + "    , ? -- fechapre date\n"
                    + "    , ? -- estado boolean\n"
                    + "    , ? -- estadomora boolean\n"
                    + "    , ? -- estadomulta boolean\n"
                    + "    , ? -- dnilector character\n"
                    + ") returning *");
         */

        if (currentNode == null) {
            System.err.println("No se ha generado una instacia de prestamo");
            throw new NullPointerException();
        }
        if (this.selectLecotor == null) {
            Dialogs.errorMessage("No se ha seleccionado un lector");
            return null;
        }
        if (this.currentNode.getDetallePrestamo().isEmpty()) {
            Dialogs.errorMessage("Necesita al menos un libro \n"
                    + "en este prestamo para poder guardarse");
            return null;
        }

        Date fecha = (java.sql.Date) this.formTracker.getValue("fechaPrestamo");
        String dniLector = this.selectLecotor.getDni();

        this.currentNode.setDnilector(dniLector);
        this.currentNode.setEstado(true);
        this.currentNode.setEstadomora(false);
        this.currentNode.setEstadomulta(false);
        this.currentNode.setFechapre(fecha);

        return this.currentNode;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        prestamoPopop = new javax.swing.JPopupMenu();
        btnDelete = new javax.swing.JMenuItem();
        form = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        input_nombre = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        fechaPrestamo = new com.toedter.calendar.JDateChooser();
        input_nombre1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNumPrestamo = new javax.swing.JTextField();
        panel_lector = new javax.swing.JPanel();
        datospanel = new javax.swing.JPanel();
        input = new javax.swing.JPanel();
        input_nombre2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtDniLector = new javax.swing.JTextField();
        btnSearchLector = new javax.swing.JButton();
        btnOpenMlector = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtIInfoLector = new javax.swing.JTextArea();
        librospanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        btnLibroMant = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tableLibros = new javax.swing.JTable();
        buttons = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        btnClean = new javax.swing.JButton();
        prestamo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableDetail = new javax.swing.JTable();

        btnDelete.setText("Eliminar");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });
        prestamoPopop.add(btnDelete);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jPanel2.setPreferredSize(new java.awt.Dimension(550, 60));
        jPanel2.setLayout(new java.awt.BorderLayout(80, 0));

        input_nombre.setPreferredSize(new java.awt.Dimension(250, 60));
        input_nombre.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Fecha:");
        input_nombre.add(jLabel2, java.awt.BorderLayout.PAGE_START);
        input_nombre.add(fechaPrestamo, java.awt.BorderLayout.CENTER);

        jPanel2.add(input_nombre, java.awt.BorderLayout.LINE_START);

        input_nombre1.setPreferredSize(new java.awt.Dimension(250, 60));
        input_nombre1.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setText("Número de Prestamo:");
        input_nombre1.add(jLabel3, java.awt.BorderLayout.PAGE_START);

        txtNumPrestamo.setPreferredSize(new java.awt.Dimension(80, 30));
        input_nombre1.add(txtNumPrestamo, java.awt.BorderLayout.PAGE_END);

        jPanel2.add(input_nombre1, java.awt.BorderLayout.LINE_END);

        panel_lector.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        datospanel.setPreferredSize(new java.awt.Dimension(320, 150));

        input.setPreferredSize(new java.awt.Dimension(360, 70));

        input_nombre2.setPreferredSize(new java.awt.Dimension(250, 60));
        input_nombre2.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Dni Lector:");
        input_nombre2.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        txtDniLector.setPreferredSize(new java.awt.Dimension(80, 30));
        input_nombre2.add(txtDniLector, java.awt.BorderLayout.PAGE_END);

        input.add(input_nombre2);

        btnSearchLector.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/searchmagnifierinterfacesymbol1_79893.png"))); // NOI18N
        btnSearchLector.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnSearchLector.setIconTextGap(10);
        btnSearchLector.setMargin(new java.awt.Insets(10, 20, 25, 14));
        btnSearchLector.setPreferredSize(new java.awt.Dimension(40, 40));
        btnSearchLector.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnSearchLector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchLectorActionPerformed(evt);
            }
        });
        input.add(btnSearchLector);

        btnOpenMlector.setText("M. Lector");
        btnOpenMlector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenMlectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout datospanelLayout = new javax.swing.GroupLayout(datospanel);
        datospanel.setLayout(datospanelLayout);
        datospanelLayout.setHorizontalGroup(
            datospanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(datospanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnOpenMlector)
                .addContainerGap(231, Short.MAX_VALUE))
            .addGroup(datospanelLayout.createSequentialGroup()
                .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        datospanelLayout.setVerticalGroup(
            datospanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, datospanelLayout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(input, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnOpenMlector)
                .addContainerGap())
        );

        panel_lector.add(datospanel);

        jPanel5.setPreferredSize(new java.awt.Dimension(300, 150));
        jPanel5.setLayout(new javax.swing.BoxLayout(jPanel5, javax.swing.BoxLayout.LINE_AXIS));

        txtIInfoLector.setColumns(20);
        txtIInfoLector.setRows(5);
        jScrollPane1.setViewportView(txtIInfoLector);

        jPanel5.add(jScrollPane1);

        panel_lector.add(jPanel5);

        librospanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("libros:");
        librospanel.add(jLabel5);

        btnLibroMant.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/libro.png"))); // NOI18N
        btnLibroMant.setPreferredSize(new java.awt.Dimension(50, 50));
        btnLibroMant.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLibroMantActionPerformed(evt);
            }
        });
        librospanel.add(btnLibroMant);

        jPanel3.setMinimumSize(new java.awt.Dimension(500, 100));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 500, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        librospanel.add(jPanel3);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(600, 250));

        tableLibros.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tableLibros.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableLibrosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tableLibros);

        librospanel.add(jScrollPane3);

        buttons.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnSave.setText("Guardar");
        btnSave.setPreferredSize(new java.awt.Dimension(100, 50));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        buttons.add(btnSave);

        jButton3.setText("Prestamos");
        jButton3.setPreferredSize(new java.awt.Dimension(120, 50));
        buttons.add(jButton3);

        btnClean.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/new.png"))); // NOI18N
        btnClean.setPreferredSize(new java.awt.Dimension(50, 50));
        btnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCleanActionPerformed(evt);
            }
        });
        buttons.add(btnClean);

        javax.swing.GroupLayout formLayout = new javax.swing.GroupLayout(form);
        form.setLayout(formLayout);
        formLayout.setHorizontalGroup(
            formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formLayout.createSequentialGroup()
                .addGap(39, 39, 39)
                .addComponent(panel_lector, javax.swing.GroupLayout.PREFERRED_SIZE, 662, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(formLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addGroup(formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(buttons, javax.swing.GroupLayout.PREFERRED_SIZE, 551, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(librospanel, javax.swing.GroupLayout.PREFERRED_SIZE, 625, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(formLayout.createSequentialGroup()
                .addGap(48, 48, 48)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 611, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        formLayout.setVerticalGroup(
            formLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(formLayout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(panel_lector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(librospanel, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(buttons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        jLabel1.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel1.setText("Prestamo:");

        tableDetail.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Titulo", "Cantidad"
            }
        ));
        tableDetail.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableDetailMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableDetail);

        javax.swing.GroupLayout prestamoLayout = new javax.swing.GroupLayout(prestamo);
        prestamo.setLayout(prestamoLayout);
        prestamoLayout.setHorizontalGroup(
            prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prestamoLayout.createSequentialGroup()
                .addGroup(prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, prestamoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(prestamoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        prestamoLayout.setVerticalGroup(
            prestamoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(prestamoLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 494, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(form, javax.swing.GroupLayout.PREFERRED_SIZE, 679, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(prestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(prestamo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(form, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
  
   private String displayLector(Lector lector){
       return  String.format("Nombre: %s", selectLecotor.getName()) + "\n"
                            + String.format("Apellidos : %s", selectLecotor.getLastName()) + "\n"
                            + String.format("Fecha de Nacimiento: %s", selectLecotor.getDateBirthay()) + "\n"
                            + String.format("Dirección : %s", selectLecotor.getDirection()) + "\n"
                            + String.format("Vigencia : %s", selectLecotor.getLabelVigencia()) + "\n"
                            + String.format("Sexo : %s", selectLecotor.getSexLabel()) + "\n";
   }
   
   private void displayLector(){
       if (txtDniLector.getText().trim().length() <= 8) {
            String dni = txtDniLector.getText();
            try {
                Optional<Lector> optLector = this.readerController.getLector(dni);
                if (optLector.isPresent()) {
                    this.selectLecotor = optLector.get();
                    txtIInfoLector.setText(displayLector(selectLecotor));
                } else {
                    Dialogs.successMessage("No existe este lector");
                }
            } catch (SQLException ex) {
                Dialogs.bderrorMessage();
                Logger.getLogger(FrmPrestamo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
   }
   
    private void btnSearchLectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchLectorActionPerformed

     this.displayLector();

    }//GEN-LAST:event_btnSearchLectorActionPerformed

    private void btnOpenMlectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenMlectorActionPerformed
        // TODO add your handling code here:

        FrmLector.open();
    }//GEN-LAST:event_btnOpenMlectorActionPerformed

    private void btnLibroMantActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLibroMantActionPerformed
        FrmLibro.open();
    }//GEN-LAST:event_btnLibroMantActionPerformed

    private void tableLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLibrosMouseClicked

        if (evt.getClickCount() == 2 && evt.getButton() == MouseEvent.BUTTON1) {

            int row = tableLibros.getSelectedRow();
            LeTableModel<Libro> libroModel = (LeTableModel<Libro>) tableLibros.getModel();
            Libro libro = libroModel.get(row);
            if (currentNode.exitLibroinDetail(libro)) {
                Dialogs.errorMessage("Este libro ya ha sido agregado");
                return;
            }

            Prestamo.LibroView libroviw = new Prestamo.LibroView();
            libroviw.estadoDev = false;
            libroviw.libro = libro;

            this.currentNode.addNewdDetailView(libroviw);

            this.initCamps.fillDetails();

        }

    }//GEN-LAST:event_tableLibrosMouseClicked

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed

        try {

            if (this.getValues() != null) {
                if (!isEditMode()) {
                    Optional<Prestamo> optPrestamo = this.controller.createNode(this.getValues());
                           
                    if (optPrestamo.isPresent()) {
                        Dialogs.successMessage("Prestamo correctamente creado");
                        this.changueState(optPrestamo.get());
                    } else {
                        throw new Exception("Error al crear este registo");
                    }
                } else {
                    Optional<Prestamo> optPrestamo = this.controller.uodateNode(currentNode.getCodigopre(), this.getValues());
                    if (optPrestamo.isPresent()) {
                        Dialogs.successMessage("Prestamo correctamente editado");
                          //this.changueState(optPrestamo.get());
                    } else {
                        throw new Exception("Error al editar este registo");
                    }
                }

            }
        } catch (Exception e) {
            if (e instanceof SQLException) {
                Dialogs.bderrorMessage();
                return;
            }
            e.printStackTrace();
            Dialogs.errorMessage(e.getMessage());

        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCleanActionPerformed
        this.changueState(null);
    }//GEN-LAST:event_btnCleanActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
       int row = tableDetail.getSelectedRow();
       LeTableModel<Prestamo.LibroView> model = (LeTableModel<Prestamo.LibroView>) tableDetail.getModel();
       if(model.get(row) != null){
       this.currentNode.removeDetailView(model.get(row));
       
       this.initCamps.fillDetails();
       
       }
    prestamoPopop.setVisible(false);
       
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void tableDetailMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableDetailMouseClicked
      
      if(evt.getButton() == 3){
          
            prestamoPopop.setLocation(evt.getLocationOnScreen());
           prestamoPopop.setVisible(true);
           
        
      }
      
    }//GEN-LAST:event_tableDetailMouseClicked

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
            java.util.logging.Logger.getLogger(FrmPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmPrestamo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmPrestamo dialog = new FrmPrestamo(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton btnClean;
    private javax.swing.JMenuItem btnDelete;
    private javax.swing.JButton btnLibroMant;
    private javax.swing.JButton btnOpenMlector;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnSearchLector;
    private javax.swing.JPanel buttons;
    private javax.swing.JPanel datospanel;
    private com.toedter.calendar.JDateChooser fechaPrestamo;
    private javax.swing.JPanel form;
    private javax.swing.JPanel input;
    private javax.swing.JPanel input_nombre;
    private javax.swing.JPanel input_nombre1;
    private javax.swing.JPanel input_nombre2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPanel librospanel;
    private javax.swing.JPanel panel_lector;
    private javax.swing.JPanel prestamo;
    private javax.swing.JPopupMenu prestamoPopop;
    private javax.swing.JTable tableDetail;
    private javax.swing.JTable tableLibros;
    private javax.swing.JTextField txtDniLector;
    private javax.swing.JTextArea txtIInfoLector;
    private javax.swing.JTextField txtNumPrestamo;
    // End of variables declaration//GEN-END:variables
}
