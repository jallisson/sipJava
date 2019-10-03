/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.processo;

import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Date;
import java.util.List;
import sip.atividade.Atividade;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;



/**
 *
 * @author T2Ti
 */
public class Processo {

    /**
     * @return the observação
     */

 

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String numProcesso;
    private Date dataProcesso;
    private String mesAno;
    private Requerente requerente;
    private Usuario usuario;
    private String controleDig;
    private Double valor;
    private String tipoLicenca;
    private String tramitouDMA;
    private Atividade atividade;
    private String arquivado;
    private String controle;
    private List<AnexosProcesso> anexosProcesso;
    private String lancadoDma;
    private String mp;
    private Denuncia denuncia;
    private Pessoa pessoa;
    private String observacao;
    
   


    public Processo() {
    }
    
    public String getNumProcesso(){
        return numProcesso;
    }
    
    public void setNumProcesso(String numProcesso){
    this.numProcesso = numProcesso;
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
    public String getControleDigitador() {
        return controleDig;
    }

    /**
     * @param controle the cpf to set
     */
    public void setControleDigitador(String controle) {
        this.controleDig = controle;
    }

    /**
     * @return the dataProcesso
     */
    public Date getDataProcesso() {
        return dataProcesso;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataProcesso(Date dataProcesso) {
        this.dataProcesso = dataProcesso;
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
     * @param usuario the digitador to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
     * @return the valor
     */
    public Double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(Double valor) {
        this.valor = valor;
    }

    /**
     * @return the tipo
     */
    public String getTipoLicenca() {
        return tipoLicenca;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipoLicenca(String tipo) {
        this.tipoLicenca = tipo;
    }

    /**
     * @return the situacao
     */


    /**
     * @return the anexosProcesso
     */
    public List<AnexosProcesso> getAnexosProcesso() {
        return anexosProcesso;
    }

    /**
     * @param anexosProcesso the anexosProcesso to set
     */
    public void setAnexosProcesso(List<AnexosProcesso> anexosProcesso) {
        this.anexosProcesso = anexosProcesso;
    }

    /**
     * @return the tramitouDMA
     */
    public String getTramitouDMA() {
        return tramitouDMA;
    }

    /**
     * @param tramitouDMA the tramitouDMA to set
     */
    public void setTramitouDMA(String tramitouDMA) {
        this.tramitouDMA = tramitouDMA;
    }

    /**
     * @return the atividade
     */
    public Atividade getAtividade() {
        return atividade;
    }

    /**
     * @param atividade the atividade to set
     */
    public void setAtividade(Atividade atividade) {
        this.atividade = atividade;
    }

    /**
     * @return the arquivado
     */
    public String getArquivado() {
        return arquivado;
    }

    /**
     * @param arquivado the arquivado to set
     */
    public void setArquivado(String arquivado) {
        this.arquivado = arquivado;
    }

    /**
     * @return the controle
     */
    public String getControle() {
        return controle;
    }

    /**
     * @param controle the controle to set
     */
    public void setControle(String controle) {
        this.controle = controle;
    }

    /**
     * @return the num_digitado
     */


    /**
     * @return the email
     */
    
    
      /**
     * @return the lancadoDma
     */
    public String getLancadoDma() {
        return lancadoDma;
    }

    /**
     * @param lancadoDma the lancadoDma to set
     */
    public void setLancadoDma(String lancadoDma) {
        this.lancadoDma = lancadoDma;
    }

    /**
     * @return the mp
     */
    public String getMp() {
        return mp;
    }

    /**
     * @param mp the mp to set
     */
    public void setMp(String mp) {
        this.mp = mp;
    }

    /**
     * @return the denuncia
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param denuncia the denuncia to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
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
     * @return the baixouDma
     */
}
