/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.usuario;

import sip.acessobd.AcessoBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;

/**
 *
 * @author T2Ti
 */
public class UsuarioBD {
   
    private PreparedStatement ps;
    public Connection con;
    public ResultSet rs;
    private ResultSet rs1; 
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaUsuario = "select * from usuario order by usuario.id";
    private String consultaUsuarioSimples = "select * from usuario order by usuario.id";
    private String consultaModUsuario = "select na.*, usu.* from nivel_acesso na join usuario usu on na.id_digitador = usu.id where dig.id = ?";
    private String consultaUsuarioNome = "select * from usuario where usuario.nome like ?";
    private String consultaUsuarioID = "select *from usuario where usuario.id like ?";
    private String incluiUsuario = "insert into usuario (nome, login, senha, setor) values(?, ?, ?, ?)";
    private String alteraUsuario = "update usuario set nome = ?, login = ?, senha = ?, setor = ? where usuario.id = ?";
    private String excluiUsuario = "delete from usuario where usuario.id = ?";
    private String cont = "select count(*) from usuario";
    private PreparedStatement ps2;
    
    
    public List<Usuario> consultaUsuario() {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSetor(rs.getString("setor"));
                //monitor.setCpf(rs.getString("cpf"));
                //monitor.setTelefone(rs.getString("telefone"));

                listaUsuario.add(usuario);
            }
            acessoBD.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }   
    
     public List<Usuario> consultaUsuarioSimples() {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuarioSimples);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setSetor(rs.getString("setor"));

                listaUsuario.add(usuario);
            }
            acessoBD.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }   
    
     public List<Usuario> consultaUsuarioNAcesso() {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSetor(rs.getString("setor"));
                //monitor.setCpf(rs.getString("cpf"));
                //monitor.setTelefone(rs.getString("telefone"));
                
                
                
                ps2 = con.prepareStatement(consultaModUsuario);
                ps2.setInt(1, usuario.getId());
                rs1 = ps2.executeQuery();
                     
                 List<NAcesso> listaNivelAcesso = new ArrayList<NAcesso>();
                while(rs1.next()){
                    NAcesso nivelAcesso = new NAcesso();
                                     
                    nivelAcesso.setId(rs1.getInt("na.id"));
                    nivelAcesso.setNomeModulo(rs1.getString("na.nome_modulo"));
                    
                    
                    Usuario digitador1 = new Usuario();
                    digitador1.setId(rs1.getInt("usu.id"));
                    digitador1.setNome(rs1.getString("usu.nome"));
                    
                    
                    
                    nivelAcesso.setUsuario(digitador1);
                    listaNivelAcesso.add(nivelAcesso);
                }
                usuario.setNivelAce(listaNivelAcesso);
                
                listaUsuario.add(usuario);
            }
            acessoBD.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }   
     
    public void consultaUsuarioTeste() {
       // List<Monitor> listaMonitor = new ArrayList<Monitor>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuario);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setLogin(rs.getString("login"));
                usuario.setSetor(rs.getString("setor"));
                //monitor.setCpf(rs.getString("cpf"));
                //monitor.setTelefone(rs.getString("telefone"));

               // listaMonitor.add(monitor);
            }
            acessoBD.desconectar();
        } catch (SQLException e) {
            e.printStackTrace();
        }
     
    }      
    

      public List<Usuario> consultaUsuarioNome(String nome) {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuarioNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));  
                usuario.setSetor(rs.getString("setor"));
             
                listaUsuario.add(usuario);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }
      
       public List<Usuario> consultaUsuarioID(String id) {
        List<Usuario> listaUsuario = new ArrayList<Usuario>();
        Usuario usuario;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUsuarioID);
            id = "%" + id + "%";
            ps.setString(1, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                usuario = new Usuario();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));  
                usuario.setSetor(rs.getString("setor"));
                //monitor.setCpf(rs.getString("cpf"));
                //monitor.setTelefone(rs.getString("telefone"));
             
                listaUsuario.add(usuario);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaUsuario;
    }
      
        
   public boolean incluiUsuario(Usuario usuario) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiUsuario);

            ps.setString(1, usuario.getNome());
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getSetor());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraUsuario(Usuario usuario) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraUsuario);

            ps.setString(1, usuario.getNome()); 
            ps.setString(2, usuario.getLogin());
            ps.setString(3, usuario.getSenha());
            ps.setString(4, usuario.getSetor());
            ps.setInt(5, usuario.getId());
              
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

       
    public boolean excluiUsuario(Usuario usuario) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiUsuario);

            ps.setInt(1, usuario.getId());

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
       
       
       
       
       
       
       
       
       
       
    

