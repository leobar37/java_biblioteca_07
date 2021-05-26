
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class LeTableModel<T > extends DefaultTableModel {
    /*
     * Problema:
     *
     * al dar una seleccion a la tabla se selecciona una fila
     * lo ideal seria que pudiera seleccionar un objeto
     * para facilitar el manejo de datos y tambien pode usar los methodos del objeto
     *
     * Planteamiento
     *  al instancial la clase recibe un Consumer al cual le pasa el objeto
     *  y este debe retornarme el identificador (key)
     *  al momento de agregar un objeto llamo a la funcion para poder sacar la key
     *
     * 1 metodos
     * - get(key) <- retorna el objet dandode una key
     * - add(objeto) <- agregar un objetos al modelo
     * -- add many <- agrega muchos objetos al modelo
     *
     * ## este modelo esta inspirado  en un blog heredando algunas caracteristicas
     *
     *
     * */

    private EventListenerList listenerList;
    private List columnIdentifiers;
    private  Map<Integer ,T> listData = new HashMap<Integer , T>();
    
    private  Function<T , Object[]>  callbackRow;
   /**
    * 
    * @param consumer :  ES UN CALLBAK UTILIZADO PARA OBTENER LA KEY DEL OBJECTO
    * @param callbackRow  : ES UN CALLBACK UTILIZADO PARA OBTENER LA FILA DE LA TABLA
    */
    public LeTableModel(Function<T , Object[]>  callbackRow){
        columnIdentifiers = new ArrayList();
        listenerList = new EventListenerList();
       
        this.callbackRow = callbackRow;
    }
    
    /* ***************** *
     * Manejo de eventos *
     * ***************** */

    @Override
    public void addTableModelListener(TableModelListener l) {
        listenerList.add(TableModelListener.class, l);
    }

    @Override
    public void removeTableModelListener(TableModelListener l) {
        listenerList.remove(TableModelListener.class, l);
    }

    /**
     * Método conveniente para obtener obtener una lista con
     * los TableModelListeners suscriptos a nuestro modelo.
     * NO es parte de la interfaz TableModel
     */
    public TableModelListener[] getTableModelListeners() {
        return listenerList.getListeners(TableModelListener.class);
    }

    /**
     * Método general para notificar a los {@code TableModelListeners}
     * que ha ocurrido un evento.
     * Nota: Los listeners son notificados en orden inverso al de suscripción.
     */
    protected void notifyTableChanged(TableModelEvent e) {
        TableModelListener[] listeners = getTableModelListeners();
        for (int i = listeners.length - 1; i >= 0; i--) {
            listeners[i].tableChanged(e);
        }
    }

    /**
     * Notifica que el header de la tabla ha cambiado.
     */
    protected void notifyTableHeaderChanged() {
        TableModelEvent e = new TableModelEvent(this, TableModelEvent.HEADER_ROW);
        notifyTableChanged(e);
    }

    /**
     * Notifica que han sido insertadas nuevas filas.
     *
     * @param firstRow El índice de la primera fila insertada.
     * @param lastRow El índice de la última fila insertada.
     */
    protected void notifyTableRowsInserted(int firstRow, int lastRow) {
        TableModelEvent e = new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.INSERT);
        notifyTableChanged(e);
    }

    /**
     * Notifica que una o más filas en un rango han sido modificadas.
     *
     * @param firstRow El índice de la primera fila en el rango.
     * @param lastRow El índice de la última fila en el rango.
     */
    protected void notifyTableRowsUpdated(int firstRow, int lastRow) {
        TableModelEvent e = new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.UPDATE);
        notifyTableChanged(e);
    }

    /**
     * Notifica que una o más filas en un rango han sido borradas.
     *
     * @param firstRow El índice de la primera fila en el rango.
     * @param lastRow El índice de la última fila en el rango.
     */
    protected void notifyTableRowsDeleted(int firstRow, int lastRow) {
        TableModelEvent e = new TableModelEvent(this, firstRow, lastRow, TableModelEvent.ALL_COLUMNS, TableModelEvent.DELETE);
        notifyTableChanged(e);
    }

    /**
     * Notifica que el valor de una celda ha cambiado.
     *
     * @param row El índice de la fila a la que pertenece la celda.
     * @param column El índice de la columna a la que pertenece la celda.
     */
    protected void notifyTableCellUpdated(int row, int column) {
        TableModelEvent e = new TableModelEvent(this, row, row, column);
    }

    @Override
    public int getRowCount() {
        return super.dataVector.size();
    }
    @Override
    public int getColumnCount() {
        return columnIdentifiers.size();
    }
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex < 0 || columnIndex >= getColumnCount()) {
            throw new ArrayIndexOutOfBoundsException(columnIndex);
        } else {
            return columnIdentifiers.get(columnIndex).toString();
        }
    }
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    public void addRow(T domainObject)  {
        int rowIndex = listData.size();
        
         
         super.addRow(this.callbackRow.apply(domainObject));

         this.listData.put(rowIndex, domainObject);
        notifyTableRowsInserted(rowIndex, rowIndex);
    }
    public void addRows(List<T> domainObjects) {
        if (!listData.isEmpty()) {
            int firstRow = listData.size();
            for (T obj: domainObjects){
                this.addRow(obj);
            }
            int lastRow = listData.size() - 1;
            notifyTableRowsInserted(firstRow, lastRow);
        }
    }
    public void clearTableModelData() {
        if (!listData.isEmpty()) {
            int lastRow = listData.size() - 1;
            listData.clear();
            notifyTableRowsDeleted(0, lastRow);
        }
    }

    /**
     * Return objet by key
     */
    public T get(int key){
        return  listData.get(key);
    }

    public void addColumns(List columnIdentifiers) {
        this.columnIdentifiers.clear();
        this.columnIdentifiers.addAll(columnIdentifiers);
        notifyTableHeaderChanged();
    }
    

}
