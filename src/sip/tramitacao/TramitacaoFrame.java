/* To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.tramitacao;

import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.processo.ProcessoBD;
import sip.processo.ProcessoFrame;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.Constantes;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import org.json.simple.JSONObject;
import sip.processo.AnexosProcesso;
import sip.util.ColorirTabelaTramitacao;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public final class TramitacaoFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private JDateChooser jdcData;
    private DefaultTableModel tableModel;
    private DefaultTableCellRenderer cellModel;
    private ListSelectionModel listModel;
    private List<Tramitacao> listTramitacao;
    private Tramitacao tramitacao;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private Processo processo;
    private List<Processo> listProcesso;
    private Usuario usuario;
    private List<Usuario> listUsuario;
    String idUsuario = Menu.id_Usuario;
    String observacao;
    private String status;
    JPopupMenu jPopupMenu = new JPopupMenu();
    JMenuItem jMenuItemAlterar = new JMenuItem();
    private List<Tramitacao> listTramitacaoProcAnexos;
    private List<AnexosProcesso> listAnexosProcesso;
    private AnexosProcesso anexosProcesso;
    private Integer idProcesso;
    private String deOnde;

    public TramitacaoFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idUsuario);
        jPanel19.setVisible(false);
        jdcData.setEnabled(false);
        txtFiltroProcesso.setVisible(false);
        btnFiltrar.setVisible(false);
        txtFiltroProcesso1.setVisible(false);
        btnFiltrar1.setVisible(false);
        controlePesquisa();
        executaAtalhoBtnExcluirAlterar();
        MenuJtableDetalhe();
        

        //cbStatus.addItem("estado"); adicionar item no status
        //habilitaParecer();
        //habilitaSituacaoCad();
        //habilitaLaudoMzu();
        //habilitaLote();
        //ConfiguraStatus();
        //configuraStatusDestino();
        //configuraLaudoMzu();
        //tabelaInformacoesCores();


    }

    private void fechando() {
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        this.addInternalFrameListener(
                new InternalFrameAdapter() {

                    @Override
                    public void internalFrameClosing(InternalFrameEvent e) {

                        Menu.menuVisivel();

                        dispose();

                    }
                });
    }

    private void tabelaInformacoesCores() {
        //txtTituloEmitido.setBackground(new Color(126,195 ,255));
        //txtTituloEntregue.setBackground(Color.GREEN);
        //txtTituloObservacao.setBackground(Color.YELLOW);
    }

    private void executaAtalhoBtnExcluirAlterar() {
        // ATALHO ALT + E MAIS A SENHA admsipexe
        InputMap inputMap = this.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.ALT_DOWN_MASK), "forward");
        this.getRootPane().setInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap);
        this.getRootPane().getActionMap().put("forward", new AbstractAction() {

            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                String senha = "";
                
                senha = JOptionPane.showInputDialog(null, "Senha ?", "Pergunta", JOptionPane.PLAIN_MESSAGE);
                //SENHA PARA ACESSAR O BOTÃO EXCLUIR!
                if (senha.equals("adm12345")) {
                    btnAlterar.setEnabled(true);
                    btnExcluir.setEnabled(true);


                } else {
                    btnExcluir.setEnabled(false);
                }
                //btnAlterar.setEnabled(false);
            }
        });
    }

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel7.add(jdcData);
    }

    private void inserirZeroEsqPesq(JTextField field) {
        boolean ehNumero = true;
        try {
            Integer.parseInt(field.getText());
        } catch (Exception e) {
            ehNumero = false;
        }
        //000000 estudo de quantidade 6 zeros
        if (ehNumero == true) {
            String tam = field.getText();
            if (tam.length() == 1) {
                String valor = field.getText();
                field.setText("00000" + valor);
            } else if (tam.length() == 2) {
                String valor = field.getText();
                field.setText("0000" + valor);
            } else if (tam.length() == 3) {
                String valor = field.getText();
                field.setText("000" + valor);
            } else if (tam.length() == 4) {
                String valor = field.getText();
                field.setText("00" + valor);
            }
        }
    }

    private void controlaObservacao(JTextArea jtext) {
        String tam = jtext.getText();
        if (tam.length() > 0 && !"".equals(jtext.getText().trim())) {
            observacao = jtext.getText();
        } else {
            observacao = null;
        }

    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblTramitacao.getModel();

        listModel = tblTramitacao.getSelectionModel();

        tblTramitacao.setDefaultRenderer(Object.class, new ColorirTabelaTramitacao());

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblTramitacao.getModel());
        tblTramitacao.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);


        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {

                    mostraDetalheTramitacao();
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

            DecimalFormat formatoValor = new DecimalFormat("#,###.00");
            NumberFormatter formatterValor = new NumberFormatter(formatoValor);
            formatterValor.setValueClass(Double.class);
            //ftfValorDigitado.setFormatterFactory(new DefaultFormatterFactory(formatterValor));
            //ftfValorTotal.setFormatterFactory(new DefaultFormatterFactory(formatterValor))

            //tblTramitacao.getColumnModel().getColumn(0).setMaxWidth(0);  
            //tblTramitacao.getColumnModel().getColumn(0).setMinWidth(0);  
            //tblTramitacao.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);  
            //tblTramitacao.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);  

            //tblTramitacao.getColumnModel().getColumn(0).setPreferredWidth(60);//1
            tblTramitacao.getColumnModel().getColumn(0).setPreferredWidth(80);//1
            tblTramitacao.getColumnModel().getColumn(1).setPreferredWidth(220);//1
            tblTramitacao.getColumnModel().getColumn(2).setPreferredWidth(150);//2
            tblTramitacao.getColumnModel().getColumn(3).setPreferredWidth(190);//3
            tblTramitacao.getColumnModel().getColumn(4).setPreferredWidth(40);//4
            tblTramitacao.getColumnModel().getColumn(5).setPreferredWidth(40);//5
            tblTramitacao.getColumnModel().getColumn(6).setPreferredWidth(40);//6
            tblTramitacao.getColumnModel().getColumn(7).setPreferredWidth(90);//7
            tblTramitacao.getColumnModel().getColumn(8).setPreferredWidth(30);//8
            tblTramitacao.getColumnModel().getColumn(9).setPreferredWidth(60);//9

            tblTramitacao.getColumnModel().getColumn(10).setMaxWidth(0);
            tblTramitacao.getColumnModel().getColumn(10).setMinWidth(0);
            tblTramitacao.getTableHeader().getColumnModel().getColumn(10).setMaxWidth(0);
            tblTramitacao.getTableHeader().getColumnModel().getColumn(10).setMinWidth(0);

        } catch (ParseException ex) {
            Logger.getLogger(TramitacaoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void controlePesquisa() {
        if (cbPesquisa.getSelectedItem().equals("Filtro Pelo Processo")) {
            txtFiltroProcesso.setVisible(true);
            btnFiltrar.setVisible(true);
            txtFiltroProcesso1.setVisible(false);
            btnFiltrar1.setVisible(false);
        } else if (cbPesquisa.getSelectedItem().equals("Situação Atual")) {
            txtFiltroProcesso1.setVisible(true);
            btnFiltrar1.setVisible(true);
            txtFiltroProcesso.setVisible(false);
            btnFiltrar.setVisible(false);
        }

    }

    private void atualizaTabela() {
        inserirZeroEsqPesq(txtFiltroProcesso);
        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        String nome = null;

        if (txtFiltroProcesso.getText().equals("")) {
            listTramitacao = tramitacaoBD.consultaTramitacao();
        } else {
            String valor = txtFiltroProcesso.getText();
            listTramitacao = tramitacaoBD.consultaTramitacaoPro(valor, valor);
        }
        avisaPesquisaProcesso();
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listTramitacao.size(); i++) {
                 if(listTramitacao.get(i).getRequerente().getNome() == null){
              nome = listTramitacao.get(i).getPessoa().getNome();
            }else{
                     
               nome = listTramitacao.get(i).getRequerente().getNome();
            }
                 
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            tableModel.insertRow(i, new Object[]{
                        //listTramitacao.get(i).getId(),
                        listTramitacao.get(i).getProcesso().getNumProcesso(),
                        listTramitacao.get(i).getProcesso().getTipoLicenca(),
                        nome,
                        listTramitacao.get(i).getStatus(),
                        listTramitacao.get(i).getSetor(),
                        listTramitacao.get(i).getSetorOrigem(),
                        listTramitacao.get(i).getSetorDestino(),
                        simpleDateFormat.format(listTramitacao.get(i).getDataTramitacao()),
                        //listTramitacao.get(i).getBairro().getNome(),
                        //listTramitacao.get(i).getLoteTitulacao().getLote(),
                        //listTramitacao.get(i).getLaudoMzu(),
                        //listTramitacao.get(i).getSituacaoCad(),
                        listTramitacao.get(i).getParecer(),
                        listTramitacao.get(i).getUsuario().getLogin(),
                        listTramitacao.get(i).getObservacao()
                    });
        }

    }

    private void atualizaTabelaSitAtual() {
        inserirZeroEsqPesq(txtFiltroProcesso1);
        TramitacaoBD tramitacaoBD = new TramitacaoBD();

        if (txtFiltroProcesso1.getText().equals("")) {
            //JOptionPane.showMessageDialog(this, "Informe o Numero do Processo", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            listTramitacao = tramitacaoBD.consultaTramUltimoNumProce(txtFiltroProcesso1.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listTramitacao.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            tableModel.insertRow(i, new Object[]{
                        listTramitacao.get(i).getProcesso().getNumProcesso(),
                        listTramitacao.get(i).getProcesso().getTipoLicenca(),
                        listTramitacao.get(i).getRequerente().getNome(),
                        listTramitacao.get(i).getStatus(),
                        listTramitacao.get(i).getSetor(),
                        listTramitacao.get(i).getSetorOrigem(),
                        listTramitacao.get(i).getSetorDestino(),
                        simpleDateFormat.format(listTramitacao.get(i).getDataTramitacao()),
                        listTramitacao.get(i).getParecer(),
                        listTramitacao.get(i).getUsuario().getLogin(),
                        listTramitacao.get(i).getObservacao()
                    });
        }
    }

    private void avisaPesquisaProcesso() {
        if (listTramitacao.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Não existe o processo: " + txtFiltroProcesso.getText() + " no sistema!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheTramitacao() {
        if (tblTramitacao.getSelectedRow() != -1) {
            int indice = tblTramitacao.getSelectedRow();

            txtIdProcesso.setText(listTramitacao.get(indice).getProcesso().getId().toString());
            txtNumeroProcesso.setText(listTramitacao.get(indice).getProcesso().getNumProcesso());

            //txtIdLote.setText(listTramitacao.get(indice).getLoteTitulacao().getId().toString());
            //txtNomeLote.setText(listTramitacao.get(indice).getLoteTitulacao().getLote());


            jdcData.setDate(listTramitacao.get(indice).getDataTramitacao());
            ftfMes.setText(listTramitacao.get(indice).getMesAno());

            // cbStatus.setSelectedItem(listTramitacao.get(indice).getStatus());
            cbParecer.setSelectedItem(listTramitacao.get(indice).getParecer());
            cbSetorOrigem.setSelectedItem(listTramitacao.get(indice).getSetor());

            txtaObservacao.setText(listTramitacao.get(indice).getObservacao());

        } else {
            txtIdProcesso.setText("");
            txtNumeroProcesso.setText("");
            //txtIdLote.setText("");
            //txtNomeLote.setText("");
            ftfMes.setText("");
            jdcData.setDate(null);
            cbStatus.setSelectedItem("SELECIONE");
            cbParecer.setSelectedItem("SELECIONE");
            cbSetorOrigem.setSelectedItem("SELECIONE");
            txtaObservacao.setText("");
        }
    }

    private void limpaCampos() {
        processo = null;
        txtIdProcesso.setText("");
        txtNumeroProcesso.setText("");

        anexosProcesso = null;
        txtNomeAnexo.setText("");


        //txtIdLote.setText("");
        //txtNomeLote.setText("");

        jdcData.setDate(null);
        ftfMes.setText("");
        cbStatus.setSelectedItem("SELECIONE");
        cbParecer.setSelectedItem("SELECIONE");
        cbSetorOrigem.setSelectedItem("SELECIONE");

        txtaObservacao.setText("");
    }

    private void limpaCamposSemProcData() {

        //txtIdLote.setText("");
        //txtNomeLote.setText("");


        cbStatus.setSelectedItem("SELECIONE");
        cbParecer.setSelectedItem("SELECIONE");
        cbSetorOrigem.setSelectedItem("SELECIONE");
        txtaObservacao.setText("");
    }

    private void alteraProcessoArquivado() {

        if (cbStatus.getSelectedItem() == "ARQUIVADO") {


            processo.setId(getProcesso().getId());

            processo.setArquivado("SIM");

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.alteraProcArquivado(processo)) {

                JOptionPane.showMessageDialog(this, "Processo Arquivo com sucesso", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "Erro ao arquivar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else if (cbStatus.getSelectedItem() == "DESARQUIVADO") {
            processo.setId(getProcesso().getId());

            processo.setArquivado("NAO");

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.alteraProcArquivado(processo)) {

                JOptionPane.showMessageDialog(this, "Processo Desarquivado com sucesso", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

            } else {
                JOptionPane.showMessageDialog(this, "Erro ao arquivar!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    

private void incluirTramitacao() throws ParseException {
        //configuraParecer();
       
        if (processo == null) {
            JOptionPane.showMessageDialog(this, "Informe o Processo", "Processo", JOptionPane.INFORMATION_MESSAGE);
            txtIdProcesso.requestFocus();
        }else if ("SIM".equals(processo.getArquivado()) && cbStatus.getSelectedItem() != "DESARQUIVADO") {
            JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento!", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
        }else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (cbStatus.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Status", "Status", JOptionPane.INFORMATION_MESSAGE);
            cbStatus.requestFocus();
        } else if (cbParecer.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Parecer", "Parecer", JOptionPane.INFORMATION_MESSAGE);
            cbParecer.requestFocus();
        } else if (cbSetorDestino.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Setor de Destino", "Setor", JOptionPane.INFORMATION_MESSAGE);
            cbSetorDestino.requestFocus();
        } else if (cbParecer.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o parecer", "Laudo", JOptionPane.INFORMATION_MESSAGE);
            cbParecer.requestFocus();
        } else {
            controlaObservacao(txtaObservacao);
            String controle;
            //
            SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            String theDate = dateTimeFormat.format(jdcData.getDate());
            //retirou theDate
            java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
            java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

            ftfMes.setValue(jdcData.getDate());
            controle = processo.getId() + " " + cbStatus.getSelectedItem() + " " + cbSetorOrigem.getSelectedItem() + " " + cbSetorDestino.getSelectedItem(); //+ " " + new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime());

            Tramitacao tram = new Tramitacao();
            tram.setUsuario(getUsuario());
            tram.setProcesso(getProcesso());
            tram.setDataTramitacao(timeStamp);

            //tram.setDataTramitacao(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));


            tram.setMesAno(ftfMes.getText());
            tram.setUsuario(getUsuario());
            if (cbStatus.getSelectedItem().equals("DOC ANEXADO TIPO :")) {
                tram.setStatus(cbTipoDoc.getSelectedItem().toString() + " ANEXADO");
            } else {
                tram.setStatus(cbStatus.getSelectedItem().toString());
            }

            if (cbStatus.getSelectedItem().equals("DOC ANEXADO TIPO :") && cbTipoDoc.getSelectedItem().equals("PARECER CONCLUSIVO") || cbTipoDoc.getSelectedItem().equals("PARECER JURIDICO") || cbTipoDoc.getSelectedItem().equals("PARECER TÉCNICO")) {
                tram.setParecer(cbParecer.getSelectedItem().toString());
            } else {
                tram.setParecer("");
            }

            if (cbStatus.getSelectedItem().equals("ENVIADO") || cbStatus.getSelectedItem().equals("RETORNO")) {
                tram.setSetor(" ");
            } else {
                tram.setSetor(getUsuario().getSetor());
            }
            if (cbStatus.getSelectedItem().equals("RETORNO") && cbSetorDestino.getSelectedItem().equals("CADASTRO")) {
                //tram.setSituacaoCad(cbSituacaoCad.getSelectedItem().toString());
            } else {
                tram.setSituacaoCad(" ");
            }

            tram.setSetorOrigem(cbSetorOrigem.getSelectedItem().toString());
            tram.setSetorDestino(cbSetorDestino.getSelectedItem().toString());
            tram.setObservacao(observacao);
            tram.setControle(controle);
            if (cbStatus.getSelectedItem().equals("DESPACHO EMITIDO")) {
                tram.setAnexosProcesso(getAnexosProcesso());
            } else {
                if (txtNomeAnexo.getText().equals(" ")) {
                    tram.setAnexosProcesso(null);
                } else {
                    tram.setAnexosProcesso(getAnexosProcesso());
                }
            }

            TramitacaoBD tramitacaoBD = new TramitacaoBD();
       
            if (tramitacaoBD.incluiTramitacao(tram)) {
                JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                if("DENÚNCIA".equals(getProcesso().getTipoLicenca())){
                    enviaNotificacaoDenunciaFcm(getProcesso().getDenuncia().getToken(),"Foi incluido no processo de dénuncia: "+getProcesso().getNumProcesso()+" o "+ cbTipoDoc.getSelectedItem().toString());
                }
                alteraProcessoArquivado();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();

            } else {
                JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
            }




        }


    }

    private void alteraTramitacao() {
        if (processo == null) {
            JOptionPane.showMessageDialog(this, "Informe o Processo", "Processo", JOptionPane.INFORMATION_MESSAGE);
            txtIdProcesso.requestFocus();
        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (cbStatus.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Status", "Status", JOptionPane.INFORMATION_MESSAGE);
            cbStatus.requestFocus();
        } else if (cbParecer.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Parecer", "Parecer", JOptionPane.INFORMATION_MESSAGE);
            cbParecer.requestFocus();
        }/* else if (cbLaudo.getSelectedItem().equals("SELECIONE")) {
        JOptionPane.showMessageDialog(this, "Informe o Laudo", "Laudo Mzu", JOptionPane.INFORMATION_MESSAGE);
        cbLaudo.requestFocus();
        }*/ else if (cbSetorDestino.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe o Setor de Destino", "Setor", JOptionPane.INFORMATION_MESSAGE);
            cbSetorDestino.requestFocus();

        } else {
            Tramitacao tram = new Tramitacao();
            ftfMes.setValue(jdcData.getDate());
            String controle = processo.getId() + " " + cbStatus.getSelectedItem() + " " + cbParecer.getSelectedItem() + " " + cbSetorOrigem.getSelectedItem();

            tram.setId(listTramitacao.get(tblTramitacao.getSelectedRow()).getId());
            tram.setUsuario(getUsuario());
            tram.setProcesso(getProcesso());
            //tram.setDataTramitacao(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            tram.setMesAno(ftfMes.getText());
            tram.setUsuario(getUsuario());
            tram.setStatus(cbStatus.getSelectedItem().toString());
            tram.setParecer(cbParecer.getSelectedItem().toString());
            if (cbStatus.getSelectedItem().equals("RECEBIDO")) {
                tram.setSetor(getUsuario().getSetor());
            } else {
                tram.setSetor("");
            }
            tram.setSetorOrigem(cbSetorOrigem.getSelectedItem().toString());
            tram.setSetorDestino(cbSetorDestino.getSelectedItem().toString());
            tram.setObservacao(txtaObservacao.getText());
            tram.setControle(controle);

            TramitacaoBD tramitacaoBD = new TramitacaoBD();

            if (tramitacaoBD.alteraTramitacao(tram)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiTramitacao() {

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.excluiTramitacao(listTramitacao.get(tblTramitacao.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados da tramitação excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para a tramitação!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void MenuJtableDetalhe() {

        jMenuItemAlterar.setText("Abrir Anexo");

        jMenuItemAlterar.addActionListener(
                new java.awt.event.ActionListener() {
                    // Importe a classe java.awt.event.ActionEvent

                    public void actionPerformed(ActionEvent e) {
                        int index = tblTramitacao.getSelectedRow();
                        try {
                            obterValorSelecao();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                });

        jPopupMenu.add(jMenuItemAlterar);

    }

    private void obterValorSelecao() throws IOException {
        if (tblTramitacao.getSelectedRow() != -1) {
            TramitacaoBD tramitacaoBD = new TramitacaoBD();
            listTramitacaoProcAnexos = tramitacaoBD.consultaTramitacao();
            //listAnexosProcesso = listTramitacaoProcAnexos.get(tblTramitacao.getSelectedRow()).getListAnexosProcesso();
            //listCadastrosImobImovel = listImovel.get(tblDetalheImobiliario.getSelectedRow()).getCadastrosImobImovel();
            int indice = tblTramitacao.getSelectedRow();
            try {
                //String arquivoteste = listTramitacaoProcAnexos.get(2).getAnexosProcesso().getNomeArquivo();
                String arquivo = listTramitacao.get(indice).getAnexosProcesso().getNomeArquivo();

                Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler " + "file:\\\\192.168.13.200\\sip\\arquivos\\" + arquivo);
                //java.awt.Desktop.getDesktop().open(new File("file:\\192.168.13.200\\sip\\arquivos\\GPS 05-2017.pdf"));
                //java.awt.Desktop.getDesktop().open(new File("C:\\Users\\jallisson\\Desktop\\GPS 05-2017.pdf"));

            } catch (Exception erro) {
                JOptionPane.showMessageDialog(null, "nao existe arquivo." + erro);
                erro.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um anexo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtaObservacao.addKeyListener(new KeyListener() {

            @Override
        public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
        public void run() {
                        JTextArea campo = (JTextArea) e.getSource();
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

        txtIdProcesso.setEditable(true);

        txtaObservacao.setEditable(true);
        //txtIdLote.setEditable(true);
        //btnSelecionaLote.setEnabled(true);
        btnSelecionaProcesso.setEnabled(true);
        btnSelecionaAnexo.setEnabled(true);
        jdcData.setEnabled(false);
        cbStatus.setEnabled(true);
        cbParecer.setEnabled(true);
        cbSetorOrigem.setEnabled(true);
        //cbLaudo.setEnabled(true);
    }

    private void desabilitaCampos() {

        txtIdProcesso.setEditable(false);
        txtaObservacao.setEditable(false);
        //txtIdLote.setEditable(false);
        //btnSelecionaLote.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        btnSelecionaAnexo.setEnabled(false);
        jdcData.setEnabled(false);
        cbStatus.setEnabled(false);
        cbParecer.setEnabled(false);
        cbSetorOrigem.setEnabled(false);
        //cbLaudo.setEnabled(false);

    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);

    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);

    }

    private void centralizaJanela(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 6,
                (this.getHeight() - internalFrame.getHeight()) / 2);
    }

    private void setaDataAtual() {

        java.util.Date hoje = new java.util.Date();

        jdcData.setDate(hoje);
    }

    public void requestFocusIdProcesso() {
        txtIdProcesso.requestFocus();
    }

    public void configuraSituacaoCad() {
        if (cbStatus.getSelectedItem().equals("RETORNO") && cbSetorOrigem.getSelectedItem().equals("CADASTRO")) {
            //cbSituacaoCad.setEnabled(true);
        }//else
        //cbSituacaoCad.setEnabled(false);

    }

    private void ConfiguraStatus() {
        cbSetorOrigem.removeItem(" ");
        cbSetorDestino.removeItem(" ");
        cbParecer.removeItem(" ");
        cbTipoDoc.removeItem(" ");

        if (cbStatus.getSelectedItem().equals("RECEBIDO")) {

            cbSetorOrigem.addItem(" ");
            cbSetorOrigem.setSelectedItem(" ");
            cbSetorOrigem.setEnabled(false);

            cbSetorDestino.addItem(" ");
            cbSetorDestino.setSelectedItem(" ");
            cbSetorDestino.setEnabled(false);

            cbParecer.addItem(" ");
            cbParecer.setSelectedItem(" ");
            cbParecer.setEnabled(false);

            cbTipoDoc.addItem(" ");
            cbTipoDoc.setSelectedItem(" ");
            cbTipoDoc.setEnabled(false);

        } else if (cbStatus.getSelectedItem().equals("SELECIONE")) {
            cbSetorOrigem.setSelectedItem("SELECIONE");
            cbSetorOrigem.setEnabled(false);
            //cbSetorOrigem.removeItem(" ");

            cbSetorDestino.setSelectedItem("SELECIONE");
            cbSetorDestino.setEnabled(false);
            //cbSetorDestino.removeItem(" ");

            cbTipoDoc.setEnabled(false);
        } else if (cbStatus.getSelectedItem().equals("ENVIADO")) {
            //cbSetorOrigem.removeItem(" ");
            cbSetorOrigem.setSelectedItem(getUsuario().getSetor());
            cbSetorOrigem.setEnabled(false);

            cbSetorDestino.setSelectedItem("SELECIONE");
            cbSetorDestino.setEnabled(true);
            //cbSetorDestino.removeItem(" ");

            cbParecer.addItem(" ");
            cbParecer.setSelectedItem(" ");
            cbParecer.setEnabled(false);

            cbTipoDoc.addItem(" ");
            cbTipoDoc.setSelectedItem(" ");
            cbTipoDoc.setEnabled(false);


        } else if (cbStatus.getSelectedItem().equals("RETORNO")) {
            //buscaTramitacao(txtIdProcesso.getText());
            // cbSetorOrigem.removeItem(" ");
            cbSetorOrigem.setSelectedItem(getUsuario().getSetor());
            cbSetorOrigem.setEnabled(false);

            cbSetorDestino.setSelectedItem("SELECIONE");
            cbSetorDestino.setEnabled(true);
            //cbSetorDestino.removeItem(" ");

            cbParecer.addItem(" ");
            cbParecer.setSelectedItem(" ");
            cbParecer.setEnabled(false);

            cbTipoDoc.addItem(" ");
            cbTipoDoc.setSelectedItem(" ");
            cbTipoDoc.setEnabled(false);

        } else if (cbStatus.getSelectedItem().equals("ARQUIVADO") || cbStatus.getSelectedItem().equals("LICENÇA EMITIDA") || cbStatus.getSelectedItem().equals("DESARQUIVADO")) {
            cbSetorOrigem.addItem(" ");
            cbSetorOrigem.setSelectedItem(" ");
            cbSetorOrigem.setEnabled(false);

            cbSetorDestino.addItem(" ");
            cbSetorDestino.setSelectedItem(" ");
            cbSetorDestino.setEnabled(false);

            cbParecer.addItem(" ");
            cbParecer.setSelectedItem(" ");
            cbParecer.setEnabled(false);

            cbTipoDoc.addItem(" ");
            cbTipoDoc.setSelectedItem(" ");
            cbTipoDoc.setEnabled(false);
        } else if (cbStatus.getSelectedItem().equals("DOC ANEXADO TIPO :")) {
            if (anexosProcesso == null) {
                JOptionPane.showMessageDialog(this, "Informe o Anexo", "Anexo", JOptionPane.INFORMATION_MESSAGE);
                btnSelecionaAnexo.requestFocus();
                cbStatus.setSelectedItem("SELECIONE");
            } else {
                cbSetorOrigem.addItem(" ");
                cbSetorOrigem.setSelectedItem(" ");
                cbSetorOrigem.setEnabled(false);

                cbSetorDestino.addItem(" ");
                cbSetorDestino.setSelectedItem(" ");
                cbSetorDestino.setEnabled(false);

                cbTipoDoc.setEnabled(true);

                cbParecer.setEnabled(true);

                String valor = cbTipoDoc.getSelectedItem().toString();

                if (!"PARECER CONCLUSIVO".equals(valor) & !"PARECER JURIDICO".equals(valor) & !"PARECER TÉCNICO".equals(valor)) {
                    cbParecer.addItem(" ");
                    cbParecer.setSelectedItem(" ");
                    cbParecer.setEnabled(false);
                } else {
                    cbParecer.removeItem(" ");
                    cbParecer.setSelectedItem("SELECIONE");
                    cbParecer.setEnabled(true);



                }

            }
        }
    }

    public void configuraOrigemDestino() {

        /*
        if(!"RETORNO".equals(cbStatus.getSelectedItem())){
        if (cbSetorDestino.getSelectedItem().equals(getUsuario().getSetor())) {
        cbSetorDestino.setSelectedItem("SELECIONE");
        }
        }*/
        if (cbSetorDestino.getSelectedItem().equals(getUsuario().getSetor())) {
            cbSetorDestino.setSelectedItem("SELECIONE");
        }


    }

    private void habilitaSituacaoCad() {
        if (getUsuario().getSetor().equals("TITULAÇÃO") || getUsuario().getSetor().equals("DIGITAÇÃO")) {
            //jPSituacaoCad.setVisible(true);
        } else {
            //jPSituacaoCad.setVisible(false);
        }
    }

    private void habilitaLaudoMzu() {
        if (getUsuario().getSetor().equals("MZU") || (getUsuario().getSetor().equals("ADMIN"))) {
            //jPLaudoMzu.setVisible(true);
        } else {
            //jPLaudoMzu.setVisible(false);
        }
    }

    private void habilitaLote() {
        if (getUsuario().getSetor().equals("TITULAÇÃO") || (getUsuario().getSetor().equals("ADMIN")) || (getUsuario().getSetor().equals("DIGITAÇÃO"))) {
            //jPLote.setVisible(true);
            //jPLote.setEnabled(false);
        } else {
            //jPLote.setVisible(false);
            // jPLote.setEnabled(true);
        }
    }

    private void configuraParecer() {
        String setor = "JURIDICO";
        boolean qualSetor = false;

        if (getUsuario().getSetor().equals(setor)) {
            qualSetor = true;
        }

        if (qualSetor == false) {
            cbParecer.addItem(" ");
            cbParecer.setSelectedItem(" ");
        }
    }

    private void configuraLaudoMzu() {
        String setor = "MZU";
        boolean qualSetor = false;

        if (getUsuario().getSetor().equals(setor)) {
            qualSetor = true;
        }

        if (qualSetor == false) {
            //cbLaudo.addItem(" ");
            //cbLaudo.setSelectedItem(" ");
        }
    }

    private void configuraStatusDestino() {
        String setorMzu = "MZU";
        String setorTit = "TITULAÇÃO";
        String setorPro = "PROTOCOLO";
        String setorJur = "JURIDICO";
        String setorGab = "GABINETE";
        String setorDig = "DIGITAÇÃO";

        if (getUsuario().getSetor().equals(setorMzu)) {
            //cbStatus.removeItem("ENVIADO");
            //cbStatus.removeItem("RECEBIDO");
            cbStatus.removeItem("EM LOTE");
            cbStatus.removeItem("TITULO ENTREGUE");
            cbStatus.removeItem("ARQUIVADO");
            cbStatus.removeItem("RETORNO");
            //cbSetorDestino.setEnabled(false);
            //cbSetorDestino.setSelectedItem("TITULAÇÃO");
        } else if ((getUsuario().getSetor().equals(setorJur)) || (getUsuario().getSetor().equals(setorGab))) {
            cbStatus.removeItem("EM LOTE");
            cbStatus.removeItem("TITULO ENTREGUE");
            cbStatus.removeItem("ARQUIVADO");
            cbStatus.removeItem("RETORNO");
        } else if (getUsuario().getSetor().equals(setorPro)) {
            cbStatus.removeItem("RECEBIDO");
            cbStatus.removeItem("RETORNO");
            cbStatus.removeItem("EM LOTE");
            cbStatus.removeItem("TITULO ENTREGUE");
            cbStatus.removeItem("ARQUIVADO");
            cbStatus.removeItem("RETORNO");
        }
    }
    
    public void enviaNotificacaoDenunciaFcm(String tkn, String corpo) {
        try {

            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "key=AIzaSyAoQh5qojWNnm_Tk5fAl8tsnFueD6mG-AE");
            conn.setRequestProperty("Content-Type", "application/json");

            JSONObject json = new JSONObject();

            json.put("to", tkn);

            JSONObject info = new JSONObject();
            info.put("title", "Secretaria do Meio Ambiente");   // Notification title
            info.put("body", corpo); // Notification body
            info.put("color", "#000099");

            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            conn.getInputStream();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao enviar notificação!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
            limpaCampos();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao enviar notificação!\n" + e, "Erro", JOptionPane.ERROR_MESSAGE);
        }

        // return null;
    }

    public void buscaProcesso() {
        ProcessoBD processoBD = new ProcessoBD();
        listProcesso = processoBD.consultaProcesso("ProcessoFrame");
        int binario = 0;
        try {
            int max = listProcesso.size();
            int id_busca = Integer.parseInt(txtIdProcesso.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listProcesso.get(i).getId();
                if (listProcesso.get(i).getId() == id_busca) {
                    setProcesso(listProcesso.get(i));
                    binario = 1;

                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdProcesso.setText("");
                txtNumeroProcesso.setText("");
                setProcesso(null);
            }
        } catch (NumberFormatException e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setProcesso(null);
            txtNumeroProcesso.setText("");
            JOptionPane.showMessageDialog(this, "Digite o ID", "ID", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void buscaTramitacao(String id) {
        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        listTramitacao = tramitacaoBD.consultaTramUltimoIdProce(id);
        int binario = 0;
        try {
            int max = listTramitacao.size();
            int id_busca = Integer.parseInt(id);
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listTramitacao.get(i).getProcesso().getId();
                if (listTramitacao.get(i).getProcesso().getId() == id_busca) {
                    setTramitacao(listTramitacao.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                //JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                limpaCamposSemProcData();
                setaDataAtual();
                setTramitacao(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setTramitacao(null);
            txtNumeroProcesso.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaUsuario(String idMonitor) {
        UsuarioBD usuarioBD = new UsuarioBD();
        listUsuario = usuarioBD.consultaUsuarioSimples();
        int binario = 0;
        try {
            int max = listUsuario.size();
            int id_busca = Integer.parseInt(idMonitor);
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
                usuario = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            usuario = null;
            txtNomeUsuario.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
            buscaLote();
        }
    }

    private void buscaLote() {
        /*
        LoteTitulacaoBD loteBD = new LoteTitulacaoBD();
        listLoteTitu = loteBD.consultaLote();
        int binario = 0;
        try {
        int max = listLoteTitu.size();
        int id_busca = Integer.parseInt(txtIdLote.getText());
        //Percorre a lista  
        for (int i = 0; i < max; i++) {
        listLoteTitu.get(i).getId();
        if (listLoteTitu.get(i).getId() == id_busca) {
        setLoteTitulacao(listLoteTitu.get(i));
        binario = 1;
        }
        }
        if (binario == 0) {
        JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
        txtIdLote.setText("");
        txtNomeLote.setText("");
        setLoteTitulacao(null);
        }
        } catch (Exception e) {
        System.out.println("ERRO: " + e);
        e.printStackTrace();
        setLoteTitulacao(null);
        txtNomeLote.setText("");
        //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }*/
    }

    private void buscaLoteTramite() {
        /*
        LoteTitulacaoBD loteBD = new LoteTitulacaoBD();
        listLoteTitu = loteBD.consultaLote();
        int binario = 0;
        try {
        int max = listLoteTitu.size();
        int id_busca = Integer.parseInt(txtIdLote.getText());
        //Percorre a lista  
        for (int i = 0; i < max; i++) {
        listLoteTitu.get(i).getId();
        if (listLoteTitu.get(i).getId() == id_busca) {
        setLoteTitulacao(listLoteTitu.get(i));
        binario = 1;
        }
        }
        if (binario == 0) {
        //JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
        txtIdLote.setText("");
        txtNomeLote.setText("");
        setLoteTitulacao(null);
        }
        } catch (Exception e) {
        System.out.println("ERRO: " + e);
        e.printStackTrace();
        setLoteTitulacao(null);
        txtNomeLote.setText("");
        //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }*/
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

        buttonGroupValor = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNomeUsuario = new javax.swing.JTextField();
        btnSelecionaUsuario = new javax.swing.JButton();
        txtIdUsuario = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtNumeroProcesso = new javax.swing.JTextField();
        btnSelecionaProcesso = new javax.swing.JButton();
        txtIdProcesso = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPParecer = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbParecer = new javax.swing.JComboBox();
        jPOrigem = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        cbSetorOrigem = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        cbStatus = new javax.swing.JComboBox();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtaObservacao = new javax.swing.JTextArea();
        jLabel20 = new javax.swing.JLabel();
        jPDestino = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbSetorDestino = new javax.swing.JComboBox();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel21 = new javax.swing.JPanel();
        txtNomeAnexo = new javax.swing.JTextField();
        btnSelecionaAnexo = new javax.swing.JButton();
        jPTipoDoc = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        cbTipoDoc = new javax.swing.JComboBox();
        jPanel3 = new javax.swing.JPanel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTramitacao = new javax.swing.JTable();
        cbPesquisa = new javax.swing.JComboBox();
        txtFiltroProcesso1 = new javax.swing.JTextField();
        btnFiltrar1 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setTitle("Tramitação");
        setPreferredSize(new java.awt.Dimension(1350, 660));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_64x64.png"))); // NOI18N
        jLabel1.setText("Tramitação");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel19.setBackground(new java.awt.Color(255, 255, 255));
        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Usuario"));
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
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Processo"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel18, gridBagConstraints);

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
        jPanel20.add(txtNumeroProcesso, gridBagConstraints);

        btnSelecionaProcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaProcesso.setEnabled(false);
        btnSelecionaProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaProcessoActionPerformed(evt);
            }
        });
        btnSelecionaProcesso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnSelecionaProcessoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaProcesso, gridBagConstraints);

        txtIdProcesso.setEditable(false);
        txtIdProcesso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtIdProcessoMouseExited(evt);
            }
        });
        txtIdProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdProcessoActionPerformed(evt);
            }
        });
        txtIdProcesso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdProcessoFocusLost(evt);
            }
        });
        txtIdProcesso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdProcessoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdProcessoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdProcessoKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdProcesso, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel20, gridBagConstraints);

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));
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

        jPanel7.setBackground(new java.awt.Color(255, 255, 255));
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
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPParecer.setBackground(new java.awt.Color(255, 255, 255));
        jPParecer.setBorder(javax.swing.BorderFactory.createTitledBorder("Parecer"));
        jPParecer.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Parecer");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPParecer.add(jLabel5, gridBagConstraints);

        cbParecer.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbParecer.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "DEFERIDO", "INDEFERIDO" }));
        cbParecer.setEnabled(false);
        cbParecer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbParecerActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPParecer.add(cbParecer, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPParecer, gridBagConstraints);

        jPOrigem.setBackground(new java.awt.Color(255, 255, 255));
        jPOrigem.setBorder(javax.swing.BorderFactory.createTitledBorder("Setor Origem"));
        jPOrigem.setLayout(new java.awt.GridBagLayout());

        jLabel6.setText("Setor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPOrigem.add(jLabel6, gridBagConstraints);

        cbSetorOrigem.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbSetorOrigem.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "PROTOCOLO", "DMA", "LICENCIAMENTO", "FISCALIZAÇÃO", "DIGITAÇÃO", "ANÁLISE", "JURIDICO", "ADMINISTRAÇÃO", "ADMIN" }));
        cbSetorOrigem.setEnabled(false);
        cbSetorOrigem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSetorOrigemActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPOrigem.add(cbSetorOrigem, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPOrigem, gridBagConstraints);

        jPanel8.setBackground(new java.awt.Color(255, 255, 255));
        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder("Status"));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel7.setText("Status");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel7, gridBagConstraints);

        cbStatus.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbStatus.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "ENVIADO", "RECEBIDO", "RETORNO", "ARQUIVADO", "DESARQUIVADO", "LICENÇA EMITIDA", "DOC ANEXADO TIPO :" }));
        cbStatus.setEnabled(false);
        cbStatus.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbStatusFocusLost(evt);
            }
        });
        cbStatus.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbStatusMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbStatusMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cbStatusMouseReleased(evt);
            }
        });
        cbStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbStatusActionPerformed(evt);
            }
        });
        cbStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbStatusKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                cbStatusKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cbStatusKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(cbStatus, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel8, gridBagConstraints);

        txtaObservacao.setColumns(20);
        txtaObservacao.setEditable(false);
        txtaObservacao.setRows(5);
        txtaObservacao.setWrapStyleWord(true);
        txtaObservacao.setName("Observação"); // NOI18N
        jScrollPane2.setViewportView(txtaObservacao);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jScrollPane2, gridBagConstraints);

        jLabel20.setText("Observação");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel2.add(jLabel20, gridBagConstraints);

        jPDestino.setBackground(new java.awt.Color(255, 255, 255));
        jPDestino.setBorder(javax.swing.BorderFactory.createTitledBorder("Setor Destino"));
        jPDestino.setLayout(new java.awt.GridBagLayout());

        jLabel8.setText("Setor");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPDestino.add(jLabel8, gridBagConstraints);

        cbSetorDestino.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbSetorDestino.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "PROTOCOLO", "DMA", "LICENCIAMENTO", "FISCALIZAÇÃO", "DIGITAÇÃO", "ANÁLISE", "JURIDICO", "ADMINISTRAÇÃO", "ADMIN" }));
        cbSetorDestino.setEnabled(false);
        cbSetorDestino.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbSetorDestinoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbSetorDestinoMousePressed(evt);
            }
        });
        cbSetorDestino.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSetorDestinoActionPerformed(evt);
            }
        });
        cbSetorDestino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbSetorDestinoFocusLost(evt);
            }
        });
        cbSetorDestino.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbSetorDestinoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPDestino.add(cbSetorDestino, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPDestino, gridBagConstraints);

        jPanel5.setBackground(new java.awt.Color(255, 255, 255));
        jPanel5.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/INFORMAÇÃO4.png"))); // NOI18N
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel2, gridBagConstraints);

        jLabel21.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel21.setText("Cores da Tabela ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        jPanel5.add(jLabel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 6;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel5, gridBagConstraints);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Anexo"));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        txtNomeAnexo.setEditable(false);
        txtNomeAnexo.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeAnexo.setEnabled(false);
        txtNomeAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeAnexoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(txtNomeAnexo, gridBagConstraints);

        btnSelecionaAnexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaAnexo.setEnabled(false);
        btnSelecionaAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaAnexoActionPerformed(evt);
            }
        });
        btnSelecionaAnexo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnSelecionaAnexoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(btnSelecionaAnexo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        jPTipoDoc.setBackground(new java.awt.Color(255, 255, 255));
        jPTipoDoc.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo Doc"));
        jPTipoDoc.setLayout(new java.awt.GridBagLayout());

        jLabel9.setText("Tipo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPTipoDoc.add(jLabel9, gridBagConstraints);

        cbTipoDoc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbTipoDoc.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "DESPACHO ", "ENCAMINHAMENTO", "LAUDO ", "MEMORANDO", "NOTIFICAÇÃO", "OFÍCIO", "PARECER CONCLUSIVO", "PARECER JURIDICO", "PARECER TÉCNICO", "REQUERIMENTO", "REQUISIÇÃO", "SOLICITAÇÃO", "TABELA", "TERMO", "TERMO DE COMPARECIMENTO" }));
        cbTipoDoc.setEnabled(false);
        cbTipoDoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoDocActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 10;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPTipoDoc.add(cbTipoDoc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPTipoDoc, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        txtFiltroProcesso.setToolTipText("INFORME OS QUATROS DIGITOS DO PROCESSO EXEMPLO PROCESSO 0755 E NÃO 755");
        txtFiltroProcesso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroProcessoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                txtFiltroProcessoMouseEntered(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                txtFiltroProcessoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txtFiltroProcessoMouseReleased(evt);
            }
        });
        txtFiltroProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroProcessoActionPerformed(evt);
            }
        });
        txtFiltroProcesso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroProcessoFocusLost(evt);
            }
        });
        txtFiltroProcesso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtFiltroProcessoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroProcesso, gridBagConstraints);

        btnFiltrar.setText("Pesquisar");
        btnFiltrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(btnFiltrar, gridBagConstraints);

        tblTramitacao.setBackground(new java.awt.Color(203, 216, 227));
        tblTramitacao.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        tblTramitacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Processo", "Tipo", "Requerente ou Denunciado", "Status", "Setor", "Origem", "Destino", "Data", "Parecer", "Usuario", "obs"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTramitacao.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        tblTramitacao.setSelectionBackground(new java.awt.Color(153, 153, 255));
        tblTramitacao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTramitacao.getTableHeader().setReorderingAllowed(false);
        tblTramitacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTramitacaoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tblTramitacaoMousePressed(evt);
            }
        });
        tblTramitacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                tblTramitacaoFocusLost(evt);
            }
        });
        tblTramitacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTramitacaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblTramitacao);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane1, gridBagConstraints);

        cbPesquisa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbPesquisa.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Filtro Pelo Processo", "Situação Atual" }));
        cbPesquisa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbPesquisaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbPesquisaMousePressed(evt);
            }
        });
        cbPesquisa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbPesquisaActionPerformed(evt);
            }
        });
        cbPesquisa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbPesquisaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(cbPesquisa, gridBagConstraints);

        txtFiltroProcesso1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroProcesso1MouseClicked(evt);
            }
        });
        txtFiltroProcesso1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroProcesso1ActionPerformed(evt);
            }
        });
        txtFiltroProcesso1.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroProcesso1FocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroProcesso1, gridBagConstraints);

        btnFiltrar1.setText("Pesquisar");
        btnFiltrar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiltrar1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(btnFiltrar1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        btnNovo.setMnemonic('N');
        btnNovo.setText("Novo Alt+N");
        btnNovo.setToolTipText("Alt+N");
        btnNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNovoActionPerformed(evt);
            }
        });
        jPanel4.add(btnNovo);

        btnAlterar.setMnemonic('A');
        btnAlterar.setText("Alterar Alt+A");
        btnAlterar.setEnabled(false);
        btnAlterar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarActionPerformed(evt);
            }
        });
        jPanel4.add(btnAlterar);

        btnExcluir.setText("Excluir");
        btnExcluir.setEnabled(false);
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
        txtFiltroProcesso.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblTramitacao.setEnabled(false);
            limpaSelecaoTabela();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            caixaAlta();
            setaDataAtual();

            //buscaProcesso();
            buscaUsuario(idUsuario);
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            if (status == null ? cbStatus.getSelectedItem().toString().trim() == null : status.equals(cbStatus.getSelectedItem().toString().trim())) {
                JOptionPane.showMessageDialog(this, "A tramição anterior é a mesma do status Selecionado!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
            } else {
                      try {
                incluirTramitacao();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
               
                tblTramitacao.setEnabled(true);
            }
            // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraTramitacao();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblTramitacao.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblTramitacao.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaProcesso();
            buscaLote();
            buscaUsuario(idUsuario);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma tramitação da lista!", "Tramitaçao", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed

        if (tblTramitacao.getSelectedRow() != -1) {

            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da tramitação?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiTramitacao();

            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma tramitação da lista!", "Tramitação", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseClicked
    }//GEN-LAST:event_txtFiltroProcessoMouseClicked

    private void txtFiltroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcessoActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoActionPerformed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        //atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void txtNomeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeUsuarioActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeUsuarioActionPerformed

    private void btnSelecionaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaUsuarioActionPerformed
}//GEN-LAST:event_btnSelecionaUsuarioActionPerformed

    private void txtIdUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdUsuarioFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdUsuarioFocusLost

    private void txtNumeroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcessoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNumeroProcessoActionPerformed

    private void btnSelecionaProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoActionPerformed
        setDeOnde("SelecionaProcesso");
        ProcessoFrame processoFrame = new ProcessoFrame(this);
        processoFrame.setVisible(true);
        this.getDesktopPane().add(processoFrame);
        processoFrame.toFront();
        centralizaJanela(processoFrame);             // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaProcessoActionPerformed

    private void txtIdProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdProcessoFocusLost
        buscaProcesso();
        buscaTramitacao(txtIdProcesso.getText());
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoFocusLost

    private void cbParecerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbParecerActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbParecerActionPerformed

    private void cbSetorOrigemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSetorOrigemActionPerformed
        configuraSituacaoCad();// TODO add your handling code here:
    }//GEN-LAST:event_cbSetorOrigemActionPerformed

    private void txtIdProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdProcessoActionPerformed
        buscaProcesso();
        buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoActionPerformed

    private void cbPesquisaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbPesquisaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbPesquisaActionPerformed

    private void cbPesquisaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbPesquisaMouseClicked
        controlePesquisa(); // TODO add your handling code here:
    }//GEN-LAST:event_cbPesquisaMouseClicked

    private void cbPesquisaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbPesquisaMousePressed
        controlePesquisa();  // TODO add your handling code here:
    }//GEN-LAST:event_cbPesquisaMousePressed

    private void cbPesquisaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbPesquisaFocusLost
        controlePesquisa();// TODO add your handling code here:
    }//GEN-LAST:event_cbPesquisaFocusLost

    private void txtFiltroProcesso1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcesso1MouseClicked
        atualizaTabelaSitAtual();  // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcesso1MouseClicked

    private void txtFiltroProcesso1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcesso1ActionPerformed
        atualizaTabelaSitAtual();  // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcesso1ActionPerformed

    private void txtFiltroProcesso1FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcesso1FocusLost
        atualizaTabelaSitAtual();  // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcesso1FocusLost

    private void btnFiltrar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrar1ActionPerformed
        atualizaTabelaSitAtual();  // TODO add your handling code here:
    }//GEN-LAST:event_btnFiltrar1ActionPerformed

    private void txtIdProcessoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIdProcessoMouseExited
        //buscaProcesso();  
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoMouseExited

    private void btnSelecionaProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaProcessoFocusLost

    private void txtIdProcessoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdProcessoKeyPressed
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoKeyPressed

    private void txtIdProcessoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdProcessoKeyReleased
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoKeyReleased

    private void txtIdProcessoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdProcessoKeyTyped
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdProcessoKeyTyped

    private void cbStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbStatusKeyPressed
        // if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        // configuraStatusParecer();// TODO add your handling code here:
}//GEN-LAST:event_cbStatusKeyPressed

    private void cbStatusFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbStatusFocusLost
        //ConfiguraStatus();
        //configuraSituacaoCad();
        //configuraStatusDestino();// TODO add your handling code here:
}//GEN-LAST:event_cbStatusFocusLost

    private void cbStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbStatusActionPerformed
        ConfiguraStatus();
        //configuraSituacaoCad();
        // configuraStatusDestino();// TODO add your handling code here:
}//GEN-LAST:event_cbStatusActionPerformed

    private void cbStatusMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbStatusMouseReleased
        // configuraStatusParecer(); // TODO add your handling code here:
}//GEN-LAST:event_cbStatusMouseReleased

    private void cbStatusMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbStatusMousePressed
        // if (evt.getClickCount() == 1) {
        //     ConfiguraStatus();
        //  } // TODO add your handling code here:
}//GEN-LAST:event_cbStatusMousePressed

    private void cbStatusMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbStatusMouseClicked
         if (evt.getClickCount() == 1) {
            ConfiguraStatus();
         }
        //configuraStatusDestino();
        // }
        // TODO add your handling code here:
}//GEN-LAST:event_cbStatusMouseClicked

    private void cbSetorDestinoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSetorDestinoActionPerformed
        configuraOrigemDestino();
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSetorDestinoActionPerformed

    private void cbSetorDestinoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbSetorDestinoMouseClicked
        if (evt.getClickCount() == 1) {
            configuraOrigemDestino();

        }
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSetorDestinoMouseClicked

    private void cbSetorDestinoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbSetorDestinoKeyPressed

        configuraOrigemDestino();
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSetorDestinoKeyPressed

    private void cbSetorDestinoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbSetorDestinoMousePressed
        if (evt.getClickCount() == 1) {
            configuraOrigemDestino();
        }  // TODO add your handling code here:
    }//GEN-LAST:event_cbSetorDestinoMousePressed

    private void cbSetorDestinoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbSetorDestinoFocusLost
        cbSetorDestino.removeItem(" "); // TODO add your handling code here:
    }//GEN-LAST:event_cbSetorDestinoFocusLost

    private void tblTramitacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTramitacaoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modo == Constantes.INCLUIR) {
                //selecionaProcessoImovel();
                dispose();

            }
            if (modo == Constantes.ALTERAR) {
                // selecionaCobradorAlterar();
                dispose();

            } else if (modo == Constantes.RELATORIO) {
                //selecionaCobRelPeri();
                dispose();
            }
            evt.consume();
        }
}//GEN-LAST:event_tblTramitacaoKeyPressed

    private void tblTramitacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTramitacaoMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) { // Clique com botao direito do mouse. 

            jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

            //JOptionPane.showMessageDialog(this, "Processo Selecionado!", "Processo", JOptionPane.INFORMATION_MESSAGE);
            //instanciei meu novo formulario   
        }


        if (evt.getClickCount() == 1) {
            tblTramitacao.setSelectionBackground(Color.BLUE);
        }
        if (evt.getClickCount() == 2) {
            if (modo == Constantes.INCLUIR) {
                //selecionaProcessoImovel();
                dispose();

            }
            if (modo == Constantes.ALTERAR) {
                //selecionaCobradorAlterar();
                dispose();

            } else if (modo == Constantes.RELATORIO) {
                //selecionaCobRelPeri();
                dispose();
            }
            evt.consume();
        }
}//GEN-LAST:event_tblTramitacaoMouseClicked

    private void tblTramitacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_tblTramitacaoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_tblTramitacaoFocusLost

    private void txtFiltroProcessoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtFiltroProcessoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcessoKeyPressed

    private void txtFiltroProcessoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcessoMouseReleased

    private void txtFiltroProcessoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcessoMouseEntered

    private void txtFiltroProcessoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroProcessoMousePressed

    private void tblTramitacaoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTramitacaoMousePressed
        if (evt.getClickCount() == 1) {
        }  // TODO add your handling code here:
    }//GEN-LAST:event_tblTramitacaoMousePressed

    private void txtNomeAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAnexoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeAnexoActionPerformed

    private void btnSelecionaAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAnexoActionPerformed
        //setIdProcesso(analiseFrame.getIdProcesso());
        //int indice = tblParecer.getSelectedRow();

        try {
            setDeOnde("SelecionaAnexo");
            setIdProcesso(getProcesso().getId());

            ProcessoFrame processoFrame = new ProcessoFrame(this);
            processoFrame.setVisible(true);
            this.getDesktopPane().add(processoFrame);
            processoFrame.toFront();
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "informe um id do processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);

        }
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaAnexoActionPerformed

    private void btnSelecionaAnexoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaAnexoFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaAnexoFocusLost

    private void cbTipoDocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoDocActionPerformed
        ConfiguraStatus();        // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoDocActionPerformed

    private void cbStatusKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbStatusKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusKeyTyped

    private void cbStatusKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbStatusKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cbStatusKeyReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnFiltrar1;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaAnexo;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JButton btnSelecionaUsuario;
    private javax.swing.ButtonGroup buttonGroupValor;
    private javax.swing.JComboBox cbParecer;
    private javax.swing.JComboBox cbPesquisa;
    private javax.swing.JComboBox cbSetorDestino;
    private javax.swing.JComboBox cbSetorOrigem;
    private javax.swing.JComboBox cbStatus;
    private javax.swing.JComboBox cbTipoDoc;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPDestino;
    private javax.swing.JPanel jPOrigem;
    private javax.swing.JPanel jPParecer;
    private javax.swing.JPanel jPTipoDoc;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tblTramitacao;
    private javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtFiltroProcesso1;
    private javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNomeAnexo;
    private javax.swing.JTextField txtNomeUsuario;
    private javax.swing.JTextField txtNumeroProcesso;
    private javax.swing.JTextArea txtaObservacao;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the requerente
     */
    /**
     * @return the digitador
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

    /**
     * @return the processo
     */
    public Processo getProcesso() {
        return processo;
    }

    /**
     * @param processo the processo to set
     */
    public void setProcesso(Processo processo) {
        this.processo = processo;
        txtNumeroProcesso.setText(processo.getNumProcesso());
        txtIdProcesso.setText(processo.getId().toString());
    }

    /**
     * @return the tramitacao
     */
    public Tramitacao getTramitacao() {
        return tramitacao;
    }

    /**
     * @param tramitacao the tramitacao to set
     */
    public void setTramitacao(Tramitacao tramitacao) {
        this.tramitacao = tramitacao;
        //txtNomeLote.setText(tramitacao.getLoteTitulacao().getLote());
        //txtIdLote.setText(tramitacao.getLoteTitulacao().getId().toString());
        //jdcData.setDate(tramitacao.getDataTramitacao());
        //ftfMes.setText(tramitacao.getMesAno());
        //cbStatus.setSelectedItem(tramitacao.getStatus());
        status = tramitacao.getStatus();
        //cbParecer.setSelectedItem(tramitacao.getParecer());
        //cbSetorOrigem.setSelectedItem(tramitacao.getSetorDestino());
        //txtaObservacao.setText(tramitacao.getObservacao());

        if (getUsuario().getSetor().equals("TITULAÇÃO")) {
            //buscaLoteTramite();
        }

    }

    /**
     * @return the idProcesso
     */
    public Integer getIdProcesso() {
        return idProcesso;
    }

    /**
     * @param idProcesso the idProcesso to set
     */
    public void setIdProcesso(Integer idProcesso) {
        this.idProcesso = idProcesso;
    }

    /**
     * @return the anexosProcesso
     */
    public AnexosProcesso getAnexosProcesso() {
        return anexosProcesso;
    }

    /**
     * @param anexosProcesso the anexosProcesso to set
     */
    public void setAnexosProcesso(AnexosProcesso anexosProcesso) {
        this.anexosProcesso = anexosProcesso;
        this.anexosProcesso = anexosProcesso;
        txtNomeAnexo.setText(anexosProcesso.getNomeArquivo());
    }

    /**
     * @return the deOnde
     */
    public String getDeOnde() {
        return deOnde;
    }

    /**
     * @param deOnde the deOnde to set
     */
    public void setDeOnde(String deOnde) {
        this.deOnde = deOnde;
    }
}
