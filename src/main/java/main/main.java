package main;



import ca.krasnay.sqlbuilder.SelectBuilder;
import ca.krasnay.sqlbuilder.SelectCreator;
import com.formdev.flatlaf.FlatDarkLaf;

import config.bdConnection;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.Disposable;
import mdlaf.MaterialLookAndFeel;
import mdlaf.themes.JMarsDarkTheme;
import javax.swing.UIManager;
import mdlaf.themes.MaterialLiteTheme;
import mdlaf.themes.MaterialOceanicTheme;

import org.reactivestreams.Subscription;
import utils.Uiutils;
import view.FrmArea;
import view.FrmAutor;
import view.FrmEditorial;
import view.FrmLector;
import view.FrmLibro;
import view.FrmPrestamo;
import view.LoguinForm;
import view.frmPrincipal;
import view.LoguinForm;

public class main {

    public static void main(String[] args) {
        try {
          UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
          LoguinForm.open();
            //FrmPrestamo.open();
//FrmEditorial.open();
         // frmPrincipal.open();
           // FrmArea.open();
          // LoguinForm.open();
            //FrmAutor.open()        
  
            //FrmLibro.open();
         
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
