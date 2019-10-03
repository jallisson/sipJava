/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.cidade;

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
public class CidadeBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaCidade = "select * from cidade order by cidade.id desc";
    private String consultaCidadeNome = "select * from cidade where cidade.nome like ?";
    private String incluiCidade = "insert into cidade (nome, uf) values(?, ?)";
    private String alteraCidade = "update cidade set nome = ?, uf = ? where cidade.id = ?";
    private String excluiCidade = "delete from cidade where cidade.id = ?";
    


    public List<Cidade> consultaCidade() {
        List<Cidade> listaCidade = new ArrayList<Cidade>();
        Cidade cidade;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaCidade);
            rs = ps.executeQuery();
            while (rs.next()) {
                cidade = new Cidade();
                cidade.setId(rs.getInt("id"));
                cidade.setNome(rs.getString("nome"));
               // localidade.setEndereco(rs.getString("endereco"));
               // localidade.setCidade(rs.getString("cidade"));
               // localidade.setCidade(rs.getString("cidade"));
              //  localidade.setCep(rs.getString("cep"));
                cidade.setUf(rs.getString("uf"));
              //  localidade.setTelefone(rs.getString("telefone"));
              //  localidade.setEmail(rs.getString("email"));

                listaCidade.add(cidade);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCidade;
    }

    public List<Cidade> consultaCidade (String nome) {
        List<Cidade> listaCidade = new ArrayList<Cidade>();
        Cidade cidade;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaCidadeNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                cidade = new Cidade();
                cidade.setId(rs.getInt("id"));
                cidade.setNome(rs.getString("nome"));
                //localidade.setEndereco(rs.getString("endereco"));
                //localidade.setCidade(rs.getString("cidade"));
                //localidade.setCidade(rs.getString("cidade"));
                //localidade.setCep(rs.getString("cep"));
                //verifica.setUf(rs.getString("uf"));
                //localidade.setTelefone(rs.getString("telefone"));
                //localidade.setEmail(rs.getString("email"));

                listaCidade.add(cidade);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaCidade;
    }
        
    public boolean incluiCidade(Cidade cidade) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiCidade);

            ps.setString(1, cidade.getNome());
            //ps.setString(2, localidade.getEndereco());
            //ps.setString(3, localidade.getCidade());
            //ps.setString(4, localidade.getCidade());
            ps.setString(2, cidade.getUf());
            //ps.setString(6, localidade.getCep());
            //ps.setString(7, localidade.getTelefone());
            //ps.setString(8, localidade.getEmail());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraCidade(Cidade cidade) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraCidade);

            ps.setString(1, cidade.getNome());
            //ps.setString(2, localidade.getEndereco());
            //ps.setString(3, localidade.getCidade());
            //ps.setString(4, localidade.getCidade());
            ps.setString(2, cidade.getUf());
            //ps.setString(6, localidade.getCep());
            //ps.setString(7, localidade.getTelefone());
            //ps.setString(8, localidade.getEmail());
            ps.setInt(3, cidade.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiCidade(Cidade sigla) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiCidade);

            ps.setInt(1, sigla.getId());

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
