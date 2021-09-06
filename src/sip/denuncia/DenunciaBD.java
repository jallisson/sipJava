/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.denuncia;

import sip.acessobd.AcessoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import sip.analista.Analista;
import sip.distribuicao.Distribuicao;
import sip.logradouro.Logradouro;
import sip.naturezaocorrencia.NaturezaOcorrencia;
import sip.pessoa.Pessoa;
import sip.processo.Processo;
import sip.requerente.Requerente;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class DenunciaBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaDenuncia = "SELECT oco.*, comunicante.*, denunciado.*, natureza.*, logra_comu.nome, logra_denun.nome, usu.nome FROM denuncia oco LEFT JOIN pessoa comunicante ON oco.id_comunicante = comunicante.id LEFT JOIN pessoa denunciado ON oco.id_denunciado = denunciado.id JOIN natureza_ocorrencia natureza ON oco.id_natureza_ocorrencia = natureza.id LEFT JOIN logradouro logra_comu ON comunicante.id_logradouro = logra_comu.id LEFT JOIN logradouro logra_denun ON denunciado.id_logradouro = logra_denun.id  JOIN usuario usu ON oco.id_usuario = usu.id ORDER BY oco.id DESC";
    private String consultaDenunciaApp = "SELECT oco.* FROM denuncia oco where oco.origem = 'App'  ORDER BY oco.id DESC";
    private String consultaDenunciaNome = "SELECT oco.*, comunicante.*, denunciado.*, natureza.*, logra_comu.nome, logra_denun.nome, usu.nome FROM denuncia oco LEFT JOIN pessoa comunicante ON oco.id_comunicante = comunicante.id LEFT JOIN pessoa denunciado ON oco.id_denunciado = denunciado.id JOIN natureza_ocorrencia natureza ON oco.id_natureza_ocorrencia = natureza.id LEFT JOIN logradouro logra_comu ON comunicante.id_logradouro = logra_comu.id LEFT JOIN logradouro logra_denun ON denunciado.id_logradouro = logra_denun.id  JOIN usuario usu ON oco.id_usuario = usu.id where natureza.nome like ? or comunicante.nome like ? or denunciado.nome like ? or logra_denun.nome like ?  ORDER BY oco.id DESC";
    private String consultaDenunciaNomeApp = "SELECT oco.* FROM denuncia oco where oco.origem = 'App' and oco.tipo_denuncia like ? or oco.denunciado like ? or oco.local_denuncia like ?  ORDER BY oco.id DESC";
    private String incluiDenuncia = "insert into denuncia (id_usuario, data_registro, id_natureza_ocorrencia, id_comunicante, id_denunciado, relato_ocorrencia, link) values(?, ?, ?, ?, ?, ?, ?)";
    private String alteraDenuncia = "update denuncia set data_registro = ?, id_natureza_ocorrencia = ?, id_comunicante = ?, id_denunciado = ?, relato_ocorrencia = ?, link = ? where denuncia.id = ?";
    private String excluiDenuncia = "delete from denuncia where denuncia.id = ?";
    private String alteraDenunciaStatus = "update denuncia set status_app = ? where denuncia.id = ?";
    


    public List<Denuncia> consultaDenuncia() {
        List<Denuncia> listDenuncia = new ArrayList<>();
        Denuncia denuncia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaDenuncia);
            rs = ps.executeQuery();
            while (rs.next()) {
                denuncia = new Denuncia();
                denuncia.setId(rs.getInt("oco.id"));
                denuncia.setDataRegistro(rs.getDate("oco.data_registro"));   
                denuncia.setRelatoOcorencia(rs.getString("oco.relato_ocorrencia"));
                denuncia.setOrigem(rs.getString("oco.origem"));
                denuncia.setStatusApp(rs.getString("oco.status_app"));
                denuncia.setLink(rs.getString("oco.link"));
                               
                
                NaturezaOcorrencia natureza = new NaturezaOcorrencia();
                natureza.setId(rs.getInt("natureza.id"));
                natureza.setNome(rs.getString("natureza.nome"));
                denuncia.setNaturezaOcorrencia(natureza);
              
                Pessoa comunicante = new Pessoa();
                comunicante.setId(rs.getInt("comunicante.id"));
                comunicante.setNome(rs.getString("comunicante.nome"));
                denuncia.setComunicante(comunicante);
                
                Logradouro lograComunicante = new Logradouro();
                lograComunicante.setNome(rs.getString("logra_comu.nome"));
                comunicante.setLogradouro(lograComunicante);
                
                Pessoa denunciado = new Pessoa();
                denunciado.setId(rs.getInt("denunciado.id"));
                denunciado.setNome(rs.getString("denunciado.nome"));
                denuncia.setPDenunciado(denunciado);
                
                Logradouro lograDenunciado = new Logradouro();
                lograDenunciado.setNome(rs.getString("logra_denun.nome"));
                denunciado.setLogradouro(lograDenunciado);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                denuncia.setUsuario(usuario);
                
                listDenuncia.add(denuncia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDenuncia;
    }

    public List<Denuncia> consultaDenunciaApp() {
        List<Denuncia> listDenuncia = new ArrayList<>();
        Denuncia denuncia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaDenunciaApp);
            rs = ps.executeQuery();
            while (rs.next()) {
                denuncia = new Denuncia();
                denuncia.setId(rs.getInt("oco.id"));
                denuncia.setTipoDenuncia(rs.getString("oco.tipo_denuncia"));
                denuncia.setDenunciado(rs.getString("oco.denunciado"));
                denuncia.setLocalDenuncia(rs.getString("oco.local_denuncia"));
                denuncia.setDataDenuncia(rs.getDate("oco.data_denuncia"));
                denuncia.setDescricao(rs.getString("oco.descricao"));
                denuncia.setToken(rs.getString("oco.token_gcm"));
                denuncia.setStatusApp(rs.getString("oco.status_app"));
                denuncia.setOrigem(rs.getString("oco.origem"));
                
                listDenuncia.add(denuncia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDenuncia;
    }
    
    public List<Denuncia> consultaDenunciaNome (String nome) {
        List<Denuncia> listDenuncia = new ArrayList<>();
        Denuncia denuncia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaDenunciaNome);
            nome = "%" + nome + "%";
            //nomeDois = "%" + nomeDois + "%";
            ps.setString(1, nome);
            ps.setString(2, nome);
            ps.setString(3, nome);
            ps.setString(4, nome);
                     
            rs = ps.executeQuery();
            while (rs.next()) {
                 denuncia = new Denuncia();
                denuncia.setId(rs.getInt("oco.id"));
                denuncia.setDataRegistro(rs.getDate("oco.data_registro"));   
                denuncia.setRelatoOcorencia(rs.getString("oco.relato_ocorrencia"));
                denuncia.setOrigem(rs.getString("oco.origem"));
                denuncia.setStatusApp(rs.getString("oco.status_app"));
                               
                
                NaturezaOcorrencia natureza = new NaturezaOcorrencia();
                natureza.setId(rs.getInt("natureza.id"));
                natureza.setNome(rs.getString("natureza.nome"));
                denuncia.setNaturezaOcorrencia(natureza);
              
                Pessoa comunicante = new Pessoa();
                comunicante.setId(rs.getInt("comunicante.id"));
                comunicante.setNome(rs.getString("comunicante.nome"));
                denuncia.setComunicante(comunicante);
                
                Logradouro lograComunicante = new Logradouro();
                lograComunicante.setNome(rs.getString("logra_comu.nome"));
                comunicante.setLogradouro(lograComunicante);
                
                Pessoa denunciado = new Pessoa();
                denunciado.setId(rs.getInt("denunciado.id"));
                denunciado.setNome(rs.getString("denunciado.nome"));
                denuncia.setPDenunciado(denunciado);
                
                Logradouro lograDenunciado = new Logradouro();
                lograDenunciado.setNome(rs.getString("logra_denun.nome"));
                denunciado.setLogradouro(lograDenunciado);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                denuncia.setUsuario(usuario);
                
                listDenuncia.add(denuncia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDenuncia;
    }
    
    
    public List<Denuncia> consultaDenunciaAppNome(String nome) {
        List<Denuncia> listDenuncia = new ArrayList<>();
        Denuncia denuncia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaDenunciaNomeApp);
            nome = "%" + nome + "%";
            //nomeDois = "%" + nomeDois + "%";
            ps.setString(1, nome);
            ps.setString(2, nome);
            ps.setString(3, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                denuncia = new Denuncia();
                denuncia.setId(rs.getInt("oco.id"));
                denuncia.setTipoDenuncia(rs.getString("oco.tipo_denuncia"));
                denuncia.setDenunciado(rs.getString("oco.denunciado"));
                denuncia.setLocalDenuncia(rs.getString("oco.local_denuncia"));
                denuncia.setDataDenuncia(rs.getDate("oco.data_denuncia"));
                denuncia.setDescricao(rs.getString("oco.descricao"));
                denuncia.setToken(rs.getString("oco.token_gcm"));
                denuncia.setStatusApp(rs.getString("oco.status_app"));
                denuncia.setOrigem(rs.getString("oco.origem"));
                
                listDenuncia.add(denuncia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listDenuncia;
    }
    
           
    public boolean incluiDenuncia(Denuncia denuncia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiDenuncia);

            ps.setInt(1, denuncia.getUsuario().getId());
            ps.setDate(2, denuncia.getDataRegistro());
            ps.setInt(3, denuncia.getNaturezaOcorrencia().getId());
            ps.setInt(4, denuncia.getComunicante().getId());
            ps.setInt(5, denuncia.getPDenunciado().getId());
            ps.setString(6, denuncia.getRelatoOcorencia());
            ps.setString(7, denuncia.getLink());
           
            
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
    
    public boolean alteraDenuncia(Denuncia denuncia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDenuncia);

            //ps.setInt(1, denuncia.getUsuario().getId());
            ps.setDate(1, denuncia.getDataRegistro());
            ps.setInt(2, denuncia.getNaturezaOcorrencia().getId());
            ps.setInt(3, denuncia.getComunicante().getId());
            ps.setInt(4, denuncia.getPDenunciado().getId());
            ps.setString(5, denuncia.getRelatoOcorencia());
            ps.setString(6, denuncia.getLink());
                       
            ps.setInt(7, denuncia.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraDenunciaStatusApp(Denuncia denuncia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDenunciaStatus);

            //ps.setInt(1, denuncia.getUsuario().getId());
            ps.setString(1, denuncia.getStatusApp());
            ps.setInt(2, denuncia.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
       
     public boolean excluiDenuncia(Denuncia denuncia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiDenuncia);

            ps.setInt(1, denuncia.getId());

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
