/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.denuncia;

import com.hexidec.ekit.Ekit;
import com.hexidec.ekit.EkitCore;
import sip.gabinete.Gabinete;
import sip.menulogin.Menu;
import sip.util.Constantes;
import com.toedter.calendar.JDateChooser;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
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
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sip.acessobd.AcessoBD;
import sip.emissaolicenca.EmissaoLicencaFrame;
import sip.naturezaocorrencia.NaturezaOcorrencia;
import sip.naturezaocorrencia.NaturezaOcorrenciaBD;
import sip.naturezaocorrencia.NaturezaOcorrenciaFrame;
import sip.pessoa.Pessoa;
import sip.pessoa.PessoaBD;
import sip.pessoa.PessoaFrame;
import sip.processo.Processo;
import sip.processo.ProcessoFrame;
import sip.tramitacao.Tramitacao;
import sip.tramitacao.TramitacaoBD;
import sip.triagem.TriagemFrame;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.util.ColorirTabelaDenuncia;
import sip.util.ColorirTabelaDenunciaApp;
import sip.util.ColorirTabelaTramitacao;
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class DenunciaFrame extends javax.swing.JInternalFrame {

    private JDateChooser jdcData;
    private DefaultTableModel tableModel;
    private DefaultTableModel tableModelApp;
    private ListSelectionModel listModel;
    private ListSelectionModel listModelApp;
    private List<Denuncia> listDenuncia;
    private List<Denuncia> listDenunciaApp;
    private List<Gabinete> listGabinete;
    private int modo;
    private int modoSeleciona;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private NaturezaOcorrencia naturezaOcorrencia;
    private List<NaturezaOcorrencia> listNaturezaOcorrencia;
    private String controleEscolhaPessoa;

    private Pessoa comunicante;
    private Pessoa denunciado;
    private List<Pessoa> listPessoa;

    private Processo processo;
    private Integer idProcesso;
    private String idAnalise;
    private String SituacaoParecer;//viavel não-viavel
    JPopupMenu jPopupMenu = new JPopupMenu();
    private String tramitaParaOnde;
    EkitCore core = new EkitCore();
    private TriagemFrame triagemFrame;
    private ProcessoFrame processoFrame;

    /**
     * Creates new form ClienteFrame
     */
    public DenunciaFrame() {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        ftfMesTramite.setVisible(false);
        jlClipe.setVisible(false);
        //EditorTexto();
        //popupBtnTramitar();
    }

    public DenunciaFrame(TriagemFrame triagemFrame) {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        this.triagemFrame = triagemFrame;
        modoSeleciona = Constantes.TRIAGEM_FRAME;
        jTabbedPane1.setVisible(false);
        ftfMesTramite.setVisible(false);
        jlClipe.setVisible(false);
    }

    public DenunciaFrame(ProcessoFrame processoFrame) {
        initComponents();
        defineModelo();
        formataData();
        jdcData.setEnabled(false);
        buscaUsuario(idUsuario);
        caixaAlta();
        fechando();
        this.processoFrame = processoFrame;
        modoSeleciona = Constantes.PROCESSO_FRAME;
        jTabbedPane1.setVisible(false);
        ftfMesTramite.setVisible(false);
        jlClipe.setVisible(false);
    }

    private void EditorTexto() {
        //jPanel26.setLayout(new FlowLayout());
        //jPanel26.add(core, FlowLayout.LEFT);
        //jPanel26.add(core.getToolBarStyles(true), FlowLayout.LEFT);
        //jPanel26.add(core.getToolBarFormat(true), FlowLayout.LEFT);
        //jPanel26.add(core.getToolBarMain(true), FlowLayout.LEFT);

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
        tableModel = (DefaultTableModel) tblDenuncia.getModel();
        tableModelApp = (DefaultTableModel) tblDenunciaApp.getModel();

        listModel = tblDenuncia.getSelectionModel();
        listModelApp = tblDenunciaApp.getSelectionModel();
        
        tblDenuncia.setDefaultRenderer(Object.class, new ColorirTabelaDenuncia());

        tblDenunciaApp.setDefaultRenderer(Object.class, new ColorirTabelaDenunciaApp());

        TableRowSorter<TableModel> sorter = new TableRowSorter<>(tblDenunciaApp.getModel());
        tblDenunciaApp.setRowSorter(sorter);

        List<RowSorter.SortKey> sortKeys = new ArrayList<>(25);
        //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        //sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);

        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheDenuncia();
                }
            }
        });

        listModelApp.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheDenunciaApp();
                }
            }
        });
        try {
            MaskFormatter mascara = new MaskFormatter("##.###-###");
            mascara.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatter = new DefaultFormatterFactory(mascara);
            // ftfCep.setFormatterFactory(formatter);

            tblDenuncia.getColumnModel().getColumn(0).setPreferredWidth(50);//0
            tblDenuncia.getColumnModel().getColumn(1).setPreferredWidth(300);//1
            tblDenuncia.getColumnModel().getColumn(2).setPreferredWidth(350);//2
            tblDenuncia.getColumnModel().getColumn(3).setPreferredWidth(300);//3
            tblDenuncia.getColumnModel().getColumn(4).setPreferredWidth(350);//4
            tblDenuncia.getColumnModel().getColumn(5).setPreferredWidth(300);//5
            tblDenuncia.getColumnModel().getColumn(6).setPreferredWidth(150);//5
            tblDenuncia.getColumnModel().getColumn(7).setPreferredWidth(100);//5
            
            tblDenuncia.getColumnModel().getColumn(8).setMaxWidth(0);
            tblDenuncia.getColumnModel().getColumn(8).setMinWidth(0);
            tblDenuncia.getTableHeader().getColumnModel().getColumn(8).setMaxWidth(0);
            tblDenuncia.getTableHeader().getColumnModel().getColumn(8).setMinWidth(0);

            tblDenunciaApp.getColumnModel().getColumn(0).setPreferredWidth(50);//0
            tblDenunciaApp.getColumnModel().getColumn(1).setPreferredWidth(300);//1
            tblDenunciaApp.getColumnModel().getColumn(2).setPreferredWidth(350);//2
            tblDenunciaApp.getColumnModel().getColumn(3).setPreferredWidth(300);//3
            tblDenunciaApp.getColumnModel().getColumn(4).setPreferredWidth(350);//4
            tblDenunciaApp.getColumnModel().getColumn(5).setPreferredWidth(300);//5

            tblDenunciaApp.getColumnModel().getColumn(6).setMaxWidth(0);
            tblDenunciaApp.getColumnModel().getColumn(6).setMinWidth(0);
            tblDenunciaApp.getTableHeader().getColumnModel().getColumn(6).setMaxWidth(0);
            tblDenunciaApp.getTableHeader().getColumnModel().getColumn(6).setMinWidth(0);

        } catch (ParseException ex) {
            Logger.getLogger(DenunciaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        DenunciaBD denunciaDB = new DenunciaBD();

        if (txtFiltro.getText().equals("")) {
            listDenuncia = denunciaDB.consultaDenuncia();
            listDenunciaApp = denunciaDB.consultaDenunciaApp();

        } else {
            String valor = txtFiltro.getText();
            listDenuncia = denunciaDB.consultaDenunciaNome(valor);
            listDenunciaApp = denunciaDB.consultaDenunciaAppNome(valor);
        }

        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }

        int numeroLinhasApp = tableModelApp.getRowCount();
        for (int i = 0; i < numeroLinhasApp; i++) {
            tableModelApp.removeRow(0);
        }

        for (int i = 0; i < listDenuncia.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listDenuncia.get(i).getId(),
                listDenuncia.get(i).getNaturezaOcorrencia().getNome(),
                listDenuncia.get(i).getComunicante().getNome(),
                listDenuncia.get(i).getComunicante().getLogradouro().getNome(),
                listDenuncia.get(i).getPDenunciado().getNome(),
                listDenuncia.get(i).getPDenunciado().getLogradouro().getNome(),
                simpleDateFormat.format(listDenuncia.get(i).getDataRegistro()),
                listDenuncia.get(i).getUsuario().getNome(),
                listDenuncia.get(i).getStatusApp()
            });
        }

        for (int i = 0; i < listDenunciaApp.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModelApp.insertRow(i, new Object[]{
                listDenunciaApp.get(i).getId(),
                listDenunciaApp.get(i).getTipoDenuncia(),
                listDenunciaApp.get(i).getDenunciado(),
                listDenunciaApp.get(i).getLocalDenuncia(),
                simpleDateFormat.format(listDenunciaApp.get(i).getDataDenuncia()),
                listDenunciaApp.get(i).getToken(),
                listDenunciaApp.get(i).getStatusApp()
            });
        }
    }

    private void mostraDetalheDenuncia() {
        if (tblDenuncia.getSelectedRow() != -1) {
            int indice = tblDenuncia.getSelectedRow();

            txtIdNaturezaOcorrencia.setText(listDenuncia.get(indice).getNaturezaOcorrencia().getId().toString());
            txtNomeNatureza.setText(listDenuncia.get(indice).getNaturezaOcorrencia().getNome());
            txtIdComunicante.setText(listDenuncia.get(indice).getComunicante().getId().toString());
            txtNomeComunicante.setText(listDenuncia.get(indice).getComunicante().getNome());
            txtIdDenunciado.setText(listDenuncia.get(indice).getPDenunciado().getId().toString());
            txtNomeDenunciado.setText(listDenuncia.get(indice).getPDenunciado().getNome());
            jdcData.setDate(listDenuncia.get(indice).getDataRegistro());
            jTARelatoOcorrencia.setText(listDenuncia.get(indice).getRelatoOcorencia());
            jTALink.setText(listDenuncia.get(indice).getLink());

        } else {
            //txtNome.setText("");
            //txtDescricao.setText("");
        }
    }

    private void mostraDetalheDenunciaApp() {
        if (tblDenunciaApp.getSelectedRow() != -1) {
            int indice = tblDenunciaApp.getSelectedRow();
            txtTipoDenuncia.setText(listDenunciaApp.get(indice).getTipoDenuncia());
            txtDenunciado.setText(listDenunciaApp.get(indice).getDenunciado());
            txtLocalDenuncia.setText(listDenunciaApp.get(indice).getLocalDenuncia());
            jDCDataDenuncia.setDate(listDenunciaApp.get(indice).getDataDenuncia());
            jTADescricaoDenuncia.setText(listDenunciaApp.get(indice).getDescricao());

        } else {
            //txtNome.setText("");
            //txtDescricao.setText("");
        }
    }

    private void incluiDenuncia() {
        if (getNaturezaOcorrencia() == null) {
            JOptionPane.showMessageDialog(this, "Selecione a natureza da ocorrência!", "Natureza", JOptionPane.INFORMATION_MESSAGE);

        } else if (getComunicante() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o comunicante!", "Comunicante", JOptionPane.INFORMATION_MESSAGE);

        } else if (getDenunciado() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o denunciado!", "Denunciado", JOptionPane.INFORMATION_MESSAGE);

        } else if (jTARelatoOcorrencia.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Informe o relato!", "Relato", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Denuncia ocorrencia = new Denuncia();
            ftfMes.setValue(jdcData.getDate().getTime());

            ocorrencia.setUsuario(getUsuario());
            ocorrencia.setDataRegistro(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            ocorrencia.setNaturezaOcorrencia(getNaturezaOcorrencia());
            ocorrencia.setComunicante(getComunicante());
            ocorrencia.setPDenunciado(getDenunciado());
            ocorrencia.setRelatoOcorencia(jTARelatoOcorrencia.getText().trim());
            ocorrencia.setLink(jTALink.getText().trim());

            DenunciaBD analiseBD = new DenunciaBD();
            if (analiseBD.incluiDenuncia(ocorrencia)) {
                JOptionPane.showMessageDialog(this, "Denuncia cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);

                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Possivelmente os dados já existe!", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraDenuncia() {
        if (getNaturezaOcorrencia() == null) {
            JOptionPane.showMessageDialog(this, "Selecione a natureza da ocorrência!", "Natureza", JOptionPane.INFORMATION_MESSAGE);

        } else if (getComunicante() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o comunicante!", "Comunicante", JOptionPane.INFORMATION_MESSAGE);

        } else if (getDenunciado() == null) {
            JOptionPane.showMessageDialog(this, "Selecione o denunciado!", "Denunciado", JOptionPane.INFORMATION_MESSAGE);

        } else if (jTARelatoOcorrencia.getText() == null) {
            JOptionPane.showMessageDialog(this, "Informe o relato!", "Relato", JOptionPane.INFORMATION_MESSAGE);

        } else if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        } else {

            java.util.Date pega = jdcData.getDate();
            SimpleDateFormat formato = new SimpleDateFormat("yyyy");
            String ano = formato.format(pega);

            Denuncia ocorrencia = new Denuncia();
            ftfMes.setValue(jdcData.getDate().getTime());

            ocorrencia.setId(this.listDenuncia.get(tblDenuncia.getSelectedRow()).getId());
            ocorrencia.setUsuario(getUsuario());
            ocorrencia.setDataRegistro(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));
            ocorrencia.setNaturezaOcorrencia(getNaturezaOcorrencia());
            ocorrencia.setComunicante(getComunicante());
            ocorrencia.setPDenunciado(getDenunciado());
            ocorrencia.setRelatoOcorencia(jTARelatoOcorrencia.getText().trim());
            ocorrencia.setLink(jTALink.getText().trim());

            DenunciaBD analiseBD = new DenunciaBD();
            if (analiseBD.alteraDenuncia(ocorrencia)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                tblDenuncia.setEnabled(true);
                desabilitaBotoes();
                desabilitaCampos();
                limpaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
   

    private void exlcuiDenuncia() {
        DenunciaBD loteBD = new DenunciaBD();
        if (loteBD.excluiDenuncia(listDenuncia.get(tblDenuncia.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe Tramitação !", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void executaRelatorio() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblDenuncia.getSelectedRow();
        int mostraID = listDenuncia.get(indice).getId();
        
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
        int indice = tblDenunciaApp.getSelectedRow();
        int mostraID = listDenunciaApp.get(indice).getId();
        
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

    private void caixaAlta() {
        jTARelatoOcorrencia.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextArea campo = (JTextArea) e.getSource();
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
            txtIdNaturezaOcorrencia.setText("");
            txtNomeNatureza.setText("");

            txtIdComunicante.setText("");
            txtNomeComunicante.setText("");

            txtIdDenunciado.setText("");
            txtNomeDenunciado.setText("");

            jdcData.setDate(null);
            jTARelatoOcorrencia.setText("");
            jTALink.setText("");

        }
    }

    private void habilitaCampos() {

        txtIdNaturezaOcorrencia.setEditable(true);
        btnSelecionaNatureza.setEnabled(true);

        txtIdComunicante.setEditable(true);
        btnSelecionaComunicante.setEnabled(true);

        txtIdDenunciado.setEditable(true);
        btnSelecionaDenunciado.setEnabled(true);

        jdcData.setEnabled(true);
        jTARelatoOcorrencia.setEditable(true);
        jTALink.setEditable(true);

    }

    private void desabilitaCampos() {
        txtIdNaturezaOcorrencia.setEditable(false);
        btnSelecionaNatureza.setEnabled(false);

        txtIdComunicante.setEditable(false);
        btnSelecionaComunicante.setEnabled(false);

        txtIdDenunciado.setEditable(false);
        btnSelecionaDenunciado.setEnabled(false);

        jdcData.setEnabled(false);
        jTARelatoOcorrencia.setEditable(false);
        jTALink.setEditable(false);
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

    private void selecionaDenunciaTriagem() {
        if (tblDenuncia.getSelectedRow() != -1) {
            triagemFrame.setDenuncia(listDenuncia.get(tblDenuncia.getSelectedRow()));
            this.dispose();
            triagemFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um lote da lista!", "Lote Titulação", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    

    private void selecionaDenunciaProcesso() {
        if (tblDenuncia.getSelectedRow() != -1) {
            processoFrame.setDenuncia(listDenuncia.get(tblDenuncia.getSelectedRow()));
            this.dispose();
            processoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma denuncia da lista!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void selecionaDenunciaAppProcesso() {
        if (tblDenunciaApp.getSelectedRow() != -1) {
            processoFrame.setDenuncia(listDenunciaApp.get(tblDenunciaApp.getSelectedRow()));
            this.dispose();
            processoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma denuncia da lista!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
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
                setUsuario(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setUsuario(null);
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaNaturezaOcorrrencia() {
        NaturezaOcorrenciaBD naturezaBD = new NaturezaOcorrenciaBD();
        listNaturezaOcorrencia = naturezaBD.consultaNaturezaOcorrencia();
        int binario = 0;
        try {
            int max = listNaturezaOcorrencia.size();
            int id_busca = Integer.parseInt(txtIdNaturezaOcorrencia.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listNaturezaOcorrencia.get(i).getId();
                if (listNaturezaOcorrencia.get(i).getId() == id_busca) {
                    setNaturezaOcorrencia(listNaturezaOcorrencia.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdNaturezaOcorrencia.setText("");
                txtNomeNatureza.setText("");
                naturezaOcorrencia = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            naturezaOcorrencia = null;
            txtNomeNatureza.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaComunicante() {
        PessoaBD pessoaBD = new PessoaBD();
        listPessoa = pessoaBD.consultaPessoa();
        int binario = 0;
        try {
            int max = listPessoa.size();
            int id_busca = Integer.parseInt(txtIdComunicante.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listPessoa.get(i).getId();
                if (listPessoa.get(i).getId() == id_busca) {
                    setComunicante(listPessoa.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdComunicante.setText("");
                txtNomeComunicante.setText("");
                setComunicante(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            comunicante = null;
            txtNomeComunicante.setText("");
            txtIdComunicante.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaDenunciado() {
        PessoaBD pessoaBD = new PessoaBD();
        listPessoa = pessoaBD.consultaPessoa();
        int binario = 0;
        try {
            int max = listPessoa.size();
            int id_busca = Integer.parseInt(txtIdDenunciado.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listPessoa.get(i).getId();
                if (listPessoa.get(i).getId() == id_busca) {
                    setDenunciado(listPessoa.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdDenunciado.setText("");
                txtNomeDenunciado.setText("");
                setDenunciado(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            denunciado = null;
            txtNomeDenunciado.setText("");
            txtIdDenunciado.setText("");
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
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblDenuncia = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblDenunciaApp = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnImprimir = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel24 = new javax.swing.JLabel();
        btnSelecionaNatureza = new javax.swing.JButton();
        txtIdNaturezaOcorrencia = new javax.swing.JTextField();
        txtNomeNatureza = new javax.swing.JTextField();
        jPanel13 = new javax.swing.JPanel();
        ftfMes = new javax.swing.JFormattedTextField();
        jLabel16 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        ftfMesTramite = new javax.swing.JFormattedTextField();
        jPanel24 = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        btnSelecionaComunicante = new javax.swing.JButton();
        txtIdComunicante = new javax.swing.JTextField();
        txtNomeComunicante = new javax.swing.JTextField();
        jPanel25 = new javax.swing.JPanel();
        jLabel26 = new javax.swing.JLabel();
        btnSelecionaDenunciado = new javax.swing.JButton();
        txtIdDenunciado = new javax.swing.JTextField();
        txtNomeDenunciado = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTARelatoOcorrencia = new javax.swing.JTextArea();
        jPanel6 = new javax.swing.JPanel();
        jTabbedPane3 = new javax.swing.JTabbedPane();
        jPanel26 = new javax.swing.JPanel();
        txtLocalDenuncia = new javax.swing.JTextField();
        txtDenunciado = new javax.swing.JTextField();
        txtTipoDenuncia = new javax.swing.JTextField();
        jDCDataDenuncia = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTADescricaoDenuncia = new javax.swing.JTextArea();
        jPanel10 = new javax.swing.JPanel();
        jPanel27 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTALink = new javax.swing.JTextArea();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Denúncia");
        setPreferredSize(new java.awt.Dimension(900, 650));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/DENUNCIA_64x64 copy.png"))); // NOI18N
        jLabel1.setText("Denúncia");
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

        jLabel2.setText("Filtro Nome");
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

        tblDenuncia.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblDenuncia.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Natureza Ocorrencia", "Comunicante", "Endereco", "Denunciado", "Endereco", "Data", "Usuario", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true, true, true, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblDenuncia.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDenuncia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDenunciaMouseClicked(evt);
            }
        });
        tblDenuncia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDenunciaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblDenuncia);

        jTabbedPane2.addTab("Denuncias Locais", jScrollPane1);

        tblDenunciaApp.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tblDenunciaApp.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Tipo_Denuncia", "Denunciado", "Local Denuncia", "Data", "Token", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
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
        tblDenunciaApp.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblDenunciaApp.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblDenunciaAppMouseClicked(evt);
            }
        });
        tblDenunciaApp.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblDenunciaAppKeyPressed(evt);
            }
        });
        jScrollPane3.setViewportView(tblDenunciaApp);

        jTabbedPane2.addTab("Denuncias APP", jScrollPane3);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel3.add(jTabbedPane2, gridBagConstraints);

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

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        getContentPane().add(jPanel4, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel23.setBorder(javax.swing.BorderFactory.createTitledBorder("Natureza Ocorrencia"));
        jPanel23.setLayout(new java.awt.GridBagLayout());

        jLabel24.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(jLabel24, gridBagConstraints);

        btnSelecionaNatureza.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaNatureza.setEnabled(false);
        btnSelecionaNatureza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaNaturezaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(btnSelecionaNatureza, gridBagConstraints);

        txtIdNaturezaOcorrencia.setEditable(false);
        txtIdNaturezaOcorrencia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdNaturezaOcorrenciaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel23.add(txtIdNaturezaOcorrencia, gridBagConstraints);

        txtNomeNatureza.setEditable(false);
        txtNomeNatureza.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeNatureza.setEnabled(false);
        txtNomeNatureza.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeNaturezaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel23.add(txtNomeNatureza, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel23, gridBagConstraints);

        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Registro"));
        jPanel13.setLayout(new java.awt.GridBagLayout());

        ftfMes.setEditable(false);
        ftfMes.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
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
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 60;
        gridBagConstraints.ipady = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

        jPanel24.setBorder(javax.swing.BorderFactory.createTitledBorder("Comunicante"));
        jPanel24.setLayout(new java.awt.GridBagLayout());

        jLabel25.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(jLabel25, gridBagConstraints);

        btnSelecionaComunicante.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaComunicante.setEnabled(false);
        btnSelecionaComunicante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaComunicanteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(btnSelecionaComunicante, gridBagConstraints);

        txtIdComunicante.setEditable(false);
        txtIdComunicante.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdComunicanteFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel24.add(txtIdComunicante, gridBagConstraints);

        txtNomeComunicante.setEditable(false);
        txtNomeComunicante.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeComunicante.setEnabled(false);
        txtNomeComunicante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeComunicanteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel24.add(txtNomeComunicante, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel24, gridBagConstraints);

        jPanel25.setBorder(javax.swing.BorderFactory.createTitledBorder("Denunciado"));
        jPanel25.setLayout(new java.awt.GridBagLayout());

        jLabel26.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel25.add(jLabel26, gridBagConstraints);

        btnSelecionaDenunciado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaDenunciado.setEnabled(false);
        btnSelecionaDenunciado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaDenunciadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel25.add(btnSelecionaDenunciado, gridBagConstraints);

        txtIdDenunciado.setEditable(false);
        txtIdDenunciado.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdDenunciadoFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel25.add(txtIdDenunciado, gridBagConstraints);

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
        jPanel25.add(txtNomeDenunciado, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel25, gridBagConstraints);

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

        jLabel8.setBackground(new java.awt.Color(255, 102, 102));
        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 0));
        jLabel8.setText("Enviado Fiscal");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel8, gridBagConstraints);

        jLabel9.setBackground(new java.awt.Color(255, 102, 102));
        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(0, 255, 0));
        jLabel9.setText("Finalizado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel9, gridBagConstraints);

        jLabel10.setBackground(new java.awt.Color(255, 136, 136));
        jLabel10.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 136, 136));
        jLabel10.setText("Protocolado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel10, gridBagConstraints);

        jLabel11.setBackground(new java.awt.Color(0, 255, 255));
        jLabel11.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(0, 255, 255));
        jLabel11.setText("Reaberto");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel9.add(jLabel11, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        jPanel2.add(jPanel9, gridBagConstraints);

        jTabbedPane1.addTab("Lançamento", jPanel2);

        jPanel5.setLayout(new java.awt.GridBagLayout());

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Relato Ocorrência"));
        jPanel22.setLayout(new java.awt.GridBagLayout());

        jTARelatoOcorrencia.setEditable(false);
        jTARelatoOcorrencia.setColumns(20);
        jTARelatoOcorrencia.setRows(5);
        jTARelatoOcorrencia.setWrapStyleWord(true);
        jScrollPane2.setViewportView(jTARelatoOcorrencia);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel22.add(jScrollPane2, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jPanel22, gridBagConstraints);

        jTabbedPane1.addTab("Relato", jPanel5);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel26.setBorder(javax.swing.BorderFactory.createTitledBorder("Local"));
        jPanel26.setLayout(new java.awt.GridBagLayout());

        txtLocalDenuncia.setEditable(false);
        txtLocalDenuncia.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtLocalDenuncia.setEnabled(false);
        txtLocalDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtLocalDenunciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(txtLocalDenuncia, gridBagConstraints);

        txtDenunciado.setEditable(false);
        txtDenunciado.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtDenunciado.setEnabled(false);
        txtDenunciado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtDenunciadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(txtDenunciado, gridBagConstraints);

        txtTipoDenuncia.setEditable(false);
        txtTipoDenuncia.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtTipoDenuncia.setEnabled(false);
        txtTipoDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTipoDenunciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(txtTipoDenuncia, gridBagConstraints);

        jDCDataDenuncia = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_'); jDCDataDenuncia.setBorder(new LineBorder(new Color(30, 144, 255), 1, false)); jDCDataDenuncia.setBackground(Color.WHITE); jDCDataDenuncia.setBounds(255, 91, 87, 20);
        jDCDataDenuncia.setBackground(new java.awt.Color(255, 255, 255));
        jDCDataDenuncia.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jDCDataDenuncia, gridBagConstraints);

        jLabel3.setText("Tipo Denúncia");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jLabel3, gridBagConstraints);

        jLabel4.setText("Denunciado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jLabel4, gridBagConstraints);

        jLabel5.setText("Local Denúncia");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jLabel5, gridBagConstraints);

        jLabel6.setText("Data");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel26.add(jLabel6, gridBagConstraints);

        jTabbedPane3.addTab("Detalhe", jPanel26);

        jPanel8.setLayout(new java.awt.GridBagLayout());

        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder("Descrição"));
        jPanel29.setLayout(new java.awt.GridBagLayout());

        jTADescricaoDenuncia.setEditable(false);
        jTADescricaoDenuncia.setColumns(20);
        jTADescricaoDenuncia.setRows(5);
        jTADescricaoDenuncia.setWrapStyleWord(true);
        jScrollPane4.setViewportView(jTADescricaoDenuncia);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel29.add(jScrollPane4, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jPanel29, gridBagConstraints);

        jTabbedPane3.addTab("Descrição", jPanel8);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.weighty = 1.0;
        jPanel6.add(jTabbedPane3, gridBagConstraints);

        jTabbedPane1.addTab("Detalhe Denúncia_APP", jPanel6);

        jPanel10.setLayout(new java.awt.GridBagLayout());

        jPanel27.setBorder(javax.swing.BorderFactory.createTitledBorder("Link"));
        jPanel27.setLayout(new java.awt.GridBagLayout());

        jTALink.setEditable(false);
        jTALink.setColumns(20);
        jTALink.setRows(5);
        jTALink.setWrapStyleWord(true);
        jScrollPane5.setViewportView(jTALink);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel27.add(jScrollPane5, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel10.add(jPanel27, gridBagConstraints);

        jTabbedPane1.addTab("Link", jPanel10);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipady = 50;
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        DenunciaBD DenunciaBD = new DenunciaBD();
        if (DenunciaBD.testaConexao()) {
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblDenuncia.setEnabled(false);
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
            incluiDenuncia();
            tblDenuncia.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraDenuncia();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblDenuncia.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblDenuncia.getSelectedRow() != -1) {
            buscaNaturezaOcorrrencia();
            buscaComunicante();
            buscaDenunciado();
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            tblDenuncia.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ocorrencia da lista!", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblDenuncia.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da Denuncia?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiDenuncia();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ocorrencia da lista!", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void tblDenunciaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDenunciaMouseClicked
        if (evt.getClickCount() == 2) {
            switch (modoSeleciona) {
                case Constantes.TRIAGEM_FRAME:
                    selecionaDenunciaTriagem();
                    dispose();
                    break;
                case Constantes.PROCESSO_FRAME:
                    selecionaDenunciaProcesso();
                    dispose();
                    break;
                case Constantes.PARECERANALISE_FRAME:
                    //selecionaDenunciaParecerDenuncia();
                    dispose();
                    break;
                case Constantes.GABINETE_FRAME:
                    //selecionaDenunciaGabinete();
                    dispose();
                    break;
                default:
                    break;
            }

            evt.consume();
        }
    }//GEN-LAST:event_tblDenunciaMouseClicked

    private void tblDenunciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDenunciaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (modoSeleciona) {
                case Constantes.TRIAGEM_FRAME:
                    selecionaDenunciaTriagem();
                    dispose();
                    break;
                case Constantes.PROCESSO_FRAME:
                    selecionaDenunciaProcesso();
                    dispose();
                    break;
                case Constantes.GABINETE_FRAME:
                    //selecionaDenunciaGabinete();
                    dispose();
                    break;
                default:
                    break;
            }
        }
        evt.consume();
    }//GEN-LAST:event_tblDenunciaKeyPressed

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
        atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void btnSelecionaNaturezaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaNaturezaActionPerformed
        NaturezaOcorrenciaFrame naturezaFrame = new NaturezaOcorrenciaFrame(this);
        naturezaFrame.setVisible(true);
        this.getDesktopPane().add(naturezaFrame);
        naturezaFrame.toFront(); // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaNaturezaActionPerformed

    private void txtIdNaturezaOcorrenciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdNaturezaOcorrenciaFocusLost
        buscaNaturezaOcorrrencia();  // TODO add your handling code here:
}//GEN-LAST:event_txtIdNaturezaOcorrenciaFocusLost

    private void txtNomeNaturezaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeNaturezaActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeNaturezaActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void btnSelecionaComunicanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaComunicanteActionPerformed
        setControleEscolhaPessoa("comunicante");
        PessoaFrame pessoaFrame = new PessoaFrame(this);
        pessoaFrame.setVisible(true);
        this.getDesktopPane().add(pessoaFrame);
        pessoaFrame.toFront(); // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaComunicanteActionPerformed

    private void txtIdComunicanteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdComunicanteFocusLost
        buscaComunicante(); // TODO add your handling code here:
    }//GEN-LAST:event_txtIdComunicanteFocusLost

    private void txtNomeComunicanteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeComunicanteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeComunicanteActionPerformed

    private void btnSelecionaDenunciadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaDenunciadoActionPerformed
        setControleEscolhaPessoa("denunciado");
        PessoaFrame pessoaFrame = new PessoaFrame(this);
        pessoaFrame.setVisible(true);
        this.getDesktopPane().add(pessoaFrame);
        pessoaFrame.toFront();  // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaDenunciadoActionPerformed

    private void txtIdDenunciadoFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdDenunciadoFocusLost
        buscaDenunciado();// TODO add your handling code here:
    }//GEN-LAST:event_txtIdDenunciadoFocusLost

    private void txtNomeDenunciadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeDenunciadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeDenunciadoActionPerformed

    private void btnImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImprimirActionPerformed
        if (tblDenuncia.getSelectedRow() != -1) {

            executaRelatorio();

        } else if(tblDenunciaApp.getSelectedRow() != -1) {
           executaRelatorioApp();
        }else{
       JOptionPane.showMessageDialog(this, "Selecione uma denúncia na tabela!", "Denúncia", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnImprimirActionPerformed

    private void tblDenunciaAppMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblDenunciaAppMouseClicked
 if (evt.getClickCount() == 2) {
            switch (modoSeleciona) {
                case Constantes.TRIAGEM_FRAME:
                    //selecionaDenunciaTriagem();
                    dispose();
                    break;
                case Constantes.PROCESSO_FRAME:
                    selecionaDenunciaAppProcesso();
                    dispose();
                    break;
                case Constantes.PARECERANALISE_FRAME:
                    //selecionaDenunciaParecerDenuncia();
                    dispose();
                    break;
                case Constantes.GABINETE_FRAME:
                    //selecionaDenunciaGabinete();
                    dispose();
                    break;
                default:
                    break;
            }

            evt.consume();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_tblDenunciaAppMouseClicked

    private void tblDenunciaAppKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblDenunciaAppKeyPressed
         if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            switch (modoSeleciona) {
                case Constantes.TRIAGEM_FRAME:
                    //selecionaDenunciaTriagem();
                    dispose();
                    break;
                case Constantes.PROCESSO_FRAME:
                    selecionaDenunciaAppProcesso();
                    dispose();
                    break;
                case Constantes.GABINETE_FRAME:
                    //selecionaDenunciaGabinete();
                    dispose();
                    break;
                default:
                    break;
            }
        }
        evt.consume();// TODO add your handling code here:
    }//GEN-LAST:event_tblDenunciaAppKeyPressed

    private void txtLocalDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtLocalDenunciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtLocalDenunciaActionPerformed

    private void txtDenunciadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtDenunciadoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDenunciadoActionPerformed

    private void txtTipoDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTipoDenunciaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTipoDenunciaActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnImprimir;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaComunicante;
    private javax.swing.JButton btnSelecionaDenunciado;
    private javax.swing.JButton btnSelecionaNatureza;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private com.toedter.calendar.JDateChooser jDCDataDenuncia;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTextArea jTADescricaoDenuncia;
    private javax.swing.JTextArea jTALink;
    private javax.swing.JTextArea jTARelatoOcorrencia;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTabbedPane jTabbedPane3;
    private javax.swing.JLabel jlClipe;
    private javax.swing.JTable tblDenuncia;
    private javax.swing.JTable tblDenunciaApp;
    private javax.swing.JTextField txtDenunciado;
    private javax.swing.JTextField txtFiltro;
    public javax.swing.JTextField txtIdComunicante;
    public javax.swing.JTextField txtIdDenunciado;
    public javax.swing.JTextField txtIdNaturezaOcorrencia;
    private javax.swing.JTextField txtLocalDenuncia;
    private javax.swing.JTextField txtNomeComunicante;
    private javax.swing.JTextField txtNomeDenunciado;
    private javax.swing.JTextField txtNomeNatureza;
    private javax.swing.JTextField txtTipoDenuncia;
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
    public NaturezaOcorrencia getNaturezaOcorrencia() {
        return naturezaOcorrencia;
    }

    /**
     * @param distribuicao the distribuicao to set
     */
    public void setNaturezaOcorrencia(NaturezaOcorrencia natureza) {
        this.naturezaOcorrencia = natureza;
        txtNomeNatureza.setText(natureza.getNome());
        txtIdNaturezaOcorrencia.setText(natureza.getId().toString());
    }

    /**
     * @return the comunicante
     */
    public Pessoa getComunicante() {
        return comunicante;
    }

    /**
     * @param comunicante the comunicante to set
     */
    public void setComunicante(Pessoa comunicante) {
        this.comunicante = comunicante;
        txtNomeComunicante.setText(comunicante.getNome());
        txtIdComunicante.setText(comunicante.getId().toString());
    }

    /**
     * @return the denunciado
     */
    public Pessoa getDenunciado() {
        return denunciado;
    }

    /**
     * @param denunciado the denunciado to set
     */
    public void setDenunciado(Pessoa denunciado) {
        this.denunciado = denunciado;
        txtNomeDenunciado.setText(denunciado.getNome());
        txtIdDenunciado.setText(denunciado.getId().toString());
    }

    /**
     * @return the id
     */
    /**
     * @return the controleEscolhaPessoa
     */
    public String getControleEscolhaPessoa() {
        return controleEscolhaPessoa;
    }

    /**
     * @param controleEscolhaPessoa the controleEscolhaPessoa to set
     */
    public void setControleEscolhaPessoa(String controleEscolhaPessoa) {
        this.controleEscolhaPessoa = controleEscolhaPessoa;
    }
}
