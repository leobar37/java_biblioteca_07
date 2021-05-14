package TrackerFom;

import TrackerFom.adapters.DateChosserAdapter;
import TrackerFom.adapters.JComboBoxAdapter;
import TrackerFom.adapters.JTextAreaAdapter;
import TrackerFom.adapters.TextField;
import TrackerFom.interns.constants;
import TrackerFom.models.AbtractTrackerForm;
import TrackerFom.models.Field;


import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

import java.util.function.Predicate;

/*
* idea: ELaborar un clase
que tome una serie
de elementos Jcomponent
y  poder
- Agregarle validaciones
- Poder limpiarlos sin generar verbosidad
*
idea :
1 - Pedir el id, el componente, y el tipo de clase
2 - Unirlo a un map identificado por el id
3 - agregar validacion de acuerdo al id
    - Una validacion es una función que me debe retornar
      true o false
    - Cada field puede tener un una serie de validaciones

 Features
 * Clean values
 * get values
 *
* */

public class TrackerForm extends AbtractTrackerForm {

    private Map<String , Field>  list =  new HashMap<String , Field>();

    public TrackerForm(Field ...fields){
        for (Field field : fields){
              this.addField(field);
        }
    }
    @Override
    public void addField(Field field) {
        if(list.containsKey(field.id)){
            new Exception(" [Tracker Form] Ke already exist");
           return;
        }
        this.list.put(field.id , field);
    }

    public<T> void addField(String  id , JComponent component, Class<T> type, Predicate<T> predicate , String messageError){
         Field field = new Field(id , component , predicate , messageError);
         field.type =type;
         this.addField(field);
    }

    public<T> void addField(String  id , JComponent component, Class<T> type){
        Field field = new Field(id , component);
        field.type =type;
        this.addField(field);
    }
      // get value of JtextField
      public<T> Object getValue(String id){

         if(!this.list.containsKey(id)){
             System.err.println(String.format("not exist field with  id: %s" , id));
               return null;
         }
         Field field = this.list.get(id);

         // support for Jtext field
          if(field.component instanceof JTextField){
              TextField  textField =  new TextField(field);
              return  textField.getValue();
          }
          // support for JDate choosser
          if(field.getNameOfype().equalsIgnoreCase( constants.FIELD_DATE_CHOSSER)){
              DateChosserAdapter adapter = new DateChosserAdapter(field);
              return  adapter.getValue();
          }
          // SUPPORT FOR JTEXAREA
          if(field.component instanceof  JTextArea){
              JTextAreaAdapter adapter = new JTextAreaAdapter(field);
              return  adapter.getValue();
          }
          // support JCombo Bo
          if(field.component instanceof  JComboBox){
              JComboBoxAdapter adapter = new JComboBoxAdapter(field);
              return  adapter.getValue();
          }
          System.err.println(String.format("%s  class type not found or not supported" , field.type.getClass().getName()));
          return  null;
      }

      public void cleanForm(){
         this.list.values().forEach( val -> {
              cleanField(val);
         });
      }

      private void cleanField(Field field){
          if(field.component instanceof JTextField){
              TextField  textField =  new TextField(field);
              textField.clean();
          }
          if(field.getNameOfype().equalsIgnoreCase(constants.FIELD_DATE_CHOSSER)){
              DateChosserAdapter adapter = new DateChosserAdapter(field);
              adapter.clean();
          }
          if(field.component instanceof  JTextArea){
              JTextAreaAdapter adapter = new JTextAreaAdapter(field);
              adapter.clean();
          }
          // support JCombo Bo
          if(field.component instanceof  JComboBox){
              JComboBoxAdapter adapter = new JComboBoxAdapter(field);
                adapter.clean();
          }
      }
      public  boolean isValid(){

         for (Field field : this.list.values()){
              if(!this.validateField(field)){
                  return  false;
              }
         }
        return  true;
      }
      public String getError(){

          for (Field field : this.list.values()){
              System.out.println(field.id);
             if(!this.validateField(field)){
                 System.out.println(field.messageError);
                 if(field.messageError !=  null){
                     return field.messageError;
                 }else{
                     return  "Existen campos vacios";
                 }
             }
          }

          return  "Existen campos vacios";
      }
      private boolean validateField(Field field){
          if(field.component instanceof JTextField){
              TextField  textField =  new TextField(field);
              return  textField.isValid();
          }
          if(field.getNameOfype().equalsIgnoreCase(constants.FIELD_DATE_CHOSSER)){
              DateChosserAdapter adapter = new DateChosserAdapter(field);
              return  adapter.isValid();
          }
          if(field.component instanceof  JTextArea){
              JTextAreaAdapter adapter = new JTextAreaAdapter(field);
            return  adapter.isValid();
          }
          // support JCombo Bo
          if(field.component instanceof  JComboBox){
              JComboBoxAdapter adapter = new JComboBoxAdapter(field);
             return  adapter.isValid();
          }
          return  true;
      }
}
