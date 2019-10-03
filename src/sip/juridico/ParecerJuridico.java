/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.juridico;

import sip.processo.Processo;
import sip.requerente.Requerente;
import java.sql.Date;
import java.util.List;
import sip.processo.AnexosProcesso;
import sip.usuario.Usuario;



/**
 *
 * @author T2Ti
 */
public class ParecerJuridico {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private Juridico juridico;
    private Date dataParecer;
    private String parecer;
    private String controle;
    private String anexoIncluso;
    private String Tipo;
    
    //OBJETOS
    private Processo processo;
    private Requerente requerente;
    private Usuario usuario;
    private AnexosProcesso anexosProcesso;
    private List<AnexosProcesso> ListAnexosProcesso;
    
    



    public ParecerJuridico() {
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
    public Date getDataParecer() {
        return dataParecer;
    }

    /**
     * @param dataParecer the dataParecer to set
     */
    public void setDataParecer(Date dataParecer) {
        this.dataParecer = dataParecer;
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
    public String getParecer() {
        return parecer;
    }

    /**
     * @param Tipo the Tipo to set
     */
    public void setParecer(String parecer) {
        this.parecer = parecer;
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
     * @return the anexoIncluso
     */
    public String getAnexoIncluso() {
        return anexoIncluso;
    }

    /**
     * @param anexoIncluso the anexoIncluso to set
     */
    public void setAnexoIncluso(String anexoIncluso) {
        this.anexoIncluso = anexoIncluso;
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
     * @return the Tipo
     */
    public String getTipo() {
        return Tipo;
    }

    /**
     * @param Tipo the Tipo to set
     */
    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

   

}
