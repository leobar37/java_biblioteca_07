/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import TrackerFom.TrackerForm;
import TrackerFom.Validations;
import com.toedter.calendar.JDateChooser;

import java.awt.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.sql.Date;


import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import javax.swing.*;


import controllers.ReaderController;


import globals.Constants;
import models.Lector;
import models.Person;
import models.enums.Sexo;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;
import utils.Alterns;
import utils.Dialogs;
import models.BuildFielCall;

public class PanerlMantenainceLector extends JPanel {

  /*
  * validacione en cuenta:
  * verificar que cuando guarde un lector no tenga uno actual
  * */
    JLabel dnliLabel;
    JTextField dniText;
    // name
    JLabel namLabel;
    JTextField nameText;

    // lastName
    JLabel lastName;
    JTextField lastNameText;

    // fecha nacimiento
    JLabel dateBirthLabel;
    JDateChooser dateBitJCalendar;

    // sexo
    JComboBox<String> cboSex;
    JLabel sexLabel;
  

        // direction
    JLabel directionLbl = new JLabel("Dirección");
    JTextField directionTxt = new JTextField("");

    // vigencia
    JLabel validLbl = new JLabel("vigencia");
    JToggleButton validToggle = new JToggleButton();

    // telefono
    JLabel lblPhone = new JLabel("Télefono");
    JTextField textPhone = new JTextField();

    // constants

      // buttons
    JButton new_  = new JButton("Nuevo");
    JButton delete = new JButton("Eliminar");
    JButton modiFy = new JButton("Modificar");
    JButton cleanForm = new JButton("Limpiar");

    JButton btnConsultLector = new JButton("+");

   // trecker form
    TrackerForm form;

    // current Lector select
    private Lector currentLector = null;

    public ReaderController controllerReader;

    public Consumer<Void> updateLectorsConsummer;

    public PanerlMantenainceLector(ReaderController controller) {
        super();
        // guide : http://www.miglayout.com/QuickStart.pdf

        this.controllerReader = controller;
        if(this.controllerReader == null){
            new Exception("Not found controller");
        }
        this.setLayout(this.miglayout());
        //MaterialPanelUI  panel = new MaterialPanelUI();
       
        this.formInit();
        
        this.setupButtons();
        // listeners
        this.saveButton();
        this.deleteLector();
        // ui events
        this.events();
        this.eventSerachLectorForDni();
        this.modifyLector();
        this.cleanForm();
        
        // test value
       // this.patchValuesTest();


    }
    private void patchValuesTest(){
        this.dniText.setText("98765422");
        this.cboSex.setSelectedIndex(1);
        this.nameText.setText("Elmer Joselito");
        this.lastNameText.setText("Leon Barboza");
        this.validToggle.setSelected(true);
        this.dateBitJCalendar.setDate(Date.valueOf(LocalDate.now()));
        this.directionTxt.setText("Mi House is");
        this.textPhone.setText("987654321");
    }

    private void  patchValuesLector(Lector lector){
       this.dniText.setText(lector.getDni());
       this.cboSex.setSelectedIndex(lector.evaluateSex());
       this.nameText.setText(lector.getName());
       this.lastNameText.setText(lector.getLastName());
       this.textPhone.setText(lector.getPhone());
       this.dateBitJCalendar.setDate(lector.getDateBirthay());
       this.validToggle.setSelected(lector.isVigencia());
       this.updateTextToogle();
       this.directionTxt.setText(lector.getDirection());

       // selecto
      this.currentLector =  lector;
    }
    private void updateTextToogle(){
        if(this.validToggle.isSelected()){
            this.validToggle.setText("Activo");
        }else{
            this.validToggle.setText("Inactivo");
        }
    }


