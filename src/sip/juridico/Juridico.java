/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.juridico;

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
public class Juridico {

   // private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private Integer id;
    private Distribuicao distribuicao;
    private Date dataJuridico;
    //para consulta
    private Requerente requerente;
    private Processo processo;
    private String tramitouGabinete;
    private String tramitouEmissao;
    private List<ParecerJuridico> listParecerJuridico;


    public Juridico() {
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
    public Date getDataJuridico() {
        return dataJuridico;
    }

    /**
     * @param dataLote the dataLote to set
     */
    public void setDataJuridico(Date dataLote) {
        this.dataJuridico = dataLote;
    }

    /**
     * @return the mesAno
     */


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
     * @return the listParecerAnalise
     */
    public List<ParecerJuridico> getListParecerJuridico() {
        return listParecerJuridico;
    }

    /**
     * @param listParecerAnalise the listParecerAnalise to set
     */
    public void setListParecerJuridico(List<ParecerJuridico> listParecerJuridico) {
        this.listParecerJuridico = listParecerJuridico;
    }

    /**
     * @return the tramitouGabinete
     */
    public String getTramitouGabinete() {
        return tramitouGabinete;
    }

    /**
     * @param tramitouGabinete the tramitouGabinete to set
     */
    public void setTramitouGabinete(String tramitouGabinete) {
        this.tramitouGabinete = tramitouGabinete;
    }

    /**
     * @return the tramitouEmissao
     */
    public String getTramitouEmissao() {
        return tramitouEmissao;
    }

    /**
     * @param tramitouEmissao the tramitouEmissao to set
     */
    public void setTramitouEmissao(String tramitouEmissao) {
        this.tramitouEmissao = tramitouEmissao;
    }

    /**
     * @return the nomeArquivo1
     */

}
