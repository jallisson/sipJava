/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.cidade;

/**
 *
 * @author T2Ti
 */
public class Cidade {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String uf;


    public Cidade() {
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

    /**
     * @return the uf
     */
    public String getUf() {
        return uf;
    }

    /**
     * @param uf the uf to set
     */
    public void setUf(String uf) {
        this.uf = uf;
    }

}
