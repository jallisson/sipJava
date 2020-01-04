/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.fiscalizacao;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;
import sip.fiscalizacao.*;
import sip.menulogin.Menu;
import sip.util.Constantes;
import sip.requerente.Requerente;
import sip.emissaolicenca.EmissaoLicencaFrame;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JYearChooser;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONObject;
import sip.acessobd.AcessoBD;
import sip.analista.Analista;
import sip.analista.AnalistaBD;
import sip.denuncia.Denuncia;
import sip.denuncia.DenunciaBD;
import sip.distribuicao.Distribuicao;
import sip.distribuicao.DistribuicaoBD;
import sip.distribuicao.DistribuicaoFrame;
import sip.processo.Processo;
import sip.processo.ProcessoFrame;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.ColorirTabelaDenuncia;
import sip.util.ColorirTabelaFiscalizacao;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class FiscalizacaoFrame extends javax.swing.JInternalFrame {

    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Fiscalizacao> listFiscalizacao;
    private List<Fiscalizacao> listFiscalizacaoParecer;
    private List<ParecerFiscalizacao> listParecerFiscalizacao;
    private int modo;
    private int modoSeleciona;
    private EmissaoLicencaFrame emissaoTituloFrame;
    private ParecerFiscalizacaoFrame parecerFiscalizacaoFrame;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private Distribuicao distribuicao;
    private List<Distribuicao> listDistribuicao;
    private Processo processo;
    private Integer idProcesso;
    private String idFiscalizacao;
    private String SituacaoParecer;//viavel não-viavel
    JPopupMenu jPopupMenu = new JPopupMenu();
    private final String apiKey = "AIzaSyAoQh5qojWNnm_Tk5fAl8tsnFueD6mG-AE";

    /**
     * Creates new form ClienteFrame
     */
    public FiscalizacaoFrame() {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        jPanel19.setVisible(false);
        jlClipe.setVisible(false);
        popupBtnTramitarViavelNaoViavel();
        Saldo();

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

    public FiscalizacaoFrame(EmissaoLicencaFrame emissaoTituloFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.emissaoTituloFrame = emissaoTituloFrame;
        modoSeleciona = Constantes.INCLUIR;
        formataData();
    }

    public FiscalizacaoFrame(ParecerFiscalizacaoFrame parecerFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.parecerFiscalizacaoFrame = parecerFrame;
        modoSeleciona = Constantes.PARECERFISCALIZACAO_FRAME;
        formataData();
    }

    private void popupBtnTramitarViavelNaoViavel() {
        //cria os itens
        JMenuItem item1 = new JMenuItem("Viavel");
        JMenuItem item2 = new JMenuItem("Não Viavel");
        //cria o menu popup e acrescenta os itens

        jPopupMenu.add(item1);
        jPopupMenu.addSeparator();
        jPopupMenu.add(item2);

        //jPopupMenu.show(btnTramitar, 10, 10);
        //atribui uma ação para os itens
        item1.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSituacaoParecer("VIAVEL");
                if (getSituacaoParecer() != null) {
                    buscaDistribuicao();
                    consultaParecerFiscalizacao();
                }
            }
        });
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setSituacaoParecer("NÃO VIAVEL");
                if (getSituacaoParecer() != null) {
                    buscaDistribuicao();
                    consultaParecerFiscalizacao();
                }
            }
        });
    }

    private void Saldo() {
        FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        listFiscalizacao = fiscalizacaoBD.consultaFiscalizacao(getUsuario().getId());
        //int testando = listFiscalizacao.size();
        if(listFiscalizacao.size() > 0 && listFiscalizacao.size() < 2){
        //FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        //listFiscalizacao = fiscalizacaoBD.consultaFiscalizacao(getUsuario().getId());
        int entrada = listFiscalizacao.get(0).getAnalista().getQtdeEntrada();
        int saida = listFiscalizacao.get(0).getAnalista().getQtdeSaida();
        int saldo = entrada - saida;

        jLabelEntrada.setText(Integer.toString(entrada));
        jLabelSaida.setText(Integer.toString(saida));
        jLabelSaldo.setText(Integer.toString(saldo));
        }else if(listFiscalizacao.size() > 1){
        //FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        //listFiscalizacao = fiscalizacaoBD.consultaFiscalizacao(getUsuario().getId());
        int entrada = listFiscalizacao.get(1).getAnalista().getQtdeEntrada();
        int saida = listFiscalizacao.get(1).getAnalista().getQtdeSaida();
        int saldo = entrada - saida;

        jLabelEntrada.setText(Integer.toString(entrada));
        jLabelSaida.setText(Integer.toString(saida));
        jLabelSaldo.setText(Integer.toString(saldo));
        }
    }

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel7.add(jdcData);
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblFiscalizacao.getModel();
        listModel = tblFiscalizacao.getSelectionModel();
        tblFiscalizacao.setDefaultRenderer(Object.class, new ColorirTabelaFiscalizacao());
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheFiscalizacao();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblFiscalizacao.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblFiscalizacao.getColumnModel().getColumn(1).setPreferredWidth(350);
            tblFiscalizacao.getColumnModel().getColumn(2).setPreferredWidth(1000);
            tblFiscalizacao.getColumnModel().getColumn(3).setPreferredWidth(800);
            tblFiscalizacao.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblFiscalizacao.getColumnModel().getColumn(5).setPreferredWidth(300);
            tblFiscalizacao.getColumnModel().getColumn(6).setPreferredWidth(200);
            tblFiscalizacao.getColumnModel().getColumn(7).setMaxWidth(0);
            tblFiscalizacao.getColumnModel().getColumn(7).setMinWidth(0);
            tblFiscalizacao.getTableHeader().getColumnModel().getColumn(7).setMaxWidth(0);
            tblFiscalizacao.getTableHeader().getColumnModel().getColumn(7).setMinWidth(0);

        } catch (ParseException ex) {
            Logger.getLogger(FiscalizacaoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        String nome = null;
        if (txtFiltro.getText().equals("")) {
            listFiscalizacao = fiscalizacaoBD.consultaFiscalizacao(getUsuario().getId());
        } else {
            String valor = txtFiltro.getText();
            listFiscalizacao = fiscalizacaoBD.consultaFiscalizacaoNome(valor, valor, getUsuario().getId());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listFiscalizacao.size(); i++) {
            if (listFiscalizacao.get(i).getRequerente().getNome() == null) {
                nome = listFiscalizacao.get(i).getPessoa().getNome();
            } else {
                nome = listFiscalizacao.get(i).getRequerente().getNome();
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listFiscalizacao.get(i).getId(),
                listFiscalizacao.get(i).getProcesso().getNumProcesso(),
                listFiscalizacao.get(i).getProcesso().getTipoLicenca(),
                nome,
                listFiscalizacao.get(i).getAnalista().getNome(),
                simpleDateFormat.format(listFiscalizacao.get(i).getDataFiscalizacao()),
                //listFiscalizacao.get(i).getProcesso().getDenuncia().getToken()
                listFiscalizacao.get(i).getUsuario().getNome(),
                listFiscalizacao.get(i).getConcluido(),});
        }

    }

    private void consultaParecerFiscalizacao() {
        FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        if (txtFiltro.getText().trim().equals("")) {
            listFiscalizacaoParecer = fiscalizacaoBD.consultaParecerFiscalizacao(getUsuario().getId());
        } else {
            //String valor = txtFiltroProcesso.getText();
            //listProcAnexos = processoBD.consultaAnexosProcessoNome(valor, valor);
        }
        detalheParecer();
    }

    private void detalheParecer() {
        try {
            if (tblFiscalizacao.getSelectedRow() != -1) {
                listParecerFiscalizacao = listFiscalizacaoParecer.get(tblFiscalizacao.getSelectedRow()).getListParecerFiscalizacao();
                //removeAnexosProcessoTabela();
            }
            //int indice = tblFiscalizacao.getSelectedRow();
            //String teste = listParecerFiscalizacao.get(0).getAnexoIncluso();

            if ("SIM".equals(listParecerFiscalizacao.get(0).getAnexoIncluso())) {

                if (listParecerFiscalizacao.size() > 1) {
                    JOptionPane.showMessageDialog(this, "Lance um parecer para finalizar o processo!", "Observação", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    if ("NAO".equals(listDistribuicao.get(0).getProcesso().getArquivado())) {
                        try {
                            //JOptionPane.showMessageDialog(this, "Tramita!", "teste", JOptionPane.INFORMATION_MESSAGE);
                            executaTramitacaoConclusaoFiscalizacao();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                        try {
                            executaTramitacaoRetornoDMA();
                        } catch (ParseException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                    }

                }
            } else {
                JOptionPane.showMessageDialog(this, "Dever concluir o parecer para finalizar o processo", "Parecer", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Não existe nenhum parecer do fiscal", "Parecer", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void mostraDetalheFiscalizacao() {
        if (tblFiscalizacao.getSelectedRow() != -1) {
            int indice = tblFiscalizacao.getSelectedRow();
            txtIdDistribuicao.setText(listFiscalizacao.get(indice).getDistribuicao().getId().toString());
            txtNumeroProcesso.setText(listFiscalizacao.get(indice).getProcesso().getNumProcesso());
            if (listFiscalizacao.get(indice).getRequerente().getNome() == null) {
                txtNomeRequerente.setText(listFiscalizacao.get(indice).getPessoa().getNome());
            } else {
                txtNomeRequerente.setText(listFiscalizacao.get(indice).getRequerente().getNome());
            }

            jdcData.setDate(listFiscalizacao.get(indice).getDataFiscalizacao());

        } else {
            //txtNome.setText("");
            //txtDescricao.setText("");
        }
    }

    private void subtraiEntradaAnalista() {
        int indice = tblFiscalizacao.getSelectedRow();
        int qtde = 1;

        Analista analista = new Analista();
        analista.setId(listFiscalizacao.get(indice).getAnalista().getId());
        analista.setQtdeSaida(listFiscalizacao.get(indice).getAnalista().getQtdeSaida() + qtde);

        AnalistaBD analistaBD = new AnalistaBD();
        if (analistaBD.alteraAnalistaSaida(analista)) {
            alteraFiscalizacaoConcluido();
            JOptionPane.showMessageDialog(this, "Processo subtraido do Analista", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            Saldo();
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro enviar dado analista!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaRelatorio() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblFiscalizacao.getSelectedRow();
        int mostraID = listFiscalizacao.get(indice).getProcesso().getDenuncia().getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("LOGO_CIDADE", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("ID_OCORRENCIA", (long) mostraID);

            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\Ocorrencia.jasper", parametros, acessoBd.conectar());

            //JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            JasperViewer.viewReport(jp, false);

        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorioApp() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblFiscalizacao.getSelectedRow();
        int mostraID = listFiscalizacao.get(indice).getProcesso().getDenuncia().getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("LOGO_CIDADE", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("ID_OCORRENCIA", (long) mostraID);

            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\DenunciaApp.jasper", parametros, acessoBd.conectar());

            //JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            JasperViewer.viewReport(jp, false);

        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorioCapa() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblFiscalizacao.getSelectedRow();
        JasperPrint jp;

        int mostraID = listFiscalizacao.get(indice).getProcesso().getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");

            //*para funcionario abrir os relatorio dentro do pacote tem q configurar o parametros no ireport como InputScream
            //InputStream caminhoImagemBrasao = getClass().getResourceAsStream("/sip/imagemrelatorio/logocidade.jpg");
            //parametros.put("CAMINHO_IMAGEM", caminhoImagemBrasao);
            //parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");
            parametros.put("ID_PROCESSO", (long) mostraID);

            if (listFiscalizacao.get(indice).getProcesso().getDenuncia().getOrigem() != null) {
                jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "//relatorios//FolhaRostoDenunciaApp.jasper", parametros, acessoBd.conectar());
            } else {
                jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "//relatorios//FolhaRostoDenuncia.jasper", parametros, acessoBd.conectar());
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

    private void limpaCampos() {
        {
            txtIdDistribuicao.setText("");
            txtNumeroProcesso.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);

        }
    }

    private void alteraDistRecebidoFiscalizacao() {
        Distribuicao distri = new Distribuicao();
        DistribuicaoBD distribuicaoBD = new DistribuicaoBD();
        distri.setStatus("RECEBIDO");
        distri.setId(getDistribuicao().getId());

        if (distribuicaoBD.alteraDistRecebidoFiscalizacao(distri)) {

        } else {
            JOptionPane.showMessageDialog(this, "Erro ao baixar na Distribuição!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void excluiDistribuicao() {

        DistribuicaoBD processoBD = new DistribuicaoBD();
        if (processoBD.excluiProcesso(listDistribuicao.get(tblFiscalizacao.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Distribuicao excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            subtraiEntradaAnalista();
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esses dados!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void incluiFiscalizacao() {
        if (getDistribuicao() == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição!", "Distribuição", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if ("SIM".equals(getDistribuicao().getProcesso().getArquivado())) {
            JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Fiscalizacao fiscalizacao = new Fiscalizacao();
            ftfMes.setValue(jdcData.getDate().getTime());

            fiscalizacao.setUsuario(getUsuario());
            fiscalizacao.setDistribuicao(getDistribuicao());
            fiscalizacao.setDataFiscalizacao(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            fiscalizacao.setAno(ano);
            fiscalizacao.setConcluido("NAO");

            FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
            if (fiscalizacaoBD.incluiFiscalizacao(fiscalizacao)) {
                JOptionPane.showMessageDialog(this, "Fiscalizacao cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                alteraDistRecebidoFiscalizacao();
                try {
                    executaTramitacao();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente a Fiscalizacao já existe!", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraFiscalizacao() {
        if (distribuicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição!", "Distribuição", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Fiscalizacao fiscalizacao = new Fiscalizacao();
            ftfMes.setValue(jdcData.getDate().getTime());

            fiscalizacao.setId(this.listFiscalizacao.get(tblFiscalizacao.getSelectedRow()).getId());
            fiscalizacao.setDataFiscalizacao(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            fiscalizacao.setAno(ano);

            FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
            if (fiscalizacaoBD.alteraFiscalizacao(fiscalizacao)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                tblFiscalizacao.setEnabled(true);
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraFiscalizacaoConcluido() {
        java.util.Date pega = jdcData.getDate();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        String ano = formato.format(pega);

        Fiscalizacao fiscalizacao = new Fiscalizacao();
        ftfMes.setValue(jdcData.getDate().getTime());

        fiscalizacao.setId(this.listFiscalizacao.get(tblFiscalizacao.getSelectedRow()).getId());
        fiscalizacao.setConcluido("SIM");

        FiscalizacaoBD fiscalizacaoBD = new FiscalizacaoBD();
        if (fiscalizacaoBD.alteraFiscalizacaoConcluido(fiscalizacao)) {
            JOptionPane.showMessageDialog(this, "Fiscalizacao concluida com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //atualizaTabela();
            //desabilitaBotoes();
            //desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao concluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void exlcuiFiscalizacao() {
        FiscalizacaoBD loteBD = new FiscalizacaoBD();
        if (loteBD.excluiFiscalizacao(listFiscalizacao.get(tblFiscalizacao.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Tramitação !", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void ModificaStatusDenuncia(Integer idDenuncia) {
        Denuncia denuncia = new Denuncia();
        denuncia.setId(idDenuncia);
        denuncia.setStatusApp("Finalizado");

        DenunciaBD denunciaBD = new DenunciaBD();

        if (denunciaBD.alteraDenunciaStatusApp(denuncia)) {
            JOptionPane.showMessageDialog(this, "Status Denúncia Modificado!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
            //executaTramitacaoDistribuicao();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao modificar Status ", "Denúncia", JOptionPane.ERROR_MESSAGE);
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

    private void executaTramitacao() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMes.setValue(jdcDataTramite.getDate());

        //int ultima = tableModel.getRowCount() - 1;
        //if (ultima > -1) {
        //}
        //tblFiscalizacao.setRowSelectionInterval(0, 0);
        //int indice = tblFiscalizacao.getSelectedRow();
        //int idProc = listFiscalizacao.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "RECEBIDO DO DMA" + " " + "FISCALIZAÇÃO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("RECEBIDO DO DMA");
        tram.setParecer(" ");
        tram.setSetor("FISCALIZAÇÃO");
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

    private void executaTramitacaoConclusaoFiscalizacao() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());

        //int ultima = tableModel.getRowCount() - 1;
        //if (ultima > -1) {
        //}
        //tblFiscalizacao.setRowSelectionInterval(0, 0);
        //int indice = tblFiscalizacao.getSelectedRow();
        //int idProc = listFiscalizacao.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "PROCESSO CONCLUIDO NA FISCALIZAÇÃO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("PROCESSO CONCLUIDO NA FISCALIZAÇÃO");
        tram.setParecer(getSituacaoParecer());//VIAVEL OU NÃO VIAVEL
        tram.setSetor("FISCALIZAÇÃO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            setSituacaoParecer(null);
            subtraiEntradaAnalista();
            //String numTokenTeste2 = getDistribuicao().getProcesso().getNumProcesso();
            //String numTokenTeste = getDistribuicao().getDenuncia().getToken();

            ModificaStatusDenuncia(getDistribuicao().getDenuncia().getId());
            if ("App".equals(getDistribuicao().getDenuncia().getOrigem())) {
                //enviaNotificacaoDenuncia("Denúncia: " + getDistribuicao().getProcesso().getNumProcesso() + " Finalizada pelo fiscal e retornou ao DMA", getDistribuicao().getDenuncia().getToken());
                enviaNotificacaoDenunciaFcm(getDistribuicao().getDenuncia().getToken(),"Processo de Denúncia: " + getDistribuicao().getProcesso().getNumProcesso() + " Finalizada pelo fiscal e retornou ao DMA FISCALIZAÇÃO");
            }
            atualizaTabela();
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

    private void executaTramitacaoRetornoDMA() throws ParseException {
        //try{
        java.util.Date date = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(date);

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());

        //int ultima = tableModel.getRowCount() - 1;
        //if (ultima > -1) {
        //}
        //tblFiscalizacao.setRowSelectionInterval(0, 0);
        //int indice = tblFiscalizacao.getSelectedRow();
        //int idProc = listFiscalizacao.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "ENVIADO" + " " + " " + "" + " " + "DMA";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("ENVIADO");
        tram.setParecer(getSituacaoParecer());//VIAVEL OU NÃO VIAVEL
        tram.setSetor(" ");
        tram.setSetorOrigem("FISCALIZAÇÃO");
        tram.setSetorDestino("DMA");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            //JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            setSituacaoParecer(null);
            subtraiEntradaAnalista();
            atualizaTabela();
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
    }

    private void habilitaCampos() {

        txtIdDistribuicao.setEditable(true);
        btnSelecionaDistribuicao.setEnabled(true);
        jdcData.setEnabled(true);

    }

    private void desabilitaCampos() {
        txtIdDistribuicao.setEditable(false);
        btnSelecionaDistribuicao.setEnabled(false);
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

    private void selecionaFiscalizacaoParecerFiscalizacao() {
        if (tblFiscalizacao.getSelectedRow() != -1) {
            parecerFiscalizacaoFrame.setFiscalizacao(listFiscalizacao.get(tblFiscalizacao.getSelectedRow()));
            this.dispose();
            parecerFiscalizacaoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma fiscalizacao da lista!", "Fiscalizacao", JOptionPane.INFORMATION_MESSAGE);
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

    private void buscaDistribuicao() {
        DistribuicaoBD distribuicaoBD = new DistribuicaoBD();
        listDistribuicao = distribuicaoBD.consultaDistribuicao("FiscalizacaoFrame", getUsuario().getId());
        int binario = 0;
        try {
            int max = listDistribuicao.size();
            int id_busca = Integer.parseInt(txtIdDistribuicao.getText());
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listDistribuicao.get(i).getId();
                if (listDistribuicao.get(i).getId() == id_busca) {
                    setDistribuicao(listDistribuicao.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdDistribuicao.setText("");
                txtNumeroProcesso.setText("");
                distribuicao = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            distribuicao = null;
            txtNumeroProcesso.setText("");
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlClipe = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFiscalizacao = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnImprimir1 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        btnAnexar = new javax.swing.JButton();
        jPanel23 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btnSelecionaDistribuicao = new javax.swing.JButton();
        txtIdDistribuicao = new javax.swing.JTextField();
        txtNumeroProcesso = new javax.swing.JTextField();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNomeUsuario = new javax.swing.JTextField();
        btnSelecionaUsuario = new javax.swing.JButton();
        txtIdUsuario = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        txtNomeRequerente = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        ftfMes = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        ftfMesTramite = new javax.swing.JFormattedTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabelEntrada = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabelSaida = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabelSaldo = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        btnParecerT1 = new javax.swing.JButton();
        btnTramitar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Lote Emissão Titulo\n");
        setPreferredSize(new java.awt.Dimension(1100, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/Fiscalizacao_64x64.png"))); // NOI18N
        jLabel1.setText("Fiscalização");
        jPanel1.add(jLabel1);

        jlClipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/clipe64x64.png"))); // NOI18N
        jPanel1.add(jlClipe);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Processo");
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

        tblFiscalizacao.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblFiscalizacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Tipo Processo", "Denunciado", "Fiscal", "Data Análise", "Usuario", "Concluido"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFiscalizacao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblFiscalizacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFiscalizacaoMouseClicked(evt);
            }
        });
        tblFiscalizacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblFiscalizacaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblFiscalizacao);

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

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/printer.png"))); // NOI18N
        btnImprimir.setText("IMPRIMIR DENÚNCIA");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir);

        btnImprimir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/printer.png"))); // NOI18N
        btnImprimir1.setText("Capa Processo");
        btnImprimir1.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                btnImprimir1AncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        btnImprimir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimir1ActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        btnAnexar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/ANEXAR_16x16.png"))); // NOI18N
        btnAnexar.setText("Anexos");
        btnAnexar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnexarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(btnAnexar, gridBagConstraints);

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Distribuicao"));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabel24.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(jLabel24, gridBagConstraints);

        btnSelecionaDistribuicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaDistribuicao.setEnabled(false);
        btnSelecionaDistribuicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaDistribuicaoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(btnSelecionaDistribuicao, gridBagConstraints);

        txtIdDistribuicao.setEditable(false);
        txtIdDistribuicao.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdDistribuicaoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel23.add(txtIdDistribuicao, gridBagConstraints);

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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel23, gridBagConstraints);

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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Denunciado"));
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Análise"));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        ftfMes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ftfMes.setEditable(false);
        ftfMes.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter(new java.text.SimpleDateFormat("MMMM' 'yy"))));
        ftfMes.setToolTipText("Data do Video");
        ftfMes.setEnabled(false);
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
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel9.setBackground(new java.awt.Color(0, 0, 51));
        jPanel9.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Cores da Tabela", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 51))); // NOI18N
        jPanel9.setLayout(new java.awt.GridBagLayout());

        jLabel7.setBackground(new java.awt.Color(255, 0, 0));
        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 0, 0));
        jLabel7.setText("Novo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel7, gridBagConstraints);

        jLabel9.setBackground(new java.awt.Color(255, 102, 102));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setText("Finalizado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel9, gridBagConstraints);

        jPanel10.setBackground(new java.awt.Color(0, 0, 51));
        jPanel10.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Saldo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(255, 255, 51))); // NOI18N
        jPanel10.setLayout(new java.awt.GridBagLayout());

        jLabel8.setBackground(new java.awt.Color(255, 0, 0));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 0, 0));
        jLabel8.setText("Entrada :");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabel8, gridBagConstraints);

        jLabelEntrada.setBackground(new java.awt.Color(255, 102, 102));
        jLabelEntrada.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelEntrada.setForeground(new java.awt.Color(255, 0, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabelEntrada, gridBagConstraints);

        jLabel10.setBackground(new java.awt.Color(255, 0, 0));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 0));
        jLabel10.setText("Saida");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabel10, gridBagConstraints);

        jLabelSaida.setBackground(new java.awt.Color(255, 102, 102));
        jLabelSaida.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabelSaida.setForeground(new java.awt.Color(255, 255, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabelSaida, gridBagConstraints);

        jLabel11.setBackground(new java.awt.Color(255, 0, 0));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 255, 0));
        jLabel11.setText("Saldo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabel11, gridBagConstraints);

        jLabelSaldo.setBackground(new java.awt.Color(255, 102, 102));
        jLabelSaldo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        jLabelSaldo.setForeground(new java.awt.Color(0, 255, 0));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jLabelSaldo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel10, gridBagConstraints);

        jTabbedPane1.addTab("Lançamento", jPanel2);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        btnParecerT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/prancheta_64x64.png"))); // NOI18N
        btnParecerT1.setText("Laudo / Auto de infração");
        btnParecerT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParecerT1ActionPerformed(evt);
            }
        });
        jPanel5.add(btnParecerT1, new java.awt.GridBagConstraints());

        btnTramitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_64x64.png"))); // NOI18N
        btnTramitar.setText("Finalizar & Tramitar DMA");
        btnTramitar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTramitarMouseClicked(evt);
            }
        });
        btnTramitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(btnTramitar, gridBagConstraints);

        jTabbedPane1.addTab("Adicionais", jPanel5);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        FiscalizacaoBD nacionalidadeBD = new FiscalizacaoBD();
        if (nacionalidadeBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblFiscalizacao.setEnabled(false);
            limpaSelecaoTabela();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiFiscalizacao();
            tblFiscalizacao.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraFiscalizacao();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblFiscalizacao.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {
            buscaDistribuicao();
            habilitaCampos();
            txtIdDistribuicao.setEnabled(false);
            habilitaBotoes();
            btnSelecionaDistribuicao.setEnabled(false);
            modo = Constantes.EDIT_MODE;
            tblFiscalizacao.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Nacionalidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiFiscalizacao();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void tblFiscalizacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFiscalizacaoMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.PARECERFISCALIZACAO_FRAME) {
                selecionaFiscalizacaoParecerFiscalizacao();
                dispose();
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblFiscalizacaoMouseClicked

    private void tblFiscalizacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblFiscalizacaoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modoSeleciona == Constantes.PARECERFISCALIZACAO_FRAME) {
                selecionaFiscalizacaoParecerFiscalizacao();
                dispose();
            }
        }
        evt.consume();
    }//GEN-LAST:event_tblFiscalizacaoKeyPressed

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void btnSelecionaDistribuicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaDistribuicaoActionPerformed
        DistribuicaoFrame distFrame = new DistribuicaoFrame(this);
        distFrame.setVisible(true);
        this.getDesktopPane().add(distFrame);
        distFrame.toFront(); // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaDistribuicaoActionPerformed

    private void txtIdDistribuicaoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdDistribuicaoFocusLost
        buscaDistribuicao();  // TODO add your handling code here:
}//GEN-LAST:event_txtIdDistribuicaoFocusLost

    private void txtNumeroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcessoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNumeroProcessoActionPerformed

    private void txtNomeUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeUsuarioActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeUsuarioActionPerformed

    private void btnSelecionaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaUsuarioActionPerformed
}//GEN-LAST:event_btnSelecionaUsuarioActionPerformed

    private void txtIdUsuarioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdUsuarioFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdUsuarioFocusLost

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void btnAnexarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnexarActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {
            int indice = tblFiscalizacao.getSelectedRow();

            setIdProcesso(listFiscalizacao.get(indice).getProcesso().getId());
            //int idProcesso = listFiscalizacao.get(indice).getProcesso().getId();
            ProcessoFrame processoFrame = new ProcessoFrame(this);
            processoFrame.setVisible(true);
            this.getDesktopPane().add(processoFrame);
            processoFrame.toFront();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnAnexarActionPerformed

    private void btnParecerT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParecerT1ActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {
            int indice = tblFiscalizacao.getSelectedRow();

            setIdFiscalizacao(listFiscalizacao.get(indice).getId().toString());
            setIdProcesso(listFiscalizacao.get(indice).getProcesso().getId());

            ParecerFiscalizacaoFrame parecerFiscalizacao = new ParecerFiscalizacaoFrame(this);
            parecerFiscalizacao.setVisible(true);
            this.getDesktopPane().add(parecerFiscalizacao);
            parecerFiscalizacao.toFront();// TODO add your handling code here:

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnParecerT1ActionPerformed

    private void btnTramitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void btnTramitarMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTramitarMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON1) { // Clique com botao direito do mouse.
            if (tblFiscalizacao.getSelectedRow() != -1) {
                //mostra na tela
                jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma fiscalizacao da lista!", "Cadastro Imobiliario", JOptionPane.INFORMATION_MESSAGE);
            }

        }

    }//GEN-LAST:event_btnTramitarMouseClicked

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {
            int indice = tblFiscalizacao.getSelectedRow();
            if (listFiscalizacao.get(indice).getProcesso().getDenuncia().getOrigem() != null) {
                executaRelatorioApp();
            } else {
                executaRelatorio();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo na tabela!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void btnImprimir1AncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_btnImprimir1AncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir1AncestorAdded

    private void btnImprimir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir1ActionPerformed
        if (tblFiscalizacao.getSelectedRow() != -1) {

            executaRelatorioCapa();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo na tabela!", "Emissão", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnAnexar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnImprimir1;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnParecerT1;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaDistribuicao;
    private javax.swing.JButton btnSelecionaUsuario;
    private javax.swing.JButton btnTramitar;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabelEntrada;
    private javax.swing.JLabel jLabelSaida;
    private javax.swing.JLabel jLabelSaldo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlClipe;
    private javax.swing.JTable tblFiscalizacao;
    private javax.swing.JTextField txtFiltro;
    public javax.swing.JTextField txtIdDistribuicao;
    private javax.swing.JTextField txtIdUsuario;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNomeUsuario;
    private javax.swing.JTextField txtNumeroProcesso;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the usuario
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param usuario the usuario to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return the distribuicao
     */
    public Distribuicao getDistribuicao() {
        return distribuicao;
    }

    /**
     * @param distribuicao the distribuicao to set
     */
    public void setDistribuicao(Distribuicao distribuicao) {
        this.distribuicao = distribuicao;
        txtNumeroProcesso.setText(distribuicao.getProcesso().getNumProcesso());
        txtIdDistribuicao.setText(distribuicao.getId().toString());
        txtNomeRequerente.setText(distribuicao.getPessoa().getNome());
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
    }

    /**
     * @return the id
     */
    public Integer getIdProcesso() {
        return idProcesso;
    }

    /**
     * @param id the id to set
     */
    public void setIdProcesso(Integer id) {
        this.idProcesso = id;
    }

    /**
     * @return the idFiscalizacao
     */
    public String getIdFiscalizacao() {
        return idFiscalizacao;
    }

    /**
     * @param idFiscalizacao the idFiscalizacao to set
     */
    public void setIdFiscalizacao(String idFiscalizacao) {
        this.idFiscalizacao = idFiscalizacao;
    }

    /**
     * @return the SituacaoParecer
     */
    public String getSituacaoParecer() {
        return SituacaoParecer;
    }

    /**
     * @param SituacaoParecer the SituacaoParecer to set
     */
    public void setSituacaoParecer(String SituacaoParecer) {
        this.SituacaoParecer = SituacaoParecer;

    }
    /**
     * @return the id
     */
}
