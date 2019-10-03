/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.tramitacao;


import sip.processo.Processo;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Timestamp;
import java.util.List;
import sip.analise.ParecerAnalise;
import sip.autorizacaoeventos.AutorizacaoEventos;
import sip.bairro.Bairro;
import sip.denuncia.Denuncia;
import sip.emissaolicenca.EmissaoLicenca;
import sip.fiscalizacao.ParecerFiscalizacao;
import sip.juridico.ParecerJuridico;
import sip.pessoa.Pessoa;
import sip.processo.AnexosProcesso;



/**
 *
 * @author T2Ti
 */
public class Tramitacao {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Timestamp dataTramitaçao;
    private String mesAno;
    private Usuario usuario;
    private Processo processo;
    private String controle;
    private String status;
    private String parecer;
    private String setor;
    private String setorOrigem;
    private String setorDestino;
    private String laudoMzu;
    private String observacao;
    private String situacaoCad;
    private Requerente requerente;
    private Bairro bairro;
    private EmissaoLicenca emissaoLicenca;
    private AutorizacaoEventos autorizacaoEventos;
    private List<AnexosProcesso> ListAnexosProcesso;
    private AnexosProcesso anexosProcesso;
    private ParecerAnalise parecerAnalise;
    private ParecerJuridico parecerJuridico;
    private ParecerFiscalizacao parecerFiscalizacao;
    private Pessoa pessoa;
    private Denuncia denuncia;
   


    public Tramitacao() {
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the nome
     */


    /**
     * @return the telefone
     */
    /*public String getTelefone() {
        return telefone;
    }*/

    /**
     * @param telefone the telefone to set
     */
    /*public void setTelefone(String telefone) {
        this.telefone = telefone;
    }*/

    /**
     * @return the cpf
     */
    public String getControle() {
        return controle;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setControle(String controle) {
        this.controle = controle;
    }

    /**
     * @return the dataProcesso
     */
    public Timestamp getDataTramitacao() {
        return dataTramitaçao;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataTramitacao(Timestamp dataTramitacao) {
        this.dataTramitaçao = dataTramitacao;
    }

    /**
     * @return the mesAno
     */
    public String getMesAno() {
        return mesAno;
    }

    /**
     * @param mesAno the mesAno to set
     */
    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
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
    }


    /**
     * @return the processo
     */


    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the parecer
     */
    public String getParecer() {
        return parecer;
    }

    /**
     * @param parecer the parecer to set
     */
    public void setParecer(String parecer) {
        this.parecer = parecer;
    }

    /**
     * @return the setor
     */
    public String getSetor() {
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(String setor) {
        this.setor = setor;
    }

    /**
     * @return the observacao
     */
    public String getObservacao() {
        return observacao;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setObservacao(String observacao) {
        this.observacao = observacao;
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
     * @return the setorOrigem
     */
    public String getSetorOrigem() {
        return setorOrigem;
    }

    /**
     * @param setorOrigem the setorOrigem to set
     */
    public void setSetorOrigem(String setorOrigem) {
        this.setorOrigem = setorOrigem;
    }

    /**
     * @return the setorDestino
     */
    public String getSetorDestino() {
        return setorDestino;
    }

    /**
     * @param setorDestino the setorDestino to set
     */
    public void setSetorDestino(String setorDestino) {
        this.setorDestino = setorDestino;
    }

    /**
     * @return the hora
     */


    /**
     * @return the laudoMzu
     */
    public String getLaudoMzu() {
        return laudoMzu;
    }

    /**
     * @param laudoMzu the laudoMzu to set
     */
    public void setLaudoMzu(String laudoMzu) {
        this.laudoMzu = laudoMzu;
    }

    /**
     * @return the situacaoCad
     */
    public String getSituacaoCad() {
        return situacaoCad;
    }

    /**
     * @param situacaoCad the situacaoCad to set
     */
    public void setSituacaoCad(String situacaoCad) {
        this.situacaoCad = situacaoCad;
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
    }

    /**
     * @return the bairro
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the emissaoLicenca
     */
    public EmissaoLicenca getEmissaoLicenca() {
        return emissaoLicenca;
    }

    /**
     * @param emissaoLicenca the emissaoLicenca to set
     */
    public void setEmissaoLicenca(EmissaoLicenca emissaoLicenca) {
        this.emissaoLicenca = emissaoLicenca;
    }

    /**
     * @return the autorizacaoEventos
     */
    public AutorizacaoEventos getAutorizacaoEventos() {
        return autorizacaoEventos;
    }

    /**
     * @param autorizacaoEventos the autorizacaoEventos to set
     */
    public void setAutorizacaoEventos(AutorizacaoEventos autorizacaoEventos) {
        this.autorizacaoEventos = autorizacaoEventos;
    }

    /**
     * @return the ListAnexosProcesso
     */
    public List<AnexosProcesso> getListAnexosProcesso() {
        return ListAnexosProcesso;
    }

    /**
     * @param ListAnexosProcesso the ListAnexosProcesso to set
     */
    public void setListAnexosProcesso(List<AnexosProcesso> ListAnexosProcesso) {
        this.ListAnexosProcesso = ListAnexosProcesso;
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
    }

    /**
     * @return the parecerAnalise
     */
    public ParecerAnalise getParecerAnalise() {
        return parecerAnalise;
    }

    /**
     * @param parecerAnalise the parecerAnalise to set
     */
    public void setParecerAnalise(ParecerAnalise parecerAnalise) {
        this.parecerAnalise = parecerAnalise;
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
     * @return the parecereFiscalizacao
     */
    public ParecerFiscalizacao getParecerFiscalizacao() {
        return parecerFiscalizacao;
    }

    /**
     * @param parecereFiscalizacao the parecereFiscalizacao to set
     */
    public void setParecerFiscalizacao(ParecerFiscalizacao parecereFiscalizacao) {
        this.parecerFiscalizacao = parecereFiscalizacao;
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @return the denuncia
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @param denuncia the denuncia to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    /**
     * @return the anexosProcesso
     */


    /**
     * @return the email
     */
}
