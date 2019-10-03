/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.lotedma;

import java.sql.Date;

/**
 *
 * @author T2Ti
 */
public class LoteDma {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String lote;
    private String descricao;
    private Date dataLote;
    private String mesAno;
    



    public LoteDma() {
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
    public String getLote() {
        return lote;
    }

    /**
     * @param nome the nome to set
     */
    public void setLote(String nome) {
        this.lote = nome;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the dataLote
     */
    public Date getDataLote() {
        return dataLote;
    }

    /**
     * @param dataLote the dataLote to set
     */
    public void setDataLote(Date dataLote) {
        this.dataLote = dataLote;
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

}
