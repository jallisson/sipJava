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

import sip.gabinete.Gabinete;
import sip.menulogin.Menu;
import sip.util.Constantes;
import sip.emissaolicenca.EmissaoLicencaFrame;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import sip.autorizacaoeventos.AutorizacaoEventosFrame;
import sip.distribuicao.Distribuicao;
import sip.distribuicao.DistribuicaoBD;
import sip.distribuicao.DistribuicaoFrame;
import sip.gabinete.GabineteBD;
import sip.gabinete.GabineteFrame;
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
public class JuridicoFrame extends javax.swing.JInternalFrame {

    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Juridico> listJuridico;
    private List<Juridico> listJuridicoP;
    private List<ParecerJuridico> listParecerJuridico;
    private List<Gabinete> listGabinete;
    private int modo;
    private int modoSeleciona;
    private EmissaoLicencaFrame emissaoLicencaFrame;
    private ParecerJuridicoFrame parecerJuridicoFrame;
    private AutorizacaoEventosFrame autorizacaoEventosFrame;
    private GabineteFrame gabineteFrame;
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
    private String tramitaParaOnde;

    /** Creates new form ClienteFrame */
    public JuridicoFrame() {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        ftfMesTramite.setVisible(false);
        jPanel19.setVisible(false);
        jlClipe.setVisible(false);
        //popupBtnTramitar();
    }

    public JuridicoFrame(EmissaoLicencaFrame emissaoTituloFrame) {
        initComponents();
        defineModelo();
        formataData();
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        jTabbedPane1.removeAll();
        jlClipe.setVisible(false);
        this.emissaoLicencaFrame = emissaoTituloFrame;
        modoSeleciona = Constantes.EMISSAOLICENCAFRAME;
        formataData();
    }

    public JuridicoFrame(AutorizacaoEventosFrame autorizacaoEventosFrame) {
        initComponents();
        defineModelo();
        formataData();
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        jTabbedPane1.removeAll();
        jlClipe.setVisible(false);
        this.autorizacaoEventosFrame = autorizacaoEventosFrame;
        modoSeleciona = Constantes.AUTORIZACAOEVENTOS_FRAME;
        formataData();
    }

    public JuridicoFrame(ParecerJuridicoFrame parecerFrame) {
        initComponents();
        defineModelo();
        formataData();
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        jTabbedPane1.removeAll();
        jlClipe.setVisible(false);
        this.parecerJuridicoFrame = parecerFrame;
        modoSeleciona = Constantes.PARECERJURIDICO_FRAME;
    }

