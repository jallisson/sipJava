/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.tipolicenca;

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
public class TipoLicencaBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaTipoLicenca = "select * from tipo_licenca order by tipo_licenca.id desc";
    private String consultaTipoLicencaNome = "select * from tipo_licenca where tipo_licenca.nome like ?";
    private String incluiTipoLicenca = "insert into tipo_licenca (nome, abreviatura) values(?, ?)";
    private String alteraTipoLicenca = "update tipo_licenca set nome = ?, abreviatura = ? where tipo_licenca.id = ?";
    private String excluiTipoLicenca = "delete from tipo_licenca where tipo_licenca.id = ?";
    


    public List<TipoLicenca> consultaTipoLicenca() {
        List<TipoLicenca> listaTipoLicenca = new ArrayList<TipoLicenca>();
        TipoLicenca tipo_licenca;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTipoLicenca);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo_licenca = new TipoLicenca();
                tipo_licenca.setId(rs.getInt("id"));
                tipo_licenca.setNome(rs.getString("nome"));
               // localidade.setEndereco(rs.getString("endereco"));
               // localidade.setTipoLicenca(rs.getString("tipo_licenca"));
               // localidade.setTipoLicenca(rs.getString("tipo_licenca"));
              //  localidade.setCep(rs.getString("cep"));
                tipo_licenca.setAbreviatura(rs.getString("abreviatura"));
              //  localidade.setTelefone(rs.getString("telefone"));
              //  localidade.setEmail(rs.getString("email"));

                listaTipoLicenca.add(tipo_licenca);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoLicenca;
    }

    public List<TipoLicenca> consultaTipoLicenca (String nome) {
        List<TipoLicenca> listaTipoLicenca = new ArrayList<TipoLicenca>();
        TipoLicenca tipo_licenca;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTipoLicencaNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo_licenca = new TipoLicenca();
                tipo_licenca.setId(rs.getInt("id"));
                tipo_licenca.setNome(rs.getString("nome"));
                //localidade.setEndereco(rs.getString("endereco"));
                //localidade.setTipoLicenca(rs.getString("tipo_licenca"));
                //localidade.setTipoLicenca(rs.getString("tipo_licenca"));
                //localidade.setCep(rs.getString("cep"));
                //verifica.setUf(rs.getString("abreviatura"));
                //localidade.setTelefone(rs.getString("telefone"));
                //localidade.setEmail(rs.getString("email"));

                listaTipoLicenca.add(tipo_licenca);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoLicenca;
    }
        
    public boolean incluiTipoLicenca(TipoLicenca tipo_licenca) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiTipoLicenca);

            ps.setString(1, tipo_licenca.getNome());
            //ps.setString(2, localidade.getEndereco());
            //ps.setString(3, localidade.getTipoLicenca());
            //ps.setString(4, localidade.getTipoLicenca());
            ps.setString(2, tipo_licenca.getAbreviatura());
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
    
    public boolean alteraTipoLicenca(TipoLicenca tipo_licenca) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTipoLicenca);

            ps.setString(1, tipo_licenca.getNome());
            //ps.setString(2, localidade.getEndereco());
            //ps.setString(3, localidade.getTipoLicenca());
            //ps.setString(4, localidade.getTipoLicenca());
            ps.setString(2, tipo_licenca.getAbreviatura());
            //ps.setString(6, localidade.getCep());
            //ps.setString(7, localidade.getTelefone());
            //ps.setString(8, localidade.getEmail());
            ps.setInt(3, tipo_licenca.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiTipoLicenca(TipoLicenca sigla) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiTipoLicenca);

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
