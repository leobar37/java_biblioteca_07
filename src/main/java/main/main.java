package main;

import com.formdev.flatlaf.FlatDarkLaf;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import javax.swing.UIManager;

import org.reactivestreams.Subscription;
import utils.Uiutils;
import view.FrmLector;
import view.LoguinForm;
import view.frmPrincipal;

public class main {

    public static void main(String[] args) {
        try {
          UIManager.setLookAndFeel(new MaterialLookAndFeel(new JMarsDarkTheme()));
          
          LoguinForm.open();
           // principal.setLocationRelativeTo(null);
            //FrmLector  frmLector = new FrmLector();
            //Uiutils.openWindow(frmLector);
            // frmLector.setVisible(true);
             //frmLector.setLocationRelativeTo(null);
          // LoguinForm formLoguin = new LoguinForm(null, false);
          // formLoguin.setVisible(true);
          // formLoguin.setLocationRelativeTo(null);
           
       } catch (Exception ex) {
            System.err.println("" +  ex.toString());
        }



    }

}
