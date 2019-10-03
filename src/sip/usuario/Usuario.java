/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.usuario;

import sip.bean.NivelAcesso;
import java.util.List;

/**
 *
 * @author T2Ti
 */
public class Usuario {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String login;
    private String senha;
    private String setor;
    private List<NivelAcesso> nivelAcesso;
    private List<NAcesso> nivelAce;


    public Usuario() {
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
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the nivelAcesso
     */
    public List<NivelAcesso> getNivelAcesso() {
        return nivelAcesso;
    }

    /**
     * @param nivelAcesso the nivelAcesso to set
     */
    public void setNivelAcesso(List<NivelAcesso> nivelAcesso) {
        this.nivelAcesso = nivelAcesso;
    }

    /**
     * @return the nivelAce
     */
    public List<NAcesso> getNivelAce() {
        return nivelAce;
    }

    /**
     * @param nivelAce the nivelAce to set
     */
    public void setNivelAce(List<NAcesso> nivelAce) {
        this.nivelAce = nivelAce;
    }

    /**
     * @return the setor
     */
    public String getSetor() {
        return setor;
    }

    /**
     * @param setor the setor to set
     */
    public void setSetor(String setor) {
        this.setor = setor;
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
    /*public String getCpf() {
        return cpf;
    }*/

    /**
     * @param cpf the cpf to set
     */
    /*public void setCpf(String cpf) {
        this.cpf = cpf;
    }*/

    /**
     * @return the email
     */
}
