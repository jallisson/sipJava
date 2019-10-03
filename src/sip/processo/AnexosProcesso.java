/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.processo;

import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Date;



/**
 *
 * @author T2Ti
 */
public class AnexosProcesso {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private Processo processo;
    private String nomeArquivo;
    private String caminhoArquivo;
    private String origemArquivo;
    private Usuario usuario;
    private String descricaoAnexo;

   


    public AnexosProcesso() {
    }

    /**
     * @return the nomeArquivo
     */
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    /**
     * @param nomeArquivo the nomeArquivo to set
     */
    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    /**
     * @return the caminhoArquivo
     */
    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    /**
     * @param caminhoArquivo the caminhoArquivo to set
     */
    public void setCaminhoArquivo(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
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
     * @return the origemArquivo
     */
    public String getOrigemArquivo() {
        return origemArquivo;
    }

    /**
     * @param origemArquivo the origemArquivo to set
     */
    public void setOrigemArquivo(String origemArquivo) {
        this.origemArquivo = origemArquivo;
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
     * @return the descricaoAnexo
     */
    public String getDescricaoAnexo() {
        return descricaoAnexo;
    }

    /**
     * @param descricaoAnexo the descricaoAnexo to set
     */
    public void setDescricaoAnexo(String descricaoAnexo) {
        this.descricaoAnexo = descricaoAnexo;
    }
    

}
