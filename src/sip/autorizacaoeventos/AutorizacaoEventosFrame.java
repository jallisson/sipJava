/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.autorizacaoeventos;

import java.awt.HeadlessException;
import java.io.IOException;
import java.util.Calendar;
import sip.bairro.Bairro;
import sip.juridico.Juridico;
import sip.logradouro.Logradouro;
import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.processo.ProcessoBD;
import sip.processo.ProcessoFrame;
import sip.tipoevento.TipoEvento;
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
import javax.swing.Action;
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
import net.sf.jasperreports.view.JasperViewer;
import sip.bairro.BairroBD;
import sip.bairro.BairroFrame;
import sip.juridico.JuridicoBD;
import sip.juridico.JuridicoFrame;
import sip.localevento.LocalEvento;
import sip.localevento.LocalEventoBD;
import sip.localevento.LocalEventoFrame;
import sip.logradouro.LogradouroBD;
import sip.logradouro.LogradouroFrame;
import sip.tipoevento.TipoEventoBD;
import sip.tipoevento.TipoEventoFrame;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class AutorizacaoEventosFrame extends javax.swing.JInternalFrame {

    /**
     * @return the localEvento
     */
    AcessoBD conClientes;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private JDateChooser jdcDataTeste;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<AutorizacaoEventos> listAutorizacaoEventos;
    private AutorizacaoEventos autorizacaoEventos;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoExcluirAnexo;
    private Processo processo;
    private List<Processo> listProcesso;
    private List<Juridico> listJuridico;
    private Juridico juridico;
    private Usuario usuario;
    private List<Usuario> listUsuario;
    String idUsuario = Menu.id_Usuario;
    String vizinho = "Sim";
    String convocacao = "Não";
    String emitido = "não";
    String layout = "Nenhum";
    private int tamLoteDetalhe;
    private JFormattedTextField JFormattedTextField;
    AutorizacaoEventosBD emiBD = new AutorizacaoEventosBD();
    String impressaoDuplex;
    private String nomeArquivo;
    private String caminhoArquivo;
    JasperPrint jp = null;
    private List<Logradouro> listLogradouro;
    private Logradouro logradouro;
    private List<LocalEvento> listLocalEvento;
    private LocalEvento localEvento;
    private List<Bairro> listBairro;
    private Bairro bairro;
    private List<TipoEvento> listTipoEvento;
    private TipoEvento tipoEvento;

    public AutorizacaoEventosFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idUsuario);
        jdcData.setEnabled(false);
        formataDataTramitacao();
        //setaDataAtualTramite();
        ftfMesTramite.setVisible(false);
        jlClipe.setVisible(false);
        btnDespacho.setVisible(false);
        btnImprimir3.setVisible(false);
        jRBNenhum.setVisible(false);
        //jPanel23.setVisible(false);
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
        tblAutorizacao.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {

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
        jdcDataTramite.setDate(hoje);
    }

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel6.add(jdcData);
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

    private void controlaLocalEvento() {
        if (jRBLogradouro.isSelected()) {
            txtIdLogradouro.setEditable(true);
            btnSelecionaRua.setEnabled(true);
        } else {
            txtIdLogradouro.setEditable(false);
            btnSelecionaRua.setEnabled(false);
        }
        if (jRBImovel.isSelected()) {
            txtIdLocalEvento.setEditable(true);
            btnSelecionaLocalEvento.setEnabled(true);
        } else {
            txtIdLocalEvento.setEditable(false);
            btnSelecionaLocalEvento.setEnabled(false);
        }
        if (jRBNenhum.isSelected()) {
            txtIdLocalEvento.setEditable(false);
            btnSelecionaLocalEvento.setEnabled(false);
            txtIdLogradouro.setEditable(false);
            btnSelecionaRua.setEnabled(false);

        }
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblAutorizacao.getModel();
        listModel = tblAutorizacao.getSelectionModel();
        //tblEmissao.setDefaultRenderer(Object.class, new ColorirTabelaEmissaoTitulo());
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheAutorizacao();
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

            DecimalFormat formatoEstimativa = new DecimalFormat("####");
            NumberFormatter formatterEstoque = new NumberFormatter(formatoEstimativa);
            formatterEstoque.setValueClass(Integer.class);
            ftfEstimativa.setFormatterFactory(new DefaultFormatterFactory(formatterEstoque));

            MaskFormatter mascaraHora = new MaskFormatter("##:##");
            //mascaraTel.setValueContainsLiteralCharacters(false);
            mascaraHora.setValidCharacters("0123456789:;");
            DefaultFormatterFactory formatterHora = new DefaultFormatterFactory(mascaraHora);
            ftfHoraInicio.setFormatterFactory(formatterHora);
            ftfHoraTermino.setFormatterFactory(formatterHora);

            tblAutorizacao.getColumnModel().getColumn(0).setPreferredWidth(100);//1
            tblAutorizacao.getColumnModel().getColumn(1).setPreferredWidth(150);//2
            tblAutorizacao.getColumnModel().getColumn(2).setPreferredWidth(500);//3
            tblAutorizacao.getColumnModel().getColumn(3).setPreferredWidth(300);//4
            tblAutorizacao.getColumnModel().getColumn(4).setPreferredWidth(120);//5
            tblAutorizacao.getColumnModel().getColumn(5).setPreferredWidth(120);//5
            tblAutorizacao.getColumnModel().getColumn(6).setPreferredWidth(250);//5

            //tblEmissao.getColumnModel().getColumn(4).setCellRenderer(colorRenderer);
        } catch (ParseException ex) {
            Logger.getLogger(AutorizacaoEventosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        AutorizacaoEventosBD emissaoBD = new AutorizacaoEventosBD();

        if (txtFiltroProcesso.getText().equals("")) {
            listAutorizacaoEventos = emissaoBD.consultaAutorizacao();
        } else {
            String valor = txtFiltroProcesso.getText();
            listAutorizacaoEventos = emissaoBD.consultaEmissaoPro(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listAutorizacaoEventos.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

            tableModel.insertRow(i, new Object[]{
                listAutorizacaoEventos.get(i).getId(),
                listAutorizacaoEventos.get(i).getProcesso().getNumProcesso(),
                listAutorizacaoEventos.get(i).getProcesso().getTipoLicenca(),
                listAutorizacaoEventos.get(i).getRequerente().getNome(),
                simpleDateFormat.format(listAutorizacaoEventos.get(i).getDataEvento()),
                listAutorizacaoEventos.get(i).getEmitido(),
                listAutorizacaoEventos.get(i).getUsuario().getNome()});
        }
    }

    private void executaRelatorioDespacho() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblAutorizacao.getSelectedRow();

        int mostraID = listAutorizacaoEventos.get(indice).getId();

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
            Logger.getLogger(AutorizacaoEventosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorioAutorizacao() {

        AcessoBD acessoBd = new AcessoBD();
        int indice = tblAutorizacao.getSelectedRow();
        int mostraID = listAutorizacaoEventos.get(indice).getId();
        //String tipoLicenca = getProcesso().getTipoLicenca();

        try {
            HashMap parametros = new HashMap();
            parametros.put("ID_AUTORIZACAO", (long) mostraID);
            parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("CAMINHO_LOGOPREFEITURA", System.getProperty("user.dir") + "\\imagem\\naodrogas.png");
            parametros.put("SUBREPORT_DIR", System.getProperty("user.dir") + "\\relatorios\\autorizacao\\eventos\\");
            jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\autorizacao\\eventos\\Autorizacao.jasper", parametros, acessoBd.conectar());

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
            alteraEmitido();

            //alteraTituloImpresso();
        } catch (JRException ex) {
            Logger.getLogger(AutorizacaoEventosFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheAutorizacao() {
        if (tblAutorizacao.getSelectedRow() != -1) {
            int indice = tblAutorizacao.getSelectedRow();

            if (listAutorizacaoEventos.get(indice).getJuridico().getId() != 0) {
                txtIdJuridico.setText(listAutorizacaoEventos.get(indice).getJuridico().getId().toString());
                txtNumeroProcJur.setText(listAutorizacaoEventos.get(indice).getProcesso().getNumProcesso());
                txtNomeRequerente.setText(listAutorizacaoEventos.get(indice).getRequerente().getNome());

                txtIdProcesso.setText("");
                txtNumeroProcesso.setText("");
            } else {
                txtIdJuridico.setText("");
                txtNumeroProcJur.setText("");

                txtIdProcesso.setText(listAutorizacaoEventos.get(indice).getProcesso().getId().toString());
                txtNumeroProcesso.setText(listAutorizacaoEventos.get(indice).getProcesso().getNumProcesso());
                txtNomeRequerente.setText(listAutorizacaoEventos.get(indice).getRequerente().getNome());
            }

            if (listAutorizacaoEventos.get(indice).getLogradouro().getId() != 0) {
                txtIdLogradouro.setText(listAutorizacaoEventos.get(indice).getLogradouro().getId().toString());
                txtNomeLogradouro.setText(listAutorizacaoEventos.get(indice).getLogradouro().getNome());
                jRBLogradouro.setSelected(true);

                txtIdLocalEvento.setText("");
                txtNomeLocalEvento.setText("");
            } else {
                txtIdLogradouro.setText("");
                txtNomeLogradouro.setText("");

                jRBImovel.setSelected(true);
                txtIdLocalEvento.setText(listAutorizacaoEventos.get(indice).getLocalEvento().getId().toString());
                txtNomeLocalEvento.setText(listAutorizacaoEventos.get(indice).getLocalEvento().getNome());
            }

            txtNomeEvento.setText(listAutorizacaoEventos.get(indice).getNomeEvento());
            txtIdTipoEvento.setText(listAutorizacaoEventos.get(indice).getTipoEvento().getId().toString());
            txtNomeTipoEvento.setText(listAutorizacaoEventos.get(indice).getTipoEvento().getNome());
            txtIdBairro.setText(listAutorizacaoEventos.get(indice).getBairro().getId().toString());
            txtNomeBairro.setText(listAutorizacaoEventos.get(indice).getBairro().getNome());
            cbDescricaoArea.setSelectedItem(listAutorizacaoEventos.get(indice).getDescricaoArea());
            ftfEstimativa.setValue(listAutorizacaoEventos.get(indice).getEstimativa());
            jdcData.setDate(listAutorizacaoEventos.get(indice).getDataEvento());
            ftfMes.setText(listAutorizacaoEventos.get(indice).getMesAno());
            ftfHoraInicio.setValue(listAutorizacaoEventos.get(indice).getHoraInicial());
            ftfHoraTermino.setValue(listAutorizacaoEventos.get(indice).getHoraFinal());

        } else {
            txtIdLogradouro.setText("");
            txtNomeLogradouro.setText("");
            txtIdLocalEvento.setText("");
            txtNomeLocalEvento.setText("");
        }
    }

    private void executaTramitacaoEmitido() throws ParseException {
        buscaAutorizacao(listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getId());

        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        int indice = tblAutorizacao.getSelectedRow();

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());
        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getProcesso().getId() + " " + "AUTORIZAÇÃO PARA EVENTOS EMITIDA" + " " + "LICENCIAMENTO" + listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getId();
        //String controle = getJuridico().getProcesso().getId() + " " + "LICENÇA EMITIDA" + " " + "LICENCIAMENTO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("AUTORIZAÇÃO PARA EVENTOS EMITIDA");
        tram.setParecer(" ");
        tram.setSetor("DMA");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setAutorizacaoEventos(getAutorizacaoEventos());

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

    private void incluiAutorizacao() {
        /*if (getJuridico() == null) {
        JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Analise", JOptionPane.INFORMATION_MESSAGE);
        txtIdJuridico.requestFocus();
        } else*/
        if (getProcesso() == null && getJuridico() == null) {
            JOptionPane.showMessageDialog(this, "O campo processo ou juridico\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
        } else if (txtNomeEvento.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Nome do Evento\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtNomeEvento.requestFocus();
        } else if (getTipoEvento() == null) {
            JOptionPane.showMessageDialog(this, "O Tipo de Evento\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdTipoEvento.requestFocus();
        } else if (ftfHoraInicio.getText().equals("  :  ")) {
            JOptionPane.showMessageDialog(this, "O Campo hora inicial\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfHoraInicio.requestFocus();
        } else if (ftfHoraTermino.getText().equals("  :  ")) {
            JOptionPane.showMessageDialog(this, "O Campo hora Termino\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfHoraTermino.requestFocus();
        } else if (ftfEstimativa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "O Campo Estimativa\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfEstimativa.requestFocus();
        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
            jdcData.requestFocus();
        } else if (getLogradouro() == null && jRBLogradouro.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Local do Evento\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtIdLogradouro.requestFocus();
        } else if (getLocalEvento() == null && jRBImovel.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Local do Evento\ndeve ser preenchido!", "Nome Imovel", JOptionPane.INFORMATION_MESSAGE);
            txtIdLocalEvento.requestFocus();
        } else if (getBairro() == null) {
            JOptionPane.showMessageDialog(this, "O campo Bairro\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdBairro.requestFocus();
        } else {

            AutorizacaoEventos autorizacao = new AutorizacaoEventos();

            ftfMes.setValue(jdcData.getDate());
            //ftfAno.setValue(jdcData.getDate());
            //exemplo do controle = 1212/2015
            //String controle = ftfAno.getText();

            autorizacao.setUsuario(getUsuario());
            autorizacao.setJuridico(getJuridico());
            autorizacao.setProcesso(getProcesso());
            autorizacao.setNomeEvento(txtNomeEvento.getText());
            autorizacao.setTipoEvento(getTipoEvento());
            autorizacao.setLogradouro(getLogradouro());
            autorizacao.setBairro(getBairro());
            autorizacao.setHoraInicial(ftfHoraInicio.getText().trim());
            autorizacao.setHoraFinal(ftfHoraTermino.getText().trim());
            autorizacao.setDescricaoArea(cbDescricaoArea.getSelectedItem().toString());
            autorizacao.setEstimativa((Integer) ftfEstimativa.getValue());
            autorizacao.setDataEvento(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            autorizacao.setMesAno(ftfMes.getText());
            autorizacao.setEmitido(emitido);
            autorizacao.setLocalEvento(getLocalEvento());

            AutorizacaoEventosBD EmissaoBD = new AutorizacaoEventosBD();
            if (EmissaoBD.incluiAutorizacaoEventos(autorizacao)) {
                JOptionPane.showMessageDialog(this, "Autorização salva com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                //executaTramitacaoLancamento();
                jRBImovel.setSelected(false);
                controlaLocalEvento();
                copiarArquivo();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
                nomeArquivo = null;
                caminhoArquivo = null;

                //atualizaId();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraLicenca() {
        if (getProcesso() == null && getJuridico() == null) {
            JOptionPane.showMessageDialog(this, "O campo processo ou juridico\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
        } else if (txtNomeEvento.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Nome do Evento\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtNomeEvento.requestFocus();
        } else if (getTipoEvento() == null) {
            JOptionPane.showMessageDialog(this, "O Tipo de Evento\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdTipoEvento.requestFocus();
        } else if (ftfHoraInicio.getText().equals("  :  ")) {
            JOptionPane.showMessageDialog(this, "O Campo hora inicial\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfHoraInicio.requestFocus();
        } else if (ftfHoraTermino.getText().equals("  :  ")) {
            JOptionPane.showMessageDialog(this, "O Campo hora Termino\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfHoraTermino.requestFocus();
        } else if (ftfEstimativa.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "O Campo Estimativa\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            ftfEstimativa.requestFocus();
        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
            jdcData.requestFocus();
        } else if (getLogradouro() == null && jRBLogradouro.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Local do Evento\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
            txtIdLogradouro.requestFocus();
        } else if (getLocalEvento() == null && jRBImovel.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Local do Evento\ndeve ser preenchido!", "Nome Imovel", JOptionPane.INFORMATION_MESSAGE);
            txtIdLocalEvento.requestFocus();
        } else if (getBairro() == null) {
            JOptionPane.showMessageDialog(this, "O campo Bairro\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdBairro.requestFocus();
        } else {
            //buscaEmissao(listEmissaoLicenca.get(tblEmissao.getSelectedRow()).getId());

            AutorizacaoEventos autorizacao = new AutorizacaoEventos();
            ftfMes.setValue(jdcData.getDate());
            //ftfAno.setValue(jdcData.getDate());
            //String controle = ftfAno.getText();

            autorizacao.setId(this.listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getId());
            autorizacao.setUsuario(getUsuario());
            autorizacao.setNomeEvento(txtNomeEvento.getText());
            autorizacao.setTipoEvento(getTipoEvento());
            autorizacao.setLogradouro(getLogradouro());
            autorizacao.setBairro(getBairro());
            autorizacao.setHoraInicial(ftfHoraInicio.getText().trim());
            autorizacao.setHoraFinal(ftfHoraTermino.getText().trim());
            autorizacao.setDescricaoArea(cbDescricaoArea.getSelectedItem().toString());
            autorizacao.setEstimativa((Integer) ftfEstimativa.getValue());
            autorizacao.setDataEvento(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            autorizacao.setMesAno(ftfMes.getText());
            autorizacao.setEmitido(emitido);
            autorizacao.setLocalEvento(getLocalEvento());

            AutorizacaoEventosBD emissaoBD = new AutorizacaoEventosBD();

            if (emissaoBD.alterarAutorizacaoEventos(autorizacao)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                copiarArquivo();
                atualizaTabela();
                tblAutorizacao.setEnabled(true);
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

        AutorizacaoEventos emissao = new AutorizacaoEventos();
        ftfMes.setValue(jdcData.getDate());
        //ftfAno.setValue(jdcData.getDate());
        //String controle = ftfAno.getText();
        //emissao.setId(setEmissaoLicenca().getId());
        emissao.setUsuario(getUsuario());

        emissao.setProcesso(getProcesso());
        emissao.setDataEvento(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
        emissao.setMesAno(ftfMes.getText());
        emissao.setUsuario(getUsuario());

        AutorizacaoEventosBD emissaoBD = new AutorizacaoEventosBD();

        if (emissaoBD.alterarAutorizacaoEventos(emissao)) {
            JOptionPane.showMessageDialog(this, "Dados alterados!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void alteraEmitido() {
        Date dataDoUsuario = listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getDataEvento();

        Calendar c = Calendar.getInstance();//Instancia a classe Calendar.
        c.setTime(dataDoUsuario);//Altera a data atual,pela sua data
        c.set(Calendar.DAY_OF_MONTH, c.get(Calendar.DAY_OF_MONTH) + 365);//Altera a data para +1 dia.
        jdcDataTeste.setDate(c.getTime());

        emitido = "sim";

        AutorizacaoEventos autorizacao = new AutorizacaoEventos();
        autorizacao.setId(listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getId());
        autorizacao.setEmitido(this.emitido);

        AutorizacaoEventosBD emissaoBD = new AutorizacaoEventosBD();
        if (emissaoBD.alteraAutorizacaoEventosEmitido(autorizacao)) {
            try {
                executaTramitacaoEmitido();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Erro ao tramitar dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluiTitulo() {

        AutorizacaoEventosBD emissaoBD = new AutorizacaoEventosBD();
        if (emissaoBD.excluiAutorizacaoEventos(listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()))) {
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
        txtNomeEvento.addKeyListener(new KeyListener() {

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
            processo = null;
            juridico = null;
            tipoEvento = null;
            logradouro = null;
            bairro = null;
            localEvento = null;

            txtIdJuridico.setText("");
            txtNumeroProcJur.setText("");
            txtNomeRequerente.setText("");

            txtIdProcesso.setText("");
            txtNumeroProcesso.setText("");
            txtNomeEvento.setText("");
            txtIdTipoEvento.setText("");
            txtNomeTipoEvento.setText("");
            txtIdLogradouro.setText("");
            txtNomeLogradouro.setText("");
            txtIdLocalEvento.setText("");
            txtNomeLocalEvento.setText("");
            txtIdBairro.setText("");
            txtNomeBairro.setText("");
            ftfEstimativa.setValue(null);

            jdcData.setDate(null);
            ftfMes.setText("");
            ftfHoraInicio.setValue(null);
            ftfHoraTermino.setValue(null);
            ftfMesTramite.setText("");

        }
    }

    private void habilitaCampos() {

        jRBLogradouro.setEnabled(true);
        jRBImovel.setEnabled(true);
        txtIdJuridico.setEditable(true);
        txtIdProcesso.setEditable(true);
        txtIdTipoEvento.setEditable(true);
        //txtIdLogradouro.setEditable(true);
        txtIdBairro.setEditable(true);
        txtNomeEvento.setEditable(true);
        ftfEstimativa.setEditable(true);
        cbDescricaoArea.setEnabled(true);
        btnSelecionaTipoEvento.setEnabled(true);
        //btnSelecionaRua.setEnabled(true);
        btnSelecionaBairro.setEnabled(true);
        btnSelecionaJuridico.setEnabled(true);
        btnSelecionaProcesso.setEnabled(true);
        jdcData.setEnabled(true);
        ftfHoraInicio.setEditable(true);
        ftfHoraTermino.setEditable(true);
    }

    private void desabilitaCampos() {

        jRBLogradouro.setEnabled(false);
        jRBImovel.setEnabled(false);
        txtIdJuridico.setEditable(false);
        txtIdProcesso.setEditable(false);
        txtIdTipoEvento.setEditable(false);
        //txtIdLogradouro.setEditable(false);
        txtIdBairro.setEditable(false);
        txtNomeEvento.setEditable(false);
        btnSelecionaJuridico.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        btnSelecionaTipoEvento.setEnabled(false);
        //btnSelecionaRua.setEnabled(false);
        btnSelecionaBairro.setEnabled(false);
        jdcData.setEnabled(false);
        ftfHoraInicio.setEditable(false);
        ftfHoraTermino.setEditable(false);
        ftfEstimativa.setEditable(false);
        cbDescricaoArea.setEnabled(false);

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
        jdcData.setDate(hoje);
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
        Integer indice = tblAutorizacao.getSelectedRow();
        Integer id = listAutorizacaoEventos.get(indice).getProcesso().getId();
        int binario = 0;
        try {
            int max = listProcesso.size();
            int id_busca = listAutorizacaoEventos.get(indice).getProcesso().getId();
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
        AutorizacaoEventosBD tramitacaoBD = new AutorizacaoEventosBD();
        //listEmissaoTitulo = tramitacaoBD.consultaTramUltimoIdProce(id);
        int binario = 0;
        try {
            int max = listAutorizacaoEventos.size();
            int id_busca = Integer.parseInt(id);
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listAutorizacaoEventos.get(i).getProcesso().getId();
                if (listAutorizacaoEventos.get(i).getProcesso().getId() == id_busca) {
                    setTramitacao(listAutorizacaoEventos.get(i));
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

    public void buscaAutorizacao(int id) {
        AutorizacaoEventosBD autorizacaoBD = new AutorizacaoEventosBD();
        listAutorizacaoEventos = autorizacaoBD.consultaAutorizacao();
        int binario = 0;
        try {
            int max = listAutorizacaoEventos.size();
            int id_busca = id;
            //Percorre a lista
            for (int i = 0; i < max; i++) {
                listAutorizacaoEventos.get(i).getId();
                if (listAutorizacaoEventos.get(i).getId() == id_busca) {
                    setAutorizacaoEventos(listAutorizacaoEventos.get(i));
                    binario = 1;

                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdProcesso.setText("");
                //txtNumeroProcesso.setText("");
                setAutorizacaoEventos(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setAutorizacaoEventos(null);
            //txtNumeroProcesso.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaLogradouro() {
        LogradouroBD ruaBD = new LogradouroBD();
        listLogradouro = ruaBD.consultaLogradouro();
        int binario = 0;
        try {
            int max = listLogradouro.size();
            int id_busca = Integer.parseInt(txtIdLogradouro.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listLogradouro.get(i).getId();
                if (listLogradouro.get(i).getId() == id_busca) {
                    setLogradouro(listLogradouro.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdLogradouro.setText("");
                txtNomeLogradouro.setText("");
                setLogradouro(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setLogradouro(null);
            txtNomeLogradouro.setText("");
            //txtIdLogradouro.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaLocalEvento() {
        LocalEventoBD localEventoBD = new LocalEventoBD();
        listLocalEvento = localEventoBD.consultaLocalEvento();
        int binario = 0;
        try {
            int max = listLocalEvento.size();
            int id_busca = Integer.parseInt(txtIdLocalEvento.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listLocalEvento.get(i).getId();
                if (listLocalEvento.get(i).getId() == id_busca) {
                    setLocalEvento(listLocalEvento.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdLocalEvento.setText("");
                txtNomeLocalEvento.setText("");
                setLocalEvento(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setLocalEvento(null);
            txtNomeLocalEvento.setText("");
            txtIdLocalEvento.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaBairro() {
        BairroBD BairroBD = new BairroBD();
        listBairro = BairroBD.consultaBairro();
        int binario = 0;
        try {
            int max = listBairro.size();
            int id_busca = Integer.parseInt(txtIdBairro.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listBairro.get(i).getId();
                if (listBairro.get(i).getId() == id_busca) {
                    setBairro(listBairro.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdBairro.setText("");
                txtNomeBairro.setText("");
                setBairro(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setBairro(null);
            txtNomeBairro.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaTipoEvento() {
        TipoEventoBD tipoEventoBD = new TipoEventoBD();
        listTipoEvento = tipoEventoBD.consultaTipoEvento();
        int binario = 0;
        try {
            int max = listTipoEvento.size();
            int id_busca = Integer.parseInt(txtIdTipoEvento.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listTipoEvento.get(i).getId();
                if (listTipoEvento.get(i).getId() == id_busca) {
                    setTipoEvento(listTipoEvento.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdTipoEvento.setText("");
                txtNomeTipoEvento.setText("");
                setTipoEvento(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setTipoEvento(null);
            txtNomeTipoEvento.setText("");
            //txtIdTipoEvento.setText("");
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

        buttonGroupLocal = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jlClipe = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAutorizacao = new javax.swing.JTable();
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
        jPanel17 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        txtNomeLogradouro = new javax.swing.JTextField();
        btnSelecionaRua = new javax.swing.JButton();
        txtIdLogradouro = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        txtNomeBairro = new javax.swing.JTextField();
        btnSelecionaBairro = new javax.swing.JButton();
        txtIdBairro = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        txtNomeEvento = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel22 = new javax.swing.JLabel();
        txtNomeTipoEvento = new javax.swing.JTextField();
        btnSelecionaTipoEvento = new javax.swing.JButton();
        txtIdTipoEvento = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel23 = new javax.swing.JLabel();
        ftfEstimativa = new javax.swing.JFormattedTextField();
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbDescricaoArea = new javax.swing.JComboBox();
        jPanel21 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        txtNomeLocalEvento = new javax.swing.JTextField();
        btnSelecionaLocalEvento = new javax.swing.JButton();
        txtIdLocalEvento = new javax.swing.JTextField();
        jRBLogradouro = new javax.swing.JRadioButton();
        jRBImovel = new javax.swing.JRadioButton();
        jRBNenhum = new javax.swing.JRadioButton();

        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Emissão Titulo\n");
        setPreferredSize(new java.awt.Dimension(1050, 690));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/autorizacao_64x64.png"))); // NOI18N
        jLabel1.setText("Autorização de Eventos");
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

        tblAutorizacao.setFont(new java.awt.Font("Verdana", 1, 10)); // NOI18N
        tblAutorizacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Id", "Processo", "Licença", "Requerente", "Data Evento", "Emitido", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAutorizacao.setSelectionBackground(new java.awt.Color(51, 51, 255));
        tblAutorizacao.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAutorizacao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAutorizacaoMouseClicked(evt);
            }
        });
        tblAutorizacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAutorizacaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAutorizacao);

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
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerente"));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        txtNomeRequerente.setEditable(false);
        txtNomeRequerente.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.gridwidth = 3;
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
        txtNumeroProcJur.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 350;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel20, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder("Horário "));
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
        gridBagConstraints.ipadx = 40;
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
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfHoraInicio, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 380;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel23, gridBagConstraints);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Local Evento - Nome Logradouro"));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel20.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel20, gridBagConstraints);

        txtNomeLogradouro.setEditable(false);
        txtNomeLogradouro.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeLogradouro.setEnabled(false);
        txtNomeLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeLogradouroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(txtNomeLogradouro, gridBagConstraints);

        btnSelecionaRua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaRua.setEnabled(false);
        btnSelecionaRua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaRuaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(btnSelecionaRua, gridBagConstraints);

        txtIdLogradouro.setEditable(false);
        txtIdLogradouro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdLogradouroFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel17.add(txtIdLogradouro, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel17, gridBagConstraints);

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Bairro"));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabel21.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(jLabel21, gridBagConstraints);

        txtNomeBairro.setEditable(false);
        txtNomeBairro.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeBairro.setEnabled(false);
        txtNomeBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeBairroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(txtNomeBairro, gridBagConstraints);

        btnSelecionaBairro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaBairro.setEnabled(false);
        btnSelecionaBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaBairroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(btnSelecionaBairro, gridBagConstraints);

        txtIdBairro.setEditable(false);
        txtIdBairro.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdBairroFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel16.add(txtIdBairro, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel16, gridBagConstraints);

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel18.setLayout(new java.awt.GridBagLayout());

        txtNomeEvento.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanel18.add(txtNomeEvento, gridBagConstraints);

        jLabel8.setText("Nome Evento: ");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel18.add(jLabel8, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel18, gridBagConstraints);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Tipo Evento"));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        jLabel22.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLabel22, gridBagConstraints);

        txtNomeTipoEvento.setEditable(false);
        txtNomeTipoEvento.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeTipoEvento.setEnabled(false);
        txtNomeTipoEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeTipoEventoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(txtNomeTipoEvento, gridBagConstraints);

        btnSelecionaTipoEvento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaTipoEvento.setEnabled(false);
        btnSelecionaTipoEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaTipoEventoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(btnSelecionaTipoEvento, gridBagConstraints);

        txtIdTipoEvento.setEditable(false);
        txtIdTipoEvento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdTipoEventoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel19.add(txtIdTipoEvento, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel23.setText("Estimativa");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel23, gridBagConstraints);

        ftfEstimativa.setEditable(false);
        ftfEstimativa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(ftfEstimativa, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.insets = new java.awt.Insets(42, 5, 5, 5);
        jPanel2.add(jPanel8, gridBagConstraints);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Descrição Área:");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel5, gridBagConstraints);

        cbDescricaoArea.setEditable(true);
        cbDescricaoArea.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "PRIVADA", "ABERTA", "SEMI-ABERTA" }));
        cbDescricaoArea.setEnabled(false);
        cbDescricaoArea.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbDescricaoAreaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(cbDescricaoArea, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(30, 5, 5, 5);
        jPanel2.add(jPanel5, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Local Evento - Nome Imovel"));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        jLabel25.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(jLabel25, gridBagConstraints);

        txtNomeLocalEvento.setEditable(false);
        txtNomeLocalEvento.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeLocalEvento.setEnabled(false);
        txtNomeLocalEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeLocalEventoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(txtNomeLocalEvento, gridBagConstraints);

        btnSelecionaLocalEvento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaLocalEvento.setEnabled(false);
        btnSelecionaLocalEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaLocalEventoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(btnSelecionaLocalEvento, gridBagConstraints);

        txtIdLocalEvento.setEditable(false);
        txtIdLocalEvento.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdLocalEventoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel21.add(txtIdLocalEvento, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        buttonGroupLocal.add(jRBLogradouro);
        jRBLogradouro.setText("Logradouro");
        jRBLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBLogradouroActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        jPanel2.add(jRBLogradouro, gridBagConstraints);

        buttonGroupLocal.add(jRBImovel);
        jRBImovel.setText("Imovel");
        jRBImovel.setMaximumSize(new java.awt.Dimension(81, 23));
        jRBImovel.setMinimumSize(new java.awt.Dimension(81, 23));
        jRBImovel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBImovelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        jPanel2.add(jRBImovel, gridBagConstraints);

        buttonGroupLocal.add(jRBNenhum);
        jRBNenhum.setText("Nenhum");
        jRBNenhum.setMaximumSize(new java.awt.Dimension(81, 23));
        jRBNenhum.setMinimumSize(new java.awt.Dimension(81, 23));
        jRBNenhum.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBNenhumActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        jPanel2.add(jRBNenhum, gridBagConstraints);

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

        AutorizacaoEventosBD tramitacaoBD = new AutorizacaoEventosBD();
        if (tramitacaoBD.testaConexao()) {
            jRBImovel.setSelected(true);
            controlaLocalEvento();
            limpaSelecaoTabela();
            limpaCampos();
            habilitaCampos();
            btnFiltrar.setEnabled(false);
            tblAutorizacao.setEnabled(false);
            limpaSelecaoTabela();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            caixaAlta();
            setaDataAtual();
            buscaJuridico();
            buscaLogradouro();
            buscaTipoEvento();
            buscaBairro();
            buscaUsuario(idUsuario);

        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {
        if (modo == Constantes.INSERT_MODE) {
            incluiAutorizacao();
            tblAutorizacao.setEnabled(true);
            // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraLicenca();
        }
    }

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        jRBNenhum.setSelected(true);
        controlaLocalEvento();
        desabilitaBotoes();
        desabilitaCampos();
        controlaLocalEvento();
        limpaCampos();
        tblAutorizacao.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed

        if (tblAutorizacao.getSelectedRow() != -1) {
            if ("sim".equals(listAutorizacaoEventos.get(tblAutorizacao.getSelectedRow()).getEmitido())) {
                JOptionPane.showMessageDialog(this, "Processo Emitido não pode ser Alterado\nConsulte o Adm do Sistema!", "Tramitaçao", JOptionPane.INFORMATION_MESSAGE);
            } else {
               
                controlaLocalEvento();
                habilitaCampos();
                tblAutorizacao.setEnabled(false);
                txtIdProcesso.setEditable(false);
                btnSelecionaProcesso.setEnabled(false);
                txtIdJuridico.setEditable(false);
                btnSelecionaJuridico.setEnabled(false);
                habilitaBotoes();
                modo = Constantes.EDIT_MODE;
                buscaUsuario(idUsuario);
                buscaTipoEvento();
                buscaBairro();
                buscaProcesso();
                buscaJuridico();
                buscaLocalEvento();
                buscaLogradouro();
                
             
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma tramitação da lista!", "Tramitaçao", JOptionPane.INFORMATION_MESSAGE);
        }


    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {
        if (tblAutorizacao.getSelectedRow() != -1) {
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

    private void tblAutorizacaoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAutorizacaoMouseClicked
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
    }//GEN-LAST:event_tblAutorizacaoMouseClicked

    private void tblAutorizacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAutorizacaoKeyPressed
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
    }//GEN-LAST:event_tblAutorizacaoKeyPressed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        atualizaTabela();

    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed

        if (tblAutorizacao.getSelectedRow() != -1) {
            int indice = tblAutorizacao.getSelectedRow();
            buscaJuridico();
            buscaProcesso();
            String teste = listAutorizacaoEventos.get(indice).getProcesso().getArquivado();
            if ("NAO".equals(listAutorizacaoEventos.get(indice).getProcesso().getArquivado())) {
                executaRelatorioAutorizacao();
            } else {
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
        if (tblAutorizacao.getSelectedRow() != -1) {
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnDespachoActionPerformed

    private void btnImprimir3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimir3ActionPerformed
        if (tblAutorizacao.getSelectedRow() != -1) {

            executaRelatorioAutorizacao();

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

    private void txtNomeLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLogradouroActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeLogradouroActionPerformed

    private void btnSelecionaRuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaRuaActionPerformed
        LogradouroFrame ruaFrame = new LogradouroFrame(this);
        ruaFrame.setVisible(true);
        this.getDesktopPane().add(ruaFrame);
        ruaFrame.toFront();
        centralizaJanela(ruaFrame);  // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaRuaActionPerformed

    private void txtIdLogradouroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdLogradouroFocusLost
        buscaLogradouro();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdLogradouroFocusLost

    private void txtNomeBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeBairroActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeBairroActionPerformed

    private void btnSelecionaBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaBairroActionPerformed
        BairroFrame BairroFrame = new BairroFrame(this);
        BairroFrame.setVisible(true);
        this.getDesktopPane().add(BairroFrame);
        BairroFrame.toFront();
        centralizaJanela(BairroFrame);
}//GEN-LAST:event_btnSelecionaBairroActionPerformed

    private void txtIdBairroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdBairroFocusLost
        buscaBairro();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdBairroFocusLost

    private void txtNomeTipoEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeTipoEventoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeTipoEventoActionPerformed

    private void btnSelecionaTipoEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaTipoEventoActionPerformed
        TipoEventoFrame tipoEventoFrame = new TipoEventoFrame(this);
        tipoEventoFrame.setVisible(true);
        this.getDesktopPane().add(tipoEventoFrame);
        tipoEventoFrame.toFront();
        centralizaJanela(tipoEventoFrame);   // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaTipoEventoActionPerformed

    private void txtIdTipoEventoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdTipoEventoFocusLost
        buscaTipoEvento();// TODO add your handling code here:
    }//GEN-LAST:event_txtIdTipoEventoFocusLost

    private void cbDescricaoAreaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbDescricaoAreaActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbDescricaoAreaActionPerformed

    private void txtNomeLocalEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLocalEventoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeLocalEventoActionPerformed

    private void btnSelecionaLocalEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaLocalEventoActionPerformed
        LocalEventoFrame localEventoFrame = new LocalEventoFrame(this);
        localEventoFrame.setVisible(true);
        this.getDesktopPane().add(localEventoFrame);
        localEventoFrame.toFront();
        centralizaJanela(localEventoFrame);    // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaLocalEventoActionPerformed

    private void txtIdLocalEventoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdLocalEventoFocusLost
        buscaLocalEvento();// TODO add your handling code here:
    }//GEN-LAST:event_txtIdLocalEventoFocusLost

    private void jRBLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBLogradouroActionPerformed
        controlaLocalEvento();
        // TODO add your handling code here:
    }//GEN-LAST:event_jRBLogradouroActionPerformed

    private void jRBImovelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBImovelActionPerformed
        controlaLocalEvento();
        // TODO add your handling code here:
    }//GEN-LAST:event_jRBImovelActionPerformed

    private void jRBNenhumActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBNenhumActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRBNenhumActionPerformed

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
    private javax.swing.JButton btnSelecionaBairro;
    private javax.swing.JButton btnSelecionaJuridico;
    private javax.swing.JButton btnSelecionaLocalEvento;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JButton btnSelecionaRua;
    private javax.swing.JButton btnSelecionaTipoEvento;
    private javax.swing.ButtonGroup buttonGroupLocal;
    private javax.swing.JComboBox cbDescricaoArea;
    public javax.swing.JFormattedTextField ftfEstimativa;
    private javax.swing.JFormattedTextField ftfHoraInicio;
    private javax.swing.JFormattedTextField ftfHoraTermino;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRBImovel;
    private javax.swing.JRadioButton jRBLogradouro;
    private javax.swing.JRadioButton jRBNenhum;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlClipe;
    private javax.swing.JTable tblAutorizacao;
    private javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtIdBairro;
    private javax.swing.JTextField txtIdJuridico;
    private javax.swing.JTextField txtIdLocalEvento;
    private javax.swing.JTextField txtIdLogradouro;
    public javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtIdTipoEvento;
    private javax.swing.JTextField txtNomeBairro;
    private javax.swing.JTextField txtNomeEvento;
    private javax.swing.JTextField txtNomeLocalEvento;
    private javax.swing.JTextField txtNomeLogradouro;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNomeTipoEvento;
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
    public void setTramitacao(AutorizacaoEventos emissao) {
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
    public void setEmissaoTitulo(AutorizacaoEventos emissaoTitulo) {
        this.setAutorizacaoEventos(emissaoTitulo);
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
    public AutorizacaoEventos getAutorizacaoEventos() {
        return autorizacaoEventos;
    }

    /**
     * @param emissaoLicenca the emissaoLicenca to set
     */
    public void setAutorizacaoEventos(AutorizacaoEventos autorizacao) {
        this.autorizacaoEventos = autorizacao;
    }

    /**
     * @return the logradouro
     */
    public Logradouro getLogradouro() {
        return logradouro;
    }

    /**
     * @param logradouro the logradouro to set
     */
    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
        txtNomeLogradouro.setText(logradouro.getNome());
        txtIdLogradouro.setText(logradouro.getId().toString());
    }

    /**
     * @return the bairro
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
        txtNomeBairro.setText(bairro.getNome());
        txtIdBairro.setText(bairro.getId().toString());
    }

    /**
     * @return the tipoEvento
     */
    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    /**
     * @param tipoEvento the tipoEvento to set
     */
    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
        txtIdTipoEvento.setText(tipoEvento.getId().toString());
        txtNomeTipoEvento.setText(tipoEvento.getNome());

    }

    public LocalEvento getLocalEvento() {
        return localEvento;
    }

    /**
     * @param localEvento the localEvento to set
     */
    public void setLocalEvento(LocalEvento localEvento) {
        this.localEvento = localEvento;
        txtIdLocalEvento.setText(localEvento.getId().toString());
        txtNomeLocalEvento.setText(localEvento.getNome());
    }
}
