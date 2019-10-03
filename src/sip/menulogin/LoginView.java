/*
 * LoginView.java
 *
 * Created on 12 de Setembro de 2008, 00:45
 */

package sip.menulogin;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.Beans;
import java.net.InetAddress;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import sip.bean.Usuario;
import sip.util.EncriptaSenha;

/**
 *
 * @author  Albert
 */
public class LoginView extends javax.swing.JFrame {

    /** Creates new form LoginView */
    public LoginView() {
        //caixaBaixa();
        initComponents();
        if (!Beans.isDesignTime()) {
            entityManager.getTransaction().begin();
        }
    }
    
    private void conectandoNoLogin(){
       
        query = entityManager.createNamedQuery("Usuario.verificaLogin");
        query.setParameter("login", txtUsuario.getText());
        query.setParameter("senha", EncriptaSenha.encripta(jPSenha.getText()));
        List<Usuario> data = query.getResultList();
        
        if (data.size() > 0) {
            String args[] = new String[1];
            args[0] = data.get(0).getId().toString();
            Menu.main(args);
            
            dispose();
        } else {
            JOptionPane.showMessageDialog(null, "Usuário/Senha incorretos");
            txtUsuario.setText("");
            jPSenha.setText("");
            //dispose();
        }
    }
    
    private void verificaLogin(){
       try{
         conectandoNoLogin();
         System.out.println(InetAddress.getLocalHost().getHostName());//nome da maquina que está usando
    }catch(Exception ex){
        //JOptionPane.showMessageDialog(null,"Sem conexão tente novamente! ");
        //txtUsuario.setText("");
        //jPSenha.setText("");
         conectandoNoLogin();
    }
    
    
    }
    
     private void caixaBaixa(){
         txtUsuario.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toLowerCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
         jPSenha.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JPasswordField campo = (JPasswordField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toLowerCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
          
        }
    

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("SipPU").createEntityManager();
        query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT u FROM Usuario u");
        list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : query.getResultList();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txtUsuario = new javax.swing.JTextField();
        jPSenha = new javax.swing.JPasswordField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("versão 1.1 - SEMMARH");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/fundosiplogin.jpg"))); // NOI18N

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jLabel4.setText("Usuário:");

        jLabel5.setText("Senha:");

        txtUsuario.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        txtUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtUsuarioFocusLost(evt);
            }
        });
        txtUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtUsuarioActionPerformed(evt);
            }
        });

        jPSenha.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        jPSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPSenhaFocusLost(evt);
            }
        });
        jPSenha.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jPSenhaKeyPressed(evt);
            }
        });

        jButton1.setText("Confirma");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jButton1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jButton1KeyPressed(evt);
            }
        });

        jButton2.setText("Cancela");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 112, Short.MAX_VALUE))
                        .addGap(26, 26, 26))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(6, 6, 6)
                                .addComponent(jLabel5)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPSenha, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                            .addComponent(txtUsuario, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtUsuario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jPSenha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(34, 34, 34))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(7, 7, 7)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        setSize(new java.awt.Dimension(233, 296));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
          
    verificaLogin();
    
}//GEN-LAST:event_jButton1ActionPerformed

private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    dispose();
}//GEN-LAST:event_jButton2ActionPerformed

private void jButton1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jButton1KeyPressed
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            evt.consume();  
           
            verificaLogin();
        } 
}//GEN-LAST:event_jButton1KeyPressed

private void jPSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPSenhaFocusLost
  // verificaLogin(); // TODO add your handling code here:
}//GEN-LAST:event_jPSenhaFocusLost

private void jPSenhaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jPSenhaKeyPressed
  
    if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            evt.consume();  
            verificaLogin();
        }  // TODO add your handling code here:
}//GEN-LAST:event_jPSenhaKeyPressed

    private void txtUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtUsuarioFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioFocusLost

    private void txtUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsuarioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsuarioActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
         
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                   UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Select the Look and Feel
           // UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                    new LoginView().setVisible(true);
                    txtUsuario.requestFocus();

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(LoginView.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                }catch(Exception ex){
                 JOptionPane.showMessageDialog(null,ex);
                        }   
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPasswordField jPSenha;
    private javax.swing.JPanel jPanel1;
    private java.util.List list;
    private javax.persistence.Query query;
    private static javax.swing.JTextField txtUsuario;
    // End of variables declaration//GEN-END:variables

}