    private  void events(){
        this.validToggle.addItemListener( l -> {
           this.updateTextToogle();
        });
    }
    private void deleteLector(){
       this.delete.addActionListener(e -> {
           if(this.currentLector != null){
             int res = Dialogs.confirmMessage("¿Desea eliminar este lector?" , "Advertencia");
              if(res == JOptionPane.YES_OPTION){
                 try {
                     Optional<Lector> lector =  this.controllerReader.deleteLector(this.currentLector.getDni());

                     if(lector.isPresent()) {
                         Dialogs.successMessage(String.format("%s has sido eliminado", lector.get().getName()));
                         if(this.updateLectorsConsummer != null){
                             System.out.println("notify lector update");
                             this.updateLectorsConsummer.accept(null);
                         }
                     }
                     else Dialogs.errorMessage("No hemos podido elliminar este lector");

                 }catch (SQLException ex){
                     Dialogs.errorMessage("Ha ocurrido un error con la base de datos");
                 }
              }
           }else{
               Dialogs.errorMessage("No se ha seleccionado ningún Lector");
           }
       });
    }
    private Lector getValuesFromForm(){
        String name = nameText.getText();
        String lastName = lastNameText.getText();
        Date dateBirth = Alterns.normalizeToSqL(this.dateBitJCalendar.getDate());
        String direction = this.directionTxt.getText();
        boolean vigencia = this.validToggle.isSelected();
        String phone =  this.textPhone.getText();
        String dni = this.dniText.getText();
        Sexo sexo = this.cboSex.getSelectedItem().toString() == "Masculino" ?  Sexo.MASCULINO : Sexo.FEMENINO;
        Lector obLector =  new Lector(dni, name , direction , phone);
        obLector.setVigencia(vigencia);
        obLector.setDateBirthay(dateBirth);
        obLector.setLastName(lastName);
        obLector.setSex(sexo);
       return  obLector;
    }

