/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.emissaolicenca;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Calendar;
import sip.juridico.Juridico;
import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.processo.ProcessoBD;
import sip.processo.ProcessoFrame;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.Constantes;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;

import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileWriter;
import java.lang.NullPointerException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.border.LineBorder;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import sip.juridico.JuridicoBD;
import sip.juridico.JuridicoFrame;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class EmissaoLicencaFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private JDateChooser jdcDataLancamento;
    private JDateChooser jdcDataTramite;
    private JDateChooser jdcDataTeste;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<EmissaoLicenca> listEmissaoLicenca;
    private EmissaoLicenca emissaoLicenca;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoExcluirAnexo;
    private Processo processo;
    private List<Processo> listProcesso;
    private List<Juridico> listJuridico;
    private Juridico juridico;
    private Usuario usuario;
    private List<Usuario> listUsuario;
    String idDigitador = Menu.id_Usuario;
    String vizinho = "Sim";
    String convocacao = "Não";
    String emitido = "não";
    String layout = "Nenhum";
    private int tamLoteDetalhe;
    private JFormattedTextField JFormattedTextField;
    EmissaoLicencaBD emiBD = new EmissaoLicencaBD();
    String impressaoDuplex;
    private String nomeArquivo;
    private String caminhoArquivo;
    JasperPrint jp = null;

    public EmissaoLicencaFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idDigitador);
        jdcDataLancamento.setEnabled(false);
        formataDataTramitacao();
        setaDataAtualTramite();
        ftfMesTramite.setVisible(false);
        jlClipe.setVisible(false);
        jPanel23.setVisible(false);


       int id = emiBD.iD;
        //ExecutaAtalhoBtnExcluirAlterar();
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

    private void ExecutaAtalhoBtnExcluirAlterar() {
        tblEmissao.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected,
                        hasFocus, row, column);
                if (row == 2) {
                    setBackground(Color.LIGHT_GRAY);
                } else {
                    setBackground(null);
                }
                return this;
            }
        });
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

                if (senha.equals("admsip")) {
                    btnExcluir.setEnabled(true);
                    btnAlterar.setEnabled(true);
                } else {
                    btnExcluir.setEnabled(false);
                    btnAlterar.setEnabled(false);
                }
            }
        });
    }

    private void formataDataTramitacao() {

        //jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTeste = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
    }

    private void setaDataAtualTramite() {
        java.util.Date hoje = new java.util.Date();
        jdcDataLancamento.setDate(hoje);
        
    }

    private void formataData() {

        jdcDataLancamento = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataLancamento.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcDataLancamento.setBackground(Color.WHITE);
        jdcDataLancamento.setBounds(255, 91, 87, 20);

        jPanel6.add(jdcDataLancamento);
    }
    DefaultTableCellRenderer colorRenderer = new DefaultTableCellRenderer() {

        @Override
        public void setValue(Object value) {
            switch ((String) value) {
                case "Sim":
                    setBackground(Color.YELLOW);

                    break;
                case "Não":
                    setBackground(Color.WHITE);

                    break;
            }
            super.setValue(value);
            setForeground(Color.BLACK);
        }
    };

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblEmissao.getModel();
        listModel = tblEmissao.getSelectionModel();
        //tblEmissao.setDefaultRenderer(Object.class, new ColorirTabelaEmissaoTitulo());
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheEmissao();
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
            ftfAreaTerreno.setFormatterFactory(new DefaultFormatterFactory(formatterValor));
            ftfAreaConstruida.setFormatterFactory(new DefaultFormatterFactory(formatterValor));

            MaskFormatter mascaraHora = new MaskFormatter("##:##");
            //mascaraTel.setValueContainsLiteralCharacters(false);
            mascaraHora.setValidCharacters("0123456789:;");
            DefaultFormatterFactory formatterHora = new DefaultFormatterFactory(mascaraHora);
            ftfHoraInicio.setFormatterFactory(formatterHora);
            ftfHoraTermino.setFormatterFactory(formatterHora);


            tblEmissao.getColumnModel().getColumn(0).setPreferredWidth(100);//1
            tblEmissao.getColumnModel().getColumn(1).setPreferredWidth(150);//2
            tblEmissao.getColumnModel().getColumn(2).setPreferredWidth(500);//3
            tblEmissao.getColumnModel().getColumn(3).setPreferredWidth(300);//4
            tblEmissao.getColumnModel().getColumn(4).setPreferredWidth(120);//5
            tblEmissao.getColumnModel().getColumn(5).setPreferredWidth(70);//5
            tblEmissao.getColumnModel().getColumn(6).setPreferredWidth(120);//5
            tblEmissao.getColumnModel().getColumn(7).setPreferredWidth(250);//5


            //tblEmissao.getColumnModel().getColumn(4).setCellRenderer(colorRenderer);
        } catch (ParseException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        String dataValidade = "";
        EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();

        if (txtFiltroProcesso.getText().equals("")) {
            listEmissaoLicenca = emissaoBD.consultaEmissao();
        } else {
            String valor = txtFiltroProcesso.getText();
            listEmissaoLicenca = emissaoBD.consultaEmissaoPro(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listEmissaoLicenca.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            if (listEmissaoLicenca.get(i).getDataValidade() != null) {
                dataValidade = simpleDateFormat.format(listEmissaoLicenca.get(i).getDataValidade());
            }

            tableModel.insertRow(i, new Object[]{
                        listEmissaoLicenca.get(i).getId(),
                        listEmissaoLicenca.get(i).getProcesso().getNumProcesso(),
                        listEmissaoLicenca.get(i).getProcesso().getTipoLicenca(),
                        listEmissaoLicenca.get(i).getRequerente().getNome(),
                        simpleDateFormat.format(listEmissaoLicenca.get(i).getDataEmissao()),
                        listEmissaoLicenca.get(i).getEmitido(),
                        dataValidade,
                        listEmissaoLicenca.get(i).getUsuario().getNome()});
        }
    }

    private void executaRelatorioDespacho() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblEmissao.getSelectedRow();

        int mostraID = listEmissaoLicenca.get(indice).getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("ID_EMISSAO", (long) mostraID);
            parametros.put("CAMINHO_IMAGEM1", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("CAMINHO_IMAGEM2", System.getProperty("user.dir") + "\\imagem\\logoserf.jpg");
            parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");

            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\Despacho.jasper", parametros, acessoBd.conectar());
            JOptionPane.showMessageDialog(this, "Visualização gerada!", "Informação", JOptionPane.INFORMATION_MESSAGE);

            JasperViewer.viewReport(jp, false);

            //alteraTituloImpresso();

        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorioLicenca() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblEmissao.getSelectedRow();
        int mostraID = listEmissaoLicenca.get(indice).getId();
        String tipoLicenca = getProcesso().getTipoLicenca();
 alteraEmitido();
        try {
            HashMap parametros = new HashMap();
            parametros.put("ID_EMISSAO", (long) mostraID);
            parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("CAMINHO_MARCADAGUA", System.getProperty("user.dir") + "\\imagem\\logocidadeopaco.jpg");
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\Licenca.jasper", parametros, acessoBd.conectar());

            /*switch (tipoLicenca) {
            case "LICENÇA DE OPERAÇÃO":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "LICENÇA DE INSTALAÇÃO":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\li\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\li\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "LICENÇA PRÉVIA":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lp\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lp\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "LICENÇA ÚNICA":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lu\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lu\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "LICENÇA AMBIENTAL DE REGULARIZAÇÃO":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "PRORROGAÇÃO DE LICENÇA DE OPERAÇÃO":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            case "RENOVAÇÃO DE LICENÇA DE OPERAÇÃO":
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\emissao\\lo\\Licenca.jasper", parametros, acessoBd.conectar());
            break;
            }*/
           
            
            //JRDocxExporter exporter = new JRDocxExporter();
            //exporter.setExporterInput(new SimpleExporterInput(jp));      
            //File exportReportFile = new File("teste" + ".docx");
            //exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exportReportFile));
            //exporter.exportReport();

            //alteraTituloImpresso();

        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheEmissao() {
        if (tblEmissao.getSelectedRow() != -1) {
            int indice = tblEmissao.getSelectedRow();

            if (listEmissaoLicenca.get(indice).getJuridico().getId() != 0) {
                txtIdJuridico.setText(listEmissaoLicenca.get(indice).getJuridico().getId().toString());
                txtNumeroProcJur.setText(listEmissaoLicenca.get(indice).getProcesso().getNumProcesso());
                txtNomeRequerente.setText(listEmissaoLicenca.get(indice).getRequerente().getNome());

                txtIdProcesso.setText("");
                txtNumeroProcesso.setText("");
            } else {
                txtIdJuridico.setText("");
                txtNumeroProcJur.setText("");

                txtIdProcesso.setText(listEmissaoLicenca.get(indice).getProcesso().getId().toString());
                txtNumeroProcesso.setText(listEmissaoLicenca.get(indice).getProcesso().getNumProcesso());
                txtNomeRequerente.setText(listEmissaoLicenca.get(indice).getRequerente().getNome());
            }


            jdcDataLancamento.setDate(listEmissaoLicenca.get(indice).getDataEmissao());
            ftfMes.setText(listEmissaoLicenca.get(indice).getMesAno());
            ftfAreaTerreno.setValue(listEmissaoLicenca.get(indice).getAreaTerreno());
            ftfAreaConstruida.setValue(listEmissaoLicenca.get(indice).getAreaConstruida());
            ftfHoraInicio.setValue(listEmissaoLicenca.get(indice).getHoraInicial());
            ftfHoraTermino.setValue(listEmissaoLicenca.get(indice).getHoraFinal());
            //txtControleForm.setText(listEmissaoTitulo.get(indice).getControleForm());

            //txtArquivo.setText(listEmissaoTitulo.get(indice).getNomeArquivo());

        } else {
            //txtIdProcesso.setText("");
            //txtNumeroProcesso.setText("");
            //txtIdLote.setText("");
            //txtNomeLote.setText("");
            //ftfMes.setText("");
            //jdcData.setDate(null);
        }
    }

    private void limpaCampos() {
        {
            processo = null;
            txtIdJuridico.setText("");
            txtNumeroProcJur.setText("");
            txtNomeRequerente.setText("");
            jdcDataLancamento.setDate(null);
            ftfMes.setText("");
            ftfAreaTerreno.setValue(null);
            ftfAreaConstruida.setValue(null);
            ftfHoraInicio.setValue(null);
            ftfHoraTermino.setValue(null);
            ftfMesTramite.setText("");

        }
    }

    private void executaTramitacaoEmitido() throws ParseException {
        buscaEmissao(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());
        
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());
        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataLancamento.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());
                
        String controle = getProcesso().getId() + " " + "LICENÇA EMITIDA" + " " + "LICENCIAMENTO";
        //String controle = getJuridico().getProcesso().getId() + " " + "LICENÇA EMITIDA" + " " + "LICENCIAMENTO";


        tram.setUsuario(getUsuario());
        tram.setProcesso(getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("LICENÇA EMITIDA");
        tram.setParecer(" ");
        tram.setSetor("LICENCIAMENTO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setEmissaoLicenca(getEmissaoLicenca());


        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {

            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            JasperViewer.viewReport(jp, false);
            atualizaTabela();
            //desabilitaBotoes();
            //desabilitaCampos();
            //limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
            JasperViewer.viewReport(jp, false);
        }
    }

  
    private void incluiLicenca() {
        /*if (getJuridico() == null) {
        JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Analise", JOptionPane.INFORMATION_MESSAGE);
        txtIdJuridico.requestFocus();
        } else*/ if (jdcDataLancamento.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            EmissaoLicenca emissao = new EmissaoLicenca();

            ftfMes.setValue(jdcDataLancamento.getDate());
            //ftfAno.setValue(jdcData.getDate());
            //exemplo do controle = 1212/2015


            //String controle = ftfAno.getText();

            emissao.setUsuario(getUsuario());
            emissao.setJuridico(getJuridico());
            emissao.setDataEmissao(new java.sql.Date(((java.util.Date) jdcDataLancamento.getDate()).getTime()));
            emissao.setMesAno(ftfMes.getText());
            emissao.setAreaTerreno((Double) ftfAreaTerreno.getValue());
            emissao.setAreaConstruida((Double) ftfAreaConstruida.getValue());
            emissao.setHoraInicial(ftfHoraInicio.getText().trim());
            emissao.setHoraFinal(ftfHoraTermino.getText().trim());
            emissao.setEmitido(emitido);
            emissao.setProcesso(getProcesso());

            EmissaoLicencaBD EmissaoBD = new EmissaoLicencaBD();
            if (EmissaoBD.incluiEmissaoLicenca(emissao)) {
                JOptionPane.showMessageDialog(this, "Licença cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                //executaTramitacaoLancamento();
                copiarArquivo();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
                nomeArquivo = null;
                caminhoArquivo = null;

                //atualizaId();
            } else {
                JOptionPane.showMessageDialog(this, "Processo já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraLicenca() {
        if (getJuridico() == null) {
            JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Analise", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
        } else if (jdcDataLancamento.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {
            //buscaEmissao(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());

            EmissaoLicenca emissao = new EmissaoLicenca();
            ftfMes.setValue(jdcDataLancamento.getDate());
            //ftfAno.setValue(jdcData.getDate());
            //String controle = ftfAno.getText();

            emissao.setId(this.listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());
            emissao.setUsuario(getUsuario());
            emissao.setJuridico(getJuridico());
            emissao.setDataEmissao(new java.sql.Date(((java.util.Date) jdcDataLancamento.getDate()).getTime()));
            emissao.setMesAno(ftfMes.getText());
            emissao.setAreaTerreno((Double) ftfAreaTerreno.getValue());
            emissao.setAreaConstruida((Double) ftfAreaConstruida.getValue());
            emissao.setHoraInicial(ftfHoraInicio.getText().trim());
            emissao.setHoraFinal(ftfHoraTermino.getText().trim());
            //emissao.setEmitido(emitido);
            //emissao.setProcesso(getProcesso());


            EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();

            if (emissaoBD.alteraEmissaoLicenca(emissao)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                copiarArquivo();
                atualizaTabela();
                tblEmissao.setEnabled(true);
                desabilitaBotoes();
                desabilitaCampos();
                nomeArquivo = null;
                caminhoArquivo = null;

            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraTituloExluirAnexo() {
        buscaEmissao(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());

        EmissaoLicenca emissao = new EmissaoLicenca();
        ftfMes.setValue(jdcDataLancamento.getDate());
        //ftfAno.setValue(jdcData.getDate());
        //String controle = ftfAno.getText();
        //emissao.setId(setEmissaoLicenca().getId());
        emissao.setUsuario(getUsuario());

        emissao.setProcesso(getProcesso());
        emissao.setDataEmissao(new java.sql.Date(((java.util.Date) jdcDataLancamento.getDate()).getTime()));
        emissao.setMesAno(ftfMes.getText());
        emissao.setUsuario(getUsuario());

        EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();

        if (emissaoBD.alteraEmissaoLicenca(emissao)) {
            JOptionPane.showMessageDialog(this, "Dados alterados!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void alteraEmitido() {
        Date dataDoUsuario = listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getDataEmissao();
      
        Calendar c = Calendar.getInstance();//Instancia a classe Calendar.
        c.setTime(dataDoUsuario);//Altera a data atual,pela sua data
        c.set(Calendar.DAY_OF_MONTH,c.get(Calendar.DAY_OF_MONTH)+365);//Altera a data para +1 dia.
        jdcDataTeste.setDate(c.getTime());

        emitido = "sim";

        EmissaoLicenca emissao = new EmissaoLicenca();
        emissao.setId(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());
        emissao.setEmitido(this.emitido);
        emissao.setDataValidade(new java.sql.Date(((java.util.Date) jdcDataTeste.getDate()).getTime()));

        EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();
        if (emissaoBD.alteraEmissaoLicencaEmitidoValidade(emissao)) {
            try {
                executaTramitacaoEmitido();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Erro ao inserir a validade!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluiTitulo() {

        EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();
        if (emissaoBD.excluiEmissaoTitulo(listEmissaoLicenca.get(tblEmissao.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do processo excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            //atualizaId();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Existe relacionamento com outra tabela!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void copiarArquivo() {
        try {

            //copiar arquivo para servidor
            Path source = Paths.get(caminhoArquivo);//("C:\\Temp\\source.txt");
            Path destination = Paths.get("\\\\192.168.13.200\\sip\\arquivos\\" + nomeArquivo);//("C:\\diretorio\\"+nomeArquivo);
            if (destination.toFile().exists()) {
                destination.toFile().delete();
            }
            Files.copy(source, destination);

        } catch (NullPointerException | HeadlessException | IOException erro) {
            //JOptionPane.showMessageDialog(null, "Nao foi possível carregar arquivo.");
        }
    }

    private void caixaAlta() {
    }

    private void habilitaCampos() {

        txtIdJuridico.setEditable(true);
        txtIdProcesso.setEditable(true);
        btnSelecionaJuridico.setEnabled(true);
        btnSelecionaProcesso.setEnabled(true);
        jdcDataLancamento.setEnabled(true);
        ftfAreaTerreno.setEditable(true);
        ftfAreaConstruida.setEditable(true);
        ftfHoraInicio.setEditable(true);
        ftfHoraTermino.setEditable(true);
    }

    private void desabilitaCampos() {

        txtIdJuridico.setEditable(false);
        txtIdProcesso.setEditable(false);
        btnSelecionaJuridico.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        jdcDataLancamento.setEnabled(false);
        ftfAreaTerreno.setEditable(false);
        ftfAreaConstruida.setEditable(false);
        ftfHoraInicio.setEditable(false);
        ftfHoraTermino.setEditable(false);
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

    private void centralizaJanela(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 6,
                (this.getHeight() - internalFrame.getHeight()) / 2);
    }

    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcDataLancamento.setDate(hoje);
    }

    private void anulaProcesso() {
        txtIdProcesso.setText("");
        txtNumeroProcesso.setText("");
        processo = null;
    }

    private void anulaJuridico() {
        txtIdJuridico.setText("");
        txtNumeroProcJur.setText("");
        juridico = null;
    }

    private void buscaProcesso() {
        ProcessoBD processoBD = new ProcessoBD();
        listProcesso = processoBD.consultaProcesso("ProcessoFrame");
        Integer indice = tblEmissao.getSelectedRow();
        Integer id = listEmissaoLicenca.get(indice).getProcesso().getId();
        int binario = 0;
        try {
            int max = listProcesso.size();
            int id_busca = listEmissaoLicenca.get(indice).getProcesso().getId();
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
                txtNumeroProcJur.setText("");
                setProcesso(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setProcesso(null);
            txtNumeroProcJur.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaProcessoCampo() {
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
                anulaProcesso();
                txtNomeRequerente.setText("");
            } else {
                anulaJuridico();
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            anulaProcesso();
            txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaJuridico() {
        JuridicoBD analiseBD = new JuridicoBD();
        listJuridico = analiseBD.consultaJuridico();
        int binario = 0;
        try {
            int max = listJuridico.size();
            int id_busca = Integer.parseInt(txtIdJuridico.getText());
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listJuridico.get(i).getId();
                if (listJuridico.get(i).getId() == id_busca) {
                    setJuridico(listJuridico.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                anulaJuridico();
                txtNomeRequerente.setText("");
            } else {
                anulaProcesso();
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            anulaJuridico();
            txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaTramitacao(String id) {
        EmissaoLicencaBD tramitacaoBD = new EmissaoLicencaBD();
        //listEmissaoTitulo = tramitacaoBD.consultaTramUltimoIdProce(id);
        int binario = 0;
        try {
            int max = listEmissaoLicenca.size();
            int id_busca = Integer.parseInt(id);
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listEmissaoLicenca.get(i).getProcesso().getId();
                if (listEmissaoLicenca.get(i).getProcesso().getId() == id_busca) {
                    setTramitacao(listEmissaoLicenca.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                //JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdProcesso.setText("");
                //txtNumeroProcesso.setText("");
                setTramitacao(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setTramitacao(null);
            txtNumeroProcJur.setText("");
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

    public void buscaEmissao(int id) {
        EmissaoLicencaBD emissaoBD = new EmissaoLicencaBD();
        listEmissaoLicenca = emissaoBD.consultaEmissao();
        int binario = 0;
        try {
            int max = listEmissaoLicenca.size();
            int id_busca = id;
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listEmissaoLicenca.get(i).getId();
                if (listEmissaoLicenca.get(i).getId() == id_busca) {
                    setEmissaoLicenca(listEmissaoLicenca.get(i));
                    binario = 1;

                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdProcesso.setText("");
                //txtNumeroProcesso.setText("");
                setEmissaoTitulo(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setEmissaoTitulo(null);
            //txtNumeroProcesso.setText("");
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

        buttonGroupValor = new javax.swing.ButtonGroup();
        buttonGroupTamLote = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlClipe = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblEmissao = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        btnDespacho = new javax.swing.JButton();
        btnImprimir3 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        ftfMesTramite = new javax.swing.JFormattedTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        txtNomeRequerente = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtNumeroProcJur = new javax.swing.JTextField();
        btnSelecionaJuridico = new javax.swing.JButton();
        txtIdJuridico = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        ftfAreaTerreno = new javax.swing.JFormattedTextField();
        ftfAreaConstruida = new javax.swing.JFormattedTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ftfHoraTermino = new javax.swing.JFormattedTextField();
        ftfHoraInicio = new javax.swing.JFormattedTextField();
        jPanel23 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btnSelecionaProcesso = new javax.swing.JButton();
        txtIdProcesso = new javax.swing.JTextField();
        txtNumeroProcesso = new javax.swing.JTextField();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Emissão Titulo\n");
        setPreferredSize(new java.awt.Dimension(1050, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(191, 205, 219));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/titulo_64x64.png"))); // NOI18N
        jLabel1.setText("Emissão Licença");
        jPanel1.add(jLabel1);

        jlClipe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/clipe64x64.png"))); // NOI18N
        jPanel1.add(jlClipe);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Nome / Processo");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

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

        tblEmissao.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        tblEmissao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Processo", "Licença", "Requerente", "Data", "Emitido", "Validade", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false, true, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblEmissao.setSelectionBackground(new java.awt.Color(51, 51, 255));
        tblEmissao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblEmissao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblEmissaoMouseClicked(evt);
            }
        });
        tblEmissao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblEmissaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblEmissao);

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

        btnImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/printer.png"))); // NOI18N
        btnImprimir.setText("EMITIR + TRAMITAR");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir);

        btnDespacho.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/search.png"))); // NOI18N
        btnDespacho.setText("Visualizar Despacho");
        btnDespacho.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDespachoActionPerformed(evt);
            }
        });
        jPanel4.add(btnDespacho);

        btnImprimir3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/search.png"))); // NOI18N
        btnImprimir3.setText("Visualizar Titulo");
        btnImprimir3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimir3ActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data "));
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
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel13.add(jPanel6, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerente"));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        txtNomeRequerente.setEditable(false);
        txtNomeRequerente.setFont(new java.awt.Font("Verdana", 1, 12));
        txtNomeRequerente.setEnabled(false);
        txtNomeRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeRequerenteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(txtNomeRequerente, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel22, gridBagConstraints);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Juridico"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel18, gridBagConstraints);

        txtNumeroProcJur.setEditable(false);
        txtNumeroProcJur.setFont(new java.awt.Font("Verdana", 1, 12));
        txtNumeroProcJur.setEnabled(false);
        txtNumeroProcJur.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNumeroProcJurActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(txtNumeroProcJur, gridBagConstraints);

        btnSelecionaJuridico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaJuridico.setEnabled(false);
        btnSelecionaJuridico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaJuridicoActionPerformed(evt);
            }
        });
        btnSelecionaJuridico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnSelecionaJuridicoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaJuridico, gridBagConstraints);

        txtIdJuridico.setEditable(false);
        txtIdJuridico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtIdJuridicoMouseExited(evt);
            }
        });
        txtIdJuridico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdJuridicoActionPerformed(evt);
            }
        });
        txtIdJuridico.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdJuridicoFocusLost(evt);
            }
        });
        txtIdJuridico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdJuridicoKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdJuridicoKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdJuridicoKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdJuridico, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel20, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Medidas do Terreno"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel14.setText("Area Terreno");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel14, gridBagConstraints);

        jLabel15.setText("Medida Direita");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel15, gridBagConstraints);

        ftfAreaTerreno.setEditable(false);
        ftfAreaTerreno.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(java.text.NumberFormat.getIntegerInstance())));
        ftfAreaTerreno.setMaximumSize(new java.awt.Dimension(1, 1));
        ftfAreaTerreno.setMinimumSize(new java.awt.Dimension(1, 20));
        ftfAreaTerreno.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfAreaTerrenoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(ftfAreaTerreno, gridBagConstraints);

        ftfAreaConstruida.setEditable(false);
        ftfAreaConstruida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfAreaConstruidaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(ftfAreaConstruida, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel5, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Horário de Funcionamento"));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel17.setText("Inicio");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel17, gridBagConstraints);

        jLabel19.setText("Termino");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel19, gridBagConstraints);

        ftfHoraTermino.setEditable(false);
        ftfHoraTermino.setToolTipText("Horas valida 00:00:00 as 23:59:59");
        ftfHoraTermino.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ftfHoraTerminoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfHoraTermino, gridBagConstraints);

        ftfHoraInicio.setEditable(false);
        ftfHoraInicio.setToolTipText("Horas valida 00:00:00 as 23:59:59");
        ftfHoraInicio.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                ftfHoraInicioFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfHoraInicio, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel7, gridBagConstraints);

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
        txtNumeroProcesso.setFont(new java.awt.Font("Verdana", 1, 12));
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
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 380;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel23, gridBagConstraints);

        jTabbedPane1.addTab("Dados", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            atualizaTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Sem dados do imovel!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

        txtFiltroProcesso.setText("");
    }

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {

        EmissaoLicencaBD tramitacaoBD = new EmissaoLicencaBD();
        if (tramitacaoBD.testaConexao()) {
            limpaSelecaoTabela();
            limpaCampos();
            habilitaCampos();
            btnFiltrar.setEnabled(false);
            tblEmissao.setEnabled(false);
            limpaSelecaoTabela();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            caixaAlta();
            setaDataAtual();
            buscaJuridico();
            buscaUsuario(idDigitador);


        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        if (modo == Constantes.INSERT_MODE) {
            incluiLicenca();
            tblEmissao.setEnabled(true);
            // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraLicenca();
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblEmissao.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if ("sim".equals(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getEmitido())) {
            JOptionPane.showMessageDialog(this, "Processo Emitido não pode ser Alterado\nConsulte o Adm do Sistema!", "Tramitaçao", JOptionPane.INFORMATION_MESSAGE);
        } else {
            if (tblEmissao.getSelectedRow() != -1) {
                habilitaCampos();
                txtIdJuridico.setEditable(false);
                btnSelecionaJuridico.setEnabled(false);
                habilitaBotoes();
                modo = Constantes.EDIT_MODE;
                buscaJuridico();

                tblEmissao.setEnabled(false);

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma tramitação da lista!", "Tramitaçao", JOptionPane.INFORMATION_MESSAGE);
            }
        }

    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        if (tblEmissao.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da tramitação?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiTitulo();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma tramitação da lista!", "Tramitação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void txtFiltroProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcessoActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoActionPerformed

    private void tblEmissaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblEmissaoMouseClicked
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
    }//GEN-LAST:event_tblEmissaoMouseClicked

    private void tblEmissaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblEmissaoKeyPressed
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
    }//GEN-LAST:event_tblEmissaoKeyPressed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        atualizaTabela();

    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        if (tblEmissao.getSelectedRow() != -1) {
            int indice = tblEmissao.getSelectedRow();
            buscaJuridico();
            buscaProcesso();
            if("NAO".equals(listEmissaoLicenca.get(indice).getProcesso().getArquivado())){
                executaRelatorioLicenca();
            }else{
                  JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
              }            
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void btnDespachoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDespachoActionPerformed
        if (tblEmissao.getSelectedRow() != -1) {
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnDespachoActionPerformed

    private void btnImprimir3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir3ActionPerformed
        if (tblEmissao.getSelectedRow() != -1) {

            executaRelatorioLicenca();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnImprimir3ActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void txtNumeroProcJurActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcJurActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNumeroProcJurActionPerformed

    private void btnSelecionaJuridicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaJuridicoActionPerformed
        JuridicoFrame juridicoFrame = new JuridicoFrame(this);
        juridicoFrame.setVisible(true);
        this.getDesktopPane().add(juridicoFrame);
        juridicoFrame.toFront();
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaJuridicoActionPerformed

    private void btnSelecionaJuridicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaJuridicoFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaJuridicoFocusLost

    private void txtIdJuridicoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIdJuridicoMouseExited
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoMouseExited

    private void txtIdJuridicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdJuridicoActionPerformed
        buscaJuridico();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoActionPerformed

    private void txtIdJuridicoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdJuridicoFocusLost
        buscaJuridico();
        //buscaTramitacao(txtIdProcesso.getText());
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoFocusLost

    private void txtIdJuridicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdJuridicoKeyPressed
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoKeyPressed

    private void txtIdJuridicoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdJuridicoKeyReleased
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoKeyReleased

    private void txtIdJuridicoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdJuridicoKeyTyped
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdJuridicoKeyTyped

    private void ftfAreaTerrenoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfAreaTerrenoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfAreaTerrenoActionPerformed

    private void ftfAreaConstruidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfAreaConstruidaActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfAreaConstruidaActionPerformed

    private void ftfHoraTerminoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ftfHoraTerminoFocusLost
}//GEN-LAST:event_ftfHoraTerminoFocusLost

    private void ftfHoraInicioFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_ftfHoraInicioFocusLost
}//GEN-LAST:event_ftfHoraInicioFocusLost

    private void btnSelecionaProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaProcessoActionPerformed
        ProcessoFrame processoFrame = new ProcessoFrame(this);
        processoFrame.setVisible(true);
        this.getDesktopPane().add(processoFrame);
        processoFrame.toFront();
        processoFrame.txtFiltroProcesso.requestFocus();
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaProcessoActionPerformed

    private void txtIdProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdProcessoFocusLost
        buscaProcessoCampo();  // TODO add your handling code here:
}//GEN-LAST:event_txtIdProcessoFocusLost

    private void txtNumeroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNumeroProcessoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNumeroProcessoActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnDespacho;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnImprimir3;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaJuridico;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.ButtonGroup buttonGroupTamLote;
    private javax.swing.ButtonGroup buttonGroupValor;
    private javax.swing.JFormattedTextField ftfAreaConstruida;
    private javax.swing.JFormattedTextField ftfAreaTerreno;
    private javax.swing.JFormattedTextField ftfHoraInicio;
    private javax.swing.JFormattedTextField ftfHoraTermino;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlClipe;
    private javax.swing.JTable tblEmissao;
    private javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtIdJuridico;
    public javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNumeroProcJur;
    private javax.swing.JTextField txtNumeroProcesso;
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
        txtNomeRequerente.setText(processo.getRequerente().getNome());
    }

    /**
     * @return the loteTitulacao
     */
    /**
     * @return the tramitacao
     */
    /**
     * @param tramitacao the tramitacao to set
     */
    public void setTramitacao(EmissaoLicenca emissao) {
        this.setEmissaoTitulo(emissao);

        //jdcData.setDate(tramitacao.getDataTramitacao());
        ftfMes.setText(emissao.getMesAno());
        //txtaObservacao.setText(tramitacao.getConvocacao());

    }

    /**
     * @return the emissaoTitulo
     */
    /**
     * @param emissaoTitulo the emissaoTitulo to set
     */
    public void setEmissaoTitulo(EmissaoLicenca emissaoTitulo) {
        this.setEmissaoLicenca(emissaoTitulo);
    }

    /**
     * @return the juridico
     */
    public Juridico getJuridico() {
        return juridico;
    }

    /**
     * @param juridico the juridico to set
     */
    public void setJuridico(Juridico juridico) {
        this.juridico = juridico;
        txtIdJuridico.setText(juridico.getId().toString());
        txtNumeroProcJur.setText(juridico.getProcesso().getNumProcesso());
        txtNomeRequerente.setText(juridico.getRequerente().getNome());
    }

    /**
     * @return the emissaoLicenca
     */
    public EmissaoLicenca getEmissaoLicenca() {
        return emissaoLicenca;
    }

    /**
     * @param emissaoLicenca the emissaoLicenca to set
     */
    public void setEmissaoLicenca(EmissaoLicenca emissaoLicenca) {
        this.emissaoLicenca = emissaoLicenca;
    }
}
