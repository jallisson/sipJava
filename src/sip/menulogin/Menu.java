/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

 /*
 * JavaStarterT2Ti.java
 *
 * Created on 20/02/2011, 10:24:39
 */
package sip.menulogin;

import sip.acessobd.AcessoBD;
import sip.bean.NivelAcesso;
import sip.bairro.BairroFrame;
import sip.cidade.CidadeFrame;
import sip.usuario.Usuario;
import sip.usuario.UsuarioBD;
import sip.usuario.UsuarioFrame;
import sip.util.DesktopPaneImagem;
import sip.requerente.RequerenteFrame;

import sip.processo.ProcessoFrame;

import sip.logradouro.LogradouroFrame;
import sip.sobre.AvisoFrame;
import sip.sobre.SobreFrame_1;
import sip.tramitacao.TramitacaoFrame;
import sip.util.Constantes;

import sip.emissaolicenca.EmissaoLicencaFrame;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Frame;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.Beans;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.help.HelpBroker;
import javax.help.HelpSet;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.WindowConstants;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sip.analise.AnaliseFrame;
import sip.analista.AnalistaFrame;
import sip.atividade.AtividadeFrame1;
import sip.autorizacaoeventos.AutorizacaoEventosFrame;
import sip.distribuicao.DistribuicaoFrame;
import sip.gabinete.GabineteFrame;
import sip.juridico.JuridicoFrame;
import sip.localevento.LocalEventoFrame;
import sip.naturezaocorrencia.NaturezaOcorrencia;
import sip.naturezaocorrencia.NaturezaOcorrenciaFrame;
import sip.denuncia.DenunciaFrame;
import sip.fiscalizacao.FiscalizacaoFrame;
import sip.lotedma.LoteDmaFrame;
import sip.movimentodma.MovimentoDmaFrame;
import sip.pessoa.PessoaFrame;
import sip.relatorio.RelProcAnalistaDistFrame;
import sip.relatorio.RelatorioMovimentoProcessoFrame;
import sip.relatoriosarquivo.RelatorioTriagemPoluicaoFrame;
import sip.tipoevento.TipoEventoFrame;
import sip.triagem.TriagemFrame;
import sip.utilitarios.ControleAcessosView1;
import sip.resumotriagem.ResumoTriagemFrame;
import sip.tipolicenca.TipoLicencaFrame;
import sip.tramitacao.TramitacaoMonitorFrame;

/**
 *
 * @author T2Ti
 */
public class Menu extends javax.swing.JFrame {

    DesktopPaneImagem desktopPane = new DesktopPaneImagem(".\\imagem\\fundoazulclro.jpg");
    private AcessoBD acessoBD = new AcessoBD();
    public static String id_Usuario;
    private List<Usuario> listUsuario;
    private Usuario usuario;
    private int modo;
    private HelpSet hs;
    private HelpBroker hb;
    private URL hsURL;

    /**
     * Creates new form JavaStarterT2Ti
     */
    public Menu() {
        initComponents();
        if (!Beans.isDesignTime()) {
            entityManager.getTransaction().begin();
        }
        //openHelp();
        setSize(380, 522);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        //add(desktopPane, "card1");
        //jDesktopPane1.add(sobreFrame1);chamada de uma tela de aviso
        //sobreFrame1.setVisible(true);chamada de uma tela de aviso
        add(desktopPane, "card1");
        //setLocationRelativeTo(null);
        fechando(this);
        //menuInvisivel();
        controleModulo();
        //mnuNivelAcesso1.setVisible(false);
        //iniciandoAviso();
        this.setModalExclusionType(ModalExclusionType.TOOLKIT_EXCLUDE);
        buscaUsuario(id_Usuario);
        centralizaJLabel(jLabel1);
        centralizaJLabel1(jLabel2);
     
    }

    public final void testando() {
        if (modo == Constantes.INCLUIR) {
            menuInvisivel();
        } else {
            menuVisivel();
        }

    }

