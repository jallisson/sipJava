/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.naturezaocorrencia;

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
import sip.autorizacaoeventos.AutorizacaoEventosFrame;
import sip.denuncia.DenunciaFrame;
import sip.pessoa.PessoaFrame;

/**
 *
 * @author T2Ti
 */
public final class NaturezaOcorrenciaFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<NaturezaOcorrencia> listNaturezaOcorrencia;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private AutorizacaoEventosFrame autorizacaoEventosFrame;
    private PessoaFrame pessoaFrame;
    private DenunciaFrame ocorrenciaFrame;
    
    

    /** Creates new form ClienteFrame */
    public NaturezaOcorrenciaFrame() {
        initComponents();
        defineModelo();
        caixaAlta();
        fechando();

    }
    
    public NaturezaOcorrenciaFrame(DenunciaFrame ocorrenciaFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.ocorrenciaFrame = ocorrenciaFrame;
        txtFiltroNaturezaOcorrencia.requestFocus();
        modoSeleciona = Constantes.OCORRENCIA_FRAME;
        
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

    
    
    private void defineModelo() {
        tableModel = (DefaultTableModel) tblNaturezaOcorrencia.getModel();
        listModel = tblNaturezaOcorrencia.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheNaturezaOcorrencia();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblNaturezaOcorrencia.getColumnModel().getColumn(1).setPreferredWidth(700);
        } catch (ParseException ex) {
            Logger.getLogger(NaturezaOcorrenciaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        NaturezaOcorrenciaBD NaturezaOcorrenciaBD = new NaturezaOcorrenciaBD();

        if (txtFiltroNaturezaOcorrencia.getText().equals("")) {
            listNaturezaOcorrencia = NaturezaOcorrenciaBD.consultaNaturezaOcorrencia();
        } else {
            listNaturezaOcorrencia = NaturezaOcorrenciaBD.consultaNaturezaOcorrenciaNome(txtFiltroNaturezaOcorrencia.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listNaturezaOcorrencia.size(); i++) {
            tableModel.insertRow(i, new Object[]{listNaturezaOcorrencia.get(i).getId(), listNaturezaOcorrencia.get(i).getNome()});
        }

    }

    private void mostraDetalheNaturezaOcorrencia() {
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            int indice = tblNaturezaOcorrencia.getSelectedRow();
            txtNome.setText(listNaturezaOcorrencia.get(indice).getNome());

        } else {
            txtNome.setText("");

        }
    }

    private void limpaCampos() {
        {
            txtNome.setText("");

        }
    }

    private void incluiNaturezaOcorrencia() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do NaturezaOcorrencia!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            NaturezaOcorrencia NaturezaOcorrencia = new NaturezaOcorrencia();
            NaturezaOcorrencia.setNome(txtNome.getText().trim());

            NaturezaOcorrenciaBD agenteBD = new NaturezaOcorrenciaBD();
            if (agenteBD.incluiNaturezaOcorrencia(NaturezaOcorrencia)) {
                JOptionPane.showMessageDialog(this, "NaturezaOcorrencia cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o NaturezaOcorrencia já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraNaturezaOcorrencia() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do NaturezaOcorrencia!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            NaturezaOcorrencia NaturezaOcorrencia = new NaturezaOcorrencia();
            NaturezaOcorrencia.setId(this.listNaturezaOcorrencia.get(tblNaturezaOcorrencia.getSelectedRow()).getId());
            NaturezaOcorrencia.setNome(txtNome.getText().trim());

            NaturezaOcorrenciaBD NaturezaOcorrenciaBD = new NaturezaOcorrenciaBD();
            if (NaturezaOcorrenciaBD.alteraNaturezaOcorrencia(NaturezaOcorrencia)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiNaturezaOcorrencia() {
        NaturezaOcorrenciaBD NaturezaOcorrenciaBD = new NaturezaOcorrenciaBD();
        if (NaturezaOcorrenciaBD.excluiNaturezaOcorrencia(listNaturezaOcorrencia.get(tblNaturezaOcorrencia.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do NaturezaOcorrencia excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o NaturezaOcorrencia!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtNome.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
       }

    private void habilitaCampos() {
        txtNome.setEditable(true);

    }

    private void desabilitaCampos() {
        txtNome.setEditable(false);
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

    private void selecionaNaturezaOcorrenciaOcorrencia() {
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            ocorrenciaFrame.setNaturezaOcorrencia(listNaturezaOcorrencia.get(tblNaturezaOcorrencia.getSelectedRow()));
            this.dispose();
            ocorrenciaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione dados da lista!", "Natureza Ocorrencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void selecionaNaturezaOcorrenciaAutorizacaoEventos() {
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            //autorizacaoEventosFrame.setNaturezaOcorrencia(listNaturezaOcorrencia.get(tblNaturezaOcorrencia.getSelectedRow()));
            this.dispose();
            autorizacaoEventosFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um NaturezaOcorrencia da lista!", "NaturezaOcorrencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
      private void selecionaNaturezaOcorrenciaPessoa() {
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            //pessoaFrame.setNaturezaOcorrencia(listNaturezaOcorrencia.get(tblNaturezaOcorrencia.getSelectedRow()));
            this.dispose();
            pessoaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um NaturezaOcorrencia da lista!", "NaturezaOcorrencia", JOptionPane.INFORMATION_MESSAGE);
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
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroNaturezaOcorrencia = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblNaturezaOcorrencia = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Natureza Ocorrência");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/NATUREZA_64x64.png"))); // NOI18N
        jLabel1.setText("Natureza Ocorrência");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Logradouro");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        txtNome.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNome, gridBagConstraints);

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

        txtFiltroNaturezaOcorrencia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroNaturezaOcorrenciaActionPerformed(evt);
            }
        });
        txtFiltroNaturezaOcorrencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroNaturezaOcorrenciaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroNaturezaOcorrencia, gridBagConstraints);

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

        tblNaturezaOcorrencia.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblNaturezaOcorrencia.setModel(new javax.swing.table.DefaultTableModel(
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
        tblNaturezaOcorrencia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblNaturezaOcorrencia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblNaturezaOcorrenciaMouseClicked(evt);
            }
        });
        tblNaturezaOcorrencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblNaturezaOcorrenciaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblNaturezaOcorrencia);

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
        txtFiltroNaturezaOcorrencia.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        NaturezaOcorrenciaBD ruaAvBD = new NaturezaOcorrenciaBD();
        if (ruaAvBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblNaturezaOcorrencia.setEnabled(false);
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
            incluiNaturezaOcorrencia();
            tblNaturezaOcorrencia.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraNaturezaOcorrencia();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblNaturezaOcorrencia.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um NaturezaOcorrencia da lista!", "NaturezaOcorrencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblNaturezaOcorrencia.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do NaturezaOcorrencia?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiNaturezaOcorrencia();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um NaturezaOcorrencia da lista!", "NaturezaOcorrencia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroNaturezaOcorrenciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroNaturezaOcorrenciaActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroNaturezaOcorrenciaActionPerformed

    private void tblNaturezaOcorrenciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblNaturezaOcorrenciaMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.OCORRENCIA_FRAME) {
                selecionaNaturezaOcorrenciaOcorrencia();
                dispose();
            }
            evt.consume();
        }
    }//GEN-LAST:event_tblNaturezaOcorrenciaMouseClicked

    private void tblNaturezaOcorrenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblNaturezaOcorrenciaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             if (modoSeleciona == Constantes.OCORRENCIA_FRAME) {
                selecionaNaturezaOcorrenciaOcorrencia();
                dispose();
            }
             evt.consume();
        }
    }//GEN-LAST:event_tblNaturezaOcorrenciaKeyPressed

    private void txtFiltroNaturezaOcorrenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroNaturezaOcorrenciaFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroNaturezaOcorrenciaFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblNaturezaOcorrencia;
    private javax.swing.JTextField txtFiltroNaturezaOcorrencia;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
