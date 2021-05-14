package TrackerFom.adapters;

import TrackerFom.models.Field;

 public abstract class Adapter {
  Field field;
  public abstract Object  getValue();
 public abstract void clean();
 public boolean isValid() {
  if(this.field.isValidCallback == null){
   return  true;
  }
  boolean res =this.field.isValidCallback.test(this.getValue());

  return !res;
 }
}
