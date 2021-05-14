package TrackerFom.adapters;

import TrackerFom.models.Field;

import javax.swing.*;

public class TextField  extends   Adapter{

    public TextField(Field field){
        super.field = field;
    }
    @Override
    public  Object getValue() {
        JTextField component =  (JTextField) field.component;
        String value =  component.getText();
        if(String.class == field.type){
            return value;     }
        if(Integer.class == field.type){
            return Integer.parseInt(value);
        }
        if(Float.class == field.type){
            return  Float.parseFloat(value);
        }
        System.err.println(String.format("%s  class type not found or not supported" , field.type.getClass().getName()));
        return null;
    }

    @Override
    public void clean() {
       JTextField comp =  (JTextField) field.component;
       comp.setText("");
    }



}
