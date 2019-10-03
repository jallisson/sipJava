/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.analise;

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
public class ParecerAnalise {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private Analise analise;
    private Date dataParecer;
    private String Tipo;
    private String controle;
    private Date dataVistoria;
    private String anexoIncluso;
    
    //OBJETOS
    private Usuario usuario;
    private Processo processo;
    private Requerente requerente;
    private AnexosProcesso anexosProcesso;
    private List<AnexosProcesso> ListAnexosProcesso;
    
    



    public ParecerAnalise() {
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
    public Analise getAnalise() {
        return analise;
    }

    /**
     * @param analise the analise to set
     */
    public void setAnalise(Analise analise) {
        this.analise = analise;
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
    public String getTipo() {
        return Tipo;
    }

    /**
     * @param Tipo the Tipo to set
     */
    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
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
     * @return the dataVistoria
     */
    public Date getDataVistoria() {
        return dataVistoria;
    }

    /**
     * @param dataVistoria the dataVistoria to set
     */
    public void setDataVistoria(Date dataVistoria) {
        this.dataVistoria = dataVistoria;
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

   

}
