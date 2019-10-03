/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ClienteFrame.java
 *
 * Created on 20/02/2011, 11:05:14
 */
package sip.requerente;

import sip.menulogin.Menu;
import sip.acessobd.AcessoBD;
import sip.bairro.Bairro;
import sip.bairro.BairroBD;
import sip.bairro.BairroFrame;
import sip.cidade.Cidade;
import sip.cidade.CidadeBD;
import sip.cidade.CidadeFrame;
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
public class RequerenteFrame extends javax.swing.JInternalFrame {

    AcessoBD conClientes;
    private DefaultTableModel tableModel;
    private ListSelectionModel listModel;
    private List<Requerente> listRequerente;
    private List<Requerente> listRequerenteRep;
    private Requerente requerente;
    //private List<ClienteOrigem> clientes;
    private int modo;
    private int modoProcesso;
    private ProcessoFrame processoFrame;
    private RequerenteFrame requerenteFrame;
    private Bairro bairro;
    private Logradouro logradouro;
    private List<Bairro> listBairro;
    private List<Logradouro> listLogradouro;
    private Cidade cidade;
    private List<Cidade> listCidade;
    private String representado;
    private String tipoPessoa;

    public RequerenteFrame() {
        initComponents();
        defineModelo();
        caixaAlta();
        //caixaBaixa();
        fechando();
        configuraEstadoCivilCasado();
        configuraTipoPessoa();

    }

  
    public RequerenteFrame(ProcessoFrame processoFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        this.processoFrame = processoFrame;
        configuraEstadoCivilCasado();
        configuraTipoPessoa();
        //jLabel9.setVisible(false);
        //ftfTel.setVisible(false);
        modoProcesso = Constantes.PROCESSO;
    }

