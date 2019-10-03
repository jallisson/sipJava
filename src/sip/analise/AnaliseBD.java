/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.analise;

import sip.acessobd.AcessoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sip.analista.Analista;
import sip.distribuicao.Distribuicao;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class AnaliseBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaParecerAnalise = "SELECT panalise.*, analise.* FROM analise analise LEFT JOIN parecer_analise panalise ON  analise.id  = panalise.id_analise WHERE panalise.id_analise = ? GROUP BY panalise.id";
    private String consultaAnalise = "select anali.*, dist.id, usu.nome, pro.*, ana.id, ana.nome, ana.qtde_saida, req.nome FROM analise anali JOIN distribuicao dist ON anali.id_distribuicao = dist.id JOIN usuario usu ON anali.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id JOIN analista ana ON dist.id_analista = ana.id JOIN requerente req ON pro.id_requerente = req.id order by anali.id desc";
    private String consultaAnaliseNome = "select anali.*, dist.id, usu.nome, pro.*, ana.id, ana.nome, req.* FROM analise anali JOIN distribuicao dist ON anali.id_distribuicao = dist.id JOIN usuario usu ON anali.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id JOIN analista ana ON dist.id_analista = ana.id JOIN requerente req ON pro.id_requerente = req.id order by anali.id desc where pro.num_processo like ? or req.nome like ?";
    private String incluiAnalise = "insert into analise (id_usuario, id_distribuicao, data_analise, ano, concluido) values(?, ?, ?, ?, ?)";
    private String alteraAnalise = "update analise set data_analise = ?, ano = ?, nome_arquivo1 = ?, caminho_arquivo1 = ? where analise.id = ?";
    private String alteraAnaliseConcluido = "update analise set concluido = ? where analise.id = ?";
    private String excluiAnalise = "delete from analise where analise.id = ?";
    


    public List<Analise> consultaAnalise() {
        List<Analise> listAnalise = new ArrayList<>();
        Analise analise;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAnalise);
            rs = ps.executeQuery();
            while (rs.next()) {
                analise = new Analise();
                analise.setId(rs.getInt("anali.id"));
                analise.setDataAnalise(rs.getDate("anali.data_analise"));
                analise.setAno(rs.getString("anali.ano"));
                analise.setConcluido(rs.getString("anali.concluido"));
                //analise.setNomeArquivo1(rs.getString("anali.nome_arquivo1"));
                //analise.setCaminhoArquivo1(rs.getString("anali.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                analise.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                analise.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                analise.setDistribuicao(distribuicao);
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));
                analista.setQtdeSaida(rs.getInt("ana.qtde_saida"));
                analise.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                analise.setUsuario(usuario);
                
                listAnalise.add(analise);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnalise;
    }

    public List<Analise> consultaAnaliseNome (String numero, String nome) {
        List<Analise> listaAnalise = new ArrayList<>();
        Analise analise;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAnaliseNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                analise = new Analise();
                analise.setId(rs.getInt("anali.id"));
                analise.setDataAnalise(rs.getDate("anali.data_analise"));
                analise.setAno(rs.getString("anali.ano"));
                analise.setConcluido(rs.getString("anali.concluido"));
                //analise.setNomeArquivo1(rs.getString("anali.nome_arquivo1"));
                //analise.setCaminhoArquivo1(rs.getString("anali.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                analise.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                analise.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));            
                analise.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                analise.setUsuario(usuario);
                
                listaAnalise.add(analise);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAnalise;
    }
    
        public List<Analise> consultaParecerAnalise() {
        List<Analise> listAnalise = new ArrayList<>();
        Analise analise;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAnalise);
            rs = ps.executeQuery();
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                analise = new Analise();
                analise.setId(rs.getInt("anali.id"));
                analise.setDataAnalise(rs.getDate("anali.data_analise"));
                analise.setAno(rs.getString("anali.ano"));
                 analise.setConcluido(rs.getString("anali.concluido"));
                //analise.setNomeArquivo1(rs.getString("anali.nome_arquivo1"));
                //analise.setCaminhoArquivo1(rs.getString("anali.caminho_arquivo1"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                analise.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                analise.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));            
                analise.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                analise.setUsuario(usuario);

                ps = con.prepareStatement(consultaParecerAnalise);
                ps.setInt(1, analise.getId());
                
                rs1 = ps.executeQuery();

                List<ParecerAnalise> listParecerAnalise = new ArrayList<>();
                while (rs1.next()) {
                    ParecerAnalise parecerAnalise = new ParecerAnalise();

                    parecerAnalise.setId(rs1.getInt("panalise.id"));
                    parecerAnalise.setTipo(rs1.getString("panalise.tipo"));
                    parecerAnalise.setAnexoIncluso(rs1.getString("panalise.anexo_incluso"));
                        
                     //Usuario usu= new Usuario();
                     //usu.setNome(rs1.getString("usu.nome"));
                     //usu.setSetor(rs1.getString("usu.setor"));
                                         
                     //parecerAnalise.setUsuario(usu);
                    
                    listParecerAnalise.add(parecerAnalise);

                }
                analise.setListParecerAnalise(listParecerAnalise);

                listAnalise.add(analise);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnalise;
    }
        
    public boolean incluiAnalise(Analise analise) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiAnalise);

            ps.setInt(1, analise.getUsuario().getId());
            ps.setInt(2, analise.getDistribuicao().getId());
            ps.setDate(3, analise.getDataAnalise());
            ps.setString(4, analise.getAno());
            ps.setString(5, analise.getConcluido());
            
           
            
            /*
            if(analise.getNomeArquivo1() == null){
                ps.setNull(5, 1);
            }else{
                ps.setString(5, analise.getNomeArquivo1());
            }
           
            if(analise.getCaminhoArquivo1() == null){
                ps.setNull(6, 1);
            }else{
                ps.setString(6, analise.getCaminhoArquivo1());
            }*/
                        
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraAnalise(Analise analise) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAnalise);

            ps.setDate(1, analise.getDataAnalise());
            ps.setString(2, analise.getAno());
            
            /*
            if(analise.getNomeArquivo1() == null){
                ps.setNull(3, 1);
            }else{
                ps.setString(3, analise.getNomeArquivo1());
            }
           
            if(analise.getCaminhoArquivo1() == null){
                ps.setNull(4, 1);
            }else{
                ps.setString(4, analise.getCaminhoArquivo1());
            }*/
           
            ps.setInt(5, analise.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraAnaliseConcluido(Analise analise) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAnaliseConcluido);

            ps.setString(1, analise.getConcluido());
            ps.setInt(2, analise.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiAnalise(Analise analise) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiAnalise);

            ps.setInt(1, analise.getId());

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
