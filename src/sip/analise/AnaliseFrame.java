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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import sip.analista.Analista;
import sip.analista.AnalistaBD;
import sip.distribuicao.Distribuicao;
import sip.distribuicao.DistribuicaoBD;
import sip.distribuicao.DistribuicaoFrame;
import sip.processo.Processo;
import sip.processo.ProcessoFrame;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class AnaliseFrame extends javax.swing.JInternalFrame {

    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Analise> listAnalise;
    private List<Analise> listAnaliseParecer;
    private List<ParecerAnalise> listParecerAnalise;
    private int modo;
    private int modoSeleciona;
    private EmissaoLicencaFrame emissaoTituloFrame;
    private ParecerAnaliseFrame parecerAnaliseFrame;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private Distribuicao distribuicao;
    private List<Distribuicao> listDistribuicao;
    private Processo processo;
    private Integer idProcesso;
    private String idAnalise;
    private String SituacaoParecer;//viavel não-viavel
    JPopupMenu jPopupMenu = new JPopupMenu();

    /**
     * Creates new form ClienteFrame
     */
    public AnaliseFrame() {
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

    public AnaliseFrame(EmissaoLicencaFrame emissaoTituloFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.emissaoTituloFrame = emissaoTituloFrame;
        modoSeleciona = Constantes.INCLUIR;
        formataData();
    }

    public AnaliseFrame(ParecerAnaliseFrame parecerFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.parecerAnaliseFrame = parecerFrame;
        modoSeleciona = Constantes.PARECERANALISE_FRAME;
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
                    consultaParecerAnalise();
                }
            }
        });
        item2.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                setSituacaoParecer("NÃO VIAVEL");
                if (getSituacaoParecer() != null) {
                    buscaDistribuicao();
                    consultaParecerAnalise();
                }
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

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblAnalise.getModel();
        listModel = tblAnalise.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheAnalise();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblAnalise.getColumnModel().getColumn(0).setPreferredWidth(100);
            tblAnalise.getColumnModel().getColumn(1).setPreferredWidth(350);
            tblAnalise.getColumnModel().getColumn(2).setPreferredWidth(1000);
            tblAnalise.getColumnModel().getColumn(3).setPreferredWidth(800);
            tblAnalise.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblAnalise.getColumnModel().getColumn(5).setPreferredWidth(300);
            tblAnalise.getColumnModel().getColumn(6).setPreferredWidth(200);

        } catch (ParseException ex) {
            Logger.getLogger(AnaliseFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        AnaliseBD analiseBD = new AnaliseBD();

        if (txtFiltro.getText().equals("")) {
            listAnalise = analiseBD.consultaAnalise();
        } else {
            String valor = txtFiltro.getText();
            listAnalise = analiseBD.consultaAnaliseNome(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listAnalise.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listAnalise.get(i).getId(),
                listAnalise.get(i).getProcesso().getNumProcesso(),
                listAnalise.get(i).getProcesso().getTipoLicenca(),
                listAnalise.get(i).getRequerente().getNome(),
                listAnalise.get(i).getAnalista().getNome(),
                simpleDateFormat.format(listAnalise.get(i).getDataAnalise()),
                listAnalise.get(i).getUsuario().getNome()
            });
        }

    }

    private void consultaParecerAnalise() {
        AnaliseBD analiseBD = new AnaliseBD();
        if (txtFiltro.getText().trim().equals("")) {
            listAnaliseParecer = analiseBD.consultaParecerAnalise();
        } else {
            //String valor = txtFiltroProcesso.getText();
            //listProcAnexos = processoBD.consultaAnexosProcessoNome(valor, valor);
        }
        detalheParecer();
    }

    private void detalheParecer() {
        if (tblAnalise.getSelectedRow() != -1) {
            listParecerAnalise = listAnaliseParecer.get(tblAnalise.getSelectedRow()).getListParecerAnalise();
            //removeAnexosProcessoTabela();     
        }
        //int indice = tblAnalise.getSelectedRow();
        //String teste = listParecerAnalise.get(0).getAnexoIncluso();

        if ("SIM".equals(listParecerAnalise.get(0).getAnexoIncluso())) {

            if (listParecerAnalise.size() > 1) {
                JOptionPane.showMessageDialog(this, "Lance todos parecer para finalizar o processo!", "Observação", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if ("NAO".equals(listDistribuicao.get(0).getProcesso().getArquivado())) {
                    try {
                        //JOptionPane.showMessageDialog(this, "Tramita!", "teste", JOptionPane.INFORMATION_MESSAGE);
                        executaTramitacaoConclusaoAnalise();
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
            JOptionPane.showMessageDialog(this, "Dever concluir o parecer técnico para finalizar", "Parecer", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void mostraDetalheAnalise() {
        if (tblAnalise.getSelectedRow() != -1) {
            int indice = tblAnalise.getSelectedRow();
            txtIdDistribuicao.setText(listAnalise.get(indice).getDistribuicao().getId().toString());
            txtNumeroProcesso.setText(listAnalise.get(indice).getProcesso().getNumProcesso());
            txtNomeRequerente.setText(listAnalise.get(indice).getRequerente().getNome());
            jdcData.setDate(listAnalise.get(indice).getDataAnalise());

        } else {
            //txtNome.setText("");
            //txtDescricao.setText("");
        }
    }

    private void subtraiEntradaAnalista() {
        int indice = tblAnalise.getSelectedRow();
        int qtde = 1;

        Analista analista = new Analista();
        analista.setId(listAnalise.get(indice).getAnalista().getId());
        analista.setQtdeSaida(listAnalise.get(indice).getAnalista().getQtdeSaida() + qtde);

        AnalistaBD analistaBD = new AnalistaBD();
        if (analistaBD.alteraAnalistaSaida(analista)) {
            alteraAnaliseConcluido();
            JOptionPane.showMessageDialog(this, "Processo subtraido do Analista", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            desabilitaBotoes();
            desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro enviar dado analista!", "Erro", JOptionPane.ERROR_MESSAGE);
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

    private void incluiAnalise() {
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

            Analise analise = new Analise();
            ftfMes.setValue(jdcData.getDate().getTime());

            analise.setUsuario(getUsuario());
            analise.setDistribuicao(getDistribuicao());
            analise.setDataAnalise(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            analise.setAno(ano);
            analise.setConcluido("NAO");

            AnaliseBD analiseBD = new AnaliseBD();
            if (analiseBD.incluiAnalise(analise)) {
                JOptionPane.showMessageDialog(this, "Analise cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                try {
                    executaTramitacao();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente a Analise já existe!", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraAnalise() {
        if (distribuicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição!", "Distribuição", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Analise analise = new Analise();
            ftfMes.setValue(jdcData.getDate().getTime());

            analise.setId(this.listAnalise.get(tblAnalise.getSelectedRow()).getId());
            analise.setDataAnalise(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            analise.setAno(ano);

            AnaliseBD analiseBD = new AnaliseBD();
            if (analiseBD.alteraAnalise(analise)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                tblAnalise.setEnabled(true);
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraAnaliseConcluido() {
        java.util.Date pega = jdcData.getDate();
        SimpleDateFormat formato = new SimpleDateFormat("yyyy");
        String ano = formato.format(pega);

        Analise analise = new Analise();
        ftfMes.setValue(jdcData.getDate().getTime());

        analise.setId(this.listAnalise.get(tblAnalise.getSelectedRow()).getId());
        analise.setConcluido("SIM");

        AnaliseBD analiseBD = new AnaliseBD();
        if (analiseBD.alteraAnaliseConcluido(analise)) {
            JOptionPane.showMessageDialog(this, "Analise concluida com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //atualizaTabela();
            //desabilitaBotoes();
            //desabilitaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao concluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }

    private void exlcuiAnalise() {
        AnaliseBD loteBD = new AnaliseBD();
        if (loteBD.excluiAnalise(listAnalise.get(tblAnalise.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Tramitação !", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
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
        //tblAnalise.setRowSelectionInterval(0, 0);
        //int indice = tblAnalise.getSelectedRow();
        //int idProc = listAnalise.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "RECEBIDO DO DMA" + " " + "ANALISE";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("RECEBIDO DO DMA");
        tram.setParecer(" ");
        tram.setSetor("ANALISE");
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

    private void executaTramitacaoConclusaoAnalise() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);

        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());

        //int ultima = tableModel.getRowCount() - 1;  
        //if (ultima > -1) { 
        //}
        //tblAnalise.setRowSelectionInterval(0, 0);
        //int indice = tblAnalise.getSelectedRow();
        //int idProc = listAnalise.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "PARECERES DA ANÁLISE FINALIZADO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("PARECERES DA ANALISE FINALIZADO");
        tram.setParecer(getSituacaoParecer());//VIAVEL OU NÃO VIAVEL
        tram.setSetor("ANÁLISE");
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
        //tblAnalise.setRowSelectionInterval(0, 0);
        //int indice = tblAnalise.getSelectedRow();
        //int idProc = listAnalise.get(indice).getId();
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        String controle = getDistribuicao().getProcesso().getId() + " " + "ENVIADO" + " " + " " + "ANALISE" + " " + "DMA";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("ENVIADO");
        tram.setParecer(" ");//VIAVEL OU NÃO VIAVEL
        tram.setSetor(" ");
        tram.setSetorOrigem("ANÁLISE");
        tram.setSetorDestino("DMA");
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
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

    private void selecionaAnaliseParecerAnalise() {
        if (tblAnalise.getSelectedRow() != -1) {
            parecerAnaliseFrame.setAnalise(listAnalise.get(tblAnalise.getSelectedRow()));
            this.dispose();
            parecerAnaliseFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma analise da lista!", "Analise", JOptionPane.INFORMATION_MESSAGE);
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
        listDistribuicao = distribuicaoBD.consultaDistribuicao("AnaliseFrame",0);
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
        tblAnalise = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
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
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/1006_64x64.png"))); // NOI18N
        jLabel1.setText("Análise Técnico");
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

        tblAnalise.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblAnalise.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Tipo Processo", "Requerente", "Analista", "Data Análise", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblAnalise.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblAnalise.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblAnaliseMouseClicked(evt);
            }
        });
        tblAnalise.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblAnaliseKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblAnalise);

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

        jPanel2.setLayout(new java.awt.GridBagLayout());

        btnAnexar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/ANEXAR_16x16.png"))); // NOI18N
        btnAnexar.setText("Anexos");
        btnAnexar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnexarActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
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
        gridBagConstraints.gridwidth = 3;
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
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Requerente"));
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jTabbedPane1.addTab("Lançamento", jPanel2);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        btnParecerT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/prancheta_64x64.png"))); // NOI18N
        btnParecerT1.setText("Parecer / Notificação");
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
        AnaliseBD nacionalidadeBD = new AnaliseBD();
        if (nacionalidadeBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblAnalise.setEnabled(false);
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
            incluiAnalise();
            tblAnalise.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraAnalise();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblAnalise.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblAnalise.getSelectedRow() != -1) {
            buscaDistribuicao();
            habilitaCampos();
            txtIdDistribuicao.setEnabled(false);
            habilitaBotoes();
            btnSelecionaDistribuicao.setEnabled(false);
            modo = Constantes.EDIT_MODE;
            tblAnalise.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblAnalise.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Nacionalidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiAnalise();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void tblAnaliseMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblAnaliseMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.PARECERANALISE_FRAME) {
                selecionaAnaliseParecerAnalise();
                dispose();
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblAnaliseMouseClicked

    private void tblAnaliseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblAnaliseKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modoSeleciona == Constantes.PARECERANALISE_FRAME) {
                selecionaAnaliseParecerAnalise();
                dispose();
            }
        }
        evt.consume();
    }//GEN-LAST:event_tblAnaliseKeyPressed

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
        if (tblAnalise.getSelectedRow() != -1) {
            int indice = tblAnalise.getSelectedRow();

            setIdProcesso(listAnalise.get(indice).getProcesso().getId());
            //int idProcesso = listAnalise.get(indice).getProcesso().getId();
            ProcessoFrame processoFrame = new ProcessoFrame(this);
            processoFrame.setVisible(true);
            this.getDesktopPane().add(processoFrame);
            processoFrame.toFront();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }

    }//GEN-LAST:event_btnAnexarActionPerformed

    private void btnParecerT1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnParecerT1ActionPerformed
        if (tblAnalise.getSelectedRow() != -1) {
            int indice = tblAnalise.getSelectedRow();

            setIdAnalise(listAnalise.get(indice).getId().toString());
            setIdProcesso(listAnalise.get(indice).getProcesso().getId());

            ParecerAnaliseFrame parecerAnalise = new ParecerAnaliseFrame(this);
            parecerAnalise.setVisible(true);
            this.getDesktopPane().add(parecerAnalise);
            parecerAnalise.toFront();// TODO add your handling code here:

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
            if (tblAnalise.getSelectedRow() != -1) {
                //mostra na tela
                jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());

            } else {
                JOptionPane.showMessageDialog(this, "Selecione uma analise da lista!", "Cadastro Imobiliario", JOptionPane.INFORMATION_MESSAGE);
            }

        }

    }//GEN-LAST:event_btnTramitarMouseClicked
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnAnexar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnParecerT1;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaDistribuicao;
    private javax.swing.JButton btnSelecionaUsuario;
    private javax.swing.JButton btnTramitar;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel jlClipe;
    private javax.swing.JTable tblAnalise;
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
        txtNomeRequerente.setText(distribuicao.getRequerente().getNome());
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
     * @return the idAnalise
     */
    public String getIdAnalise() {
        return idAnalise;
    }

    /**
     * @param idAnalise the idAnalise to set
     */
    public void setIdAnalise(String idAnalise) {
        this.idAnalise = idAnalise;
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
