/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.juridico;

import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
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
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
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
import javax.swing.ListSelectionModel;
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
import net.sf.jasperreports.view.JasperViewer;
import sip.processo.AnexosProcesso;
import sip.processo.ProcessoFrame;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class ParecerJuridicoFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<ParecerJuridico> listParecer;
    private int modo;
    private int modoSeleciona;
    private Juridico juridico;
    private List<Juridico> listJuridico;
    private String loteUnico;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private AnexosProcesso anexosProcesso;
    private JuridicoFrame juridicoFrame;
    private ParecerJuridico parecerJuridico;
    private Processo processo;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private String parecer;
    private String tipoDoc;
    private String tipo;
    private List<Tramitacao> listTramitacao;
    private Tramitacao tramitacao;
    private String idLote;
    private Integer idProcesso;
    private int lastId;
    JPopupMenu jPopupMenu = new JPopupMenu();
    JMenuItem jMenuItemAlterar = new JMenuItem();
    private List<ParecerJuridico> listParecerProcAnexos;

    /** Creates new form ClienteFrame */
    public ParecerJuridicoFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        jdcData.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
    }

    public ParecerJuridicoFrame(JuridicoFrame juridicoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        this.juridicoFrame = juridicoFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        jdcData.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
        btnNovo.doClick();
        btnNovo.setVisible(false);
        txtIdJuridico.setText(juridicoFrame.getIdAnalise());
        MenuJtableDetalhe();
        buscaJuridico();
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
            ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
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
            tblParecer.getColumnModel().getColumn(6).setPreferredWidth(100);




        } catch (ParseException ex) {
            Logger.getLogger(ParecerJuridicoFrame.class.getName()).log(Level.SEVERE, null, ex);
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
                case "PARECER": {
                    JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\parecer\\juridico\\parecer.jasper", parametros, acessoBd.conectar());
                    JOptionPane.showMessageDialog(this, "Aquarde enquanto é gerado!", "Informação", JOptionPane.INFORMATION_MESSAGE);
                    JasperViewer.viewReport(jp, false);
                    break;
                }
                case "NOTIFICAÇÃO": {
                    JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\parecer\\juridico\\notificacao.jasper", parametros, acessoBd.conectar());
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


        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();

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
                        listParecer.get(i).getParecer(),
                        simpleDateFormat.format(listParecer.get(i).getDataParecer()),
                        listParecer.get(i).getUsuario().getNome()
                    });
        }

    }

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        java.util.Date hoje = new java.util.Date();
        jdcData.setDate(hoje);

        jPanel7.add(jdcData);
    }

    private void mostraDetalheParecer() {
        if (tblParecer.getSelectedRow() != -1) {
            int indice = tblParecer.getSelectedRow();
            txtIdJuridico.setText(listParecer.get(indice).getJuridico().getId().toString());
            txtNomeRequerente.setText(listParecer.get(indice).getRequerente().getNome());
            jdcData.setDate(listParecer.get(indice).getDataParecer());
            txtNomeAnexo.setText(listParecer.get(indice).getAnexosProcesso().getDescricaoAnexo());
            switch (listParecer.get(indice).getParecer()) {
                case "TECNICO":
                    jRBPDeferido.setSelected(true);
                    break;
                case "CONCLUSIVO":
                    jRBPIndeferido.setSelected(true);
                    break;
            }


        } else {
             txtNomeAnexo.setText("");
            txtIdJuridico.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
            jRBPDeferido.setSelected(true);
        }
    }

    private void selecionaTipoParecer() {
        if (jRBPDeferido.isSelected()) {
            parecer = "DEFERIDO";
        } else if (jRBPIndeferido.isSelected()) {
            parecer = "INDEFERIDO";
        } else if (jRBNotificacao.isSelected()) {
            parecer = "NOTIFICAÇÃO";
        }
    }

    private void selecionaTipoDocumento() {
        if (jRBPDeferido.isSelected()) {
            tipoDoc = "PARECER JURÍDICO";
            tipo = "PARECER";
        } else if (jRBPIndeferido.isSelected()) {
            tipoDoc = "PARECER JURÍDICO";
            tipo = "PARECER";
        } else if (jRBNotificacao.isSelected()) {
            tipoDoc = "NOTIFICAÇÃO";
            tipo = "NOTIFICAÇÃO";
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
                try {
                    executaTramitacaoConcluido();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void executaTramitacaoEdicao() throws ParseException {
        selecionaTipoDocumento();

        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);


        Tramitacao tram = new Tramitacao();

        ftfMes.setValue(jdcDataTramite.getDate());
        ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
        listParecer = parecerBD.consultaParecer();
        lastId = parecerBD.getLastId();
        buscaParecer(lastId);


        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();
        String controle = getJuridico().getProcesso().getId() + " " + tipoDoc + " EM EDIÇÃO";


        tram.setUsuario(getUsuario());
        tram.setProcesso(getJuridico().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setStatus(tipoDoc + " EM EDIÇÃO");
        tram.setParecer(" ");
        tram.setSetor("JURÍDICO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        if (tipoDoc.equals(parecer)) {
            //tram.setParecer(tipoDoc);
            tram.setParecer("");
        } else {
            //tram.setParecer(parecer);
            tram.setParecer("");
        }
        tram.setObservacao(null);
        tram.setControle(controle);
        //tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerJuridico(getParecerJuridico());

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao lançada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoConcluido() throws ParseException {
        selecionaTipoDocumento();
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();

        ftfMes.setValue(jdcDataTramite.getDate());
        ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
        listParecer = parecerBD.consultaParecer();
        lastId = parecerBD.getLastId();
        buscaParecer(lastId);
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());
        
        //int indice = tblEmissao.getSelectedRow();
        //int idProcesso =  listEmissaoTitulo.get(indice).getProcesso().getId();
        String controle = getJuridico().getProcesso().getId() + " " + tipoDoc + " CONCLUIDO";


        tram.setUsuario(getUsuario());
        tram.setProcesso(getJuridico().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setStatus(tipoDoc + " CONCLUIDO");
        tram.setParecer(" ");
        tram.setSetor("JURÍDICO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        if (tipoDoc.equals(parecer)) {
            tram.setParecer(tipoDoc);
        } else {
            tram.setParecer(parecer);
        }
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerJuridico(getParecerJuridico());

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao lançada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoAnexoNao() throws ParseException {
        selecionaTipoDocumento();
        
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
        
        String controle = getJuridico().getProcesso().getId() + " " + tipoDoc + " CONCLUIDO";
        
        tram.setUsuario(getUsuario());
        tram.setProcesso(getJuridico().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setStatus(tipoDoc + " CONCLUIDO");
        tram.setParecer(" ");
        tram.setSetor("JURÍDICO");
        tram.setSetorOrigem(" ");
        tram.setSetorDestino(" ");
        if (tipoDoc.equals(parecer)) {
            tram.setParecer(tipoDoc);
        } else {
            tram.setParecer(parecer);
        }
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerJuridico(getParecerJuridico());

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao editada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void executaTramitacaoAnexoSim() throws SQLException {
        int indice = tblParecer.getSelectedRow();
        int IdParecer = listParecer.get(indice).getId();


        Tramitacao tram = new Tramitacao();

        tram.setAnexosProcesso(getAnexosProcesso());
        tram.setParecerJuridico(getParecerJuridico());


        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.alteraTramitacaoAnexoParecerJuridio(tram, IdParecer)) {
            JOptionPane.showMessageDialog(this, "tramitacao alterada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);


        } else {
            JOptionPane.showMessageDialog(this, "Erro na alteração da tramitação", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incluiParecer() {

        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (getJuridico() == null) {
            JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Juridico", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
        }else if (getJuridico().getProcesso().getArquivado().equals("SIM")) {
             JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);   
        } else {
            selecionaTipoDocumento();
            selecionaTipoParecer();
            ParecerJuridico parecerJur = new ParecerJuridico();
            String controle;
            ftfMes.setValue(jdcData.getDate());
            controle = getJuridico().getId() + " " + parecer;

            parecerJur.setUsuario(getUsuario());
            parecerJur.setJuridico(getJuridico());
            parecerJur.setDataParecer(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            parecerJur.setParecer(parecer);
            parecerJur.setControle(controle);
            parecerJur.setAnexosProcesso(getAnexosProcesso());
            parecerJur.setAnexoIncluso("NAO");
            parecerJur.setTipo(tipo);

            ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
            if (parecerBD.incluiParecer(parecerJur)) {
                JOptionPane.showMessageDialog(this, "Parecer " + this.parecer + " lançado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                try {
                    executaTramitacaoEdicao();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
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
                JOptionPane.showMessageDialog(this, "Possivelmente o parecer " + this.parecer + " já existe !", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraParecer() {
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (getJuridico() == null) {
            JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Juridico", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
        } else if (getAnexosProcesso() == null) {
            JOptionPane.showMessageDialog(this, "Informe o anexo!", "Anexo", JOptionPane.INFORMATION_MESSAGE);
            btnSelecionaAnexo.requestFocus();
        } else {
            selecionaTipoDocumento();
            selecionaTipoParecer();
            ParecerJuridico parecerJur = new ParecerJuridico();
            String controle;
            ftfMes.setValue(jdcData.getDate());
            controle = getJuridico().getId() + " " + parecer;

            parecerJur.setUsuario(getUsuario());
            parecerJur.setJuridico(getJuridico());
            parecerJur.setId(this.listParecer.get(tblParecer.getSelectedRow()).getId());
            parecerJur.setDataParecer(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            parecerJur.setParecer(this.parecer);
            parecerJur.setControle(controle);
            parecerJur.setAnexosProcesso(getAnexosProcesso());
            parecerJur.setAnexoIncluso("SIM");
            parecerJur.setTipo(tipo);

            ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
            if (parecerBD.alterarParecer(parecerJur)) {
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
        ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
        if (parecerBD.excluiParecer(listParecer.get(tblParecer.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            //JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o bairro!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        //txtNomeTécnico.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }

    private void limpaCampos() {
        {

            txtIdJuridico.setText("");
            txtNomeRequerente.setText("");
            txtNomeAnexo.setText("");

        }
    }

    private void habilitaCampos() {

        jdcData.setEnabled(true);
        txtIdJuridico.setEditable(true);
        btnSelecionaJuridico.setEnabled(true);
        btnSelecionaAnexo.setEnabled(true);
        jRBPDeferido.setEnabled(true);
        jRBPIndeferido.setEnabled(true);

    }

    private void desabilitaCampos() {
        jdcData.setEnabled(false);
        txtIdJuridico.setEditable(false);
        btnSelecionaJuridico.setEnabled(false);
        jRBPDeferido.setEnabled(false);
        jRBPIndeferido.setEnabled(false);
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

    public void setTramitacao(Tramitacao tramitacao) {
        this.tramitacao = tramitacao;
        //idLote = tramitacao.getLoteTitulacao().getId().toString();

    }

    private void buscaJuridico() {
        JuridicoBD juridicoBD = new JuridicoBD();
        listJuridico = juridicoBD.consultaJuridico();
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
                txtIdJuridico.setText("");
                txtNomeRequerente.setText("");
                juridico = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            juridico = null;
            txtNomeRequerente.setText("");
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
                //limpaCamposSemProcData();
                //setaDataAtual();
                //setTramitacao(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            //setTramitacao(null);
            //txtNumeroProcesso.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public void buscaParecer(int id) {
        ParecerJuridicoBD parecerBD = new ParecerJuridicoBD();
        listParecer = parecerBD.consultaParecer();
        int binario = 0;
        try {
            int max = listParecer.size();
            int id_busca = id;
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listParecer.get(i).getId();
                if (listParecer.get(i).getId() == id_busca) {
                    setParecerJuridico(listParecer.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                //txtIdAnalise.setText("");
                //txtNomeRequerente.setText("");
                setParecerJuridico(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            //analise = null;
            //txtNomeRequerente.setText("");
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

        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel13 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        ftfMes = new javax.swing.JFormattedTextField();
        jRBPIndeferido = new javax.swing.JRadioButton();
        jRBPDeferido = new javax.swing.JRadioButton();
        jRBNotificacao = new javax.swing.JRadioButton();
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
        btnSelecionaJuridico = new javax.swing.JButton();
        txtIdJuridico = new javax.swing.JTextField();
        jPanel21 = new javax.swing.JPanel();
        txtNomeAnexo = new javax.swing.JTextField();
        btnSelecionaAnexo = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Parecer Juridico");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/conclusivo_64x64.png"))); // NOI18N
        jLabel1.setText("PARECER / NOTIFICAÇÃO - JURÍDICO");
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
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Parecer/Notificação"));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        buttonGroup1.add(jRBPIndeferido);
        jRBPIndeferido.setText("Parecer Indeferido");
        jRBPIndeferido.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBPIndeferido, gridBagConstraints);

        buttonGroup1.add(jRBPDeferido);
        jRBPDeferido.setSelected(true);
        jRBPDeferido.setText("Parecer Deferido");
        jRBPDeferido.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBPDeferido, gridBagConstraints);

        buttonGroup1.add(jRBNotificacao);
        jRBNotificacao.setText("Notificação");
        jRBNotificacao.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.PAGE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jRBNotificacao, gridBagConstraints);

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

        tblParecer.setFont(new java.awt.Font("Verdana", 1, 12));
        tblParecer.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROCESSO", "LICENÇA", "REQUERENTE", "PARECER", "DATA", "USUARIO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
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
        btnImprimir.setText("Numeração");
        btnImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImprimirActionPerformed(evt);
            }
        });
        jPanel4.add(btnImprimir);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
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

        txtNomeRequerente.setEditable(false);
        txtNomeRequerente.setFont(new java.awt.Font("Verdana", 1, 12));
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
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel20, gridBagConstraints);

        jPanel21.setBackground(new java.awt.Color(255, 255, 255));
        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Anexo"));
        jPanel21.setLayout(new java.awt.GridBagLayout());

        txtNomeAnexo.setEditable(false);
        txtNomeAnexo.setFont(new java.awt.Font("Verdana", 1, 12));
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
        ParecerJuridicoBD parecerJurBD = new ParecerJuridicoBD();
        if (parecerJurBD.testaConexao()) {
            habilitaCampos();

            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblParecer.setEnabled(false);
            limpaSelecaoTabela();
            txtIdJuridico.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            //caixaAlta();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            String controle = getJuridico().getProcesso().getId() + " " + "PARECER JURIDICO CONCLUIDO ";
            incluiParecer();
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
            buscaJuridico();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblParecer.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Processo?", "Confirmação", JOptionPane.YES_NO_OPTION);
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
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

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
    private javax.swing.JButton btnSelecionaAnexo;
    private javax.swing.JButton btnSelecionaJuridico;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRBNotificacao;
    private javax.swing.JRadioButton jRBPDeferido;
    private javax.swing.JRadioButton jRBPIndeferido;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblParecer;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdJuridico;
    private javax.swing.JTextField txtNomeAnexo;
    private javax.swing.JTextField txtNomeRequerente;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the imovel
     */
    public Juridico getJuridico() {
        return juridico;
    }

    /**
     * @param imovel the imovel to set
     */
    public void setJuridico(Juridico juridico) {
        this.juridico = juridico;
        txtIdJuridico.setText(juridico.getId().toString());
        txtNomeRequerente.setText(juridico.getRequerente().getNome());
        String idProcesso = juridico.getProcesso().getId().toString();
        //buscaTramitacao(idProcesso);
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
     * @return the parecerJuridico
     */
    public ParecerJuridico getParecerJuridico() {
        return parecerJuridico;
    }

    /**
     * @param parecerJuridico the parecerJuridico to set
     */
    public void setParecerJuridico(ParecerJuridico parecerJuridico) {
        this.parecerJuridico = parecerJuridico;
    }
    /**
     * @return the loteTitulacao
     */
}
