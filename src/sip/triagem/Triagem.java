/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.triagem;

import sip.gabinete.*;
import sip.processo.Processo;
import sip.requerente.Requerente;
import java.sql.Date;
import sip.juridico.Juridico;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;
import sip.usuario.Usuario;



/**
 *
 * @author T2Ti
 */
public class Triagem {

    /**
     * @return the pDenunciado
     */
    public Pessoa getpDenunciado() {
        return pDenunciado;
    }

    /**
     * @param pDenunciado the pDenunciado to set
     */
    public void setpDenunciado(Pessoa pDenunciado) {
        this.pDenunciado = pDenunciado;
    }

   // private static final long serialVersionUID = 1L;

    private Integer id;
    private Denuncia denuncia;
    private Date dataTriagem;
    private String resumo;
    
    //PARA CONSULTAS
    private Usuario usuario;
    private Pessoa pDenunciado;
    
    



    public Triagem() {
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
     * @return the analise
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param denuncia the analise to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }

    /**
     * @return the dataParecer
     */
    public Date getDataTriagem() {
        return dataTriagem;
    }

    /**
     * @param dataTriagem the dataParecer to set
     */
    public void setDataTriagem(Date dataTriagem) {
        this.dataTriagem = dataTriagem;
    }



    /**
     * @return the controle
     */

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
     * @return the tramitouEmissao
     */
    public String getResumo() {
        return resumo;
    }

    /**
     * @param resumo the tramitouEmissao to set
     */
    public void setResumo(String resumo) {
        this.resumo = resumo;
    }

   

}
