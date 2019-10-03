/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.gabinete;

import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sip.juridico.Juridico;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class GabineteBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    //private String consultaLaudoImovel = "select *from laudo_imovel";
    private String consultaGabinete = "SELECT pro.*, req.*, juridico.*, usu.nome, gabi.*, dist.id FROM gabinete gabi JOIN juridico juridico ON gabi.id_juridico = juridico.id JOIN distribuicao dist ON juridico.id_distribuicao = dist.id JOIN usuario usu ON gabi.id_usuario = usu.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id ORDER BY gabi.id";
    private String consultaGabineteProcesso = "SELECT pro.*, req.*, juridico.*, usu.nome, gabi.*, dist.id FROM gabinete gabi JOIN juridico juridico ON gabi.id_juridico = juridico.id JOIN distribuicao dist ON juridico.id_distribuicao = dist.id JOIN usuario usu ON gabi.id_usuario = usu.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id ORDER BY gabi.id where pro.num_processo like ? ORDER BY gabi.id";
    private String incluiGabinete = "insert into gabinete (id_usuario, id_juridico, data_gabinete, tramitou_juridico) values(?, ?, ?, ?)";
    private String alteraGabinete = "update gabinete set id_usuario, data_gabinete = ? where gabinete.id = ?";
    private String alteraGabiTramiJuridico= "update gabinete set tramitou_juridico = ? where gabinete.id = ?";
    private String excluiGabinete = "delete from gabinete where gabinete.id = ?";
    private String consultaUltimoId = "select LAST_INSERT_ID() as id from gabinete";
    int mostraID;


    public List<Gabinete> consultaGabinete() {
        List<Gabinete> listGabinete = new ArrayList<>();
        Gabinete gabinete;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaGabinete);
            rs = ps.executeQuery();
            while (rs.next()) {
                gabinete = new Gabinete();
                gabinete.setId(rs.getInt("gabi.id"));              
                gabinete.setDataGabinete(rs.getDate("gabi.data_gabinete"));    
                gabinete.setTramitouJuridico(rs.getString("gabi.tramitou_juridico"));
                
                Processo processo = new Processo();
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                gabinete.setProcesso(processo);
                
                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                gabinete.setJuridico(juridico);
                                        
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                gabinete.setRequerente(requerente);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                gabinete.setUsuario(usuario);

               
                listGabinete.add(gabinete);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGabinete;
    }

    public List<Gabinete> consultaGabineteNome (String Numprocesso) {
        List<Gabinete> listGabinete = new ArrayList<>();
        Gabinete gabinete;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaGabineteProcesso);
            Numprocesso = "%" + Numprocesso + "%";
            ps.setString(1, Numprocesso);
            rs = ps.executeQuery();
            while (rs.next()) {
                gabinete = new Gabinete();
                gabinete.setId(rs.getInt("gabi.id"));              
                gabinete.setDataGabinete(rs.getDate("gabi.data_gabinete"));    
                gabinete.setTramitouJuridico(rs.getString("gabi.tramitou_juridico"));
                
                Processo processo = new Processo();
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                gabinete.setProcesso(processo);
                
                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                gabinete.setJuridico(juridico);
                                        
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                gabinete.setRequerente(requerente);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                gabinete.setUsuario(usuario);

               
                listGabinete.add(gabinete);
                
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listGabinete;
    }
        
    public boolean incluiGabinete(Gabinete parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiGabinete);
            
            ps.setInt(1, parecer.getUsuario().getId());
            ps.setInt(2, parecer.getJuridico().getId());
            ps.setDate(3, parecer.getDataGabinete());
            ps.setString(4, parecer.getTramitouJuridico());
            
            ps.executeUpdate();
            
            ps = con.prepareStatement(consultaUltimoId);
            rs = ps.executeQuery();
            rs.next();
            parecer.setId(rs.getInt("id"));
            mostraID = parecer.getId();
            
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraGabinete(Gabinete parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraGabinete);

            ps.setInt(1, parecer.getUsuario().getId());
            ps.setInt(2, parecer.getJuridico().getId());
            ps.setDate(3, parecer.getDataGabinete());
            
            ps.setInt(6, parecer.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraGabiTramiJuridico(Gabinete gabinete) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraGabiTramiJuridico);

            ps.setString(1, gabinete.getTramitouJuridico());
            ps.setInt(2, gabinete.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiParecer(Gabinete parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiGabinete);

            ps.setInt(1, parecer.getId());

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
