/*http://stackoverflow.com/questions/28823670/how-to-sort-jtable-in-shortest-way
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

/**
 *
 * @author jallisson
 */
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

public class OrdenarTabela{

    public static void main(String[] args) {
        OrdenarTabela ordenarTabela = new OrdenarTabela();
    }

    public OrdenarTabela() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                DefaultTableModel model = new DefaultTableModel(new String[]{"People", "Place", "Organisation", "Event", "Mentions"}, 0);
                model.addRow(new Object[]{"Prankster", "USA", "Microsoft Pvt Ltd", "Party'14", 900000});
                model.addRow(new Object[]{"Ramanuj", "India", "Tata Consultancy", "Party'14", 500000});
                model.addRow(new Object[]{"Banana", "India", "Tata Consultancy", "Party'14", 500000});

                JTable table = new JTable(model);
                TableRowSorter<TableModel> sorter = new TableRowSorter<>(table.getModel());
                table.setRowSorter(sorter);

                List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
                sortKeys.add(new RowSorter.SortKey(4, SortOrder.ASCENDING));
                sortKeys.add(new RowSorter.SortKey(0, SortOrder.ASCENDING));
                sorter.setSortKeys(sortKeys);

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new JScrollPane(table));
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

}
