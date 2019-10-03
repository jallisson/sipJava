/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.gabinete;

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
import sip.util.HorarioDeVerao;

/**
 *
 * @author T2Ti
 */
public class GabineteFrame extends javax.swing.JInternalFrame {

    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Gabinete> listGabinete;
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
    
    /** Creates new form ClienteFrame */
    public GabineteFrame() {
        initComponents();
        defineModelo();
        formataData();
        formataDataTramitacao();
        setaDataAtual();
        caixaAlta();
        fechando();
        jdcData.setEnabled(false);
        ftfMes.setVisible(false);
        buscaUsuario(idUsuario);
        ftfMesTramite.setVisible(false);
    }
    
     public GabineteFrame(JuridicoFrame analiseFrame) {
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
        txtIdJuridico.setText(analiseFrame.getIdAnalise());
        buscaJuridico();
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
       
        tableModel = (DefaultTableModel) tblGabinete.getModel();
        listModel = tblGabinete.getSelectionModel();
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


            tblGabinete.getColumnModel().getColumn(0).setPreferredWidth(40);
            tblGabinete.getColumnModel().getColumn(1).setPreferredWidth(150);
            tblGabinete.getColumnModel().getColumn(2).setPreferredWidth(270);
            tblGabinete.getColumnModel().getColumn(3).setPreferredWidth(380);
            tblGabinete.getColumnModel().getColumn(4).setPreferredWidth(300);
            tblGabinete.getColumnModel().getColumn(5).setPreferredWidth(150);
            
            
            
            
        } catch (ParseException ex) {
            Logger.getLogger(GabineteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void executaRelatorio() {
        AcessoBD acessoBd = new AcessoBD();
        int indice = tblGabinete.getSelectedRow();

        int mostraID = listGabinete.get(indice).getId();

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
    
    

    private void atualizaTabela() {
        GabineteBD parecerBD = new GabineteBD();

        if (txtFiltro.getText().equals("")) {
            listGabinete = parecerBD.consultaGabinete();
        } else {
            listGabinete = parecerBD.consultaGabineteNome(txtFiltro.getText());
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listGabinete.size(); i++) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
            tableModel.insertRow(i, new Object[]{
                listGabinete.get(i).getId(), 
                listGabinete.get(i).getProcesso().getNumProcesso(),
                listGabinete.get(i).getProcesso().getTipoLicenca(),
                listGabinete.get(i).getRequerente().getNome(),
                simpleDateFormat.format(listGabinete.get(i).getDataGabinete()),
                listGabinete.get(i).getUsuario().getNome()               
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
        if (tblGabinete.getSelectedRow() != -1) {
            int indice = tblGabinete.getSelectedRow();
            txtIdJuridico.setText(listGabinete.get(indice).getJuridico().getId().toString());
            txtNomeRequerente.setText(listGabinete.get(indice).getRequerente().getNome());
            jdcData.setDate(listGabinete.get(indice).getDataGabinete());   
    
        
  
        } else {
            txtIdJuridico.setText("");
            txtNomeRequerente.setText("");
            jdcData.setDate(null);
        }
    }
        
    private void executaTramitacao() throws ParseException{
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
            
            
            String controle = getJuridico().getProcesso().getId() + " " + "RECEBIDO DO JURIDICO" + " " + "GABINETE";
            
                
            tram.setUsuario(getUsuario());
            tram.setProcesso(getJuridico().getProcesso());
            tram.setDataTramitacao(timeStamp);
            tram.setMesAno(ftfMes.getText());
            tram.setStatus("RECEBIDO DO JURIDICO");
            tram.setParecer(" ");
            tram.setSetor("GABINETE");
            tram.setSetorOrigem(" ");
            tram.setSetorDestino(" ");
            tram.setParecer(" ");
            tram.setObservacao(null);
            tram.setControle(controle);
           
            TramitacaoBD tramitacaoBD = new TramitacaoBD();
     
                if (tramitacaoBD.incluiTramitacao(tram)) {
                JOptionPane.showMessageDialog(this, "tramitacao lançada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
              
                } else {
                JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
               } 
            }
    
    
     private void executaTramiRetornoJur() throws ParseException {
        java.util.Date hoje = new java.util.Date();
        jdcDataTramite = new JDateChooser("dd/MM/yyyy", "##/##/#####", '_');
        jdcDataTramite.setDate(hoje);
        
        Tramitacao tram = new Tramitacao();
        ftfMesTramite.setValue(jdcDataTramite.getDate());
        
        SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String theDate = dateTimeFormat.format(jdcDataTramite.getDate());
        java.util.Date converteDataHora = dateTimeFormat.parse(new HorarioDeVerao().horarioVerao());
        java.sql.Timestamp timeStamp = new java.sql.Timestamp(converteDataHora.getTime());
                
        String controle = getJuridico().getProcesso().getId() + " " + "RETORNO" + " " + " " + "GABINETE" + " " + "JURIDICO";

        tram.setUsuario(getUsuario());
        tram.setProcesso(getJuridico().getProcesso());
        tram.setDataTramitacao(timeStamp);
        tram.setMesAno(ftfMesTramite.getText());
        tram.setUsuario(getUsuario());
        tram.setStatus("RETORNO");
        tram.setParecer(" ");
        tram.setSetor(" ");
        tram.setSetorOrigem("GABINETE");
        tram.setSetorDestino("JURIDICO");
        tram.setLaudoMzu(" ");
        tram.setObservacao("RATIFICADO");
        tram.setControle(controle);

        TramitacaoBD tramitacaoBD = new TramitacaoBD();
        if (tramitacaoBD.incluiTramitacao(tram)) {
            JOptionPane.showMessageDialog(this, "tramitacao cadastrada com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            //alteraProcTramiDMA();
            //atualizaTabela();
            //esabilitaBotoes();
            //desabilitaCampos();
            //limpaCampos();

        } else {
            JOptionPane.showMessageDialog(this, "Tramitacao já cadastrada", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void incluiGabinete() {
       
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        }
                
        else  if (getJuridico() == null){
            JOptionPane.showMessageDialog(this, "Informe o dados do juridico!", "Analise", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
         } else  if ("SIM".equals(getJuridico().getProcesso().getArquivado())){
           JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
            
         }      
                
       else {
            Gabinete gabinete = new Gabinete();
            
            ftfMes.setValue(jdcData.getDate());
            
            
            gabinete.setUsuario(getUsuario());
            gabinete.setJuridico(getJuridico());
            gabinete.setDataGabinete(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime())); 
            gabinete.setTramitouJuridico("NAO");


            GabineteBD parecerBD = new GabineteBD();
            if (parecerBD.incluiGabinete(gabinete)) {
                JOptionPane.showMessageDialog(this, "Parecer lançado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                try {
                    executaTramitacao();
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
                JOptionPane.showMessageDialog(this, "Possivelmente o parecer já existe !", "Erro", JOptionPane.ERROR_MESSAGE);
                
            }
        }
    }

    private void alteraGabinete() {
        if (jdcData.getDate() == null) {
            JOptionPane.showMessageDialog(this, "Informe a data!", "Detalhe", JOptionPane.INFORMATION_MESSAGE);
        }
                
        else  if (getJuridico() == null){
            JOptionPane.showMessageDialog(this, "Informe o Imovel!", "Imovel", JOptionPane.INFORMATION_MESSAGE);
            txtIdJuridico.requestFocus();
         } 
         
        
      else {
            Gabinete parecerJur = new Gabinete();
            String controle;
            ftfMes.setValue(jdcData.getDate());
            controle = getJuridico().getId() + " " + parecerJur;
           
            parecerJur.setUsuario(getUsuario());
            parecerJur.setJuridico(getJuridico());
            parecerJur.setId(this.listGabinete.get(tblGabinete.getSelectedRow()).getId());       
            parecerJur.setDataGabinete(new java.sql.Date(((java.util.Date) jdcData.getDate()).getTime()));


            GabineteBD parecerBD = new GabineteBD();
            if (parecerBD.alteraGabinete(parecerJur)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void alteraGabiTramJuridico() {
        Gabinete gabi = new Gabinete();

        gabi.setId(listGabinete.get(tblGabinete.getSelectedRow()).getId());

        gabi.setTramitouJuridico("SIM");

        GabineteBD gabineteBD = new GabineteBD();

        if (gabineteBD.alteraGabiTramiJuridico(gabi)) {
            try {
                //JOptionPane.showMessageDialog(this, "Pr!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                executaTramiRetornoJur();
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao tramitar para Analise!", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void exlcuiGabinete() {
        GabineteBD parecerBD = new GabineteBD();
        if (parecerBD.excluiParecer(listGabinete.get(tblGabinete.getSelectedRow()))) {
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
                      
            txtIdJuridico.setText("");
            
            txtNomeRequerente.setText("");
           
        }  
    }
       
    private void habilitaCampos() {        
     
        jdcData.setEnabled(true);
        txtIdJuridico.setEditable(true);
        btnSelecionaJuridico.setEnabled(true);
       
    }

    private void desabilitaCampos() {
        jdcData.setEnabled(false);
        txtIdJuridico.setEditable(false);
        btnSelecionaJuridico.setEnabled(false);
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
        tblGabinete = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();
        btnTramitar = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jPanel20 = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        txtNomeRequerente = new javax.swing.JTextField();
        btnSelecionaJuridico = new javax.swing.JButton();
        txtIdJuridico = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setTitle("Laudo Técnico");
        setPreferredSize(new java.awt.Dimension(1000, 600));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/gabinetfinal_64x64.png"))); // NOI18N
        jLabel1.setText("GABINETE");
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
        gridBagConstraints.gridy = 3;
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

        tblGabinete.setFont(new java.awt.Font("Verdana", 1, 12));
        tblGabinete.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "PROCESSO", "LICENÇA", "REQUERENTE", "DATA", "USUARIO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class, java.lang.Object.class
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
        tblGabinete.setGridColor(new java.awt.Color(102, 102, 102));
        tblGabinete.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblGabinete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblGabineteMouseClicked(evt);
            }
        });
        tblGabinete.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblGabineteKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblGabinete);
        tblGabinete.getColumnModel().getColumn(4).setResizable(false);
        tblGabinete.getColumnModel().getColumn(5).setResizable(false);

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

        btnTramitar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitar.setText("Tramitar Juridico");
        btnTramitar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitarActionPerformed(evt);
            }
        });
        jPanel4.add(btnTramitar);

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

        getAccessibleContext().setAccessibleName("Cadastro Agência");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltro.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        GabineteBD gabineteBD = new GabineteBD();
            if(gabineteBD.testaConexao()){
                habilitaCampos();
                 
                limpaCampos();
                btnFiltrar.setEnabled(false);
                tblGabinete.setEnabled(false);
                limpaSelecaoTabela();
                txtIdJuridico.requestFocus();
                habilitaBotoes();
                modo = Constantes.INSERT_MODE;
                //caixaAlta();
            }
            else
                JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiGabinete();
            tblGabinete.setEnabled(true);
        } else if (modo == Constantes.EDIT_MODE) {
            alteraGabinete();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        tblGabinete.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblGabinete.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaJuridico();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblGabinete.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Bairro?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                exlcuiGabinete();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um bairro da lista!", "Bairro", JOptionPane.INFORMATION_MESSAGE);
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
        JuridicoFrame juriFrame = new JuridicoFrame(this);
        juriFrame.setVisible(true);
        this.getDesktopPane().add(juriFrame);
        juriFrame.toFront();
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

    private void tblGabineteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblGabineteMouseClicked
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
}//GEN-LAST:event_tblGabineteMouseClicked

    private void tblGabineteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblGabineteKeyPressed
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
}//GEN-LAST:event_tblGabineteKeyPressed

    private void btnTramitarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitarActionPerformed
        if (tblGabinete.getSelectedRow() != -1) {
            int indice = tblGabinete.getSelectedRow();
            int resposta = JOptionPane.showConfirmDialog(this, "Confirme o tramite?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                buscaJuridico();
                 if ("NAO".equals(listGabinete.get(indice).getProcesso().getArquivado())) {
                    alteraGabiTramJuridico();

                } else {
                    JOptionPane.showMessageDialog(this, "Processo Arquivado sem possibilidade de Movimento", "Arquivado", JOptionPane.INFORMATION_MESSAGE);
                }
        }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Processo da lista!", "Process", JOptionPane.INFORMATION_MESSAGE);
        }
}//GEN-LAST:event_btnTramitarActionPerformed

    private void ftfMesTramiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfMesTramiteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfMesTramiteActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaJuridico;
    private javax.swing.JButton btnTramitar;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField ftfMes;
    private javax.swing.JFormattedTextField ftfMesTramite;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblGabinete;
    private javax.swing.JTextField txtFiltro;
    private javax.swing.JTextField txtIdJuridico;
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
