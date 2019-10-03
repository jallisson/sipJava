/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.util;

import com.hexidec.ekit.EkitCore;
import java.awt.FlowLayout;
import javax.swing.JFrame;

/**
 *
 * @author jallisson
 */
public class SwingHTMLEditor extends JFrame {
    public SwingHTMLEditor() {
        setTitle("Teste Simples do Ekit HTML Editor");
        setSize(460, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        EkitCore core = new EkitCore();

        setLayout(new FlowLayout());

        add(core, FlowLayout.LEFT);
        add(core.getToolBarStyles(true), FlowLayout.LEFT);
        add(core.getToolBarFormat(true), FlowLayout.LEFT);
        add(core.getToolBarMain(true), FlowLayout.LEFT);
        add(core.getMenuBar(), FlowLayout.LEFT);

        setVisible(true);
     }

    public static void main(String[] args) {
        new SwingHTMLEditor();
    }
}
