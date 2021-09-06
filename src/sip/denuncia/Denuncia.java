/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.denuncia;

import java.sql.Date;
import java.util.List;
import sip.analista.Analista;
import sip.distribuicao.Distribuicao;
import sip.naturezaocorrencia.NaturezaOcorrencia;
import sip.pessoa.Pessoa;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class Denuncia {

    /**
     * @return the link
     */
    public String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public void setLink(String link) {
        this.link = link;
    }

    /**
     * @return the origem
     */
    public String getOrigem() {
        return origem;
    }

    /**
     * @param origem the origem to set
     */
    public void setOrigem(String origem) {
        this.origem = origem;
    }

    /**
     * @return the statusApp
     */
    public String getStatusApp() {
        return statusApp;
    }

    /**
     * @param statusApp the statusApp to set
     */
    public void setStatusApp(String statusApp) {
        this.statusApp = statusApp;
    }

    /**
     * @return the token
     */
    public String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the descricao
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * @param descricao the descricao to set
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * @return the tipoDenuncia
     */
    public String getTipoDenuncia() {
        return tipoDenuncia;
    }

    /**
     * @return the denunciado
     */
    public String getDenunciado() {
        return denunciado;
    }

    /**
     * @return the localDenuncia
     */
    public String getLocalDenuncia() {
        return localDenuncia;
    }

    /**
     * @return the dataDenuncia
     */
    public Date getDataDenuncia() {
        return dataDenuncia;
    }

    /**
     * @param tipoDenuncia the tipoDenuncia to set
     */
    public void setTipoDenuncia(String tipoDenuncia) {
        this.tipoDenuncia = tipoDenuncia;
    }

    /**
     * @param denunciado the denunciado to set
     */
    public void setDenunciado(String denunciado) {
        this.denunciado = denunciado;
    }

    /**
     * @param localDenuncia the localDenuncia to set
     */
    public void setLocalDenuncia(String localDenuncia) {
        this.localDenuncia = localDenuncia;
    }

    /**
     * @param dataDenuncia the dataDenuncia to set
     */
    public void setDataDenuncia(Date dataDenuncia) {
        this.dataDenuncia = dataDenuncia;
    }

   // private static final long serialVersionUID = 1L;

    
    private Integer id;
    private Usuario usuario;
    private Date dataRegistro;
    private String relatoOcorencia;
    private NaturezaOcorrencia naturezaOcorrencia;
    private Pessoa comunicante;
    private Pessoa PDenunciado;
    private String tipoDenuncia;
    private String denunciado;
    private String localDenuncia;
    private Date dataDenuncia;
    private String descricao;
    private String token;
    private String statusApp;
    private String origem;
    private String link;
    


    public Denuncia() {
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
     * @return the dataLote
     */
    public Date getDataRegistro() {
        return dataRegistro;
    }

    /**
     * @param dataLote the dataLote to set
     */
    public void setDataRegistro(Date dataLote) {
        this.dataRegistro = dataLote;
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
     * @return the comunicante
     */
    public Pessoa getComunicante() {
        return comunicante;
    }

    /**
     * @param comunicante the comunicante to set
     */
    public void setComunicante(Pessoa comunicante) {
        this.comunicante = comunicante;
    }

    /**
     * @return the PDenunciado
     */
    public Pessoa getPDenunciado() {
        return PDenunciado;
    }

    /**
     * @param PDenunciado the PDenunciado to set
     */
    public void setPDenunciado(Pessoa PDenunciado) {
        this.PDenunciado = PDenunciado;
    }

    /**
     * @return the relatoOcorencia
     */
    public String getRelatoOcorencia() {
        return relatoOcorencia;
    }

    /**
     * @param relatoOcorencia the relatoOcorencia to set
     */
    public void setRelatoOcorencia(String relatoOcorencia) {
        this.relatoOcorencia = relatoOcorencia;
    }

    /**
     * @return the naturezaOcorrencia
     */
    public NaturezaOcorrencia getNaturezaOcorrencia() {
        return naturezaOcorrencia;
    }

    /**
     * @param naturezaOcorrencia the naturezaOcorrencia to set
     */
    public void setNaturezaOcorrencia(NaturezaOcorrencia naturezaOcorrencia) {
        this.naturezaOcorrencia = naturezaOcorrencia;
    }
}
