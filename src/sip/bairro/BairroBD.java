/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.bairro;

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
public class BairroBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaBairro = "select * from bairro order by bairro.id desc";
    private String consultaBairroNome = "select * from bairro where bairro.nome like ?";
    private String incluiBairro = "insert into bairro (nome, livro, folha, matricula, tipo) values(?, ?, ?, ?, ?)";
    private String alteraBairro = "update bairro set nome = ?, livro = ?, folha = ?, matricula = ?, tipo = ? where bairro.id = ?";
    private String excluiBairro = "delete from bairro where bairro.id = ?";
    


    public List<Bairro> consultaBairro() {
        List<Bairro> listaBairro = new ArrayList<Bairro>();
        Bairro bairro;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaBairro);
            rs = ps.executeQuery();
            while (rs.next()) {
                bairro = new Bairro();
                bairro.setId(rs.getInt("id"));
                bairro.setNome(rs.getString("nome"));
                bairro.setLivro(rs.getString("livro"));
                bairro.setFolha(rs.getInt("folha"));
                bairro.setMatricula(rs.getString("matricula"));
                bairro.setTipo(rs.getString("tipo"));

                listaBairro.add(bairro);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaBairro;
    }

    public List<Bairro> consultaBairro (String nome) {
        List<Bairro> listaBairro = new ArrayList<Bairro>();
        Bairro bairro;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaBairroNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                bairro = new Bairro();
                bairro.setId(rs.getInt("id"));
                bairro.setNome(rs.getString("nome"));
                bairro.setNome(rs.getString("nome"));
                bairro.setLivro(rs.getString("livro"));
                bairro.setFolha(rs.getInt("folha"));
                bairro.setMatricula(rs.getString("matricula"));
                bairro.setTipo(rs.getString("tipo"));

                listaBairro.add(bairro);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaBairro;
    }
        
    public boolean incluiBairro(Bairro bairro) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiBairro);

            ps.setString(1, bairro.getNome());
            ps.setString(2, bairro.getLivro());
            ps.setInt(3, bairro.getFolha());
            ps.setString(4, bairro.getMatricula());
            ps.setString(5, bairro.getTipo());
            
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraBairro(Bairro bairro) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraBairro);

            ps.setString(1, bairro.getNome());
            ps.setString(2, bairro.getLivro());
            ps.setInt(3, bairro.getFolha());
            ps.setString(4, bairro.getMatricula());
            ps.setString(5, bairro.getTipo());
            
            ps.setInt(6, bairro.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiBairro(Bairro bairro) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiBairro);

            ps.setInt(1, bairro.getId());

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
