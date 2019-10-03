/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.distribuicao;

import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Date;
import sip.analista.Analista;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;
import sip.processo.Processo;



/**
 *
 * @author T2Ti
 */
public class Distribuicao {

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

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Analista analista;
    private Processo processo;
    private Requerente requerente;
    private Date dataDistribuicao;
    private String mesAno;
    private String controle;
    private Usuario usuario;
    private String tramitouAnalise;
    private String tramitouJuridico;
    private Pessoa pessoa;
    private Denuncia denuncia;
    private String tramitouFiscalizacao;
    private String status;
   


    public Distribuicao() {
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
     * @return the dataProcesso
     */
    public Date getDataDist() {
        return dataDistribuicao;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataDist(Date dataProcesso) {
        this.dataDistribuicao = dataProcesso;
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
    public Processo getProcesso() {
        return processo;
    }

    /**
     * @param digitador the digitador to set
     */
    public void setProcesso(Processo processo) {
        this.processo = processo;
    }

    /**
     * @return the requerente
     */
    public Analista getAnalista() {
        return analista;
    }

    /**
     * @param requerente the requerente to set
     */
    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    public String getControle() {
        return controle;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setControle(String tipo) {
        this.controle = tipo;
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
     * @return the tramitou
     */
    public String getTramitouAnalise() {
        return tramitouAnalise;
    }

    /**
     * @param tramitou the tramitou to set
     */
    public void setTramitouAnalise(String tramitou) {
        this.tramitouAnalise = tramitou;
    }

    /**
     * @return the tramitouJuridico
     */
    public String getTramitouJuridico() {
        return tramitouJuridico;
    }

    /**
     * @param tramitouJuridico the tramitouJuridico to set
     */
    public void setTramitouJuridico(String tramitouJuridico) {
        this.tramitouJuridico = tramitouJuridico;
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
     * @return the tramitouFiscalizacao
     */
    public String getTramitouFiscalizacao() {
        return tramitouFiscalizacao;
    }

    /**
     * @param tramitouFiscalizacao the tramitouFiscalizacao to set
     */
    public void setTramitouFiscalizacao(String tramitouFiscalizacao) {
        this.tramitouFiscalizacao = tramitouFiscalizacao;
    }
}










