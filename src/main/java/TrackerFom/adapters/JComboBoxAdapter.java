package TrackerFom.adapters;

import TrackerFom.models.Field;

import javax.swing.*;

public class JComboBoxAdapter extends   Adapter {


    public JComboBoxAdapter(Field field){
        super.field = field;
    }
    @Override
    public Object getValue() {
        JComboBox  co = (JComboBox) this.field.component;
        String value =  co.getSelectedItem().toString();
        if(String.class == field.type){
            return value;     }
        if(Integer.class == field.type){

            return Integer.parseInt(value);
        }
        if(Float.class == field.type){
            return  Float.parseFloat(value);
        }
        return co.getSelectedItem();

    }

    @Override
    public void clean() {
        JComboBox  co = (JComboBox) this.field.component;
        co.setSelectedIndex(-1);
    }

    @Override
    public boolean isValid() {
        return true;
    }
}
