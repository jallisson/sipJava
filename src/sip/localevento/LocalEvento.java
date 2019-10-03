/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.localevento;

import sip.logradouro.*;

/**
 *
 * @author T2Ti
 */
public class LocalEvento {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;



    public LocalEvento() {
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
    public String getNome() {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

}
