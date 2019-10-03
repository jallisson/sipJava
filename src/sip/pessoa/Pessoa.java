/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.pessoa;

import sip.bairro.Bairro;
import sip.cidade.Cidade;
import sip.logradouro.Logradouro;

/**
 *
 * @author T2Ti
 */
public class Pessoa {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private Logradouro logradouro;
    private String numero;
    private Bairro bairro;
    private Cidade cidade;
    private String telFixo;
    private String telCel;
    private String complemento;


    public Pessoa() {
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
     * @return the empresa
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * @param empresa the empresa to set
     */
    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the nacionalidade
     */



    /**
     * @return the ruaAv
     */
    public Logradouro getLogradouro() {
        return logradouro;
    }

    /**
     * @param ruaAv the ruaAv to set
     */
    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the cidade
     */
    public Cidade getCidade() {
        return cidade;
    }

    /**
     * @param cidade the cidade to set
     */
    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    /**
     * @return the nacionalidade
     */


    /**
     * @return the telFixo
     */
    public String getTelFixo() {
        return telFixo;
    }

    /**
     * @param telFixo the telFixo to set
     */
    public void setTelFixo(String telFixo) {
        this.telFixo = telFixo;
    }

    /**
     * @return the telCel
     */
    public String getTelCel() {
        return telCel;
    }

    /**
     * @param telCel the telCel to set
     */
    public void setTelCel(String telCel) {
        this.telCel = telCel;
    }

    /**
     * @return the nubente
     */


    /**
     * @return the regime
     */

    /**
     * @return the representadoNome
     */
   

    
    /**
     * @return the representadoTipo
     */


    /**
     * @return the razaoSocial
     */
   

    /**
     * @return the cnpj
     */
       /**
     * @return the requerente
     */


    /**
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the cep
     */


    /**
     * @return the id_empresa
     */


    /**
     * @return the email
     */
}
