/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TrackerFom.adapters;

import TrackerFom.models.Field;
import javax.swing.JSpinner;

/**
 *
 * @author USER
 */
public class JSpinnerAdapter extends Adapter{
    
    
    public JSpinnerAdapter(Field field){
              super.field = field;
    }
    
    @Override
    public Object getValue() {
        
        JSpinner spinner =  (JSpinner)this.field.component;
        
        String value=  spinner.getValue().toString();
        
        return  Integer.parseInt(value);
        
       
    }

    @Override
    public void clean() {
       JSpinner spinner =  (JSpinner)this.field.component;
       spinner.setValue(0);
    }
     
    
}
