/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.pessoa;

import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.bairro.Bairro;
import sip.bairro.BairroBD;
import sip.bairro.BairroFrame;
import sip.cidade.Cidade;
import sip.cidade.CidadeBD;
import sip.cidade.CidadeFrame;
import sip.denuncia.DenunciaFrame;
import sip.processo.ProcessoFrame;
import sip.util.Constantes;
import sip.logradouro.Logradouro;
import sip.logradouro.LogradouroBD;
import sip.logradouro.LogradouroFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author T2Ti
 */
public class PessoaFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Pessoa> listPessoa;
    private List<Pessoa> listPessoaRep;
    private Pessoa Pessoa;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoSeleciona;
    private PessoaFrame PessoaFrame;
    private Bairro bairro;
    private Logradouro logradouro;
    private List<Bairro> listBairro;
    private List<Logradouro> listLogradouro;
    private Cidade cidade;
    private List<Cidade> listCidade;
    private DenunciaFrame ocorrenciaFrame;
    
    public PessoaFrame() {
        initComponents();
        defineModelo();
        caixaAlta();
        //caixaBaixa();
        fechando();
    }
    
    public PessoaFrame(DenunciaFrame ocorrenciaFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        fechando();
        this.ocorrenciaFrame = ocorrenciaFrame;
        txtNome.requestFocus();
        modoSeleciona = Constantes.PESSOA_FRAME;
        //caixaBaixa();
        
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
        tableModel = (DefaultTableModel) tblPessoa.getModel();
        listModel = tblPessoa.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalhePessoa();
                }
            }
        });
        try {



            DecimalFormat formatoNumeral = new DecimalFormat("####");
            NumberFormatter formatterNumeral = new NumberFormatter(formatoNumeral);
            formatterNumeral.setValueClass(Integer.class);
            //ftfNumero.setFormatterFactory(new DefaultFormatterFactory(formatterNumeral));
            //(99)3524-3231
            MaskFormatter mascaraTel = new MaskFormatter("##.###-###");
            mascaraTel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterTel = new DefaultFormatterFactory(mascaraTel);
            //ftfCep.setFormatterFactory(formatterTel);
            //(99)99142-0419
            MaskFormatter mascaraCel = new MaskFormatter("(##)#####-####");
            mascaraCel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterCel = new DefaultFormatterFactory(mascaraCel);
            ftfCel.setFormatterFactory(formatterCel);



            tblPessoa.getColumnModel().getColumn(1).setPreferredWidth(400);
            tblPessoa.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblPessoa.getColumnModel().getColumn(3).setPreferredWidth(300);
        } catch (ParseException ex) {
            Logger.getLogger(PessoaFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        PessoaBD PessoaBD = new PessoaBD();

        if (txtFiltroPessoa.getText().equals("")) {
            listPessoa = PessoaBD.consultaPessoa();
        } else {
            String valor = txtFiltroPessoa.getText();
            listPessoa = PessoaBD.consultaPessoaNome(valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listPessoa.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                        listPessoa.get(i).getId(),
                        listPessoa.get(i).getNome(),
                    });
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

  

    private void mostraDetalhePessoa() {
        if (tblPessoa.getSelectedRow() != -1) {
            int indice = tblPessoa.getSelectedRow();
            //txtProfissao.setText(listPessoa.get(indice).getChapa());

            txtNome.setText(listPessoa.get(indice).getNome());
            txtIdLogradouro.setText(listPessoa.get(indice).getLogradouro().getId().toString());
            txtNomeLogradouro.setText(listPessoa.get(indice).getLogradouro().getNome());
            txtNumero.setText(listPessoa.get(indice).getNumero());
            txtIdBairro.setText(listPessoa.get(indice).getBairro().getId().toString());
            txtNomeBairro.setText(listPessoa.get(indice).getBairro().getNome());
            txtIdCidade.setText(listPessoa.get(indice).getCidade().getId().toString());
            txtNomeCidade.setText(listPessoa.get(indice).getCidade().getNome());
            ftfTel.setText(listPessoa.get(indice).getTelFixo());
            ftfCel.setText(listPessoa.get(indice).getTelCel());
          
            txtComplemento.setText(listPessoa.get(indice).getComplemento());
          


           } else {
            /*
            txtNomeRazao.setText("");
            cbNacionalidade.setSelectedItem("SELECIONE");
            cbEstadoCivil.setSelectedItem("SELECIONE");
            txtProfissao.setText("");
            txtRg.setText("");
            txtOrgao.setText("");
            ftfCpfCnpj.setText("");
            txtIdLogradouro.setText("");
            txtNumero.setText("");
            txtNomeRazao.setText("");
            txtIdBairro.setText("");
            txtNomeBairro.setText("");
            txtIdCidade.setText("");
            txtNomeCidade.setText("");
            ftfTel.setText("");
            ftfCel.setText("");
            txtConjugue.setText("");
            cbRegime.setSelectedItem(" ");
            cbTipoRepresentado.setSelectedItem(" ");
            txtRepresentadoNome.setText("");
             */
        }
    }

    

    private void incluiPessoa() {
      

        if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
    
        }  else {

            Pessoa pessoa = new Pessoa();


            pessoa.setNome(txtNome.getText().trim());
            pessoa.setLogradouro(getLogradouro());
            pessoa.setNumero(txtNumero.getText().trim());
            pessoa.setTelFixo(ftfTel.getText());
            pessoa.setTelCel(ftfCel.getText());        
            pessoa.setBairro(getBairro());
            pessoa.setComplemento(txtComplemento.getText());
            pessoa.setCidade(getCidade());


            PessoaBD PessoaBD = new PessoaBD();
            if (PessoaBD.incluiPessoa(pessoa)) {
                JOptionPane.showMessageDialog(this, "Pessoa cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();

            } else {
                JOptionPane.showMessageDialog(this, "Pessoa já cadastrado!\n já existem", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraPessoa() {
       if (txtNome.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNome.requestFocus();
           
        }  else {

            Pessoa pessoa = new Pessoa();
             
            
           
            pessoa.setId(listPessoa.get(tblPessoa.getSelectedRow()).getId());
            pessoa.setNome(txtNome.getText().trim());
            pessoa.setLogradouro(getLogradouro());
            pessoa.setNumero(txtNumero.getText().trim());
            pessoa.setTelFixo(ftfTel.getText());
            pessoa.setTelCel(ftfCel.getText());        
            pessoa.setBairro(getBairro());
            pessoa.setComplemento(txtComplemento.getText());
            pessoa.setCidade(getCidade());
           


            PessoaBD PessoaBD = new PessoaBD();
            if (PessoaBD.alteraPessoa(pessoa)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                tblPessoa.setEnabled(true);
                 txtFiltroPessoa.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
                tblPessoa.setEnabled(true);
                txtFiltroPessoa.setEnabled(true);
            }
        }
    }

    /*
    private void alteraPessoa() {
    configuraRegime();
    habilitaRepresentado();
    configuraSemRepresentado();
    if (txtNomeRazao.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtNomeRazao.requestFocus();
    removeItem();
    } else if (txtProfissao.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Profissão\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtProfissao.requestFocus();
    removeItem();
    } else if (txtRg.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Rg\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtRg.requestFocus();
    removeItem();
    } else if (ftfCpfCnpj.getText().equals("   .   .   -  ")) {
    JOptionPane.showMessageDialog(this, "O campo Cpf\ndeve ser preenchido!", "Cpf", JOptionPane.INFORMATION_MESSAGE);
    ftfCpfCnpj.requestFocus();
    removeItem();
    } else if (getLogradouro() == null) {
    JOptionPane.showMessageDialog(this, "O campo Logradouro\ndeve ser preenchido!", "Logradouro", JOptionPane.INFORMATION_MESSAGE);
    txtIdLogradouro.requestFocus();
    removeItem();
    } else if (txtNumero.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Numero\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtNumero.requestFocus();
    removeItem();
    } else if (getBairro() == null) {
    JOptionPane.showMessageDialog(this, "O campo Bairro\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtIdBairro.requestFocus();
    removeItem();
    } else if (getCidade() == null) {
    JOptionPane.showMessageDialog(this, "O campo Cidade\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    txtIdCidade.requestFocus();
    removeItem();
    } else if (cbNacionalidade.getSelectedItem().equals("SELECIONE")) {
    JOptionPane.showMessageDialog(this, "Informe a Nacionalidade!", "Campos", JOptionPane.INFORMATION_MESSAGE);
    cbNacionalidade.requestFocus();
    removeItem();
    } else if (cbEstadoCivil.getSelectedItem().equals("SELECIONE")) {
    JOptionPane.showMessageDialog(this, "Selecione o Estado Civil!", "Estado Civil", JOptionPane.INFORMATION_MESSAGE);
    cbEstadoCivil.requestFocus();
    removeItem();
    } else if ((ftfCel.getText().equals("(  )     -    "))) {
    JOptionPane.showMessageDialog(this, "O campo Telefone\ndeve ser preenchido!", "Telefone", JOptionPane.INFORMATION_MESSAGE);
    ftfTel.requestFocus();
    removeItem();
    } else if (txtConjugue.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Conjugue\ndeve ser preenchido!", "Conjugue", JOptionPane.INFORMATION_MESSAGE);
    } else if (txtRepresentadoNome.getText().trim().equals("")) {
    JOptionPane.showMessageDialog(this, "O campo Representado\ndeve ser preenchido!", "Representado", JOptionPane.INFORMATION_MESSAGE);
    removeItem();
    } else if (cbTipoRepresentado.getSelectedItem().equals("SELECIONE")) {
    JOptionPane.showMessageDialog(this, "Selecione se o Pessoa é Maior ou Menor!", "Tipo", JOptionPane.INFORMATION_MESSAGE);
    cbTipoRepresentado.requestFocus();
    removeItem();
    } else if (cbRegime.getSelectedItem().equals(" ")) {
    JOptionPane.showMessageDialog(this, "Selecione o Regime!", "Regime", JOptionPane.INFORMATION_MESSAGE);
    cbRegime.requestFocus();
    removeItem();
    } else {
    Pessoa Pessoa = new Pessoa();
    Pessoa.setId(listPessoa.get(tblPessoa.getSelectedRow()).getId());
    Pessoa.setNome(txtNomeRazao.getText().trim());
    Pessoa.setNacionalidade(cbNacionalidade.getSelectedItem().toString());
    Pessoa.setEstadoCivil(cbEstadoCivil.getSelectedItem().toString());
    Pessoa.setProfissao(txtProfissao.getText().trim());
    Pessoa.setRg(txtRg.getText().trim());
    Pessoa.setOrgao(txtOrgao.getText().trim());
    Pessoa.setCpf(ftfCpfCnpj.getText());
    Pessoa.setLogradouro(getLogradouro());
    Pessoa.setNumero(txtNumero.getText().trim());
    Pessoa.setBairro(getBairro());
    Pessoa.setCidade(getCidade());
    Pessoa.setTelFixo(ftfTel.getText());
    Pessoa.setTelCel(ftfCel.getText());
    if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
    Pessoa.setNubente(txtConjugue.getText().trim());
    Pessoa.setRegime(cbRegime.getSelectedItem().toString());
    } else {
    Pessoa.setNubente("***");
    Pessoa.setRegime("");
    }
    Pessoa.setRepresentado(representado);
    //caso do representado
    if (jCBRepresentado.isSelected()) {
    Pessoa.setRepresentadoNome(txtRepresentadoNome.getText().trim());
    Pessoa.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
    } else {
    Pessoa.setRepresentadoNome(txtRepresentadoNome.getText().trim());
    Pessoa.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
    }
    
    
    PessoaBD PessoaBD = new PessoaBD();
    if (PessoaBD.alteraPessoa(Pessoa)) {
    JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
    removeItem();
    atualizaTabela();
    desabilitaBotoes();
    desabilitaCampos();
    
    } else {
    JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
    removeItem();
    }
    }
    }
     */
    private void excluiPessoa() {

        PessoaBD monitorBD = new PessoaBD();
        if (monitorBD.excluiPessoa(listPessoa.get(tblPessoa.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Pessoa excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esse Pessoa!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtNome.addKeyListener(new KeyListener() {

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

        txtNumero.addKeyListener(new KeyListener() {

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
        
        txtComplemento.addKeyListener(new KeyListener() {

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

    private void caixaBaixa() {
        txtNome.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(final KeyEvent e) {
                SwingUtilities.invokeLater(new Runnable() {

                    @Override
                    public void run() {
                        JTextField campo = (JTextField) e.getSource();
                        int posicaoCursor = campo.getCaretPosition();
                        campo.setText(campo.getText().toLowerCase());
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

    private void habilitaCampos() {
        txtNome.setEditable(true);
        txtIdBairro.setEditable(true);
        txtIdLogradouro.setEditable(true);
        txtIdCidade.setEditable(true);
        txtNumero.setEditable(true);
        ftfTel.setEditable(true);
        ftfCel.setEditable(true);
        txtComplemento.setEditable(true);
        
    }

    private void desabilitaCampos() {
       
        txtNome.setEditable(false);
        txtIdBairro.setEditable(false);
        txtIdLogradouro.setEditable(false);
        txtIdCidade.setEditable(false);    
        txtNumero.setEditable(false);
        ftfTel.setEditable(false);
        ftfCel.setEditable(false);
        txtComplemento.setEditable(false);
    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        btnSelecionaBairro.setEnabled(false);
        btnSelecionaRua.setEnabled(false);
        btnSelecionaCidade.setEnabled(false);
        btnSelecionaRua.setEnabled(false);
        btnSelecionaCidade.setEnabled(false);
  }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        btnSelecionaBairro.setEnabled(true);
        btnSelecionaRua.setEnabled(true);
        btnSelecionaCidade.setEnabled(true);

    }
    
    private void limpaCampos() {
        {
            
            txtNome.setText("");
            txtNumero.setText("");
            
            
            ftfTel.setText("");
            ftfCel.setText("");

            logradouro = null;
            txtIdLogradouro.setText("");
            txtNomeLogradouro.setText("");
            txtComplemento.setText("");

            bairro = null;
            txtIdBairro.setText("");
            txtNomeBairro.setText("");

            Pessoa = null;
            
           
            cidade = null;
            txtIdCidade.setText("");
            txtNomeCidade.setText("");

            
        }
    }



    private void selecionaPessoaComunicante() {
        if (tblPessoa.getSelectedRow() != -1) {
            ocorrenciaFrame.setComunicante(listPessoa.get(tblPessoa.getSelectedRow()));
            this.dispose();
            ocorrenciaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Pessoa da lista!", "Pessoa", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void selecionaPessoaDenunciado() {
        if (tblPessoa.getSelectedRow() != -1) {
            ocorrenciaFrame.setDenunciado(listPessoa.get(tblPessoa.getSelectedRow()));
            this.dispose();
            ocorrenciaFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Pessoa da lista!", "Pessoa", JOptionPane.INFORMATION_MESSAGE);
        }
    }


    private void centralizaJanela(JInternalFrame internalFrame) {
        internalFrame.setLocation((this.getWidth() - internalFrame.getWidth()) / 2,
                (this.getHeight() - internalFrame.getHeight()) / 2);
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
                bairro = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            bairro = null;
            txtNomeBairro.setText("");
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
                logradouro = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            logradouro = null;
            txtNomeLogradouro.setText("");
            //JOptionPane.showMessageDialog(this, "Digite o ID","ID",JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void buscaCidade() {
        CidadeBD cidadeBD = new CidadeBD();
        listCidade = cidadeBD.consultaCidade();
        int binario = 0;
        try {
            int max = listCidade.size();
            int id_busca = Integer.parseInt(txtIdCidade.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listCidade.get(i).getId();
                if (listCidade.get(i).getId() == id_busca) {
                    setCidade(listCidade.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdCidade.setText("");
                txtNomeCidade.setText("");
                cidade = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            cidade = null;
            txtNomeCidade.setText("");
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        txtNomeBairro = new javax.swing.JTextField();
        btnSelecionaBairro = new javax.swing.JButton();
        txtIdBairro = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        txtNomeLogradouro = new javax.swing.JTextField();
        btnSelecionaRua = new javax.swing.JButton();
        txtIdLogradouro = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txtNumero = new javax.swing.JTextField();
        txtComplemento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNomeCidade = new javax.swing.JTextField();
        btnSelecionaCidade = new javax.swing.JButton();
        txtIdCidade = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        ftfTel = new javax.swing.JFormattedTextField();
        ftfCel = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        ftfEspaçamento = new javax.swing.JFormattedTextField();
        jPanel8 = new javax.swing.JPanel();
        jLNome = new javax.swing.JLabel();
        txtNome = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroPessoa = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblPessoa = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        btnNovo = new javax.swing.JButton();
        btnAlterar = new javax.swing.JButton();
        btnExcluir = new javax.swing.JButton();
        btnSalvar = new javax.swing.JButton();
        btnCancelar = new javax.swing.JButton();

        setClosable(true);
        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Cadastro Requerente");
        setPreferredSize(new java.awt.Dimension(900, 670));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(new java.awt.Color(122, 149, 222));
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/PESSOA_64x64.png"))); // NOI18N
        jLabel1.setText("PESSOA - Comunicante/Denunciado ");
        jPanel1.add(jLabel1);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel1, gridBagConstraints);

        jPanel2.setLayout(new java.awt.GridBagLayout());

        jPanel16.setBorder(javax.swing.BorderFactory.createTitledBorder("Bairro"));
        jPanel16.setLayout(new java.awt.GridBagLayout());

        jLabel14.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel16.add(jLabel14, gridBagConstraints);

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
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 400;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel16, gridBagConstraints);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Logradouro"));
        jPanel17.setLayout(new java.awt.GridBagLayout());

        jLabel15.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel15, gridBagConstraints);

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

        jLabel12.setText("Numero");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 7;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(jLabel12, gridBagConstraints);

        txtNumero.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel17.add(txtNumero, gridBagConstraints);

        txtComplemento.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 6;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 0, 5, 5);
        jPanel17.add(txtComplemento, gridBagConstraints);

        jLabel7.setText("Complemento");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel17.add(jLabel7, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel17, gridBagConstraints);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Cidade"));
        jPanel19.setLayout(new java.awt.GridBagLayout());

        jLabel17.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLabel17, gridBagConstraints);

        txtNomeCidade.setEditable(false);
        txtNomeCidade.setFont(new java.awt.Font("Verdana", 1, 12)); // NOI18N
        txtNomeCidade.setEnabled(false);
        txtNomeCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtNomeCidadeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(txtNomeCidade, gridBagConstraints);

        btnSelecionaCidade.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaCidade.setEnabled(false);
        btnSelecionaCidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaCidadeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(btnSelecionaCidade, gridBagConstraints);

        txtIdCidade.setEditable(false);
        txtIdCidade.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdCidadeFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel19.add(txtIdCidade, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel13.setText("Telefone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.EAST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel13, gridBagConstraints);

        ftfTel.setEditable(false);
        ftfTel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        ftfTel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfTelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfTel, gridBagConstraints);

        ftfCel.setEditable(false);
        ftfCel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfCelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfCel, gridBagConstraints);

        jLabel9.setText("Celular");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel9, gridBagConstraints);

        ftfEspaçamento.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        ftfEspaçamento.setEditable(false);
        ftfEspaçamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfEspaçamentoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.BASELINE;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfEspaçamento, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel7, gridBagConstraints);

        jPanel8.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel8.setLayout(new java.awt.GridBagLayout());

        jLNome.setText("Nome");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLNome, gridBagConstraints);

        txtNome.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(txtNome, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel8, gridBagConstraints);

        jTabbedPane1.addTab("Basico", jPanel2);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        getContentPane().add(jTabbedPane1, gridBagConstraints);

        jPanel3.setLayout(new java.awt.GridBagLayout());

        jLabel2.setText("Filtro Nome - Cpf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(jLabel2, gridBagConstraints);

        txtFiltroPessoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroPessoaMouseClicked(evt);
            }
        });
        txtFiltroPessoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroPessoaActionPerformed(evt);
            }
        });
        txtFiltroPessoa.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroPessoaFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroPessoa, gridBagConstraints);

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

        tblPessoa.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        tblPessoa.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nome", "Rg", "Cpf"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblPessoa.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblPessoa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblPessoaMouseClicked(evt);
            }
        });
        tblPessoa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblPessoaKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblPessoa);
        if (tblPessoa.getColumnModel().getColumnCount() > 0) {
            tblPessoa.getColumnModel().getColumn(0).setResizable(false);
            tblPessoa.getColumnModel().getColumn(1).setResizable(false);
            tblPessoa.getColumnModel().getColumn(2).setResizable(false);
        }

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
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
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
        gridBagConstraints.weightx = 1.0;
        getContentPane().add(jPanel4, gridBagConstraints);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnFiltrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFiltrarActionPerformed
        atualizaTabela();
        txtFiltroPessoa.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        PessoaBD cobradorBD = new PessoaBD();
        if (cobradorBD.testaConexao()) {

           
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblPessoa.setEnabled(false);
            limpaSelecaoTabela();
            txtNome.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            caixaAlta();
            limpaCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiPessoa();
            tblPessoa.setEnabled(true);
            // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraPessoa();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblPessoa.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblPessoa.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaLogradouro();
            buscaBairro();
            buscaCidade();
            tblPessoa.setEnabled(false);
            txtFiltroPessoa.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Pessoa da lista!", "Pessoa", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblPessoa.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Pessoa?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiPessoa();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Pessoa da lista!", "Pessoa", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void txtFiltroPessoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroPessoaMouseClicked
    }//GEN-LAST:event_txtFiltroPessoaMouseClicked

    private void txtFiltroPessoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroPessoaActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroPessoaActionPerformed

    private void txtFiltroPessoaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroPessoaFocusLost
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroPessoaFocusLost

    private void tblPessoaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblPessoaMouseClicked
        if (evt.getClickCount() == 2) {
             if (modoSeleciona == Constantes.PESSOA_FRAME) {
                 if ("comunicante".equals(ocorrenciaFrame.getControleEscolhaPessoa())) {
                    selecionaPessoaComunicante();
                }else if ("denunciado".equals(ocorrenciaFrame.getControleEscolhaPessoa())) {
                    selecionaPessoaDenunciado();
                }
                dispose();
            }
             evt.consume();
        }
    }//GEN-LAST:event_tblPessoaMouseClicked

    private void tblPessoaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblPessoaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             if (modoSeleciona == Constantes.PESSOA_FRAME) {
                 if ("comunicante".equals(ocorrenciaFrame.getControleEscolhaPessoa())) {
                    selecionaPessoaComunicante();
                }else if ("denunciado".equals(ocorrenciaFrame.getControleEscolhaPessoa())) {
                    selecionaPessoaDenunciado();
                }
                dispose();
            }
             evt.consume();
        }
    }//GEN-LAST:event_tblPessoaKeyPressed

    private void ftfCelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfCelActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfCelActionPerformed

    private void ftfTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfTelActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfTelActionPerformed

    private void txtIdLogradouroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdLogradouroFocusLost
        buscaLogradouro();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdLogradouroFocusLost

    private void btnSelecionaRuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaRuaActionPerformed
        LogradouroFrame ruaFrame = new LogradouroFrame(this);
        ruaFrame.setVisible(true);
        this.getDesktopPane().add(ruaFrame);
        ruaFrame.toFront();
        centralizaJanela(ruaFrame);  // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaRuaActionPerformed

    private void txtNomeLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLogradouroActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeLogradouroActionPerformed

    private void txtIdBairroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdBairroFocusLost
        buscaBairro();        // TODO add your handling code here:
}//GEN-LAST:event_txtIdBairroFocusLost

    private void btnSelecionaBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaBairroActionPerformed
        BairroFrame BairroFrame = new BairroFrame(this);
        BairroFrame.setVisible(true);
        this.getDesktopPane().add(BairroFrame);
        BairroFrame.toFront();
        centralizaJanela(BairroFrame);
}//GEN-LAST:event_btnSelecionaBairroActionPerformed

    private void txtNomeBairroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeBairroActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeBairroActionPerformed

    private void txtIdCidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdCidadeFocusLost
        buscaCidade();
}//GEN-LAST:event_txtIdCidadeFocusLost

    private void btnSelecionaCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCidadeActionPerformed
        CidadeFrame cidadeFrame = new CidadeFrame(this);
        cidadeFrame.setVisible(true);
        this.getDesktopPane().add(cidadeFrame);
        cidadeFrame.toFront();
        centralizaJanela(cidadeFrame); // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaCidadeActionPerformed

    private void txtNomeCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeCidadeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeCidadeActionPerformed

    private void ftfEspaçamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfEspaçamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfEspaçamentoActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaBairro;
    private javax.swing.JButton btnSelecionaCidade;
    private javax.swing.JButton btnSelecionaRua;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JFormattedTextField ftfCel;
    private javax.swing.JFormattedTextField ftfEspaçamento;
    private javax.swing.JFormattedTextField ftfTel;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTable tblPessoa;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JTextField txtFiltroPessoa;
    private javax.swing.JTextField txtIdBairro;
    private javax.swing.JTextField txtIdCidade;
    private javax.swing.JTextField txtIdLogradouro;
    private javax.swing.JTextField txtNome;
    private javax.swing.JTextField txtNomeBairro;
    private javax.swing.JTextField txtNomeCidade;
    private javax.swing.JTextField txtNomeLogradouro;
    private javax.swing.JTextField txtNumero;
    // End of variables declaration//GEN-END:variables

    public Bairro getBairro() {
        return bairro;
    }

    public void setBairro(Bairro Bairro) {
        this.bairro = Bairro;
        txtNomeBairro.setText(Bairro.getNome());
        txtIdBairro.setText(Bairro.getId().toString());
    }

    /**
     * @return the ruaAv
     */
    public Logradouro getLogradouro() {
        return logradouro;
    }

    /**
     * @param ruaAv the ruaAv to set
     */
    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
        txtNomeLogradouro.setText(logradouro.getNome());
        txtIdLogradouro.setText(logradouro.getId().toString());

    }

    /**
     * @return the cidade
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
        txtNomeCidade.setText(cidade.getNome());
        txtIdCidade.setText(cidade.getId().toString());

    }

    /**
     * @return the Pessoa
     */
    public Pessoa getPessoa() {
        return Pessoa;
    }

    /**
     * @param Pessoa the Pessoa to set
     */
    public void setPessoa(Pessoa Pessoa) {
        this.Pessoa = Pessoa;
    }
}
