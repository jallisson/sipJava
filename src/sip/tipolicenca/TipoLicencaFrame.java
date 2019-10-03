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
package sip.tipolicenca;

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
public class TipoLicencaFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<TipoLicenca> listaTipoLicenca;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private PessoaFrame pessoaFrame;
    
    

    /** Creates new form ClienteFrame */
    public TipoLicencaFrame() {
        initComponents();
        defineModelo();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        caixaAlta();
        fechando();
    }
        
       
    public TipoLicencaFrame(RequerenteFrame requerenteFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        this.requerenteFrame = requerenteFrame;
        modoSeleciona = Constantes.INCLUIR;
    }
    
     public TipoLicencaFrame(PessoaFrame pessoaFrame) {
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
        tableModel = (DefaultTableModel) tblTipoLicenca.getModel();
        listModel = tblTipoLicenca.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheTipoLicenca();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
           // ftfCep.setFormatterFactory(formatter);

            tblTipoLicenca.getColumnModel().getColumn(1).setPreferredWidth(700);
        } catch (ParseException ex) {
            Logger.getLogger(TipoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        TipoLicencaBD TipoLicencaBD = new TipoLicencaBD();

        if (txtFiltroTipoLicenca.getText().equals("")) {
            listaTipoLicenca = TipoLicencaBD.consultaTipoLicenca();
        } else {
            listaTipoLicenca = TipoLicencaBD.consultaTipoLicenca(txtFiltroTipoLicenca.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listaTipoLicenca.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                listaTipoLicenca.get(i).getId(), 
                listaTipoLicenca.get(i).getNome(),
                listaTipoLicenca.get(i).getAbreviatura()
            });
        }
   
    }

    private void mostraDetalheTipoLicenca() {
        if (tblTipoLicenca.getSelectedRow() != -1) {
            int indice = tblTipoLicenca.getSelectedRow();
            txtNome.setText(listaTipoLicenca.get(indice).getNome());
            txtAbreviatura.setText(listaTipoLicenca.get(indice).getAbreviatura());
           
        } else {
            txtNome.setText("");
            txtAbreviatura.setText("");
        }
    }
    
     private void limpaCampos(){
        {
            txtNome.setText("");
            txtAbreviatura.setText("");
        }  
    }

    private void incluiTipoLicenca() {
        if (txtNome.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do TipoLicenca!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
         }
                
           if (txtAbreviatura.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe a Abreviatura!", "Abreviatura", JOptionPane.INFORMATION_MESSAGE);
            txtAbreviatura.requestFocus();
         }
       else {
            TipoLicenca tipoLicenca = new TipoLicenca();
            tipoLicenca.setNome(txtNome.getText().trim());
            tipoLicenca.setAbreviatura(txtAbreviatura.getText().trim());

            TipoLicencaBD agenteBD = new TipoLicencaBD();
            if (agenteBD.incluiTipoLicenca(tipoLicenca)) {
                JOptionPane.showMessageDialog(this, "TipoLicenca cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o TipoLicenca já existe !", "Erro", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }

    private void alteraTipoLicenca() {
      if (txtNome.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do TipoLicenca!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
         }
              
        if (txtAbreviatura.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe a Abreviatura!", "Abreviatura", JOptionPane.INFORMATION_MESSAGE);
            txtAbreviatura.requestFocus();
         }
      else {
            TipoLicenca tipoLicenca = new TipoLicenca();
            tipoLicenca.setId(this.listaTipoLicenca.get(tblTipoLicenca.getSelectedRow()).getId());
            tipoLicenca.setNome(txtNome.getText().trim());
            //fornecedor.setEndereco(txtEndereco.getText().trim());
            //fornecedor.setTipoLicenca(txtTipoLicenca.getText().trim());
           // fornecedor.setTipoLicenca(txtTipoLicenca.getText().trim());
            tipoLicenca.setAbreviatura(txtAbreviatura.getText().trim());
           // fornecedor.setCep((String) ftfCep.getValue());
           // fornecedor.setTelefone(txtTelefone.getText());
           // fornecedor.setEmail(txtEmail.getText());

            TipoLicencaBD tipoLicencaBD = new TipoLicencaBD();
            if (tipoLicencaBD.alteraTipoLicenca(tipoLicenca)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiTipoLicenca() {
        TipoLicencaBD tipoLicencaBD = new TipoLicencaBD();
        if (tipoLicencaBD.excluiTipoLicenca(listaTipoLicenca.get(tblTipoLicenca.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do TipoLicenca excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o tipoLicenca!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta(){
        txtNome.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
        txtAbreviatura.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
   
    }
    
       
    private void habilitaCampos() {        
        txtNome.setEditable(true);
        //txtEndereco.setEditable(true);
        //txtTipoLicenca.setEditable(true);
        //txtTipoLicenca.setEditable(true);
        txtAbreviatura.setEditable(true);
        //ftfCep.setEditable(true);
       // txtTelefone.setEditable(true);
        //txtEmail.setEditable(true);
    }

    private void desabilitaCampos() {
        txtNome.setEditable(false);
        //txtEndereco.setEditable(false);
        //txtTipoLicenca.setEditable(false);
        //txtTipoLicenca.setEditable(false);
         txtNome.setEditable(false);
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
    
    private void selecionaTipoLicencaRequerente(){
        if (tblTipoLicenca.getSelectedRow() != -1) {
            //requerenteFrame.setTipoLicenca(listaTipoLicenca.get(tblTipoLicenca.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
     private void selecionaTipoLicencaPessoa(){
        if (tblTipoLicenca.getSelectedRow() != -1) {
            //pessoaFrame.setTipoLicenca(listaTipoLicenca.get(tblTipoLicenca.getSelectedRow()));
            this.dispose();
            pessoaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
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
        txtFiltroTipoLicenca = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTipoLicenca = new javax.swing.JTable();
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
        txtAbreviatura = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Tipo Licença");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/9433_64x64.png"))); // NOI18N
        jLabel1.setText("Tipo Licença");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        txtNome.setEditable(false);
        txtNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeActionPerformed(evt);
            }
        });
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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
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

        txtFiltroTipoLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroTipoLicencaActionPerformed(evt);
            }
        });
        txtFiltroTipoLicenca.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroTipoLicencaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroTipoLicenca, gridBagConstraints);

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

        tblTipoLicenca.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblTipoLicenca.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Abreviatura"
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
        tblTipoLicenca.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTipoLicenca.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTipoLicencaMouseClicked(evt);
            }
        });
        tblTipoLicenca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTipoLicencaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblTipoLicenca);

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

        jLabel5.setText("Abreviatura");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel5, gridBagConstraints);

        txtAbreviatura.setEditable(false);
        txtAbreviatura.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtAbreviaturaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(txtAbreviatura, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel5, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroTipoLicenca.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        TipoLicencaBD tipoLicencaBD = new TipoLicencaBD();
            if(tipoLicencaBD.testaConexao()){
                habilitaCampos();
                limpaCampos();
                btnFiltrar.setEnabled(false);
                tblTipoLicenca.setEnabled(false);
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
            incluiTipoLicenca();
            tblTipoLicenca.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraTipoLicenca();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblTipoLicenca.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblTipoLicenca.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblTipoLicenca.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do TipoLicenca?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiTipoLicenca();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void btnSelecionaCarroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCarroActionPerformed
        if (tblTipoLicenca.getSelectedRow() != -1) {
           // motoristaFrame.setEmpresa(tipoLicenca.get(tblTipoLicenca.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSelecionaCarroActionPerformed

    private void txtFiltroTipoLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroTipoLicencaActionPerformed
       atualizaTabela();
    }//GEN-LAST:event_txtFiltroTipoLicencaActionPerformed

    private void btnSelecionaCarro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCarro1ActionPerformed
      if (tblTipoLicenca.getSelectedRow() != -1) {
            //relAgPeriodo.setAgencia(carro.get(tblCarro.getSelectedRow()));
            this.dispose();
            //relAgPeriodo.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um tipoLicenca da lista!", "TipoLicenca", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnSelecionaCarro1ActionPerformed

    private void tblTipoLicencaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTipoLicencaMouseClicked
        if (evt.getClickCount() == 2) {  
           if (modoSeleciona == Constantes.INCLUIR) {
                 selecionaTipoLicencaRequerente();
                       dispose();  
           } else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                 selecionaTipoLicencaPessoa();
                  dispose();                      
           evt.consume();  
          } 
        } 
    }//GEN-LAST:event_tblTipoLicencaMouseClicked

    private void tblTipoLicencaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTipoLicencaKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            if (modo == Constantes.INCLUIR) {
                 selecionaTipoLicencaRequerente();
                       dispose();  
           } else if (modo == Constantes.PESSOA_FRAME) {
                 selecionaTipoLicencaPessoa();
                  dispose();                      
           evt.consume();  
 
        } 
        }  
    }//GEN-LAST:event_tblTipoLicencaKeyPressed

    private void txtFiltroTipoLicencaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroTipoLicencaFocusLost
       atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroTipoLicencaFocusLost

    private void txtNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeActionPerformed

    private void txtAbreviaturaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtAbreviaturaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtAbreviaturaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaCarro;
    private javax.swing.JButton btnSelecionaCarro1;
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
    private javax.swing.JTable tblTipoLicenca;
    private javax.swing.JTextField txtAbreviatura;
    private javax.swing.JTextField txtFiltroTipoLicenca;
    private javax.swing.JTextField txtNome;
    // End of variables declaration//GEN-END:variables
}
