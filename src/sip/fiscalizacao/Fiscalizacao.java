/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.fiscalizacao;

import java.sql.Date;
import java.util.List;
import sip.analista.Analista;
import sip.denuncia.Denuncia;
import sip.distribuicao.Distribuicao;
import sip.pessoa.Pessoa;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class Fiscalizacao {



   // private static final long serialVersionUID = 1L;

    private Usuario usuario;
    private Integer id;
    private Distribuicao distribuicao;
    private Date dataFiscalizacao;
    private String ano;
    //para consulta
    private Requerente requerente;
    private Processo processo;
    private Analista analista;
    private List<ParecerFiscalizacao> listParecerFiscalizacao;
    private String concluido;
    private Pessoa pessoa;
    private Denuncia denuncia;


    public Fiscalizacao() {
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
    public Date getDataFiscalizacao() {
        return dataFiscalizacao;
    }

    /**
     * @param dataLote the dataLote to set
     */
    public void setDataFiscalizacao(Date dataLote) {
        this.dataFiscalizacao = dataLote;
    }

    /**
     * @return the mesAno
     */
    public String getAno() {
        return ano;
    }

    /**
     * @param mesAno the mesAno to set
     */
    public void setAno(String mesAno) {
        this.ano = mesAno;
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
     * @return the analista
     */
    public Analista getAnalista() {
        return analista;
    }

    /**
     * @param analista the analista to set
     */
    public void setAnalista(Analista analista) {
        this.analista = analista;
    }

    /**
     * @return the listParecerAnalise
     */
    public List<ParecerFiscalizacao> getListParecerFiscalizacao() {
        return listParecerFiscalizacao;
    }

    /**
     * @param listParecerAnalise the listParecerAnalise to set
     */
    public void setListParecerFiscalizacao(List<ParecerFiscalizacao> listParecerAnalise) {
        this.listParecerFiscalizacao = listParecerAnalise;
    }

    /**
     * @return the nomeArquivo1
     */

        /**
     * @return the concluido
     */
    public String getConcluido() {
        return concluido;
    }

    /**
     * @param concluido the concluido to set
     */
    public void setConcluido(String concluido) {
        this.concluido = concluido;
    }

    /**
     * @return the pessoa
     */
    public Pessoa getPessoa() {
        return pessoa;
    }

    /**
     * @return the denuncia
     */
    public Denuncia getDenuncia() {
        return denuncia;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    /**
     * @param denuncia the denuncia to set
     */
    public void setDenuncia(Denuncia denuncia) {
        this.denuncia = denuncia;
    }
    
}
