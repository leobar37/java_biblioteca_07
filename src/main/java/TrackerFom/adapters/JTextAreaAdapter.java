package TrackerFom.adapters;

import TrackerFom.models.Field;

import javax.swing.*;

public class JTextAreaAdapter extends   Adapter {

    public JTextAreaAdapter(Field field){
        this.field =field;

    }
    @Override
    public  Object getValue() {
        JTextArea component =  (JTextArea) field.component;
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
        JTextArea comp =  (JTextArea) field.component;
        comp.setText("");
    }

    @Override
    public boolean isValid() {
        return false;
    }
}
