/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.analise;

//import com.independentsoft.office.word.WordDocument;
import java.sql.SQLException;
import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.requerente.RequerenteFrame;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.Constantes;
import sip.emissaolicenca.EmissaoLicencaFrame;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javax.swing.text.NumberFormatter;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.view.JasperViewer;
import sip.processo.AnexosProcesso;
import sip.processo.ProcessoFrame;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class ParecerAnaliseFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<ParecerAnalise> listParecer;
    private ParecerAnalise parecerAnalise;
    private int modo;
    private int modoSeleciona;
    private Analise analise;
    private List<Analise> listAnalise;
    private String loteUnico;
    private JDateChooser jdcDataParecer;
    private JDateChooser jdcDataVistoria;
    private JDateChooser jdcDataTramite;
    private AnaliseFrame analiseFrame;
    private Usuario usuario;
    private Processo processo;
    private AnexosProcesso anexosProcesso;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private String tipo;
    private String idLote;
    JPopupMenu jPopupMenu = new JPopupMenu();
    JMenuItem jMenuItemAlterar = new JMenuItem();
    private List<ParecerAnalise> listParecerProcAnexos;
    private List<AnexosProcesso> listAnexosProcesso;
    private Integer idProcesso;
    /** Creates new form ClienteFrame */
    private List<Tramitacao> listTramitacao;
    private Tramitacao tramitacao;
    private int lastId;

    public ParecerAnaliseFrame() {
        initComponents();
        defineModelo();
        formataData();
        enviarWord();
        setaDataAtual();
        caixaAlta();
        fechando();
        jdcDataParecer.setEnabled(false);
        jdcDataVistoria.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
    }

    public ParecerAnaliseFrame(AnaliseFrame analiseFrame) {
        initComponents();
        defineModelo();
        formataData();
        enviarWord();
        setaDataAtual();
        caixaAlta();
        fechando();
        btnNovo.setVisible(false);
        this.analiseFrame = analiseFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        jdcDataParecer.setEnabled(false);
        jdcDataVistoria.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
        btnNovo.doClick();
        txtIdAnalise.setText(analiseFrame.getIdAnalise());
        MenuJtableDetalhe();
        buscaAnalise();
        btnSelecionaAnexo.setEnabled(false);
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

        tableModel = (DefaultTableModel) tblParecer.getModel();
        listModel = tblParecer.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheParecer();
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


            tblParecer.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblParecer.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblParecer.getColumnModel().getColumn(2).setPreferredWidth(270);
            tblParecer.getColumnModel().getColumn(3).setPreferredWidth(380);
            tblParecer.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblParecer.getColumnModel().getColumn(5).setPreferredWidth(150);



        } catch (ParseException ex) {
            Logger.getLogger(ParecerAnaliseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorio() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblParecer.getSelectedRow();

        int mostraID = listParecer.get(indice).getId();

        try {
            HashMap parametros = new HashMap();
            //parametros.put("CAMINHO_IMAGEM1", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            //parametros.put("CAMINHO_IMAGEM2", System.getProperty("user.dir") + "\\imagem\\logoserf.jpg");
            //parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");
            parametros.put("ID_PARECER", (long) mostraID);
            switch (listParecer.get(indice).getTipo()) {
                case "PARECER TECNICO": {
                    JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\parecer\\analise\\parecer_tecnico.jasper", parametros, acessoBd.conectar());
                    JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    JasperViewer.viewReport(jp, false);
                    break;
                }
                case "PARECER CONCLUSIVO": {
                    JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\parecer\\analise\\parecer_conclusivo.jasper", parametros, acessoBd.conectar());
                    JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    JasperViewer.viewReport(jp, false);
                    break;
                }
                case "NOTIFICAÇÃO": {
                    JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\parecer\\analise\\notificacao.jasper", parametros, acessoBd.conectar());
                    JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    JasperViewer.viewReport(jp, false);
                    break;
                }



            }


        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void executaRelatorioAutomatico() {
        AcessoBD acessoBd = new AcessoBD();
        //int mostraID = laudoBD.mostraID;

        try {
            HashMap parametros = new HashMap();
            parametros.put("CAMINHO_IMAGEM1", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("CAMINHO_IMAGEM2", System.getProperty("user.dir") + "\\imagem\\logoserf.jpg");
            parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");
            //parametros.put("ID_LAUDO", (long) mostraID);

            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\laudoMzu.jasper", parametros, acessoBd.conectar());
            JOptionPane.showMessageDialog(this, "Aquarde enquanto o relatório é impresso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            //JasperViewer.viewReport(jp, false);
            JasperPrintManager.printReport(jp, false);
            /* for(int i = 0; i <= 2; i++){
            //JasperPrintManager.printPage(jp,0,false);       
            }*/

            JRDocxExporter exporter = new JRDocxExporter();
            exporter.setExporterInput(new SimpleExporterInput(jp));
            File exportReportFile = new File("teste" + ".docx");
            exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(exportReportFile));
            exporter.exportReport();


        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();

        if (txtFiltro.getText().equals("")) {
            listParecer = parecerBD.consultaParecer();
        } else {
            listParecer = parecerBD.consultaParecerNome(txtFiltro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listParecer.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                        listParecer.get(i).getId(),
                        listParecer.get(i).getProcesso().getNumProcesso(),
                        listParecer.get(i).getProcesso().getTipoLicenca(),
                        listParecer.get(i).getRequerente().getNome(),
                        listParecer.get(i).getTipo(),
                        simpleDateFormat.format(listParecer.get(i).getDataParecer())
                    });
        }

    }

    private void enviarWord() {
        try {
            //WordDocument doc = new WordDocument("D:\\PARECERTECMODELO.docx");

            //doc.replace("#[nome]","John Smith");

            //File diretorio = new File("D:\\doc");
            //diretorio.mkdir();

            //doc.save("D:\\TESTE\\PARECERTECMODELO.doc", true);
            Runtime.getRuntime().exec("rundll32 url.dll FileProtocolHandler " + "D:\\TESTE\\PARECERTECMODELO.doc");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void selecionaTipoDocumento() {
        if (jRBPTecnico.isSelected()) {
            tipo = "PARECER TECNICO";
        } else if (jRBPConclusivo.isSelected()) {
            tipo = "PARECER CONCLUSIVO";
        } else if (jRBPNotificacao.isSelected()) {
            tipo = "NOTIFICAÇÃO";
        }
    }


    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcDataParecer.setDate(hoje);
    }

    private void formataData() {

        jdcDataParecer = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataParecer.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcDataParecer.setBackground(Color.WHITE);
        jdcDataParecer.setBounds(255, 91, 87, 20);

        jdcDataVistoria = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataVistoria.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcDataVistoria.setBackground(Color.WHITE);
        jdcDataVistoria.setBounds(255, 91, 87, 20);
        
       

        jPanel8.add(jdcDataVistoria);
        jPanel7.add(jdcDataParecer);
    }

    private void mostraDetalheParecer() {
        if (tblParecer.getSelectedRow() != -1) {
            int indice = tblParecer.getSelectedRow();
            txtIdAnalise.setText(listParecer.get(indice).getAnalise().getId().toString());
            txtNomeRequerente.setText(listParecer.get(indice).getRequerente().getNome());
            jdcDataParecer.setDate(listParecer.get(indice).getDataParecer());
            jdcDataVistoria.setDate(listParecer.get(indice).getDataVistoria());
            txtNomeAnexo.setText(listParecer.get(indice).getAnexosProcesso().getDescricaoAnexo());
            String anexoEstudo = listParecer.get(indice).getAnexosProcesso().getNomeArquivo();
            switch (listParecer.get(indice).getTipo()) {
                case "PARECER TECNICO":
                    jRBPTecnico.setSelected(true);
                    break;
                case "PARECER CONCLUSIVO":
                    jRBPConclusivo.setSelected(true);
                    break;
                case "NOTIFICAÇÃO":
                    jRBPNotificacao.setSelected(true);
                    break;
            }


            //txtNomeAnexo.setText(listParecer.get(indice).getListAnexosProcesso().get(indice).getNomeArquivo());
        } else {
            txtNomeAnexo.setText("");
            txtIdAnalise.setText("");
            txtNomeRequerente.setText("");
            jdcDataParecer.setDate(null);
            jdcDataVistoria.setDate(null);
            jRBPTecnico.setSelected(true);
        }

    }

    private void controleDaTramicacao() {
        int indice = tblParecer.getSelectedRow();

        if ("NAO".equals(listParecer.get(indice).getAnexoIncluso())) {
            try {
                executaTramitacaoAnexoNao();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } else {
            try {
                executaTramitacaoAnexoSim();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void executaTramitacaoEdicao() throws SQLException, ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();

        ftfMes.setValue(jdcDataTramite.getDate());
        ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
        listParecer = parecerBD.consultaParecer();
        lastId = parecerBD.getLastId();
        buscaParecer(lastId);

        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();
        String controle = getAnalise().getProcesso().getId() + " " + tipo + " EM EDIÇÃO";


        tram.setUsuario(getUsuario());
        tram.setProcesso(getAnalise().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setStatus(tipo + " EM EDIÇÃO");
        tram.setParecer(" ");
        tram.setSetor("ANÁLISE");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setParecer("ANÁLISE " + tipo);
        tram.setObservacao(null);
        tram.setControle(controle);
        //tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerAnalise(getParecer());

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao lançada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoAnexoNao() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMes.setValue(jdcDataTramite.getDate());
        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        
        String controle = getAnalise().getProcesso().getId() + " " + tipo + " CONCLUIDO";


        tram.setUsuario(getUsuario());
        tram.setProcesso(getAnalise().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setStatus(tipo + " CONCLUIDO");
        tram.setParecer(" ");
        tram.setSetor("ANÁLISE");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        tram.setParecer("ANÁLISE " + tipo);
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerAnalise(getParecer());

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao lançada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoAnexoSim() throws SQLException {
        int indice = tblParecer.getSelectedRow();
        int IdParecer = listParecer.get(indice).getId();

        Tramitacao tram = new Tramitacao();

        tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerAnalise(getParecer());


        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.alteraTramitacaoAnexoParecerAnalise(tram, IdParecer)) {
            JOptionPane.showMessageDialog(this, "tramitacao alterada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Erro na alteração da tramitação", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incluiParecer() throws SQLException, ParseException {

        if (jdcDataParecer.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (getAnalise() == null) {
            JOptionPane.showMessageDialog(this, "Informe a analise!", "Analise", JOptionPane.INFORMATION_MESSAGE);
            txtIdAnalise.requestFocus();
        }else if (getAnalise().getProcesso().getArquivado().equals("SIM")) {
             JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
            txtIdAnalise.requestFocus();
        }/*else  if (getAnexosProcesso() == null){
        JOptionPane.showMessageDialog(this, "Informe o anexo!", "Anexo", JOptionPane.INFORMATION_MESSAGE);
        txtIdAnalise.requestFocus();
        }*/ else {
            selecionaTipoDocumento();
            ParecerAnalise parecer = new ParecerAnalise();
            String controle;
            ftfMes.setValue(jdcDataParecer.getDate());
            controle = getAnalise().getId() + " " + tipo;

            parecer.setUsuario(getUsuario());
            parecer.setAnalise(getAnalise());
            parecer.setDataParecer(new java.sql.Date(((java.util.Date) jdcDataParecer.getDate()).getTime()));
            parecer.setTipo(tipo);
            parecer.setControle(controle);
            parecer.setAnexosProcesso(getAnexosProcesso());
            if (jdcDataVistoria.getDate() == null) {
                parecer.setDataVistoria(null);
            } else {
                parecer.setDataVistoria(new java.sql.Date(((java.util.Date) jdcDataVistoria.getDate()).getTime()));
            }
            parecer.setAnexoIncluso("NAO");


            ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
            if (parecerBD.incluiParecer(parecer)) {
                JOptionPane.showMessageDialog(this, tipo + " lançado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                executaTramitacaoEdicao();
                /*
                int resultado = JOptionPane.showConfirmDialog(null, "Deseja Imprimir o Laudo?", "Imprimir", JOptionPane.YES_NO_OPTION);
                
                if (resultado == JOptionPane.YES_OPTION) {
                executaRelatorioAutomatico(terrenoBD);  
                }
                 */
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente o " + tipo + " já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraParecer() {
        if (jdcDataParecer.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (getAnalise() == null) {
            JOptionPane.showMessageDialog(this, "Informe a Analise!", "Imovel", JOptionPane.INFORMATION_MESSAGE);
            txtIdAnalise.requestFocus();
        } else if (getAnexosProcesso() == null) {
            JOptionPane.showMessageDialog(this, "Informe o anexo!", "Anexo", JOptionPane.INFORMATION_MESSAGE);
            btnSelecionaAnexo.requestFocus();
        } else {
            selecionaTipoDocumento();
            ParecerAnalise parecer = new ParecerAnalise();
            String controle;
            ftfMes.setValue(jdcDataParecer.getDate());
            controle = getAnalise().getId() + " " + tipo;

            parecer.setAnalise(getAnalise());
            parecer.setId(this.listParecer.get(tblParecer.getSelectedRow()).getId());
            parecer.setDataParecer(new java.sql.Date(((java.util.Date) jdcDataParecer.getDate()).getTime()));
            parecer.setTipo(tipo);
            parecer.setControle(controle);
            parecer.setAnexosProcesso(getAnexosProcesso());
            if (jdcDataVistoria.getDate() == null) {
                parecer.setDataVistoria(null);
            } else {
                parecer.setDataVistoria(new java.sql.Date(((java.util.Date) jdcDataVistoria.getDate()).getTime()));
            }
            parecer.setAnexoIncluso("SIM");


            ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
            if (parecerBD.alterarParecer(parecer)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                controleDaTramicacao();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void exlcuiParecer() {
        ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
        if (parecerBD.excluiParecer(listParecer.get(tblParecer.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe existe integração de tabelas!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        //txtNomeTécnico.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }

    private void MenuJtableDetalhe() {

        jMenuItemAlterar.setText("Abrir Anexo");

        jMenuItemAlterar.addActionListener(
                new java.awt.event.ActionListener() {
                    // Importe a classe java.awt.event.ActionEvent

                    public void actionPerformed(ActionEvent e) {
                        int index = tblParecer.getSelectedRow();
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
        if (tblParecer.getSelectedRow() != -1) {
            ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
            listParecerProcAnexos = parecerBD.consultaParecer();
            //listAnexosProcesso = listParecerProcAnexos.get(tblParecer.getSelectedRow()).getListAnexosProcesso();
            //listCadastrosImobImovel = listImovel.get(tblDetalheImobiliario.getSelectedRow()).getCadastrosImobImovel();
            int indice = tblParecer.getSelectedRow();
            try {

                //String arquivo = listAnexosProcesso.get(indice).getNomeArquivo();
                String arquivo = listParecerProcAnexos.get(indice).getAnexosProcesso().getNomeArquivo();

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

    private void limpaCampos() {
        {

            txtIdAnalise.setText("");
            txtNomeRequerente.setText("");
            txtNomeAnexo.setText("");

        }
    }

    private void habilitaCampos() {

        jdcDataParecer.setEnabled(true);
        jdcDataVistoria.setEnabled(true);
        txtIdAnalise.setEditable(false);
        btnSelecionaAnalise.setEnabled(false);
        btnSelecionaAnexo.setEnabled(true);
        jRBPTecnico.setEnabled(true);
        jRBPConclusivo.setEnabled(true);
        jRBPNotificacao.setEnabled(true);

    }

    private void desabilitaCampos() {
        jdcDataParecer.setEnabled(false);
        jdcDataVistoria.setEnabled(false);
        txtIdAnalise.setEditable(false);
        btnSelecionaAnalise.setEnabled(false);
        jRBPTecnico.setEnabled(false);
        jRBPConclusivo.setEnabled(false);
        jRBPNotificacao.setEnabled(false);
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

    private void buscaAnalise() {
        AnaliseBD analiseBD = new AnaliseBD();
        listAnalise = analiseBD.consultaAnalise();
        int binario = 0;
        try {
            int max = listAnalise.size();
            int id_busca = Integer.parseInt(txtIdAnalise.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listAnalise.get(i).getId();
                if (listAnalise.get(i).getId() == id_busca) {
                    setAnalise(listAnalise.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdAnalise.setText("");
                txtNomeRequerente.setText("");
                analise = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            analise = null;
            txtNomeRequerente.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void buscaParecer(int id) {
        ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
        listParecer = parecerBD.consultaParecer();
        int binario = 0;
        try {
            int max = listParecer.size();
            int id_busca = id;
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listParecer.get(i).getId();
                if (listParecer.get(i).getId() == id_busca) {
                    setParecer(listParecer.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdAnalise.setText("");
                //txtNomeRequerente.setText("");
                setParecer(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            //analise = null;
            //txtNomeRequerente.setText("");
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
            //buscaLote();
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jRBPConclusivo = new javax.swing.JRadioButton();
        jRBPTecnico = new javax.swing.JRadioButton();
        jRBPNotificacao = new javax.swing.JRadioButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jLabel21 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblParecer = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtNomeRequerente = new javax.swing.JTextField();
        btnSelecionaAnalise = new javax.swing.JButton();
        txtIdAnalise = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        txtNomeAnexo = new javax.swing.JTextField();
        btnSelecionaAnexo = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Laudo Técnico");
        setPreferredSize(new java.awt.Dimension(900, 550));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/prancheta_64x64.png"))); // NOI18N
        jLabel1.setText("PARECER / NOTIFICAÇÃO - ANALISE");
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

        jPanel13.setBackground(new java.awt.Color(255, 255, 255));
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Parecer / Analise"));
        jPanel13.setLayout(new java.awt.GridBagLayout());

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
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel13.add(jLabel20, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        buttonGroup1.add(jRBPConclusivo);
        jRBPConclusivo.setText("Parecer Conclusivo");
        jRBPConclusivo.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBPConclusivo, gridBagConstraints);

        buttonGroup1.add(jRBPTecnico);
        jRBPTecnico.setSelected(true);
        jRBPTecnico.setText("Parecer Técnico");
        jRBPTecnico.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBPTecnico, gridBagConstraints);

        buttonGroup1.add(jRBPNotificacao);
        jRBPNotificacao.setText("Notificação");
        jRBPNotificacao.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBPNotificacao, gridBagConstraints);

        jPanel14.setBackground(new java.awt.Color(255, 255, 255));
        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Vistoria"));
        jPanel14.setLayout(new java.awt.GridBagLayout());

        jPanel8.setLayout(new java.awt.GridBagLayout());
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 15;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel14.add(jPanel8, gridBagConstraints);

        jLabel21.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        jPanel14.add(jLabel21, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel14, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel2, gridBagConstraints);

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro");
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

        tblParecer.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        tblParecer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROCESSO", "LICENÇA", "REQUERENTE", "TIPO ", "DATA PARECER / ANALISE"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class
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
        tblParecer.setGridColor(new java.awt.Color(102, 102, 102));
        tblParecer.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblParecer.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblParecerMouseClicked(evt);
            }
        });
        tblParecer.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblParecerKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblParecer);

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

        btnAlterar.setText("Alterar/Concluir");
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
        btnImprimir.setText("Numeração");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir);

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

        jPanel20.setBackground(new java.awt.Color(255, 255, 255));
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Analise"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel18, gridBagConstraints);

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

        btnSelecionaAnalise.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaAnalise.setEnabled(false);
        btnSelecionaAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaAnaliseActionPerformed(evt);
            }
        });
        btnSelecionaAnalise.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnSelecionaAnaliseFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaAnalise, gridBagConstraints);

        txtIdAnalise.setEditable(false);
        txtIdAnalise.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtIdAnaliseMouseExited(evt);
            }
        });
        txtIdAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdAnaliseActionPerformed(evt);
            }
        });
        txtIdAnalise.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdAnaliseFocusLost(evt);
            }
        });
        txtIdAnalise.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdAnaliseKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdAnaliseKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdAnaliseKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdAnalise, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel20, gridBagConstraints);

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
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel21, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        ParecerAnaliseBD parecerBD = new ParecerAnaliseBD();
        if (parecerBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblParecer.setEnabled(false);
            limpaSelecaoTabela();
            txtIdAnalise.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            try {
                incluiParecer();

            } catch (SQLException | ParseException ex) {
                ex.printStackTrace();
            }
            tblParecer.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraParecer();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblParecer.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblParecer.getSelectedRow() != -1) {

            habilitaCampos();
            habilitaBotoes();

            modo = Constantes.EDIT_MODE;
            buscaAnalise();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblParecer.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Parecer?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiParecer();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
        //atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void btnSelecionaAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAnaliseActionPerformed



        AnaliseFrame analiseFramee = new AnaliseFrame(this);
        analiseFramee.setVisible(true);
        this.getDesktopPane().add(analiseFramee);
        analiseFramee.toFront();
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaAnaliseActionPerformed

    private void btnSelecionaAnaliseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaAnaliseFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaAnaliseFocusLost

    private void txtIdAnaliseMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIdAnaliseMouseExited
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseMouseExited

    private void txtIdAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdAnaliseActionPerformed
        buscaAnalise();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseActionPerformed

    private void txtIdAnaliseFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdAnaliseFocusLost
        buscaAnalise();
        //buscaTramitacao(txtIdProcesso.getText());
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseFocusLost

    private void txtIdAnaliseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdAnaliseKeyPressed
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseKeyPressed

    private void txtIdAnaliseKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdAnaliseKeyReleased
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseKeyReleased

    private void txtIdAnaliseKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdAnaliseKeyTyped
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdAnaliseKeyTyped

    private void tblParecerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblParecerMouseClicked
        if (evt.getButton() == MouseEvent.BUTTON3) { // Clique com botao direito do mouse. 
            jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
            //JOptionPane.showMessageDialog(this, "Processo Selecionado!", "Processo", JOptionPane.INFORMATION_MESSAGE);
            //instanciei meu novo formulario   
        }

        if (evt.getClickCount() == 2) {
            if (modo == Constantes.INCLUIR) {
                //selecionaBairroRequerente();
                dispose();
            } else if (modo == Constantes.ALTERAR) {
                //selecionaBairroMapa();
                dispose();
                evt.consume();
            }
        }
}//GEN-LAST:event_tblParecerMouseClicked

    private void tblParecerKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblParecerKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modo == Constantes.INCLUIR) {
                //selecionaBairroRequerente();
                dispose();
            } else if (modo == Constantes.ALTERAR) {
                //selecionaBairroMapa();
                dispose();
                evt.consume();

            }
        }
}//GEN-LAST:event_tblParecerKeyPressed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (tblParecer.getSelectedRow() != -1) {

            executaRelatorio();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo na tabela!", "Emissão", JOptionPane.INFORMATION_MESSAGE);
        }// TODO add your handling code here:
}//GEN-LAST:event_btnImprimirActionPerformed

    private void txtNomeAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeAnexoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeAnexoActionPerformed

    private void btnSelecionaAnexoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaAnexoActionPerformed
        //setIdProcesso(analiseFrame.getIdProcesso());
        int indice = tblParecer.getSelectedRow();

        setIdProcesso(listParecer.get(indice).getProcesso().getId());

        ProcessoFrame processoFrame = new ProcessoFrame(this);
        processoFrame.setVisible(true);
        this.getDesktopPane().add(processoFrame);
        processoFrame.toFront();  // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaAnexoActionPerformed

    private void btnSelecionaAnexoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaAnexoFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaAnexoFocusLost
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaAnalise;
    private javax.swing.JButton btnSelecionaAnexo;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JRadioButton jRBPConclusivo;
    private javax.swing.JRadioButton jRBPNotificacao;
    private javax.swing.JRadioButton jRBPTecnico;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblParecer;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdAnalise;
    private javax.swing.JTextField txtNomeAnexo;
    private javax.swing.JTextField txtNomeRequerente;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the imovel
     */
    public Analise getAnalise() {
        return analise;
    }

    /**
     * @param imovel the imovel to set
     */
    public void setAnalise(Analise analise) {
        this.analise = analise;
        txtIdAnalise.setText(analise.getId().toString());
        txtNomeRequerente.setText(analise.getRequerente().getNome());
        String idPro = analise.getProcesso().getId().toString();
    }

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
        txtNomeAnexo.setText(anexosProcesso.getNomeArquivo());
    }

    /**
     * @return the parecer
     */
    public ParecerAnalise getParecer() {
        return parecerAnalise;
    }

    /**
     * @param parecer the parecer to set
     */
    public void setParecer(ParecerAnalise parecer) {
        this.parecerAnalise = parecer;
    }
    /**
     * @return the loteTitulacao
     */
    /**
     * @return the usuario
     */
}
