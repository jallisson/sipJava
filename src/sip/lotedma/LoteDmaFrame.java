/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.lotedma;

import sip.menulogin.Menu;
import sip.requerente.RequerenteFrame;
import sip.util.Constantes;
import sip.requerente.Requerente;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import sip.movimentodma.MovimentoDmaFrame;

/**
 *
 * @author T2Ti
 */
public class LoteDmaFrame extends javax.swing.JInternalFrame {

    private JDateChooser jdcData;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<LoteDma> listLote;
    private int modo;
    private int modoSeleciona;
    private RequerenteFrame requerenteFrame;
    private Requerente requerente;
    private MovimentoDmaFrame movimentoDmaFrame;

    /** Creates new form ClienteFrame */
    public LoteDmaFrame() {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
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

    public LoteDmaFrame(MovimentoDmaFrame movimentoDmaFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.movimentoDmaFrame = movimentoDmaFrame;
        modoSeleciona = Constantes.MOVIMENTO_DMA_FRAME;
        formataData();
    }

   

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel7.add(jdcData);
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblLote.getModel();
        listModel = tblLote.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheLote();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblLote.getColumnModel().getColumn(1).setPreferredWidth(500);
            tblLote.getColumnModel().getColumn(2).setPreferredWidth(200);


        } catch (ParseException ex) {
            Logger.getLogger(LoteDmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        LoteDmaBD NacionalidadeBD = new LoteDmaBD();

        if (txtFiltroLote.getText().equals("")) {
            listLote = NacionalidadeBD.consultaLote();
        } else {
            listLote = NacionalidadeBD.consultaLoteNome(txtFiltroLote.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listLote.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                        listLote.get(i).getId(),
                        listLote.get(i).getDescricao(),
                        simpleDateFormat.format(listLote.get(i).getDataLote())
                    });
        }

    }

    private void mostraDetalheLote() {
        if (tblLote.getSelectedRow() != -1) {
            int indice = tblLote.getSelectedRow();
            //txtNome.setText(listLote.get(indice).getLote());
            txtDescricao.setText(listLote.get(indice).getDescricao());
            jdcData.setDate(listLote.get(indice).getDataLote());
            ftfMes.setText(listLote.get(indice).getMesAno());
        } else {
            //txtNome.setText("");
            txtDescricao.setText("");
        }
    }

    private void limpaCampos() {
        {
            //txtNome.setText("");
            txtDescricao.setText("");
            jdcData.setDate(null);
            ftfMes.setText("");
        }
    }

