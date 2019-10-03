/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 * 
 * 
 * 
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.cidade;

import sip.menulogin.Menu;
import sip.requerente.RequerenteFrame;
import sip.util.Constantes;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import sip.pessoa.PessoaFrame;

/**
 *
 * @author T2Ti
 */
public class CidadeFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Cidade> listaCidade;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private PessoaFrame pessoaFrame;
    
    

    /** Creates new form ClienteFrame */
    public CidadeFrame() {
        initComponents();
        defineModelo();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        caixaAlta();
        fechando();
    }
        
       
    public CidadeFrame(RequerenteFrame requerenteFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        this.requerenteFrame = requerenteFrame;
        modoSeleciona = Constantes.INCLUIR;
    }
    
     public CidadeFrame(PessoaFrame pessoaFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        this.pessoaFrame = pessoaFrame;
        modoSeleciona = Constantes.PESSOA_FRAME;
    }
    
  
    
     private void fechando() {  
          this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  
         
        this.addInternalFrameListener(  
            new InternalFrameAdapter(){  
            @Override
                public void internalFrameClosing(InternalFrameEvent e) {  
                       dispose();
                       Menu.menuVisivel();
                     
              }  
            }
          );
       }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblCidade.getModel();
        listModel = tblCidade.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheCidade();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
           // ftfCep.setFormatterFactory(formatter);

            tblCidade.getColumnModel().getColumn(1).setPreferredWidth(700);
        } catch (ParseException ex) {
            Logger.getLogger(CidadeFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        CidadeBD CidadeBD = new CidadeBD();

        if (txtFiltroCidade.getText().equals("")) {
            listaCidade = CidadeBD.consultaCidade();
        } else {
            listaCidade = CidadeBD.consultaCidade(txtFiltroCidade.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listaCidade.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                listaCidade.get(i).getId(), 
                listaCidade.get(i).getNome(),
                listaCidade.get(i).getUf()
            });
        }
   
    }

    private void mostraDetalheCidade() {
        if (tblCidade.getSelectedRow() != -1) {
            int indice = tblCidade.getSelectedRow();
            txtNome.setText(listaCidade.get(indice).getNome());
            cbUf.setSelectedItem(listaCidade.get(indice).getUf());
           
        } else {
            txtNome.setText("");
        }
    }
    
     private void limpaCampos(){
        {
            txtNome.setText("");
            cbUf.setSelectedItem(" ");
        }  
    }

    private void incluiCidade() {
        if (txtNome.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do Cidade!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
         }
                
          if (cbUf.getSelectedItem().equals(" ")){
            JOptionPane.showMessageDialog(this, "Informe a UF", "Uf", JOptionPane.INFORMATION_MESSAGE);
            cbUf.requestFocus();
         }
       else {
            Cidade cidade = new Cidade();
            cidade.setNome(txtNome.getText().trim());
            cidade.setUf(cbUf.getSelectedItem().toString());

            CidadeBD agenteBD = new CidadeBD();
            if (agenteBD.incluiCidade(cidade)) {
                JOptionPane.showMessageDialog(this, "Cidade cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Cidade já existe !", "Erro", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }

    private void alteraCidade() {
      if (txtNome.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do Cidade!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
         }
              
        if (cbUf.getSelectedItem().equals(" ")){
            JOptionPane.showMessageDialog(this, "Informe a UF", "UF", JOptionPane.INFORMATION_MESSAGE);
            cbUf.requestFocus();
         }
      else {
            Cidade cidade = new Cidade();
            cidade.setId(this.listaCidade.get(tblCidade.getSelectedRow()).getId());
            cidade.setNome(txtNome.getText().trim());
            //fornecedor.setEndereco(txtEndereco.getText().trim());
            //fornecedor.setCidade(txtCidade.getText().trim());
           // fornecedor.setCidade(txtCidade.getText().trim());
            cidade.setUf(cbUf.getSelectedItem().toString());
           // fornecedor.setCep((String) ftfCep.getValue());
           // fornecedor.setTelefone(txtTelefone.getText());
           // fornecedor.setEmail(txtEmail.getText());

            CidadeBD cidadeBD = new CidadeBD();
            if (cidadeBD.alteraCidade(cidade)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiCidade() {
        CidadeBD cidadeBD = new CidadeBD();
        if (cidadeBD.excluiCidade(listaCidade.get(tblCidade.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Cidade excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o cidade!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta(){
        txtNome.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }
    
       
    private void habilitaCampos() {        
        txtNome.setEditable(true);
        //txtEndereco.setEditable(true);
        //txtCidade.setEditable(true);
        //txtCidade.setEditable(true);
        cbUf.setEnabled(true);
        //ftfCep.setEditable(true);
       // txtTelefone.setEditable(true);
        //txtEmail.setEditable(true);
    }

    private void desabilitaCampos() {
        txtNome.setEditable(false);
        //txtEndereco.setEditable(false);
        //txtCidade.setEditable(false);
        //txtCidade.setEditable(false);
        cbUf.setEnabled(false);
        //ftfCep.setEditable(false);
        //txtTelefone.setEditable(false);
       // txtEmail.setEditable(false);
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
  
    private void limpaSelecaoTabela(){
         int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }
    
    private void selecionaCidadeRequerente(){
        if (tblCidade.getSelectedRow() != -1) {
            requerenteFrame.setCidade(listaCidade.get(tblCidade.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
     private void selecionaCidadePessoa(){
        if (tblCidade.getSelectedRow() != -1) {
            pessoaFrame.setCidade(listaCidade.get(tblCidade.getSelectedRow()));
            this.dispose();
            pessoaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
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
        txtNome = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroCidade = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblCidade = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnSelecionaCarro1 = new javax.swing.JButton();
        btnSelecionaCarro = new javax.swing.JButton();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbUf = new javax.swing.JComboBox();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Cidade");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/cidade_64x64.png"))); // NOI18N
        jLabel1.setText("Cidade");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        txtNome.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNome, gridBagConstraints);

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

        txtFiltroCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroCidadeActionPerformed(evt);
            }
        });
        txtFiltroCidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroCidadeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroCidade, gridBagConstraints);

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

        tblCidade.setFont(new java.awt.Font("Tahoma", 1, 14));
        tblCidade.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Uf"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblCidade.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblCidade.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblCidadeMouseClicked(evt);
            }
        });
        tblCidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblCidadeKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblCidade);

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

        btnSelecionaCarro1.setText("Seleciona Sigla");
        btnSelecionaCarro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaCarro1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnSelecionaCarro1);

        btnSelecionaCarro.setText("Seleciona Sigla");
        btnSelecionaCarro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaCarroActionPerformed(evt);
            }
        });
        jPanel4.add(btnSelecionaCarro);

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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(0, 230, 0, 0);
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Uf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel5, gridBagConstraints);

        cbUf.setModel(new javax.swing.DefaultComboBoxModel(new String[] { " ", "AC", "AL", "AM", "AP", "BA", "CE", "DF", "ES", "GO", "MA", "MG", "MS", "MT", "PA", "PB", "PE", "PI", "PR", "RJ", "RN", "RS", "RO", "RR", "SC", "SE", "SP", "TO" }));
        cbUf.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbUfActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 5);
        jPanel5.add(cbUf, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel5, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroCidade.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        CidadeBD cidadeBD = new CidadeBD();
            if(cidadeBD.testaConexao()){
                habilitaCampos();
                limpaCampos();
                btnFiltrar.setEnabled(false);
                tblCidade.setEnabled(false);
                limpaSelecaoTabela();
                txtNome.requestFocus();
                habilitaBotoes();
                modo = Constantes.INSERT_MODE;
                //caixaAlta();
            }
            else
                JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiCidade();
            tblCidade.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraCidade();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblCidade.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblCidade.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblCidade.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Cidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiCidade();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnSelecionaCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCarroActionPerformed
        if (tblCidade.getSelectedRow() != -1) {
           // motoristaFrame.setEmpresa(cidade.get(tblCidade.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSelecionaCarroActionPerformed

    private void txtFiltroCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroCidadeActionPerformed
       atualizaTabela();
    }//GEN-LAST:event_txtFiltroCidadeActionPerformed

    private void btnSelecionaCarro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCarro1ActionPerformed
      if (tblCidade.getSelectedRow() != -1) {
            //relAgPeriodo.setAgencia(carro.get(tblCarro.getSelectedRow()));
            this.dispose();
            //relAgPeriodo.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cidade da lista!", "Cidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSelecionaCarro1ActionPerformed

    private void tblCidadeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblCidadeMouseClicked
        if (evt.getClickCount() == 2) {  
           if (modoSeleciona == Constantes.INCLUIR) {
                 selecionaCidadeRequerente();
                       dispose();  
           } else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                 selecionaCidadePessoa();
                  dispose();                      
           evt.consume();  
          } 
        } 
    }//GEN-LAST:event_tblCidadeMouseClicked

    private void tblCidadeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblCidadeKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            if (modo == Constantes.INCLUIR) {
                 selecionaCidadeRequerente();
                       dispose();  
           } else if (modo == Constantes.PESSOA_FRAME) {
                 selecionaCidadePessoa();
                  dispose();                      
           evt.consume();  
 
        } 
        }  
    }//GEN-LAST:event_tblCidadeKeyPressed

    private void txtFiltroCidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroCidadeFocusLost
       atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroCidadeFocusLost

    private void cbUfActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbUfActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbUfActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaCarro;
    private javax.swing.JButton btnSelecionaCarro1;
    private javax.swing.JComboBox cbUf;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblCidade;
    private javax.swing.JTextField txtFiltroCidade;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
