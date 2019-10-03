/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.atividade;

import sip.menulogin.Menu;
import sip.requerente.RequerenteFrame;
import sip.util.Constantes;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import sip.processo.ProcessoFrame;

/**
 *
 * @author T2Ti
 */
public final class AtividadeFrame1 extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Atividade> listAtividade;
    private int modo;
    private int modoSeleciona;
    private ProcessoFrame processoFrame;
    
    

    /** Creates new form ClienteFrame */
    public AtividadeFrame1() {
        initComponents();
        defineModelo();
        caixaAlta();
        fechando();
     

    }

    private void fechando() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addInternalFrameListener(
                new InternalFrameAdapter() {

                    @Override
                    public void internalFrameClosing(InternalFrameEvent e) {
                        dispose();
                        Menu.menuVisivel();

                    }
                });
    }

    
    public AtividadeFrame1(ProcessoFrame processoFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.processoFrame = processoFrame;
        txtFiltroAtividade.requestFocus();
        modoSeleciona = Constantes.PROCESSO;                    
    }
   

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblAtividade.getModel();
        listModel = tblAtividade.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheAtividade();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblAtividade.getColumnModel().getColumn(1).setPreferredWidth(700);
        } catch (ParseException ex) {
            Logger.getLogger(AtividadeFrame1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        AtividadeBD atividadeBD = new AtividadeBD();

        if (txtFiltroAtividade.getText().equals("")) {
            listAtividade = atividadeBD.consultaAtividade();
        } else {
            listAtividade = atividadeBD.consultaAtividade(txtFiltroAtividade.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listAtividade.size(); i++) {
            tableModel.insertRow(i, new Object[]{listAtividade.get(i).getId(), listAtividade.get(i).getNome()});
        }

    }

    private void mostraDetalheAtividade() {
        if (tblAtividade.getSelectedRow() != -1) {
            int indice = tblAtividade.getSelectedRow();
            txtNome.setText(listAtividade.get(indice).getNome());
            cbPotencialPoluidor.setSelectedItem(listAtividade.get(indice).getPotencialPoluidor());

        } else {
            txtNome.setText("");

        }
    }

    private void limpaCampos() {
        {
            txtNome.setText("");

        }
    }

    private void incluiAtividade() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Atividade!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            Atividade atividade = new Atividade();
            atividade.setNome(txtNome.getText().trim());
            atividade.setPotencialPoluidor(cbPotencialPoluidor.getSelectedItem().toString());

            AtividadeBD atividadeBD = new AtividadeBD();
            if (atividadeBD.incluiAtividade(atividade)) {
                JOptionPane.showMessageDialog(this, "Atividade cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Atividade já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraAtividade() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Atividade!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            Atividade atividade = new Atividade();
            atividade.setId(this.listAtividade.get(tblAtividade.getSelectedRow()).getId());
            atividade.setNome(txtNome.getText().trim());
            atividade.setPotencialPoluidor(cbPotencialPoluidor.getSelectedItem().toString());

            AtividadeBD atividadeBD = new AtividadeBD();
            if (atividadeBD.alteraAtividade(atividade)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiAtividade() {
        AtividadeBD atividadeBD = new AtividadeBD();
        if (atividadeBD.excluiAtividade(listAtividade.get(tblAtividade.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Atividade excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o atividade!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtNome.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
       }

    private void habilitaCampos() {
        txtNome.setEditable(true);
        cbPotencialPoluidor.setEnabled(true);

    }

    private void desabilitaCampos() {
        txtNome.setEditable(false);
        cbPotencialPoluidor.setEnabled(false);
    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void selecionaAtividadeProcesso() {
        if (tblAtividade.getSelectedRow() != -1) {
            processoFrame.setAtividade(listAtividade.get(tblAtividade.getSelectedRow()));
            this.dispose();
            processoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Atividade da lista!", "Atividade", JOptionPane.INFORMATION_MESSAGE);
        }
    }

   
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        cbPotencialPoluidor = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroAtividade = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAtividade = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Atividade\n\n");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/MZU_64x64.png"))); // NOI18N
        jLabel1.setText("Atividade");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Potêncial Poluidor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        txtNome.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNome, gridBagConstraints);

        cbPotencialPoluidor.setEditable(true);
        cbPotencialPoluidor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ALTA", "MÉDIA", "BAIXA" }));
        cbPotencialPoluidor.setEnabled(false);
        cbPotencialPoluidor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPotencialPoluidorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(cbPotencialPoluidor, gridBagConstraints);

        jLabel4.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltroAtividade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroAtividadeActionPerformed(evt);
            }
        });
        txtFiltroAtividade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroAtividadeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroAtividade, gridBagConstraints);

        btnFiltrar.setText("Pesquisar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(btnFiltrar, gridBagConstraints);

        tblAtividade.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblAtividade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAtividade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAtividade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAtividadeMouseClicked(evt);
            }
        });
        tblAtividade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAtividadeKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAtividade);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jScrollPane1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnNovo.setText("Novo");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        jPanel4.add(btnNovo);

        btnAlterar.setText("Alterar");
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });
        jPanel4.add(btnAlterar);

        btnExcluir.setText("Excluir");
        btnExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirActionPerformed(evt);
            }
        });
        jPanel4.add(btnExcluir);

        btnSalvar.setText("Salvar");
        btnSalvar.setEnabled(false);
        btnSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalvarActionPerformed(evt);
            }
        });
        jPanel4.add(btnSalvar);

        btnCancelar.setText("Cancelar");
        btnCancelar.setEnabled(false);
        btnCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelarActionPerformed(evt);
            }
        });
        jPanel4.add(btnCancelar);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroAtividade.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        AtividadeBD ruaAvBD = new AtividadeBD();
        if (ruaAvBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblAtividade.setEnabled(false);
            limpaSelecaoTabela();
            txtNome.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiAtividade();
            tblAtividade.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraAtividade();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblAtividade.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblAtividade.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Atividade da lista!", "Atividade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblAtividade.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Atividade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiAtividade();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Atividade da lista!", "Atividade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroAtividadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroAtividadeActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroAtividadeActionPerformed

    private void tblAtividadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAtividadeMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.PROCESSO) {
                selecionaAtividadeProcesso();
                dispose();
            } else if (modoSeleciona == Constantes.ALTERAR) {
                
                dispose();
                evt.consume();
            }
        }
    }//GEN-LAST:event_tblAtividadeMouseClicked

    private void tblAtividadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAtividadeKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             if (modo == Constantes.PROCESSO) {
                selecionaAtividadeProcesso();
                dispose();
            } else if (modoSeleciona == Constantes.ALTERAR) {
                dispose();
                evt.consume();
            }
        }
    }//GEN-LAST:event_tblAtividadeKeyPressed

    private void txtFiltroAtividadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroAtividadeFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroAtividadeFocusLost

    private void cbPotencialPoluidorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPotencialPoluidorActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbPotencialPoluidorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cbPotencialPoluidor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAtividade;
    private javax.swing.JTextField txtFiltroAtividade;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
