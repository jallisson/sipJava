/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.movimentodma;

import sip.distribuicao.*;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Date;
import sip.analista.Analista;
import sip.lotedma.LoteDma;
import sip.processo.Processo;



/**
 *
 * @author T2Ti
 */
public class MovimentoDma {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Processo processo;
    private Requerente requerente;
    private Date dataMovimento;
    private String mesAno;
    private String controle;
    private Usuario usuario;
    private String movimento;
    private String situacao;
    private LoteDma lote;
    
    
   


    public MovimentoDma() {
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
    public Date getDataMovimento() {
        return dataMovimento;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataMovimento(Date dataProcesso) {
        this.dataMovimento = dataProcesso;
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
     * @return the observacao
     */
    public String getMovimento() {
        return movimento;
    }

    /**
     * @param observacao the observacao to set
     */
    public void setMovimento(String observacao) {
        this.movimento = observacao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the lote
     */
    public LoteDma getLote() {
        return lote;
    }

    /**
     * @param lote the lote to set
     */
    public void setLote(LoteDma lote) {
        this.lote = lote;
    }


}










