/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.triagem;

import sip.gabinete.*;
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
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class TriagemBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    //private String consultaLaudoImovel = "select *from laudo_imovel";
    private String consultaTriagem = "SELECT denuncia.*, usu.nome, triagem.*, pdenunciado.nome FROM triagem_denuncia triagem JOIN denuncia denuncia ON triagem.id_denuncia = denuncia.id JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id JOIN usuario usu ON triagem.id_usuario = usu.id ORDER BY denuncia.id";
    private String consultaTriagemNome = "SELECT denuncia.*, usu.nome, triagem.*, pdenunciado.nome FROM triagem_denuncia triagem JOIN denuncia denuncia ON triagem.id_denuncia = denuncia.id JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id JOIN usuario usu ON triagem.id_usuario = usu.id where pdenunciado.nome ? ORDER BY triagem.id";
    private String incluiTriagem = "insert into triagem_denuncia (id_usuario, id_denuncia, data_triagem, resumo) values(?, ?, ?, ?)";
    private String alteraTriagem = "update triagem_denuncia set id_usuario, id_denuncia, data_triagem = ?, resumo = ? where triagem_denuncia.id = ?";
    private String excluiTriagem = "delete from triagem_denuncia where triagem_denuncia.id = ?";
    private String consultaUltimoId = "select LAST_INSERT_ID() as id from triagem_denuncia";
    int mostraID;


    public List<Triagem> consultaTriagem() {
        List<Triagem> listTriagem = new ArrayList<>();
        Triagem triagem;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTriagem);
            rs = ps.executeQuery();
            while (rs.next()) {
                triagem = new Triagem();
                triagem.setId(rs.getInt("triagem.id"));              
                triagem.setDataTriagem(rs.getDate("triagem.data_triagem"));    
                triagem.setResumo(rs.getString("triagem.resumo"));
                
                                
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                triagem.setDenuncia(denuncia);
                                        
                
                Pessoa pDenunciado = new Pessoa();
                pDenunciado.setNome(rs.getString("pdenunciado.nome"));
                triagem.setpDenunciado(pDenunciado);
                
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                triagem.setUsuario(usuario);

               
                listTriagem.add(triagem);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTriagem;
    }

    public List<Triagem> consultaTriagemNome (String nome) {
        List<Triagem> listTriagem = new ArrayList<>();
        Triagem triagem;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTriagemNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
             while (rs.next()) {
                triagem = new Triagem();
                triagem.setId(rs.getInt("triagem.id"));              
                triagem.setDataTriagem(rs.getDate("triagem.data_triagem"));    
                triagem.setResumo(rs.getString("triagem.resumo"));
                
                                
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                triagem.setDenuncia(denuncia);
                                        
                
                Pessoa pDenunciado = new Pessoa();
                pDenunciado.setNome(rs.getString("pdenunciado.nome"));
                triagem.setpDenunciado(pDenunciado);
                
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                triagem.setUsuario(usuario);

               
                listTriagem.add(triagem);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTriagem;
    }
        
    public boolean incluiTriagem(Triagem triagem) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiTriagem);
            
            ps.setInt(1, triagem.getUsuario().getId());
            ps.setInt(2, triagem.getDenuncia().getId());
            ps.setDate(3, triagem.getDataTriagem());
            ps.setString(4, triagem.getResumo());
            
            ps.executeUpdate();
            
            ps = con.prepareStatement(consultaUltimoId);
            rs = ps.executeQuery();
            rs.next();
            triagem.setId(rs.getInt("id"));
            mostraID = triagem.getId();
            
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraTriagem(Triagem triagem) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTriagem);

            ps.setInt(1, triagem.getUsuario().getId());
            ps.setInt(2, triagem.getDenuncia().getId());
            ps.setDate(3, triagem.getDataTriagem());
            ps.setString(4, triagem.getResumo());
            
            ps.setInt(5, triagem.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
        
     public boolean excluiTriagem(Triagem triagem) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiTriagem);

            ps.setInt(1, triagem.getId());

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
