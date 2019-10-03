/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.logradouro;

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
import sip.pessoa.PessoaFrame;

/**
 *
 * @author T2Ti
 */
public final class LogradouroFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Logradouro> listLogradouro;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private AutorizacaoEventosFrame autorizacaoEventosFrame;
    private PessoaFrame pessoaFrame;
    
    

    /** Creates new form ClienteFrame */
    public LogradouroFrame() {
        initComponents();
        defineModelo();
        caixaAlta();
        fechando();
        tituloVisivel(jLabel1);

    }
    
    public LogradouroFrame(RequerenteFrame requerenteFrame) {
        initComponents();
        defineModelo();
          caixaAlta();
        this.requerenteFrame = requerenteFrame;
        txtFiltroLogradouro.requestFocus();
        modoSeleciona = Constantes.INCLUIR;
                 jLabel4.setVisible(false);
                 jLabel5.setVisible(false);
                 jLabel6.setVisible(false);
                 jLabel7.setVisible(false);
        
    }
    
    public LogradouroFrame(AutorizacaoEventosFrame autorizacaoEventosFrame) {
        initComponents();
        defineModelo();
          caixaAlta();
        this.autorizacaoEventosFrame = autorizacaoEventosFrame;
        txtFiltroLogradouro.requestFocus();
        modoSeleciona = Constantes.AUTORIZACAOEVENTOS_FRAME;
                 jLabel4.setVisible(false);
                 jLabel5.setVisible(false);
                 jLabel6.setVisible(false);
                 jLabel7.setVisible(false);
        
    }
    
    public LogradouroFrame(PessoaFrame pessoaFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.pessoaFrame = pessoaFrame;
        txtFiltroLogradouro.requestFocus();
        modoSeleciona = Constantes.PESSOA_FRAME;
                 jLabel4.setVisible(false);
                 jLabel5.setVisible(false);
                 jLabel6.setVisible(false);
                 jLabel7.setVisible(false);
        
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

    public void tituloVisivel(JLabel jlabel) {

        int num = 0;

        if (jlabel == jLabel1) {
            num = 1;
        }
        if (jlabel == jLabel4) {
            num = 4;
        }
        if (jlabel == jLabel5) {
            num = 5;
        }
        if (jlabel == jLabel6) {
            num = 6;
        }
        if (jlabel == jLabel7) {
            num = 7;
        }

        switch (num) {
            case 1:
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                break;
            case 4:
                jLabel1.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                break;
            case 5:
                jLabel1.setVisible(false);
                jLabel4.setVisible(false);
                jLabel6.setVisible(false);
                jLabel7.setVisible(false);
                break;
            case 6:
                jLabel1.setVisible(false);
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel7.setVisible(false);
                break;
            case 7:
                jLabel1.setVisible(false);
                jLabel4.setVisible(false);
                jLabel5.setVisible(false);
                jLabel6.setVisible(false);
                break;
        }

    }

    
    private void defineModelo() {
        tableModel = (DefaultTableModel) tblLogradouro.getModel();
        listModel = tblLogradouro.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheLogradouro();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblLogradouro.getColumnModel().getColumn(1).setPreferredWidth(700);
        } catch (ParseException ex) {
            Logger.getLogger(LogradouroFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        LogradouroBD logradouroBD = new LogradouroBD();

        if (txtFiltroLogradouro.getText().equals("")) {
            listLogradouro = logradouroBD.consultaLogradouro();
        } else {
            listLogradouro = logradouroBD.consultaLogradouro(txtFiltroLogradouro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listLogradouro.size(); i++) {
            tableModel.insertRow(i, new Object[]{listLogradouro.get(i).getId(), listLogradouro.get(i).getNome()});
        }

    }

    private void mostraDetalheLogradouro() {
        if (tblLogradouro.getSelectedRow() != -1) {
            int indice = tblLogradouro.getSelectedRow();
            txtNome.setText(listLogradouro.get(indice).getNome());

        } else {
            txtNome.setText("");

        }
    }

    private void limpaCampos() {
        {
            txtNome.setText("");

        }
    }

    private void incluiLogradouro() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Logradouro!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            Logradouro logradouro = new Logradouro();
            logradouro.setNome(txtNome.getText().trim());

            LogradouroBD agenteBD = new LogradouroBD();
            if (agenteBD.incluiLogradouro(logradouro)) {
                JOptionPane.showMessageDialog(this, "Logradouro cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Logradouro já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraLogradouro() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Logradouro!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
        } else {
            Logradouro logradouro = new Logradouro();
            logradouro.setId(this.listLogradouro.get(tblLogradouro.getSelectedRow()).getId());
            logradouro.setNome(txtNome.getText().trim());

            LogradouroBD logradouroBD = new LogradouroBD();
            if (logradouroBD.alteraLogradouro(logradouro)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiLogradouro() {
        LogradouroBD logradouroBD = new LogradouroBD();
        if (logradouroBD.excluiLogradouro(listLogradouro.get(tblLogradouro.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Logradouro excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o logradouro!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
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

    private void selecionaLogradouroRequerente() {
        if (tblLogradouro.getSelectedRow() != -1) {
            requerenteFrame.setLogradouro(listLogradouro.get(tblLogradouro.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Logradouro da lista!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void selecionaLogradouroAutorizacaoEventos() {
        if (tblLogradouro.getSelectedRow() != -1) {
            autorizacaoEventosFrame.setLogradouro(listLogradouro.get(tblLogradouro.getSelectedRow()));
            this.dispose();
            autorizacaoEventosFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Logradouro da lista!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
      private void selecionaLogradouroPessoa() {
        if (tblLogradouro.getSelectedRow() != -1) {
            pessoaFrame.setLogradouro(listLogradouro.get(tblLogradouro.getSelectedRow()));
            this.dispose();
            pessoaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Logradouro da lista!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
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
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroLogradouro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLogradouro = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Logradouro\n");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ROTA 64X64.png"))); // NOI18N
        jLabel1.setText("Logradouro");
        jPanel1.add(jLabel1);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ROTA 64X64.png"))); // NOI18N
        jLabel4.setText("Logradouro Frente");
        jPanel1.add(jLabel4);

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ROTA 64X64.png"))); // NOI18N
        jLabel5.setText("Logradouro Direita");
        jPanel1.add(jLabel5);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ROTA 64X64.png"))); // NOI18N
        jLabel6.setText("Logradouro Fundo");
        jPanel1.add(jLabel6);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ROTA 64X64.png"))); // NOI18N
        jLabel7.setText("Logradouro Esquerda");
        jPanel1.add(jLabel7);

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

        txtFiltroLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroLogradouroActionPerformed(evt);
            }
        });
        txtFiltroLogradouro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroLogradouroFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroLogradouro, gridBagConstraints);

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

        tblLogradouro.setFont(new java.awt.Font("Tahoma", 1, 14));
        tblLogradouro.setModel(new javax.swing.table.DefaultTableModel(
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
        tblLogradouro.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblLogradouro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLogradouroMouseClicked(evt);
            }
        });
        tblLogradouro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblLogradouroKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblLogradouro);

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
        txtFiltroLogradouro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        LogradouroBD ruaAvBD = new LogradouroBD();
        if (ruaAvBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblLogradouro.setEnabled(false);
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
            incluiLogradouro();
            tblLogradouro.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraLogradouro();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblLogradouro.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblLogradouro.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Logradouro da lista!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblLogradouro.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Logradouro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiLogradouro();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Logradouro da lista!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroLogradouroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroLogradouroActionPerformed

    private void tblLogradouroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLogradouroMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.INCLUIR) {
                selecionaLogradouroRequerente();
                dispose();
            } else if (modoSeleciona == Constantes.AUTORIZACAOEVENTOS_FRAME) {
                selecionaLogradouroAutorizacaoEventos();
                dispose();
                evt.consume();
            }else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                selecionaLogradouroPessoa();
                dispose();
                evt.consume();
            }
            
            
            
        }
    }//GEN-LAST:event_tblLogradouroMouseClicked

    private void tblLogradouroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLogradouroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             if (modoSeleciona == Constantes.INCLUIR) {
                selecionaLogradouroRequerente();
                dispose();
            } else if (modoSeleciona == Constantes.AUTORIZACAOEVENTOS_FRAME) {
                selecionaLogradouroAutorizacaoEventos();
                dispose();
                evt.consume();
            }else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                selecionaLogradouroPessoa();
                dispose();
                evt.consume();
            }
        }
    }//GEN-LAST:event_tblLogradouroKeyPressed

    private void txtFiltroLogradouroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroLogradouroFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroLogradouroFocusLost
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
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLogradouro;
    private javax.swing.JTextField txtFiltroLogradouro;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
