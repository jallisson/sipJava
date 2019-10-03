/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.distribuicao;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.Constantes;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.tramitacao.TramitacaoFrame;
import sip.emissaolicenca.EmissaoLicencaFrame;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONObject;
import sip.analise.AnaliseFrame;
import sip.analista.Analista;
import sip.analista.AnalistaBD;
import sip.analista.AnalistaFrame;
import sip.denuncia.Denuncia;
import sip.denuncia.DenunciaBD;
import sip.fiscalizacao.FiscalizacaoFrame;
import sip.juridico.JuridicoFrame;
import sip.processo.Processo;
import sip.processo.ProcessoBD;
import sip.processo.ProcessoFrame;
import sip.util.ColorirTabelaDistribuicao;
import sip.util.ColorirTabelaTramitacao;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class DistribuicaoFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private JDateChooser jdcDataMudanca;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Distribuicao> listDistribuicao;
    private Distribuicao distribuicao;
    private List<Processo> listProcesso;
    private Processo processo;
    private int modo;
    private int modoSeleciona;
    private TramitacaoFrame tramitacaoFrame;
    private Analista analista;
    private List<Analista> listAnalista;
    private Usuario usuario;
    private List<Usuario> listUsuario;
    private EmissaoLicencaFrame emissaoTituloFrame;
    private AnaliseFrame analiseFrame;
    private JuridicoFrame juridicoFrame;
    private FiscalizacaoFrame fiscalizacaoFrame;
    //private FiscalizacaoFrame fiscalizacaoFrame;
    String idUsuario = Menu.id_Usuario;
    String tipo = "Normal";
    String situacao = "Normal";
    private String id;
    private String consultaDeOnde = "DistribuicaoFrame";
    private String tramitaParaOnde;
    private final String apiKey = "AIzaSyAoQh5qojWNnm_Tk5fAl8tsnFueD6mG-AE";

    public DistribuicaoFrame() {
        initComponents();
        buscaUsuario(idUsuario);
        defineModelo();
        formataData();
        caixaAlta();
        fechando();

        jdcData.setEnabled(false);
    }

    public DistribuicaoFrame(TramitacaoFrame tramitacaoFrame) {
        initComponents();
        buscaUsuario(idUsuario);
        defineModelo();
        formataData();
        caixaAlta();
        this.tramitacaoFrame = tramitacaoFrame;
        modoSeleciona = Constantes.ALTERAR;

        jdcData.setEnabled(false);
        txtFiltroProcesso.requestFocus();
        //jRBNenhum.setVisible(false);

    }

    public DistribuicaoFrame(EmissaoLicencaFrame emissao) {
        initComponents();
        buscaUsuario(idUsuario);
        defineModelo();
        formataData();
        caixaAlta();
        fechando();

        jdcData.setEnabled(false);
        txtFiltroProcesso.requestFocus();
        this.emissaoTituloFrame = emissao;
        modoSeleciona = Constantes.EMISSAOLICENCAFRAME;
        //jRBNenhum.setVisible(false);
    }

    public DistribuicaoFrame(JuridicoFrame juridicoFrame) {
        initComponents();
        buscaUsuario(idUsuario);
        defineModelo();
        formataData();
        caixaAlta();
        fechando();

        jPanel20.setVisible(false);
        jPanel13.setVisible(false);
        jPanel23.setVisible(false);
        jPanel21.setVisible(false);
        jPanel4.setVisible(false);
        jdcData.setEnabled(false);
        this.juridicoFrame = juridicoFrame;
        modoSeleciona = Constantes.JURIDICO_FRAME;
        consultaDeOnde = "JuridicoFrame";
        //jRBNenhum.setVisible(false); 
    }

    public DistribuicaoFrame(AnaliseFrame analiseFrame) {
        initComponents();
        buscaUsuario(idUsuario);
        defineModelo();
        formataData();
        caixaAlta();
        fechando();

        jPanel20.setVisible(false);
        jPanel13.setVisible(false);
        jPanel23.setVisible(false);
        jPanel21.setVisible(false);
        jPanel4.setVisible(false);
        jdcData.setEnabled(false);
        this.analiseFrame = analiseFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        consultaDeOnde = "AnaliseFrame";
        //jRBNenhum.setVisible(false); 
    }

    public DistribuicaoFrame(FiscalizacaoFrame fiscalizacaoFrame) {
        initComponents();
        buscaUsuario(idUsuario);
        modoSeleciona = Constantes.FISCALIZACAO_FRAME;
        defineModelo();
        formataData();
        caixaAlta();
        fechando();

        jPanel20.setVisible(false);
        jPanel13.setVisible(false);
        jPanel23.setVisible(false);
        jPanel21.setVisible(false);
        jPanel4.setVisible(false);
        jdcData.setEnabled(false);
        this.fiscalizacaoFrame = fiscalizacaoFrame;

        consultaDeOnde = "FiscalizacaoFrame";
        //jRBNenhum.setVisible(false); 
    }

    /*
     public DistribuicaoFrame(FiscalizacaoFrame fiscalizacaoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idUsuario);

        jPanel20.setVisible(false);
        jPanel13.setVisible(false);
        jPanel23.setVisible(false);
        jPanel21.setVisible(false);
        jPanel4.setVisible(false);
        jdcData.setEnabled(false);
        this.fiscalizacaoFrame = fiscalizacaoFrame;
        modoSeleciona = Constantes.FISCALIZACAO_FRAME;
        consultaDeOnde = "DistribuicaoFrame";
        //jRBNenhum.setVisible(false); 
    }*/

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
            if (tam.length() == 1) {
                String valor = txtFiltroProcesso.getText();
                txtFiltroProcesso.setText("000" + valor);
            } else if (tam.length() == 2) {
                String valor = txtFiltroProcesso.getText();
                txtFiltroProcesso.setText("00" + valor);
            } else if (tam.length() == 3) {
                String valor = txtFiltroProcesso.getText();
                txtFiltroProcesso.setText("0" + valor);
            }
        }
    }

    private void defineModelo() {

        tableModel = (DefaultTableModel) tblDistribuicao.getModel();
        listModel = tblDistribuicao.getSelectionModel();
        String teste = getUsuario().getSetor();
        int testes = modoSeleciona;
        if ("FISCALIZAÇÃO".equals(getUsuario().getSetor()) && modoSeleciona == Constantes.FISCALIZACAO_FRAME) {
            tblDistribuicao.setDefaultRenderer(Object.class, new ColorirTabelaDistribuicao());

            TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblDistribuicao.getModel());
            tblDistribuicao.setRowSorter(sorter);

            List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
            //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
            //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
            sorter.setSortKeys(sortKeys);
        }

        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheDistribuicao();
                    // mostraDetalheReqAnterior();

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

            tblDistribuicao.getColumnModel().getColumn(0).setMaxWidth(0);
            tblDistribuicao.getColumnModel().getColumn(0).setMinWidth(0);
            tblDistribuicao.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            tblDistribuicao.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

            //tblDistribuicao.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblDistribuicao.getColumnModel().getColumn(1).setPreferredWidth(130);
            tblDistribuicao.getColumnModel().getColumn(2).setPreferredWidth(350);
            tblDistribuicao.getColumnModel().getColumn(3).setPreferredWidth(350);
            tblDistribuicao.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblDistribuicao.getColumnModel().getColumn(5).setPreferredWidth(130);
            tblDistribuicao.getColumnModel().getColumn(6).setPreferredWidth(100);
            tblDistribuicao.getColumnModel().getColumn(7).setPreferredWidth(70);

        } catch (ParseException ex) {
            Logger.getLogger(DistribuicaoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void centralizaJanelaRegVen(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 36,
                (this.getHeight() - internalFrame.getHeight()) / 12);
    }

    private void atualizaTabela() {
        DistribuicaoBD distribuicaoBD = new DistribuicaoBD();
        inserirZeroEsqPesq();
        String nome = null;
        if (txtFiltroProcesso.getText().equals("")) {
            if ("FiscalizacaoFrame".equals(consultaDeOnde)) {
                listDistribuicao = distribuicaoBD.consultaDistribuicao(consultaDeOnde, getUsuario().getId());
            } else {
                listDistribuicao = distribuicaoBD.consultaDistribuicao(consultaDeOnde, 0);
            }

        } else {
            String valor = txtFiltroProcesso.getText();
            listDistribuicao = distribuicaoBD.consultaProcessoNum(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listDistribuicao.size(); i++) {
            if (listDistribuicao.get(i).getRequerente().getNome() == null) {
                nome = listDistribuicao.get(i).getPessoa().getNome();
            } else {
                nome = listDistribuicao.get(i).getRequerente().getNome();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listDistribuicao.get(i).getId(),
                listDistribuicao.get(i).getProcesso().getNumProcesso(),
                nome,
                listDistribuicao.get(i).getProcesso().getTipoLicenca(),
                listDistribuicao.get(i).getAnalista().getNome(),
                simpleDateFormat.format(listDistribuicao.get(i).getDataDist()),
                //listDistribuicao.get(i).getAnalisado(),
                listDistribuicao.get(i).getUsuario().getNome(),
                listDistribuicao.get(i).getStatus()
            });
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheDistribuicao() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            int indice = tblDistribuicao.getSelectedRow();

            txtIdProcesso.setText(listDistribuicao.get(indice).getProcesso().getId().toString());
            txtNumeroProcesso.setText(listDistribuicao.get(indice).getProcesso().getNumProcesso());
            if (listDistribuicao.get(indice).getRequerente().getNome() == null) {
                txtNomeRequerente.setText(listDistribuicao.get(indice).getPessoa().getNome());
            } else {
                txtNomeRequerente.setText(listDistribuicao.get(indice).getRequerente().getNome());
            }

            txtNomeAnalista.setText(listDistribuicao.get(indice).getAnalista().getNome());
            txtIdAnalista.setText(listDistribuicao.get(indice).getAnalista().getId().toString());
            txtNomeAnalista.setText(listDistribuicao.get(indice).getAnalista().getNome());
            jdcData.setDate(listDistribuicao.get(indice).getDataDist());
            ftfMes.setText(listDistribuicao.get(indice).getMesAno());
            //txtQtdProcesso.setText(listDistribuicao.get(indice).getAnalista().getSaldo().toString());
            if ("SIM".equals(listDistribuicao.get(indice).getTramitouAnalise())) {
                btnTramitarAnalise.setEnabled(false);

            } else {
                btnTramitarAnalise.setEnabled(true);
            }
            if ("SIM".equals(listDistribuicao.get(indice).getTramitouJuridico())) {
                btnTramitarJuridico.setEnabled(false);

            } else {
                btnTramitarJuridico.setEnabled(true);
            }
            if ("SIM".equals(listDistribuicao.get(indice).getTramitouFiscalizacao())) {
                btnTramitarFiscalização.setEnabled(false);

            } else {
                btnTramitarFiscalização.setEnabled(true);
            }

        } else {

            //txtIdAnalista.setText("");
            //txtNomeAnalista.setText("");
            //jdcData.setDate(null);
            //ftfMes.setText("");
        }
    }
//método descontinuado
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
            info.put("color", "blue");

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
        Denuncia denuncia = new Denuncia();
        denuncia.setId(idDenuncia);
        denuncia.setStatusApp("Enviado Fiscal");

        DenunciaBD denunciaBD = new DenunciaBD();

        if (denunciaBD.alteraDenunciaStatusApp(denuncia)) {
            JOptionPane.showMessageDialog(this, "Status Denúncia Modificado!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
            //executaTramitacaoDistribuicao();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao modificar Status ", "Denúncia", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alteraTramitouAnalise() {
        Distribuicao distri = new Distribuicao();
        DistribuicaoBD distribuicaoBD = new DistribuicaoBD();

        distri.setId(listDistribuicao.get(tblDistribuicao.getSelectedRow()).getId());
        distri.setTramitouAnalise("SIM");

        if (distribuicaoBD.alteraDistTramiAnalise(distri)) {
            //JOptionPane.showMessageDialog(this, "Pr!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);                    
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void somaEntradaAnalista() {
        int qtdeEntrada = 1;
        int indice = tblDistribuicao.getSelectedRow();
        Analista analist = new Analista();

        analist.setId(listDistribuicao.get(indice).getAnalista().getId());
        analist.setQtdeEntrada(listDistribuicao.get(indice).getAnalista().getQtdeEntrada() + qtdeEntrada);

        AnalistaBD analistaBD = new AnalistaBD();
        if (analistaBD.alteraAnalistaEntrada(analist)) {
            JOptionPane.showMessageDialog(this, "Entrada para o Analista OK!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            try {
                if ("NAO".equals(listDistribuicao.get(indice).getProcesso().getArquivado())) {
                    executaTramitacaoDmaDestino();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.ERROR_MESSAGE);
                }

            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            switch (tramitaParaOnde) {
                case "analise":
                    alteraTramitouAnalise();
                    break;
            }
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro enviar dados de entrada para o analista!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void subtraiEntradaAnalista() {
        int indice = tblDistribuicao.getSelectedRow();
        int qtdeEntrada = 1;

        Analista analist = new Analista();
        analist.setId(listDistribuicao.get(indice).getAnalista().getId());
        analist.setQtdeEntrada(listDistribuicao.get(indice).getAnalista().getQtdeEntrada() - qtdeEntrada);

        AnalistaBD analistaBD = new AnalistaBD();
        if (analistaBD.alteraAnalistaEntrada(analist)) {
            JOptionPane.showMessageDialog(this, "Deu certo Analista!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro enviar dado analista!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incluiDistribuicao() {
        if (getAnalista() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um analista!", "Analista", JOptionPane.INFORMATION_MESSAGE);

        } else if (getProcesso() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Analista", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if ("SIM".equals(getProcesso().getArquivado())) {
            JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
        } else {
            Distribuicao distri = new Distribuicao();
            String controle;
            ftfMes.setValue(jdcData.getDate());
            controle = processo.getId() + " " + analista.getId() + " " + new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime());

            //selecaoSituacao();
            distri.setUsuario(getUsuario());
            distri.setAnalista(getAnalista());
            distri.setProcesso(getProcesso());
            distri.setDataDist(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            distri.setMesAno(ftfMes.getText());
            //distri.setAnalisado("NAO");
            distri.setControle(controle);
            distri.setTramitouAnalise("NAO");
            distri.setTramitouJuridico("NAO");
            distri.setTramitouFiscalizacao("NAO");
            alteraProcessoLancadoDma();
            DistribuicaoBD distribuicaoBD = new DistribuicaoBD();
            if (distribuicaoBD.incluiDistribuicao(distri)) {
                JOptionPane.showMessageDialog(this, "Distribuicao cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                //somaEntradaAnalista();
                atualizaTabela();
                try {
                    if ("NAO".equals(getProcesso().getArquivado())) {
                        executaTramitacao();
                    } else {
                        JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.ERROR_MESSAGE);
                    }

                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();

            } else {
                JOptionPane.showMessageDialog(this, "Distribuicao já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    private void alteraProcessoLancadoDma() {

        if (getProcesso() != null) {

            processo.setId(getProcesso().getId());

            processo.setLancadoDma("SIM");

            ProcessoBD processoBD = new ProcessoBD();

            if (processoBD.alteraProcLancadoDMA(processo)) {

                //JOptionPane.showMessageDialog(this, "Processo ", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Ao baixar no processo!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void alteraDistribuicao() {

        if (getAnalista() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o requerente!", "Campos", JOptionPane.INFORMATION_MESSAGE);

        }
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            //selecaoSituacao();
            Distribuicao distri = new Distribuicao();

            distri.setDataDist(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            distri.setMesAno(ftfMes.getText());

            DistribuicaoBD processoBD = new DistribuicaoBD();
            if (processoBD.alteraDistribuicao(distri)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                tblDistribuicao.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraDistTramiAnaliseJuridico() {
        Distribuicao distri = new Distribuicao();
        DistribuicaoBD distribuicaoBD = new DistribuicaoBD();
        distri.setId(listDistribuicao.get(tblDistribuicao.getSelectedRow()).getId());
        switch (tramitaParaOnde) {
            case "analise":
                somaEntradaAnalista();
                break;
            case "juridico":
                distri.setTramitouJuridico("SIM");
                if (distribuicaoBD.alteraDistTramiJuridico(distri)) {
                    try {
                        //JOptionPane.showMessageDialog(this, "Pr!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                        executaTramitacaoDmaDestino();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                break;

            case "fiscalização":
                distri.setTramitouFiscalizacao("SIM");
                if (distribuicaoBD.alteraDistTramiFiscalizacao(distri)) {
                    try {
                        //JOptionPane.showMessageDialog(this, "Pr!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                        executaTramitacaoDmaDestino();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
                break;

        }
    }

    private void excluiDistribuicao() {

        DistribuicaoBD processoBD = new DistribuicaoBD();
        if (processoBD.excluiProcesso(listDistribuicao.get(tblDistribuicao.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Distribuicao excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            subtraiEntradaAnalista();
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esses dados!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void executaTramitacao() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMes.setValue(jdcDataTramite.getDate());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        //int ultima = tableModel.getRowCount() - 1;  
        //if (ultima > -1) { 
        //}
        //tblDistribuicao.setRowSelectionInterval(0, 0);
        //int indice = tblDistribuicao.getSelectedRow();
        //int idProcesso = listDistribuicao.get(indice).getId();
        String controle = getProcesso().getId() + " " + "RECEBIDO DO PROTOCOLO" + " " + "DMA";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("RECEBIDO DO PROTOCOLO");
        tram.setParecer(" ");
        tram.setSetor("DMA");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setLaudoMzu(" ");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
            limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoDmaDestino() throws ParseException {
        //try{
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = null;
        switch (tramitaParaOnde) {
            case "analise":
                controle = getProcesso().getId() + " " + "ENVIADO" + " " + " " + "DMA" + " " + "ANALISE";
                break;
            case "juridico":
                controle = getProcesso().getId() + " " + "ENVIADO" + " " + " " + "DMA" + " " + "JURIDICO";
                break;
            case "fiscalização":
                controle = getProcesso().getId() + " " + "ENVIADO" + " " + " " + "DMA" + " " + "FISCALIZAÇÃO";
                break;
        }

        tram.setUsuario(getUsuario());
        tram.setProcesso(getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("ENVIADO");
        tram.setParecer(" ");
        tram.setSetor(" ");
        switch (tramitaParaOnde) {
            case "analise":
                tram.setSetorOrigem("DMA");
                tram.setSetorDestino("ANÁLISE");
                break;
            case "juridico":
                tram.setSetorOrigem("DMA");
                tram.setSetorDestino("JURÍDICO");
                break;
            case "fiscalização":
                tram.setSetorOrigem("DMA");
                tram.setSetorDestino("FISCALIZAÇÃO");
                break;
        }
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //alteraDistTramiAnalise();
            //atualizaTabela();
            switch (tramitaParaOnde) {
                case "analise":
                    somaEntradaAnalista();
                    break;
                case "fiscalização":
                    somaEntradaAnalista();
                    ModificaStatusDenuncia(getProcesso().getDenuncia().getId());
                    if ("App".equals(getProcesso().getDenuncia().getOrigem())) {
                        //enviaNotificacaoDenuncia("Denúncia: " + getProcesso().getNumProcesso() + " enviada ao setor da fiscalização", getProcesso().getDenuncia().getToken());
                        enviaNotificacaoDenunciaFcm(getProcesso().getDenuncia().getToken(),"Denúncia: " + getProcesso().getNumProcesso() + " enviada ao setor da fiscalização");
                    }
                    break;
            }

            desabilitaBotoes();
            desabilitaCampos();
            limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
        /*}catch(NullPointerException e){
        //JOptionPane.showMessageDialog(this, "Erro ao tramitar "+e, "Erro", JOptionPane.ERROR_MESSAGE);
        }*/
    }

    private void caixaAlta() {
        //txtControleDig.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }

    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcData.setDate(hoje);
    }

    private void limpaCampos() {
        {
            analista = null;
            txtIdAnalista.setText("");
            txtNomeAnalista.setText("");
            txtIdProcesso.setText("");
            txtNumeroProcesso.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
            ftfMes.setText("");

        }
    }

    private void habilitaCampos() {
        //txtIdUsuario.setEnabled(true);
        txtIdAnalista.setEditable(true);
        btnSelecionaAnalista.setEnabled(true);

        txtIdProcesso.setEditable(true);
        btnSelecionaProcesso.setEnabled(true);

        jdcData.setEnabled(true);

    }

    private void desabilitaCampos() {

        txtIdAnalista.setEditable(false);
        txtIdProcesso.setEditable(false);

        btnSelecionaAnalista.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        jdcData.setEnabled(false);

    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);

        btnExcluir.setEnabled(true);
        btnSelecionaAnalista.setEnabled(true);
        btnTramitarAnalise.setEnabled(true);
        btnTramitarJuridico.setEnabled(true);
        //btnSegundaVia.setEnabled(false);
    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);

        btnExcluir.setEnabled(false);
        //btnSegundaVia.setEnabled(true);

        btnTramitarAnalise.setEnabled(false);
        btnTramitarJuridico.setEnabled(false);
    }

    private void selecionaProcessoTramitacao() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            //tramitacaoFrame.setProcesso(listDistribuicao.get(tblProcesso.getSelectedRow()));
            tramitacaoFrame.requestFocusIdProcesso();
            this.dispose();
            tramitacaoFrame.toFront();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoEmissaoTitulo() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            //emissaoTituloFrame.setProcesso(listDistribuicao.get(tblProcesso.getSelectedRow()));
            this.dispose();
            emissaoTituloFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Cobrador da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaDistribuicaoAnalise() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            analiseFrame.setDistribuicao(listDistribuicao.get(tblDistribuicao.getSelectedRow()));
            this.dispose();
            analiseFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaDistribuicaoJuridico() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            juridicoFrame.setDistribuicao(listDistribuicao.get(tblDistribuicao.getSelectedRow()));
            this.dispose();
            juridicoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaDistribuicaoFiscalizacao() {
        if (tblDistribuicao.getSelectedRow() != -1) {
            fiscalizacaoFrame.setDistribuicao(listDistribuicao.get(tblDistribuicao.getSelectedRow()));
            this.dispose();
            fiscalizacaoFrame.toFront();
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

    private void buscaProcesso(String onde) {
        ProcessoBD processoBD = new ProcessoBD();
        if ("lancamento".equals(onde)) {
            listProcesso = processoBD.consultaProcesso("DistribuicaoFrame");
        } else if ("tramitacao".equals(onde)) {
            listProcesso = processoBD.consultaProcesso(null);
        }

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
                JOptionPane.showMessageDialog(this, "ID não existe - Processo", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdProcesso.setText("");
                txtNumeroProcesso.setText("");
                txtNomeRequerente.setText("");
                processo = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            txtIdProcesso.setText("");
            txtNumeroProcesso.setText("");
            txtNomeRequerente.setText("");
            processo = null;

            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaAnalista() {
        AnalistaBD analistaBD = new AnalistaBD();
        listAnalista = analistaBD.consultaAnalista();
        int binario = 0;
        try {
            int max = listAnalista.size();
            int id_busca = Integer.parseInt(txtIdAnalista.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listAnalista.get(i).getId();
                if (listAnalista.get(i).getId() == id_busca) {
                    setAnalista(listAnalista.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe - Analista", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdAnalista.setText("");
                txtNomeAnalista.setText("");
                analista = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            analista = null;
            txtNomeAnalista.setText("");
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
                JOptionPane.showMessageDialog(this, "ID não existe - Usuario", "ID", JOptionPane.ERROR_MESSAGE);
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel20 = new javax.swing.JPanel();
        txtNomeAnalista = new javax.swing.JTextField();
        btnSelecionaAnalista = new javax.swing.JButton();
        txtIdAnalista = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        ftfMesTramite = new javax.swing.JFormattedTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btnSelecionaProcesso = new javax.swing.JButton();
        txtIdProcesso = new javax.swing.JTextField();
        txtNumeroProcesso = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        txtNomeRequerente = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDistribuicao = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnTramitarAnalise = new javax.swing.JButton();
        btnTramitarJuridico = new javax.swing.JButton();
        btnTramitarFiscalização = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Cadastro Processo\n");
        setPreferredSize(new java.awt.Dimension(1000, 670));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/DISTRIB_64x64.png"))); // NOI18N
        jLabel1.setText("Distribuição DMA");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Analista"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        txtNomeAnalista.setEditable(false);
        txtNomeAnalista.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeAnalista.setEnabled(false);
        txtNomeAnalista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeAnalistaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(txtNomeAnalista, gridBagConstraints);

        btnSelecionaAnalista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaAnalista.setEnabled(false);
        btnSelecionaAnalista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaAnalistaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaAnalista, gridBagConstraints);

        txtIdAnalista.setEditable(false);
        txtIdAnalista.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdAnalistaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdAnalista, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Processo"));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabel24.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(jLabel24, gridBagConstraints);

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
        jPanel23.add(btnSelecionaProcesso, gridBagConstraints);

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
        jPanel23.add(txtIdProcesso, gridBagConstraints);

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
        jPanel23.add(txtNumeroProcesso, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jPanel23, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerente ou Denunciado"));
        jPanel21.setLayout(new java.awt.GridBagLayout());

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
        jPanel21.add(txtNomeRequerente, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        jPanel2.add(jPanel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel2, gridBagConstraints);

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

        tblDistribuicao.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblDistribuicao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Requerente", "Tipo Licença", "Analista/Fiscal", "Data", "Usuario", "STATUS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDistribuicao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDistribuicao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDistribuicaoMouseClicked(evt);
            }
        });
        tblDistribuicao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDistribuicaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDistribuicao);

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
        gridBagConstraints.ipady = 23;
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

        btnTramitarAnalise.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitarAnalise.setText("Tramitar Análise");
        btnTramitarAnalise.setDisabledSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/OK.png"))); // NOI18N
        btnTramitarAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarAnaliseActionPerformed(evt);
            }
        });
        jPanel4.add(btnTramitarAnalise);

        btnTramitarJuridico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitarJuridico.setText("Tramitar Juridico");
        btnTramitarJuridico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarJuridicoActionPerformed(evt);
            }
        });
        jPanel4.add(btnTramitarJuridico);

        btnTramitarFiscalização.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitarFiscalização.setText("Tramitar Fiscalização");
        btnTramitarFiscalização.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarFiscalizaçãoActionPerformed(evt);
            }
        });
        jPanel4.add(btnTramitarFiscalização);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        jMenuBar1.setBackground(new java.awt.Color(122, 149, 222));

        jMenu1.setText("Listagem");

        jMenuItem1.setText("Processos não baixado");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem1);

        jMenuBar1.add(jMenu1);

        setJMenuBar(jMenuBar1);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroProcesso.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        DistribuicaoBD cobradorBD = new DistribuicaoBD();
        if (cobradorBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblDistribuicao.setEnabled(false);
            limpaSelecaoTabela();
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
                incluiDistribuicao();
                tblDistribuicao.setEnabled(true);
                // txtNomeCliente.requestFocus();
                break;
            case Constantes.EDIT_MODE:
                alteraDistribuicao();
                break;
            //alteraProcessoRequerente();
            case Constantes.EDIT_MODE_REQ:
                break;
            default:
                break;
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblDistribuicao.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblDistribuicao.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            txtIdAnalista.setEditable(false);
            btnSelecionaAnalista.setEnabled(false);
            modo = Constantes.EDIT_MODE;
            buscaAnalista();
            buscaUsuario(idUsuario);
            tblDistribuicao.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblDistribuicao.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do processo?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiDistribuicao();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseClicked
    }//GEN-LAST:event_txtFiltroProcessoMouseClicked

    private void txtFiltroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcessoActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoActionPerformed

    private void tblDistribuicaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDistribuicaoMouseClicked
        if (evt.getClickCount() == 2) {

            switch (modoSeleciona) {
                case Constantes.INCLUIR:
                    //selecionaProcessoImovel();
                    dispose();
                    break;
                case Constantes.ALTERAR:
                    selecionaProcessoTramitacao();
                    dispose();
                    break;
                case Constantes.EMISSAOLICENCAFRAME:
                    selecionaProcessoEmissaoTitulo();
                    dispose();
                    break;
                case Constantes.ANALISE_FRAME:
                    selecionaDistribuicaoAnalise();
                    dispose();
                    break;
                case Constantes.JURIDICO_FRAME:
                    selecionaDistribuicaoJuridico();
                    dispose();
                    break;
                case Constantes.FISCALIZACAO_FRAME:
                    selecionaDistribuicaoFiscalizacao();
                    dispose();
                    break;
                default:
                    break;
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblDistribuicaoMouseClicked

    private void tblDistribuicaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDistribuicaoKeyPressed

        switch (modoSeleciona) {
            case Constantes.INCLUIR:
                //selecionaProcessoImovel();
                dispose();
                break;
            case Constantes.ALTERAR:
                selecionaProcessoTramitacao();
                dispose();
                break;
            case Constantes.EMISSAOLICENCAFRAME:
                selecionaProcessoEmissaoTitulo();
                dispose();
                break;
            case Constantes.ANALISE_FRAME:
                selecionaDistribuicaoAnalise();
                dispose();
                break;
            case Constantes.JURIDICO_FRAME:
                selecionaDistribuicaoJuridico();
                dispose();
                break;
            case Constantes.FISCALIZACAO_FRAME:
                selecionaDistribuicaoFiscalizacao();
                dispose();
                break;
            default:
                break;
        }
        evt.consume();

    }//GEN-LAST:event_tblDistribuicaoKeyPressed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void txtNomeAnalistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAnalistaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeAnalistaActionPerformed

    private void btnSelecionaAnalistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAnalistaActionPerformed
        AnalistaFrame analistaFrame = new AnalistaFrame(this);
        analistaFrame.setVisible(true);
        this.getDesktopPane().add(analistaFrame);
        analistaFrame.toFront();
        centralizaJanelaRegVen(analistaFrame);             // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaAnalistaActionPerformed

    private void txtIdAnalistaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdAnalistaFocusLost
        buscaAnalista();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnalistaFocusLost

    private void btnSelecionaProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoActionPerformed
        ProcessoFrame processoFrame = new ProcessoFrame(this);
        processoFrame.setVisible(true);
        this.getDesktopPane().add(processoFrame);
        processoFrame.toFront();
        processoFrame.txtFiltroProcesso.requestFocus();
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaProcessoActionPerformed

    private void txtIdProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdProcessoFocusLost
        buscaProcesso("lancamento");  // TODO add your handling code here:
}//GEN-LAST:event_txtIdProcessoFocusLost

    private void txtNumeroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcessoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNumeroProcessoActionPerformed

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void btnTramitarAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarAnaliseActionPerformed
        if (tblDistribuicao.getSelectedRow() != -1) {
            int indice = tblDistribuicao.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                tramitaParaOnde = "analise";
                buscaProcesso("tramitacao");
                if ("NAO".equals(listDistribuicao.get(indice).getProcesso().getArquivado())) {
                    alteraDistTramiAnaliseJuridico();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
}//GEN-LAST:event_btnTramitarAnaliseActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void btnTramitarJuridicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarJuridicoActionPerformed

        if (tblDistribuicao.getSelectedRow() != -1) {
            int indice = tblDistribuicao.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                tramitaParaOnde = "juridico";
                buscaProcesso("tramitacao");
                if ("NAO".equals(listDistribuicao.get(indice).getProcesso().getArquivado())) {
                    alteraDistTramiAnaliseJuridico();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.ERROR_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarJuridicoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        AcessoBD acessoBd = new AcessoBD();
        try {
            HashMap parametros = new HashMap();
            //parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\DTI.jpg");
            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\ListagemProcessoNaoLancadoDma.jasper", parametros, acessoBd.conectar());
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void btnTramitarFiscalizaçãoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarFiscalizaçãoActionPerformed
        if (tblDistribuicao.getSelectedRow() != -1) {
            int indice = tblDistribuicao.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                tramitaParaOnde = "fiscalização";
                buscaProcesso("tramitacao");
                if ("NAO".equals(listDistribuicao.get(indice).getProcesso().getArquivado())) {
                    alteraDistTramiAnaliseJuridico();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.ERROR_MESSAGE);
                }

            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarFiscalizaçãoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGVTipo;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaAnalista;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JButton btnTramitarAnalise;
    private javax.swing.JButton btnTramitarFiscalização;
    private javax.swing.JButton btnTramitarJuridico;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblDistribuicao;
    private javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtIdAnalista;
    public javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtNomeAnalista;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNumeroProcesso;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the requerente
     */
    public Analista getAnalista() {
        return analista;
    }

    /**
     * @param requerente the requerente to set
     */
    public void setAnalista(Analista analista) {
        this.analista = analista;
        txtNomeAnalista.setText(analista.getNome());
        txtIdAnalista.setText(analista.getId().toString());
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
        return processo;
    }

    /**
     * @param processo the processo to set
     */
    public void setProcesso(Processo processo) {
        this.processo = processo;
        txtNumeroProcesso.setText(processo.getNumProcesso());
        txtIdProcesso.setText(processo.getId().toString());
        if (processo.getRequerente().getNome() == null) {
            txtNomeRequerente.setText(processo.getPessoa().getNome());
        } else {
            txtNomeRequerente.setText(processo.getRequerente().getNome());
        }

    }

    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
}