    public RequerenteFrame(RequerenteFrame requerenteFrame) {
        initComponents();
        defineModelo();
        caixaAlta();
        jTabbedPane1.setVisible(false);
        jPanel4.setVisible(false);
        this.requerenteFrame = requerenteFrame;
        configuraEstadoCivilCasado();
        configuraTipoPessoa();
        //jLabel9.setVisible(false);
        //ftfTel.setVisible(false);
        modoProcesso = Constantes.RUAFRENTE;
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

    private void defineModeloCnpjCpf() {
        try {
            if (jrbPessoaFisica.isSelected()) {
                MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
                mascaraCpf.setValueContainsLiteralCharacters(false);
                DefaultFormatterFactory formatterCpf = new DefaultFormatterFactory(mascaraCpf);
                ftfCpfCnpj.setFormatterFactory(formatterCpf);
            } else {
                MaskFormatter mascaraCnpj = new MaskFormatter("##.###.###/####-##");
                mascaraCnpj.setValueContainsLiteralCharacters(false);
                DefaultFormatterFactory formatterCnpj = new DefaultFormatterFactory(mascaraCnpj);
                ftfCpfCnpj.setFormatterFactory(formatterCnpj);
            }

        } catch (ParseException ex) {
            Logger.getLogger(RequerenteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void defineModelo() {
        tableModel = (DefaultTableModel) tblRequerente.getModel();
        listModel = tblRequerente.getSelectionModel();
        listModel.addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    mostraDetalheRequerente();
                }
            }
        });
        try {
            if (jrbPessoaFisica.isSelected()) {
                MaskFormatter mascaraCpf = new MaskFormatter("###.###.###-##");
                mascaraCpf.setValueContainsLiteralCharacters(false);
                DefaultFormatterFactory formatterCpf = new DefaultFormatterFactory(mascaraCpf);
                ftfCpfCnpj.setFormatterFactory(formatterCpf);
            } else {
                MaskFormatter mascaraCnpj = new MaskFormatter("##.###.###/####-##");
                mascaraCnpj.setValueContainsLiteralCharacters(false);
                DefaultFormatterFactory formatterCnpj = new DefaultFormatterFactory(mascaraCnpj);
                ftfCpfCnpj.setFormatterFactory(formatterCnpj);
            }


            DecimalFormat formatoNumeral = new DecimalFormat("####");
            NumberFormatter formatterNumeral = new NumberFormatter(formatoNumeral);
            formatterNumeral.setValueClass(Integer.class);
            //ftfNumero.setFormatterFactory(new DefaultFormatterFactory(formatterNumeral));
            //(99)3524-3231
            MaskFormatter mascaraTel = new MaskFormatter("##.###-###");
            mascaraTel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterTel = new DefaultFormatterFactory(mascaraTel);
            ftfCep.setFormatterFactory(formatterTel);
            //(99)99142-0419
            MaskFormatter mascaraCel = new MaskFormatter("(##)#####-####");
            mascaraCel.setValueContainsLiteralCharacters(false);
            DefaultFormatterFactory formatterCel = new DefaultFormatterFactory(mascaraCel);
            ftfCel.setFormatterFactory(formatterCel);



            tblRequerente.getColumnModel().getColumn(1).setPreferredWidth(400);
            tblRequerente.getColumnModel().getColumn(2).setPreferredWidth(300);
            tblRequerente.getColumnModel().getColumn(3).setPreferredWidth(300);
        } catch (ParseException ex) {
            Logger.getLogger(RequerenteFrame.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void atualizaTabela() {
        RequerenteBD RequerenteBD = new RequerenteBD();

        if (txtFiltroRequerente.getText().equals("")) {
            listRequerente = RequerenteBD.consultaRequerente();
        } else {
            String valor = txtFiltroRequerente.getText();
            listRequerente = RequerenteBD.consultaRequerente(valor, valor);
        }
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
        for (int i = 0; i < listRequerente.size(); i++) {
            tableModel.insertRow(i, new Object[]{
                        listRequerente.get(i).getId(),
                        listRequerente.get(i).getNome(),
                        listRequerente.get(i).getRg(),
                        listRequerente.get(i).getCpf()
                    });
        }
    }

    private void limpaSelecaoTabela() {
        int numeroLinhas = tableModel.getRowCount();
        for (int i = 0; i < numeroLinhas; i++) {
            tableModel.removeRow(0);
        }
    }

    private void removeItem() {
        cbRegime.removeItem("***");
        cbTipoRepresentado.removeItem("***");
    }

    private void habilitaRepresentado() {
        if (jCBRepresentado.isSelected()) {
            representado = "Sim";
            if (jrbPessoaFisica.isSelected()) {
                cbTipoRepresentado.setEnabled(true);
                txtRepresentadoNome.setEditable(true);
            } else {
                txtIdRequerente.setEnabled(true);
                btnSelecionaRequerente.setEnabled(true);
            }

        } else {
            representado = "Não";
            cbTipoRepresentado.setEnabled(false);
            txtRepresentadoNome.setEditable(false);
            txtIdRequerente.setEnabled(false);
            btnSelecionaRequerente.setEnabled(false);

        }

    }

    private void configuraSemRepresentado() {
        if (jCBRepresentado.isSelected()) {
        } else {
            if (jrbPessoaFisica.isSelected()) {
                txtRepresentadoNome.setText("***");
                cbTipoRepresentado.addItem("***");
                cbTipoRepresentado.setSelectedItem("***");
            } else {
                requerente = null;
                txtIdRequerente.setText("");
                txtNomeRequerente.setText("");
            }



        }

    }

    private void habilitaNubenteRegime() {
        jLConjugue.setEnabled(true);
        txtConjugue.setEnabled(true);
        jLRegime.setEnabled(true);
        cbRegime.setEnabled(true);

    }

    private void desabilitaNubenteRegime() {
        jLConjugue.setEnabled(false);
        txtConjugue.setEnabled(false);
        jLRegime.setEnabled(false);
        cbRegime.setEnabled(false);
    }

    private void configuraEstadoCivilCasado() {
        if(jrbPessoaFisica.isSelected()){
            if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
            habilitaNubenteRegime();
            txtConjugue.setText("");
        } else {
            desabilitaNubenteRegime();
            txtConjugue.setText("***");
            cbRegime.setSelectedItem(" ");
        }
        }
        
    }

    private void configuraTipoPessoa() {
        if (jrbPessoaFisica.isSelected()) {
            tipoPessoa = "fisica";
            jLNome.setVisible(true);
            jPanel11.setVisible(true);
            jPanel20.setVisible(false);
            jLRazaoSocial.setVisible(false);
            jLCnpj.setVisible(false);
            jLCpf.setVisible(true);
            cbEstadoCivil.setEnabled(true);
            cbNacionalidade.setEnabled(true);
            txtProfissao.setEnabled(true);
            txtRg.setEnabled(true);
            txtOrgao.setEnabled(true);
        } else {
            tipoPessoa = "juridica";
            jLCpf.setVisible(false);
            jLCnpj.setVisible(true);
            jLRazaoSocial.setVisible(true);
            jPanel20.setVisible(true);
            jPanel11.setVisible(false);
            jLNome.setVisible(false);
            cbEstadoCivil.setEnabled(false);
            cbNacionalidade.setEnabled(false);
            jLRazaoSocial.setEnabled(true);
            txtProfissao.setEnabled(false);
            txtRg.setEnabled(false);
            txtOrgao.setEnabled(false);
        }

    }

    private void configuraRegime() {
        if(jrbPessoaFisica.isSelected()){
             if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
                } else {
                    cbRegime.addItem("***");
                    cbRegime.setSelectedItem("***");
            }
        }
       
    }

    private void mostraDetalheRequerente() {
        if (tblRequerente.getSelectedRow() != -1) {
            int indice = tblRequerente.getSelectedRow();
            //txtProfissao.setText(listRequerente.get(indice).getChapa());

            txtNomeRazao.setText(listRequerente.get(indice).getNome());
            cbNacionalidade.setSelectedItem(listRequerente.get(indice).getNacionalidade());
            cbEstadoCivil.setSelectedItem(listRequerente.get(indice).getEstadoCivil());
            txtProfissao.setText(listRequerente.get(indice).getProfissao());
            txtRg.setText(listRequerente.get(indice).getRg());
            txtOrgao.setText(listRequerente.get(indice).getOrgao());

            txtIdLogradouro.setText(listRequerente.get(indice).getLogradouro().getId().toString());
            txtNomeLogradouro.setText(listRequerente.get(indice).getLogradouro().getNome());
            txtNumero.setText(listRequerente.get(indice).getNumero());
            txtIdBairro.setText(listRequerente.get(indice).getBairro().getId().toString());
            txtNomeBairro.setText(listRequerente.get(indice).getBairro().getNome());
            txtIdCidade.setText(listRequerente.get(indice).getCidade().getId().toString());
            txtNomeCidade.setText(listRequerente.get(indice).getCidade().getNome());
            ftfTel.setText(listRequerente.get(indice).getTelFixo());
            ftfCel.setText(listRequerente.get(indice).getTelCel());
            txtConjugue.setText(listRequerente.get(indice).getNubente());
            cbRegime.setSelectedItem(listRequerente.get(indice).getRegime());
            txtComplemento.setText(listRequerente.get(indice).getComplemento());
            ftfCep.setText(listRequerente.get(indice).getCep());


            if ((listRequerente.get(indice).getTipoPessoa()).equals("fisica")) {
                jrbPessoaFisica.setSelected(true);
                configuraTipoPessoa();
                defineModeloCnpjCpf();
                //defineModelo(); 
            } else if (listRequerente.get(indice).getTipoPessoa().equals("juridica")) {
                jrbPessoaJuridica.setSelected(true);
                configuraTipoPessoa();
                defineModeloCnpjCpf();
                //defineModelo(); 
            }
            ftfCpfCnpj.setText(listRequerente.get(indice).getCpf());
            if(listRequerente.get(indice).getRepresentadoTipo() == null){
                cbTipoRepresentado.setSelectedItem("SELECIONE");
            }else{
                cbTipoRepresentado.setSelectedItem(listRequerente.get(indice).getRepresentadoTipo());
             }
                
            
            txtRepresentadoNome.setText(listRequerente.get(indice).getRepresentadoNome());

            txtIdRequerente.setFocusable(false);
            txtIdRequerente.setFocusable(true);


            if (listRequerente.get(indice).getRepresentado() == null) {
                jCBRepresentado.setSelected(false);
            } else if (listRequerente.get(indice).getRepresentado().equals("Sim")) {
                jCBRepresentado.setSelected(true);

            } else {
                jCBRepresentado.setSelected(false);
            }


            desabilitaNubenteRegime();
            if (listRequerente.get(indice).getRequerente().getId() != 0) {
                txtIdRequerente.setText(listRequerente.get(indice).getRequerente().getId().toString());
                txtNomeRequerente.setText(listRequerente.get(indice).getRequerente().getNome());
            } else {
                txtIdRequerente.setText("");
                txtNomeRequerente.setText("");
            }

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

    private void limpaCampos() {
        {
            txtProfissao.setText("");
            txtNomeRazao.setText("");
            txtNumero.setText("");
            txtProfissao.setText("");
            txtRg.setText("");
            txtOrgao.setText("");
            ftfCpfCnpj.setText("");
            ftfTel.setText("");
            ftfCel.setText("");

            logradouro = null;
            txtIdLogradouro.setText("");
            txtNomeLogradouro.setText("");

            bairro = null;
            txtIdBairro.setText("");
            txtNomeBairro.setText("");

            requerente = null;
            txtIdRequerente.setText("");
            txtNomeRequerente.setText("");

            //nacionalidade = null;
            cbNacionalidade.setSelectedItem("SELECIONE");
            cbEstadoCivil.setSelectedItem("SELECIONE");
            cidade = null;
            txtIdCidade.setText("");
            txtNomeCidade.setText("");

            cbTipoRepresentado.setSelectedItem("SELECIONE");
            txtRepresentadoNome.setText("");
            jCBRepresentado.setSelected(false);

        }
    }

    private void incluiRequerente() {
        configuraRegime();
        habilitaRepresentado();
        configuraSemRepresentado();

        if (txtNomeRazao.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNomeRazao.requestFocus();
            removeItem();
        } else if (ftfCpfCnpj.getText().equals("   .   .   -  ") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Cpf\ndeve ser preenchido!", "Cpf", JOptionPane.INFORMATION_MESSAGE);
            ftfCpfCnpj.requestFocus();
            removeItem();
        } else if (txtProfissao.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Profissão\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtProfissao.requestFocus();
            removeItem();
        } else if (txtNomeRazao.getText().trim().equals("") && jrbPessoaJuridica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Razão Social\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNomeRazao.requestFocus();
        } else if (ftfCpfCnpj.getText().equals("   .   .   -  ") && jrbPessoaJuridica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Cnpj\ndeve ser preenchido!", "Cnpj", JOptionPane.INFORMATION_MESSAGE);
            ftfCpfCnpj.requestFocus();

        } else if (requerente == null && jrbPessoaJuridica.isSelected() && jCBRepresentado.isSelected()) {
            JOptionPane.showMessageDialog(this, "Informe o requerente que representa a empresa\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdRequerente.requestFocus();
        } else if (txtRg.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Rg\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtRg.requestFocus();
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
        }else if (jrbPessoaFisica.isSelected() && cbNacionalidade.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe a Nacionalidade!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            cbNacionalidade.requestFocus();
            removeItem();
        } else if (jrbPessoaFisica.isSelected() && cbEstadoCivil.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Selecione o Estado Civil!", "Estado Civil", JOptionPane.INFORMATION_MESSAGE);
            cbEstadoCivil.requestFocus();
            removeItem();
        } else if ((ftfCel.getText().equals("(  )     -    "))) {
            JOptionPane.showMessageDialog(this, "O campo Telefone\ndeve ser preenchido!", "Telefone", JOptionPane.INFORMATION_MESSAGE);
            ftfTel.requestFocus();
            removeItem();
        } else if (txtConjugue.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Conjugue\ndeve ser preenchido!", "Conjugue", JOptionPane.INFORMATION_MESSAGE);
        } else if (txtRepresentadoNome.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Representado\ndeve ser preenchido!", "Representado", JOptionPane.INFORMATION_MESSAGE);
            removeItem();
        } else if (cbTipoRepresentado.getSelectedItem().equals("SELECIONE") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "Selecione se o Requerente é Maior ou Menor!", "Tipo", JOptionPane.INFORMATION_MESSAGE);
            cbTipoRepresentado.requestFocus();
            removeItem();
        } else if (jrbPessoaFisica.isSelected() && cbRegime.getSelectedItem().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Selecione o Regime!", "Regime", JOptionPane.INFORMATION_MESSAGE);
            cbRegime.requestFocus();
            removeItem();
        }else if ((txtComplemento.getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "O campo Complemento\ndeve ser preenchido!", "Complemento", JOptionPane.INFORMATION_MESSAGE);
            txtComplemento.requestFocus();
            removeItem();
        } else if ((ftfCep.getText().equals("  .   -   "))) {
            JOptionPane.showMessageDialog(this, "O campo Cep\ndeve ser preenchido!", "Cep", JOptionPane.INFORMATION_MESSAGE);
            ftfCep.requestFocus();
            removeItem();
        }  else {

            Requerente Requerente = new Requerente();


            Requerente.setNome(txtNomeRazao.getText().trim());
            if(jrbPessoaFisica.isSelected()){
                Requerente.setNacionalidade(cbNacionalidade.getSelectedItem().toString());
                Requerente.setEstadoCivil(cbEstadoCivil.getSelectedItem().toString());
                Requerente.setProfissao(txtProfissao.getText().trim());
                Requerente.setRg(txtRg.getText().trim());
                Requerente.setOrgao(txtOrgao.getText().trim());
            }else{
                Requerente.setNacionalidade(null);
                Requerente.setEstadoCivil(null);
                Requerente.setProfissao(null);
                Requerente.setRg(null);
                Requerente.setOrgao(null);
            }
                
            
            Requerente.setCpf(ftfCpfCnpj.getText());
            Requerente.setLogradouro(getLogradouro());
            Requerente.setNumero(txtNumero.getText().trim());
            Requerente.setBairro(getBairro());
            Requerente.setCidade(getCidade());
            Requerente.setTelFixo(ftfTel.getText());
            Requerente.setTelCel(ftfCel.getText());
            //caso do estado civil
            if(jrbPessoaFisica.isSelected()){
                if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
                Requerente.setNubente(txtConjugue.getText().trim());
                Requerente.setRegime(cbRegime.getSelectedItem().toString());
            } else {
                Requerente.setNubente(null);
                Requerente.setRegime(null);
            }
            }else{
                Requerente.setNubente(null);
                Requerente.setRegime(null);
            }
            Requerente.setRepresentado(representado);
            //caso do representado
            if (jCBRepresentado.isSelected() && jrbPessoaFisica.isSelected()) {
                Requerente.setRepresentadoNome(txtRepresentadoNome.getText().trim());
                Requerente.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
            } else {
                Requerente.setRepresentadoNome(null);
                Requerente.setRepresentadoTipo(null);
            }
            Requerente.setTipoPessoa(tipoPessoa);
            if (jCBRepresentado.isSelected() && jrbPessoaJuridica.isSelected()) {
                Requerente.setRequerente(getRequerente());
            } else {
                Requerente.setRequerente(null);
            }
             Requerente.setComplemento(txtComplemento.getText());
             Requerente.setCep(ftfCep.getText());


            RequerenteBD RequerenteBD = new RequerenteBD();
            if (RequerenteBD.incluiRequerente(Requerente)) {
                JOptionPane.showMessageDialog(this, "Requerente cadastrado com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                removeItem();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();

            } else {

                removeItem();
                JOptionPane.showMessageDialog(this, "Requerente já cadastrado!\nChapa ou Cpf já existem", "Erro", JOptionPane.ERROR_MESSAGE);

            }
        }
    }

    private void alteraRequerente() {
        configuraRegime();
        habilitaRepresentado();
        configuraSemRepresentado();

        if (txtNomeRazao.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Nome\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNomeRazao.requestFocus();
            removeItem();
        } else if (ftfCpfCnpj.getText().equals("   .   .   -  ") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Cpf\ndeve ser preenchido!", "Cpf", JOptionPane.INFORMATION_MESSAGE);
            ftfCpfCnpj.requestFocus();
            removeItem();
        } else if (txtProfissao.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Profissão\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtProfissao.requestFocus();
            removeItem();
        } else if (txtNomeRazao.getText().trim().equals("") && jrbPessoaJuridica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Razão Social\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtNomeRazao.requestFocus();
        } else if (ftfCpfCnpj.getText().equals("   .   .   -  ") && jrbPessoaJuridica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Cnpj\ndeve ser preenchido!", "Cnpj", JOptionPane.INFORMATION_MESSAGE);
            ftfCpfCnpj.requestFocus();

        } else if (requerente == null && jrbPessoaJuridica.isSelected() && jCBRepresentado.isSelected()) {
            JOptionPane.showMessageDialog(this, "Informe o requerente que representa a empresa\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtIdRequerente.requestFocus();
        } else if (txtRg.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Rg\ndeve ser preenchido!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            txtRg.requestFocus();
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
        } else if (jrbPessoaFisica.isSelected() && cbNacionalidade.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Informe a Nacionalidade!", "Campos", JOptionPane.INFORMATION_MESSAGE);
            cbNacionalidade.requestFocus();
            removeItem();
        } else if (jrbPessoaFisica.isSelected() && cbEstadoCivil.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Selecione o Estado Civil!", "Estado Civil", JOptionPane.INFORMATION_MESSAGE);
            cbEstadoCivil.requestFocus();
            removeItem();
        } else if ((ftfCel.getText().equals("(  )     -    "))) {
            JOptionPane.showMessageDialog(this, "O campo Telefone\ndeve ser preenchido!", "Telefone", JOptionPane.INFORMATION_MESSAGE);
            ftfTel.requestFocus();
            removeItem();
        } else if (txtConjugue.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Conjugue\ndeve ser preenchido!", "Conjugue", JOptionPane.INFORMATION_MESSAGE);
        } else if (txtRepresentadoNome.getText().trim().equals("") && jrbPessoaFisica.isSelected()) {
            JOptionPane.showMessageDialog(this, "O campo Representado\ndeve ser preenchido!", "Representado", JOptionPane.INFORMATION_MESSAGE);
            removeItem();
        } else if (jrbPessoaFisica.isSelected() && cbTipoRepresentado.getSelectedItem().equals("SELECIONE")) {
            JOptionPane.showMessageDialog(this, "Selecione se o Requerente é Maior ou Menor!", "Tipo", JOptionPane.INFORMATION_MESSAGE);
            cbTipoRepresentado.requestFocus();
            removeItem();
        } else if (jrbPessoaFisica.isSelected() && cbRegime.getSelectedItem().equals(" ")) {
            JOptionPane.showMessageDialog(this, "Selecione o Regime!", "Regime", JOptionPane.INFORMATION_MESSAGE);
            cbRegime.requestFocus();
            removeItem();
        }else if ((txtComplemento.getText().equals(""))) {
            JOptionPane.showMessageDialog(this, "O campo Complemento\ndeve ser preenchido!", "Complemento", JOptionPane.INFORMATION_MESSAGE);
            txtComplemento.requestFocus();
            removeItem();
        } else if ((ftfCep.getText().equals("  .   -   "))) {
            JOptionPane.showMessageDialog(this, "O campo Cep\ndeve ser preenchido!", "Cep", JOptionPane.INFORMATION_MESSAGE);
            ftfCep.requestFocus();
            removeItem();
        }  else {

            Requerente Requerente = new Requerente();
             
            
           
            Requerente.setId(listRequerente.get(tblRequerente.getSelectedRow()).getId());
            Requerente.setNome(txtNomeRazao.getText().trim());
            if(jrbPessoaFisica.isSelected()){
                Requerente.setNacionalidade(cbNacionalidade.getSelectedItem().toString());
                Requerente.setEstadoCivil(cbEstadoCivil.getSelectedItem().toString());
                Requerente.setProfissao(txtProfissao.getText().trim());
                Requerente.setRg(txtRg.getText().trim());
                Requerente.setOrgao(txtOrgao.getText().trim());
            }else{
                Requerente.setNacionalidade(null);
                Requerente.setEstadoCivil(null);
                Requerente.setProfissao(null);
                Requerente.setRg(null);
                Requerente.setOrgao(null);
            }           
            Requerente.setCpf(ftfCpfCnpj.getText());
            Requerente.setLogradouro(getLogradouro());
            Requerente.setNumero(txtNumero.getText().trim());
            Requerente.setBairro(getBairro());
            Requerente.setCidade(getCidade());
            Requerente.setTelFixo(ftfTel.getText());
            Requerente.setTelCel(ftfCel.getText());
            //caso do estado civil
            if(jrbPessoaFisica.isSelected()){
                if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
                Requerente.setNubente(txtConjugue.getText().trim());
                Requerente.setRegime(cbRegime.getSelectedItem().toString());
            } else {
                Requerente.setNubente(null);
                Requerente.setRegime(null);
            }
            }else{
                Requerente.setNubente(null);
                Requerente.setRegime(null);
            }
            
            Requerente.setRepresentado(representado);
            //caso do representado
            if (jCBRepresentado.isSelected() && jrbPessoaFisica.isSelected()) {
                Requerente.setRepresentadoNome(txtRepresentadoNome.getText().trim());
                Requerente.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
            } else {
                Requerente.setRepresentadoNome(null);
                Requerente.setRepresentadoTipo(null);
            }
            Requerente.setTipoPessoa(tipoPessoa);
            if (jCBRepresentado.isSelected() && jrbPessoaJuridica.isSelected()) {
                Requerente.setRequerente(getRequerente());
            } else {
                Requerente.setRequerente(null);
            }
             Requerente.setComplemento(txtComplemento.getText());
             Requerente.setCep(ftfCep.getText());


            RequerenteBD RequerenteBD = new RequerenteBD();
            if (RequerenteBD.alteraRequerente(Requerente)) {
                JOptionPane.showMessageDialog(this, "Dados alterados com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
                removeItem();
                atualizaTabela();
                desabilitaBotoes();
                desabilitaCampos();
                tblRequerente.setEnabled(true);
                 txtFiltroRequerente.setEnabled(true);

            } else {
                JOptionPane.showMessageDialog(this, "Erro ao alterar os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
                removeItem();
                tblRequerente.setEnabled(true);
                txtFiltroRequerente.setEnabled(true);
            }
        }
    }

    /*
    private void alteraRequerente() {
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
    JOptionPane.showMessageDialog(this, "Selecione se o Requerente é Maior ou Menor!", "Tipo", JOptionPane.INFORMATION_MESSAGE);
    cbTipoRepresentado.requestFocus();
    removeItem();
    } else if (cbRegime.getSelectedItem().equals(" ")) {
    JOptionPane.showMessageDialog(this, "Selecione o Regime!", "Regime", JOptionPane.INFORMATION_MESSAGE);
    cbRegime.requestFocus();
    removeItem();
    } else {
    Requerente Requerente = new Requerente();
    Requerente.setId(listRequerente.get(tblRequerente.getSelectedRow()).getId());
    Requerente.setNome(txtNomeRazao.getText().trim());
    Requerente.setNacionalidade(cbNacionalidade.getSelectedItem().toString());
    Requerente.setEstadoCivil(cbEstadoCivil.getSelectedItem().toString());
    Requerente.setProfissao(txtProfissao.getText().trim());
    Requerente.setRg(txtRg.getText().trim());
    Requerente.setOrgao(txtOrgao.getText().trim());
    Requerente.setCpf(ftfCpfCnpj.getText());
    Requerente.setLogradouro(getLogradouro());
    Requerente.setNumero(txtNumero.getText().trim());
    Requerente.setBairro(getBairro());
    Requerente.setCidade(getCidade());
    Requerente.setTelFixo(ftfTel.getText());
    Requerente.setTelCel(ftfCel.getText());
    if (cbEstadoCivil.getSelectedItem().equals("CASADO") || cbEstadoCivil.getSelectedItem().equals("CASADA")) {
    Requerente.setNubente(txtConjugue.getText().trim());
    Requerente.setRegime(cbRegime.getSelectedItem().toString());
    } else {
    Requerente.setNubente("***");
    Requerente.setRegime("");
    }
    Requerente.setRepresentado(representado);
    //caso do representado
    if (jCBRepresentado.isSelected()) {
    Requerente.setRepresentadoNome(txtRepresentadoNome.getText().trim());
    Requerente.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
    } else {
    Requerente.setRepresentadoNome(txtRepresentadoNome.getText().trim());
    Requerente.setRepresentadoTipo(cbTipoRepresentado.getSelectedItem().toString());
    }
    
    
    RequerenteBD RequerenteBD = new RequerenteBD();
    if (RequerenteBD.alteraRequerente(Requerente)) {
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
    private void excluiRequerente() {

        RequerenteBD monitorBD = new RequerenteBD();
        if (monitorBD.excluiRequerente(listRequerente.get(tblRequerente.getSelectedRow()))) {
            JOptionPane.showMessageDialog(this, "Dados do Requerente excluídos com sucesso!", "Confirmação", JOptionPane.INFORMATION_MESSAGE);
            atualizaTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao excluir os dados!", "Erro", JOptionPane.ERROR_MESSAGE);
            JOptionPane.showMessageDialog(this, "Possivelmente existe tabela para esse Requerente!", "Informação", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void caixaAlta() {
        txtNomeRazao.addKeyListener(new KeyListener() {

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
        txtRepresentadoNome.addKeyListener(new KeyListener() {

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
        txtOrgao.addKeyListener(new KeyListener() {

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
        txtConjugue.addKeyListener(new KeyListener() {

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
        txtProfissao.addKeyListener(new KeyListener() {

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
        txtRg.addKeyListener(new KeyListener() {

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
        txtProfissao.addKeyListener(new KeyListener() {
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
        jCBRepresentado.setEnabled(true);
        txtProfissao.setEditable(true);
        txtNomeRazao.setEditable(true);
        txtIdBairro.setEditable(true);
        txtIdLogradouro.setEditable(true);
        txtIdCidade.setEditable(true);
        cbNacionalidade.setEnabled(true);
        cbEstadoCivil.setEnabled(true);
        txtRg.setEditable(true);
        txtOrgao.setEditable(true);
        txtNumero.setEditable(true);
        ftfCpfCnpj.setEditable(true);
        ftfTel.setEditable(true);
        ftfCel.setEditable(true);
        txtComplemento.setEditable(true);
        ftfCep.setEditable(true);       
    }

    private void desabilitaCampos() {
        txtIdRequerente.setEnabled(false);
        jCBRepresentado.setEnabled(false);
        txtProfissao.setEditable(false);
        txtNomeRazao.setEditable(false);
        txtIdBairro.setEditable(false);
        txtIdLogradouro.setEditable(false);
        txtIdCidade.setEditable(false);
        cbNacionalidade.setEnabled(false);
        cbEstadoCivil.setEnabled(false);
        txtRg.setEditable(false);
        txtOrgao.setEditable(false);
        txtNumero.setEditable(false);
        ftfCpfCnpj.setEditable(false);
        ftfTel.setEditable(false);
        ftfCel.setEditable(false);
        txtComplemento.setEditable(true);
        ftfCep.setEditable(true);       
        cbTipoRepresentado.setEnabled(false);
        txtRepresentadoNome.setEditable(false);

    }

    private void desabilitaBotoes() {
        btnSalvar.setEnabled(false);
        btnCancelar.setEnabled(false);
        btnNovo.setEnabled(true);
        btnAlterar.setEnabled(true);
        btnExcluir.setEnabled(true);
        btnSelecionaBairro.setEnabled(false);
        cbNacionalidade.setEditable(false);
        btnSelecionaRua.setEnabled(false);
        btnSelecionaCidade.setEnabled(false);
        btnSelecionaRua.setEnabled(false);
        btnSelecionaCidade.setEnabled(false);
        jrbPessoaFisica.setEnabled(true);
        jrbPessoaJuridica.setEnabled(true);


    }

    private void habilitaBotoes() {
        btnSalvar.setEnabled(true);
        btnCancelar.setEnabled(true);
        btnNovo.setEnabled(false);
        btnAlterar.setEnabled(false);
        btnExcluir.setEnabled(false);
        jrbPessoaFisica.setEnabled(false);
        jrbPessoaJuridica.setEnabled(false);
        btnSelecionaBairro.setEnabled(true);
        btnSelecionaRua.setEnabled(true);
        btnSelecionaCidade.setEnabled(true);

    }



    private void selecionaRequerenteProcesso() {
        if (tblRequerente.getSelectedRow() != -1) {
            processoFrame.setRequerente(listRequerente.get(tblRequerente.getSelectedRow()));
            this.dispose();
            processoFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Requerente da lista!", "Requerente", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private void selecionaRequerenteRequerente() {
        if (tblRequerente.getSelectedRow() != -1) {
            requerenteFrame.setRequerente(listRequerente.get(tblRequerente.getSelectedRow()));
            this.dispose();
            requerenteFrame.toFront();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Requerente da lista!", "Requerente", JOptionPane.INFORMATION_MESSAGE);
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

    private void buscaRequerente() {
        RequerenteBD requerenteBD = new RequerenteBD();
        listRequerenteRep = requerenteBD.consultaRequerente();
        int binario = 0;
        try {
            int max = listRequerenteRep.size();
            int id_busca = Integer.parseInt(txtIdRequerente.getText());
            //Percorre a lista  
            for (int i = 0; i < max; i++) {
                listRequerenteRep.get(i).getId();
                if (listRequerenteRep.get(i).getId() == id_busca) {
                    setRequerente(listRequerenteRep.get(i));
                    binario = 1;
                }
            }
            if (binario == 0) {
                JOptionPane.showMessageDialog(this, "ID não existe", "ID", JOptionPane.ERROR_MESSAGE);
                txtIdRequerente.setText("");
                txtNomeRequerente.setText("");
                requerente = null;
            }
        } catch (Exception e) {
            System.out.println("ERRO: " + e);
            e.printStackTrace();
            requerente = null;
            txtNomeRequerente.setText("");
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
        jPanel5 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtRg = new javax.swing.JTextField();
        txtOrgao = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        ftfCpfCnpj = new javax.swing.JFormattedTextField();
        jLCnpj = new javax.swing.JLabel();
        jLCpf = new javax.swing.JLabel();
        jPanel19 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        txtNomeCidade = new javax.swing.JTextField();
        btnSelecionaCidade = new javax.swing.JButton();
        txtIdCidade = new javax.swing.JTextField();
        ftfCep = new javax.swing.JFormattedTextField();
        jLCnpj1 = new javax.swing.JLabel();
        jPNacionalidade = new javax.swing.JPanel();
        cbNacionalidade = new javax.swing.JComboBox();
        jPEstadoCivil = new javax.swing.JPanel();
        cbEstadoCivil = new javax.swing.JComboBox();
        txtConjugue = new javax.swing.JTextField();
        jLConjugue = new javax.swing.JLabel();
        jLRegime = new javax.swing.JLabel();
        cbRegime = new javax.swing.JComboBox();
        jPanel7 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        txtProfissao = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        ftfTel = new javax.swing.JFormattedTextField();
        ftfCel = new javax.swing.JFormattedTextField();
        jLabel9 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jLNome = new javax.swing.JLabel();
        jLRazaoSocial = new javax.swing.JLabel();
        txtNomeRazao = new javax.swing.JTextField();
        jPanel6 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        cbTipoRepresentado = new javax.swing.JComboBox();
        txtRepresentadoNome = new javax.swing.JTextField();
        jLRepresentado1 = new javax.swing.JLabel();
        jCBRepresentado = new javax.swing.JCheckBox();
        jPanel20 = new javax.swing.JPanel();
        txtNomeRequerente = new javax.swing.JTextField();
        btnSelecionaRequerente = new javax.swing.JButton();
        txtIdRequerente = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtFiltroRequerente = new javax.swing.JTextField();
        btnFiltrar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblRequerente = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jrbPessoaFisica = new javax.swing.JRadioButton();
        jrbPessoaJuridica = new javax.swing.JRadioButton();
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
        setPreferredSize(new java.awt.Dimension(1099, 670));
        getContentPane().setLayout(new java.awt.GridBagLayout());

        jPanel1.setBackground(java.awt.SystemColor.inactiveCaption);
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 36));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagemsip/requerente_64x64.png"))); // NOI18N
        jLabel1.setText("Requerente");
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
        txtNomeBairro.setFont(new java.awt.Font("Verdana", 1, 12));
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
        gridBagConstraints.gridy = 5;
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
        txtNomeLogradouro.setFont(new java.awt.Font("Verdana", 1, 12));
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
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel17, gridBagConstraints);

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder("Documentação"));
        jPanel5.setLayout(new java.awt.GridBagLayout());

        jLabel5.setText("Rg");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel5, gridBagConstraints);

        txtRg.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 50;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(txtRg, gridBagConstraints);

        txtOrgao.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(txtOrgao, gridBagConstraints);

        jLabel6.setText("Orgão");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLabel6, gridBagConstraints);

        ftfCpfCnpj.setEditable(false);
        ftfCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        ftfCpfCnpj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfCpfCnpjActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(ftfCpfCnpj, gridBagConstraints);

        jLCnpj.setText("Cnpj");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLCnpj, gridBagConstraints);

        jLCpf.setText("Cpf");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel5.add(jLCpf, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel5, gridBagConstraints);

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
        txtNomeCidade.setFont(new java.awt.Font("Verdana", 1, 12));
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

        ftfCep.setEditable(false);
        ftfCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        ftfCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfCepActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(ftfCep, gridBagConstraints);

        jLCnpj1.setText("CEP");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_END;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel19.add(jLCnpj1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 20;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel19, gridBagConstraints);

        jPNacionalidade.setBorder(javax.swing.BorderFactory.createTitledBorder("Nacionalidade"));
        jPNacionalidade.setLayout(new java.awt.GridBagLayout());

        cbNacionalidade.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbNacionalidade.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "BRASILEIRO", "BRASILEIRA", "ESTRANGEIRO", "ESTRANGEIRA" }));
        cbNacionalidade.setEnabled(false);
        cbNacionalidade.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbNacionalidadeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPNacionalidade.add(cbNacionalidade, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.LINE_START;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 0);
        jPanel2.add(jPNacionalidade, gridBagConstraints);

        jPEstadoCivil.setBorder(javax.swing.BorderFactory.createTitledBorder("Estado Civil / Outros"));
        jPEstadoCivil.setLayout(new java.awt.GridBagLayout());

        cbEstadoCivil.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbEstadoCivil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "CASADO", "CASADA", "SOLTEIRO", "SOLTEIRA", "DIVORCIADO", "DIVORCIADA", "SEPARADO", "SEPARADA", "VIÚVO", "VIÚVA", "OUTRO" }));
        cbEstadoCivil.setEnabled(false);
        cbEstadoCivil.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbEstadoCivilMouseClicked(evt);
            }
        });
        cbEstadoCivil.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbEstadoCivilActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPEstadoCivil.add(cbEstadoCivil, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 361;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPEstadoCivil.add(txtConjugue, gridBagConstraints);

        jLConjugue.setText("Conjugue");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPEstadoCivil.add(jLConjugue, gridBagConstraints);

        jLRegime.setText("Regime");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPEstadoCivil.add(jLRegime, gridBagConstraints);

        cbRegime.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbRegime.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "COMUNHÃO PARCIAL DE BENS", "COMUNHÃO DE BENS", "SEPARAÇÃO TOTAL DE BENS", "SEPARAÇÃO PARCIAL DE BENS", "PARTICIPAÇÃO FINAL NOS AQUESTOS", " " }));
        jPEstadoCivil.add(cbRegime, new java.awt.GridBagConstraints());

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.gridwidth = 18;
        gridBagConstraints.ipadx = 40;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPEstadoCivil, gridBagConstraints);

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jPanel7.setLayout(new java.awt.GridBagLayout());

        jLabel4.setText("Profissão");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel4, gridBagConstraints);

        txtProfissao.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.ipadx = 200;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(txtProfissao, gridBagConstraints);

        jLabel13.setText("Telefone");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
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
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfTel, gridBagConstraints);

        ftfCel.setEditable(false);
        ftfCel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ftfCelActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 5;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 100;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(ftfCel, gridBagConstraints);

        jLabel9.setText("Celular");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 4;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel7.add(jLabel9, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
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

        jLRazaoSocial.setText("Razão Social");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(jLRazaoSocial, gridBagConstraints);

        txtNomeRazao.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 20;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel8.add(txtNomeRazao, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 21;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel2.add(jPanel8, gridBagConstraints);

        jTabbedPane1.addTab("Basico", jPanel2);

        jPanel6.setLayout(new java.awt.GridBagLayout());

        jPanel11.setBorder(javax.swing.BorderFactory.createTitledBorder("Pessoa Física Representado"));
        jPanel11.setLayout(new java.awt.GridBagLayout());

        cbTipoRepresentado.setFont(new java.awt.Font("Tahoma", 1, 11));
        cbTipoRepresentado.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "SELECIONE", "MAIOR", "MENOR" }));
        cbTipoRepresentado.setEnabled(false);
        cbTipoRepresentado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cbTipoRepresentadoMouseClicked(evt);
            }
        });
        cbTipoRepresentado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbTipoRepresentadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 45;
        gridBagConstraints.ipady = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(cbTipoRepresentado, gridBagConstraints);

        txtRepresentadoNome.setEditable(false);
        txtRepresentadoNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRepresentadoNomeActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.ipadx = 600;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(txtRepresentadoNome, gridBagConstraints);

        jLRepresentado1.setFont(new java.awt.Font("Tahoma", 0, 10));
        jLRepresentado1.setText("Representado");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel11.add(jLRepresentado1, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jPanel11, gridBagConstraints);

        jCBRepresentado.setText("Representado");
        jCBRepresentado.setEnabled(false);
        jCBRepresentado.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jCBRepresentadoMouseClicked(evt);
            }
        });
        jCBRepresentado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCBRepresentadoActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        jPanel6.add(jCBRepresentado, gridBagConstraints);

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Pessoa Juridica Representado"));
        jPanel20.setLayout(new java.awt.GridBagLayout());

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

        btnSelecionaRequerente.setIcon(new javax.swing.ImageIcon(getClass().getResource("/sip/imagem/lupa_16x16.png"))); // NOI18N
        btnSelecionaRequerente.setEnabled(false);
        btnSelecionaRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelecionaRequerenteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 3;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = -30;
        gridBagConstraints.ipady = -5;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(btnSelecionaRequerente, gridBagConstraints);

        txtIdRequerente.setEnabled(false);
        txtIdRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtIdRequerenteActionPerformed(evt);
            }
        });
        txtIdRequerente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtIdRequerenteFocusGained(evt);
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtIdRequerenteFocusLost(evt);
            }
        });
        txtIdRequerente.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
                txtIdRequerenteCaretPositionChanged(evt);
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
            }
        });
        txtIdRequerente.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
                txtIdRequerenteAncestorRemoved(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 2;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.ipadx = 30;
        jPanel20.add(txtIdRequerente, gridBagConstraints);

        jLabel19.setText("Id");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel20.add(jLabel19, gridBagConstraints);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel6.add(jPanel20, gridBagConstraints);

        jTabbedPane1.addTab("Representado", jPanel6);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.weightx = 1.0;
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

        txtFiltroRequerente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtFiltroRequerenteMouseClicked(evt);
            }
        });
        txtFiltroRequerente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtFiltroRequerenteActionPerformed(evt);
            }
        });
        txtFiltroRequerente.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                txtFiltroRequerenteFocusLost(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.weightx = 1.0;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        jPanel3.add(txtFiltroRequerente, gridBagConstraints);

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

        tblRequerente.setFont(new java.awt.Font("Tahoma", 1, 14));
        tblRequerente.setModel(new javax.swing.table.DefaultTableModel(
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
        tblRequerente.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        tblRequerente.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblRequerenteMouseClicked(evt);
            }
        });
        tblRequerente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tblRequerenteKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(tblRequerente);
        tblRequerente.getColumnModel().getColumn(0).setResizable(false);
        tblRequerente.getColumnModel().getColumn(1).setResizable(false);
        tblRequerente.getColumnModel().getColumn(2).setResizable(false);

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
        getContentPane().add(jPanel3, gridBagConstraints);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        buttonGroup1.add(jrbPessoaFisica);
        jrbPessoaFisica.setSelected(true);
        jrbPessoaFisica.setText("Pessoa Física");
        jrbPessoaFisica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbPessoaFisicaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jrbPessoaFisicaMousePressed(evt);
            }
        });
        jrbPessoaFisica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbPessoaFisicaActionPerformed(evt);
            }
        });
        jrbPessoaFisica.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jrbPessoaFisicaFocusLost(evt);
            }
        });
        jrbPessoaFisica.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jrbPessoaFisicaKeyReleased(evt);
            }
        });
        jPanel4.add(jrbPessoaFisica);

        buttonGroup1.add(jrbPessoaJuridica);
        jrbPessoaJuridica.setText("Pessoa Jurídica");
        jrbPessoaJuridica.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jrbPessoaJuridicaMouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jrbPessoaJuridicaMousePressed(evt);
            }
        });
        jrbPessoaJuridica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jrbPessoaJuridicaActionPerformed(evt);
            }
        });
        jrbPessoaJuridica.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jrbPessoaJuridicaFocusLost(evt);
            }
        });
        jrbPessoaJuridica.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jrbPessoaJuridicaKeyReleased(evt);
            }
        });
        jPanel4.add(jrbPessoaJuridica);

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
        txtFiltroRequerente.setText("");
    }//GEN-LAST:event_btnFiltrarActionPerformed

    private void btnNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNovoActionPerformed
        RequerenteBD cobradorBD = new RequerenteBD();
        if (cobradorBD.testaConexao()) {

            removeItem();
            habilitaCampos();
            limpaCampos();
            btnFiltrar.setEnabled(false);
            tblRequerente.setEnabled(false);
            limpaSelecaoTabela();
            txtNomeRazao.requestFocus();
            habilitaBotoes();
            modo = Constantes.INSERT_MODE;
            caixaAlta();
            limpaCampos();
            cbRegime.removeItem("***");
            cbTipoRepresentado.removeItem("***");
            configuraTipoPessoa();
        } else {
            JOptionPane.showMessageDialog(this, "Erro com a conexão!", "Erro", JOptionPane.ERROR_MESSAGE);
        }

    }//GEN-LAST:event_btnNovoActionPerformed

    private void btnSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalvarActionPerformed
        if (modo == Constantes.INSERT_MODE) {
            incluiRequerente();
            tblRequerente.setEnabled(true);
            // txtNomeCliente.requestFocus();
        } else if (modo == Constantes.EDIT_MODE) {
            alteraRequerente();
        }
    }//GEN-LAST:event_btnSalvarActionPerformed

    private void btnCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelarActionPerformed
        desabilitaBotoes();
        desabilitaCampos();
        limpaCampos();
        tblRequerente.setEnabled(true);
        btnFiltrar.setEnabled(true);
    }//GEN-LAST:event_btnCancelarActionPerformed

    private void btnAlterarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlterarActionPerformed
        if (tblRequerente.getSelectedRow() != -1) {
            habilitaCampos();
            habilitaBotoes();
            modo = Constantes.EDIT_MODE;
            buscaLogradouro();
            buscaBairro();
            buscaCidade();
            tblRequerente.setEnabled(false);
            txtFiltroRequerente.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Requerente da lista!", "Requerente", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnAlterarActionPerformed

    private void btnExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExcluirActionPerformed
        if (tblRequerente.getSelectedRow() != -1) {
            int resposta = JOptionPane.showConfirmDialog(this, "Confirma exclusão do Requerente?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (resposta == JOptionPane.YES_OPTION) {
                excluiRequerente();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um Requerente da lista!", "Requerente", JOptionPane.INFORMATION_MESSAGE);
        }
    }//GEN-LAST:event_btnExcluirActionPerformed

    private void ftfCpfCnpjActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfCpfCnpjActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfCpfCnpjActionPerformed

    private void txtFiltroRequerenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtFiltroRequerenteMouseClicked
    }//GEN-LAST:event_txtFiltroRequerenteMouseClicked

    private void txtFiltroRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtFiltroRequerenteActionPerformed
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroRequerenteActionPerformed

    private void txtFiltroRequerenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtFiltroRequerenteFocusLost
        atualizaTabela();
    }//GEN-LAST:event_txtFiltroRequerenteFocusLost

    private void tblRequerenteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblRequerenteMouseClicked
        if (evt.getClickCount() == 2) {
             if (modoProcesso == Constantes.PROCESSO) {
                selecionaRequerenteProcesso();
                dispose();
            } else if (modoProcesso == Constantes.RUAFRENTE) {
                selecionaRequerenteRequerente();
                dispose();
            }
        }
    }//GEN-LAST:event_tblRequerenteMouseClicked

    private void tblRequerenteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tblRequerenteKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
             if (modoProcesso == Constantes.PROCESSO) {
                selecionaRequerenteProcesso();
                dispose();
            } else if (modoProcesso == Constantes.RUAFRENTE) {
                selecionaRequerenteRequerente();
                dispose();
            }

        }
    }//GEN-LAST:event_tblRequerenteKeyPressed

    private void txtNomeLogradouroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeLogradouroActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeLogradouroActionPerformed

    private void btnSelecionaRuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaRuaActionPerformed
        LogradouroFrame ruaFrame = new LogradouroFrame(this);
        ruaFrame.setVisible(true);
        this.getDesktopPane().add(ruaFrame);
        ruaFrame.toFront();
        centralizaJanela(ruaFrame);  // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaRuaActionPerformed

    private void txtIdLogradouroFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdLogradouroFocusLost
        buscaLogradouro();        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdLogradouroFocusLost

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

    private void txtNomeCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeCidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtNomeCidadeActionPerformed

    private void btnSelecionaCidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaCidadeActionPerformed
        CidadeFrame cidadeFrame = new CidadeFrame(this);
        cidadeFrame.setVisible(true);
        this.getDesktopPane().add(cidadeFrame);
        cidadeFrame.toFront();
        centralizaJanela(cidadeFrame); // TODO add your handling code here:
    }//GEN-LAST:event_btnSelecionaCidadeActionPerformed

    private void txtIdCidadeFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdCidadeFocusLost
        buscaCidade();
    }//GEN-LAST:event_txtIdCidadeFocusLost

    private void cbNacionalidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbNacionalidadeActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbNacionalidadeActionPerformed

    private void ftfCelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfCelActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_ftfCelActionPerformed

    private void ftfTelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfTelActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfTelActionPerformed

    private void cbEstadoCivilActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbEstadoCivilActionPerformed
        configuraEstadoCivilCasado();
}//GEN-LAST:event_cbEstadoCivilActionPerformed

    private void cbEstadoCivilMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbEstadoCivilMouseClicked
        if (evt.getClickCount() == 1) {
            configuraEstadoCivilCasado();
        } // TODO add your handling code here:
}//GEN-LAST:event_cbEstadoCivilMouseClicked

    private void cbTipoRepresentadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbTipoRepresentadoActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_cbTipoRepresentadoActionPerformed

    private void cbTipoRepresentadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cbTipoRepresentadoMouseClicked
        // TODO add your handling code here:
}//GEN-LAST:event_cbTipoRepresentadoMouseClicked

    private void jCBRepresentadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCBRepresentadoActionPerformed
        habilitaRepresentado(); // TODO add your handling code here:
    }//GEN-LAST:event_jCBRepresentadoActionPerformed

    private void jCBRepresentadoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jCBRepresentadoMouseClicked
        //if (evt.getClickCount() == 1) {
        // habilitaRepresentado();
        //} // TODO add your handling code here:
    }//GEN-LAST:event_jCBRepresentadoMouseClicked

    private void txtRepresentadoNomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRepresentadoNomeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRepresentadoNomeActionPerformed

    private void jrbPessoaFisicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbPessoaFisicaActionPerformed
        configuraTipoPessoa();
        defineModeloCnpjCpf(); // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaFisicaActionPerformed

    private void txtNomeRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtNomeRequerenteActionPerformed
        // TODO add your handling code here:
}//GEN-LAST:event_txtNomeRequerenteActionPerformed

    private void btnSelecionaRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelecionaRequerenteActionPerformed
        RequerenteFrame requerenteFrame = new RequerenteFrame(this);
        requerenteFrame.setVisible(true);
        this.getDesktopPane().add(requerenteFrame);
        requerenteFrame.toFront();
        //centralizaJanelaRegVen(requerenteFrame);             // TODO add your handling code here:
}//GEN-LAST:event_btnSelecionaRequerenteActionPerformed

    private void txtIdRequerenteFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdRequerenteFocusLost
        buscaRequerente();           // TODO add your handling code here:
}//GEN-LAST:event_txtIdRequerenteFocusLost

    private void jrbPessoaFisicaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbPessoaFisicaMousePressed
        if (evt.getClickCount() == 1) {
            configuraTipoPessoa();
            defineModeloCnpjCpf();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaFisicaMousePressed

    private void jrbPessoaJuridicaMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbPessoaJuridicaMousePressed
        if (evt.getClickCount() == 1) {
            configuraTipoPessoa();
            defineModeloCnpjCpf();
        }
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaJuridicaMousePressed

    private void jrbPessoaFisicaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbPessoaFisicaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaFisicaMouseClicked

    private void jrbPessoaJuridicaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jrbPessoaJuridicaMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaJuridicaMouseClicked

    private void jrbPessoaJuridicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jrbPessoaJuridicaActionPerformed
        configuraTipoPessoa();
        defineModeloCnpjCpf(); // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaJuridicaActionPerformed

    private void jrbPessoaFisicaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jrbPessoaFisicaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaFisicaKeyReleased

    private void jrbPessoaJuridicaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jrbPessoaJuridicaKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaJuridicaKeyReleased

    private void jrbPessoaJuridicaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jrbPessoaJuridicaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaJuridicaFocusLost

    private void jrbPessoaFisicaFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jrbPessoaFisicaFocusLost
        // TODO add your handling code here:
    }//GEN-LAST:event_jrbPessoaFisicaFocusLost

    private void txtIdRequerenteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtIdRequerenteActionPerformed
        buscaRequerente();                 // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRequerenteActionPerformed

    private void txtIdRequerenteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtIdRequerenteFocusGained
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRequerenteFocusGained

    private void txtIdRequerenteAncestorRemoved(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_txtIdRequerenteAncestorRemoved
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRequerenteAncestorRemoved

    private void txtIdRequerenteCaretPositionChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtIdRequerenteCaretPositionChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtIdRequerenteCaretPositionChanged

    private void ftfCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftfCepActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftfCepActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAlterar;
    private javax.swing.JButton btnCancelar;
    private javax.swing.JButton btnExcluir;
    private javax.swing.JButton btnFiltrar;
    private javax.swing.JButton btnNovo;
    private javax.swing.JButton btnSalvar;
    private javax.swing.JButton btnSelecionaBairro;
    private javax.swing.JButton btnSelecionaCidade;
    private javax.swing.JButton btnSelecionaRequerente;
    private javax.swing.JButton btnSelecionaRua;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox cbEstadoCivil;
    private javax.swing.JComboBox cbNacionalidade;
    private javax.swing.JComboBox cbRegime;
    private javax.swing.JComboBox cbTipoRepresentado;
    private javax.swing.JFormattedTextField ftfCel;
    private javax.swing.JFormattedTextField ftfCep;
    private javax.swing.JFormattedTextField ftfCpfCnpj;
    private javax.swing.JFormattedTextField ftfTel;
    private javax.swing.JCheckBox jCBRepresentado;
    private javax.swing.JLabel jLCnpj;
    private javax.swing.JLabel jLCnpj1;
    private javax.swing.JLabel jLConjugue;
    private javax.swing.JLabel jLCpf;
    private javax.swing.JLabel jLNome;
    private javax.swing.JLabel jLRazaoSocial;
    private javax.swing.JLabel jLRegime;
    private javax.swing.JLabel jLRepresentado1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPEstadoCivil;
    private javax.swing.JPanel jPNacionalidade;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton jrbPessoaFisica;
    private javax.swing.JRadioButton jrbPessoaJuridica;
    private javax.swing.JTable tblRequerente;
    private javax.swing.JTextField txtComplemento;
    private javax.swing.JTextField txtConjugue;
    private javax.swing.JTextField txtFiltroRequerente;
    private javax.swing.JTextField txtIdBairro;
    private javax.swing.JTextField txtIdCidade;
    private javax.swing.JTextField txtIdLogradouro;
    private javax.swing.JTextField txtIdRequerente;
    private javax.swing.JTextField txtNomeBairro;
    private javax.swing.JTextField txtNomeCidade;
    private javax.swing.JTextField txtNomeLogradouro;
    private javax.swing.JTextField txtNomeRazao;
    private javax.swing.JTextField txtNomeRequerente;
    private javax.swing.JTextField txtNumero;
    private javax.swing.JTextField txtOrgao;
    private javax.swing.JTextField txtProfissao;
    private javax.swing.JTextField txtRepresentadoNome;
    private javax.swing.JTextField txtRg;
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
     * @return the requerente
     */
    public Requerente getRequerente() {
        return requerente;
    }

    /**
     * @param requerente the requerente to set
     */
    public void setRequerente(Requerente requerente) {
        this.requerente = requerente;
        txtNomeRequerente.setText(requerente.getNome());
        txtIdRequerente.setText(requerente.getId().toString());
    }
}
