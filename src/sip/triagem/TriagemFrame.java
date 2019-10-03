/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.triagem;

import sip.triagem.*;
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


import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import net.sf.jasperreports.engine.JasperPrintManager;
import net.sf.jasperreports.view.JasperViewer;
import sip.juridico.Juridico;
import sip.juridico.JuridicoBD;
import sip.juridico.JuridicoFrame;
import sip.denuncia.Denuncia;
import sip.denuncia.DenunciaBD;
import sip.denuncia.DenunciaFrame;
import sip.resumotriagem.ResumoTriagem;
import sip.resumotriagem.ResumoTriagemBD;
import sip.util.HorarioDeVerao;
import sip.resumotriagem.ResumoTriagemFrame;

/**
 *
 * @author T2Ti
 */
public class TriagemFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Triagem> listTriagem;
    private int modo;
    private int modoSeleciona;
    private Juridico juridico;
    private List<Juridico> listJuridico;
    private String loteUnico;
    private JDateChooser jdcData;
    private JDateChooser jdcDataTramite;
    private JuridicoFrame juridicoFrame;    
    private Processo processo;
    String idUsuario = Menu.id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private List<Tramitacao> listTramitacao;
    private Tramitacao tramitacao;
    private String idLote;
    private List<Denuncia> listDenuncia;
    private List<ResumoTriagem> listResumoTriagem;
    private Denuncia denuncia;
    
    /** Creates new form ClienteFrame */
    public TriagemFrame() {
        initComponents();
        defineModelo();
        formataData();
        formataDataTramitacao();
        setaDataAtual();
        caixaAlta();
        fechando();
        popularComboBoxResumo();
        jdcData.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
        ftfMesTramite.setVisible(false);
    }
    
     public TriagemFrame(JuridicoFrame analiseFrame) {
        initComponents();
        defineModelo();
        formataData();
        formataDataTramitacao();
        setaDataAtual();
        caixaAlta();
        fechando();
        this.juridicoFrame = analiseFrame;
        modoSeleciona = Constantes.ANALISE_FRAME;
        jdcData.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
        btnNovo.doClick();
        txtIdDenuncia.setText(analiseFrame.getIdAnalise());
        buscaDenuncia();
    }
    

     
      private void fechando() {  
          this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);  
         
        this.addInternalFrameListener(  
            new InternalFrameAdapter(){  
            @Override
                public void internalFrameClosing(InternalFrameEvent e) {  
                       dispose();
                       Menu.menuVisivel();
                     
              }  
            }
          );
       }

    private void defineModelo() {
       
        tableModel = (DefaultTableModel) tblTriagem.getModel();
        listModel = tblTriagem.getSelectionModel();
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


            tblTriagem.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblTriagem.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblTriagem.getColumnModel().getColumn(2).setPreferredWidth(270);
            tblTriagem.getColumnModel().getColumn(3).setPreferredWidth(380);
            
            
            
            
        } catch (ParseException ex) {
            Logger.getLogger(TriagemFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void executaRelatorio() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblTriagem.getSelectedRow();

        int mostraID = listTriagem.get(indice).getId();

        try {
            HashMap parametros = new HashMap();
            parametros.put("CAMINHO_IMAGEM1", System.getProperty("user.dir") + "\\imagem\\logocidade.jpg");
            parametros.put("CAMINHO_IMAGEM2", System.getProperty("user.dir") + "\\imagem\\logoserf.jpg");
            parametros.put("CAMINHO_IMAGEM3", System.getProperty("user.dir") + "\\imagem\\logoprefeitura.jpg");
            parametros.put("ID_LAUDO", (long) mostraID);

            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\laudoMzu.jasper", parametros, acessoBd.conectar());
            JOptionPane.showMessageDialog(this, "Aquarde enquanto o relatório é impresso!", "Informação", JOptionPane.INFORMATION_MESSAGE);
            JasperViewer.viewReport(jp, false);

            //JasperPrintManager.printReport(jp, false); 
          /* for(int i = 0; i <= 2; i++){
            //JasperPrintManager.printPage(jp,0,false);       
            }*/


        } catch (JRException ex) {
            Logger.getLogger(EmissaoLicencaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
     private void executaRelatorioAutomatico( ) {
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
    
    private void popularComboBoxResumo(){
         ResumoTriagemBD resumoTriagemBD = new ResumoTriagemBD();

            listResumoTriagem = resumoTriagemBD.consultaResumoTriagem();
        
             
        for (int i = 0; i < listResumoTriagem.size(); i++) {
            cbResumo.addItem(listResumoTriagem.get(i).getNome());
        }
    }

    private void atualizaTabela() {
        TriagemBD parecerBD = new TriagemBD();

        if (txtFiltro.getText().equals("")) {
            listTriagem = parecerBD.consultaTriagem();
        } else {
            listTriagem = parecerBD.consultaTriagemNome(txtFiltro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listTriagem.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listTriagem.get(i).getId(), 
                simpleDateFormat.format(listTriagem.get(i).getDataTriagem()),
                listTriagem.get(i).getpDenunciado().getNome(),              
                listTriagem.get(i).getUsuario().getNome()               
            });
        }
   
    }
    
    
    private void formataDataTramitacao() {

        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
  
    }
    
    private void setaDataAtual() {
        java.util.Date hoje = new java.util.Date();
        jdcData.setDate(hoje);
        jdcDataTramite.setDate(hoje);
    }
    
        private void formataData() {

        jdcData = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcData.setBorder(new LineBorder(new Color(30, 144, 255), 1, false));
        jdcData.setBackground(Color.WHITE);
        jdcData.setBounds(255, 91, 87, 20);

        jPanel7.add(jdcData);
    }
    
   

    private void mostraDetalheParecer() {
        if (tblTriagem.getSelectedRow() != -1) {
            int indice = tblTriagem.getSelectedRow();
            txtIdDenuncia.setText(listTriagem.get(indice).getDenuncia().getId().toString());
            txtNomeDenunciado.setText(listTriagem.get(indice).getpDenunciado().getNome());
            jdcData.setDate(listTriagem.get(indice).getDataTriagem());   
    
        
  
        } else {
            txtIdDenuncia.setText("");
            txtNomeDenunciado.setText("");
            jdcData.setDate(null);
        }
    }
        
   
    private void incluiTriagem() {
       
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        }
                
        else  if (getDenuncia() == null){
            JOptionPane.showMessageDialog(this, "Informe o dados da Ocorrência!", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
            txtIdDenuncia.requestFocus();
         }    
                
       else {
            Triagem triagem = new Triagem();
            
            ftfMes.setValue(jdcData.getDate());            
            triagem.setUsuario(getUsuario());
            triagem.setDenuncia(getDenuncia());
            triagem.setDataTriagem(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime())); 
            triagem.setResumo(cbResumo.getSelectedItem().toString());


            TriagemBD parecerBD = new TriagemBD();
            if (parecerBD.incluiTriagem(triagem)) {
                JOptionPane.showMessageDialog(this, "Triagem lançado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
             
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
                JOptionPane.showMessageDialog(this, "Possivelmente a triagem já existe !", "Erro", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }

    private void alteraTriagem() {
       if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        }
                
        else  if (getDenuncia() == null){
            JOptionPane.showMessageDialog(this, "Informe o dados da Ocorrência!", "Denuncia", JOptionPane.INFORMATION_MESSAGE);
            txtIdDenuncia.requestFocus();
         }    
         
        
      else {
            Triagem triagem = new Triagem();
            
            ftfMes.setValue(jdcData.getDate());
           
           
            triagem.setUsuario(getUsuario());
            triagem.setDenuncia(getDenuncia());
            triagem.setDataTriagem(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime())); 
            triagem.setResumo(cbResumo.getSelectedItem().toString());
            triagem.setId(this.listTriagem.get(tblTriagem.getSelectedRow()).getId());       
            


            TriagemBD triagemBD = new TriagemBD();
            if (triagemBD.alteraTriagem(triagem)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    
    private void exlcuiTriagem() {
        TriagemBD triagemBD = new TriagemBD();
        if (triagemBD.excluiTriagem(listTriagem.get(tblTriagem.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            //JOptionPane.showMessageDialog(this, "Possivelmente existe Processo para o bairro!", "Aviso", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta(){
       
        //txtNomeTécnico.addKeyListener(new KeyListener(){@Override public void keyTyped(final KeyEvent e) {SwingUtilities.invokeLater(new Runnable() {@Override public void run() {JTextField campo = (JTextField) e.getSource(); int posicaoCursor = campo.getCaretPosition(); campo.setText(campo.getText().toUpperCase()); if (posicaoCursor != campo.getCaretPosition()) {campo.setCaretPosition(posicaoCursor);}}});} @Override public void keyReleased(KeyEvent e) {} @Override public void keyPressed(KeyEvent e) {}});  
    
    }
    
    
      private void limpaCampos(){
        {
                      
            txtIdDenuncia.setText("");
            
            txtNomeDenunciado.setText("");
           
        }  
    }
       
    private void habilitaCampos() {        
     
        jdcData.setEnabled(true);
        txtIdDenuncia.setEditable(true);
        btnSelecionaDenuncia.setEnabled(true);
        cbResumo.setEnabled(true);
       
    }

    private void desabilitaCampos() {
        jdcData.setEnabled(false);
        txtIdDenuncia.setEditable(false);
        btnSelecionaDenuncia.setEnabled(false);
        cbResumo.setEnabled(false);
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
  
    private void limpaSelecaoTabela(){
         int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }
    
    public void setTramitacao(Tramitacao tramitacao) {
        this.tramitacao = tramitacao;
        //idLote = tramitacao.getLoteTitulacao().getId().toString();
        
    }
    
     
    
     private void buscaDenuncia() {
        DenunciaBD denunciaBD = new DenunciaBD();
        listDenuncia = denunciaBD.consultaDenuncia();
        int binario = 0;
        try {
            int max = listDenuncia.size();
            int id_busca = Integer.parseInt(txtIdDenuncia.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listDenuncia.get(i).getId();
                if (listDenuncia.get(i).getId() == id_busca) {
                    setDenuncia(listDenuncia.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdDenuncia.setText("");
                txtNomeDenunciado.setText("");
                juridico = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            juridico = null;
            txtNomeDenunciado.setText("");
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
                setaDataAtual();
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
        ftfMesTramite = new javax.swing.JFormattedTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltro = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTriagem = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtNomeDenunciado = new javax.swing.JTextField();
        btnSelecionaDenuncia = new javax.swing.JButton();
        txtIdDenuncia = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        cbResumo = new javax.swing.JComboBox();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Triagem");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/MOTCOB_64x64.png"))); // NOI18N
        jLabel1.setText("TRIAGEM DAS DENÚNCIAS");
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
        jPanel13.setBorder(javax.swing.BorderFactory.createTitledBorder("Data "));
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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.ipady = 10;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel13, gridBagConstraints);

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

        tblTriagem.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        tblTriagem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "DATA", "DENUNCIADO", "USUARIO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblTriagem.setGridColor(new java.awt.Color(102, 102, 102));
        tblTriagem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblTriagem.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTriagemMouseClicked(evt);
            }
        });
        tblTriagem.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblTriagemKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblTriagem);
        if (tblTriagem.getColumnModel().getColumnCount() > 0) {
            tblTriagem.getColumnModel().getColumn(3).setResizable(false);
        }

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
        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Ocorrência"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

        jLabel18.setText("Denunciado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel18, gridBagConstraints);

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
        jPanel20.add(txtNomeDenunciado, gridBagConstraints);

        btnSelecionaDenuncia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaDenuncia.setEnabled(false);
        btnSelecionaDenuncia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                btnSelecionaDenunciaFocusLost(evt);
            }
        });
        btnSelecionaDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaDenunciaActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaDenuncia, gridBagConstraints);

        txtIdDenuncia.setEditable(false);
        txtIdDenuncia.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdDenunciaFocusLost(evt);
            }
        });
        txtIdDenuncia.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseExited(java.awt.event.MouseEvent evt) {
                txtIdDenunciaMouseExited(evt);
            }
        });
        txtIdDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdDenunciaActionPerformed(evt);
            }
        });
        txtIdDenuncia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIdDenunciaKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtIdDenunciaKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtIdDenunciaKeyTyped(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdDenuncia, gridBagConstraints);

        jLabel19.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel20, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLabel8.setText("Verificação");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLabel8, gridBagConstraints);

        cbResumo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        cbResumo.setEnabled(false);
        cbResumo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbResumoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 20;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(cbResumo, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jPanel8, gridBagConstraints);

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        TriagemBD triagemBD = new TriagemBD();
            if(triagemBD.testaConexao()){
                habilitaCampos();
                 
                limpaCampos();
                btnFiltrar.setEnabled(false);
                tblTriagem.setEnabled(false);
                limpaSelecaoTabela();
                txtIdDenuncia.requestFocus();
                habilitaBotoes();
                modo = Constantes.INSERT_MODE;
                //caixaAlta();
            }
            else
                JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiTriagem();
            tblTriagem.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraTriagem();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblTriagem.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblTriagem.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaDenuncia();
            tblTriagem.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ocorrência da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblTriagem.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão da Triagem?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiTriagem();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma ocorrência da lista!", "Triagem", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroActionPerformed
       atualizaTabela();
    }//GEN-LAST:event_txtFiltroActionPerformed

    private void txtFiltroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroFocusLost
       atualizaTabela(); // TODO add your handling code here:
    }//GEN-LAST:event_txtFiltroFocusLost

    private void txtNomeDenunciadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeDenunciadoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeDenunciadoActionPerformed

    private void btnSelecionaDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaDenunciaActionPerformed
        DenunciaFrame denuncia = new DenunciaFrame(this);
        denuncia.setVisible(true);
        this.getDesktopPane().add(denuncia);
        denuncia.toFront();
                                   // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaDenunciaActionPerformed

    private void btnSelecionaDenunciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_btnSelecionaDenunciaFocusLost
        // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaDenunciaFocusLost

    private void txtIdDenunciaMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtIdDenunciaMouseExited
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaMouseExited

    private void txtIdDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdDenunciaActionPerformed
        buscaDenuncia();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaActionPerformed

    private void txtIdDenunciaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdDenunciaFocusLost
        buscaDenuncia();
        //buscaTramitacao(txtIdProcesso.getText());
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaFocusLost

    private void txtIdDenunciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdDenunciaKeyPressed
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaKeyPressed

    private void txtIdDenunciaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdDenunciaKeyReleased
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaKeyReleased

    private void txtIdDenunciaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIdDenunciaKeyTyped
        //buscaProcesso();
        //buscaTramitacao(txtIdProcesso.getText()); // TODO add your handling code here:
        // TODO add your handling code here:
}//GEN-LAST:event_txtIdDenunciaKeyTyped

    private void tblTriagemMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTriagemMouseClicked
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
}//GEN-LAST:event_tblTriagemMouseClicked

    private void tblTriagemKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblTriagemKeyPressed
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
}//GEN-LAST:event_tblTriagemKeyPressed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    private void cbResumoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbResumoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbResumoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaDenuncia;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbResumo;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblTriagem;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdDenuncia;
    private javax.swing.JTextField txtNomeDenunciado;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the imovel
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param denuncia the imovel to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
        txtIdDenuncia.setText(denuncia.getId().toString());
        txtNomeDenunciado.setText(denuncia.getPDenunciado().getNome());
        //String idProcesso = juridico.getProcesso().getId().toString();
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
     * @return the loteTitulacao
     */



    /**
     * @return the usuario
     */
    
}
