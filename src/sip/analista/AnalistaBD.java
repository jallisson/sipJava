/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.analista;

import sip.acessobd.AcessoBD;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class AnalistaBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaAnalista = "SELECT analista.*, analista.qtde_entrada - analista.qtde_saida AS saldo, usu.* FROM analista analista  JOIN usuario usu ON analista.id_usuario = usu.id where analista.tipo = 'FISCAL' ORDER BY analista.id DESC";
    private String consultaAnalistaNome = "SELECT analista.*,  analista.qtde_entrada - analista.qtde_saida AS saldo, usu.* FROM analista analista  JOIN usuario usu ON analista.id_usuario = usu.id where analista.nome like ?";
    private String incluiAnalista = "insert into analista (nome, matricula, qtde_entrada, qtde_saida, id_usuario, tipo) values(?, ?, ?, ?, ?, ?)";
    private String alteraAnalista = "update analista set nome = ?, matricula = ?, id_usuario = ?, tipo = ? where analista.id = ?";
    private String alteraAnalistaQtdeEnt = "update analista set qtde_entrada = ? where analista.id = ?";
    private String alteraAnalistaQtdeSaida = "update analista set qtde_saida = ? where analista.id = ?";
    private String excluiAnalista = "delete from terreno_imob where terreno_imob.id = ?";
    
    public List<Analista> consultaAnalista() {
        List<Analista> listaAnalista = new ArrayList<>();
        Analista analista;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAnalista);
            rs = ps.executeQuery();
            while (rs.next()) {
                analista = new Analista();
                analista.setId(rs.getInt("analista.id"));
                analista.setNome(rs.getString("analista.nome"));
                analista.setMatricula(rs.getString("analista.matricula"));
                analista.setQtdeEntrada(rs.getInt("analista.qtde_entrada"));
                analista.setQtdeSaida(rs.getInt("analista.qtde_saida"));
                analista.setSaldo(rs.getInt("saldo"));
                analista.setTipo(rs.getString("analista.tipo"));

                
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));
                
                analista.setUsuario(usuario);
                
                              
                listaAnalista.add(analista);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAnalista;
    }

    public List<Analista> consultaAnalistaNome (String nome) {
        List<Analista> listaAnalista = new ArrayList<>();
        Analista analista;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAnalistaNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                analista = new Analista();
                analista.setId(rs.getInt("analista.id"));
                analista.setNome(rs.getString("analista.nome"));
                analista.setMatricula(rs.getString("analista.matricula"));
                analista.setQtdeEntrada(rs.getInt("analista.qtde_entrada"));
                analista.setQtdeSaida(rs.getInt("analista.qtde_saida"));
                analista.setSaldo(rs.getInt("saldo"));
                analista.setTipo(rs.getString("analista.tipo"));
                
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));
                
                analista.setUsuario(usuario);
                
                listaAnalista.add(analista);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAnalista;
    }
        
    public boolean incluiAnalista(Analista analista) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiAnalista);
            
           
            ps.setString(1, analista.getNome());
            ps.setString(2, analista.getMatricula());
            ps.setInt(3, analista.getQtdeEntrada());
            ps.setInt(4, analista.getQtdeSaida());
            ps.setInt(5, analista.getUsuario().getId());
            ps.setString(6, analista.getTipo());
                       
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraAnalista(Analista analista) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAnalista);

            ps.setString(1, analista.getNome());
            ps.setString(2, analista.getMatricula());
            ps.setInt(3, analista.getUsuario().getId());
           
            ps.setString(4, analista.getTipo());
              ps.setInt(5, analista.getId());          
                       

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraAnalistaEntrada(Analista analista) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAnalistaQtdeEnt);

            ps.setInt(1, analista.getQtdeEntrada());
            
            ps.setInt(2, analista.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean alteraAnalistaSaida(Analista analista) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAnalistaQtdeSaida);

            ps.setInt(1, analista.getQtdeSaida());
            
            ps.setInt(2, analista.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiAnalista(Analista analista) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiAnalista);

            ps.setInt(1, analista.getId());

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
