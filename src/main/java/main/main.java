package main;



import mdlaf.MaterialLookAndFeel;

import javax.swing.UIManager;
import mdlaf.themes.MaterialLiteTheme;

import view.LoguinForm;

public class main {

    public static void main(String[] args) {
        try {
          UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));
          LoguinForm.open();
            //FrmPrestamo.open();
            //FrmEditorial.open();
            //frmPrincipal.open();
            // FrmArea.open();
            // LoguinForm.open();
            //FrmAutor.open()        
            //FrmLibro.open();
            //principal.setLocationRelativeTo(null);
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