    private void centralizaJanela(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 2,
                (this.getHeight() - internalFrame.getHeight()) / 2);
    }

    private void centralizaJLabel(JLabel nomeLabel) {
        nomeLabel.setLocation((this.getWidth() + nomeLabel.getWidth()) / 6,
                (this.getHeight() + nomeLabel.getHeight()) + 50);
    }

    private void centralizaJLabel1(JLabel nomeLabel) {
        nomeLabel.setLocation((this.getWidth() + nomeLabel.getWidth()) / 20,
                (this.getHeight() + nomeLabel.getHeight()) + 50);
    }

    private void centralizaJanelaRegVen(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 2,
                (this.getHeight() - internalFrame.getHeight()) / 6);
    }

    private void centralizaJanelaTramitacao(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 4,
                (this.getHeight() - internalFrame.getHeight()) / 4);
    }

    private void desabilitando(JFrame jFrame, JInternalFrame jInternalFrame) {
        jFrame.setEnabled(false);
        jInternalFrame.setEnabled(true);
    }

    public void iniciandoAviso() {

        AvisoFrame avisoFrame = new AvisoFrame();
        jDesktopPane.add(avisoFrame);
        avisoFrame.setVisible(true);
        centralizaJanelaRegVen(avisoFrame);
    }

    private void fechando(JFrame janela) {

        //Desabilita o botao 'X'  
        janela.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        //Adaptador para o fechamento da janela, matando o processo  
        janela.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                //caixa de dialogo retorna um inteiro  
                int resposta = JOptionPane.showConfirmDialog(null, "Deseja realmente finalizar o sistema ?", "Finalizar", JOptionPane.YES_NO_OPTION);

                //sim = 0, nao = 1  
                if (resposta == 0) {
                    System.exit(0);
                }

            }
        });
        // janela.setSize(800,600);  
        // janela.setVisible (true); 
    }

    public void openHelp() {
        // Identifica a localização do arquivo .hs
        String pathToHS = "/appwithhelp/docs/appwithhelp-hs.xml";
        //Cria a URL para a localização do help set
        try {
            hsURL = getClass().getResource(pathToHS);
            hs = new HelpSet(null, hsURL);
        } catch (Exception ee) {
            System.out.println("HelpSet " + ee.getMessage());
            System.out.println("Help Set " + pathToHS + " não encontrado");
            return;
        }

        // Cria um objeto HelpBroker para manipular o help set
        hb = hs.createHelpBroker();
        // Mostra o help set
        hb.setDisplayed(true);
    }

    private void closed() {
        int qOpcao = JOptionPane.showConfirmDialog(this, "Deseja realmente finalizar o sistema ?", "Finalizar ", JOptionPane.YES_NO_OPTION);
        if (qOpcao == JOptionPane.YES_OPTION) {
            System.exit(0);
        } else {
            return;
        }

    }

    private boolean verificaAcesso(String nomeMenu, String idUsuario) {
        query = entityManager.createNamedQuery("NivelAcesso.verifiqueAcesso");
        query.setParameter("idUsuario", Integer.parseInt(idUsuario));
        query.setParameter("nomeModulo", nomeMenu);
        List<NivelAcesso> data = query.getResultList();
        if (data.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    private void buscaUsuario(String idUsuario) {
        UsuarioBD usuarioBD = new UsuarioBD();
        listUsuario = usuarioBD.consultaUsuario();
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
                //txtIdMonitor.setText("");
                //txtNomeMonitor.setText("");
                setUsuario(null);
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            setUsuario(null);
            //txtNomeMonitor.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public static void menuInvisivel() {
        mnuProtocolo.setEnabled(false);
        mnuMovimento.setEnabled(false);
        mnuRelatorios.setEnabled(false);
        mnuUtilitarios.setEnabled(false);
        mnuSistema.setEnabled(false);
        mnuSair.setEnabled(false);
        mnuAjuda.setEnabled(false);
    }

    public static void menuVisivel() {
        mnuProtocolo.setEnabled(true);
        mnuMovimento.setEnabled(true);
        mnuRelatorios.setEnabled(true);
        mnuUtilitarios.setEnabled(true);
        mnuSistema.setEnabled(true);
        mnuSair.setEnabled(true);
        mnuAjuda.setEnabled(true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        entityManager = java.beans.Beans.isDesignTime() ? null : javax.persistence.Persistence.createEntityManagerFactory("SipPU").createEntityManager();
        query = java.beans.Beans.isDesignTime() ? null : entityManager.createQuery("SELECT n FROM NivelAcesso n");
        list = java.beans.Beans.isDesignTime() ? java.util.Collections.emptyList() : query.getResultList();
        jDesktopPane = new javax.swing.JDesktopPane();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        btnLogradouro = new javax.swing.JButton();
        btnBairro = new javax.swing.JButton();
        btnCidade = new javax.swing.JButton();
        btnAtividade = new javax.swing.JButton();
        btnRequerente = new javax.swing.JButton();
        btnAnalista = new javax.swing.JButton();
        btnProcesso = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        btnDMA = new javax.swing.JButton();
        btnEmissaoLicenca = new javax.swing.JButton();
        btnAutorizacaoEventos = new javax.swing.JButton();
        btnAnalise = new javax.swing.JButton();
        btnJuridico = new javax.swing.JButton();
        btnGabinete = new javax.swing.JButton();
        btnTramitacao = new javax.swing.JButton();
        btnLoteDma = new javax.swing.JButton();
        btnMovimentoDma = new javax.swing.JButton();
        btnTipoLicenca = new javax.swing.JButton();
        btnFiscalizacao = new javax.swing.JButton();
        btnDenuncia = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        mnuSistema = new javax.swing.JMenu();
        mnuItemSair = new javax.swing.JMenuItem();
        mnuItemTrocaUsuario = new javax.swing.JMenuItem();
        mnuProtocolo = new javax.swing.JMenu();
        mnuItemUsuario = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        mmuProtocolo = new javax.swing.JMenu();
        mnuLogradouro = new javax.swing.JMenuItem();
        mnuItemBairro = new javax.swing.JMenuItem();
        mnuItemCidade = new javax.swing.JMenuItem();
        mnuAtividade = new javax.swing.JMenuItem();
        mnuItemRequerente = new javax.swing.JMenuItem();
        mnuItemAnalista = new javax.swing.JMenuItem();
        mnuItemTipoLicenca = new javax.swing.JMenuItem();
        mmuProtocolo1 = new javax.swing.JMenu();
        mnuLogradouro1 = new javax.swing.JMenuItem();
        mnuLogradouro2 = new javax.swing.JMenuItem();
        mnuLogradouro3 = new javax.swing.JMenuItem();
        mnuLogradouro4 = new javax.swing.JMenuItem();
        mnuLogradouro5 = new javax.swing.JMenuItem();
        mnuMovimento = new javax.swing.JMenu();
        mnuItemProcesso = new javax.swing.JMenuItem();
        jMenuDMA = new javax.swing.JMenu();
        mnuItemDistribuicao = new javax.swing.JMenuItem();
        mnuItemEmissaoLicenca = new javax.swing.JMenuItem();
        mnuTipoEvento = new javax.swing.JMenuItem();
        mnuTipoEvento1 = new javax.swing.JMenuItem();
        mnuItemAutorizacaoEventos = new javax.swing.JMenuItem();
        mnuItemLoteDma = new javax.swing.JMenuItem();
        mnuItemMovimentoDma = new javax.swing.JMenuItem();
        mnuItemAnalise = new javax.swing.JMenuItem();
        mnuItemJuridico = new javax.swing.JMenuItem();
        mnuItemJuridico1 = new javax.swing.JMenuItem();
        mnuItemGabinete = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        mnuItemTramitacao = new javax.swing.JMenuItem();
        mnuItemTramitacao1 = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        mnuItemGabinete1 = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        mnuRelatorios = new javax.swing.JMenu();
        mnuListagem = new javax.swing.JMenu();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        mnuTramitacao = new javax.swing.JMenu();
        mnuListagem2 = new javax.swing.JMenu();
        mnuItemRelMovimentoProcesso = new javax.swing.JMenuItem();
        mnuGraficos = new javax.swing.JMenu();
        mnuDenuncia = new javax.swing.JMenu();
        mnuListagem3 = new javax.swing.JMenu();
        mnuItemRelTriagemPoluicao = new javax.swing.JMenuItem();
        mnuDenuncia1 = new javax.swing.JMenu();
        mnuListagem4 = new javax.swing.JMenu();
        mnuItemRelTriagemPoluicao1 = new javax.swing.JMenuItem();
        mnuItemRelTriagemPoluicao2 = new javax.swing.JMenuItem();
        mnuUtilitarios = new javax.swing.JMenu();
        mnuNivelAcesso = new javax.swing.JMenuItem();
        mnuAjuda = new javax.swing.JMenu();
        mnuConteudo = new javax.swing.JMenuItem();
        mnuSobre1 = new javax.swing.JMenuItem();
        mnuSair = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SIP 1.0 - SEMMARH");
        setBackground(new java.awt.Color(51, 0, 51));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocationByPlatform(true);
        setMinimumSize(new java.awt.Dimension(286, 25));
        getContentPane().setLayout(new java.awt.CardLayout());

        jDesktopPane.setBackground(new java.awt.Color(255, 255, 255));
        jDesktopPane.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1000, 1, new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/fun.jpg")))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        jDesktopPane.add(jLabel1);
        jLabel1.setBounds(190, 140, 220, 60);

        jLabel2.setFont(new java.awt.Font("Verdana", 1, 11)); // NOI18N
        jDesktopPane.add(jLabel2);
        jLabel2.setBounds(190, 140, 220, 60);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/fundosipemenu2.jpg"))); // NOI18N
        jDesktopPane.add(jLabel3);
        jLabel3.setBounds(450, 180, 620, 350);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        btnLogradouro.setBackground(new java.awt.Color(255, 255, 255));
        btnLogradouro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/olho na estrada logo.png"))); // NOI18N
        btnLogradouro.setToolTipText("LOGRADOURO\n");
        btnLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogradouroActionPerformed(evt);
            }
        });
        jPanel1.add(btnLogradouro);

        btnBairro.setBackground(new java.awt.Color(255, 255, 255));
        btnBairro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/neighborhood_32x32.png"))); // NOI18N
        btnBairro.setToolTipText("BAIRRO\n");
        btnBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBairroActionPerformed(evt);
            }
        });
        jPanel1.add(btnBairro);

        btnCidade.setBackground(new java.awt.Color(255, 255, 255));
        btnCidade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/cidade_32x32.png"))); // NOI18N
        btnCidade.setToolTipText("CIDADE");
        btnCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCidadeActionPerformed(evt);
            }
        });
        jPanel1.add(btnCidade);

        btnAtividade.setBackground(new java.awt.Color(255, 255, 255));
        btnAtividade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/MZU_32x32.png"))); // NOI18N
        btnAtividade.setToolTipText("TRAMITAÇÃO");
        btnAtividade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtividadeActionPerformed(evt);
            }
        });
        jPanel1.add(btnAtividade);

        btnRequerente.setBackground(new java.awt.Color(255, 255, 255));
        btnRequerente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/requerente_32x32.png"))); // NOI18N
        btnRequerente.setToolTipText("REQUERENTE\n");
        btnRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRequerenteActionPerformed(evt);
            }
        });
        jPanel1.add(btnRequerente);

        btnAnalista.setBackground(new java.awt.Color(255, 255, 255));
        btnAnalista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/AGENTE_32x32.png"))); // NOI18N
        btnAnalista.setToolTipText("TRAMITAÇÃO");
        btnAnalista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnalistaActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnalista);

        btnProcesso.setBackground(new java.awt.Color(255, 255, 255));
        btnProcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/processo_32x32.png"))); // NOI18N
        btnProcesso.setToolTipText("PROCESSO");
        btnProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProcessoActionPerformed(evt);
            }
        });
        jPanel1.add(btnProcesso);
        jPanel1.add(jSeparator4);

        btnDMA.setBackground(new java.awt.Color(255, 255, 255));
        btnDMA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/DISTRI_32x32.png"))); // NOI18N
        btnDMA.setToolTipText("DISTRIBUIÇÃO");
        btnDMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDMAActionPerformed(evt);
            }
        });
        jPanel1.add(btnDMA);

        btnEmissaoLicenca.setBackground(new java.awt.Color(255, 255, 255));
        btnEmissaoLicenca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/titulo_32x32.png"))); // NOI18N
        btnEmissaoLicenca.setToolTipText("EMISSÃO LICENÇA");
        btnEmissaoLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEmissaoLicencaActionPerformed(evt);
            }
        });
        jPanel1.add(btnEmissaoLicenca);

        btnAutorizacaoEventos.setBackground(new java.awt.Color(255, 255, 255));
        btnAutorizacaoEventos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/autorizacao_32x32.png"))); // NOI18N
        btnAutorizacaoEventos.setToolTipText("AUTORIZAÇÃO DE EVENTOS");
        btnAutorizacaoEventos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutorizacaoEventosActionPerformed(evt);
            }
        });
        jPanel1.add(btnAutorizacaoEventos);

        btnAnalise.setBackground(new java.awt.Color(255, 255, 255));
        btnAnalise.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/laudo_32x32.png"))); // NOI18N
        btnAnalise.setToolTipText("Analise");
        btnAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAnaliseActionPerformed(evt);
            }
        });
        jPanel1.add(btnAnalise);

        btnJuridico.setBackground(new java.awt.Color(255, 255, 255));
        btnJuridico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/juridico_32x32.png"))); // NOI18N
        btnJuridico.setToolTipText("TRAMITAÇÃO");
        btnJuridico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnJuridicoActionPerformed(evt);
            }
        });
        jPanel1.add(btnJuridico);

        btnGabinete.setBackground(new java.awt.Color(255, 255, 255));
        btnGabinete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/gabinetfinal_32x32.png"))); // NOI18N
        btnGabinete.setToolTipText("TRAMITAÇÃO");
        btnGabinete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGabineteActionPerformed(evt);
            }
        });
        jPanel1.add(btnGabinete);

        btnTramitacao.setBackground(new java.awt.Color(255, 255, 255));
        btnTramitacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        btnTramitacao.setToolTipText("TRAMITAÇÃO");
        btnTramitacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTramitacaoActionPerformed(evt);
            }
        });
        jPanel1.add(btnTramitacao);

        btnLoteDma.setBackground(new java.awt.Color(255, 255, 255));
        btnLoteDma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/lote_32x32.png"))); // NOI18N
        btnLoteDma.setToolTipText("LOTE DMA");
        btnLoteDma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLoteDmaActionPerformed(evt);
            }
        });
        jPanel1.add(btnLoteDma);

        btnMovimentoDma.setBackground(new java.awt.Color(255, 255, 255));
        btnMovimentoDma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/icons8-movimento-de-estoque-32.png"))); // NOI18N
        btnMovimentoDma.setToolTipText("MOVIMENTO DMA");
        btnMovimentoDma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMovimentoDmaActionPerformed(evt);
            }
        });
        jPanel1.add(btnMovimentoDma);

        btnTipoLicenca.setBackground(new java.awt.Color(255, 255, 255));
        btnTipoLicenca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/9433_32x32.png"))); // NOI18N
        btnTipoLicenca.setToolTipText("TIPO DE LICENÇA");
        btnTipoLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTipoLicencaActionPerformed(evt);
            }
        });
        jPanel1.add(btnTipoLicenca);

        btnFiscalizacao.setBackground(new java.awt.Color(255, 255, 255));
        btnFiscalizacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/Fiscalizacao_32x32.png"))); // NOI18N
        btnFiscalizacao.setToolTipText("FISCALIZAÇÃO");
        btnFiscalizacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFiscalizacaoActionPerformed(evt);
            }
        });
        jPanel1.add(btnFiscalizacao);

        btnDenuncia.setBackground(new java.awt.Color(255, 255, 255));
        btnDenuncia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ocorrencia_32x32.png"))); // NOI18N
        btnDenuncia.setToolTipText("DENÚNCIA");
        btnDenuncia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDenunciaActionPerformed(evt);
            }
        });
        jPanel1.add(btnDenuncia);

        jDesktopPane.add(jPanel1);
        jPanel1.setBounds(0, 0, 160, 480);

        getContentPane().add(jDesktopPane, "card2");

        jMenuBar1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jMenuBar1.setEnabled(false);
        jMenuBar1.setMaximumSize(new java.awt.Dimension(286, 21));
        jMenuBar1.setMinimumSize(new java.awt.Dimension(285, 21));

        mnuSistema.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        mnuSistema.setText("Sistema");
        mnuSistema.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N

        mnuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/door_out32.png"))); // NOI18N
        mnuItemSair.setText("Sair");
        mnuItemSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemSairActionPerformed(evt);
            }
        });
        mnuSistema.add(mnuItemSair);

        mnuItemTrocaUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemTrocaUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/group_edit.png"))); // NOI18N
        mnuItemTrocaUsuario.setText("Trocar Usuario");
        mnuItemTrocaUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTrocaUsuarioActionPerformed(evt);
            }
        });
        mnuSistema.add(mnuItemTrocaUsuario);

        jMenuBar1.add(mnuSistema);

        mnuProtocolo.setText("Cadastro");
        mnuProtocolo.setToolTipText("Menu Cadastro");
        mnuProtocolo.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuProtocolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuProtocoloActionPerformed(evt);
            }
        });

        mnuItemUsuario.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mnuItemUsuario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/group.png"))); // NOI18N
        mnuItemUsuario.setText("Usuario");
        mnuItemUsuario.setToolTipText("Tela para Cadastro do Monitor");
        mnuItemUsuario.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemUsuarioActionPerformed(evt);
            }
        });
        mnuProtocolo.add(mnuItemUsuario);
        mnuProtocolo.add(jSeparator2);

        mmuProtocolo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/protocolo_32x32.png"))); // NOI18N
        mmuProtocolo.setText("Protocolo");
        mmuProtocolo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mmuProtocoloActionPerformed(evt);
            }
        });

        mnuLogradouro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/olho na estrada logo.png"))); // NOI18N
        mnuLogradouro.setText("Logradouro");
        mnuLogradouro.setToolTipText("Cadastro Logradouro");
        mnuLogradouro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouroActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuLogradouro);

        mnuItemBairro.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemBairro.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/neighborhood_32x32.png"))); // NOI18N
        mnuItemBairro.setText("Bairro");
        mnuItemBairro.setToolTipText("Cadastro de Bairro");
        mnuItemBairro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemBairroActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuItemBairro);

        mnuItemCidade.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemCidade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/cidade_32x32.png"))); // NOI18N
        mnuItemCidade.setText("Cidade");
        mnuItemCidade.setToolTipText("Cadastro de Cidade");
        mnuItemCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemCidadeActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuItemCidade);

        mnuAtividade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/MZU_32x32.png"))); // NOI18N
        mnuAtividade.setText("Atividade");
        mnuAtividade.setToolTipText("Cadastro Logradouro");
        mnuAtividade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAtividadeActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuAtividade);

        mnuItemRequerente.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        mnuItemRequerente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/requerente_32x32.png"))); // NOI18N
        mnuItemRequerente.setText("Requerente");
        mnuItemRequerente.setToolTipText("Cadastro de Requerente");
        mnuItemRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRequerenteActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuItemRequerente);

        mnuItemAnalista.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemAnalista.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/AGENTE_32x32.png"))); // NOI18N
        mnuItemAnalista.setText("Analista / Fiscal");
        mnuItemAnalista.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemAnalista.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemAnalistaActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuItemAnalista);

        mnuItemTipoLicenca.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemTipoLicenca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/9433_32x32.png"))); // NOI18N
        mnuItemTipoLicenca.setText("Tipo Licença");
        mnuItemTipoLicenca.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemTipoLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTipoLicencaActionPerformed(evt);
            }
        });
        mmuProtocolo.add(mnuItemTipoLicenca);

        mnuProtocolo.add(mmuProtocolo);

        mmuProtocolo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/DENUNCIA_32x32 copy.png"))); // NOI18N
        mmuProtocolo1.setText("Denuncia");
        mmuProtocolo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mmuProtocolo1ActionPerformed(evt);
            }
        });

        mnuLogradouro1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/PESSOA32x32.png"))); // NOI18N
        mnuLogradouro1.setText("Pessoa");
        mnuLogradouro1.setToolTipText("Cadastro Logradouro");
        mnuLogradouro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouro1ActionPerformed(evt);
            }
        });
        mmuProtocolo1.add(mnuLogradouro1);

        mnuLogradouro2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/NATUREZA_32x32.png"))); // NOI18N
        mnuLogradouro2.setText("Natureza Ocorrência");
        mnuLogradouro2.setToolTipText("Cadastro Logradouro");
        mnuLogradouro2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouro2ActionPerformed(evt);
            }
        });
        mmuProtocolo1.add(mnuLogradouro2);

        mnuLogradouro3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/ocorrencia_32x32.png"))); // NOI18N
        mnuLogradouro3.setText("Denuncia");
        mnuLogradouro3.setToolTipText("Cadastro Logradouro");
        mnuLogradouro3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouro3ActionPerformed(evt);
            }
        });
        mmuProtocolo1.add(mnuLogradouro3);

        mnuLogradouro4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/edit_32x32.png"))); // NOI18N
        mnuLogradouro4.setText("Resumo Triagem");
        mnuLogradouro4.setToolTipText("Cadastro Logradouro");
        mnuLogradouro4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouro4ActionPerformed(evt);
            }
        });
        mmuProtocolo1.add(mnuLogradouro4);

        mnuLogradouro5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/MOT_COB_32x32.png"))); // NOI18N
        mnuLogradouro5.setText("Triagem");
        mnuLogradouro5.setToolTipText("Cadastro Logradouro");
        mnuLogradouro5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuLogradouro5ActionPerformed(evt);
            }
        });
        mmuProtocolo1.add(mnuLogradouro5);

        mnuProtocolo.add(mmuProtocolo1);

        jMenuBar1.add(mnuProtocolo);

        mnuMovimento.setText("Movimento");
        mnuMovimento.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuMovimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuMovimentoActionPerformed(evt);
            }
        });

        mnuItemProcesso.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemProcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/processo_32x32.png"))); // NOI18N
        mnuItemProcesso.setText("Processo");
        mnuItemProcesso.setToolTipText("Cadastro de Processo");
        mnuItemProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemProcessoActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemProcesso);

        jMenuDMA.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/DISTRI_32x32.png"))); // NOI18N
        jMenuDMA.setText("DMA");

        mnuItemDistribuicao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemDistribuicao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/DISTRI_32x32.png"))); // NOI18N
        mnuItemDistribuicao.setText("Distribuicao DMA");
        mnuItemDistribuicao.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemDistribuicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemDistribuicaoActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuItemDistribuicao);

        mnuItemEmissaoLicenca.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemEmissaoLicenca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/titulo_32x32.png"))); // NOI18N
        mnuItemEmissaoLicenca.setText("Emissão Licença");
        mnuItemEmissaoLicenca.setToolTipText("Emissão de Titulo");
        mnuItemEmissaoLicenca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemEmissaoLicencaActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuItemEmissaoLicenca);

        mnuTipoEvento.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tipoevento_32x32.png"))); // NOI18N
        mnuTipoEvento.setText("Tipo Evento");
        mnuTipoEvento.setToolTipText("Cadastro Logradouro");
        mnuTipoEvento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTipoEventoActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuTipoEvento);

        mnuTipoEvento1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/localEvento32x32.png"))); // NOI18N
        mnuTipoEvento1.setText("Local Evento");
        mnuTipoEvento1.setToolTipText("Cadastro Logradouro");
        mnuTipoEvento1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuTipoEvento1ActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuTipoEvento1);

        mnuItemAutorizacaoEventos.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemAutorizacaoEventos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/autorizacao_32x32.png"))); // NOI18N
        mnuItemAutorizacaoEventos.setText("Autorização Eventos");
        mnuItemAutorizacaoEventos.setToolTipText("Emissão de Titulo");
        mnuItemAutorizacaoEventos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemAutorizacaoEventosActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuItemAutorizacaoEventos);

        mnuItemLoteDma.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemLoteDma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/lote_32x32.png"))); // NOI18N
        mnuItemLoteDma.setText("Lote DMA");
        mnuItemLoteDma.setToolTipText("Emissão de Titulo");
        mnuItemLoteDma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemLoteDmaActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuItemLoteDma);

        mnuItemMovimentoDma.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemMovimentoDma.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/icons8-movimento-de-estoque-32.png"))); // NOI18N
        mnuItemMovimentoDma.setText("Movimento DMA");
        mnuItemMovimentoDma.setToolTipText("Emissão de Titulo");
        mnuItemMovimentoDma.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemMovimentoDmaActionPerformed(evt);
            }
        });
        jMenuDMA.add(mnuItemMovimentoDma);

        mnuMovimento.add(jMenuDMA);

        mnuItemAnalise.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemAnalise.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/laudo_32x32.png"))); // NOI18N
        mnuItemAnalise.setText("Análise");
        mnuItemAnalise.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemAnalise.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemAnaliseActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemAnalise);

        mnuItemJuridico.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemJuridico.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/juridico_32x32.png"))); // NOI18N
        mnuItemJuridico.setText("Jurídico");
        mnuItemJuridico.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemJuridico.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemJuridicoActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemJuridico);

        mnuItemJuridico1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemJuridico1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/Fiscalizacao_32x32.png"))); // NOI18N
        mnuItemJuridico1.setText("Fiscalização");
        mnuItemJuridico1.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemJuridico1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemJuridico1ActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemJuridico1);

        mnuItemGabinete.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemGabinete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/gabinetfinal_32x32.png"))); // NOI18N
        mnuItemGabinete.setText("Gabinete");
        mnuItemGabinete.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemGabinete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemGabineteActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemGabinete);
        mnuMovimento.add(jSeparator5);

        mnuItemTramitacao.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemTramitacao.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/tramitar_32x32.png"))); // NOI18N
        mnuItemTramitacao.setText("Tramitação");
        mnuItemTramitacao.setToolTipText("Tramitação");
        mnuItemTramitacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTramitacaoActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemTramitacao);

        mnuItemTramitacao1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        mnuItemTramitacao1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/monitor_32x32.png"))); // NOI18N
        mnuItemTramitacao1.setText("Monitor de Movimento");
        mnuItemTramitacao1.setToolTipText("Tramitação");
        mnuItemTramitacao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemTramitacao1ActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemTramitacao1);
        mnuMovimento.add(jSeparator3);

        mnuItemGabinete1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, 0));
        mnuItemGabinete1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/cash_32x32.png"))); // NOI18N
        mnuItemGabinete1.setText("Receitas");
        mnuItemGabinete1.setToolTipText("Cadastro Terreno Imobiliario");
        mnuItemGabinete1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemGabinete1ActionPerformed(evt);
            }
        });
        mnuMovimento.add(mnuItemGabinete1);
        mnuMovimento.add(jSeparator6);

        jMenuBar1.add(mnuMovimento);

        mnuRelatorios.setText("Relatórios");
        mnuRelatorios.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuRelatorios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuRelatoriosActionPerformed(evt);
            }
        });

        mnuListagem.setText("Listagem");
        mnuRelatorios.add(mnuListagem);
        mnuRelatorios.add(jSeparator1);

        mnuTramitacao.setText("Tramitação");

        mnuListagem2.setText("Listagem");

        mnuItemRelMovimentoProcesso.setText("Movimento Processo");
        mnuItemRelMovimentoProcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRelMovimentoProcessoActionPerformed(evt);
            }
        });
        mnuListagem2.add(mnuItemRelMovimentoProcesso);

        mnuTramitacao.add(mnuListagem2);

        mnuRelatorios.add(mnuTramitacao);

        mnuGraficos.setText("Gráficos");
        mnuRelatorios.add(mnuGraficos);

        mnuDenuncia.setText("Denuncia");

        mnuListagem3.setText("Listagem");

        mnuItemRelTriagemPoluicao.setText("Relatorio Triagem Poluição");
        mnuItemRelTriagemPoluicao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRelTriagemPoluicaoActionPerformed(evt);
            }
        });
        mnuListagem3.add(mnuItemRelTriagemPoluicao);

        mnuDenuncia.add(mnuListagem3);

        mnuRelatorios.add(mnuDenuncia);

        mnuDenuncia1.setText("Distribuição");

        mnuListagem4.setText("Listagem");

        mnuItemRelTriagemPoluicao1.setText("Processo Por Analista em Analise");
        mnuItemRelTriagemPoluicao1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRelTriagemPoluicao1ActionPerformed(evt);
            }
        });
        mnuListagem4.add(mnuItemRelTriagemPoluicao1);

        mnuItemRelTriagemPoluicao2.setText("Processo não baixados");
        mnuItemRelTriagemPoluicao2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuItemRelTriagemPoluicao2ActionPerformed(evt);
            }
        });
        mnuListagem4.add(mnuItemRelTriagemPoluicao2);

        mnuDenuncia1.add(mnuListagem4);

        mnuRelatorios.add(mnuDenuncia1);

        jMenuBar1.add(mnuRelatorios);

        mnuUtilitarios.setText("Utilitarios");
        mnuUtilitarios.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuUtilitarios.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuUtilitariosActionPerformed(evt);
            }
        });

        mnuNivelAcesso.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        mnuNivelAcesso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/7693_32x32.png"))); // NOI18N
        mnuNivelAcesso.setText("Nível de Acesso");
        mnuNivelAcesso.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuNivelAcessoActionPerformed(evt);
            }
        });
        mnuUtilitarios.add(mnuNivelAcesso);

        jMenuBar1.add(mnuUtilitarios);

        mnuAjuda.setText("Ajuda");
        mnuAjuda.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuAjuda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuAjudaActionPerformed(evt);
            }
        });

        mnuConteudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        mnuConteudo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/information.png"))); // NOI18N
        mnuConteudo.setText("Conteúdo");
        mnuConteudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuConteudoActionPerformed(evt);
            }
        });
        mnuAjuda.add(mnuConteudo);

        mnuSobre1.setText("Sobre");
        mnuSobre1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSobre1ActionPerformed(evt);
            }
        });
        mnuAjuda.add(mnuSobre1);

        jMenuBar1.add(mnuAjuda);

        mnuSair.setText("Sair");
        mnuSair.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        mnuSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                mnuSairMouseClicked(evt);
            }
        });
        mnuSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnuSairActionPerformed(evt);
            }
        });
        jMenuBar1.add(mnuSair);

        setJMenuBar(jMenuBar1);

        setSize(new java.awt.Dimension(828, 732));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void mnuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemSairActionPerformed
        closed();
    }//GEN-LAST:event_mnuItemSairActionPerformed

    private void mnuItemUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemUsuarioActionPerformed
        UsuarioFrame usuarioFrame = new UsuarioFrame();
        jDesktopPane.add(usuarioFrame);
        usuarioFrame.setVisible(true);
        centralizaJanela(usuarioFrame);
        //menuInvisivel();

    }//GEN-LAST:event_mnuItemUsuarioActionPerformed

    private void mnuSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSairActionPerformed
        closed();     // TODO add your handling code here:
    }//GEN-LAST:event_mnuSairActionPerformed

    private void mnuSairMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mnuSairMouseClicked
        closed();    // TODO add your handling code here:
    }//GEN-LAST:event_mnuSairMouseClicked

    private void mnuItemRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRequerenteActionPerformed
        RequerenteFrame requerenteFrame = new RequerenteFrame();
        jDesktopPane.add(requerenteFrame);
        requerenteFrame.setVisible(true);
        centralizaJanelaRegVen(requerenteFrame);
        //menuInvisivel();
    }//GEN-LAST:event_mnuItemRequerenteActionPerformed

    private void mnuLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouroActionPerformed
        LogradouroFrame ruaAv = new LogradouroFrame();
        jDesktopPane.add(ruaAv);
        ruaAv.setVisible(true);
        centralizaJanela(ruaAv);

        //menuInvisivel();
        //desabilitando(this,rotaFrame);
    }//GEN-LAST:event_mnuLogradouroActionPerformed

    private void mnuProtocoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuProtocoloActionPerformed

    }//GEN-LAST:event_mnuProtocoloActionPerformed

    private void mnuRelatoriosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuRelatoriosActionPerformed
        AcessoBD acessoBd = new AcessoBD();
        try {
            HashMap parametros = new HashMap();
            parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\DTI.jpg");
            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\Rota.jasper", parametros, acessoBd.conectar());
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }  // TODO add your handling code here:
}//GEN-LAST:event_mnuRelatoriosActionPerformed

    private void mnuItemBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemBairroActionPerformed
        BairroFrame bairroFrame = new BairroFrame();
        jDesktopPane.add(bairroFrame);
        bairroFrame.setVisible(true);
        centralizaJanela(bairroFrame);
        //menuInvisivel();
    }//GEN-LAST:event_mnuItemBairroActionPerformed

    private void mnuAjudaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAjudaActionPerformed
    }//GEN-LAST:event_mnuAjudaActionPerformed

    private void mnuConteudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuConteudoActionPerformed
        openHelp();
    }//GEN-LAST:event_mnuConteudoActionPerformed

    private void mnuUtilitariosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuUtilitariosActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuUtilitariosActionPerformed

    private void mnuNivelAcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuNivelAcessoActionPerformed
        String args[] = {""};
        ControleAcessosView1.main(args);
        //menuInvisivel();
}//GEN-LAST:event_mnuNivelAcessoActionPerformed

    private void mnuSobre1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuSobre1ActionPerformed
        SobreFrame_1 sobreFrame = new SobreFrame_1();
        jDesktopPane.add(sobreFrame);
        sobreFrame.setVisible(true);
        centralizaJanelaRegVen(sobreFrame);

        // TODO add your handling code here:
}//GEN-LAST:event_mnuSobre1ActionPerformed

    private void mnuItemProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemProcessoActionPerformed
        ProcessoFrame processoFrame = new ProcessoFrame();
        jDesktopPane.add(processoFrame);
        processoFrame.setVisible(true);
        centralizaJanelaRegVen(processoFrame);
        //menuInvisivel(); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemProcessoActionPerformed

    private void mnuItemTramitacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTramitacaoActionPerformed
        TramitacaoFrame tramitacao = new TramitacaoFrame();
        jDesktopPane.add(tramitacao);
        tramitacao.setVisible(true);
        centralizaJanelaRegVen(tramitacao);
        //menuInvisivel(); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemTramitacaoActionPerformed

    private void mnuItemCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemCidadeActionPerformed
        CidadeFrame cidade = new CidadeFrame();
        jDesktopPane.add(cidade);
        cidade.setVisible(true);
        centralizaJanelaRegVen(cidade);
        //menuInvisivel();// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemCidadeActionPerformed

    private void mnuItemEmissaoLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemEmissaoLicencaActionPerformed
        EmissaoLicencaFrame emissao = new EmissaoLicencaFrame();
        jDesktopPane.add(emissao);
        emissao.setVisible(true);
        centralizaJanelaRegVen(emissao);
        //menuInvisivel();// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemEmissaoLicencaActionPerformed

    private void mnuItemAnalistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemAnalistaActionPerformed
        AnalistaFrame analista = new AnalistaFrame();
        jDesktopPane.add(analista);
        analista.setVisible(true);
        centralizaJanelaRegVen(analista);
        //menuInvisivel();// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemAnalistaActionPerformed

    private void mnuItemRelMovimentoProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRelMovimentoProcessoActionPerformed
        RelatorioMovimentoProcessoFrame movimento = new RelatorioMovimentoProcessoFrame();
        jDesktopPane.add(movimento);
        movimento.setVisible(true);
        centralizaJanela(movimento); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemRelMovimentoProcessoActionPerformed

    private void mnuItemDistribuicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemDistribuicaoActionPerformed
        DistribuicaoFrame distFrame = new DistribuicaoFrame();
        jDesktopPane.add(distFrame);
        distFrame.setVisible(true);
        centralizaJanelaRegVen(distFrame);
        // TODO add your handling code here:
}//GEN-LAST:event_mnuItemDistribuicaoActionPerformed

    private void mnuItemAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemAnaliseActionPerformed
        AnaliseFrame analiseFrame = new AnaliseFrame();
        jDesktopPane.add(analiseFrame);
        analiseFrame.setVisible(true);
        centralizaJanelaRegVen(analiseFrame); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemAnaliseActionPerformed

    private void mnuItemTrocaUsuarioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTrocaUsuarioActionPerformed
        Frame[] frames = Frame.getFrames();
        for (Frame frame : frames) {
            frame.setVisible(false);
        }

        LoginView tl = new LoginView();
        tl.setVisible(true);
        dispose();
}//GEN-LAST:event_mnuItemTrocaUsuarioActionPerformed

    private void mnuItemJuridicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemJuridicoActionPerformed
        JuridicoFrame juridicoFrame = new JuridicoFrame();
        jDesktopPane.add(juridicoFrame);
        juridicoFrame.setVisible(true);
        centralizaJanelaRegVen(juridicoFrame); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemJuridicoActionPerformed

    private void mnuItemGabineteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemGabineteActionPerformed
        GabineteFrame gabinete = new GabineteFrame();
        jDesktopPane.add(gabinete);
        gabinete.setVisible(true);
        centralizaJanelaRegVen(gabinete);// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemGabineteActionPerformed

    private void mmuProtocoloActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mmuProtocoloActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mmuProtocoloActionPerformed

    private void mnuAtividadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuAtividadeActionPerformed
        AtividadeFrame1 atividadeFrame = new AtividadeFrame1();
        jDesktopPane.add(atividadeFrame);
        atividadeFrame.setVisible(true);
        centralizaJanela(atividadeFrame);  // TODO add your handling code here:
    }//GEN-LAST:event_mnuAtividadeActionPerformed

    private void btnTramitacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTramitacaoActionPerformed
        TramitacaoFrame tramitacao = new TramitacaoFrame();
        jDesktopPane.add(tramitacao);
        tramitacao.setVisible(true);
        centralizaJanelaRegVen(tramitacao);// TODO add your handling code here:
}//GEN-LAST:event_btnTramitacaoActionPerformed

    private void btnGabineteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGabineteActionPerformed
        GabineteFrame gabinete = new GabineteFrame();
        jDesktopPane.add(gabinete);
        gabinete.setVisible(true);
        centralizaJanelaRegVen(gabinete);// TODO add your handling code here:
}//GEN-LAST:event_btnGabineteActionPerformed

    private void btnJuridicoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnJuridicoActionPerformed
        JuridicoFrame juridicoFrame = new JuridicoFrame();
        jDesktopPane.add(juridicoFrame);
        juridicoFrame.setVisible(true);
        centralizaJanelaRegVen(juridicoFrame);  // TODO add your handling code here:
}//GEN-LAST:event_btnJuridicoActionPerformed

    private void btnAnaliseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnaliseActionPerformed
        AnaliseFrame analiseFrame = new AnaliseFrame();
        jDesktopPane.add(analiseFrame);
        analiseFrame.setVisible(true);
        centralizaJanelaRegVen(analiseFrame); // TODO add your handling code here:
}//GEN-LAST:event_btnAnaliseActionPerformed

    private void btnEmissaoLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEmissaoLicencaActionPerformed
        EmissaoLicencaFrame emissao = new EmissaoLicencaFrame();
        jDesktopPane.add(emissao);
        emissao.setVisible(true);
        centralizaJanelaRegVen(emissao);// TODO add your handling code here:
}//GEN-LAST:event_btnEmissaoLicencaActionPerformed

    private void btnProcessoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProcessoActionPerformed
        ProcessoFrame processoFrame = new ProcessoFrame();
        jDesktopPane.add(processoFrame);
        processoFrame.setVisible(true);
        centralizaJanelaRegVen(processoFrame);  // TODO add your handling code here:
}//GEN-LAST:event_btnProcessoActionPerformed

    private void btnRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRequerenteActionPerformed
        RequerenteFrame requerenteFrame = new RequerenteFrame();
        jDesktopPane.add(requerenteFrame);
        requerenteFrame.setVisible(true);
        centralizaJanelaRegVen(requerenteFrame); // TODO add your handling code here:
}//GEN-LAST:event_btnRequerenteActionPerformed

    private void btnCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCidadeActionPerformed
        CidadeFrame cidade = new CidadeFrame();
        jDesktopPane.add(cidade);
        cidade.setVisible(true);
        centralizaJanelaRegVen(cidade);// TODO add your handling code here:
}//GEN-LAST:event_btnCidadeActionPerformed

    private void btnBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBairroActionPerformed
        BairroFrame bairroFrame = new BairroFrame();
        jDesktopPane.add(bairroFrame);
        bairroFrame.setVisible(true);
        centralizaJanela(bairroFrame);// TODO add your handling code here:
}//GEN-LAST:event_btnBairroActionPerformed

    private void btnLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogradouroActionPerformed
        LogradouroFrame ruaAv = new LogradouroFrame();
        jDesktopPane.add(ruaAv);
        ruaAv.setVisible(true);
        centralizaJanela(ruaAv);   // TODO add your handling code here:
}//GEN-LAST:event_btnLogradouroActionPerformed

    private void mnuTipoEventoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTipoEventoActionPerformed
        TipoEventoFrame tpEvento = new TipoEventoFrame();
        jDesktopPane.add(tpEvento);
        tpEvento.setVisible(true);
        centralizaJanela(tpEvento);// TODO add your handling code here:
    }//GEN-LAST:event_mnuTipoEventoActionPerformed

    private void mnuItemAutorizacaoEventosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemAutorizacaoEventosActionPerformed
        AutorizacaoEventosFrame tpEvento = new AutorizacaoEventosFrame();
        jDesktopPane.add(tpEvento);
        tpEvento.setVisible(true);
        centralizaJanelaRegVen(tpEvento);// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemAutorizacaoEventosActionPerformed

    private void btnDMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDMAActionPerformed
        DistribuicaoFrame distFrame = new DistribuicaoFrame();
        jDesktopPane.add(distFrame);
        distFrame.setVisible(true);
        centralizaJanelaRegVen(distFrame); // TODO add your handling code here:
}//GEN-LAST:event_btnDMAActionPerformed

    private void btnAutorizacaoEventosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutorizacaoEventosActionPerformed
        AutorizacaoEventosFrame tpEvento = new AutorizacaoEventosFrame();
        jDesktopPane.add(tpEvento);
        tpEvento.setVisible(true);
        centralizaJanelaRegVen(tpEvento);   // TODO add your handling code here:
    }//GEN-LAST:event_btnAutorizacaoEventosActionPerformed

    private void btnAtividadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtividadeActionPerformed
        AtividadeFrame1 atividadeFrame = new AtividadeFrame1();
        jDesktopPane.add(atividadeFrame);
        atividadeFrame.setVisible(true);
        centralizaJanela(atividadeFrame);   // TODO add your handling code here:
    }//GEN-LAST:event_btnAtividadeActionPerformed

    private void btnAnalistaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAnalistaActionPerformed
        AnalistaFrame analista = new AnalistaFrame();
        jDesktopPane.add(analista);
        analista.setVisible(true);
        centralizaJanelaRegVen(analista);  // TODO add your handling code here:
    }//GEN-LAST:event_btnAnalistaActionPerformed

    private void mnuLogradouro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouro1ActionPerformed
        PessoaFrame pessoaFrame = new PessoaFrame();
        jDesktopPane.add(pessoaFrame);
        pessoaFrame.setVisible(true);
        centralizaJanelaRegVen(pessoaFrame); // TODO add your handling code here:
    }//GEN-LAST:event_mnuLogradouro1ActionPerformed

    private void mmuProtocolo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mmuProtocolo1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mmuProtocolo1ActionPerformed

    private void mnuLogradouro2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouro2ActionPerformed
        NaturezaOcorrenciaFrame naturezaOcorrenciaFrame = new NaturezaOcorrenciaFrame();
        jDesktopPane.add(naturezaOcorrenciaFrame);
        naturezaOcorrenciaFrame.setVisible(true);
        centralizaJanelaRegVen(naturezaOcorrenciaFrame); // TODO add your handling code here:
    }//GEN-LAST:event_mnuLogradouro2ActionPerformed

    private void mnuLogradouro3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouro3ActionPerformed
        DenunciaFrame ocorrenciaFrame = new DenunciaFrame();
        jDesktopPane.add(ocorrenciaFrame);
        ocorrenciaFrame.setVisible(true);
        centralizaJanelaRegVen(ocorrenciaFrame);// TODO add your handling code here:
    }//GEN-LAST:event_mnuLogradouro3ActionPerformed

    private void mnuLogradouro4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouro4ActionPerformed
        ResumoTriagemFrame verificacaoTriagem = new ResumoTriagemFrame();
        jDesktopPane.add(verificacaoTriagem);
        verificacaoTriagem.setVisible(true);
        centralizaJanelaRegVen(verificacaoTriagem);// TODO add your handling code here:
    }//GEN-LAST:event_mnuLogradouro4ActionPerformed

    private void mnuLogradouro5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuLogradouro5ActionPerformed
        TriagemFrame verificacaoTriagem = new TriagemFrame();
        jDesktopPane.add(verificacaoTriagem);
        verificacaoTriagem.setVisible(true);
        centralizaJanelaRegVen(verificacaoTriagem);// TODO add your handling code here:
    }//GEN-LAST:event_mnuLogradouro5ActionPerformed

    private void mnuItemRelTriagemPoluicaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRelTriagemPoluicaoActionPerformed
        RelatorioTriagemPoluicaoFrame relatorioTriagemPoluicao = new RelatorioTriagemPoluicaoFrame();
        jDesktopPane.add(relatorioTriagemPoluicao);
        relatorioTriagemPoluicao.setVisible(true);
        centralizaJanelaRegVen(relatorioTriagemPoluicao);// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemRelTriagemPoluicaoActionPerformed

    private void mnuTipoEvento1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuTipoEvento1ActionPerformed
        LocalEventoFrame localEvento = new LocalEventoFrame();
        jDesktopPane.add(localEvento);
        localEvento.setVisible(true);
        centralizaJanelaRegVen(localEvento);// TODO add your handling code here:
    }//GEN-LAST:event_mnuTipoEvento1ActionPerformed

    private void mnuItemRelTriagemPoluicao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRelTriagemPoluicao1ActionPerformed
        RelProcAnalistaDistFrame RelProcAnalista = new RelProcAnalistaDistFrame();
        jDesktopPane.add(RelProcAnalista);
        RelProcAnalista.setVisible(true);
        centralizaJanelaRegVen(RelProcAnalista);// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemRelTriagemPoluicao1ActionPerformed

    private void mnuItemRelTriagemPoluicao2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemRelTriagemPoluicao2ActionPerformed
        AcessoBD acessoBd = new AcessoBD();
        try {
            HashMap parametros = new HashMap();
            //parametros.put("CAMINHO_IMAGEM", System.getProperty("user.dir") + "\\imagem\\DTI.jpg");
            JasperPrint jp = JasperFillManager.fillReport(System.getProperty("user.dir") + "\\relatorios\\ListagemProcessoNaoLancadoDma.jasper", parametros, acessoBd.conectar());
            JasperViewer.viewReport(jp, false);
        } catch (JRException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }          // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemRelTriagemPoluicao2ActionPerformed

    private void mnuItemTramitacao1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTramitacao1ActionPerformed
        TramitacaoMonitorFrame tramitacao = new TramitacaoMonitorFrame();
        jDesktopPane.add(tramitacao);
        tramitacao.setVisible(true);
        centralizaJanelaRegVen(tramitacao);  // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemTramitacao1ActionPerformed

    private void mnuItemJuridico1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemJuridico1ActionPerformed
        FiscalizacaoFrame fiscalizacaoFrame = new FiscalizacaoFrame();
        jDesktopPane.add(fiscalizacaoFrame);
        fiscalizacaoFrame.setVisible(true);
        centralizaJanelaRegVen(fiscalizacaoFrame);// TODO add your handling code here:
    }//GEN-LAST:event_mnuItemJuridico1ActionPerformed

    private void btnLoteDmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoteDmaActionPerformed
        LoteDmaFrame loteDmaFrame = new LoteDmaFrame();
        jDesktopPane.add(loteDmaFrame);
        loteDmaFrame.setVisible(true);
        centralizaJanelaRegVen(loteDmaFrame);// TODO add your handling code here:
    }//GEN-LAST:event_btnLoteDmaActionPerformed

    private void btnMovimentoDmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMovimentoDmaActionPerformed
        MovimentoDmaFrame movimentoDma = new MovimentoDmaFrame();
        jDesktopPane.add(movimentoDma);
        movimentoDma.setVisible(true);
        centralizaJanelaRegVen(movimentoDma);  // TODO add your handling code here:
    }//GEN-LAST:event_btnMovimentoDmaActionPerformed

    private void mnuItemLoteDmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemLoteDmaActionPerformed
        LoteDmaFrame loteDmaFrame = new LoteDmaFrame();
        jDesktopPane.add(loteDmaFrame);
        loteDmaFrame.setVisible(true);
        centralizaJanelaRegVen(loteDmaFrame); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemLoteDmaActionPerformed

    private void mnuItemMovimentoDmaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemMovimentoDmaActionPerformed
        MovimentoDmaFrame movimentoDma = new MovimentoDmaFrame();
        jDesktopPane.add(movimentoDma);
        movimentoDma.setVisible(true);
        centralizaJanelaRegVen(movimentoDma); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemMovimentoDmaActionPerformed

    private void mnuItemTipoLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemTipoLicencaActionPerformed
        TipoLicencaFrame tipoLicenca = new TipoLicencaFrame();
        jDesktopPane.add(tipoLicenca);
        tipoLicenca.setVisible(true);
        centralizaJanelaRegVen(tipoLicenca); // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemTipoLicencaActionPerformed

    private void btnTipoLicencaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTipoLicencaActionPerformed
        TipoLicencaFrame tipoLicenca = new TipoLicencaFrame();
        jDesktopPane.add(tipoLicenca);
        tipoLicenca.setVisible(true);
        centralizaJanelaRegVen(tipoLicenca);// TODO add your handling code here:
    }//GEN-LAST:event_btnTipoLicencaActionPerformed

    private void btnFiscalizacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiscalizacaoActionPerformed
        FiscalizacaoFrame fiscalizacaoFrame = new FiscalizacaoFrame();
        jDesktopPane.add(fiscalizacaoFrame);
        fiscalizacaoFrame.setVisible(true);
        centralizaJanelaRegVen(fiscalizacaoFrame);        // TODO add your handling code here:
    }//GEN-LAST:event_btnFiscalizacaoActionPerformed

    private void btnDenunciaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDenunciaActionPerformed
 DenunciaFrame ocorrenciaFrame = new DenunciaFrame();
        jDesktopPane.add(ocorrenciaFrame);
        ocorrenciaFrame.setVisible(true);
        centralizaJanelaRegVen(ocorrenciaFrame);        // TODO add your handling code here:
    }//GEN-LAST:event_btnDenunciaActionPerformed

    private void mnuItemGabinete1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuItemGabinete1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuItemGabinete1ActionPerformed

    private void mnuMovimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnuMovimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_mnuMovimentoActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(final String args[]) {

        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                
                try {
                    //UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                    UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
          
            
            // Select the Look and Feel
            //UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
                    id_Usuario = args[0];
                    new Menu().setVisible(true);

                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                } catch (InstantiationException ex) {
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,ex);
                } catch (IllegalAccessException ex) {
                    JOptionPane.showMessageDialog(null,ex);
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedLookAndFeelException ex) {
                    JOptionPane.showMessageDialog(null,ex);
                    Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
                }catch(Exception ex){
                 JOptionPane.showMessageDialog(null,ex);
                        }   
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAnalise;
    private javax.swing.JButton btnAnalista;
    private javax.swing.JButton btnAtividade;
    private javax.swing.JButton btnAutorizacaoEventos;
    private javax.swing.JButton btnBairro;
    private javax.swing.JButton btnCidade;
    private javax.swing.JButton btnDMA;
    private javax.swing.JButton btnDenuncia;
    private javax.swing.JButton btnEmissaoLicenca;
    private javax.swing.JButton btnFiscalizacao;
    private javax.swing.JButton btnGabinete;
    private javax.swing.JButton btnJuridico;
    private javax.swing.JButton btnLogradouro;
    private javax.swing.JButton btnLoteDma;
    private javax.swing.JButton btnMovimentoDma;
    private javax.swing.JButton btnProcesso;
    private javax.swing.JButton btnRequerente;
    private javax.swing.JButton btnTipoLicenca;
    private javax.swing.JButton btnTramitacao;
    private javax.persistence.EntityManager entityManager;
    private javax.swing.JDesktopPane jDesktopPane;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    public javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenu jMenuDMA;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private java.util.List list;
    private javax.swing.JMenu mmuProtocolo;
    private javax.swing.JMenu mmuProtocolo1;
    private static javax.swing.JMenu mnuAjuda;
    private javax.swing.JMenuItem mnuAtividade;
    private javax.swing.JMenuItem mnuConteudo;
    private javax.swing.JMenu mnuDenuncia;
    private javax.swing.JMenu mnuDenuncia1;
    private javax.swing.JMenu mnuGraficos;
    private javax.swing.JMenuItem mnuItemAnalise;
    private javax.swing.JMenuItem mnuItemAnalista;
    private javax.swing.JMenuItem mnuItemAutorizacaoEventos;
    private javax.swing.JMenuItem mnuItemBairro;
    private javax.swing.JMenuItem mnuItemCidade;
    private javax.swing.JMenuItem mnuItemDistribuicao;
    private javax.swing.JMenuItem mnuItemEmissaoLicenca;
    private javax.swing.JMenuItem mnuItemGabinete;
    private javax.swing.JMenuItem mnuItemGabinete1;
    private javax.swing.JMenuItem mnuItemJuridico;
    private javax.swing.JMenuItem mnuItemJuridico1;
    private javax.swing.JMenuItem mnuItemLoteDma;
    private javax.swing.JMenuItem mnuItemMovimentoDma;
    private javax.swing.JMenuItem mnuItemProcesso;
    private javax.swing.JMenuItem mnuItemRelMovimentoProcesso;
    private javax.swing.JMenuItem mnuItemRelTriagemPoluicao;
    private javax.swing.JMenuItem mnuItemRelTriagemPoluicao1;
    private javax.swing.JMenuItem mnuItemRelTriagemPoluicao2;
    private javax.swing.JMenuItem mnuItemRequerente;
    private javax.swing.JMenuItem mnuItemSair;
    private javax.swing.JMenuItem mnuItemTipoLicenca;
    private javax.swing.JMenuItem mnuItemTramitacao;
    private javax.swing.JMenuItem mnuItemTramitacao1;
    private javax.swing.JMenuItem mnuItemTrocaUsuario;
    private javax.swing.JMenuItem mnuItemUsuario;
    private javax.swing.JMenu mnuListagem;
    private javax.swing.JMenu mnuListagem2;
    private javax.swing.JMenu mnuListagem3;
    private javax.swing.JMenu mnuListagem4;
    private javax.swing.JMenuItem mnuLogradouro;
    private javax.swing.JMenuItem mnuLogradouro1;
    private javax.swing.JMenuItem mnuLogradouro2;
    private javax.swing.JMenuItem mnuLogradouro3;
    private javax.swing.JMenuItem mnuLogradouro4;
    private javax.swing.JMenuItem mnuLogradouro5;
    private static javax.swing.JMenu mnuMovimento;
    private javax.swing.JMenuItem mnuNivelAcesso;
    private static javax.swing.JMenu mnuProtocolo;
    private static javax.swing.JMenu mnuRelatorios;
    private static javax.swing.JMenu mnuSair;
    public static javax.swing.JMenu mnuSistema;
    private javax.swing.JMenuItem mnuSobre1;
    private javax.swing.JMenuItem mnuTipoEvento;
    private javax.swing.JMenuItem mnuTipoEvento1;
    private javax.swing.JMenu mnuTramitacao;
    private static javax.swing.JMenu mnuUtilitarios;
    private javax.persistence.Query query;
    // End of variables declaration//GEN-END:variables

    private void controleModulo() {

        if (verificaAcesso("Usuario", id_Usuario)) {
            mnuItemUsuario.setVisible(true);
        } else {
            mnuItemUsuario.setVisible(false);
        }
        //inicio protocolo
        if (verificaAcesso("Logradouro", id_Usuario)) {
            mnuLogradouro.setVisible(true);
            btnLogradouro.setVisible(true);
        } else {
            mnuLogradouro.setVisible(false);
            btnLogradouro.setVisible(false);
        }

        if (verificaAcesso("Bairro", id_Usuario)) {
            mnuItemBairro.setVisible(true);
            btnBairro.setVisible(true);
        } else {
            mnuItemBairro.setVisible(false);
            btnBairro.setVisible(false);
        }

        if (verificaAcesso("Cidade", id_Usuario)) {
            mnuItemCidade.setVisible(true);
            btnCidade.setVisible(true);
        } else {
            mnuItemCidade.setVisible(false);
            btnCidade.setVisible(false);
        }

        if (verificaAcesso("Atividade", id_Usuario)) {
            mnuAtividade.setVisible(true);
            btnAtividade.setVisible(true);
        } else {
            mnuAtividade.setVisible(false);
            btnAtividade.setVisible(false);
        }

        if (verificaAcesso("Requerente", id_Usuario)) {
            mnuItemRequerente.setVisible(true);
            btnRequerente.setVisible(true);
        } else {
            mnuItemRequerente.setVisible(false);
            btnRequerente.setVisible(false);
        }

        if (verificaAcesso("Analista", id_Usuario)) {
            mnuItemAnalista.setVisible(true);
            btnAnalista.setVisible(true);
        } else {
            mnuItemAnalista.setVisible(false);
            btnAnalista.setVisible(false);
        }if (verificaAcesso("Tipo Licença", id_Usuario)) {
            mnuItemTipoLicenca.setVisible(true);
            btnTipoLicenca.setVisible(true);
        } else {
            mnuItemTipoLicenca.setVisible(false);
            btnTipoLicenca.setVisible(false);
        }//fim do protocolo

        //inicio do movimento
        if (verificaAcesso("Processo", id_Usuario)) {
            mnuItemProcesso.setVisible(true);
            btnProcesso.setVisible(true);
        } else {
            mnuItemProcesso.setVisible(false);
            btnProcesso.setVisible(false);
        }
        //SUB MENU DMA

        if (verificaAcesso("Distribuicao DMA", id_Usuario)) {
            mnuItemDistribuicao.setVisible(true);
            btnDMA.setVisible(true);
        } else {
            mnuItemDistribuicao.setVisible(false);
            btnDMA.setVisible(false);
        }

        if (verificaAcesso("Emissão Licença", id_Usuario)) {
            mnuItemEmissaoLicenca.setVisible(true);
            btnEmissaoLicenca.setVisible(true);
        } else {
            mnuItemEmissaoLicenca.setVisible(false);
            btnEmissaoLicenca.setVisible(false);
        }
        if (verificaAcesso("Tipo Evento", id_Usuario)) {
            mnuTipoEvento.setVisible(true);
        } else {
            mnuTipoEvento.setVisible(false);
        }
        if (verificaAcesso("Autorização Eventos", id_Usuario)) {
            mnuItemAutorizacaoEventos.setVisible(true);
            btnAutorizacaoEventos.setVisible(true);
        } else {
            mnuItemAutorizacaoEventos.setVisible(false);
            btnAutorizacaoEventos.setVisible(false);
        } if (verificaAcesso("Lote Dma", id_Usuario)) {
            mnuItemLoteDma.setVisible(true);
            btnLoteDma.setVisible(true);
        } else {
            mnuItemLoteDma.setVisible(false);
            btnLoteDma.setVisible(false);
        }
        
        if (verificaAcesso("Movimento Dma", id_Usuario)) {
            mnuItemMovimentoDma.setVisible(true);
            btnMovimentoDma.setVisible(true);
        } else {
            mnuItemMovimentoDma.setVisible(false);
            btnMovimentoDma.setVisible(false);
        }//FIM SUB MENU DMA

        if (verificaAcesso("Análise", id_Usuario)) {
            mnuItemAnalise.setVisible(true);
            btnAnalise.setVisible(true);
        } else {
            mnuItemAnalise.setVisible(false);
            btnAnalise.setVisible(false);
        }

        if (verificaAcesso("Jurídico", id_Usuario)) {
            mnuItemJuridico.setVisible(true);
            btnJuridico.setVisible(true);
        } else {
            mnuItemJuridico.setVisible(false);
            btnJuridico.setVisible(false);
        }

        if (verificaAcesso("Gabinete", id_Usuario)) {
            mnuItemGabinete.setVisible(true);
            btnGabinete.setVisible(true);
        } else {
            mnuItemGabinete.setVisible(false);
            btnGabinete.setVisible(false);
        }

        if (verificaAcesso("Tramitação", id_Usuario)) {
            mnuItemTramitacao.setVisible(true);
            btnTramitacao.setVisible(true);
        } else {
            mnuItemTramitacao.setVisible(false);
            btnTramitacao.setVisible(false);
        }
        //RELATORIOS
        if (verificaAcesso("Movimento Processo", id_Usuario)) {
            mnuItemRelMovimentoProcesso.setVisible(true);

        } else {
            mnuItemRelMovimentoProcesso.setVisible(false);

        }
        
        
       

    }

    /**
     * @return the monitor
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param monitor the monitor to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        jLabel1.setText(usuario.getLogin());
        jLabel2.setText("Usuario:");
    }

    public int getModo() {
        return modo;
    }

    public void setModo(int modo) {
        this.modo = modo;
    }
}
