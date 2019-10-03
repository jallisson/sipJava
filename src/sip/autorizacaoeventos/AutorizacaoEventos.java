/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.autorizacaoeventos;

import sip.bairro.Bairro;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;
import java.sql.Date;
import sip.distribuicao.Distribuicao;
import sip.juridico.Juridico;
import sip.localevento.LocalEvento;
import sip.logradouro.Logradouro;
import sip.tipoevento.TipoEvento;



/**
 *
 * @author T2Ti
 */
public class AutorizacaoEventos {

    /**
     * @return the localEvento
     */
    public LocalEvento getLocalEvento() {
        return localEvento;
    }

    /**
     * @param localEvento the localEvento to set
     */
    public void setLocalEvento(LocalEvento localEvento) {
        this.localEvento = localEvento;
    }

    private static final long serialVersionUID = 1L;
    //VARIAVEI LOCAIS
    private Integer id;
    private Date dataEvento;
    private String mesAno;
    private String nomeEvento;
    private String horaInicial;
    private String horaFinal;
    private String emitido;
    private Integer estimativa;
    private String descricaoArea;
    
    //OBJETOS
    private Requerente requerente;
    private Juridico juridico;
    private Distribuicao distribuicao;
    private Usuario usuario;
    private Processo processo;
    private Bairro bairro;
    private TipoEvento tipoEvento;
    private Logradouro logradouro;
    private LocalEvento localEvento;


    public AutorizacaoEventos() {
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
    public Date getDataEvento() {
        return dataEvento;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataEvento(Date dataEvento) {
        this.dataEvento = dataEvento;
    }

    /**
     * @return the mesAno
     */
    public String getMesAno() {
        return mesAno;
    }

    /**
     * @param mesAno the mesAno to set
     */
    public void setMesAno(String mesAno) {
        this.mesAno = mesAno;
    }

    /**
     * @return the digitador
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * @param digitador the digitador to set
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
     * @return the imovel
     */


    /**
     * @return the bairro
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
     * @return the juridico
     */
    public Juridico getJuridico() {
        return juridico;
    }

    /**
     * @param juridico the juridico to set
     */
    public void setJuridico(Juridico juridico) {
        this.juridico = juridico;
    }

    /**
     * @return the areaTerreno
     */


    /**
     * @return the horaFuncionamento
     */
    public String getHoraInicial() {
        return horaInicial;
    }

    /**
     * @param horaFuncionamento the horaFuncionamento to set
     */
    public void setHoraInicial(String horaInicial) {
        this.horaInicial = horaInicial;
    }

    /**
     * @return the atividade
     */
    public String getNomeEvento() {
        return nomeEvento;
    }

    /**
     * @param atividade the atividade to set
     */
    public void setNomeEvento(String atividade) {
        this.nomeEvento = atividade;
    }

    /**
     * @return the horaFinal
     */
    public String getHoraFinal() {
        return horaFinal;
    }

    /**
     * @param horaFinal the horaFinal to set
     */
    public void setHoraFinal(String horaFinal) {
        this.horaFinal = horaFinal;
    }

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
     * @return the emitido
     */
    public String getEmitido() {
        return emitido;
    }

    /**
     * @param emitido the emitido to set
     */
    public void setEmitido(String emitido) {
        this.emitido = emitido;
    }

    /**
     * @return the dataValidade
     */

    /**
     * @return the estimativa
     */
    public Integer getEstimativa() {
        return estimativa;
    }

    /**
     * @param estimativa the estimativa to set
     */
    public void setEstimativa(Integer estimativa) {
        this.estimativa = estimativa;
    }

    /**
     * @return the descricaoArea
     */
    public String getDescricaoArea() {
        return descricaoArea;
    }

    /**
     * @param descricaoArea the descricaoArea to set
     */
    public void setDescricaoArea(String descricaoArea) {
        this.descricaoArea = descricaoArea;
    }

    /**
     * @return the bairro
     */
    public Bairro getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(Bairro bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the tipoEvento
     */
    public TipoEvento getTipoEvento() {
        return tipoEvento;
    }

    /**
     * @param tipoEvento the tipoEvento to set
     */
    public void setTipoEvento(TipoEvento tipoEvento) {
        this.tipoEvento = tipoEvento;
    }

    /**
     * @return the logradouro
     */
    public Logradouro getLogradouro() {
        return logradouro;
    }

    /**
     * @param logradouro the logradouro to set
     */
    public void setLogradouro(Logradouro logradouro) {
        this.logradouro = logradouro;
    }

    /**
     * @return the controleForm
     */




    /**
     * @return the email
     */
}
