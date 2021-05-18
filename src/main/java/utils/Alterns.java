/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;
import javax.swing.*;


public class Alterns {
    //metodo 1 acomoda el nombre
    public static String AcomodaNombre(String nombre) {
        //hacer minuscula todas las letras
        String[] nombres = nombre.split(" ");
        String nombreAc = "";
        for (int i = 0; i < nombres.length; i++) {
            int n = nombres[i].length();
            nombreAc = nombreAc + nombres[i].substring(0, 1).toUpperCase() + nombres[i].substring(1, n) + " ";
        }
        return nombreAc;
    }

    //devuelve verdadero cuando el texto es numerico
    public static boolean isNumeric(String cadena) {
        try {
            Integer.parseInt(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    } //Comprueba si el texto es un número

    //limitar teclado a solo numeros   
    public static void limitarSoloMontos(JTextField texto) {
        texto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b') && (caracter != '.')) {
                    e.consume();  // ignorar el evento de teclado
                }
            }
        });
    }

    public static void limitarSoloNumero(JTextField texto) {
        texto.addKeyListener(new KeyAdapter() {
            public void keyTyped(KeyEvent e) {
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b')) {
                    e.consume();  // ignorar el evento de teclado
                }
            }
        });
    }

    //limita un numero hasta el tamaño que se le indique
    public static void limitarSoloNumeroRes(JTextField texto, int res) {
        texto.addKeyListener(new KeyAdapter() {

            public void keyTyped(KeyEvent e) {
                String dni = texto.getText();
                char caracter = e.getKeyChar();
                // Verificar si la tecla pulsada no es un digito
                if (dni.length() >= res) {
                    e.consume();
                }
                if (((caracter < '0') || (caracter > '9')) && (caracter != '\b')) {
                    e.consume();  // ignorar el evento de teclado
                }
            }
        });
    }
    
    //metodo general para limpiar cajas

    public static void LimpiarCajas(JPanel panel) {
        for (int i = 0; i < panel.getComponents().length; i++) {
            if (panel.getComponents()[i] instanceof JTextField) {
                JTextField caja = (JTextField) panel.getComponents()[i];
                caja.setText("");
            }
            if (panel.getComponents()[i] instanceof JComboBox) {
                JComboBox cbo = (JComboBox) panel.getComponents()[i];
                cbo.setSelectedIndex(-1);
            }
            if (panel.getComponents()[i] instanceof JTextArea) {
                JTextArea textArea = (JTextArea) panel.getComponents()[i];
                textArea.setText("");
            }
            if (panel.getComponents()[i] instanceof JPasswordField) {
                JPasswordField campoContrasea = (JPasswordField) panel.getComponents()[i];
                campoContrasea.setText("");

            }

        }
    }
    
    private static boolean ifPresentComponetInArray(ArrayList<Component> arr , Component c) {

        return   arr.stream().filter(e -> e.equals(c)).toArray().length > 0;
    }


     public static void cleanBoxs(JComponent ...components){
        for (Component  e: components){

        }
     }

    //regresa false cuando hay un campo vacio y verdadero cuando esta correcto
    public static boolean testRequieredFields(JPanel panel, Component ... whiteList) {

        ArrayList<Component> whiteListComponents =  new ArrayList<>();
          for(Component e : whiteList){

              whiteListComponents.add(e);
          }
         
        boolean verificacion = true;

        for (int i = 0; i < panel.getComponents().length; i++) {

            Component component = panel.getComponents()[i];
            if(ifPresentComponetInArray(whiteListComponents, component)){
                System.out.println("escape component: " + component.toString());
                continue;
            }else{
                System.out.println(whiteListComponents.size());
                System.out.println("not present");
            }
         
            if (component instanceof JPanel) {
                verificacion = Alterns.testRequieredFields((JPanel) component, whiteList);
    
                if (!verificacion) {
                    return verificacion;
                }
            }

            if (panel.getComponents()[i] instanceof JTextField) {
                JTextField caja = (JTextField) panel.getComponents()[i];
                
                if (caja.getText().equals("")) {
                    return false;
                }

            } else {
                if (panel.getComponents()[i] instanceof JComboBox) {
                    JComboBox cbo = (JComboBox) panel.getComponents()[i];
                    if (cbo.getSelectedIndex() == -1) {

                        return false;

                    }
                } else {
                    if (panel.getComponents()[i] instanceof JPasswordField) {
                        //JPasswordField campoContrase = (JPasswordField) panel.getComponents()[i];
                        //String contrase = new String(campoContraseña.getPassword());
                       // if (contraseña.equals("") || contraseña.length() <= 5) {
                         //   return false;
                       // }

                    } else {
                        // if (panel.getComponents()[i] instanceof JDateChooser) {
                        // JDateChooser datoFecha = (JDateChooser) panel.getComponents()[i];
                        //  if (((JTextField) datoFecha.getDateEditor().getUiComponent()).getText().equalsIgnoreCase("")) {
                        //       return false;
                        //     }
                        //   }
                    }
                }

            }

        }
        return verificacion;
    }

    //este metodo es parecido al anterior due necesario por un proble del Jcombobox
    public static boolean ProbarsinJcombo(JPanel panel) {
        boolean verificacion = true;
        for (int i = 0; i < panel.getComponents().length; i++) {

            if (panel.getComponents()[i] instanceof JTextField) {
                JTextField caja = (JTextField) panel.getComponents()[i];
                if (caja.getText().equals("")) {
                    verificacion = false;
                }
            } else {
                if (panel.getComponents()[i] instanceof JComboBox) {
                    JComboBox cbo = (JComboBox) panel.getComponents()[i];
                    if (cbo.getSelectedIndex() == -1) {

                        //verificacion = false;
                        //System.out.println("aqui"+verificacion);
                    }
                } else {
                    if (panel.getComponents()[i] instanceof JPasswordField) {
                      //  JPasswordField campoContraseña = (JPasswordField) panel.getComponents()[i];
                       // String contraseña = new String(campoContraseña.getPassword());
                        //if (contraseña.equals("") || contraseña.length() <= 5) {
                           // verificacion = false;
                        //}

                    }
                }

            }

        }
        return verificacion;
    }

    //*******************************FECHAS******************//
    /*  //obtener fecha del sistema
    public static Date obtenerFechaSistema() {
        Calendar calendario = new GregorianCalendar();
        int dia = calendario.get(Calendar.DATE);
        int mes = calendario.get(Calendar.MONTH) + 1;

        java.util.Date retorno = java.sql.Date.valueOf(fecha);
        return retorno;
    }
    //convertir fecha de date a string
     */
    public static String convertirFecha(Date time) {
        SimpleDateFormat formato = new SimpleDateFormat("YYYY-MM-dd");
        return formato.format(time);
    }
    //dar un formato a la fecha 


    public static java.util.Date formatoFecha(Date dato) {
        SimpleDateFormat formato = new SimpleDateFormat("YYYY-MM-dd");
        String fecha = formato.format(dato);
        java.util.Date retorno = java.sql.Date.valueOf(fecha);
        return retorno;
    }

    // normalize every date in sql date
    public static java.sql.Date normalizeToSqL(Date dato) {
        //2015-03-31
        SimpleDateFormat formato = new SimpleDateFormat("YYYY-MM-dd");
        String fecha = formato.format(dato);
        return java.sql.Date.valueOf(fecha);
    }

    //cantidad de dias en el mes
    public static int numeroDeDiasMes(int mes) {
        int numeroDias = -1;

        switch (mes) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                numeroDias = 31;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                numeroDias = 30;
                break;
            case 2:

                Date anioActual = new Date();
                if (esBisiesto(1900 + anioActual.getYear())) {
                    numeroDias = 29;
                } else {
                    numeroDias = 28;
                }
                break;

        }

        return numeroDias;
    }

    public static boolean esBisiesto(int anio) {

        GregorianCalendar calendar = new GregorianCalendar();
        boolean esBisiesto = false;
        if (calendar.isLeapYear(anio)) {
            esBisiesto = true;
        }
        return esBisiesto;

    }
