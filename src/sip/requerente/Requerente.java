/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.requerente;

import sip.bairro.Bairro;
import sip.cidade.Cidade;
import sip.logradouro.Logradouro;

/**
 *
 * @author T2Ti
 */
public class Requerente {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private String nome;
    private String nacionalidade;
    private String estadoCivil;
    //private String telefone;
    private String profissao;
    private String rg;
    private String orgao;
    private String cpf;  
    private Logradouro logradouro;
    private String numero;
    private Bairro bairro;
    private String chapa;
    private Cidade cidade;
    private String telFixo;
    private String telCel;
    private String nubente;
    private String regime;
    private String representado;
    private String representadoNome;
    private String representadoTipo;
    private String tipoPessoa;
    private Requerente requerente;
    private String complemento;
    private String cep;


    public Requerente() {
    }

    /**
     * @return the id
     */
    
    public String getChapa(){
        return chapa;
    }
    
    public void setChapa(String chapa){
        this.chapa = chapa;
    }
    
    
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
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

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
     * @return the estadoCivil
     */
    public String getEstadoCivil() {
        return estadoCivil;
    }

    /**
     * @param estadoCivil the estadoCivil to set
     */
    public void setEstadoCivil(String estadoCivil) {
        this.estadoCivil = estadoCivil;
    }

    /**
     * @return the profissao
     */
    public String getProfissao() {
        return profissao;
    }

    /**
     * @param profissao the profissao to set
     */
    public void setProfissao(String profissao) {
        this.profissao = profissao;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the orgao
     */
    public String getOrgao() {
        return orgao;
    }

    /**
     * @param orgao the orgao to set
     */
    public void setOrgao(String orgao) {
        this.orgao = orgao;
    }

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
    public String getNacionalidade() {
        return nacionalidade;
    }

    /**
     * @param nacionalidade the nacionalidade to set
     */
    public void setNacionalidade(String nacionalidade) {
        this.nacionalidade = nacionalidade;
    }

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
    public String getNubente() {
        return nubente;
    }

    /**
     * @param nubente the nubente to set
     */
    public void setNubente(String nubente) {
        this.nubente = nubente;
    }

    /**
     * @return the regime
     */
    public String getRegime() {
        return regime;
    }

    /**
     * @param regime the regime to set
     */
    public void setRegime(String regime) {
        this.regime = regime;
    }

    /**
     * @return the representado
     */
    public String getRepresentado() {
        return representado;
    }

    /**
     * @param representado the representado to set
     */
    public void setRepresentado(String representado) {
        this.representado = representado;
    }

    /**
     * @return the representadoNome
     */
    public String getRepresentadoNome() {
        return representadoNome;
    }

    /**
     * @param representadoNome the representadoNome to set
     */
    public void setRepresentadoNome(String representadoNome) {
        this.representadoNome = representadoNome;
    }

    /**
     * @return the representadoNome
     */
   

    
    /**
     * @return the representadoTipo
     */
    public String getRepresentadoTipo() {
        return representadoTipo;
    }

    /**
     * @param representadoTipo the representadoTipo to set
     */
    public void setRepresentadoTipo(String representadoTipo) {
        this.representadoTipo = representadoTipo;
    }

    /**
     * @return the tipoPessoa
     */
    public String getTipoPessoa() {
        return tipoPessoa;
    }

    /**
     * @param tipoPessoa the tipoPessoa to set
     */
    public void setTipoPessoa(String tipoPessoa) {
        this.tipoPessoa = tipoPessoa;
    }

    /**
     * @return the razaoSocial
     */
   

    /**
     * @return the cnpj
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
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the id_empresa
     */


    /**
     * @return the email
     */
}
