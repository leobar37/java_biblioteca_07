package TrackerFom.adapters;

import TrackerFom.interns.constants;
import TrackerFom.models.Field;
import  java.lang.reflect.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateChosserAdapter extends  Adapter {


    public DateChosserAdapter(Field field){
         super.field = field;

    }

    @Override
    public Object getValue() {
        try {
            Class cls = Class.forName(this.field.component.getClass().getName());
            Method method =  cls.getMethod("getDate");
            Object arglist[] = new Object[0];
            Object result = method.invoke(field.component, arglist);
            SimpleDateFormat formato = new SimpleDateFormat("YYYY-MM-dd");
            String fecha = formato.format(result);
            return java.sql.Date.valueOf(fecha);
        }catch (Throwable ex){
           ex.printStackTrace();
        }
        return  null;
    }
    @Override
    public void clean() {
        try {
            Class cls = Class.forName(this.field.component.getClass().getName());
            Method method =  cls.getMethod("setDate" , java.util.Date.class);
            method.invoke(field.component, Date.valueOf(LocalDate.now()) );

        }catch (Throwable ex){
            ex.printStackTrace();
        }

    }
}
