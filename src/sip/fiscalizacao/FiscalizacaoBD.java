/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.fiscalizacao;

import sip.fiscalizacao.*;
import sip.acessobd.AcessoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
public class FiscalizacaoBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();//"SELECT panalise.*, analise.* FROM analise analise LEFT JOIN parecer_analise panalise ON  analise.id  = panalise.id_analise WHERE panalise.id_analise = ? GROUP BY panalise.id";
    private String consultaParecerFiscalizacao = "SELECT pfiscalizacao.*, fiscalizacao.* FROM fiscalizacao fiscalizacao LEFT JOIN parecer_fiscalizacao pfiscalizacao ON  fiscalizacao.id  = pfiscalizacao.id_fiscalizacao WHERE pfiscalizacao.id_fiscalizacao = ? GROUP BY pfiscalizacao.id";
    
    private String consultaFiscalizacao = "select fiscaliz.*, dist.id, usu.*, pro.*, ana.id, ana.nome, ana.qtde_saida, ana.qtde_entrada, req.nome, pdenunciado.nome, denuncia.* FROM fiscalizacao fiscaliz JOIN distribuicao dist ON fiscaliz.id_distribuicao = dist.id JOIN usuario usu ON fiscaliz.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id  LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id JOIN analista ana ON dist.id_analista = ana.id LEFT JOIN requerente req ON pro.id_requerente = req.id where ana.id_usuario = ? order by fiscaliz.id desc";
    private String consultaFiscalizacaoParecer = "select fiscaliz.*, dist.id, usu.*, pro.*, ana.id, ana.nome, ana.qtde_saida, req.nome, pdenunciado.nome, denuncia.* FROM fiscalizacao fiscaliz JOIN distribuicao dist ON fiscaliz.id_distribuicao = dist.id JOIN usuario usu ON fiscaliz.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id  LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id JOIN analista ana ON dist.id_analista = ana.id LEFT JOIN requerente req ON pro.id_requerente = req.id order by fiscaliz.id desc";
    
    private String consultaFiscalizacaoNome = "select fiscaliz.*, dist.id, usu.*, pro.*, ana.id, ana.nome, ana.qtde_saida, req.nome, pdenunciado.nome, denuncia.* FROM fiscalizacao fiscaliz JOIN distribuicao dist ON fiscaliz.id_distribuicao = dist.id JOIN usuario usu ON fiscaliz.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id  LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id JOIN analista ana ON dist.id_analista = ana.id LEFT JOIN requerente req ON pro.id_requerente = req.id where ana.id_usuario = ? and pro.num_processo like ? or req.nome like ? or pdenunciado.nome like ?";
    private String incluiFiscalizacao = "insert into fiscalizacao (id_usuario, id_distribuicao, data_fiscalizacao, ano, concluido) values(?, ?, ?, ?, ?)";
    private String alteraFiscalizacao = "update fiscalizacao set data_fiscalizacao = ?, ano = ?, nome_arquivo1 = ?, caminho_arquivo1 = ? where fiscalizacao.id = ?";
    private String alteraFiscalizacaoConcluido = "update fiscalizacao set concluido = ? where fiscalizacao.id = ?";
    private String excluiFiscalizacao = "delete from fiscalizacao where fiscalizacao.id = ?";
    


    public List<Fiscalizacao> consultaFiscalizacao(Integer id) {
        List<Fiscalizacao> listFiscalizacao = new ArrayList<>();
        Fiscalizacao fiscalizacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaFiscalizacao);
            ps.setInt(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                fiscalizacao = new Fiscalizacao();
                fiscalizacao.setId(rs.getInt("fiscaliz.id"));
                fiscalizacao.setDataFiscalizacao(rs.getDate("fiscaliz.data_fiscalizacao"));
                fiscalizacao.setAno(rs.getString("fiscaliz.ano"));
                fiscalizacao.setConcluido(rs.getString("fiscaliz.concluido"));
                //fiscalizacao.setNomeArquivo1(rs.getString("fiscaliz.nome_arquivo1"));
                //fiscalizacao.setCaminhoArquivo1(rs.getString("fiscaliz.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                fiscalizacao.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                fiscalizacao.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                fiscalizacao.setDistribuicao(distribuicao);
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));
                analista.setQtdeSaida(rs.getInt("ana.qtde_saida"));
                analista.setQtdeEntrada(rs.getInt("ana.qtde_entrada"));
                fiscalizacao.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                usuario.setId(rs.getInt("usu.id"));
                fiscalizacao.setUsuario(usuario);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                fiscalizacao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                processo.setDenuncia(denuncia);
                
                listFiscalizacao.add(fiscalizacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFiscalizacao;
    }

    public List<Fiscalizacao> consultaFiscalizacaoNome (String numero, String nome, Integer id) {
        List<Fiscalizacao> listaFiscalizacao = new ArrayList<>();
        Fiscalizacao fiscalizacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaFiscalizacaoNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setInt(1, id);
            ps.setString(2, numero);
            ps.setString(3, nome);
            ps.setString(4, nome);
            
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                fiscalizacao = new Fiscalizacao();
                fiscalizacao.setId(rs.getInt("fiscaliz.id"));
                fiscalizacao.setDataFiscalizacao(rs.getDate("fiscaliz.data_fiscalizacao"));
                fiscalizacao.setAno(rs.getString("fiscaliz.ano"));
                fiscalizacao.setConcluido(rs.getString("fiscaliz.concluido"));
                //fiscalizacao.setNomeArquivo1(rs.getString("fiscaliz.nome_arquivo1"));
                //fiscalizacao.setCaminhoArquivo1(rs.getString("fiscaliz.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                fiscalizacao.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                fiscalizacao.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));            
                fiscalizacao.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                 usuario.setId(rs.getInt("usu.id"));
                fiscalizacao.setUsuario(usuario);
                
                 Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                fiscalizacao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                processo.setDenuncia(denuncia);
                
                listaFiscalizacao.add(fiscalizacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaFiscalizacao;
    }
    
        public List<Fiscalizacao> consultaParecerFiscalizacao(Integer id) {
        List<Fiscalizacao> listFiscalizacao = new ArrayList<>();
        Fiscalizacao fiscalizacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaFiscalizacao);
             ps.setInt(1, id);
            rs = ps.executeQuery();
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                fiscalizacao = new Fiscalizacao();
                fiscalizacao.setId(rs.getInt("fiscaliz.id"));
                fiscalizacao.setDataFiscalizacao(rs.getDate("fiscaliz.data_fiscalizacao"));
                fiscalizacao.setAno(rs.getString("fiscaliz.ano"));
                 fiscalizacao.setConcluido(rs.getString("fiscaliz.concluido"));
                //fiscalizacao.setNomeArquivo1(rs.getString("fiscaliz.nome_arquivo1"));
                //fiscalizacao.setCaminhoArquivo1(rs.getString("fiscaliz.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                fiscalizacao.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                fiscalizacao.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));            
                fiscalizacao.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                fiscalizacao.setUsuario(usuario);

                ps = con.prepareStatement(consultaParecerFiscalizacao);
                ps.setInt(1, fiscalizacao.getId());
                
                rs1 = ps.executeQuery();

                List<ParecerFiscalizacao> listParecerFiscalizacao = new ArrayList<>();
                while (rs1.next()) {
                    ParecerFiscalizacao parecerFiscalizacao = new ParecerFiscalizacao();

                    parecerFiscalizacao.setId(rs1.getInt("pfiscalizacao.id"));
                    parecerFiscalizacao.setTipo(rs1.getString("pfiscalizacao.tipo"));
                    parecerFiscalizacao.setAnexoIncluso(rs1.getString("pfiscalizacao.anexo_incluso"));
                        
                     //Usuario usu= new Usuario();
                     //usu.setNome(rs1.getString("usu.nome"));
                     //usu.setSetor(rs1.getString("usu.setor"));
                                         
                     //parecerFiscalizacao.setUsuario(usu);
                    
                    listParecerFiscalizacao.add(parecerFiscalizacao);

                }
                fiscalizacao.setListParecerFiscalizacao(listParecerFiscalizacao);

                listFiscalizacao.add(fiscalizacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listFiscalizacao;
    }
        
    public boolean incluiFiscalizacao(Fiscalizacao fiscalizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiFiscalizacao);

            ps.setInt(1, fiscalizacao.getUsuario().getId());
            ps.setInt(2, fiscalizacao.getDistribuicao().getId());
            ps.setDate(3, fiscalizacao.getDataFiscalizacao());
            ps.setString(4, fiscalizacao.getAno());
            ps.setString(5, fiscalizacao.getConcluido());
            
           
            
            /*
            if(fiscalizacao.getNomeArquivo1() == null){
                ps.setNull(5, 1);
            }else{
                ps.setString(5, fiscalizacao.getNomeArquivo1());
            }
           
            if(fiscalizacao.getCaminhoArquivo1() == null){
                ps.setNull(6, 1);
            }else{
                ps.setString(6, fiscalizacao.getCaminhoArquivo1());
            }*/
                        
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraFiscalizacao(Fiscalizacao fiscalizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraFiscalizacao);

            ps.setDate(1, fiscalizacao.getDataFiscalizacao());
            ps.setString(2, fiscalizacao.getAno());
            
            /*
            if(fiscalizacao.getNomeArquivo1() == null){
                ps.setNull(3, 1);
            }else{
                ps.setString(3, fiscalizacao.getNomeArquivo1());
            }
           
            if(fiscalizacao.getCaminhoArquivo1() == null){
                ps.setNull(4, 1);
            }else{
                ps.setString(4, fiscalizacao.getCaminhoArquivo1());
            }*/
           
            ps.setInt(5, fiscalizacao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraFiscalizacaoConcluido(Fiscalizacao fiscalizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraFiscalizacaoConcluido);

            ps.setString(1, fiscalizacao.getConcluido());
            ps.setInt(2, fiscalizacao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiFiscalizacao(Fiscalizacao fiscalizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiFiscalizacao);

            ps.setInt(1, fiscalizacao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
     
      public boolean testaConexao() {
        try {
            con = acessoBD.conectar();
            
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
}
