/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.juridico;

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
public class JuridicoBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaParecerJuridico = "SELECT pjuridico.*, jurid.* FROM juridico jurid LEFT JOIN parecer_juridico pjuridico ON jurid.id  = pjuridico.id_juridico WHERE pjuridico.id_juridico = ? GROUP BY pjuridico.id";
    private String consultaJuridico = "SELECT jurid.*, dist.id, usu.nome, pro.*, req.nome FROM juridico jurid JOIN distribuicao dist ON jurid.id_distribuicao = dist.id JOIN usuario usu ON jurid.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id ORDER BY jurid.id DESC";
    private String consultaJuridicoNome = "SELECT jurid.*, dist.id, usu.nome, pro.*, req.nome FROM juridico jurid JOIN distribuicao dist ON jurid.id_distribuicao = dist.id JOIN usuario usu ON jurid.id_usuario = usu.id JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id  where pro.num_processo like ? or req.nome like ? ORDER BY jurid.id DESC";
    private String incluiJuridico = "insert into juridico (id_usuario, id_distribuicao, data_juridico, tramitou_gabinete, tramitou_emissao) values(?, ?, ?, ?, ?)";
    private String alteraJuridico = "update juridico set data_recebimento = ? where juridico.id = ?";
    private String alteraJurTramiGabinete= "update juridico set tramitou_gabinete = ? where juridico.id = ?";   
    private String alteraJurTramiEmissao= "update juridico set tramitou_emissao = ? where juridico.id = ?";
    private String excluiJuridico = "delete from juridico where juridico.id = ?";
    


    public List<Juridico> consultaJuridico() {
        List<Juridico> listAnalise = new ArrayList<>();
        Juridico juridico;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaJuridico);
            rs = ps.executeQuery();
            while (rs.next()) {
                juridico = new Juridico();
                juridico.setId(rs.getInt("jurid.id"));
                juridico.setDataJuridico(rs.getDate("jurid.data_juridico"));             
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                juridico.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                juridico.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                juridico.setDistribuicao(distribuicao);
                                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                juridico.setUsuario(usuario);
                
                listAnalise.add(juridico);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnalise;
    }

    public List<Juridico> consultaJuridicoNome (String numero, String nome) {
        List<Juridico> listaAnalise = new ArrayList<>();
        Juridico juridico;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaJuridicoNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                juridico = new Juridico();
                juridico.setId(rs.getInt("anali.id"));
                juridico.setDataJuridico(rs.getDate("anali.data_juridico"));           
                
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                juridico.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                juridico.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
              
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                juridico.setUsuario(usuario);
                
                listaAnalise.add(juridico);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAnalise;
    }
    
        public List<Juridico> consultaParecerJuridico() {
        List<Juridico> listAnalise = new ArrayList<>();
        Juridico juridico;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaJuridico);
            rs = ps.executeQuery();
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                juridico = new Juridico();
                juridico.setId(rs.getInt("jurid.id"));
                juridico.setDataJuridico(rs.getDate("jurid.data_juridico"));             
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                juridico.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                juridico.setProcesso(processo);
                
                Distribuicao distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                juridico.setDistribuicao(distribuicao);
                                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                juridico.setUsuario(usuario);
                
                ps = con.prepareStatement(consultaParecerJuridico);
                ps.setInt(1, juridico.getId());
                
                rs1 = ps.executeQuery();

                List<ParecerJuridico> listParecerJuridico = new ArrayList<>();
                while (rs1.next()) {
                    ParecerJuridico parecerJuridico = new ParecerJuridico();

                    parecerJuridico.setId(rs1.getInt("pjuridico.id"));
                    parecerJuridico.setParecer(rs1.getString("pjuridico.parecer"));
                        
                     //Usuario usu= new Usuario();
                     //usu.setNome(rs1.getString("usu.nome"));
                     //usu.setSetor(rs1.getString("usu.setor"));
                                         
                     //parecerAnalise.setUsuario(usu);
                    
                    listParecerJuridico.add(parecerJuridico);

                }
                juridico.setListParecerJuridico(listParecerJuridico);

                listAnalise.add(juridico);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAnalise;
    }
        
    public boolean incluiJuridico(Juridico juridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiJuridico);

            ps.setInt(1, juridico.getUsuario().getId());
            ps.setInt(2, juridico.getDistribuicao().getId());
            ps.setDate(3, juridico.getDataJuridico());
            ps.setString(4, juridico.getTramitouGabinete());
            ps.setString(5, juridico.getTramitouEmissao());
           
            
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
    
    public boolean alteraJuridico(Juridico juridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraJuridico);

            ps.setDate(1, juridico.getDataJuridico());
                       
            ps.setInt(5, juridico.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraJurTramiGabinete(Juridico juridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraJurTramiGabinete);

            ps.setString(1, juridico.getTramitouGabinete());
            ps.setInt(2, juridico.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean alteraJurTramiEmissao(Juridico juridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraJurTramiEmissao);

            ps.setString(1, juridico.getTramitouEmissao());
            ps.setInt(2, juridico.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiJuridico(Juridico juridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiJuridico);

            ps.setInt(1, juridico.getId());

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