    public JuridicoFrame(GabineteFrame gabineteFrame) {
        initComponents();
        defineModelo();
        formataData();
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        jTabbedPane1.removeAll();
        jlClipe.setVisible(false);
        this.gabineteFrame = gabineteFrame;
        modoSeleciona = Constantes.GABINETE_FRAME;
        jTabbedPane1.removeAll();
        jPanel4.setVisible(false);
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

    private void popupBtnTramitar() {
        //cria os itens
        JMenuItem item1 = new JMenuItem("DEFERIDO");
        JMenuItem item2 = new JMenuItem("INDEFERIDO");
        //cria o menu popup e acrescenta os itens

        jPopupMenu.add(item1);
        jPopupMenu.addSeparator();
        jPopupMenu.add(item2);

        //jPopupMenu.show(btnTramitar, 10, 10);

        //atribui uma ação para os itens
        item1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setSituacaoParecer("DEFERIDO");
                if (getSituacaoParecer() != null) {
                    buscaDistribuicao();
                    consultaParecerJuridico();
                }
            }
        });
        item2.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                setSituacaoParecer("INDEFERIDO");
                if (getSituacaoParecer() != null) {
                    buscaDistribuicao();
                    consultaParecerJuridico();
                }
            }
        });
    }

    private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        java.util.Date date = new java.util.Date();
        jdcData.setDate(date);

        jPanel7.add(jdcData);
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblJuridico.getModel();
        listModel = tblJuridico.getSelectionModel();
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

            tblJuridico.getColumnModel().getColumn(0).setPreferredWidth(50);//0
            tblJuridico.getColumnModel().getColumn(1).setPreferredWidth(150);//1
            tblJuridico.getColumnModel().getColumn(2).setPreferredWidth(500);//2
            tblJuridico.getColumnModel().getColumn(3).setPreferredWidth(200);//3
            tblJuridico.getColumnModel().getColumn(4).setPreferredWidth(150);//4
            tblJuridico.getColumnModel().getColumn(5).setPreferredWidth(100);//5



        } catch (ParseException ex) {
            Logger.getLogger(JuridicoFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        JuridicoBD analiseBD = new JuridicoBD();

        if (txtFiltro.getText().equals("")) {
            listJuridico = analiseBD.consultaJuridico();
        } else {
            String valor = txtFiltro.getText();
            listJuridico = analiseBD.consultaJuridicoNome(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listJuridico.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                        listJuridico.get(i).getId(),
                        listJuridico.get(i).getProcesso().getNumProcesso(),
                        listJuridico.get(i).getProcesso().getTipoLicenca(),
                        listJuridico.get(i).getRequerente().getNome(),
                        simpleDateFormat.format(listJuridico.get(i).getDataJuridico()),
                        listJuridico.get(i).getUsuario().getNome()
                    });
        }

    }

    private void consultaParecerJuridico() {
        JuridicoBD juridicoBD = new JuridicoBD();
        if (txtFiltro.getText().trim().equals("")) {
            listJuridicoP = juridicoBD.consultaParecerJuridico();
        } else {
            //String valor = txtFiltroProcesso.getText();
            //listProcAnexos = processoBD.consultaAnexosProcessoNome(valor, valor);
        }
        detalheParecer();
    }

    private void detalheParecer() {
        if (tblJuridico.getSelectedRow() != -1) {
            listParecerJuridico = listJuridicoP.get(tblJuridico.getSelectedRow()).getListParecerJuridico();
            //removeAnexosProcessoTabela();     
        }

        if (listParecerJuridico.size() < 2) {
            JOptionPane.showMessageDialog(this, "Lance os dois parecer para finalizar o processo!", "Observação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                //JOptionPane.showMessageDialog(this, "Tramita!", "teste", JOptionPane.INFORMATION_MESSAGE);
                executaTramitJurDma();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void mostraDetalheAnalise() {
        if (tblJuridico.getSelectedRow() != -1) {
            int indice = tblJuridico.getSelectedRow();
            txtIdDistribuicao.setText(listJuridico.get(indice).getDistribuicao().getId().toString());
            txtNumeroProcesso.setText(listJuridico.get(indice).getProcesso().getNumProcesso());
            txtNomeRequerente.setText(listJuridico.get(indice).getRequerente().getNome());
            jdcData.setDate(listJuridico.get(indice).getDataJuridico());

        } else {
            //txtNome.setText("");
            //txtDescricao.setText("");
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

    private void incluiJuridico() {
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

            Juridico juridico = new Juridico();
            ftfMes.setValue(jdcData.getDate().getTime());

            juridico.setUsuario(getUsuario());
            juridico.setDistribuicao(getDistribuicao());
            juridico.setDataJuridico(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            juridico.setTramitouGabinete("NAO");
            juridico.setTramitouEmissao("NAO");


            JuridicoBD analiseBD = new JuridicoBD();
            if (analiseBD.incluiJuridico(juridico)) {
                JOptionPane.showMessageDialog(this, "Juridico cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                try {
                    executaTramitacao();
                } catch (ParseException ex) {
                    ex.printStackTrace();
                }
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente os dados já existe!", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraJuridico() {
        if (distribuicao == null) {
            JOptionPane.showMessageDialog(this, "Selecione uma distribuição!", "Distribuição", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Juridico analise = new Juridico();
            ftfMes.setValue(jdcData.getDate().getTime());

            analise.setId(this.listJuridico.get(tblJuridico.getSelectedRow()).getId());
            analise.setDataJuridico(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));

            JuridicoBD analiseBD = new JuridicoBD();
            if (analiseBD.alteraJuridico(analise)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                tblJuridico.setEnabled(true);
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void alteraJurTramGabinete() {
        Juridico juridico = new Juridico();

        juridico.setId(listJuridico.get(tblJuridico.getSelectedRow()).getId());

        juridico.setTramitouGabinete("SIM");

        JuridicoBD juridicoBD = new JuridicoBD();

        if (juridicoBD.alteraJurTramiGabinete(juridico)) {
            tramitaParaOnde = "gabinete";

            int indice = tblJuridico.getSelectedRow();
            listJuridicoP = juridicoBD.consultaParecerJuridico();
            listParecerJuridico = listJuridicoP.get(tblJuridico.getSelectedRow()).getListParecerJuridico();

            try {
                if ("DEFERIDO".equals(listParecerJuridico.get(indice).getParecer())) {
                    buscaDistribuicao();
                    try {
                        executaTramitJurDma();
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "O PROCESSO NÃO FOI DEFERIDO", "NÃO DEFERIDO", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (IndexOutOfBoundsException e) {
                JOptionPane.showMessageDialog(this, "Não existe dados no parecer do jurídico", "Lançamento", JOptionPane.INFORMATION_MESSAGE);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void alteraJurTramEmissao() {
        GabineteBD parecerBD = new GabineteBD();
        int indice = tblJuridico.getSelectedRow();
        listGabinete = parecerBD.consultaGabinete();


        try {
            if ("SIM".equals(listGabinete.get(indice).getTramitouJuridico())) {
                Juridico juridico = new Juridico();

                juridico.setId(listJuridico.get(tblJuridico.getSelectedRow()).getId());

                juridico.setTramitouEmissao("SIM");

                JuridicoBD juridicoBD = new JuridicoBD();

                if (juridicoBD.alteraJurTramiEmissao(juridico)) {
                    tramitaParaOnde = "dma";


                    listJuridicoP = juridicoBD.consultaParecerJuridico();
                    listParecerJuridico = listJuridicoP.get(tblJuridico.getSelectedRow()).getListParecerJuridico();

                    try {
                        if ("DEFERIDO".equals(listParecerJuridico.get(indice).getParecer())) {
                            buscaDistribuicao();
                            try {
                                executaTramitJurDma();
                            } catch (ParseException ex) {
                                ex.printStackTrace();
                            }
                        } else {
                            JOptionPane.showMessageDialog(this, "O PROCESSO NÃO FOI DEFERIDO", "NÃO DEFERIDO", JOptionPane.INFORMATION_MESSAGE);
                        }
                    } catch (IndexOutOfBoundsException e) {
                        JOptionPane.showMessageDialog(this, "Não existe dados do gabinete", "gabinete", JOptionPane.INFORMATION_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(this, "Erro ao tramitar para Emissao!", "Erro", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "O PROCESSO NÃO FOI TRAMITADO DO GABINETE", "NÃO DEFERIDO", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IndexOutOfBoundsException e) {
            JOptionPane.showMessageDialog(this, "Não existe dados no parecer do jurídico", "Lançamento", JOptionPane.INFORMATION_MESSAGE);
        }

    }

    private void exlcuiJuridico() {
        JuridicoBD loteBD = new JuridicoBD();
        if (loteBD.excluiJuridico(listJuridico.get(tblJuridico.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
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


        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());

        //int ultima = tableModel.getRowCount() - 1;  
        //if (ultima > -1) { 

        //}
        //tblAnalise.setRowSelectionInterval(0, 0);
        //int indice = tblAnalise.getSelectedRow();
        //int idProc = listAnalise.get(indice).getId();
        String controle = getDistribuicao().getProcesso().getId() + " " + "RECEBIDO DO DMA" + " " + "JURIDICO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMes.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("RECEBIDO DO DMA");
        tram.setParecer(" ");
        tram.setSetor("JURIDICO");
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

    private void executaTramitJurDma() throws ParseException {
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

        //int ultima = tableModel.getRowCount() - 1;  
        //if (ultima > -1) { 

        //}
        //tblAnalise.setRowSelectionInterval(0, 0);
        //int indice = tblAnalise.getSelectedRow();
        //int idProc = listAnalise.get(indice).getId();
        String controle = null;
        switch (tramitaParaOnde) {
            case "gabinete":
                controle = getDistribuicao().getProcesso().getId() + " " + "ENVIADO" + " " + " " + "JURIDICO" + " " + "GABINETE";
                break;
            case "dma":
                controle = getDistribuicao().getProcesso().getId() + " " + "ENVIADO PARA EMISSÃO DA LICENÇA" + " " + " " + "JURIDICO" + " " + "DMA";
                break;
        }

        tram.setUsuario(getUsuario());
        tram.setProcesso(getDistribuicao().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        switch (tramitaParaOnde) {
            case "gabinete":
                tram.setStatus("ENVIADO");
                break;
            case "dma":
                tram.setStatus("ENVIADO PARA EMISSÃO DA LICENÇA");
                break;
        }
        tram.setParecer(" ");//VIAVEL OU NÃO VIAVEL
        tram.setSetor(" ");
        switch (tramitaParaOnde) {
            case "gabinete":
                tram.setSetorOrigem("JURÍDICO");
                tram.setSetorDestino("GABINETE");
                break;
            case "dma":
                tram.setSetorOrigem("JURÍDICO");
                tram.setSetorDestino("DMA");
                break;
        }
        tram.setObservacao(null);
        tram.setControle(controle);
        tram.setSituacaoCad(" ");

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            setSituacaoParecer(null);
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

    private void selecionaJuridicoEmissao() {
        if (tblJuridico.getSelectedRow() != -1) {
            emissaoLicencaFrame.setJuridico(listJuridico.get(tblJuridico.getSelectedRow()));
            this.dispose();
            emissaoLicencaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaJuridicoAutorizacaoEventos() {
        if (tblJuridico.getSelectedRow() != -1) {
            autorizacaoEventosFrame.setJuridico(listJuridico.get(tblJuridico.getSelectedRow()));
            this.dispose();
            autorizacaoEventosFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaJuridicoParecerJuridico() {
        if (tblJuridico.getSelectedRow() != -1) {
            parecerJuridicoFrame.setJuridico(listJuridico.get(tblJuridico.getSelectedRow()));
            this.dispose();
            parecerJuridicoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaJuridicoGabinete() {
        if (tblJuridico.getSelectedRow() != -1) {
            gabineteFrame.setJuridico(listJuridico.get(tblJuridico.getSelectedRow()));
            this.dispose();
            gabineteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
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
        jlClipe = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblJuridico = new javax.swing.JTable();
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
        btnTramitarGabinete = new javax.swing.JButton();
        btnTramitarDma = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Lote Emissão Titulo\n");
        setPreferredSize(new java.awt.Dimension(750, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/juridico_64x64.png"))); // NOI18N
        jLabel1.setText("Jurídico");
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

        tblJuridico.setFont(new java.awt.Font("Tahoma", 0, 14));
        tblJuridico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Tipo Processo", "Requerente", "Data", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblJuridico.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblJuridico.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblJuridicoMouseClicked(evt);
            }
        });
        tblJuridico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblJuridicoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblJuridico);

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
        txtNomeUsuario.setFont(new java.awt.Font("Verdana", 1, 12));
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
        jPanel21.add(txtNomeRequerente, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Recebimento Jurídico"));
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

        btnParecerT1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/conclusivo_64x64.png"))); // NOI18N
        btnParecerT1.setText("Parecer / Notificação");
        btnParecerT1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnParecerT1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        jPanel5.add(btnParecerT1, gridBagConstraints);

        btnTramitarGabinete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_64x64.png"))); // NOI18N
        btnTramitarGabinete.setText("Tramitar GABINETE");
        btnTramitarGabinete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTramitarGabineteMouseClicked(evt);
            }
        });
        btnTramitarGabinete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarGabineteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(btnTramitarGabinete, gridBagConstraints);

        btnTramitarDma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_64x64.png"))); // NOI18N
        btnTramitarDma.setText("Tramitar EMISSÃO LICENÇA");
        btnTramitarDma.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTramitarDmaMouseClicked(evt);
            }
        });
        btnTramitarDma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarDmaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(btnTramitarDma, gridBagConstraints);

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
        JuridicoBD nacionalidadeBD = new JuridicoBD();
        if (nacionalidadeBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblJuridico.setEnabled(false);
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
            incluiJuridico();
            tblJuridico.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraJuridico();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblJuridico.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblJuridico.getSelectedRow() != -1) {
            buscaDistribuicao();
            habilitaCampos();
            txtIdDistribuicao.setEnabled(false);
            habilitaBotoes();
            btnSelecionaDistribuicao.setEnabled(false);
            modo = Constantes.EDIT_MODE;
            tblJuridico.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblJuridico.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Nacionalidade?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiJuridico();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um nacionalidade da lista!", "Nacionalidade", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void tblJuridicoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblJuridicoMouseClicked
        if (evt.getClickCount() == 2) {
            if (modoSeleciona == Constantes.EMISSAOLICENCAFRAME) {
                selecionaJuridicoEmissao();
                dispose();
            } else if (modoSeleciona == Constantes.AUTORIZACAOEVENTOS_FRAME) {
                selecionaJuridicoAutorizacaoEventos();
                dispose();
            } else if (modoSeleciona == Constantes.PARECERJURIDICO_FRAME) {
                selecionaJuridicoParecerJuridico();
                dispose();
            } else if (modoSeleciona == Constantes.GABINETE_FRAME) {
                selecionaJuridicoGabinete();
                dispose();
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblJuridicoMouseClicked

    private void tblJuridicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblJuridicoKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (modoSeleciona == Constantes.EMISSAOLICENCAFRAME) {
                selecionaJuridicoEmissao();
                dispose();
            } else if (modoSeleciona == Constantes.PARECERJURIDICO_FRAME) {
                selecionaJuridicoParecerJuridico();
                dispose();
            } else if (modoSeleciona == Constantes.GABINETE_FRAME) {
                selecionaJuridicoGabinete();
                dispose();
            }
        }
        evt.consume();
    }//GEN-LAST:event_tblJuridicoKeyPressed

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
        if (tblJuridico.getSelectedRow() != -1) {
            int indice = tblJuridico.getSelectedRow();

            setIdProcesso(listJuridico.get(indice).getProcesso().getId());
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
        if (tblJuridico.getSelectedRow() != -1) {
            int indice = tblJuridico.getSelectedRow();

            setIdAnalise(listJuridico.get(indice).getId().toString());

            ParecerJuridicoFrame parecerAnalise = new ParecerJuridicoFrame(this);
            parecerAnalise.setVisible(true);
            this.getDesktopPane().add(parecerAnalise);
            parecerAnalise.toFront();// TODO add your handling code here:

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnParecerT1ActionPerformed

    private void btnTramitarGabineteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarGabineteActionPerformed
        if (tblJuridico.getSelectedRow() != -1) {
            int indice = tblJuridico.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                if ("NAO".equals(listJuridico.get(indice).getProcesso().getArquivado())) {
                    alteraJurTramGabinete();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarGabineteActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void btnTramitarGabineteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTramitarGabineteMouseClicked
        /*if (evt.getButton() == MouseEvent.BUTTON1) { // Clique com botao direito do mouse.     
        if (tblJuridico.getSelectedRow() != -1) {
        //mostra na tela
        jPopupMenu.show(evt.getComponent(), evt.getX(), evt.getY());
        
        } else {
        JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
        
        }*/
    }//GEN-LAST:event_btnTramitarGabineteMouseClicked

    private void btnTramitarDmaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTramitarDmaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarDmaMouseClicked

    private void btnTramitarDmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarDmaActionPerformed
        if (tblJuridico.getSelectedRow() != -1) {
            int indice = tblJuridico.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                if ("NAO".equals(listJuridico.get(indice).getProcesso().getArquivado())) {
                    alteraJurTramEmissao();
                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                }
            }

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        } // TODO add your handling code here:
    }//GEN-LAST:event_btnTramitarDmaActionPerformed
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
    private javax.swing.JButton btnTramitarDma;
    private javax.swing.JButton btnTramitarGabinete;
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
    private javax.swing.JTable tblJuridico;
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
