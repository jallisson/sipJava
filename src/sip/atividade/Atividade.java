/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.atividade;

/**
 *
 * @author T2Ti
 */
public class Atividade {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String potencialPoluidor;



    public Atividade() {
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
     * @return the potencialPoluidor
     */
    public String getPotencialPoluidor() {
        return potencialPoluidor;
    }

    /**
     * @param potencialPoluidor the potencialPoluidor to set
     */
    public void setPotencialPoluidor(String potencialPoluidor) {
        this.potencialPoluidor = potencialPoluidor;
    }

}