//***prueba llenar datos

    public static String RetornarMes(Date fecha) {
        String mes = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(fecha);
        int mesAux = calendar.get(Calendar.MONTH);
        switch (mesAux) {
            case Calendar.JANUARY:
                mes = "Enero";
                break;
            case Calendar.FEBRUARY:
                mes = "Febrero";
                break;
            case Calendar.MARCH:
                mes = "Marzo";
                break;
            case Calendar.APRIL:
                mes = "Abril";
                break;
            case Calendar.MAY:
                mes = "Mayo";
                break;
            case Calendar.JUNE:
                mes = "Junio";
                break;
            case Calendar.JULY:
                mes = "Julio";
                break;
            case Calendar.AUGUST:
                mes = "Agosto";
                break;
            case Calendar.SEPTEMBER:
                mes = "Septiembre";
                break;
            case Calendar.OCTOBER:
                mes = "Octubre";
                break;
            case Calendar.NOVEMBER:
                mes = "Noviembre";
                break;
            case Calendar.DECEMBER:
                mes = "Diciembre";
                break;
            default:
                break;

        }
        return mes;
    }

    //Obtiene un array de fechas
    public static java.util.List<Date> getIntervaloFechas(java.util.Date fecha_inicio, java.util.Date fecha_fin, int a) {
        Calendar fechaInicio = Calendar.getInstance();//convertir la fechas a calendar por que es mas comodo manejar fechas
        fechaInicio.setTime(fecha_inicio);
        Calendar fechaFin = Calendar.getInstance();
        fechaFin.setTime(fecha_fin);
        java.util.List<Date> listaFechas = new java.util.ArrayList<Date>();
        while (!fechaInicio.after(fechaFin)) {
            listaFechas.add(fechaInicio.getTime());
            fechaInicio.add(a, 1);
        }

        return listaFechas;
    }

    /*  public static int retornaEdad(Date OBJDate) {
        Date sistema = Alterns.obtenerFechaSistema();

        List<Date> listaFechass = getIntervaloFechas(OBJDate, sistema, Calendar.YEAR);

        return listaFechass.size();

    }*/
}
