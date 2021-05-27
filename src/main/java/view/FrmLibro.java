/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import TrackerFom.TrackerForm;
import TrackerFom.Validations;
import controllers.AreaController;
import controllers.AuthorController;
import controllers.EditorialController;
import controllers.LibroController;
import core.tablemodel.LeTableModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListModel;
import models.Area;
import models.Author;
import models.Editorial;
import models.Libro;
import utils.Dialogs;

/**
 *
 * @author USER
 */
public class FrmLibro extends javax.swing.JDialog {

    private TrackerForm formTrack = new TrackerForm();
    private Libro currentNode = null;
    private LibroController controller = LibroController.instance();
    private Editorial selectEditorial;
    private EditorialController controllerEditorial = EditorialController.instance();
    private Area selectArea;
    private AreaController areaController = AreaController.instance();

    private Author selectauthor;
    private AuthorController controllerAuthor = AuthorController.instance();

    private InitCamps initcamps = null;

    public FrmLibro(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.initFields();
    }

    public static FrmLibro open() {
        FrmLibro frm = new FrmLibro(null, false);
        frm.setLocationRelativeTo(null);
        frm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frm.setVisible(true);
        return frm;
    }

    private boolean isEditMode() {
        return this.currentNode != null;
    }

    private Libro getValues() {
        if (!this.formTrack.isValid()) {
            Dialogs.errorMessage(this.formTrack.getError());
            return null;
        }

        String isbn = (String) this.formTrack.getValue("isbn");
        String titulo = (String) this.formTrack.getValue("titulo");
        Date fechPub = (Date) this.formTrack.getValue("fechapub");
        Area area = (Area) this.formTrack.getValue("area");
        int edicion = (int) this.formTrack.getValue("edition");

        if (this.selectEditorial == null) {
            Dialogs.errorMessage("No se ha seleccionado una editorial");
            return null;
        }
        int codEditorial = this.selectEditorial.getCodigo();

        int codArea = area.getCodArea();

        if (selectauthor == null) {
            Dialogs.errorMessage("No se ha seleccionado una Author");
            return null;
        }
        int codeAuthor = this.selectauthor.getCodigo();

        Libro libro = new Libro();
        libro.setIsbn(isbn);
        libro.setTitulo(titulo);
        libro.setFechaPub(fechPub);
        libro.setCodigoArea(codArea);
        libro.setArea(area);
        libro.setCodigoEdi(codEditorial);
        libro.setEditorial(selectEditorial);
        libro.setEdicion(edicion);
        libro.setCodAuthor(codeAuthor);
        libro.setAuthor(this.selectauthor);
        return libro;
    }

    private void initFields() {
        this.formTrack.addField("isbn", txtCodigo, String.class, value -> {
            return Validations.StrinRequied(value);
        }, "El isbn es requerido");
        this.formTrack.addField("titulo", txtTitulo, String.class, value -> {
            return Validations.StrinRequied(value);
        }, "Los apellidos son requerido");
        this.formTrack.addField("fechapub", fechaPub, Date.class);
        this.formTrack.addField("area", cboArea, Area.class);
        this.formTrack.addField("edition", campEdicion, Integer.class, value -> {
            return value < 0 || value == 0;
        }, "La edicion no puede ser negativa o 0");
        this.initcamps = new InitCamps();

    }

    public class InitCamps {

        DefaultListModel modelLiEditorial;
        DefaultListModel modelAuthors;

        public InitCamps() {
            this.fillAreaCbo();
            this.fillEditorial();
            this.fillAuthors();
            this.fillLibros("");
            this.initListeners();
        }

        public void fillAreaCbo() {
            DefaultComboBoxModel modelArea = new DefaultComboBoxModel();
            try {

                areaController.updateNodes("where vigencia = true").forEach(area -> {
                    modelArea.addElement(area);
                });
                if (selectArea != null) {
                    modelArea.setSelectedItem(selectArea);
                }
                cboArea.setModel(modelArea);
            } catch (SQLException ex) {
                ex.printStackTrace();
                Dialogs.bderrorMessage();
            }

        }

