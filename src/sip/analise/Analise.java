/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.analise;

import java.sql.Date;
import java.util.List;
import sip.analista.Analista;
import sip.distribuicao.Distribuicao;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class Analise {

    /**
     * @return the concluido
     */
    public String getConcluido() {
        return concluido;
    }

    /**
     * @param concluido the concluido to set
     */
    public void setConcluido(String concluido) {
        this.concluido = concluido;
    }

   // private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private Integer id;
    private Distribuicao distribuicao;
    private Date dataAnalise;
    private String ano;
    //para consulta
    private Requerente requerente;
    private Processo processo;
    private Analista analista;
    private List<ParecerAnalise> listParecerAnalise;
    private String concluido;


    public Analise() {
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
     * @return the dataLote
     */
    public Date getDataAnalise() {
        return dataAnalise;
    }

    /**
     * @param dataLote the dataLote to set
     */
    public void setDataAnalise(Date dataLote) {
        this.dataAnalise = dataLote;
    }

    /**
     * @return the mesAno
     */
    public String getAno() {
        return ano;
    }

    /**
     * @param mesAno the mesAno to set
     */
    public void setAno(String mesAno) {
        this.ano = mesAno;
    }

    /**
     * @return the distribuicao
     */
    public Distribuicao getDistribuicao() {
        return distribuicao;
    }

    /**
     * @param distribuicao the distribuicao to set
     */
    public void setDistribuicao(Distribuicao distribuicao) {
        this.distribuicao = distribuicao;
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
     * @return the analista
     */
    public Analista getAnalista() {
        return analista;
    }

    /**
     * @param analista the analista to set
     */
    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    /**
     * @return the listParecerAnalise
     */
    public List<ParecerAnalise> getListParecerAnalise() {
        return listParecerAnalise;
    }

    /**
     * @param listParecerAnalise the listParecerAnalise to set
     */
    public void setListParecerAnalise(List<ParecerAnalise> listParecerAnalise) {
        this.listParecerAnalise = listParecerAnalise;
    }

    /**
     * @return the nomeArquivo1
     */

}