    private void incluiLote() {
        
        if (txtDescricao.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a descrição do Lote!", "Descrição", JOptionPane.INFORMATION_MESSAGE);
            txtDescricao.requestFocus();
        }
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LoteDma lote = new LoteDma();
            ftfMes.setValue(jdcData.getDate());
            //lote.setLote(txtNome.getText().trim());
            lote.setDescricao(txtDescricao.getText().trim());
            lote.setDataLote(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            lote.setMesAno(ftfMes.getText());


            LoteDmaBD agenteBD = new LoteDmaBD();
            if (agenteBD.incluiLote(lote)) {
                JOptionPane.showMessageDialog(this, "Lote cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o Lote já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraLote() {
        
        if (txtDescricao.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe a descrição do Lote!", "Descrição", JOptionPane.INFORMATION_MESSAGE);
            txtDescricao.requestFocus();
        }
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {
            LoteDma lote = new LoteDma();
            ftfMes.setValue(jdcData.getDate());
            lote.setId(this.listLote.get(tblLote.getSelectedRow()).getId());
            //lote.setLote(txtNome.getText().trim());
            lote.setDescricao(txtDescricao.getText().trim());
            lote.setDataLote(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            lote.setMesAno(ftfMes.getText());

            LoteDmaBD loteBD = new LoteDmaBD();
            if (loteBD.alteraLote(lote)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiLote() {
        LoteDmaBD loteBD = new LoteDmaBD();
        if (loteBD.excluiLote(listLote.get(tblLote.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Lote excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Tramitação para o lote!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
      
        txtDescricao.addKeyListener(new KeyListener() {

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

    private void habilitaCampos() {
        //txtNome.setEditable(true);
        txtDescricao.setEditable(true);
        jdcData.setEnabled(true);

    }

    private void desabilitaCampos() {
        //txtNome.setEditable(false);
        txtDescricao.setEditable(false);
        jdcData.setEnabled(false);

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

    private void selecionaLoteMovimentoDma() {
        if (tblLote.getSelectedRow() != -1) {
            movimentoDmaFrame.setLoteDma(listLote.get(tblLote.getSelectedRow()));
            this.dispose();
            movimentoDmaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
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
        txtDescricao = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroLote = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblLote = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Lote Dma\n");
        setPreferredSize(new java.awt.Dimension(650, 500));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/lote_64x64.png"))); // NOI18N
        jLabel1.setText("Lote Dma");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jLabel3.setText("Descrição");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jLabel3, gridBagConstraints);

        txtDescricao.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(txtDescricao, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Lote");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltroLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroLoteActionPerformed(evt);
            }
        });
        txtFiltroLote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroLoteFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroLote, gridBagConstraints);

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

        tblLote.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblLote.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Descrição", "Data"
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
        tblLote.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblLote.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblLoteMouseClicked(evt);
            }
        });
        tblLote.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblLoteKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblLote);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Cadastro"));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Mes/Ano");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(jLabel12, gridBagConstraints);

        ftfMes.setEditable(false);
        ftfMes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MMMM' 'yy"))));
        ftfMes.setToolTipText("Data do Video");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(ftfMes, gridBagConstraints);

        jLabel16.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel13.add(jLabel16, gridBagConstraints);

        jPanel7.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(jPanel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel13, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroLote.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        LoteDmaBD nacionalidadeBD = new LoteDmaBD();
        if (nacionalidadeBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblLote.setEnabled(false);
            limpaSelecaoTabela();
            //txtNome.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiLote();
            tblLote.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraLote();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblLote.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblLote.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblLote.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Nacionalidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiLote();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroLoteActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroLoteActionPerformed

    private void tblLoteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblLoteMouseClicked
        if (evt.getClickCount() == 2) {
            switch (modoSeleciona) {
                case Constantes.MOVIMENTO_DMA_FRAME:
                    selecionaLoteMovimentoDma();
                    dispose();
                    break;
                case Constantes.ALTERAR:
                    //selecionaLoteRelTituloEmitido();
                    dispose();
                    break;
                case Constantes.INSERIR:
                    //selecionaLoteRelDespachoLote();
                    dispose();
                    break;
                case Constantes.RELATORIO:
                    //selecionaLoteRelSemVizinho();
                    dispose();
                    break;
                case Constantes.LIMITE:
                    //selecionaLoteConvocacao();
                    dispose();
                    break;
                case Constantes.EDIT_MODE:
                    //selecionaLoteRelLancUsuario();
                    dispose();
                    break;
                default:
                    break;
            }
            evt.consume();
        }
    }//GEN-LAST:event_tblLoteMouseClicked

    private void tblLoteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblLoteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (modoSeleciona) {
                case Constantes.MOVIMENTO_DMA_FRAME:
                    selecionaLoteMovimentoDma();
                    dispose();
                    break;
                case Constantes.ALTERAR:
                    //selecionaLoteRelTituloEmitido();
                    dispose();
                    break;
                case Constantes.INSERIR:
                    //selecionaLoteRelDespachoLote();
                    dispose();
                    break;
                case Constantes.RELATORIO:
                    //selecionaLoteRelSemVizinho();
                    dispose();  
                    break;
                case Constantes.LIMITE:
                    //selecionaLoteConvocacao();
                    dispose();
                    break;
                default:
                    break;
            }
        }
        evt.consume();
    }//GEN-LAST:event_tblLoteKeyPressed

    private void txtFiltroLoteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroLoteFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroLoteFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblLote;
    private javax.swing.JTextField txtDescricao;
    private javax.swing.JTextField txtFiltroLote;
    // End of variables declaration//GEN-END:variables
}
