/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.analista;

import sip.menulogin.Menu;
import sip.requerente.RequerenteFrame;
import sip.usuario.Usuario;
import sip.util.Constantes;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
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
import javax.swing.text.NumberFormatter;
import sip.distribuicao.DistribuicaoFrame;
import sip.relatorio.RelProcAnalistaDistFrame;
import sip.usuario.UsuarioBD;
import sip.usuario.UsuarioFrame;

/**
 *
 * @author T2Ti
 */
public class AnalistaFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Analista> listAnalista;
    private int modo;
    private int modoSeleciona;
    private DistribuicaoFrame distFrame;
    private RelProcAnalistaDistFrame relAnalistaDist;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    
    

    /** Creates new form ClienteFrame */
    public AnalistaFrame() {
        initComponents();
        defineModelo();
       
        caixaAlta();
        fechando();
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

    public AnalistaFrame(DistribuicaoFrame distFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.distFrame = distFrame;
        modoSeleciona = Constantes.DISTRIBUICAO_FRAME;
    }
    
    public AnalistaFrame(RelProcAnalistaDistFrame relAnalistaDist) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.relAnalistaDist = relAnalistaDist;
        modoSeleciona = Constantes.RELPROCANALISTADIST_FRAME;
    }
    

    private void defineModelo() {
       
        tableModel = (DefaultTableModel) tblAnalista.getModel();
        listModel = tblAnalista.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheAnalista();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
           // ftfCep.setFormatterFactory(formatter);a
            
            DecimalFormat formatoNumeral = new DecimalFormat("####");
            NumberFormatter formatterNumeral = new NumberFormatter(formatoNumeral);
            formatterNumeral.setValueClass(Integer.class);
            //ftfFolha.setFormatterFactory(new DefaultFormatterFactory(formatterNumeral));


            tblAnalista.getColumnModel().getColumn(0).setPreferredWidth(50);
            tblAnalista.getColumnModel().getColumn(1).setPreferredWidth(200);
            tblAnalista.getColumnModel().getColumn(2).setPreferredWidth(100);
            tblAnalista.getColumnModel().getColumn(3).setPreferredWidth(50);
            tblAnalista.getColumnModel().getColumn(4).setPreferredWidth(50);
            tblAnalista.getColumnModel().getColumn(4).setPreferredWidth(50);
            
            
        } catch (ParseException ex) {
            Logger.getLogger(AnalistaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        AnalistaBD analistaBD = new AnalistaBD();

        if (txtFiltro.getText().equals("")) {
            listAnalista = analistaBD.consultaAnalista();
        } else {
            listAnalista = analistaBD.consultaAnalistaNome(txtFiltro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listAnalista.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                listAnalista.get(i).getId(), 
                listAnalista.get(i).getNome(),
                listAnalista.get(i).getTipo(),
                listAnalista.get(i).getMatricula(),
                listAnalista.get(i).getQtdeEntrada(),
                listAnalista.get(i).getQtdeSaida(),
                listAnalista.get(i).getSaldo()
            });
        }
    }
    
    private void mostraDetalheAnalista() {
        if (tblAnalista.getSelectedRow() != -1) {
            int indice = tblAnalista.getSelectedRow();
            txtNomeAnalista.setText(listAnalista.get(indice).getNome());
            txtMatricula.setText(listAnalista.get(indice).getMatricula());
            txtIdUsuario.setText(listAnalista.get(indice).getUsuario().getId().toString());
            txtNomeUsuario.setText(listAnalista.get(indice).getUsuario().getNome());
            cbTipo.setSelectedItem(listAnalista.get(indice).getTipo());
     
        } else {
            txtNomeAnalista.setText("");
            txtMatricula.setText("");
            txtIdUsuario.setText("");
            txtNomeUsuario.setText("");
        }
    }
    
   

    private void incluiAnalista() {
        if (txtNomeAnalista.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do Analista!", "Analista", JOptionPane.INFORMATION_MESSAGE);
            txtNomeAnalista.requestFocus();
         }
         if (txtMatricula.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe a Matricula!", "Matricula", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
         } 
        
                
       else {
            Integer qtdeInicial = 0;
            Analista analista = new Analista();
            analista.setNome(txtNomeAnalista.getText().trim());
            analista.setMatricula(txtMatricula.getText().trim());
            analista.setQtdeEntrada(qtdeInicial);
            analista.setQtdeSaida(qtdeInicial);
            analista.setUsuario(getUsuario());
            analista.setTipo(cbTipo.getSelectedItem().toString());
            
            AnalistaBD analistaBD = new AnalistaBD();
            if (analistaBD.incluiAnalista(analista)) {
                JOptionPane.showMessageDialog(this, "Analista cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Analista já existe !", "Erro", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }

    private void alteraAnalista() {
      if (txtNomeAnalista.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe o nome do Analista!", "Analista", JOptionPane.INFORMATION_MESSAGE);
            txtNomeAnalista.requestFocus();
         }
                
         if (txtMatricula.getText().trim().equals("")){
            JOptionPane.showMessageDialog(this, "Informe a Matricula!", "Matricula", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
         } 
        
      else {
            buscaUsuario();
            Analista analista = new Analista();
            analista.setId(this.listAnalista.get(tblAnalista.getSelectedRow()).getId());
            analista.setNome(txtNomeAnalista.getText().trim());
            analista.setMatricula(txtMatricula.getText().trim());
            analista.setUsuario(getUsuario());
            analista.setTipo(cbTipo.getSelectedItem().toString());

            AnalistaBD analistaBD = new AnalistaBD();
            if (analistaBD.alteraAnalista(analista)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exluiAnalista() {
        AnalistaBD analistaBD = new AnalistaBD();
        if (analistaBD.excluiAnalista(listAnalista.get(tblAnalista.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            //JOptionPane.showMessageDialog(this, "Possivelmente existe !", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta(){
        txtMatricula.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
        txtNomeAnalista.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
        txtFiltro.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }
    
    
      private void limpaCampos(){
        {
           
            txtNomeAnalista.setText("");
            txtMatricula.setText("");
        }  
    }
       
    private void habilitaCampos() {        
        txtNomeAnalista.setEditable(true);
        txtMatricula.setEditable(true);
        txtIdUsuario.setEditable(true);
        btnSelecionaUsuario.setEnabled(true);
        
    }

    private void desabilitaCampos() {
        txtNomeAnalista.setEditable(false);
        txtMatricula.setEditable(false);
        txtIdUsuario.setEditable(false);
        btnSelecionaUsuario.setEnabled(false);
        
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
    
     private void selecionaAnalistaDistribuicao() {
        if (tblAnalista.getSelectedRow() != -1) {
            distFrame.setAnalista(listAnalista.get(tblAnalista.getSelectedRow()));
            this.dispose();
            distFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Analista da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }
     
      private void selecionaAnalistaRelProcAnalistaDist() {
        if (tblAnalista.getSelectedRow() != -1) {
            relAnalistaDist.setAnalista(listAnalista.get(tblAnalista.getSelectedRow()));
            this.dispose();
            relAnalistaDist.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Analista da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }
     
      private void buscaUsuario() {
        UsuarioBD usuarioBD = new UsuarioBD();
        listUsuario = usuarioBD.consultaUsuario();
        int binario = 0;
        try {
            int max = listUsuario.size();
            int id_busca = Integer.parseInt(txtIdUsuario.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listUsuario.get(i).getId();
                if (listUsuario.get(i).getId() == id_busca) {
                    setUsuario(listUsuario.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdUsuario.setText("");
                txtNomeUsuario.setText("");
                setUsuario(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setUsuario(null);
            txtNomeUsuario.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
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
        txtNomeAnalista = new javax.swing.JTextField();
        txtMatricula = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAnalista = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNomeUsuario = new javax.swing.JTextField();
        btnSelecionaUsuario = new javax.swing.JButton();
        txtIdUsuario = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Analista");
        setPreferredSize(new java.awt.Dimension(700, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/AGENTE_64x64.png"))); // NOI18N
        jLabel1.setText("Analista / Fiscal");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        txtNomeAnalista.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNomeAnalista, gridBagConstraints);

        txtMatricula.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtMatricula, gridBagConstraints);

        jLabel6.setText("Matricula");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel6, gridBagConstraints);

        jLabel7.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroActionPerformed(evt);
            }
        });
        txtFiltro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltro, gridBagConstraints);

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

        tblAnalista.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblAnalista.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NOME", "TIPO", "MATRICULA", "ENTRADA", "SAIDA", "SALDO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true, false, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnalista.setGridColor(new java.awt.Color(102, 102, 102));
        tblAnalista.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAnalista.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAnalistaMouseClicked(evt);
            }
        });
        tblAnalista.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAnalistaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAnalista);

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

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
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
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel4, gridBagConstraints);

        jLabel4.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jLabel4, gridBagConstraints);

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Associar Usuario ao Analista"));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        jLabel17.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLabel17, gridBagConstraints);

        txtNomeUsuario.setEditable(false);
        txtNomeUsuario.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeUsuario.setEnabled(false);
        txtNomeUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(txtNomeUsuario, gridBagConstraints);

        btnSelecionaUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaUsuario.setEnabled(false);
        btnSelecionaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaUsuarioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(btnSelecionaUsuario, gridBagConstraints);

        txtIdUsuario.setEditable(false);
        txtIdUsuario.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdUsuarioFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel19.add(txtIdUsuario, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel19, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Tipo");
        jPanel5.add(jLabel5, new java.awt.GridBagConstraints());

        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ANALISTA", "FISCAL" }));
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
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
        jPanel5.add(cbTipo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel5, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        AnalistaBD bairroBD = new AnalistaBD();
            if(bairroBD.testaConexao()){
                
                habilitaCampos();
                
               
                btnFiltrar.setEnabled(false);
                tblAnalista.setEnabled(false);
                limpaSelecaoTabela();
              
                habilitaBotoes();
                modo = Constantes.INSERT_MODE;
                limpaCampos();
                //caixaAlta();
            }
            else
                JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiAnalista();
            tblAnalista.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraAnalista();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblAnalista.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblAnalista.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaUsuario();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um analista da lista!", "Analista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblAnalista.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Analista?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exluiAnalista();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um analista da lista!", "Analista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
       atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void tblAnalistaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnalistaMouseClicked
        if (evt.getClickCount() == 2) {  
           if (modoSeleciona == Constantes.DISTRIBUICAO_FRAME) {
                 selecionaAnalistaDistribuicao();
                       dispose();  
           }else if (modoSeleciona == Constantes.RELPROCANALISTADIST_FRAME) {
                   selecionaAnalistaRelProcAnalistaDist();
                       dispose();  
           }
           
         
           evt.consume();  
           
        } 
    }//GEN-LAST:event_tblAnalistaMouseClicked

    private void tblAnalistaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAnalistaKeyPressed
       if (evt.getKeyCode() == KeyEvent.VK_ENTER) {  
            if (modoSeleciona == Constantes.DISTRIBUICAO_FRAME) {
                 selecionaAnalistaDistribuicao();
                       dispose();  
           }else if (modoSeleciona == Constantes.RELPROCANALISTADIST_FRAME) {
                   selecionaAnalistaRelProcAnalistaDist();
                       dispose();  
           }                   
           evt.consume(); 
        }  
    }//GEN-LAST:event_tblAnalistaKeyPressed

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
       atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void txtNomeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeUsuarioActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeUsuarioActionPerformed

    private void btnSelecionaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaUsuarioActionPerformed
        UsuarioFrame usuarioFrame = new UsuarioFrame(this);
        usuarioFrame.setVisible(true);
        this.getDesktopPane().add(usuarioFrame);
        usuarioFrame.toFront();        
}//GEN-LAST:event_btnSelecionaUsuarioActionPerformed

    private void txtIdUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdUsuarioFocusLost
     buscaUsuario();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdUsuarioFocusLost

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaUsuario;
    private javax.swing.JComboBox cbTipo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblAnalista;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtMatricula;
    private javax.swing.JTextField txtNomeAnalista;
    private javax.swing.JTextField txtNomeUsuario;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param digitador the digitador to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        txtNomeUsuario.setText(usuario.getNome());
        txtIdUsuario.setText(usuario.getId().toString());
    }
}


