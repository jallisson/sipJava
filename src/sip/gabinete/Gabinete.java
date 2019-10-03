/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.gabinete;

import sip.processo.Processo;
import sip.requerente.Requerente;
import java.sql.Date;
import sip.juridico.Juridico;
import sip.usuario.Usuario;



/**
 *
 * @author T2Ti
 */
public class Gabinete {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private Juridico juridico;
    private Date dataGabinete;
    private String tramitouJuridico;
    
    //PARA CONSULTAS
    private Processo processo;
    private Requerente requerente;
    private Usuario usuario;
    
    



    public Gabinete() {
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
     * @return the analise
     */
    public Juridico getJuridico() {
        return juridico;
    }

    /**
     * @param analise the analise to set
     */
    public void setJuridico(Juridico analise) {
        this.juridico = analise;
    }

    /**
     * @return the dataParecer
     */
    public Date getDataGabinete() {
        return dataGabinete;
    }

    /**
     * @param dataParecer the dataParecer to set
     */
    public void setDataGabinete(Date dataParecer) {
        this.dataGabinete = dataParecer;
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
     * @return the Tipo
     */

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
     * @return the controle
     */

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
     * @return the tramitouEmissao
     */
    public String getTramitouJuridico() {
        return tramitouJuridico;
    }

    /**
     * @param tramitouEmissao the tramitouEmissao to set
     */
    public void setTramitouJuridico(String tramitouJuridico) {
        this.tramitouJuridico = tramitouJuridico;
    }

   

}
