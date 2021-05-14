package TrackerFom.models;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Field<T> {
    //id
    // component
    // validations
    public String id;
    public JComponent component;
    public Predicate<T>  isValidCallback;
    public  Class<T> type;
    public  String messageError;
    public Field(String id , JComponent component ){
        this.id = id;
        this.component =  component;
        this.isValidCallback = null;
    }

    public  Field(String id , JComponent component ,  Predicate<T>  isValidCallback , String messageError ){
        this(id, component);
        this.isValidCallback = isValidCallback;
      this.messageError = messageError;
    }
    public String getNameOfype(){
        System.out.println(this.component.getClass().getName());
        String[] chars = this.component.getClass().getName().split("\\.");
        return  chars[chars.length - 1];
    }


}
