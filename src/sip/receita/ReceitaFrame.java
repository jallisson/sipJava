/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.receita;

import sip.bairro.*;
import sip.menulogin.Menu;
import sip.requerente.RequerenteFrame;
import sip.util.Constantes;


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
import sip.autorizacaoeventos.AutorizacaoEventosFrame;
import sip.pessoa.PessoaFrame;

/**
 *
 * @author T2Ti
 */
public class ReceitaFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Receita> listBairro;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private AutorizacaoEventosFrame autorizacaoEventosFrame;
    private PessoaFrame pessoaFrame;

    /** Creates new form ClienteFrame *
    public ReceitaFrame() {
        initComponents();
        defineModelo();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        caixaAlta();
        fechando();
    }

    public ReceitaFrame(RequerenteFrame requerenteFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        this.requerenteFrame = requerenteFrame;
        modoSeleciona = Constantes.INCLUIR;
    }

    public ReceitaFrame(AutorizacaoEventosFrame autorizacaoEventosFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        btnSelecionaCarro.setVisible(false);
        btnSelecionaCarro1.setVisible(false);
        this.autorizacaoEventosFrame = autorizacaoEventosFrame;
        modoSeleciona = Constantes.AUTORIZACAOEVENTOS_FRAME;
    }

    public ReceitaFrame(PessoaFrame pessoaFrame) {
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
                new InternalFrameAdapter() {

                    @Override
                    public void internalFrameClosing(InternalFrameEvent e) {
                        dispose();
                        Menu.menuVisivel();

                    }
                });
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblBairro.getModel();
        listModel = tblBairro.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheBairro();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            DecimalFormat formatoNumeral = new DecimalFormat("####");
            NumberFormatter formatterNumeral = new NumberFormatter(formatoNumeral);
            formatterNumeral.setValueClass(Integer.class);
            ftfFolha.setFormatterFactory(new DefaultFormatterFactory(formatterNumeral));


            tblBairro.getColumnModel().getColumn(0).setPreferredWidth(150);
            tblBairro.getColumnModel().getColumn(1).setPreferredWidth(500);
            tblBairro.getColumnModel().getColumn(2).setPreferredWidth(200);
            tblBairro.getColumnModel().getColumn(3).setPreferredWidth(200);
            tblBairro.getColumnModel().getColumn(4).setPreferredWidth(200);
            tblBairro.getColumnModel().getColumn(5).setPreferredWidth(300);



        } catch (ParseException ex) {
            Logger.getLogger(ReceitaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        ReceitaBD BairroBD = new ReceitaBD();

        if (txtFiltroBairro.getText().equals("")) {
            listBairro = BairroBD.consultaBairro();
        } else {
            listBairro = BairroBD.consultaBairro(txtFiltroBairro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listBairro.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                        listBairro.get(i).getId(),
                        listBairro.get(i).getNome(),
                        listBairro.get(i).getLivro(),
                        listBairro.get(i).getFolha(),
                        listBairro.get(i).getMatricula(),
                        listBairro.get(i).getTipo()
                    });
        }

    }

    private void mostraDetalheBairro() {
        if (tblBairro.getSelectedRow() != -1) {
            int indice = tblBairro.getSelectedRow();
            txtNome.setText(listBairro.get(indice).getNome());
            txtLivro.setText(listBairro.get(indice).getLivro());
            ftfFolha.setValue(listBairro.get(indice).getFolha());
            txtMatricula.setText(listBairro.get(indice).getMatricula());
            cbTipo.setSelectedItem(listBairro.get(indice).getTipo());

        } else {
            txtNome.setText("");
            txtLivro.setText("");
            ftfFolha.setText("");
            txtMatricula.setText("");
            cbTipo.setSelectedItem("SELECIONE");
        }
    }

    private void incluiBairro() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Bairro!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (txtLivro.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o Livro!", "Livro", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (ftfFolha.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a Folha!", "Folha", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (txtMatricula.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a Matricula!", "Matricula", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }
        if (cbTipo.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Tipo", "Tipo", JOptionPane.INFORMATION_MESSAGE);
            cbTipo.requestFocus();
        } else {
            Receita bairro = new Receita();
            bairro.setNome(txtNome.getText().trim());
            bairro.setLivro(txtLivro.getText().trim());
            bairro.setFolha((Integer) ftfFolha.getValue());
            bairro.setMatricula(txtMatricula.getText().trim());
            bairro.setTipo(cbTipo.getSelectedItem().toString());

            ReceitaBD bairroBD = new ReceitaBD();
            if (bairroBD.incluiBairro(bairro)) {
                JOptionPane.showMessageDialog(this, "Bairro cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Bairro já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraBairro() {
        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o nome do Bairro!", "Nome", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (txtLivro.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o Livro!", "Livro", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (ftfFolha.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a Folha!", "Folha", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }

        if (txtMatricula.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a Matricula!", "Matricula", JOptionPane.INFORMATION_MESSAGE);
            txtMatricula.requestFocus();
        }
        if (cbTipo.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Tipo", "Tipo", JOptionPane.INFORMATION_MESSAGE);
            cbTipo.requestFocus();
        } else {
            Receita bairro = new Receita();
            bairro.setId(this.listBairro.get(tblBairro.getSelectedRow()).getId());
            bairro.setNome(txtNome.getText().trim());
            bairro.setLivro(txtLivro.getText().trim());
            bairro.setFolha((Integer) ftfFolha.getValue());
            bairro.setMatricula(txtMatricula.getText().trim());
            bairro.setTipo(cbTipo.getSelectedItem().toString());

            ReceitaBD bairroBD = new ReceitaBD();
            if (bairroBD.alteraBairro(bairro)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiBairro() {
        ReceitaBD bairroBD = new ReceitaBD();
        if (bairroBD.excluiBairro(listBairro.get(tblBairro.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Bairro excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o bairro!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtLivro.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextField campo = (JTextField) e.getSource();
                        int posicaoCursor = campo.getCaretPosition();
                        campo.setText(campo.getText().toUpperCase());
                        if (posicaoCursor != campo.getCaretPosition()) {
                            campo.setCaretPosition(posicaoCursor);
                        }
                    }
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        ftfFolha.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextField campo = (JTextField) e.getSource();
                        int posicaoCursor = campo.getCaretPosition();
                        campo.setText(campo.getText().toUpperCase());
                        if (posicaoCursor != campo.getCaretPosition()) {
                            campo.setCaretPosition(posicaoCursor);
                        }
                    }
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        txtMatricula.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextField campo = (JTextField) e.getSource();
                        int posicaoCursor = campo.getCaretPosition();
                        campo.setText(campo.getText().toUpperCase());
                        if (posicaoCursor != campo.getCaretPosition()) {
                            campo.setCaretPosition(posicaoCursor);
                        }
                    }
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });
        txtNome.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextField campo = (JTextField) e.getSource();
                        int posicaoCursor = campo.getCaretPosition();
                        campo.setText(campo.getText().toUpperCase());
                        if (posicaoCursor != campo.getCaretPosition()) {
                            campo.setCaretPosition(posicaoCursor);
                        }
                    }
                });
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }
        });


    }

    private void limpaCampos() {
        {
            txtNome.setText("");
            txtLivro.setText("");
            ftfFolha.setText("");
            txtMatricula.setText("");
            cbTipo.setSelectedItem("SELECIONE");

        }
    }

    private void habilitaCampos() {
        txtNome.setEditable(true);
        txtLivro.setEditable(true);
        ftfFolha.setEditable(true);
        txtMatricula.setEditable(true);
        cbTipo.setEnabled(true);
    }

    private void desabilitaCampos() {
        txtNome.setEditable(false);
        txtLivro.setEditable(false);
        ftfFolha.setEditable(false);
        txtMatricula.setEditable(false);
        cbTipo.setEnabled(false);

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

    private void selecionaBairroRequerente() {
        if (tblBairro.getSelectedRow() != -1) {
            requerenteFrame.setBairro(listBairro.get(tblBairro.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaBairroAutorizacaoEventos() {
        if (tblBairro.getSelectedRow() != -1) {
            autorizacaoEventosFrame.setBairro(listBairro.get(tblBairro.getSelectedRow()));
            this.dispose();
            autorizacaoEventosFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaBairroPessoa() {
        if (tblBairro.getSelectedRow() != -1) {
            pessoaFrame.setBairro(listBairro.get(tblBairro.getSelectedRow()));
            this.dispose();
            pessoaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }*/

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
        jLabel7 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroBairro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBairro = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbTipo = new javax.swing.JComboBox();
        jPanel22 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel27 = new javax.swing.JLabel();
        ftfMes7 = new javax.swing.JFormattedTextField();
        jPanel28 = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTARelatoOcorrencia2 = new javax.swing.JTextArea();
        jPanel24 = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        btnSelecionaProcesso = new javax.swing.JButton();
        txtIdProcesso = new javax.swing.JTextField();
        txtNumeroProcesso = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Receitas");
        setPreferredSize(new java.awt.Dimension(600, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/cash_64x64.png"))); // NOI18N
        jLabel1.setText("Receitas");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        txtNome.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 14;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtNome, gridBagConstraints);

        jLabel7.setText("Titulo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel2.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
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

        txtFiltroBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroBairroActionPerformed(evt);
            }
        });
        txtFiltroBairro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroBairroFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroBairro, gridBagConstraints);

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

        tblBairro.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblBairro.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Titulo", "Taxa", "Data Pag"
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
        tblBairro.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblBairro.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBairroMouseClicked(evt);
            }
        });
        tblBairro.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblBairroKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblBairro);

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
        gridBagConstraints.gridwidth = 2;
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
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel8.setText("Taxa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel8, gridBagConstraints);

        cbTipo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbTipo.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "GETAT", "INCRA" }));
        cbTipo.setEnabled(false);
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(cbTipo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel8, gridBagConstraints);

        jPanel22.setBackground(new java.awt.Color(255, 255, 255));
        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pagamento"));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        jPanel23.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(jPanel23, gridBagConstraints);

        jLabel27.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel22.add(jLabel27, gridBagConstraints);

        ftfMes7.setEditable(false);
        ftfMes7.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MMMM' 'yy"))));
        ftfMes7.setToolTipText("Data do Video");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(ftfMes7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        getContentPane().add(jPanel22, gridBagConstraints);
        jPanel22.getAccessibleContext().setAccessibleName("Data Pagamento");

        jPanel28.setBorder(javax.swing.BorderFactory.createTitledBorder("Observação"));
        jPanel28.setLayout(new java.awt.GridBagLayout());

        jTARelatoOcorrencia2.setColumns(20);
        jTARelatoOcorrencia2.setRows(5);
        jTARelatoOcorrencia2.setWrapStyleWord(true);
        jScrollPane6.setViewportView(jTARelatoOcorrencia2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel28.add(jScrollPane6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel28, gridBagConstraints);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Processo"));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jLabel28.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(jLabel28, gridBagConstraints);

        btnSelecionaProcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaProcesso.setEnabled(false);
        btnSelecionaProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaProcessoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(btnSelecionaProcesso, gridBagConstraints);

        txtIdProcesso.setEditable(false);
        txtIdProcesso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdProcessoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel24.add(txtIdProcesso, gridBagConstraints);

        txtNumeroProcesso.setEditable(false);
        txtNumeroProcesso.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNumeroProcesso.setEnabled(false);
        txtNumeroProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroProcessoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(txtNumeroProcesso, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        getContentPane().add(jPanel24, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        //atualizaTabela();
        txtFiltroBairro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        ReceitaBD bairroBD = new ReceitaBD();
        if (bairroBD.testaConexao()) {
           // habilitaCampos();
           // limpaCampos();
            btnFiltrar.setEnabled(false);
            tblBairro.setEnabled(false);
           // limpaSelecaoTabela();
            txtNome.requestFocus();
           // habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            //incluiBairro();
            tblBairro.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            //alteraBairro();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        //desabilitaBotoes();
      //  desabilitaCampos();
        tblBairro.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblBairro.getSelectedRow() != -1) {
          //  habilitaCampos();
           // habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblBairro.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Bairro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                //excluiBairro();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroBairroActionPerformed
       // atualizaTabela();
    }//GEN-LAST:event_txtFiltroBairroActionPerformed

    private void tblBairroMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBairroMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.INCLUIR) {
              //  selecionaBairroRequerente();
                dispose();
            } else if (modoSeleciona == Constantes.AUTORIZACAOEVENTOS_FRAME) {
                //selecionaBairroAutorizacaoEventos();
                dispose();

            } else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                //selecionaBairroPessoa();
                dispose();

            }
            evt.consume();

        }
    }//GEN-LAST:event_tblBairroMouseClicked

    private void tblBairroKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblBairroKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modo == Constantes.INCLUIR) {
                //selecionaBairroRequerente();
                dispose();
            } else if (modo == Constantes.AUTORIZACAOEVENTOS_FRAME) {
                //selecionaBairroAutorizacaoEventos();
                dispose();
            }else if (modoSeleciona == Constantes.PESSOA_FRAME) {
                //selecionaBairroPessoa();
                dispose();

            }
            evt.consume();
        }
    }//GEN-LAST:event_tblBairroKeyPressed

    private void txtFiltroBairroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroBairroFocusLost
        //atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroBairroFocusLost

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbTipoActionPerformed

    private void btnSelecionaProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoActionPerformed
        //ProcessoFrame processoFrame = new ProcessoFrame(this);
        //processoFrame.setVisible(true);
        //this.getDesktopPane().add(processoFrame);
        //processoFrame.toFront();
        //processoFrame.txtFiltroProcesso.requestFocus();
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaProcessoActionPerformed

    private void txtIdProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdProcessoFocusLost
        //buscaProcesso("lancamento");  // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoFocusLost

    private void txtNumeroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcessoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroProcessoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JComboBox cbTipo;
    private javax.swing.JFormattedTextField ftfMes7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel28;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JTextArea jTARelatoOcorrencia2;
    private javax.swing.JTable tblBairro;
    private javax.swing.JTextField txtFiltroBairro;
    public javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNumeroProcesso;
    // End of variables declaration//GEN-END:variables
}
