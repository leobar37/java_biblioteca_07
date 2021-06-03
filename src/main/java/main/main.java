package main;

import controllers.UsuarioController;
import mdlaf.MaterialLookAndFeel;

import javax.swing.UIManager;
import mdlaf.themes.MaterialLiteTheme;
import models.User;
import org.mindrot.jbcrypt.BCrypt;

import view.LoguinForm;

public class main {

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(new MaterialLookAndFeel(new MaterialLiteTheme()));

            LoguinForm.open();
            
            /*User user = new User("76280650", "Juan Beto","Los rosales","987654321");
            user.setCuestion("¿Color favorito?");
            user.setRptaCuestion("rojo");
            user.setUsername("elbeto");
            user.setPassword("pepito12345");
           
            UsuarioController.instance().addUser(user);*/
            
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
            System.err.println("" + ex.toString());
        }

    }

}
