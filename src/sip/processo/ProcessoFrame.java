/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.processo;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import com.lowagie.text.Cell;
import java.sql.SQLException;
import sip.atividade.Atividade;
import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.Constantes;
import sip.requerente.Requerente;
import sip.requerente.RequerenteBD;
import sip.requerente.RequerenteFrame;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.tramitacao.TramitacaoFrame;
import sip.emissaolicenca.EmissaoLicencaFrame;

import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import static jdk.nashorn.internal.objects.ArrayBufferView.length;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONObject;
import sip.analise.AnaliseFrame;
import sip.analise.ParecerAnaliseFrame;
import sip.atividade.AtividadeBD;
import sip.atividade.AtividadeFrame1;
import sip.autorizacaoeventos.AutorizacaoEventosFrame;
import sip.denuncia.Denuncia;
import sip.denuncia.DenunciaBD;
import sip.denuncia.DenunciaFrame;

import sip.distribuicao.DistribuicaoFrame;
import sip.fiscalizacao.FiscalizacaoFrame;
import sip.fiscalizacao.ParecerFiscalizacaoFrame;
import sip.juridico.JuridicoFrame;
import sip.juridico.ParecerJuridicoFrame;
import sip.movimentodma.MovimentoDmaFrame;
import sip.relatorio.RelatorioMovimentoProcessoFrame;
import sip.resumotriagem.ResumoTriagemBD;
import sip.tipolicenca.TipoLicenca;
import sip.tipolicenca.TipoLicencaBD;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class ProcessoFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private JDateChooser jdcDataMudanca;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableModelAnexoProcesso;
    private DefaultTableModel tableModelDetAnexoProcesso;
    private ListSelectionModel listModel;
    private List<Processo> listProcesso;
    private List<Processo> listProcAnexos;
    private List<AnexosProcesso> listAnexosProcesso;
    private List<AnexosProcesso> listAnexosProcesso2;
    private List<MudancaReq> listMudancaReq;
    private Processo processos;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoSeleciona;
    private TramitacaoFrame tramitacaoFrame;
    private AnaliseFrame analiseFrame;
    private JuridicoFrame juridicoFrame;
    private FiscalizacaoFrame fiscalizacaoFrame;
    private ParecerAnaliseFrame parecerAnaliseFrame;
    private ParecerJuridicoFrame parecerJuridicoFrame;
    private ParecerFiscalizacaoFrame parecerFiscalizacaoFrame;
    private Requerente requerente;
    private List<Requerente> listRequerente;
    private Denuncia denuncia;
    private List<Atividade> listAtividade;
    private Atividade atividade;
    private Requerente requerenteMudanca;
    private List<Requerente> listRequerenteMudanca;
    private Usuario usuario;
    private Usuario usuarioAnexos;
    private List<Usuario> listUsuario;
    private List<Denuncia> listDenuncia;
    private EmissaoLicencaFrame emissaoLicencaFrame;
    private AutorizacaoEventosFrame autorizacaoEventosFrame;
    private DistribuicaoFrame distFrame;
    private RelatorioMovimentoProcessoFrame relMovimentoProcesso;
    String idUsuario = Menu.id_Usuario;
    String tipo = "Normal";
    String situacao = "Normal";
    private Integer idProcessoAnexos;
    private String nomeArquivo;
    private String caminhoArquivo;
    private String origemArquivo;
    private Double valor_selecao;
    JPopupMenu jPopupMenu = new JPopupMenu();
    JMenuItem jMenuItemAlterar = new JMenuItem();
    private String consultaDeOnde = "ProcessoFrame";
    private int lastId;
    private String numeroProcesso;
    private String controle;
    private String mp;
    private String solicitante = "";
    private MovimentoDmaFrame movimentoDmaFrame;
    private List<TipoLicenca> listResumoTriagem;
    private final String apiKey = "AIzaSyAoQh5qojWNnm_Tk5fAl8tsnFueD6mG-AE";

    public ProcessoFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();
        jLAviso.setVisible(false);
        popularComboBoxTipoLicenca();
        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
        //jTabbedPane2.remove(jPanel2);
        MenuJtableDetalhe();//tabela anexos  
    }

    public ProcessoFrame(TramitacaoFrame tramitacaoFrame) {
        switch (tramitacaoFrame.getDeOnde()) {
            case "SelecionaProcesso":
                initComponents();
                defineModelo();
                formataData();
                caixaAlta();
                fechando();
                //configuraTipoAbreviado();
                buscaUsuario(idUsuario);

                jPanel21.setVisible(false);
                jdcData.setEnabled(false);
                jTabbedPane2.removeAll();
                jPanel4.setVisible(false);
                this.tramitacaoFrame = tramitacaoFrame;
                modoSeleciona = Constantes.TRAMITACAO_FRAME;
                //jRBNenhum.setVisible(false);
                jlClipe.setVisible(false);
                break;
            case "SelecionaAnexo":
                initComponents();
                defineModelo();
                formataData();
                caixaAlta();
                fechando();
                //configuraTipoAbreviado();
                buscaUsuario(idUsuario);
                listAnexosProcesso = new ArrayList<>();

                jPanel21.setVisible(false);
                jdcData.setEnabled(false);
                this.tramitacaoFrame = tramitacaoFrame;
                modoSeleciona = Constantes.TRAMITACAO_FRAME_ANEXO;
                jlClipe.setVisible(false);
                //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
                desabilaBotoesImportantes();
                jTabbedPane2.remove(jPanel2);
                jTabbedPane2.remove(jPanel24);
                MenuJtableDetalhe();//tabela anexos
                //configuracao referente ao seleciona Anexo
                jPanel3.setVisible(false);
                btnLocalizarAnexos.setEnabled(false);
                atualizaTabelaDetalheAnexo();
                jLAviso.setVisible(false);
                btnExcluiAnexos.setVisible(false);
                break;
        }
    }

    public ProcessoFrame(EmissaoLicencaFrame emissao) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.emissaoLicencaFrame = emissao;
        modoSeleciona = Constantes.EMISSAOLICENCAFRAME;
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
    }

    public ProcessoFrame(DistribuicaoFrame distFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        jTabbedPane2.removeAll();
        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.distFrame = distFrame;
        modoSeleciona = Constantes.DISTRIBUICAO_FRAME;
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
        consultaDeOnde = "DistribuicaoFrame";
    }

    public ProcessoFrame(AnaliseFrame analiseFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.analiseFrame = analiseFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        jTabbedPane2.remove(jPanel25);
        //MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao anexo
        btnSelecionaAnexo.setEnabled(true);
        txtDescricaoAnexo.setEditable(true);
        btnIncluiAnexo.setEnabled(true);
        modo = Constantes.INCLUIR_ANEXOS;
        jPanel3.setVisible(false);

    }
    
    public ProcessoFrame(AutorizacaoEventosFrame autorizacaoEventosFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        jTabbedPane2.removeAll();
        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.autorizacaoEventosFrame = autorizacaoEventosFrame;
        modoSeleciona = Constantes.AUTORIZACAOEVENTOS_FRAME;
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
        consultaDeOnde = "ProcessoFrame";
    }

    public ProcessoFrame(JuridicoFrame juridicoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.juridicoFrame = juridicoFrame;
        modoSeleciona = Constantes.JURIDICO_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao anexo
        btnSelecionaAnexo.setEnabled(true);
        txtDescricaoAnexo.setEditable(true);
        btnIncluiAnexo.setEnabled(true);
        modo = Constantes.INCLUIR_ANEXOS;
        jPanel3.setVisible(false);
        jTabbedPane2.remove(jPanel25);
    }

    public ProcessoFrame(FiscalizacaoFrame fiscalizacaoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.fiscalizacaoFrame = fiscalizacaoFrame;
        modoSeleciona = Constantes.FISCALIZACAO_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao anexo
        btnSelecionaAnexo.setEnabled(true);
        txtDescricaoAnexo.setEditable(true);
        btnIncluiAnexo.setEnabled(true);
        modo = Constantes.INCLUIR_ANEXOS_EXTERNOS;
        jPanel3.setVisible(false);
        btnLocalizarAnexos.setEnabled(false);
        atualizaTabelaDetalheAnexo();
        //jTabbedPane2.remove(jPanel25);
    }

    public ProcessoFrame(RelatorioMovimentoProcessoFrame relMovimentoProcesso) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        jTabbedPane2.removeAll();
        jPanel4.setVisible(false);
        this.relMovimentoProcesso = relMovimentoProcesso;
        modoSeleciona = Constantes.REL_MOVIMENTOPROCESSO;
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
    }

    public ProcessoFrame(ParecerAnaliseFrame parecerAnaliseFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        // configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.parecerAnaliseFrame = parecerAnaliseFrame;
        modoSeleciona = Constantes.PARECERANALISE_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        jTabbedPane2.remove(jPanel24);
        MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao seleciona Anexo
        jPanel3.setVisible(false);
        btnLocalizarAnexos.setEnabled(false);
        atualizaTabelaDetalheAnexo();
        jLAviso.setVisible(false);
        btnExcluiAnexos.setVisible(false);
    }

    public ProcessoFrame(ParecerJuridicoFrame parecerJuridicoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.parecerJuridicoFrame = parecerJuridicoFrame;
        modoSeleciona = Constantes.PARECERJURIDICO_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        jTabbedPane2.remove(jPanel24);
        MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao seleciona Anexo
        jPanel3.setVisible(false);
        btnLocalizarAnexos.setEnabled(false);
        atualizaTabelaDetalheAnexo();
        jLAviso.setVisible(false);
        btnExcluiAnexos.setVisible(false);

    }

    public ProcessoFrame(ParecerFiscalizacaoFrame parecerFiscalizacaoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);
        listAnexosProcesso = new ArrayList<>();

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        this.parecerFiscalizacaoFrame = parecerFiscalizacaoFrame;
        modoSeleciona = Constantes.PARECERFISCALIZACAO_FRAME;
        jlClipe.setVisible(false);

        //configuracao referente ao anexo eliminandos alguns field e panes que não não importante aqui
        desabilaBotoesImportantes();
        jTabbedPane2.remove(jPanel2);
        jTabbedPane2.remove(jPanel24);
        MenuJtableDetalhe();//tabela anexos
        //configuracao referente ao seleciona Anexo
        jPanel3.setVisible(false);
        btnLocalizarAnexos.setEnabled(false);
        atualizaTabelaDetalheAnexo();
        jLAviso.setVisible(false);
        btnExcluiAnexos.setVisible(false);

    }

    public ProcessoFrame(MovimentoDmaFrame movimentoDmaFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        //configuraTipoAbreviado();
        buscaUsuario(idUsuario);

        jPanel21.setVisible(false);
        jdcData.setEnabled(false);
        jTabbedPane2.removeAll();
        jPanel4.setVisible(false);
        this.movimentoDmaFrame = movimentoDmaFrame;
        modoSeleciona = Constantes.MOVIMENTO_DMA_FRAME;
        //jRBNenhum.setVisible(false);
        jlClipe.setVisible(false);
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

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel7.add(jdcData);
    }

    private void inserirZeroEsqPesq() {
        boolean ehNumero = true;
        try {
            Integer.parseInt(txtFiltroProcesso.getText());
        } catch (Exception e) {
            ehNumero = false;
        }

        if (ehNumero == true) {
            String tam = txtFiltroProcesso.getText();
            switch (tam.length()) {
                case 1: {
                    String valor = txtFiltroProcesso.getText();
                    txtFiltroProcesso.setText("000" + valor);
                    break;
                }
                case 2: {
                    String valor = txtFiltroProcesso.getText();
                    txtFiltroProcesso.setText("00" + valor);
                    break;
                }
                case 3: {
                    String valor = txtFiltroProcesso.getText();
                    txtFiltroProcesso.setText("0" + valor);
                    break;
                }
                default:
                    break;
            }
        }
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblProcesso.getModel();
        tableModelAnexoProcesso = (DefaultTableModel) tblInserirAnexosProcesso.getModel();
        tableModelDetAnexoProcesso = (DefaultTableModel) tblDetalheAnexo.getModel();
        listModel = tblProcesso.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheProcesso();
                    // mostraDetalheReqAnterior();

                    //mostra detalhe anexo
                    if (listProcAnexos != null) {
                        mostraDetalheAnexo();
                    }
                }
            }
        });
        try {
            MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
            mascaraCpf.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterCpf = new DefaultFormatterFactory(mascaraCpf);
            //ftfCpf.setFormatterFactory(formatterCpf);

            MaskFormatter mascaraTel = new MaskFormatter("0000");
            mascaraTel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterTel = new DefaultFormatterFactory(mascaraTel);
            //ftfNumero.setFormatterFactory(formatterTel);

            DecimalFormat formatoValor = new DecimalFormat("#,###.00");
            NumberFormatter formatterValor = new NumberFormatter(formatoValor);
            formatterValor.setValueClass(Double.class);
            //ftfValorDigitado.setFormatterFactory(new DefaultFormatterFactory(formatterValor));
            //ftfNumero1.setFormatterFactory(new DefaultFormatterFactory(formatterValor));

            tblProcesso.getColumnModel().getColumn(0).setMaxWidth(0);
            tblProcesso.getColumnModel().getColumn(0).setMinWidth(0);
            tblProcesso.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            tblProcesso.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

            //tblProcesso.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblProcesso.getColumnModel().getColumn(1).setPreferredWidth(180);
            tblProcesso.getColumnModel().getColumn(2).setPreferredWidth(500);
            tblProcesso.getColumnModel().getColumn(3).setPreferredWidth(280);
            tblProcesso.getColumnModel().getColumn(4).setPreferredWidth(150);
            tblProcesso.getColumnModel().getColumn(5).setPreferredWidth(100);

        } catch (ParseException ex) {
            Logger.getLogger(ProcessoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void centralizaJanelaRegVen(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 36,
                (this.getHeight() - internalFrame.getHeight()) / 12);
    }

    private void atualizaTabela() {
        ProcessoBD processoBD = new ProcessoBD();
        inserirZeroEsqPesq();
        String nome = null;

        if (txtFiltroProcesso.getText().equals("")) {
            if (listProcAnexos != null) {
                listProcAnexos.clear();
            }
            listProcesso = processoBD.consultaProcesso(consultaDeOnde);
            btnSelecionaProcesso.setEnabled(true);
        } else {
            String valor = txtFiltroProcesso.getText();
            listProcesso = processoBD.consultaProcessoNum(valor, valor);
            listProcAnexos = processoBD.consultaProcessoNum(valor, valor);
            btnSelecionaProcesso.setEnabled(true);
        }

        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }

        for (int i = 0; i < listProcesso.size(); i++) {
            if (listProcesso.get(i).getRequerente().getNome() == null) {
                nome = listProcesso.get(i).getPessoa().getNome();
            } else {
                nome = listProcesso.get(i).getRequerente().getNome();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listProcesso.get(i).getId(),
                listProcesso.get(i).getNumProcesso(),
                nome,
                listProcesso.get(i).getTipoLicenca(),
                simpleDateFormat.format(listProcesso.get(i).getDataProcesso()),
                listProcesso.get(i).getUsuario().getNome(),}
            );

            //cbEstudo.addItem(listProcesso.get(i).getNumProcesso());
        }
    }

    private void inserirMp() {
        if (jCBOficio.isSelected()) {
            mp = "SIM";
        } else {
            mp = "NAO";
        }
    }

    private void configuraSolicitacao() {
        if (jCBOficio.isSelected()) {
            cbSolicitacao.setEnabled(true);
            if (cbSolicitacao.getSelectedItem() == "MP") {
                solicitante = "SOLICITAÇÃO MINISTÉRIO PÚBLICO: ";
            } else if (cbSolicitacao.getSelectedItem() == "DNIT") {
                solicitante = "SOLICITAÇÃO DNIT: ";
            } else if (cbSolicitacao.getSelectedItem() == "PGM") {
                solicitante = "SOLICITAÇÃO PGM: ";
            } else {
                solicitante = "";
            }

        } else {
            cbSolicitacao.setEnabled(false);
            solicitante = "";
        }
    }

    private void configuraDenuncia() {
        if (jCBDenuncia.isSelected()) {
            cbTipo.setEnabled(false);
            cbTipo.setSelectedItem("DENÚNCIA");
            txtIdDenuncia.setEnabled(true);
            btnSelecionaDenuncia.setEnabled(true);
            txtIdRequerente.setEnabled(false);
            btnSelecionaRequerente.setEnabled(false);
            txtIdAtividade.setEnabled(false);
            btnSelecionaAtividade.setEnabled(false);
            jCBOficio.setSelected(false);
        } else {
            cbTipo.setEnabled(true);
            cbTipo.setSelectedItem("LICENÇA PRÉVIA");
            txtIdDenuncia.setEnabled(false);
            btnSelecionaDenuncia.setEnabled(false);
            txtIdRequerente.setEnabled(true);
            btnSelecionaRequerente.setEnabled(true);
            txtIdAtividade.setEnabled(true);
            btnSelecionaAtividade.setEnabled(true);

        }

    }

    private void atualizaTabelaAnexosProcesso() {
        int numeroLinhas = tblInserirAnexosProcesso.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModelAnexoProcesso.removeRow(0);
        }
        double total = 0.0;
        for (int i = 0; i < listAnexosProcesso.size(); i++) {
            tableModelAnexoProcesso.insertRow(i, new Object[]{
                listAnexosProcesso.get(i).getDescricaoAnexo(),
                listAnexosProcesso.get(i).getNomeArquivo()
            });
        }
    }

    private void atualizaTabelaEstudo() {
        limpaTabelaAnexosProcesso();
        boolean achei = false;
        int numeroLinhas = tblInserirAnexosProcesso.getRowCount();
        int numeroColunas = tblInserirAnexosProcesso.getColumnCount();
        //double total = 0.0;
        for (int i = 0; i < numeroLinhas; i++) {
            for (int j = 0; j < numeroColunas; j++) {
                String valorActual = (String) tblInserirAnexosProcesso.getValueAt(i, j);
                if (valorActual.equals(this.txtArquivo.getText().trim())) {
                    achei = true;
                    listAnexosProcesso.remove(i);
                    break;
                } else {
                    break;
                }
            }
        }
        if (achei == true) {
            String valorAtual = txtArquivo.getText().trim();
            JOptionPane.showMessageDialog(null, "O Anexo já Existe: " + valorAtual, null, WIDTH);

        } else {
            atualizaTabelaAnexosProcesso();
        }
    }

    private void atualizaTabelaDetalheAnexo() {
        ProcessoBD processoBD = new ProcessoBD();
        /*String valor;
        if (modoSeleciona == Constantes.PARECERANALISE_FRAME) {
        valor = parecerAnaliseFrame.getIdProcesso().toString();
        listProcAnexos = processoBD.consultaAnexosProcesso();
        } else {
        valor = txtFiltroProcesso.getText();
        if (txtFiltroProcesso.getText().trim().equals("")) {
        listProcAnexos = processoBD.consultaAnexosProcesso();
        
        } else {
        
        listProcAnexos = processoBD.consultaAnexosProcessoNome(valor, valor);
        }
        }*/
        switch (modoSeleciona) {
            case Constantes.PARECERANALISE_FRAME:
                listProcAnexos = processoBD.consultaAnexosProcessoId(parecerAnaliseFrame.getIdProcesso().toString());
                mostraDetalheAnexoConsultaExterna();
                break;
            case Constantes.PARECERJURIDICO_FRAME:
                listProcAnexos = processoBD.consultaAnexosProcessoId(parecerJuridicoFrame.getIdProcesso().toString());
                mostraDetalheAnexoConsultaExterna();
                break;
            case Constantes.PARECERFISCALIZACAO_FRAME:
                listProcAnexos = processoBD.consultaAnexosProcessoId(parecerFiscalizacaoFrame.getIdProcesso().toString());
                mostraDetalheAnexoConsultaExterna();
                break;
            case Constantes.FISCALIZACAO_FRAME:
                listProcAnexos = processoBD.consultaAnexosProcessoId(fiscalizacaoFrame.getIdProcesso().toString());
                mostraDetalheAnexoConsultaExterna();
                break;
            case Constantes.TRAMITACAO_FRAME_ANEXO:
                listProcAnexos = processoBD.consultaAnexosProcessoId(tramitacaoFrame.getIdProcesso().toString());
                mostraDetalheAnexoConsultaExterna();
                break;
            default:
                listProcAnexos = processoBD.consultaAnexosProcesso();
                mostraDetalheAnexo();
                break;
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheProcesso() {

        if (tblProcesso.getSelectedRow() != -1) {
            int indice = tblProcesso.getSelectedRow();
            ftfNumero.setText(listProcesso.get(indice).getNumProcesso());
            txtIdRequerente.setText(listProcesso.get(indice).getRequerente().getId().toString());
            txtNomeRequerente.setText(listProcesso.get(indice).getRequerente().getNome());
            txtIdAtividade.setText(listProcesso.get(indice).getAtividade().getId().toString());
            txtNomeAtividade.setText(listProcesso.get(indice).getAtividade().getNome());
            jdcData.setDate(listProcesso.get(indice).getDataProcesso());
            ftfMes.setText(listProcesso.get(indice).getMesAno());
            cbTipo.setSelectedItem(listProcesso.get(indice).getTipoLicenca());
            //SimpleDateFormat ano = new SimpleDateFormat("yyyy");
            //String formattedDate = ano.format(listProcesso.get(indice).getDataProcesso());
            //int year = Integer.parseInt(formattedDate);
            String teste = listProcesso.get(indice).getNumProcesso();
            String ano = teste.substring(teste.length() - 4);
            //obter 4 ultimos string do campo numero_processop
            int year = Integer.parseInt(ano);
            jycAno.setYear(year);
            if ("SIM".equals(listProcesso.get(indice).getMp())) {
                jCBOficio.setSelected(true);
            } else {
                jCBOficio.setSelected(false);
            }
            if ("DENÚNCIA".equals(listProcesso.get(indice).getTipoLicenca())) {
                jCBDenuncia.setSelected(true);
            } else {
                jCBDenuncia.setSelected(false);
            }
            txtIdDenuncia.setText(listProcesso.get(indice).getDenuncia().getId().toString());
            txtNomeDenunciado.setText(listProcesso.get(indice).getPessoa().getNome());
            jTARelatoOcorrencia.setText(listProcesso.get(indice).getObservacao());
            //txtArquivo.setText(listProcesso.get(indice).getNomeArquivo());
            /*if (listProcesso.get(indice).getNomeArquivo() == null) {
            jlClipe.setVisible(false);
            } else {
            jlClipe.setVisible(true);
            }
            /*
            if (listProcesso.get(indice).getSituacao().equals("Usucapião")) {
            jRBUsoCapiao.setSelected(true);
            } else {
            jRBUsoCapiao.setSelected(false);
            }
            /*switch (listProcesso.get(indice).getTipo()) {
            case "Segunda Via":
            jRBSegundaVia.setSelected(true);
            break;
            case "Transmutacao":
            jRBTransmutacao.setSelected(true);
            break;
            case "Normal":
            jRBNormal.setSelected(true);
            break;
            default:
            jRBNenhum.setSelected(true);
            break;
            }*/

        } else {
            ftfNumero.setText("");
            txtIdRequerente.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
            ftfMes.setText("");
            cbTipo.setSelectedItem("Normal");
            //jRBSegundaVia.setSelected(false);
            //jRBTransmutacao.setSelected(false);
        }
    }

    private void mostraDetalheReqAnterior() {
        ProcessoBD processoBD = new ProcessoBD();
        Integer idProcesso = listProcesso.get(tblProcesso.getSelectedRow()).getId();
        String iD = idProcesso.toString();
        listMudancaReq = processoBD.consultaUltimoRequerente(iD);
        String nome;

        if (tblProcesso.getSelectedRow() != -1) {

            int indice = 0;

            txtNomeRequerenteAnterior.setText(listMudancaReq.get(indice).getRequerente().getNome());
            //nome = listMudancaReq.get(indice).getRequerente().getNome();
            //JOptionPane.showMessageDialog(this, "O nome e! "+nome, "Nome", JOptionPane.INFORMATION_MESSAGE);

        } else {

            txtNomeRequerenteAnterior.setText("");

        }
    }

    private void removeAnexosProcessoTabela() {
        int numeroLinhas = tblDetalheAnexo.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModelDetAnexoProcesso.removeRow(0);
        }
    }

    private void mostraDetalheAnexo() {
        if (tblProcesso.getSelectedRow() != -1) {
            listAnexosProcesso2 = listProcAnexos.get(tblProcesso.getSelectedRow()).getAnexosProcesso();
            int testeIndex = tblProcesso.getSelectedRow();
            removeAnexosProcessoTabela();
            for (int i = 0; i < listAnexosProcesso2.size(); i++) {
                tableModelDetAnexoProcesso.insertRow(i, new Object[]{
                    listAnexosProcesso2.get(i).getId(),
                    listAnexosProcesso2.get(i).getDescricaoAnexo(),
                    listAnexosProcesso2.get(i).getNomeArquivo(),
                    listAnexosProcesso2.get(i).getUsuario().getNome(),
                    listAnexosProcesso2.get(i).getUsuario().getSetor()
                });
            }

        } else {
            removeAnexosProcessoTabela();
        }
    }

    private void mostraDetalheAnexoConsultaExterna() {
        if (modoSeleciona == Constantes.PARECERJURIDICO_FRAME || modoSeleciona == Constantes.PARECERANALISE_FRAME || modoSeleciona == Constantes.TRAMITACAO_FRAME_ANEXO || modoSeleciona == Constantes.PARECERFISCALIZACAO_FRAME || modoSeleciona == Constantes.FISCALIZACAO_FRAME) {
            listAnexosProcesso2 = listProcAnexos.get(0).getAnexosProcesso();

            removeAnexosProcessoTabela();
            for (int i = 0; i < listAnexosProcesso2.size(); i++) {
                tableModelDetAnexoProcesso.insertRow(i, new Object[]{
                    listAnexosProcesso2.get(i).getId(),
                    listAnexosProcesso2.get(i).getNomeArquivo(),
                    listAnexosProcesso2.get(i).getCaminhoArquivo(),
                    listAnexosProcesso2.get(i).getUsuario().getNome(),
                    listAnexosProcesso2.get(i).getUsuario().getSetor()
                });
            }

        } else {
            removeAnexosProcessoTabela();
        }
    }

    private void executaRelatorioCapa() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblProcesso.getSelectedRow();
        JasperPrint jp;

        int mostraID = listProcesso.get(indice).getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");

            //*para funcionario abrir os relatorio dentro do pacote tem q configurar o parametros no ireport como InputScream
            //InputStream caminhoImagemBrasao = getClass().getResourceAsStream("/sip/imagemrelatorio/logocidade.jpg");
            //parametros.put("CAMINHO_IMAGEM", caminhoImagemBrasao);
            //parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");
            parametros.put("ID_PROCESSO", (long) mostraID);

            if (jCBDenuncia.isSelected()) {
                jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "//relatorios//FolhaRostoDenuncia.jasper", parametros, acessoBd.conectar());
            } else {
                jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "//relatorios//FolhaRosto.jasper", parametros, acessoBd.conectar());
            }

            //*InputStream is = getClass().getResourceAsStream("/sip/relatoriosarquivo/FolhaRosto.jasper");
            //*JasperPrint jp = JasperFillManager.fillReport(is, parametros, acessoBd.conectar());
            //JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            JasperViewer.viewReport(jp, false);

        } catch (Exception ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, ex);
        }
    }

    private void controlaIdProcessoAnexo() {
        switch (modoSeleciona) {
            case Constantes.ANALISE_FRAME:
                idProcessoAnexos = analiseFrame.getIdProcesso();
                break;
            case Constantes.JURIDICO_FRAME:
                idProcessoAnexos = juridicoFrame.getIdProcesso();
                break;
            case Constantes.FISCALIZACAO_FRAME:
                idProcessoAnexos = fiscalizacaoFrame.getIdProcesso();
                break;
            case Constantes.PARECERFISCALIZACAO_FRAME:
                idProcessoAnexos = parecerFiscalizacaoFrame.getIdProcesso();
                break;
            default:
                idProcessoAnexos = listProcesso.get(tblProcesso.getSelectedRow()).getId();
                break;
        }
    }

    private void incluiVariosAnexosProcesso() {
        if (nomeArquivo == null) {
            JOptionPane.showMessageDialog(this, "Selecione anexos!", "Processo", JOptionPane.INFORMATION_MESSAGE);

        } else if (txtDescricaoAnexo.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Descreva o anexo!", "Erro", JOptionPane.INFORMATION_MESSAGE);
        } else {

            AnexosProcesso anexosProcesso = new AnexosProcesso();
            anexosProcesso.setNomeArquivo(nomeArquivo);
            anexosProcesso.setCaminhoArquivo(caminhoArquivo);
            anexosProcesso.setOrigemArquivo(origemArquivo);
            anexosProcesso.setDescricaoAnexo(txtDescricaoAnexo.getText());
            this.listAnexosProcesso.add(anexosProcesso);
            //atualizaTabelaCadImob();
            atualizaTabelaEstudo();
            //limpaCamposCadImobiliario();
            //anulaObjetos();
            //controlaBotaoSalvar();
            //configuraBtnIncluir();
        }
    }

    private void incluiUnicoAnexosProcesso() {
        if (nomeArquivo == null) {
            JOptionPane.showMessageDialog(this, "Selecione anexos!", "Processo", JOptionPane.INFORMATION_MESSAGE);

        } else {

            AnexosProcesso anexosProcesso = new AnexosProcesso();
            anexosProcesso.setNomeArquivo(nomeArquivo);
            anexosProcesso.setCaminhoArquivo(caminhoArquivo);
            anexosProcesso.setOrigemArquivo(origemArquivo);
            anexosProcesso.setUsuario(getUsuario());
            anexosProcesso.setId(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()).getId());

            this.listAnexosProcesso.add(anexosProcesso);
            //atualizaTabelaCadImob();
            atualizaTabelaEstudo();
            //limpaCamposCadImobiliario();
            //anulaObjetos();
            //controlaBotaoSalvar();
            //configuraBtnIncluir();
        }
    }

    private void inserirAnexosProcesso() {

        if (listAnexosProcesso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira anexo para Salvar!", "Anexo do Processo", JOptionPane.INFORMATION_MESSAGE);

        } else {
            buscaUsuario(idUsuario);
            Processo processo = new Processo();
            controlaIdProcessoAnexo();

            //processo.setId(listProcesso.get(tblProcesso.getSelectedRow()).getId());
            processo.setId(idProcessoAnexos);
            processo.setAnexosProcesso(listAnexosProcesso);
            processo.setUsuario(getUsuario());

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.IncluirAnexosProcesso(processo)) {
                JOptionPane.showMessageDialog(this, "Anexos Inserido com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                //desabilitaBotoesTabela();

                copiarServidor();
                if (modo == Constantes.INCLUIR_ANEXOS_EXTERNOS) {
                    this.dispose();
                }
                limpaTabelaAnexosProcesso();
                desabilitaBotoes();
                desabilitaCampos();
                desabilitaCamposBotoesAnexo();
                limpaCampos();
                //limpaCamposCadImobiliario();
                listAnexosProcesso.clear();
                atualizaTabela();
                tblProcesso.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Inserir Anexos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void AlterarAnexosProcesso() {
        incluiUnicoAnexosProcesso();
        if (listAnexosProcesso.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Insira anexo para Salvar!", "Anexo do Processo", JOptionPane.INFORMATION_MESSAGE);

        } else {
            buscaUsuario(idUsuario);
            Processo processo = new Processo();
            controlaIdProcessoAnexo();

            //processo.setId(listAnexosProcesso.get(tblDetalheAnexo.getSelectedRow()).getId());
            //processo.setId(idProcessoAnexos);
            processo.setAnexosProcesso(listAnexosProcesso);
            //processo.setUsuario(getUsuario());

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.AlterarAnexosProcesso(processo)) {
                JOptionPane.showMessageDialog(this, "Anexo Alterado com sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                //desabilitaBotoesTabela();
                copiarServidor();
                limpaTabelaAnexosProcesso();
                desabilitaBotoes();
                //desabilitaCampos();
                //desabilitaCamposBotoesAnexo();
                //limpaCampos();
                //limpaCamposCadImobiliario();
                listAnexosProcesso.clear();
                atualizaTabelaDetalheAnexo();
                //atualizaTabela();
                //tblProcesso.setEnabled(true);
                btnAlterarAnexo.setEnabled(true);
                tblDetalheAnexo.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Erro ao Inserir Anexos!", "Erro", JOptionPane.ERROR_MESSAGE);
            }

        }
    }

    private void controle() {
        popularComboBoxTipoLicenca();
        ftfAno.setValue(jdcData.getDate());
        Integer ano;
        ano = jdcData.getDate().getYear();
        if (jCBOficio.isSelected()) {
            numeroProcesso = "SL " + ftfNumero.getText() + "/" + jycAno.getYear();
        } else if (jCBDenuncia.isSelected()) {
            numeroProcesso = "DN " + ftfNumero.getText() + "/" + jycAno.getYear();
        } else {
            numeroProcesso = ftfNumero.getText() + "/" + jycAno.getYear();
        }
        
        String teste = "teste";
        
        //if (cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE OPERAÇÃO" || cbTipo.getSelectedItem() == "RENOVAÇÃO DE LICENÇA DE OPERAÇÃO" || cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE INSTALAÇÃO" || cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE OPERAÇÃO" || cbTipo.getSelectedItem() == "RENOVAÇÃO DA LICENÇA ÚNICA" || cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE INSTALAÇÃO" || cbTipo.getSelectedItem() == "RENOVAÇÃO DE LICENÇA DE EXTRAÇÃO") {
        if ("RLO".equals(listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura()) || "RLE".equals(listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura()) || "RLU".equals(listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura()) || "PLO".equals(listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura()) || "PLI".equals(listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura())) {
            controle = numeroProcesso + " " + listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura() + " " + ftfAno.getText();
        } else {
            controle = numeroProcesso + " " + listResumoTriagem.get(cbTipo.getSelectedIndex()).getAbreviatura();
        }

    }

    private void incluiProcesso() throws SQLException, ParseException {
        if ((ftfNumero.getText().trim().equals(""))) {
            JOptionPane.showMessageDialog(this, "O campo processo deve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfNumero.requestFocus();
        } else if (getRequerente() == null && jCBDenuncia.isSelected() == false) {
            JOptionPane.showMessageDialog(this, "Selecione o requerente!", "Requerente", JOptionPane.INFORMATION_MESSAGE);
        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if ((cbTipo.getSelectedItem() != "AUTORIZAÇÃO PARA EVENTOS") && (getAtividade() == null) && jCBDenuncia.isSelected() == false) {
            JOptionPane.showMessageDialog(this, "Informe a atividade!", "Atividade", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Processo processo = new Processo();
            ftfMes.setValue(jdcData.getDate());
            
            controle();
            inserirMp();

            //selecaoSituacao();
            processo.setNumProcesso(numeroProcesso);
            processo.setRequerente(getRequerente());
            processo.setUsuario(getUsuario());
            processo.setDataProcesso(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            processo.setMesAno(ftfMes.getText());
            processo.setTipoLicenca(solicitante + cbTipo.getSelectedItem().toString());
            processo.setTramitouDMA("NAO");
            processo.setAtividade(getAtividade());
            processo.setArquivado("NAO");
            processo.setControle(controle);
            processo.setLancadoDma("NAO");
            processo.setMp(mp);
            processo.setDenuncia(getDenuncia());
            processo.setObservacao(jTARelatoOcorrencia.getText().trim());

            ProcessoBD processoBD = new ProcessoBD();
            if (processoBD.incluiProcesso(processo)) {
                JOptionPane.showMessageDialog(this, "Processo cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

                atualizaTabela();
                executaTramitacao();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
                desabilitaCamposBotoesAnexo();

            } else {
                JOptionPane.showMessageDialog(this, "Processo já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraProcesso() {
        if ((ftfNumero.getText().trim().equals(""))) {
            JOptionPane.showMessageDialog(this, "O campo processo deve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfNumero.requestFocus();
        } else if (getRequerente() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o requerente!", "Campos", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if ((cbTipo.getSelectedItem() != "AUTORIZAÇÃO PARA EVENTOS") && (getAtividade() == null)) {
            JOptionPane.showMessageDialog(this, "Informe a atividade!", "Atividade", JOptionPane.INFORMATION_MESSAGE);
        } else {

            //selecaoSituacao();
            Processo processo = new Processo();
            controle();
            inserirMp();
            //String numeroProcesso = ftfNumero.getText() + "/" + jycAno.getYear();
            ftfMes.setValue(jdcData.getDate());
            processo.setId(listProcesso.get(tblProcesso.getSelectedRow()).getId());
            processo.setNumProcesso(numeroProcesso);
            //processo.setRequerente(getRequerente());
            processo.setUsuario(getUsuario());
            processo.setDataProcesso(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            processo.setMesAno(ftfMes.getText());
            processo.setTipoLicenca(cbTipo.getSelectedItem().toString());
            processo.setAtividade(getAtividade());
            processo.setMp(mp);
            processo.setDenuncia(getDenuncia());
            processo.setObservacao(jTARelatoOcorrencia.getText().trim());
            /*if (nomeArquivo == null) {
            processo.setNomeArquivo(null);
            } else {
            processo.setNomeArquivo(nomeArquivo);
            }
            
            if (caminhoArquivo == null) {
            processo.setCaminhoArquivo(null);
            } else {
            processo.setCaminhoArquivo(caminhoArquivo);
            }*/

            ProcessoBD processoBD = new ProcessoBD();
            if (processoBD.alteraProcesso(processo)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                tblProcesso.setEnabled(true);
                txtFiltroProcesso.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraProcessoExluirAnexo() {
        //selecaoSituacao();
        Processo processo = new Processo();

        String numeroProce = jycAno.getYear() + "/" + ftfNumero.getText();
        ftfMes.setValue(jdcData.getDate());
        processo.setId(listProcesso.get(tblProcesso.getSelectedRow()).getId());
        processo.setNumProcesso(numeroProce);
        //processo.setRequerente(getRequerente());
        processo.setUsuario(getUsuario());
        processo.setDataProcesso(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
        processo.setMesAno(ftfMes.getText());
        processo.setTipoLicenca(cbTipo.getSelectedItem().toString());

        /*
        if (nomeArquivo == null) {
        processo.setNomeArquivo(null);
        } else {
        processo.setNomeArquivo(nomeArquivo);
        }
        
        if (caminhoArquivo == null) {
        processo.setCaminhoArquivo(null);
        } else {
        processo.setCaminhoArquivo(caminhoArquivo);
        }*/
        ProcessoBD processoBD = new ProcessoBD();
        if (processoBD.alteraProcesso(processo)) {
            JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
            tblProcesso.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void alteraProcTramiDMA() {

        if ("NAO".equals(listProcesso.get(tblProcesso.getSelectedRow()).getArquivado())) {
            Processo processo = new Processo();

            processo.setId(listProcesso.get(tblProcesso.getSelectedRow()).getId());

            processo.setTramitouDMA("SIM");

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.alteraProcTramiDMA(processo)) {
                try {
                    //JOptionPane.showMessageDialog(this, "Pr!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                    executaTramitacaoDistribuicao();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
        }

    }
    

    private void excluiAnexoTabelaInserir() {
        if (tblInserirAnexosProcesso.getSelectedRow() != -1) {
            listAnexosProcesso.remove(tblInserirAnexosProcesso.getSelectedRow());
            atualizaTabelaAnexosProcesso();
            //controlaBotaoSalvar();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Pedido da Tabela!", "Pedido", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void alteraProcessoRequerente() {
        if ((ftfNumero.getText().trim().equals(""))) {
            JOptionPane.showMessageDialog(this, "O campo processo deve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfNumero.requestFocus();
        }
        if (getRequerente() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o requerente!", "Campos", JOptionPane.INFORMATION_MESSAGE);

        }
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {
            jdcDataMudanca = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
            java.util.Date hoje = new java.util.Date();
            jdcDataMudanca.setDate(hoje);
            buscaProcesso(listProcesso.get(tblProcesso.getSelectedRow()).getId());

            int teste = listProcesso.get(tblProcesso.getSelectedRow()).getRequerente().getId();

            buscaRequerenteMudanca(teste);
            MudancaReq mRequerente = new MudancaReq();
            mRequerente.setRequerente(getRequerenteMudanca());
            mRequerente.setProcesso(getProcesso());
            mRequerente.setDataMudanca(new java.sql.Date(((java.util.Date) jdcDataMudanca.getDate()).getTime()));
            mRequerente.setUsuario(getUsuario());

            //selecaoSituacao();
            Processo processo = new Processo();

            //processo.setId(listProcesso.get(tblProcesso.getSelectedRow()).getId());
            processo.setId(getProcesso().getId());
            processo.setRequerente(getRequerente());
            processo.setUsuario(getUsuario());

            ProcessoBD processoBD = new ProcessoBD();
            if (processoBD.alteraProcessoRequerente(processo, mRequerente)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void excluiProcesso() {

        ProcessoBD processoBD = new ProcessoBD();
        if (processoBD.excluiProcesso(listProcesso.get(tblProcesso.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Processo excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            desabilitaCamposBotoesAnexo();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esse processo!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void excluiAnexoProcesso() throws Exception {
        ProcessoBD processoBD = new ProcessoBD();

        if (processoBD.excluiAnexosProcesso(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Anexo excluido com Sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            if (tblDetalheAnexo.getSelectedRow() != -1) {
                int indice = tblDetalheAnexo.getSelectedRow();
                //Path destination = Paths.get("\\\\192.168.12.240\\servidor\\arquivos\\" + listProcesso.get(indice).getNomeArquivo());//("C:\\diretorio\\"+nomeArquivo);
                //Path destination = Paths.get("C:\\Users\\jallisson\\Desktop\\Nova pasta (2)\\SISTEMA SEPLUMA\\SISTEMA SIP\\MYSQL\\SIP\\Projeto\\Sip\\dist\\arquivos\\" + listProcesso.get(indice).getNomeArquivo());//("C:\\diretorio\\"+nomeArquivo);
                Path destination = Paths.get("\\\\192.168.13.200\\sip\\arquivos\\" + listAnexosProcesso2.get(indice).getNomeArquivo());
                if (destination.toFile().exists()) {
                    destination.toFile().delete();
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe relacionamento com tabelas!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void enviaNotificacaoDenuncia(String mensagem, String token) {
        Sender sender = new Sender(apiKey);
        Message message = new Message.Builder()
                .addData("message", mensagem)
                //.addData("message","testando 123")
                //.addData("denuncia", getGson().toJson(denuncia, DetalheDenuncia.class))
                .build();
        try {
            Result result = sender.send(message, token, 3);

            System.out.println(result.getCanonicalRegistrationId());
            System.out.println(result.getErrorCodeName());
            System.out.println(result.getMessageId());
            //System.out.println(denuncia.getTokenGCM().substring(0, 10));

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
        //JOptionPane.showMessageDialog(this, "Notificação enviada com Sucesso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
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
            //info.put("icon", "/sip/imagem/ic_stat_name.png" );

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

    private void ModificaStatusDenuncia(Integer idDenuncia) {
        Denuncia denunciaStatus = new Denuncia();
        denunciaStatus.setId(idDenuncia);
        denunciaStatus.setStatusApp("Protocolado");

        DenunciaBD denunciaBD = new DenunciaBD();

        if (denunciaBD.alteraDenunciaStatusApp(denunciaStatus)) {
            JOptionPane.showMessageDialog(this, "Status Denúncia Modificado!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
            //executaTramitacaoDistribuicao();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao modificar Status ", "Denúncia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacao() throws SQLException, ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMes.setValue(jdcDataTramite.getDate());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        ProcessoBD processoBD = new ProcessoBD();
        listProcesso = processoBD.consultaProcesso(consultaDeOnde);
        lastId = processoBD.getLastId();
        buscaProcesso(lastId);

        String controleTrami = +lastId + " " + "EM AUTUAÇÃO"; //+ " " + new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime());

        tram.setUsuario(getUsuario());
        tram.setProcesso(getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("EM AUTUAÇÃO");
        tram.setParecer(" ");
        tram.setSetor("PROTOCOLO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setLaudoMzu(" ");
        tram.setObservacao(null);
        tram.setControle(controleTrami);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

            //String numProcessoTeste = getProcesso().getNumProcesso();
            //String numTokenTeste = getProcesso().getDenuncia().getToken();
            //String numTokenTeste = getProcesso().getDenuncia().getOrigem();
            if (jCBDenuncia.isSelected() && "App".equals(getProcesso().getDenuncia().getOrigem())) {
                //enviaNotificacaoDenuncia("Sua Denúncia foi protocolada com o numero: " + getProcesso().getNumProcesso(), getProcesso().getDenuncia().getToken());
                enviaNotificacaoDenunciaFcm(getProcesso().getDenuncia().getToken(),"Sua Denúncia foi protocolada com o numero: " + getProcesso().getNumProcesso());
                ModificaStatusDenuncia(getProcesso().getDenuncia().getId());
                //enviaNotificacaoDenuncia("Sua Denúncia foi protocolada numero \nsfsdfaasfasfasfsafsafdsafsdafsdafsdafasfsdafsdafsdafsdafsdafdasfsadf:\n", getProcesso().getDenuncia().getToken());
            }

            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
            limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoDistribuicao() throws ParseException {

        Tramitacao tram = new Tramitacao();
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        ftfMes.setValue(jdcDataTramite.getDate());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        int indice = tblProcesso.getSelectedRow();
        int idProcesso = listProcesso.get(indice).getId();
        String controleTrami = idProcesso + " " + "ENVIADO" + " " + " " + "PROTOCOLO" + " " + "DMA";

        tram.setUsuario(getUsuario());
        tram.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("ENVIADO");
        tram.setParecer(" ");
        tram.setSetor(" ");
        tram.setSetorOrigem("PROTOCOLO");
        tram.setSetorDestino("DMA");
        tram.setLaudoMzu(" ");
        tram.setObservacao(null);
        tram.setControle(controleTrami);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //alteraProcTramiDMA();
            //atualizaTabela();
            //esabilitaBotoes();
            //desabilitaCampos();
            //limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
      private void executaTramitacaoDistribuicaoDnReaberto() throws ParseException {

        Tramitacao tram = new Tramitacao();
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        ftfMes.setValue(jdcDataTramite.getDate());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        int indice = tblProcesso.getSelectedRow();
        int idProcesso = listProcesso.get(indice).getId();
        String controleTrami = idProcesso + " " + "REABERTO" + " " + " " + "PROTOCOLO" + " " + "DMA";

        tram.setUsuario(getUsuario());
        tram.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("REABERTO");
        tram.setParecer(" ");
        tram.setSetor(" ");
        tram.setSetorOrigem("PROTOCOLO");
        tram.setSetorDestino("DMA");
        tram.setLaudoMzu(" ");
        tram.setObservacao(null);
        tram.setControle(controleTrami);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //alteraProcTramiDMA();
            //atualizaTabela();
            //esabilitaBotoes();
            //desabilitaCampos();
            //limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    

    private void caixaAlta() {
        txtDescricaoAnexo.addKeyListener(new KeyListener() {
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

    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcData.setDate(hoje);

    }

    private void limpaTabelaAnexosProcesso() {
        int numeroLinhas = tableModelAnexoProcesso.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModelAnexoProcesso.removeRow(0);
        }
    }

    private void limpaCampos() {
        {
            txtNomeRequerenteAnterior.setText("");
            ftfNumero.setText("");
            requerente = null;
            txtIdRequerente.setText("");
            txtIdAtividade.setText("");
            txtNomeAtividade.setText("");
            txtIdDenuncia.setText("");
            txtNomeDenunciado.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
            ftfMes.setText("");
            cbTipo.setSelectedItem("Normal");
            txtArquivo.setText("");
            txtDescricaoAnexo.setText("");
            jCBOficio.setSelected(false);
            jCBDenuncia.setSelected(false);
        }
    }

    private void habilitaCampos() {
        jycAno.setEnabled(true);
        ftfNumero.setEditable(true);
        ftfNumero.setEditable(true);
        txtIdRequerente.setEnabled(true);

        txtIdAtividade.setEnabled(true);

        btnSelecionaRequerente.setEnabled(true);
        btnSelecionaAtividade.setEnabled(true);
        jdcData.setEnabled(true);
        cbTipo.setEnabled(true);

        //txtIdDenuncia.setEnabled(true);
        //btnSelecionaDenuncia.setEnabled(true);
        jCBDenuncia.setEnabled(true);
        jCBOficio.setEnabled(true);
    }

    private void desabilitaCampos() {
        jycAno.setEnabled(false);
        ftfNumero.setEditable(false);
        ftfNumero.setEditable(false);
        txtIdRequerente.setEnabled(false);
        txtIdAtividade.setEnabled(false);
        cbSolicitacao.setEnabled(false);

        btnSelecionaRequerente.setEnabled(false);
        btnSelecionaAtividade.setEnabled(false);
        jdcData.setEnabled(false);
        cbTipo.setEnabled(false);

        txtIdDenuncia.setEnabled(false);
        btnSelecionaDenuncia.setEnabled(false);

        jCBDenuncia.setEnabled(false);
        jCBOficio.setEnabled(false);

    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnAlterarReq.setEnabled(true);
        btnExcluir.setEnabled(true);
        btnSelecionaRequerente.setEnabled(true);
        btnSelecionaAnexo.setEnabled(false);
        btnLAbrirArquivo.setEnabled(true);
        btnExcluirAnexo2.setEnabled(true);
        //btnSegundaVia.setEnabled(false);
    }

    private void controlBotaoSalvarPAnexos() {
        if (listAnexosProcesso.isEmpty()) {
            btnSalvar.setEnabled(false);
        } else {
            btnSalvar.setEnabled(true);
        }
    }

    private void desabilaBotoesImportantes() {
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnTramitar.setEnabled(false);
        btnAlterarReq.setEnabled(false);
    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnAlterarReq.setEnabled(false);
        btnExcluir.setEnabled(false);
        //btnSegundaVia.setEnabled(true);

        btnLAbrirArquivo.setEnabled(false);
        btnExcluirAnexo2.setEnabled(false);
    }

    private void habilitaCamposBotoesAnexo() {

        btnIncluiAnexo.setEnabled(true);
        txtDescricaoAnexo.setEditable(true);

        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnAlterarReq.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnCancelar.setEnabled(true);
    }

    private void desabilitaCamposBotoesAnexo() {

        btnIncluiAnexo.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        txtDescricaoAnexo.setEditable(false);
    }

    private void selecionaProcessoTramitacao() {
        if (tblProcesso.getSelectedRow() != -1) {
            tramitacaoFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            tramitacaoFrame.requestFocusIdProcesso();
            this.dispose();
            tramitacaoFrame.toFront();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoEmissaoLicenca() {
        if (tblProcesso.getSelectedRow() != -1) {
            emissaoLicencaFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            emissaoLicencaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoAutorizacaoEventos() {
        if (tblProcesso.getSelectedRow() != -1) {
            autorizacaoEventosFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            autorizacaoEventosFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoDistribuicao() {
        if (tblProcesso.getSelectedRow() != -1) {
            distFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            distFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoAnalise() {
        if (tblProcesso.getSelectedRow() != -1) {
            analiseFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            analiseFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoJuridico() {
        if (tblProcesso.getSelectedRow() != -1) {
            juridicoFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            juridicoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoFiscalizacao() {
        if (tblProcesso.getSelectedRow() != -1) {
            fiscalizacaoFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            fiscalizacaoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoRelMovimento() {
        if (tblProcesso.getSelectedRow() != -1) {
            relMovimentoProcesso.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            relMovimentoProcesso.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaAnexosProcessoParecerAnalise() {
        parecerAnaliseFrame.setAnexosProcesso(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()));
        this.dispose();
        parecerAnaliseFrame.toFront();
    }

    private void selecionaAnexosProcessoParecerJuridico() {
        parecerJuridicoFrame.setAnexosProcesso(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()));
        this.dispose();
        parecerJuridicoFrame.toFront();
    }

    private void selecionaAnexosProcessoParecerFiscalizacao() {
        parecerFiscalizacaoFrame.setAnexosProcesso(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()));
        this.dispose();
        parecerFiscalizacaoFrame.toFront();
    }

    private void selecionaAnexosProcessoTramitacao() {
        tramitacaoFrame.setAnexosProcesso(listAnexosProcesso2.get(tblDetalheAnexo.getSelectedRow()));
        this.dispose();
        tramitacaoFrame.toFront();
    }

    private void selecionaProcessoMovimentoDma() {
        if (tblProcesso.getSelectedRow() != -1) {
            movimentoDmaFrame.setProcesso(listProcesso.get(tblProcesso.getSelectedRow()));
            this.dispose();
            movimentoDmaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecaoSituacao() {
        /*
        if (jRBUsoCapiao.isSelected()) {
        situacao = "Usucapião";
        } else {
        situacao = "Normal";
        }
        
        /*if (jRBSegundaVia.isSelected()) {
        tipo = "Segunda Via";
        } else if (jRBTransmutacao.isSelected()) {
        tipo = "Transmutacao";
        } else {
        tipo = "Normal";
        }*/
    }

    private void copiarServidor() {
        int max = listAnexosProcesso.size();
        //Percorre a lista
        for (int i = 0; i < max; i++) {
            Path source = Paths.get(listAnexosProcesso.get(i).getOrigemArquivo());
            Path destination = Paths.get("\\\\192.168.13.200\\sip\\arquivos\\" + listAnexosProcesso.get(i).getNomeArquivo());
            try {
                Files.copy(source, destination);
                //int indice = tblDetalheAnexo.getSelectedRow();
                //Path destino = Paths.get("\\\\192.168.13.200\\sip\\arquivos\\" + listAnexosProcesso2.get(indice).getNomeArquivo());
                //if (destino.toFile().exists()) {
                //    destino.toFile().delete();
                //}
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao copiar o arquivo para o servidor\nEntre em contato com o desenvolvedor do software! " + ex, "Aviso", JOptionPane.ERROR_MESSAGE);

            }
        }

        //copiar arquivo para servidor
        //Path source = Paths.get(caminhoArquivoCompleto);//("C:\\Temp\\source.txt");
        //Path destination = Paths.get("\\\\192.168.12.240\\servidor\\arquivos\\" + nomeArquivo1);//("C:\\diretorio\\"+nomeArquivo);
        //Path destination = Paths.get("\\\\192.168.13.200\\sip\\arquivos\\" + nomeArquivo);
        // Path destination = Paths.get("C:\\Users\\jallisson\\Desktop\\Nova pasta (2)\\SISTEMA SEPLUMA\\SISTEMA SIP\\MYSQL\\SIP\\Projeto\\Sip\\dist\\arquivos\\"+nomeArquivo);
    }

    private void MenuJtableDetalhe() {

        jMenuItemAlterar.setText(""
                + ""
                + ""
                + "");

        jMenuItemAlterar.addActionListener(
                new java.awt.event.ActionListener() {
            // Importe a classe java.awt.event.ActionEvent

            public void actionPerformed(ActionEvent e) {
                //int index = tblDetalheAnexo.getSelectedRow();
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
        if (tblDetalheAnexo.getSelectedRow() != -1) {

            //listCadastrosImobImovel = listImovel.get(tblDetalheImobiliario.getSelectedRow()).getCadastrosImobImovel();
            int indice = tblDetalheAnexo.getSelectedRow();
            try {

                String arquivo = listAnexosProcesso2.get(indice).getNomeArquivo();

                Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler " + "file:\\\\192.168.13.200\\sip\\arquivos\\" + arquivo);
                //java.awt.Desktop.getDesktop().open(new File("file:\\\\192.168.13.200\\\\sip\\\\arquivos\\\\" + arquivo));
                //java.awt.Desktop.getDesktop().open(new File("C:\\Users\\jallisson\\Desktop\\GPS 05-2017.pdf"));

            } catch (Exception erro) {
                JOptionPane.showMessageDialog(null, "nao existe arquivo." + erro);
                erro.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um anexo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /*
    private void configuraTipoAbreviado() {
        if (cbTipo.getSelectedItem() == "LICENÇA DE OPERAÇÃO" || cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE OPERAÇÃO" 
                || cbTipo.getSelectedItem() == "RENOVAÇÃO DE LICENÇA DE OPERAÇÃO") {
            cbTipoAbreviado.setSelectedItem("LO");
        } else if (cbTipo.getSelectedItem() == "LICENÇA PRÉVIA") {
            cbTipoAbreviado.setSelectedItem("LP");
        } else if (cbTipo.getSelectedItem() == "LICENÇA DE INSTALAÇÃO" || cbTipo.getSelectedItem() == "PRORROGAÇÃO DE LICENÇA DE INSTALAÇÃO") {
            cbTipoAbreviado.setSelectedItem("LI");
        } else if (cbTipo.getSelectedItem() == "LICENÇA ÚNICA" || cbTipo.getSelectedItem() == "RENOVAÇÃO DA LICENÇA ÚNICA") {
            cbTipoAbreviado.setSelectedItem("LU");
        } else if (cbTipo.getSelectedItem() == "LICENÇA AMBIENTAL DE REGULARIZAÇÃO") {
            cbTipoAbreviado.setSelectedItem("LAR");
        } else if (cbTipo.getSelectedItem() == "AUTORIZAÇÃO PARA EVENTOS") {
            cbTipoAbreviado.setSelectedItem("AE");
        } else if (cbTipo.getSelectedItem() == "VIABILIDADE AMBIENTAL") {
            cbTipoAbreviado.setSelectedItem("VA");
        } else if (cbTipo.getSelectedItem() == "DISPENSA OU INSEÇÃO DE LICENCIAMENTO AMBIENTAL") {
            cbTipoAbreviado.setSelectedItem("DILA");
        } else if (cbTipo.getSelectedItem() == "LICENÇA DE EXTRAÇÃO" || cbTipo.getSelectedItem() == "RENOVAÇÃO DE LICENÇA DE EXTRAÇÃO") {
            cbTipoAbreviado.setSelectedItem("LE");
        } else if (cbTipo.getSelectedItem() == "LAUDO") {
            cbTipoAbreviado.setSelectedItem("LAD");
        } else if (cbTipo.getSelectedItem() == "DENÚNCIA") {
            cbTipoAbreviado.setSelectedItem("DEN");
        } else if (cbTipo.getSelectedItem() == "LIMPEZA DE ÁREA") {
            cbTipoAbreviado.setSelectedItem("LZ");
        }
    }*/
    private void popularComboBoxTipoLicenca() {
        TipoLicencaBD tipoLicencaBD = new TipoLicencaBD();
        listResumoTriagem = tipoLicencaBD.consultaTipoLicenca();
        for (int i = 0; i < listResumoTriagem.size(); i++) {
            cbTipo.addItem(listResumoTriagem.get(i).getNome());
        }
    }

    public void buscaProcesso(int id) {
        ProcessoBD processoBD = new ProcessoBD();
        listProcesso = processoBD.consultaProcesso("ProcessoFrame");
        int binario = 0;
        try {
            int max = listProcesso.size();
            int id_busca = id;
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
                //txtIdProcesso.setText("");
                //txtNumeroProcesso.setText("");
                setProcesso(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setProcesso(null);
            //txtNumeroProcesso.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaRequerente() {
        RequerenteBD requerenteBD = new RequerenteBD();
        listRequerente = requerenteBD.consultaRequerente();
        int binario = 0;
        try {
            int max = listRequerente.size();
            int id_busca = Integer.parseInt(txtIdRequerente.getText());
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listRequerente.get(i).getId();
                if (listRequerente.get(i).getId() == id_busca) {
                    setRequerente(listRequerente.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdRequerente.setText("");
                txtNomeRequerente.setText("");
                requerente = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            requerente = null;
            txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaAtividade() {
        AtividadeBD atividadeBD = new AtividadeBD();
        listAtividade = atividadeBD.consultaAtividade();
        int binario = 0;
        try {
            int max = listAtividade.size();
            int id_busca = Integer.parseInt(txtIdAtividade.getText());
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listAtividade.get(i).getId();
                if (listAtividade.get(i).getId() == id_busca) {
                    setAtividade(listAtividade.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdAtividade.setText("");
                txtNomeAtividade.setText("");
                atividade = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            requerente = null;
            txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaDenuncia() {
        DenunciaBD denunciaBD = new DenunciaBD();
        listDenuncia = denunciaBD.consultaDenuncia();
        int binario = 0;
        try {
            int max = listDenuncia.size();
            int id_busca = Integer.parseInt(txtIdDenuncia.getText());
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listDenuncia.get(i).getId();
                if (listDenuncia.get(i).getId() == id_busca) {
                    setDenuncia(listDenuncia.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdDenuncia.setText("");
                txtNomeDenunciado.setText("");
                denuncia = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            denuncia = null;
            txtNomeDenunciado.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaRequerenteMudanca(int id) {
        RequerenteBD requerenteBD = new RequerenteBD();
        listRequerenteMudanca = requerenteBD.consultaRequerente();
        int binario = 0;
        try {
            int max = listRequerenteMudanca.size();
            int id_busca = id;
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listRequerenteMudanca.get(i).getId();
                if (listRequerenteMudanca.get(i).getId() == id_busca) {
                    setRequerenteMudanca(listRequerenteMudanca.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                //JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdRequerente.setText("");
                //txtNomeRequerente.setText("");
                requerenteMudanca = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            requerenteMudanca = null;
            //txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaUsuario(String idUsuario) {
        UsuarioBD usuarioBD = new UsuarioBD();
        listUsuario = usuarioBD.consultaUsuarioSimples();
        int binario = 0;
        try {
            int max = listUsuario.size();
            int id_busca = Integer.parseInt(idUsuario);
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
                //txtIdUsuario.setText("");
                //txtNomeUsuario.setText("");
                usuario = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            usuario = null;
            //txtNomeUsuario.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
     private void reabrirDenuncia() {
        int indice = tblProcesso.getSelectedRow();
        Denuncia denunciaStatus = new Denuncia();
        denunciaStatus.setId( listProcesso.get(indice).getDenuncia().getId());
        denunciaStatus.setStatusApp("Reaberto");

        DenunciaBD denunciaBD = new DenunciaBD();

        if (denunciaBD.alteraDenunciaStatusApp(denunciaStatus)) {
            try {
                executaTramitacaoDistribuicaoDnReaberto();
            } catch (ParseException ex) {
                Logger.getLogger(ProcessoFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(this, "Status Denúncia Modificado!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
            

            //executaTramitacaoDistribuicao();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao modificar Status ", "Denúncia", JOptionPane.ERROR_MESSAGE);
        }
        
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        bGVTipo = new javax.swing.ButtonGroup();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlClipe = new javax.swing.JLabel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        txtNomeRequerente = new javax.swing.JTextField();
        btnSelecionaRequerente = new javax.swing.JButton();
        txtIdRequerente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        ftfMesTramite = new javax.swing.JFormattedTextField();
        ftfAno = new javax.swing.JFormattedTextField();
        jPanel14 = new javax.swing.JPanel();
        cbTipo = new javax.swing.JComboBox();
        jPanel23 = new javax.swing.JPanel();
        txtNomeDenunciado = new javax.swing.JTextField();
        btnSelecionaDenuncia = new javax.swing.JButton();
        txtIdDenuncia = new javax.swing.JTextField();
        jLabel22 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jycAno = new com.toedter.calendar.JYearChooser();
        ftfNumero = new javax.swing.JFormattedTextField();
        jLabel6 = new javax.swing.JLabel();
        jCBOficio = new javax.swing.JCheckBox();
        jLabel7 = new javax.swing.JLabel();
        jCBDenuncia = new javax.swing.JCheckBox();
        cbSolicitacao = new javax.swing.JComboBox();
        jPanel21 = new javax.swing.JPanel();
        txtNomeRequerenteAnterior = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        txtNomeAtividade = new javax.swing.JTextField();
        btnSelecionaAtividade = new javax.swing.JButton();
        txtIdAtividade = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jPanel24 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblInserirAnexosProcesso = new javax.swing.JTable();
        btnIncluiAnexo = new javax.swing.JButton();
        btnExcluirAnexo = new javax.swing.JButton();
        txtArquivo = new javax.swing.JTextField();
        btnSelecionaAnexo = new javax.swing.JButton();
        btnLAbrirArquivo = new javax.swing.JButton();
        btnExcluirAnexo2 = new javax.swing.JButton();
        txtDescricaoAnexo = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        btnSelecionaProcesso = new javax.swing.JButton();
        jPanel25 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDetalheAnexo = new javax.swing.JTable();
        jLAviso = new javax.swing.JLabel();
        btnExcluiAnexos = new javax.swing.JButton();
        btnAlterarAnexo = new javax.swing.JButton();
        txtArquivoAlterado = new javax.swing.JTextField();
        btnLocalizarAnexos = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTARelatoOcorrencia = new javax.swing.JTextArea();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblProcesso = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnAlterarReq = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnTramitar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnImprimir1 = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Processo\n");
        setPreferredSize(new java.awt.Dimension(950, 680));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/processo_64x64.png"))); // NOI18N
        jLabel1.setText("Processo");
        jPanel1.add(jLabel1);

        jlClipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/clipe64x64.png"))); // NOI18N
        jPanel1.add(jlClipe);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jTabbedPane2.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                jTabbedPane2AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        jTabbedPane2.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jTabbedPane2FocusLost(evt);
            }
        });

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerente - Requerido MP"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        txtNomeRequerente.setEditable(false);
        txtNomeRequerente.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeRequerente.setEnabled(false);
        txtNomeRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeRequerenteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(txtNomeRequerente, gridBagConstraints);

        btnSelecionaRequerente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaRequerente.setEnabled(false);
        btnSelecionaRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaRequerenteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaRequerente, gridBagConstraints);

        txtIdRequerente.setEnabled(false);
        txtIdRequerente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdRequerenteFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdRequerente, gridBagConstraints);

        jLabel19.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel20, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data"));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        jLabel12.setText("Mes/Ano");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(jLabel12, gridBagConstraints);

        ftfMes.setEditable(false);
        ftfMes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MMMM' 'yy"))));
        ftfMes.setToolTipText("Data do Video");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(ftfMes, gridBagConstraints);

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

        jLabel20.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel13.add(jLabel20, gridBagConstraints);

        ftfMesTramite.setEditable(false);
        ftfMesTramite.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MMMM' 'yy"))));
        ftfMesTramite.setToolTipText("Data do Video");
        ftfMesTramite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfMesTramiteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(ftfMesTramite, gridBagConstraints);

        ftfAno.setEditable(false);
        ftfAno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("yyyy"))));
        ftfAno.setToolTipText("Data do Video");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel13.add(ftfAno, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo Licença"));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        cbTipo.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cbTipo.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbTipoFocusLost(evt);
            }
        });
        cbTipo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbTipoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbTipoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cbTipoMouseReleased(evt);
            }
        });
        cbTipo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoActionPerformed(evt);
            }
        });
        cbTipo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbTipoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel14.add(cbTipo, gridBagConstraints);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Denuncia"));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        txtNomeDenunciado.setEditable(false);
        txtNomeDenunciado.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeDenunciado.setEnabled(false);
        txtNomeDenunciado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeDenunciadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(txtNomeDenunciado, gridBagConstraints);

        btnSelecionaDenuncia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaDenuncia.setEnabled(false);
        btnSelecionaDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaDenunciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(btnSelecionaDenuncia, gridBagConstraints);

        txtIdDenuncia.setEnabled(false);
        txtIdDenuncia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdDenunciaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel23.add(txtIdDenuncia, gridBagConstraints);

        jLabel22.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(jLabel22, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel14.add(jPanel23, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel14, gridBagConstraints);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Processo"));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jycAno.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(jycAno, gridBagConstraints);

        ftfNumero.setEditable(false);
        ftfNumero.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("000000"))));
        ftfNumero.setToolTipText("Data do Video");
        ftfNumero.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ftfNumeroFocusLost(evt);
            }
        });
        ftfNumero.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfNumeroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(ftfNumero, gridBagConstraints);

        jLabel6.setText("Processo Numero");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel16.add(jLabel6, gridBagConstraints);

        jCBOficio.setText("Solicitação      ");
        jCBOficio.setEnabled(false);
        jCBOficio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBOficioActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel16.add(jCBOficio, gridBagConstraints);

        jLabel7.setText("Ano");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel16.add(jLabel7, gridBagConstraints);

        jCBDenuncia.setText("Denúncia        ");
        jCBDenuncia.setEnabled(false);
        jCBDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBDenunciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        jPanel16.add(jCBDenuncia, gridBagConstraints);

        cbSolicitacao.setFont(new java.awt.Font("Verdana", 1, 14)); // NOI18N
        cbSolicitacao.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "MP", "DNIT", "PGM" }));
        cbSolicitacao.setEnabled(false);
        cbSolicitacao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                cbSolicitacaoFocusLost(evt);
            }
        });
        cbSolicitacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbSolicitacaoMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cbSolicitacaoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                cbSolicitacaoMouseReleased(evt);
            }
        });
        cbSolicitacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSolicitacaoActionPerformed(evt);
            }
        });
        cbSolicitacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cbSolicitacaoKeyPressed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(cbSolicitacao, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel16, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Ultimo Requerente"));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        txtNomeRequerenteAnterior.setEditable(false);
        txtNomeRequerenteAnterior.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeRequerenteAnterior.setEnabled(false);
        txtNomeRequerenteAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeRequerenteAnteriorActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(txtNomeRequerenteAnterior, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Atividade"));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        txtNomeAtividade.setEditable(false);
        txtNomeAtividade.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeAtividade.setEnabled(false);
        txtNomeAtividade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeAtividadeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(txtNomeAtividade, gridBagConstraints);

        btnSelecionaAtividade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaAtividade.setEnabled(false);
        btnSelecionaAtividade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaAtividadeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(btnSelecionaAtividade, gridBagConstraints);

        txtIdAtividade.setEnabled(false);
        txtIdAtividade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdAtividadeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel22.add(txtIdAtividade, gridBagConstraints);

        jLabel21.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(jLabel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel22, gridBagConstraints);

        jTabbedPane2.addTab("Cadastro", jPanel2);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel6.setLayout(new java.awt.GridBagLayout());

        tblInserirAnexosProcesso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Descrição", "Nome Arquivo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Object.class
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
        jScrollPane2.setViewportView(tblInserirAnexosProcesso);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jScrollPane2, gridBagConstraints);

        btnIncluiAnexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/INCLUIR_16x16.png"))); // NOI18N
        btnIncluiAnexo.setText("Incluir");
        btnIncluiAnexo.setEnabled(false);
        btnIncluiAnexo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnIncluiAnexoMouseMoved(evt);
            }
        });
        btnIncluiAnexo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIncluiAnexoMouseExited(evt);
            }
        });
        btnIncluiAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnIncluiAnexoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(btnIncluiAnexo, gridBagConstraints);

        btnExcluirAnexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/excluir_16x16.png"))); // NOI18N
        btnExcluirAnexo.setText("Excluir Anexo");
        btnExcluirAnexo.setToolTipText("Excluir Uma Ocorrência");
        btnExcluirAnexo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnExcluirAnexoMouseMoved(evt);
            }
        });
        btnExcluirAnexo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnExcluirAnexoMouseExited(evt);
            }
        });
        btnExcluirAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirAnexoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(btnExcluirAnexo, gridBagConstraints);

        txtArquivo.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(txtArquivo, gridBagConstraints);

        btnSelecionaAnexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/ANEXAR_16x16.png"))); // NOI18N
        btnSelecionaAnexo.setText("Selecionar Anexo");
        btnSelecionaAnexo.setEnabled(false);
        btnSelecionaAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaAnexoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(btnSelecionaAnexo, gridBagConstraints);

        btnLAbrirArquivo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/abrir_16x16.png"))); // NOI18N
        btnLAbrirArquivo.setText("Abrir");
        btnLAbrirArquivo.setEnabled(false);
        btnLAbrirArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLAbrirArquivoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(btnLAbrirArquivo, gridBagConstraints);

        btnExcluirAnexo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/excluir_16x16.png"))); // NOI18N
        btnExcluirAnexo2.setText("Excluir Anexo");
        btnExcluirAnexo2.setEnabled(false);
        btnExcluirAnexo2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluirAnexo2ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(btnExcluirAnexo2, gridBagConstraints);

        txtDescricaoAnexo.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(txtDescricaoAnexo, gridBagConstraints);

        jLabel5.setText("Descrição Anexo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 10, 5, 5);
        jPanel6.add(jLabel5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(jPanel6, gridBagConstraints);

        btnSelecionaProcesso.setText("Selecionar Processo");
        btnSelecionaProcesso.setEnabled(false);
        btnSelecionaProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaProcessoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        jPanel24.add(btnSelecionaProcesso, gridBagConstraints);

        jTabbedPane2.addTab("Anexos", jPanel24);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel25.setLayout(new java.awt.GridBagLayout());

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        tblDetalheAnexo.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        tblDetalheAnexo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Descrição", "Arquivo", "Usuario", "Setor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDetalheAnexo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDetalheAnexoMouseClicked(evt);
            }
        });
        tblDetalheAnexo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDetalheAnexoKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblDetalheAnexo);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel8.add(jScrollPane3, gridBagConstraints);

        jLAviso.setBackground(new java.awt.Color(255, 255, 255));
        jLAviso.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        jLAviso.setForeground(new java.awt.Color(255, 0, 0));
        jLAviso.setText("Atenção ao Excluir o Anexo, pois a exclusão é irreversível e não tem volta.");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLAviso, gridBagConstraints);

        btnExcluiAnexos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/excluir_16x16.png"))); // NOI18N
        btnExcluiAnexos.setText("Excluir");
        btnExcluiAnexos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExcluiAnexosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(btnExcluiAnexos, gridBagConstraints);

        btnAlterarAnexo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/refresh_16x16.png"))); // NOI18N
        btnAlterarAnexo.setText("Alterar Anexo");
        btnAlterarAnexo.setToolTipText("Excluir Uma Ocorrência");
        btnAlterarAnexo.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                btnAlterarAnexoMouseMoved(evt);
            }
        });
        btnAlterarAnexo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnAlterarAnexoMouseExited(evt);
            }
        });
        btnAlterarAnexo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarAnexoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(btnAlterarAnexo, gridBagConstraints);

        txtArquivoAlterado.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(txtArquivoAlterado, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel25.add(jPanel8, gridBagConstraints);

        btnLocalizarAnexos.setText("Pesquisar Anexos");
        btnLocalizarAnexos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocalizarAnexosActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel25.add(btnLocalizarAnexos, gridBagConstraints);

        jTabbedPane2.addTab("Pesquisar Anexos", jPanel25);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Observação"));
        jPanel26.setLayout(new java.awt.GridBagLayout());

        jTARelatoOcorrencia.setColumns(20);
        jTARelatoOcorrencia.setRows(5);
        jTARelatoOcorrencia.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTARelatoOcorrencia);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jScrollPane4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jPanel26, gridBagConstraints);

        jTabbedPane2.addTab("Observação", jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane2, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Requerente Processo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltroProcesso.setToolTipText("INFORME OS QUATROS DIGITOS DO PROCESSO EXEMPLO PROCESSO 0755 E NÃO 755");
        txtFiltroProcesso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroProcessoMouseClicked(evt);
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
        gridBagConstraints = new java.awt.GridBagConstraints();
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
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(btnFiltrar, gridBagConstraints);

        tblProcesso.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblProcesso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Requerente -  Requerido - Denunciado ", "Tipo Licença", "Data", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblProcesso.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblProcesso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblProcessoMouseClicked(evt);
            }
        });
        tblProcesso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblProcessoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblProcesso);

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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
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

        btnAlterarReq.setText("Alterar Requerente");
        btnAlterarReq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAlterarReqActionPerformed(evt);
            }
        });
        jPanel4.add(btnAlterarReq);

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

        btnTramitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitar.setText("Tramitar DMA");
        btnTramitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarActionPerformed(evt);
            }
        });
        jPanel4.add(btnTramitar);

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/printer.png"))); // NOI18N
        btnImprimir.setText("Capa");
        btnImprimir.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnImprimirAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir);

        btnImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/note_book.png"))); // NOI18N
        btnImprimir1.setText("Reabrir Denuncia");
        btnImprimir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimir1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroProcesso.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        ProcessoBD cobradorBD = new ProcessoBD();
        if (cobradorBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            cbTipo.setSelectedItem("LICENÇA PRÉVIA");
            btnFiltrar.setEnabled(false);
            tblProcesso.setEnabled(false);
            limpaSelecaoTabela();
            ftfNumero.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            setaDataAtual();
            caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed

        switch (modo) {
            case Constantes.INSERT_MODE:
                try {
                    incluiProcesso();
                } catch (SQLException | ParseException ex) {
                    ex.printStackTrace();
                }
                tblProcesso.setEnabled(true);
                // txtNomeCliente.requestFocus();
                break;
            case Constantes.EDIT_MODE:
                alteraProcesso();
                break;
            case Constantes.EDIT_MODE_REQ:
                alteraProcessoRequerente();
                break;
            case Constantes.INCLUIR_ANEXOS:
                atualizaTabelaDetalheAnexo();
                inserirAnexosProcesso();
                break;
            case Constantes.INCLUIR_ANEXOS_EXTERNOS:
                atualizaTabelaDetalheAnexo();
                inserirAnexosProcesso();
                break;

            case Constantes.ALTERAR_ANEXO:
                AlterarAnexosProcesso();
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        desabilitaCamposBotoesAnexo();
        limpaCampos();
        limpaSelecaoTabela();
        tblProcesso.setEnabled(true);
        btnFiltrar.setEnabled(true);
        txtFiltroProcesso.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            txtArquivo.requestFocus();
            habilitaCampos();
            habilitaBotoes();
            txtIdRequerente.setEditable(false);
            btnSelecionaRequerente.setEnabled(false);
            ftfNumero.requestFocus();
            jycAno.requestFocus();
            modo = Constantes.EDIT_MODE;
            buscaRequerente();
            buscaAtividade();
            buscaUsuario(idUsuario);
            tblProcesso.setEnabled(false);
            txtFiltroProcesso.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do processo?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiProcesso();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseClicked
    }//GEN-LAST:event_txtFiltroProcessoMouseClicked

    private void txtFiltroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcessoActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoActionPerformed

    private void tblProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblProcessoMouseClicked
        if (evt.getClickCount() == 2) {

            switch (modoSeleciona) {
                case Constantes.TRAMITACAO_FRAME:
                    selecionaProcessoTramitacao();
                    dispose();
                    break;
                case Constantes.EMISSAOLICENCAFRAME:
                    selecionaProcessoEmissaoLicenca();
                    dispose();
                    break;
                case Constantes.AUTORIZACAOEVENTOS_FRAME:
                    selecionaProcessoAutorizacaoEventos();
                    dispose();
                    break;
                case Constantes.DISTRIBUICAO_FRAME:
                    selecionaProcessoDistribuicao();
                    dispose();
                    break;
                case Constantes.ANALISE_FRAME:
                    selecionaProcessoAnalise();
                    break;
                case Constantes.JURIDICO_FRAME:
                    selecionaProcessoJuridico();
                    break;
                case Constantes.FISCALIZACAO_FRAME:
                    selecionaProcessoFiscalizacao();
                    break;
                case Constantes.REL_MOVIMENTOPROCESSO:
                    selecionaProcessoRelMovimento();
                    break;
                case Constantes.MOVIMENTO_DMA_FRAME:
                    selecionaProcessoMovimentoDma();
                    break;
                default:
                    break;
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblProcessoMouseClicked

    private void tblProcessoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblProcessoKeyPressed
        switch (modoSeleciona) {
            case Constantes.TRAMITACAO_FRAME:
                selecionaProcessoTramitacao();
                dispose();
                break;
            case Constantes.EMISSAOLICENCAFRAME:
                selecionaProcessoEmissaoLicenca();
                dispose();
                break;
            case Constantes.DISTRIBUICAO_FRAME:
                selecionaProcessoDistribuicao();
                dispose();
                break;
            case Constantes.ANALISE_FRAME:
                selecionaProcessoAnalise();
                break;
            case Constantes.JURIDICO_FRAME:
                selecionaProcessoJuridico();
                break;
            case Constantes.FISCALIZACAO_FRAME:
                selecionaProcessoFiscalizacao();
                break;
            case Constantes.REL_MOVIMENTOPROCESSO:
                selecionaProcessoRelMovimento();

                break;
            case Constantes.MOVIMENTO_DMA_FRAME:
                selecionaProcessoMovimentoDma();
                break;
            default:
                break;
        }

        evt.consume();

    }//GEN-LAST:event_tblProcessoKeyPressed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        //atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void btnAlterarReqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarReqActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            txtIdRequerente.setEditable(true);
            btnSelecionaRequerente.setEnabled(true);
            txtNomeRequerenteAnterior.setText("");
            habilitaBotoes();
            modo = Constantes.EDIT_MODE_REQ;
            buscaRequerente();
            buscaUsuario(idUsuario);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarReqActionPerformed

    private void jTabbedPane2AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_jTabbedPane2AncestorAdded
    }//GEN-LAST:event_jTabbedPane2AncestorAdded

    private void jTabbedPane2FocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTabbedPane2FocusLost
    }//GEN-LAST:event_jTabbedPane2FocusLost

    private void btnExcluirAnexo2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirAnexo2ActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            buscaRequerente();
            buscaUsuario(idUsuario);
            int indice = tblProcesso.getSelectedRow();

            //Path source = Paths.get(listEmissaoTitulo.get(indice).getCaminhoArquivo());//("C:\\Temp\\source.txt");
            //Path destination = Paths.get("\\\\192.168.12.240\\servidor\\arquivos\\" + listProcesso.get(indice).getNomeArquivo());//("C:\\diretorio\\"+nomeArquivo);
            /*Path destination = Paths.get("C:\\Users\\jallisson\\Desktop\\Nova pasta (2)\\SISTEMA SEPLUMA\\SISTEMA SIP\\MYSQL\\SIP\\Projeto\\Sip\\dist\\arquivos\\" + listProcesso.get(indice).getNomeArquivo());//("C:\\diretorio\\"+nomeArquivo);
            
            if (destination.toFile().exists()) {
            destination.toFile().delete();
            }*/
            caminhoArquivo = null;
            nomeArquivo = null;
            alteraProcessoExluirAnexo();
            atualizaTabela();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }

        // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirAnexo2ActionPerformed

    private void btnLAbrirArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLAbrirArquivoActionPerformed
        /* if (tblProcesso.getSelectedRow() != -1) {
        int indice = tblProcesso.getSelectedRow();
        try {
        
        String caminho = listProcesso.get(indice).getCaminhoArquivo();
        
        java.awt.Desktop.getDesktop().open(new File(caminho));
        } catch (IOException ex) {
        JOptionPane.showMessageDialog(null, "nao existe arquivo.");
        }
        
        } else {
        JOptionPane.showMessageDialog(this, "Selecione um Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }*/
        // TODO add your handling code here:
}//GEN-LAST:event_btnLAbrirArquivoActionPerformed

    private void btnSelecionaAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAnexoActionPerformed

        JFileChooser buscaArquivo = new JFileChooser();

        buscaArquivo.showOpenDialog(this);

        origemArquivo = "" + buscaArquivo.getSelectedFile().getAbsoluteFile();
        //caminhoArquivo1 = "" + buscaArquivo.getSelectedFile().getParentFile();
        nomeArquivo = "" + buscaArquivo.getSelectedFile().getName();
        txtArquivo.setText(nomeArquivo);
        caminhoArquivo = "\\\\192.168.13.200\\sip\\arquivos\\" + nomeArquivo;
        // caminhoArquivo =  "C:\\Users\\jallisson\\Desktop\\Nova pasta (2)\\SISTEMA SEPLUMA\\SISTEMA SIP\\MYSQL\\SIP\\Projeto\\Sip\\dist\\arquivos\\"+nomeArquivo;
        //abrir arquivo
        //java.awt.Desktop.getDesktop().open( new File( caminhoArquivo ) );

    }//GEN-LAST:event_btnSelecionaAnexoActionPerformed

    private void txtNomeRequerenteAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteAnteriorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeRequerenteAnteriorActionPerformed

    private void ftfNumeroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ftfNumeroFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfNumeroFocusLost

    private void ftfNumeroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfNumeroActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfNumeroActionPerformed

    private void cbTipoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbTipoKeyPressed
        // if(evt.getKeyCode() == KeyEvent.VK_ENTER)
        // configuraStatusParecer();// TODO add your handling code here:
    }//GEN-LAST:event_cbTipoKeyPressed

    private void cbTipoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbTipoFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_cbTipoFocusLost

    private void cbTipoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoActionPerformed
        //configuraTipoAbreviado();        // exibeSegundaVia(); // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoActionPerformed

    private void cbTipoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbTipoMouseReleased
        // configuraStatusParecer(); // TODO add your handling code here:
}//GEN-LAST:event_cbTipoMouseReleased

    private void cbTipoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbTipoMouseClicked
        if (evt.getClickCount() == 1) {
        }
        // TODO add your handling code here:
}//GEN-LAST:event_cbTipoMouseClicked

    private void cbTipoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbTipoMousePressed
        if (evt.getClickCount() == 1) {
            //exibeSegundaVia();
        } // TODO add your handling code here:
    }//GEN-LAST:event_cbTipoMousePressed

    private void txtIdRequerenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdRequerenteFocusLost
        buscaRequerente();        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRequerenteFocusLost

    private void btnSelecionaRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaRequerenteActionPerformed
        RequerenteFrame requerenteFrame = new RequerenteFrame(this);
        requerenteFrame.setVisible(true);
        this.getDesktopPane().add(requerenteFrame);
        requerenteFrame.toFront();
        centralizaJanelaRegVen(requerenteFrame);             // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaRequerenteActionPerformed

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void txtNomeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void btnIncluiAnexoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIncluiAnexoMouseExited
        this.btnIncluiAnexo.setBackground(Constantes.CINZA_CLARO);// TODO add your handling code here:
    }//GEN-LAST:event_btnIncluiAnexoMouseExited

    private void btnIncluiAnexoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnIncluiAnexoMouseMoved
        this.btnIncluiAnexo.setBackground(Color.RED);// TODO add your handling code here:
    }//GEN-LAST:event_btnIncluiAnexoMouseMoved

    private void btnIncluiAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnIncluiAnexoActionPerformed
        incluiVariosAnexosProcesso();
        controlBotaoSalvarPAnexos();
        /*if (modoSeleciona == Constantes.ANALISE_FRAME) {
            
        }*/
        // TODO add your handling code here:
    }//GEN-LAST:event_btnIncluiAnexoActionPerformed

    private void btnExcluirAnexoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirAnexoMouseExited
        this.btnExcluirAnexo.setBackground(Constantes.CINZA_CLARO);// TODO add your handling code here:
}//GEN-LAST:event_btnExcluirAnexoMouseExited

    private void btnExcluirAnexoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirAnexoMouseMoved
        this.btnExcluirAnexo.setBackground(Color.red); // TODO add your handling code here:
    }//GEN-LAST:event_btnExcluirAnexoMouseMoved

    private void btnExcluirAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirAnexoActionPerformed
        excluiAnexoTabelaInserir();

        controlBotaoSalvarPAnexos();

    }//GEN-LAST:event_btnExcluirAnexoActionPerformed

    private void btnSelecionaProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            habilitaCamposBotoesAnexo();
            tblProcesso.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Processo Selecionado!", "Processo", JOptionPane.INFORMATION_MESSAGE);
            modo = Constantes.INCLUIR_ANEXOS;
            btnSalvar.setEnabled(true);
            btnSelecionaAnexo.setEnabled(true);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaProcessoActionPerformed

    private void tblDetalheAnexoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDetalheAnexoMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) { // Clique com botao direito do mouse.

            jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            //JOptionPane.showMessageDialog(this, "Processo Selecionado!", "Processo", JOptionPane.INFORMATION_MESSAGE);
            //instanciei meu novo formulario
        }
        if (evt.getClickCount() == 2) {

            switch (modoSeleciona) {
                case Constantes.PARECERANALISE_FRAME:
                    selecionaAnexosProcessoParecerAnalise();
                    dispose();
                    break;
                case Constantes.PARECERJURIDICO_FRAME:
                    selecionaAnexosProcessoParecerJuridico();
                    dispose();
                    break;
                case Constantes.TRAMITACAO_FRAME_ANEXO:
                    selecionaAnexosProcessoTramitacao();
                    dispose();
                    break;
                case Constantes.PARECERFISCALIZACAO_FRAME:
                    selecionaAnexosProcessoParecerFiscalizacao();
                    dispose();
                    break;
                default:
                    break;
            }

            evt.consume();
        }
        /*
        if (evt.getClickCount() == 1) {
        jLAviso.setVisible(true);
        try {
        //abrir();
        obterValorSelecao();
        } catch (IOException ex) {
        ex.printStackTrace();
        }
        
        
        }*/// TODO add your handling code here:
    }//GEN-LAST:event_tblDetalheAnexoMouseClicked

    private void tblDetalheAnexoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDetalheAnexoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            jLAviso.setVisible(true);
            try {
                obterValorSelecao();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_tblDetalheAnexoKeyPressed

    private void btnExcluiAnexosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluiAnexosActionPerformed
        if (tblDetalheAnexo.getSelectedRow() != -1) {

            int resposta = JOptionPane.showConfirmDialog(this, "Confirma a exclusão do Anexo?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                // listaItensAtendimento.remove(tblDetalheAtendimento.getSelectedRow());
                try {
                    excluiAnexoProcesso();
                    atualizaTabela();
                    jLAviso.setVisible(false);
                } catch (Exception ex) {
                    Logger.getLogger(ProcessoFrame.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um anexo da lista!", "Agente", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluiAnexosActionPerformed

    private void btnLocalizarAnexosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocalizarAnexosActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            atualizaTabelaDetalheAnexo();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Anexo!", "Anexo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnLocalizarAnexosActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {

            executaRelatorioCapa();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo na tabela!", "Emissão", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
}//GEN-LAST:event_btnImprimirActionPerformed

    private void btnTramitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            int indice = tblProcesso.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                if ("NAO".equals(listProcesso.get(indice).getArquivado())) {
                    alteraProcTramiDMA();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a Process!", "Process", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void txtNomeAtividadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAtividadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeAtividadeActionPerformed

    private void btnSelecionaAtividadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAtividadeActionPerformed
        AtividadeFrame1 atividadeFrame = new AtividadeFrame1(this);
        atividadeFrame.setVisible(true);
        this.getDesktopPane().add(atividadeFrame);
        atividadeFrame.toFront();
        centralizaJanelaRegVen(atividadeFrame);// TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaAtividadeActionPerformed

    private void txtIdAtividadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdAtividadeFocusLost
        buscaAtividade();  // TODO add your handling code here:
    }//GEN-LAST:event_txtIdAtividadeFocusLost

    private void btnImprimirAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnImprimirAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimirAncestorAdded

    private void btnAlterarAnexoMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarAnexoMouseMoved
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarAnexoMouseMoved

    private void btnAlterarAnexoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAlterarAnexoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarAnexoMouseExited

    private void btnAlterarAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarAnexoActionPerformed
        if (tblDetalheAnexo.getSelectedRow() != -1) {
            JOptionPane.showMessageDialog(this, "Anexo Selecionado!", "Processo", JOptionPane.INFORMATION_MESSAGE);
            btnAlterarAnexo.setEnabled(false);
            tblDetalheAnexo.setEnabled(false);
            modo = Constantes.ALTERAR_ANEXO;
            btnSalvar.setEnabled(true);

            JFileChooser buscaArquivo = new JFileChooser();

            buscaArquivo.showOpenDialog(this);

            origemArquivo = "" + buscaArquivo.getSelectedFile().getAbsoluteFile();
            //caminhoArquivo1 = "" + buscaArquivo.getSelectedFile().getParentFile();
            nomeArquivo = "" + buscaArquivo.getSelectedFile().getName();
            txtArquivoAlterado.setText(nomeArquivo);
            caminhoArquivo = "\\\\192.168.13.200\\sip\\arquivos\\" + nomeArquivo;
            // caminhoArquivo =  "C:\\Users\\jallisson\\Desktop\\Nova pasta (2)\\SISTEMA SEPLUMA\\SISTEMA SIP\\MYSQL\\SIP\\Projeto\\Sip\\dist\\arquivos\\"+nomeArquivo;
            //abrir arquivo
            //java.awt.Desktop.getDesktop().open( new File( caminhoArquivo ) );

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um anexo da lista!", "Anexo", JOptionPane.INFORMATION_MESSAGE);
        }  // TODO add your handling code here:
    }//GEN-LAST:event_btnAlterarAnexoActionPerformed

    private void jCBDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBDenunciaActionPerformed
        configuraDenuncia();        // TODO add your handling code here:
    }//GEN-LAST:event_jCBDenunciaActionPerformed

    private void jCBOficioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBOficioActionPerformed
        configuraDenuncia();
        configuraSolicitacao();// TODO add your handling code here:
    }//GEN-LAST:event_jCBOficioActionPerformed

    private void txtNomeDenunciadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeDenunciadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeDenunciadoActionPerformed

    private void btnSelecionaDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaDenunciaActionPerformed
        DenunciaFrame denunciaFrame = new DenunciaFrame(this);
        denunciaFrame.setVisible(true);
        this.getDesktopPane().add(denunciaFrame);
        denunciaFrame.toFront();
        centralizaJanelaRegVen(denunciaFrame);          // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaDenunciaActionPerformed

    private void txtIdDenunciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdDenunciaFocusLost
        buscaDenuncia();        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdDenunciaFocusLost

    private void cbSolicitacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbSolicitacaoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoMouseClicked

    private void cbSolicitacaoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbSolicitacaoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoMousePressed

    private void cbSolicitacaoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbSolicitacaoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoMouseReleased

    private void cbSolicitacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbSolicitacaoActionPerformed
        configuraSolicitacao();        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoActionPerformed

    private void cbSolicitacaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_cbSolicitacaoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoFocusLost

    private void cbSolicitacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cbSolicitacaoKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbSolicitacaoKeyPressed

    private void btnImprimir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir1ActionPerformed
        if (tblProcesso.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma a reabertura da Denuncia?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                reabrirDenuncia();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ocorrencia da lista!", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGVTipo;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnAlterarAnexo;
    private javax.swing.JButton btnAlterarReq;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluiAnexos;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnExcluirAnexo;
    private javax.swing.JButton btnExcluirAnexo2;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnImprimir1;
    private javax.swing.JButton btnIncluiAnexo;
    private javax.swing.JButton btnLAbrirArquivo;
    private javax.swing.JButton btnLocalizarAnexos;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaAnexo;
    private javax.swing.JButton btnSelecionaAtividade;
    private javax.swing.JButton btnSelecionaDenuncia;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JButton btnSelecionaRequerente;
    private javax.swing.JButton btnTramitar;
    private javax.swing.JComboBox cbSolicitacao;
    private javax.swing.JComboBox cbTipo;
    private javax.swing.JFormattedTextField ftfAno;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JFormattedTextField ftfNumero;
    private javax.swing.JCheckBox jCBDenuncia;
    private javax.swing.JCheckBox jCBOficio;
    private javax.swing.JLabel jLAviso;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextArea jTARelatoOcorrencia;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JLabel jlClipe;
    private com.toedter.calendar.JYearChooser jycAno;
    private javax.swing.JTable tblDetalheAnexo;
    private javax.swing.JTable tblInserirAnexosProcesso;
    private javax.swing.JTable tblProcesso;
    private javax.swing.JTextField txtArquivo;
    private javax.swing.JTextField txtArquivoAlterado;
    private javax.swing.JTextField txtDescricaoAnexo;
    public javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtIdAtividade;
    private javax.swing.JTextField txtIdDenuncia;
    private javax.swing.JTextField txtIdRequerente;
    private javax.swing.JTextField txtNomeAtividade;
    private javax.swing.JTextField txtNomeDenunciado;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNomeRequerenteAnterior;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the requerente
     */
    public Requerente getRequerente() {
        return requerente;
    }

    /**
     * @param requerente the requerente to set
     */
    public void setRequerente(Requerente requerente) {
        this.requerente = requerente;
        txtNomeRequerente.setText(requerente.getNome());
        txtIdRequerente.setText(requerente.getId().toString());
    }

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
        //txtNomeUsuario.setText(usuario.getNome());
        //txtIdUsuario.setText(usuario.getId().toString());
    }

    /**
     * @return the processo
     */
    public Processo getProcesso() {
        return processos;
    }

    /**
     * @param processo the processo to set
     */
    public void setProcesso(Processo processo) {
        this.processos = processo;
    }

    /**
     * @return the requerenteMudanca
     */
    public Requerente getRequerenteMudanca() {
        return requerenteMudanca;
    }

    /**
     * @param requerenteMudanca the requerenteMudanca to set
     */
    public void setRequerenteMudanca(Requerente requerenteMudanca) {
        this.requerenteMudanca = requerenteMudanca;
    }

    public Usuario getUsuarioAnexos() {
        return usuarioAnexos;
    }

    /**
     * @return the atividade
     */
    public Atividade getAtividade() {
        return atividade;
    }

    /**
     * @param atividade the atividade to set
     */
    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
        txtNomeAtividade.setText(atividade.getNome());
        txtIdAtividade.setText(atividade.getId().toString());
    }

    /**
     * @return the denuncia
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param denuncia the denuncia to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
        if (denuncia.getPDenunciado() == null) {
            txtNomeDenunciado.setText(denuncia.getDenunciado());
        } else {
            txtNomeDenunciado.setText(denuncia.getPDenunciado().getNome());
        }

        txtIdDenuncia.setText(denuncia.getId().toString());
        ftfNumero.setValue(denuncia.getId());
    }
}
