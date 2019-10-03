/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * codigo original: http://pt.stackoverflow.com/questions/4591/como-colorir-linhas-espec%C3%ADficas-de-uma-jtable
 * codigo original: http://stackoverflow.com/questions/5557485/why-does-my-java-custom-cell-renderer-not-show-highlighting-when-the-row-cell-is
 */
package sip.util;

import java.awt.Color;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Alessandra
 */
public class ColorirTabelaFiscalizacao extends DefaultTableCellRenderer {

    /**
     * 
     */
    private static final long   serialVersionUID    = 1L;
    public static final float R = 0.9f;
    public static final float G = 0.5f; 
    public static final float B = 0.8f;
    
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
     super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
       //203,216,227
        //Color c = new Color(203,216,227);
        
        Color c = Color.WHITE;
        Object text = table.getValueAt(row, 7);
        if (text != null && "NAO".equals(text.toString())){
            // RGB
             c = Color.RED;
           
        }
         if(text != null && "SIM".equals(text.toString())){
              c = Color.GREEN;
        }
                       
           setBackground(c);      

           
       if (isSelected){
              //setForeground(Color.BLUE);
              setBackground(table.getSelectionBackground());    
      }
     

      
        return this;
       
        
    }

   
}
