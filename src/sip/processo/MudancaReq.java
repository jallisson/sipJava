/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.processo;

import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Date;



/**
 *
 * @author T2Ti
 */
public class MudancaReq {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Date dataMudanca;
    private Requerente requerente;
    private Processo processo;
    private Usuario usuario;


    public MudancaReq() {
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
   

    /**
     * @return the dataProcesso
     */
    public Date getDataMudanca() {
        return dataMudanca;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataMudanca(Date dataMudanca) {
        this.dataMudanca = dataMudanca;
    }

    /**
     * @return the mesAno
     */
   

    /**
     * @param mesAno the mesAno to set
     */


    /**
     * @return the digitador
     */
 
    /**
     * @param digitador the digitador to set
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
     * @return the valor
     */
 

    /**
     * @param valor the valor to set
     */
   

    /**
     * @return the tipo


    /**
     * @return the situacao
     */


    /**
     * @return the num_digitado
     */


    /**
     * @return the email
     */
}
