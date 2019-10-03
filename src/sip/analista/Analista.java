/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.analista;

import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class Analista {

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String matricula;    
    private Integer qtdeEntrada;
    private Integer qtdeSaida;
    private Integer saldo;
    private Usuario usuario;
    private String tipo;
    
    public Analista() {
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
    public void setNome(String quadra) {
        this.nome = quadra;
    }

    /**
     * @return the livro
     */
    public String getMatricula() {
        return matricula;
    }

    /**
     * @param livro the livro to set
     */
    public void setMatricula(String lote) {
        this.matricula = lote;
    }

    /**
     * @return the qtdeEntrada
     */
    public Integer getQtdeEntrada() {
        return qtdeEntrada;
    }

    /**
     * @param qtdeEntrada the qtdeEntrada to set
     */
    public void setQtdeEntrada(Integer qtdeEntrada) {
        this.qtdeEntrada = qtdeEntrada;
    }

    /**
     * @return the qtdeSaida
     */
    public Integer getQtdeSaida() {
        return qtdeSaida;
    }

    /**
     * @param qtdeSaida the qtdeSaida to set
     */
    public void setQtdeSaida(Integer qtdeSaida) {
        this.qtdeSaida = qtdeSaida;
    }

    /**
     * @return the saldo
     */
    public Integer getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(Integer saldo) {
        this.saldo = saldo;
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
