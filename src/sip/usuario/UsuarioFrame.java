/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.usuario;

import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.util.Constantes;
import sip.util.EncriptaSenha;
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
import sip.analise.AnaliseFrame;
import sip.analista.AnalistaFrame;

/**
 *
 * @author T2Ti
 */
public class UsuarioFrame extends javax.swing.JInternalFrame {
    
    AcessoBD conClientes;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Usuario> listUsuario;
    private AnalistaFrame analistaFrame;
  
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoSeleciona;
    
    public UsuarioFrame() {
        initComponents();
        defineModelo();
        caixaAlta();
        caixaBaixa();       
        fechando();
        
    }
    
      public UsuarioFrame(AnalistaFrame analistaFrame) {
        initComponents();
        modoSeleciona = Constantes.ANALISTA_FRAME;
        defineModelo();
        caixaAlta();
        caixaBaixa();       
        fechando();
        this.analistaFrame = analistaFrame;
        jPanel2.setVisible(false);
        jPanel4.setVisible(false);
       
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
        tableModel = (DefaultTableModel) tblUsuario.getModel();
        listModel = tblUsuario.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheUsuario();
                }
            }
        });
        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterCpf = new DefaultFormatterFactory(mascaraCpf);
            //ftfCpf.setFormatterFactory(formatterCpf);
            
            MaskFormatter mascaraTel = new MaskFormatter("(##)####-####");
            mascaraTel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterTel = new DefaultFormatterFactory(mascaraTel);
            //ftfTel.setFormatterFactory(formatterTel);

            

            tblUsuario.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblUsuario.getColumnModel().getColumn(1).setPreferredWidth(300);
            tblUsuario.getColumnModel().getColumn(2).setPreferredWidth(200);
            
            if(modoSeleciona == Constantes.ANALISTA_FRAME){
            tblUsuario.getColumnModel().getColumn(3).setMaxWidth(0);
            tblUsuario.getColumnModel().getColumn(3).setMinWidth(0);
            tblUsuario.getTableHeader().getColumnModel().getColumn(3).setMaxWidth(0);
            tblUsuario.getTableHeader().getColumnModel().getColumn(3).setMinWidth(0);
            }else{
                tblUsuario.getColumnModel().getColumn(3).setPreferredWidth(100);
            }
            
        } catch (ParseException ex) {
            Logger.getLogger(UsuarioFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        UsuarioBD usuarioBD = new UsuarioBD();

        if (txtFiltroUsuario.getText().equals("")) {
            listUsuario = usuarioBD.consultaUsuario();
        } else {
            listUsuario = usuarioBD.consultaUsuarioNome(txtFiltroUsuario.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
         for (int i = 0; i < numeroLinhas; i++) {
           tableModel.removeRow(0);
       }
         for (int i = 0; i < listUsuario.size(); i++) {
          tableModel.insertRow(i, new Object[]{
              listUsuario.get(i).getId(),
              listUsuario.get(i).getNome(),
              listUsuario.get(i).getSetor(),
              listUsuario.get(i).getLogin()
          });
         }
    }
    
     public void atualizaTabela2(String campo) {
        UsuarioBD usuarioBD = new UsuarioBD();

        if (campo.equals("")) {
            JOptionPane.showMessageDialog(this, "Digite o ID do Usuario","ID",JOptionPane.INFORMATION_MESSAGE);
        } else {
            listUsuario = usuarioBD.consultaUsuarioID(campo);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listUsuario.size(); i++) {
           tableModel.insertRow(i, new Object[]{
               listUsuario.get(i).getId(),
               listUsuario.get(i).getNome(),
               listUsuario.get(i).getSetor(),
               listUsuario.get(i).getLogin()
           });
        }
    }
    
    
    private void limpaSelecaoTabela(){
         int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }
   
    private void mostraDetalheUsuario() {
        if (tblUsuario.getSelectedRow() != -1) {
            int indice = tblUsuario.getSelectedRow();
            txtNomeUsuario.setText(listUsuario.get(indice).getNome());
            txtLogin.setText(listUsuario.get(indice).getLogin());
            cbSetor.setSelectedItem(listUsuario.get(indice).getSetor());

        } else {
            txtNomeUsuario.setText("");
            txtLogin.setText("");
            jPSenha.setText("");
            cbSetor.setSelectedItem("SELECIONE");

        }
    }
    
    
    private void limpaCampos(){
        {
            
            txtNomeUsuario.setText("");
            txtLogin.setText("");
            jPSenha.setText("");
            cbSetor.setSelectedItem("SELECIONE");
        }  
    }

    private void incluiUsuario() {
    if ((txtNomeUsuario.getText().trim().equals("")))// || (ftfCpf.getText().equals("   .   .   -  ")) || (ftfTel.getText().equals("(  )    -    "))) 
            {
            JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
          //  txtNomeCliente.requestFocus();
          }
        else if(cbSetor.getSelectedItem().equals("SELECIONE")){
            JOptionPane.showMessageDialog(this, "Informe o Setor!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            cbSetor.requestFocus();
       }     
            
  
      else {
            Usuario usuario = new Usuario();
            usuario.setNome(txtNomeUsuario.getText().trim());
            usuario.setLogin(txtLogin.getText().trim());
            usuario.setSenha(jPSenha.getText().trim());
            usuario.setSetor(cbSetor.getSelectedItem().toString());


            UsuarioBD usuarioBD = new UsuarioBD();
            if (usuarioBD.incluiUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Usuario cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Usuario já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraUsuario() {
    if ((txtNomeUsuario.getText().trim().equals("")) || (txtLogin.getText().trim().equals("")) || jPSenha.getText().equals(""))// || (ftfTel.getText().equals("(  )    -    ")))
    {
            JOptionPane.showMessageDialog(this, "O campo Nome Login e Senha\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
          //  txtNomeCliente.requestFocus(); 
    } else if(cbSetor.getSelectedItem().equals("SELECIONE")){
            JOptionPane.showMessageDialog(this, "Informe o Setor!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            cbSetor.requestFocus();
       }   
    
    
    
    else {
            Usuario usuario = new Usuario();
            usuario.setId(listUsuario.get(tblUsuario.getSelectedRow()).getId());
            usuario.setNome(txtNomeUsuario.getText().trim());
            usuario.setLogin(txtLogin.getText().trim());
            usuario.setSenha(jPSenha.getText().trim());
            usuario.setSetor(cbSetor.getSelectedItem().toString());

     

            UsuarioBD usuarioBD = new UsuarioBD();
            if (usuarioBD.alteraUsuario(usuario)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiUsuario() {
        
        UsuarioBD usuarioBD = new UsuarioBD();
        if (usuarioBD.excluiUsuario(listUsuario.get(tblUsuario.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do usuario excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esse usuario!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
        private void caixaAlta(){
        txtNomeUsuario.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
       }
        
        private void caixaBaixa(){
         txtLogin.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toLowerCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
         jPSenha.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toLowerCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
          
        }

        private void habilitaCampos() {    
        txtNomeUsuario.setEditable(true);
        txtLogin.setEditable(true);
        jPSenha.setEditable(true);
        cbSetor.setEnabled(true);

    }

    private void desabilitaCampos() {
        txtNomeUsuario.setEditable(false);
        txtLogin.setEditable(false);
        jPSenha.setEditable(false);
        cbSetor.setEnabled(true);
 
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
    
     private void selecionaUsuarioAnalistaFrame(){
        if (tblUsuario.getSelectedRow() != -1) {
            analistaFrame.setUsuario(listUsuario.get(tblUsuario.getSelectedRow()));
            this.dispose();
            analistaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Usuario da lista!", "Usuario", JOptionPane.INFORMATION_MESSAGE);
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
        txtNomeUsuario = new javax.swing.JTextField();
        txtLogin = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jPSenha = new javax.swing.JPasswordField();
        jPanel9 = new javax.swing.JPanel();
        cbSetor = new javax.swing.JComboBox();
        jLabel6 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroUsuario = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblUsuario = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Usuario");
        setPreferredSize(new java.awt.Dimension(600, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/usuario_64x64.png"))); // NOI18N
        jLabel1.setText("USUARIO");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Senha");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel3, gridBagConstraints);

        txtNomeUsuario.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNomeUsuario, gridBagConstraints);

        txtLogin.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtLogin, gridBagConstraints);

        jLabel4.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Login");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        jPanel2.add(jLabel5, gridBagConstraints);

        jPSenha.setEditable(false);
        jPSenha.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jPSenhaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPSenha, gridBagConstraints);

        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder("Setor"));
        jPanel9.setLayout(new java.awt.GridBagLayout());

        cbSetor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbSetor.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "PROTOCOLO", "DMA", "LICENCIAMENTO", "FISCALIZAÇÃO", "DIGITAÇÃO", "ANÁLISE", "JURIDICO", "ADMINISTRAÇÃO", "ADMIN" }));
        cbSetor.setEnabled(false);
        cbSetor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSetorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(cbSetor, gridBagConstraints);

        jLabel6.setText("Setor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanel2.add(jPanel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 5.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Pelo Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltroUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroUsuarioMouseClicked(evt);
            }
        });
        txtFiltroUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroUsuarioActionPerformed(evt);
            }
        });
        txtFiltroUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroUsuarioFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroUsuario, gridBagConstraints);

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

        tblUsuario.setFont(new java.awt.Font("Tahoma", 0, 14));
        tblUsuario.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Setor", "Login"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblUsuario.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblUsuario.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUsuarioMouseClicked(evt);
            }
        });
        tblUsuario.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblUsuarioKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblUsuario);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroUsuario.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
       UsuarioBD usuarioBD = new UsuarioBD();
        if(usuarioBD.testaConexao()){
             habilitaCampos();
             limpaCampos();
             btnFiltrar.setEnabled(false);
             tblUsuario.setEnabled(false);
             limpaSelecaoTabela();
             txtNomeUsuario.requestFocus();
             habilitaBotoes();
             modo = Constantes.INSERT_MODE;
             caixaAlta();
             caixaBaixa();
         }
            else
                JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiUsuario(); 
            tblUsuario.setEnabled(true);
           // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraUsuario();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblUsuario.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblUsuario.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuario da lista!", "Cliente", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblUsuario.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Usuario?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiUsuario();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuario da lista!", "Usuario", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroUsuarioMouseClicked
      
    }//GEN-LAST:event_txtFiltroUsuarioMouseClicked

    private void txtFiltroUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroUsuarioActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroUsuarioActionPerformed

    private void txtFiltroUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroUsuarioFocusLost
         atualizaTabela();
    }//GEN-LAST:event_txtFiltroUsuarioFocusLost

    private void tblUsuarioMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUsuarioMouseClicked
        if (evt.getClickCount() == 2) {
             if (modoSeleciona == Constantes.ANALISTA_FRAME) {
                selecionaUsuarioAnalistaFrame();
                dispose();
         
            evt.consume();
        }
        }// TODO add your handling code here:
    }//GEN-LAST:event_tblUsuarioMouseClicked

    private void tblUsuarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblUsuarioKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modoSeleciona == Constantes.ANALISTA_FRAME) {
                selecionaUsuarioAnalistaFrame();
                dispose();
   
            }
        }
    }//GEN-LAST:event_tblUsuarioKeyPressed

    private void jPSenhaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jPSenhaFocusLost
      jPSenha.setText(EncriptaSenha.encripta(jPSenha.getText()));
    }//GEN-LAST:event_jPSenhaFocusLost

    private void cbSetorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSetorActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbSetorActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JComboBox cbSetor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPasswordField jPSenha;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblUsuario;
    private javax.swing.JTextField txtFiltroUsuario;
    private javax.swing.JTextField txtLogin;
    private javax.swing.JTextField txtNomeUsuario;
    // End of variables declaration//GEN-END:variables


}
