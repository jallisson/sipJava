/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.bairro;

/**
 *
 * @author T2Ti
 */
public class Bairro extends Object {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String livro;
    private Integer folha;
    private String matricula;
    private String tipo;
    
   public Bairro(){
       
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
     * @return the livro
     */
    public String getLivro() {
        return livro;
    }

    /**
     * @param livro the livro to set
     */
    public void setLivro(String livro) {
        this.livro = livro;
    }

    /**
     * @return the folha
     */
    public Integer getFolha() {
        return folha;
    }

    /**
     * @param folha the folha to set
     */
    public void setFolha(Integer folha) {
        this.folha = folha;
    }

    /**
     * @return the matricula
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param matricula the matricula to set
     */
    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

}
