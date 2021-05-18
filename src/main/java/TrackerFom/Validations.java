package TrackerFom;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Validations {

    // limitOnly numbers
    public static void limitOnlyNumbers(JTextField texto, int n) {
        texto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                String dni = texto.getText();
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (dni.length() >= n) {
                    e.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b')) {
                    e.consume();  // ignorar el evento de teclado
                }
            }
        });
    }
    
    public static boolean StrinRequied(String value){
        return value.length() == 0;
    }
    
    
    
    
}
