/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.logradouro;

import sip.acessobd.AcessoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author T2Ti
 */
public class LogradouroBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaLogradouro = "select * from logradouro order by logradouro.id desc";
    private String consultaLogradouroNome = "select * from logradouro where logradouro.nome like ?";
    private String incluiLogradouro = "insert into logradouro (nome) values(?)";
    private String alteraLogradouro = "update logradouro set nome = ? where logradouro.id = ?";
    private String excluiLogradouro = "delete from logradouro where logradouro.id = ?";
    


    public List<Logradouro> consultaLogradouro() {
        List<Logradouro> listLogradouro = new ArrayList<>();
        Logradouro logradouro;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLogradouro);
            rs = ps.executeQuery();
            while (rs.next()) {
                logradouro = new Logradouro();
                logradouro.setId(rs.getInt("id"));
                logradouro.setNome(rs.getString("nome"));
                listLogradouro.add(logradouro);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLogradouro;
    }

    public List<Logradouro> consultaLogradouro (String nome) {
        List<Logradouro> listaLogradouro = new ArrayList<>();
        Logradouro logradouro;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLogradouroNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                logradouro = new Logradouro();
                logradouro.setId(rs.getInt("id"));
                logradouro.setNome(rs.getString("nome"));
        
                listaLogradouro.add(logradouro);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaLogradouro;
    }
        
    public boolean incluiLogradouro(Logradouro logradouro) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiLogradouro);

            ps.setString(1, logradouro.getNome());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraLogradouro(Logradouro logradouro) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraLogradouro);

            ps.setString(1, logradouro.getNome());
            ps.setInt(2, logradouro.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiLogradouro(Logradouro logradouro){
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiLogradouro);

            ps.setInt(1, logradouro.getId());

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
