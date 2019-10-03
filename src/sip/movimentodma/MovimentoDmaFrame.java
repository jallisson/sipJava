/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.movimentodma;

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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
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
import net.sf.jasperreports.view.JasperViewer;
import sip.analise.AnaliseFrame;
import sip.analista.Analista;
import sip.analista.AnalistaBD;
import sip.analista.AnalistaFrame;
import sip.juridico.JuridicoFrame;
import sip.lotedma.LoteDma;
import sip.lotedma.LoteDmaBD;
import sip.lotedma.LoteDmaFrame;
import sip.processo.Processo;
import sip.processo.ProcessoBD;
import sip.processo.ProcessoFrame;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class MovimentoDmaFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private JDateChooser jdcDataMudanca;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<MovimentoDma> listMovimento;
    private MovimentoDma movimento;
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
    String idUsuario = Menu.id_Usuario;
    String tipo = "Normal";
    String situacao = "Normal";
    private String id;
    private String consultaDeOnde = "MovimentoDmaFrame";
    private String tramitaParaOnde;
    private LoteDma loteDma;
    private List<LoteDma> listLoteDma;

    public MovimentoDmaFrame() {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idUsuario);

        jdcData.setEnabled(false);
    }

    public MovimentoDmaFrame(TramitacaoFrame tramitacaoFrame) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        this.tramitacaoFrame = tramitacaoFrame;
        modoSeleciona = Constantes.ALTERAR;

        buscaUsuario(idUsuario);

        jdcData.setEnabled(false);
        txtFiltroProcesso.requestFocus();
        //jRBNenhum.setVisible(false);

    }

    public MovimentoDmaFrame(EmissaoLicencaFrame emissao) {
        initComponents();
        defineModelo();
        formataData();
        caixaAlta();
        fechando();
        buscaUsuario(idUsuario);

        jdcData.setEnabled(false);
        txtFiltroProcesso.requestFocus();
        this.emissaoTituloFrame = emissao;
        modoSeleciona = Constantes.EMISSAOLICENCAFRAME;
        //jRBNenhum.setVisible(false);
    }

    public MovimentoDmaFrame(JuridicoFrame juridicoFrame) {
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
        this.juridicoFrame = juridicoFrame;
        modoSeleciona = Constantes.JURIDICO_FRAME;
        consultaDeOnde = "JuridicoFrame";
        //jRBNenhum.setVisible(false); 
    }

    public MovimentoDmaFrame(AnaliseFrame analiseFrame) {
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
        this.analiseFrame = analiseFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        consultaDeOnde = "AnaliseFrame";
        //jRBNenhum.setVisible(false); 
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
        tableModel = (DefaultTableModel) tblMovimento.getModel();
        listModel = tblMovimento.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheMovimento();
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

            tblMovimento.getColumnModel().getColumn(0).setMaxWidth(0);
            tblMovimento.getColumnModel().getColumn(0).setMinWidth(0);
            tblMovimento.getTableHeader().getColumnModel().getColumn(0).setMaxWidth(0);
            tblMovimento.getTableHeader().getColumnModel().getColumn(0).setMinWidth(0);

            //tblMovimento.getColumnModel().getColumn(0).setPreferredWidth(80);
            tblMovimento.getColumnModel().getColumn(1).setPreferredWidth(100);
            tblMovimento.getColumnModel().getColumn(2).setPreferredWidth(250);
            tblMovimento.getColumnModel().getColumn(3).setPreferredWidth(280);
            tblMovimento.getColumnModel().getColumn(4).setPreferredWidth(250);
            tblMovimento.getColumnModel().getColumn(5).setPreferredWidth(130);
            tblMovimento.getColumnModel().getColumn(6).setPreferredWidth(80);
            tblMovimento.getColumnModel().getColumn(7).setPreferredWidth(100);
            tblMovimento.getColumnModel().getColumn(8).setPreferredWidth(100);

        } catch (ParseException ex) {
            Logger.getLogger(MovimentoDmaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void centralizaJanelaRegVen(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 36,
                (this.getHeight() - internalFrame.getHeight()) / 12);
    }

    private void atualizaTabela() {
        MovimentoDmaBD movimentoBD = new MovimentoDmaBD();
        inserirZeroEsqPesq();

        if (txtFiltroProcesso.getText().equals("")) {
            listMovimento = movimentoBD.consultaMovimentoDma(consultaDeOnde);
        } else {
            String valor = txtFiltroProcesso.getText();
            listMovimento = movimentoBD.consultaProcessoNum(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listMovimento.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listMovimento.get(i).getId(),
                listMovimento.get(i).getProcesso().getNumProcesso(),
                listMovimento.get(i).getRequerente().getNome(),
                listMovimento.get(i).getProcesso().getTipoLicenca(),
                listMovimento.get(i).getLote().getDescricao(),
                listMovimento.get(i).getSituacao(),
                listMovimento.get(i).getMovimento(),
                simpleDateFormat.format(listMovimento.get(i).getDataMovimento()),
                
                listMovimento.get(i).getUsuario().getNome()
            });
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void mostraDetalheMovimento() {
        if (tblMovimento.getSelectedRow() != -1) {
            int indice = tblMovimento.getSelectedRow();

            txtIdProcesso.setText(listMovimento.get(indice).getProcesso().getId().toString());
            txtNumeroProcesso.setText(listMovimento.get(indice).getProcesso().getNumProcesso());
            txtNomeRequerente.setText(listMovimento.get(indice).getRequerente().getNome());
            txtNomeLote.setText(listMovimento.get(indice).getLote().getDescricao());
            txtIdLote.setText(listMovimento.get(indice).getLote().getId().toString());
            jdcData.setDate(listMovimento.get(indice).getDataMovimento());
            ftfMes.setText(listMovimento.get(indice).getMesAno());
            txtSituacao.setText(listMovimento.get(indice).getSituacao());
            cbMovimento.setSelectedItem(listMovimento.get(indice).getMovimento());

        } else {

            //txtIdAnalista.setText("");
            //txtNomeAnalista.setText("");
            //jdcData.setDate(null);
            //ftfMes.setText("");
        }
    }

    private void incluiMovimento() {
        if (getLoteDma() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um lote!", "Lote Dma", JOptionPane.INFORMATION_MESSAGE);

        } else if (getProcesso() == null) {
            JOptionPane.showMessageDialog(this, "Selecione um processo!", "Analista", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else if (txtSituacao.getText() == null) {
            JOptionPane.showMessageDialog(this, "Informe a situação", "Situação", JOptionPane.INFORMATION_MESSAGE);
        } else {
            MovimentoDma movi = new MovimentoDma();
            String controle;
            ftfMes.setValue(jdcData.getDate());
            controle = processo.getId() + " " + cbMovimento.getSelectedItem().toString() + loteDma.getId(); //+ " " + new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime());

            //selecaoSituacao();
            movi.setUsuario(getUsuario());
            movi.setProcesso(getProcesso());
            movi.setLote(getLoteDma());
            movi.setDataMovimento(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            movi.setMesAno(ftfMes.getText());
            movi.setSituacao(txtSituacao.getText());
            movi.setMovimento(cbMovimento.getSelectedItem().toString());
            movi.setControle(controle);

            MovimentoDmaBD movimentoBD = new MovimentoDmaBD();
            if (movimentoBD.incluiMovimentoDma(movi)) {
                JOptionPane.showMessageDialog(this, "Movimento cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                //somaEntradaAnalista();
                atualizaTabela();
            
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();

            } else {
                JOptionPane.showMessageDialog(this, "Movimento já cadastrado!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    
    private void alteraMovimento() {

        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            //selecaoSituacao();
            MovimentoDma movi = new MovimentoDma();

            movi.setDataMovimento(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            movi.setMesAno(ftfMes.getText());

            MovimentoDmaBD processoBD = new MovimentoDmaBD();
            if (processoBD.alteraMovimentoDma(movi)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                tblMovimento.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    
    private void excluiMovimento() {

        MovimentoDmaBD processoBD = new MovimentoDmaBD();
        if (processoBD.excluiProcesso(listMovimento.get(tblMovimento.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Movimento excluído com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
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
        //tblMovimento.setRowSelectionInterval(0, 0);
        //int indice = tblMovimento.getSelectedRow();
        //int idProcesso = listMovimento.get(indice).getId();
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

    
    private void caixaAlta() {
        txtSituacao.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(() -> {
            JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}
        });} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    }

    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcData.setDate(hoje);
    }

    private void limpaCampos() {
        {
            analista = null;
            txtIdLote.setText("");
            txtSituacao.setText("");
            txtNomeLote.setText("");
            txtIdProcesso.setText("");
            txtNumeroProcesso.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
            ftfMes.setText("");

        }
    }

    private void habilitaCampos() {
        //txtIdUsuario.setEnabled(true);
        txtIdLote.setEditable(true);
        btnSelecionaLote.setEnabled(true);

        txtIdProcesso.setEditable(true);
        btnSelecionaProcesso.setEnabled(true);

        jdcData.setEnabled(true);
        
        txtSituacao.setEditable(true);

    }

    private void desabilitaCampos() {

        txtIdLote.setEditable(false);
        txtIdProcesso.setEditable(false);

        btnSelecionaLote.setEnabled(false);
        btnSelecionaProcesso.setEnabled(false);
        jdcData.setEnabled(false);
         txtSituacao.setEditable(false);

    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);

        btnExcluir.setEnabled(true);
        btnSelecionaLote.setEnabled(true);
        //btnSegundaVia.setEnabled(false);
    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);

        btnExcluir.setEnabled(false);
        //btnSegundaVia.setEnabled(true);
    }

    private void selecionaProcessoTramitacao() {
        if (tblMovimento.getSelectedRow() != -1) {
            //tramitacaoFrame.setProcesso(listMovimento.get(tblProcesso.getSelectedRow()));
            tramitacaoFrame.requestFocusIdProcesso();
            this.dispose();
            tramitacaoFrame.toFront();

        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaProcessoEmissaoTitulo() {
        if (tblMovimento.getSelectedRow() != -1) {
            //emissaoTituloFrame.setProcesso(listMovimento.get(tblProcesso.getSelectedRow()));
            this.dispose();
            emissaoTituloFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Cobrador da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaMovimentoAnalise() {
        if (tblMovimento.getSelectedRow() != -1) {
            //analiseFrame.setMovimento(listMovimento.get(tblMovimento.getSelectedRow()));
            this.dispose();
            analiseFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Cobrador", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaMovimentoJuridico() {
        if (tblMovimento.getSelectedRow() != -1) {
            //juridicoFrame.setMovimento(listMovimento.get(tblMovimento.getSelectedRow()));
            this.dispose();
            juridicoFrame.toFront();
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
            listProcesso = processoBD.consultaProcesso("MovimentoFrame");
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

    private void buscaLote() {
        LoteDmaBD loteDmaDB = new LoteDmaBD();
        listLoteDma = loteDmaDB.consultaLote();
        int binario = 0;
        try {
            int max = listLoteDma.size();
            int id_busca = Integer.parseInt(txtIdLote.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listLoteDma.get(i).getId();
                if (listLoteDma.get(i).getId() == id_busca) {
                    setLoteDma(listLoteDma.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdLote.setText("");
                txtNomeLote.setText("");
                loteDma = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            loteDma = null;
            txtNomeLote.setText("");
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
        txtNomeLote = new javax.swing.JTextField();
        btnSelecionaLote = new javax.swing.JButton();
        txtIdLote = new javax.swing.JTextField();
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
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        cbMovimento = new javax.swing.JComboBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtSituacao = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroProcesso = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblMovimento = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Processo\n");
        setPreferredSize(new java.awt.Dimension(1000, 670));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/icons8-movimento-de-estoque-64.png"))); // NOI18N
        jLabel1.setText("Movimento_Dma");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Lote"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        txtNomeLote.setEditable(false);
        txtNomeLote.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeLote.setEnabled(false);
        txtNomeLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeLoteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(txtNomeLote, gridBagConstraints);

        btnSelecionaLote.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaLote.setEnabled(false);
        btnSelecionaLote.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaLoteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaLote, gridBagConstraints);

        txtIdLote.setEditable(false);
        txtIdLote.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdLoteFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdLote, gridBagConstraints);

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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
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
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridheight = 2;
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
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel23, gridBagConstraints);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel21.add(txtNomeRequerente, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel21, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Movimento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel5, gridBagConstraints);

        cbMovimento.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "ENTRADA", "SAÍDA" }));
        cbMovimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbMovimentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.VERTICAL;
        gridBagConstraints.ipadx = 25;
        gridBagConstraints.ipady = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 20, 5, 5);
        jPanel5.add(cbMovimento, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        jPanel2.add(jPanel5, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Situação");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel4, gridBagConstraints);

        txtSituacao.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(txtSituacao, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel8, gridBagConstraints);

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

        tblMovimento.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblMovimento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Processo", "Requerente", "Tipo Licença", "Lote", "Situação", "Movimento", "Data", "Usuario"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, true, true, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblMovimento.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblMovimento.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblMovimentoMouseClicked(evt);
            }
        });
        tblMovimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblMovimentoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblMovimento);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weighty = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jScrollPane1, gridBagConstraints);
        jPanel3.add(jPanel6, new java.awt.GridBagConstraints());

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
        MovimentoDmaBD cobradorBD = new MovimentoDmaBD();
        if (cobradorBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblMovimento.setEnabled(false);
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
               //if("NAO".equals(listMovimento.get(indice).getProcesso().getArquivado()))
                incluiMovimento();
                tblMovimento.setEnabled(true);
                // txtNomeCliente.requestFocus();
                break;
            case Constantes.EDIT_MODE:
                alteraMovimento();
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
        tblMovimento.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblMovimento.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            txtIdLote.setEditable(false);
            btnSelecionaLote.setEnabled(false);
            modo = Constantes.EDIT_MODE;
            buscaLote();
            buscaUsuario(idUsuario);
            tblMovimento.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um processo da lista!", "Motorista", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblMovimento.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do processo?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiMovimento();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma movibuição da lista!", "Processo", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroProcessoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroProcessoMouseClicked
    }//GEN-LAST:event_txtFiltroProcessoMouseClicked

    private void txtFiltroProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroProcessoActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoActionPerformed

    private void tblMovimentoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblMovimentoMouseClicked
        if (evt.getClickCount() == 2) {

            if (modoSeleciona == Constantes.INCLUIR) {
                //selecionaProcessoImovel();
                dispose();

            } else if (modoSeleciona == Constantes.ALTERAR) {
                selecionaProcessoTramitacao();
                dispose();

            } else if (modoSeleciona == Constantes.EMISSAOLICENCAFRAME) {
                selecionaProcessoEmissaoTitulo();
                dispose();
            } else if (modoSeleciona == Constantes.ANALISE_FRAME) {
                selecionaMovimentoAnalise();
                dispose();
            } else if (modoSeleciona == Constantes.JURIDICO_FRAME) {
                selecionaMovimentoJuridico();
                dispose();
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblMovimentoMouseClicked

    private void tblMovimentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblMovimentoKeyPressed

        if (modoSeleciona == Constantes.INCLUIR) {
            //selecionaProcessoImovel();
            dispose();

        } else if (modoSeleciona == Constantes.ALTERAR) {
            selecionaProcessoTramitacao();
            dispose();

        } else if (modoSeleciona == Constantes.EMISSAOLICENCAFRAME) {
            selecionaProcessoEmissaoTitulo();
            dispose();
        } else if (modoSeleciona == Constantes.ANALISE_FRAME) {
            selecionaMovimentoAnalise();
            dispose();
        } else if (modoSeleciona == Constantes.JURIDICO_FRAME) {
            selecionaMovimentoJuridico();
            dispose();
        }
        evt.consume();

    }//GEN-LAST:event_tblMovimentoKeyPressed

    private void txtFiltroProcessoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroProcessoFocusLost
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroProcessoFocusLost

    private void txtNomeLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLoteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeLoteActionPerformed

    private void btnSelecionaLoteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaLoteActionPerformed
        LoteDmaFrame loteDmaFrame = new LoteDmaFrame(this);
        loteDmaFrame.setVisible(true);
        this.getDesktopPane().add(loteDmaFrame);
        loteDmaFrame.toFront();
        centralizaJanelaRegVen(loteDmaFrame);             // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaLoteActionPerformed

    private void txtIdLoteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdLoteFocusLost
        buscaLote();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdLoteFocusLost

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

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

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

    private void cbMovimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbMovimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbMovimentoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup bGVTipo;
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaLote;
    private javax.swing.JButton btnSelecionaProcesso;
    private javax.swing.JComboBox cbMovimento;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
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
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblMovimento;
    private javax.swing.JTextField txtFiltroProcesso;
    private javax.swing.JTextField txtIdLote;
    public javax.swing.JTextField txtIdProcesso;
    private javax.swing.JTextField txtNomeLote;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNumeroProcesso;
    private javax.swing.JTextField txtSituacao;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the requerente
     */
    public LoteDma getLoteDma() {
        return loteDma;
    }

    /**
     * @param loteDma the requerente to set
     */
    public void setLoteDma(LoteDma loteDma) {
        this.loteDma = loteDma;
        txtNomeLote.setText(loteDma.getDescricao());
        txtIdLote.setText(loteDma.getId().toString());
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
        txtNomeRequerente.setText(processo.getRequerente().getNome());
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