    private void saveButton(){
       this.new_.addActionListener( (e) -> {
           // Lector obLector =  this.getValuesFromForm();
    if(this.form.isValid()){
      Lector obLector = this.getValuesFromForm();
           if(this.controllerReader == null){
                System.err.println("controller want not proivided");
            }else{
                try {
                    this.controllerReader.addLector(obLector);
                    Dialogs.successMessage("Se ha guardado un registro");
                } catch (SQLException ex) {
                    ///
                    Dialogs.errorMessage("No se hemos podido guardar el lector");
                    System.err.println("" + ex.getMessage());
                }
            }
    }else{
        Dialogs.errorMessage(this.form.getError());
     }
    });
    }
    // search lector for dni
    private  void  modifyLector(){
        modiFy.addActionListener(e -> {
            try {
                Optional<Lector> lector = this.controllerReader.modifyLector(this.getValuesFromForm());
                 Dialogs.successMessage("Se ha modificado correctamente el lector");
                 if(this.updateLectorsConsummer != null){
                     this.updateLectorsConsummer.accept(null);
                 }

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        });
    }
    private void  eventSerachLectorForDni(){
        this.btnConsultLector.addActionListener(event -> {
             if(!dniText.getText().isEmpty()){
                 try {
                     Optional<Lector> lectoropt = this.controllerReader.getLector(dniText.getText());

                     if(lectoropt.isPresent()){
                          this.patchValuesLector(lectoropt.get());
                      }else{
                          Dialogs.errorMessage("No hemos encontrado un lector con este dni");
                      }
                 }catch (SQLException ex){
                     System.err.println(ex.getMessage());
                    Dialogs.errorMessage("No hemos encontrado un lector con este dni error");
                 }

             }
        });
    }

    private void cleanForm(){
        this.cleanForm.addActionListener(e -> {
            this.form.cleanForm();
        });
    }


    private Dimension applySizeInput(int width) {
        return new Dimension(width, Constants.HEIGHTINPUT);
    }


    /*This code would improve with this https://docs.oracle.com/javase/8/docs/api/java/util/function/package-summary.html*/
    private void buildFieldCallback(BuildFielCall call) {
        JPanel container = new JPanel(new MigLayout("", "[][]", "[]"));
        call.effect(container);
        container.setPreferredSize(new Dimension(500,  Constants.HEIGHTINPUT));
        this.add(container, "span");
    }

    private void buildField(JTextField input, JLabel label) {
        JPanel container = new JPanel(new MigLayout("", "[][]", "[]"));
        container.add(label, "w 30%");
        input.setPreferredSize(this.applySizeInput(dniText.getWidth()));
        container.add(input, "w 70%");
        container.setPreferredSize(new Dimension(500, Constants.HEIGHTINPUT));
        this.add(container, "span");

    }

    private void formInit() {
       form = new TrackerForm();

        dnliLabel = new JLabel("DNI");
        dniText = new JTextField();
        dniText.setText("");
        // set value in form
        this.buildFieldCallback( container -> {
          container.add(dnliLabel, "w 30%");
          container.add(dniText , "w 50%");
          container.add(btnConsultLector , new CC().width("20%").gapX("20px" , "0"));
        } );
        namLabel = new JLabel("Nombres");
        nameText = new JTextField("");

        this.buildField(nameText , namLabel);

        lastName = new JLabel("Apellido");
        lastNameText = new JTextField("");

        this.buildField(lastNameText, lastName);

        dateBirthLabel = new JLabel("Fecha de N");
        dateBitJCalendar = new JDateChooser();
        dateBitJCalendar.setForeground(Color.white);

        BuildFielCall call = (JPanel container) -> {
            container.add(dateBirthLabel, "w 30%");
            container.add(dateBitJCalendar, "w 70%");

        };
         //  date
        this.buildFieldCallback(call);

        //
        // phone
        this.buildField(textPhone ,lblPhone );

        // sex
        cboSex = new JComboBox();
        Arrays.stream(Person.GENRES).forEach(e -> cboSex.addItem(e));



        sexLabel = new JLabel("Sexo");

        BuildFielCall sexCall = (JPanel container) -> {
            container.add(sexLabel, "w 30%");
            container.add(cboSex, "w 70%");
        };

        this.buildFieldCallback(sexCall);

        // direction
        this.buildField(directionTxt, directionLbl);


        // validity
        BuildFielCall validityFn = (JPanel container) -> {
            container.add(validLbl, "w 30%");
            validToggle.setSize(new Dimension(100, 100));
            container.add(validToggle, "w 20%");
        };

        this.buildFieldCallback(validityFn);

        form.addField("name", nameText , String.class  , name ->{
         return  name.length() == 0;
        } , "El nombre es obligatorio");

        this.form.addField("direction" , directionTxt , String.class,dire ->  {
           return dire.length() == 0;
         }  , "Direccion error");

        this.form.addField("genre" , cboSex , String.class);
        this.form.addField("dateBirth" , dateBitJCalendar , Date.class);
        this.form.addField("dni" , dniText , String.class);
        this.form.addField("lastName" , lastNameText , String.class);

        // name
        Validations.limitOnlyNumbers(dniText , 8);



    }

    private void setupButtons(){
       
        JPanel container = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        
        JPanel panelButtons = new JPanel( new GridLayout(2, 2 , 10 , 10));
       
        panelButtons.setPreferredSize(new Dimension( 300 , 150));
        
        // dimension
        Dimension btnDimension = new Dimension(150 , 60);
        
       JButton[] buttons= {new_ , modiFy, delete , cleanForm};
       
       for(JButton btn : buttons){
           btn.setPreferredSize(btnDimension);
              panelButtons.add(btn ,"span 1");
       }
       
       
        c.anchor = GridBagConstraints.CENTER;
        container.add(panelButtons ,c);
        container.setPreferredSize(new Dimension(500, 130));
        
        
        this.add(container);
        
    }
    
    private MigLayout miglayout() {
        return new MigLayout("", "10[]20[]", "20[][][]");
    }

}