        public void fillEditorial() {
            modelLiEditorial = new DefaultListModel();

            try {
                controllerEditorial.updateNodes("").forEach(editorial -> {
                    modelLiEditorial.addElement(editorial);
                });
                
                listEditorial.setModel(modelLiEditorial);
               if(selectEditorial !=  null){
              
                txtEditoria.setText(selectEditorial.getNombre());
               }
            } catch (SQLException ex) {
                ex.printStackTrace();
                Dialogs.bderrorMessage();
            }

        }

        private void initListeners() {
            // listen click editorial
            listEditorial.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    int index = listEditorial.getSelectedIndex();
                    if (index == -1) {
                        System.out.println("index in -1");
                        return;
                    }

                    Editorial obEditorial = (Editorial) modelLiEditorial.get(index);
                    txtEditoria.setText(obEditorial.getNombre());
                    selectEditorial = obEditorial;
                }
            });
            // listner aurhors
            listAuthors.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    int index = listAuthors.getSelectedIndex();
                    Author obAuthor = (Author) modelAuthors.get(index);
                    txtAuthorSearch.setText(obAuthor.completeName());
                    selectauthor = obAuthor;
                }
            });
        }

        public void fillAuthors() {
            modelAuthors = new DefaultListModel() {
                @Override
                public Object getElementAt(int index) {
                    Author obAuthor = (Author) super.getElementAt(index);

                    return obAuthor.getNombre() + " " + obAuthor.getApellidos() + " - " + obAuthor.getPais();
                }

                public Author getElement(int index) {
                    return (Author) super.getElementAt(index);
                }
            };
            try {
                controllerAuthor.updateNodes("").forEach(author -> {
                    modelAuthors.addElement(author);
                });
                listAuthors.setModel(modelAuthors);
                if(selectauthor != null){
                    txtAuthorSearch.setText(selectauthor.completeName());
                }
            } catch (SQLException ex) {
                Logger.getLogger(FrmLibro.class.getName()).log(Level.SEVERE, null, ex);
                Dialogs.bderrorMessage();
            }

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
            Object[] columns = {"Isbn", "Titulo", "Fecha", "Edicion", "Editorial", "Area"};
            LeTableModel<Libro> modelo = new LeTableModel<Libro>(node -> {

                return new Object[]{node.getIsbn(), node.getTitulo(), node.getFechaPub(), node.getEdicion(), node.getEditorial().getNombre(), node.getArea().getNombre()};
            });
            modelo.addColumns(Arrays.asList(columns));
            try {
                controller.updateNodes("").forEach(node -> {
                    modelo.addRow(node);
                });
                tableLibros.setModel(modelo);
            } catch (SQLException ex) {
                Dialogs.bderrorMessage();
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        container = new javax.swing.JPanel();
        Area = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        form = new javax.swing.JPanel();
        input_isbn = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        codigo = new javax.swing.JPanel();
        txtCodigo = new javax.swing.JTextField();
        btnGenerateCode = new javax.swing.JButton();
        input_titulo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtTitulo = new javax.swing.JTextField();
        input_publicacion = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        fechaPub = new com.toedter.calendar.JDateChooser();
        input_edicion = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        campEdicion = new javax.swing.JSpinner();
        input_edicion1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        cboArea = new javax.swing.JComboBox<>();
        input_Editorial = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        txtEditoria = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        listEditorial = new javax.swing.JList<>();
        input_Area = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        txtAuthorSearch = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        listAuthors = new javax.swing.JList<>();
        buttons = new javax.swing.JPanel();
        btnSave = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        table_pane = new javax.swing.JPanel();
        lblTotalArea = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableLibros = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        container.setLayout(new java.awt.GridLayout(1, 2, 10, 25));

        Area.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 20));

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 18)); // NOI18N
        jLabel5.setText("Mantenimiento de Libro");
        Area.add(jLabel5);

        form.setPreferredSize(new java.awt.Dimension(400, 800));
        form.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        input_isbn.setPreferredSize(new java.awt.Dimension(350, 75));
        input_isbn.setLayout(new java.awt.BorderLayout(6, 6));

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setText("Isbn:");
        input_isbn.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        codigo.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 8, 5));

        txtCodigo.setPreferredSize(new java.awt.Dimension(100, 30));
        codigo.add(txtCodigo);

        btnGenerateCode.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/searchmagnifierinterfacesymbol1_79893.png"))); // NOI18N
        btnGenerateCode.setPreferredSize(new java.awt.Dimension(35, 35));
        codigo.add(btnGenerateCode);

        input_isbn.add(codigo, java.awt.BorderLayout.CENTER);

        form.add(input_isbn);

        input_titulo.setPreferredSize(new java.awt.Dimension(350, 60));
        input_titulo.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel2.setText("Titulo:");
        input_titulo.add(jLabel2, java.awt.BorderLayout.PAGE_START);

        txtTitulo.setPreferredSize(new java.awt.Dimension(80, 30));
        input_titulo.add(txtTitulo, java.awt.BorderLayout.PAGE_END);

        form.add(input_titulo);

        input_publicacion.setPreferredSize(new java.awt.Dimension(350, 60));
        input_publicacion.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel6.setText("F. Publicación");
        input_publicacion.add(jLabel6, java.awt.BorderLayout.PAGE_START);
        input_publicacion.add(fechaPub, java.awt.BorderLayout.CENTER);

        form.add(input_publicacion);

        input_edicion.setPreferredSize(new java.awt.Dimension(350, 60));
        input_edicion.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setText("Edicion:");
        input_edicion.add(jLabel7, java.awt.BorderLayout.PAGE_START);
        input_edicion.add(campEdicion, java.awt.BorderLayout.CENTER);

        form.add(input_edicion);

        input_edicion1.setPreferredSize(new java.awt.Dimension(350, 60));
        input_edicion1.setLayout(new java.awt.BorderLayout(10, 10));

        jLabel10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel10.setText("Area:");
        input_edicion1.add(jLabel10, java.awt.BorderLayout.PAGE_START);

        cboArea.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        input_edicion1.add(cboArea, java.awt.BorderLayout.PAGE_END);

        form.add(input_edicion1);

        input_Editorial.setPreferredSize(new java.awt.Dimension(350, 150));
        input_Editorial.setLayout(new java.awt.BorderLayout(10, 8));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setText("Editorial:");
        input_Editorial.add(jLabel8, java.awt.BorderLayout.PAGE_START);

        txtEditoria.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel1.add(txtEditoria);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(350, 80));

        listEditorial.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(listEditorial);

        jPanel1.add(jScrollPane2);

        input_Editorial.add(jPanel1, java.awt.BorderLayout.CENTER);

        form.add(input_Editorial);

        input_Area.setPreferredSize(new java.awt.Dimension(350, 150));
        input_Area.setLayout(new java.awt.BorderLayout(10, 8));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setText("Author:");
        input_Area.add(jLabel9, java.awt.BorderLayout.PAGE_START);

        txtAuthorSearch.setPreferredSize(new java.awt.Dimension(350, 25));
        jPanel2.add(txtAuthorSearch);

        jScrollPane3.setPreferredSize(new java.awt.Dimension(350, 80));

        listAuthors.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listAuthors);

        jPanel2.add(jScrollPane3);

        input_Area.add(jPanel2, java.awt.BorderLayout.CENTER);

        form.add(input_Area);

        buttons.setPreferredSize(new java.awt.Dimension(400, 60));

        btnSave.setText("Guardar");
        btnSave.setPreferredSize(new java.awt.Dimension(200, 45));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });
        buttons.add(btnSave);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/clean.png"))); // NOI18N
        jButton2.setPreferredSize(new java.awt.Dimension(40, 40));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        buttons.add(jButton2);

        form.add(buttons);

        Area.add(form);

        container.add(Area);

        table_pane.setPreferredSize(new java.awt.Dimension(500, 510));
        table_pane.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));

        lblTotalArea.setText("libros:");
        table_pane.add(lblTotalArea);

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
        jScrollPane1.setViewportView(tableLibros);

        table_pane.add(jScrollPane1);

        container.add(table_pane);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(container, javax.swing.GroupLayout.PREFERRED_SIZE, 990, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(container, javax.swing.GroupLayout.DEFAULT_SIZE, 838, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void patchValues(Libro node) {
        txtCodigo.setText(node.getIsbn());
        txtTitulo.setText(node.getTitulo());
        fechaPub.setDate(node.getFechaPub());
        campEdicion.setValue(node.getEdicion());
        this.selectArea = node.getArea();
        this.selectEditorial = node.getEditorial();
        this.selectauthor = node.getAuthor();

        this.initcamps.fillAreaCbo();
        this.initcamps.fillAuthors();
        this.initcamps.fillEditorial();

    }

    private void changueState(Libro node) {
        if (node == null) {
            this.btnSave.setText("Guardar");
            this.currentNode = null;
            this.formTrack.cleanForm();
            this.selectArea =  null;
            this.selectEditorial = null;
            this.selectauthor = null;
            txtEditoria.setText("");
            txtAuthorSearch.setText("");

        } else {
            this.btnSave.setText("Guardar cambios");
            this.patchValues(node);
            this.currentNode = node;

        }
    }
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        // TODO add your handling code here:
        if (this.getValues() != null) {
            try {
                if (!isEditMode()) {
                    Optional<Libro> optNode = this.controller.createNode(this.getValues());
                    if (optNode.isPresent()) {
                        Dialogs.successMessage("El libro se ha creado correctamente");
                        this.changueState(optNode.get());
                        this.initcamps.fillLibros("ORDER BY fechapub ASC");
                    }else{
                        throw new Exception("No hemos podido guardar el registro");
                    }
                    

                } else {
                    Optional<Libro> optNode = this.controller.updateNode(this.currentNode.getIsbn(), this.getValues());
                    if (optNode.isPresent()) {
                        Dialogs.successMessage("El libro se ha editado correctamente");
                        this.initcamps.fillLibros("ORDER BY fechapub ASC");
                        this.changueState(optNode.get());
                    }else{
                          throw new Exception("No hemos podido editar el registro");
                    }
                  
                }

            } catch (Exception e) {
                if (e instanceof SQLException) {
                    e.printStackTrace();
                    Dialogs.bderrorMessage();
                } else {
                    e.printStackTrace();
                    Dialogs.errorMessage(e.getMessage());
                }

            }
        }

    }//GEN-LAST:event_btnSaveActionPerformed

    private void tableLibrosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableLibrosMouseClicked
       int key = tableLibros.getSelectedRow();
       LeTableModel<Libro> model = (LeTableModel<Libro>) tableLibros.getModel();
       this.currentNode =  model.get(key);
       this.changueState(currentNode);
    }//GEN-LAST:event_tableLibrosMouseClicked

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        this.changueState(null);
    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(FrmLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FrmLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FrmLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FrmLibro.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                FrmLibro dialog = new FrmLibro(new javax.swing.JFrame(), true);
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
    private javax.swing.JPanel Area;
    private javax.swing.JButton btnGenerateCode;
    private javax.swing.JButton btnSave;
    private javax.swing.JPanel buttons;
    private javax.swing.JSpinner campEdicion;
    private javax.swing.JComboBox<String> cboArea;
    private javax.swing.JPanel codigo;
    private javax.swing.JPanel container;
    private com.toedter.calendar.JDateChooser fechaPub;
    private javax.swing.JPanel form;
    private javax.swing.JPanel input_Area;
    private javax.swing.JPanel input_Editorial;
    private javax.swing.JPanel input_edicion;
    private javax.swing.JPanel input_edicion1;
    private javax.swing.JPanel input_isbn;
    private javax.swing.JPanel input_publicacion;
    private javax.swing.JPanel input_titulo;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblTotalArea;
    private javax.swing.JList<String> listAuthors;
    private javax.swing.JList<String> listEditorial;
    private javax.swing.JTable tableLibros;
    private javax.swing.JPanel table_pane;
    private javax.swing.JTextField txtAuthorSearch;
    private javax.swing.JTextField txtCodigo;
    private javax.swing.JTextField txtEditoria;
    private javax.swing.JTextField txtTitulo;
    // End of variables declaration//GEN-END:variables
}
