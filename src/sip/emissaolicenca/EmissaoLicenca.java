/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.emissaolicenca;

import sip.bairro.Bairro;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;
import java.sql.Date;
import sip.distribuicao.Distribuicao;
import sip.juridico.Juridico;



/**
 *
 * @author T2Ti
 */
public class EmissaoLicenca {

    private static final long serialVersionUID = 1L;

    private Integer id;
    //VARIAVEI LOCAIS
  
    private Date dataEmissao;
    private String mesAno;
    private Double areaTerreno;
    private Double areaConstruida;
    private String atividade;
    private String horaInicial;
    private String horaFinal;
    private String emitido;
    private Date dataValidade;
    
    //OBJETOS
    private Requerente requerente;
    private Juridico juridico;
    private Distribuicao distribuicao;
    private Usuario usuario;
    private Processo processo;


    public EmissaoLicenca() {
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
    public Date getDataEmissao() {
        return dataEmissao;
    }

    /**
     * @param dataProcesso the dataProcesso to set
     */
    public void setDataEmissao(Date dataImpressao) {
        this.dataEmissao = dataImpressao;
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
    public Double getAreaTerreno() {
        return areaTerreno;
    }

    /**
     * @param areaTerreno the areaTerreno to set
     */
    public void setAreaTerreno(Double areaTerreno) {
        this.areaTerreno = areaTerreno;
    }

    /**
     * @return the areConstruida
     */
    public Double getAreaConstruida() {
        return areaConstruida;
    }

    /**
     * @param areConstruida the areConstruida to set
     */
    public void setAreaConstruida(Double areaConstruida) {
        this.areaConstruida = areaConstruida;
    }

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
    public String getAtividade() {
        return atividade;
    }

    /**
     * @param atividade the atividade to set
     */
    public void setAtividade(String atividade) {
        this.atividade = atividade;
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
    public Date getDataValidade() {
        return dataValidade;
    }

    /**
     * @param dataValidade the dataValidade to set
     */
    public void setDataValidade(Date dataValidade) {
        this.dataValidade = dataValidade;
    }

    /**
     * @return the controleForm
     */




    /**
     * @return the email
     */
}
