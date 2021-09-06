/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.naturezaocorrencia;

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
public class NaturezaOcorrenciaBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaNaturezaOcorrencia = "select * from natureza_ocorrencia order by natureza_ocorrencia.id desc";
    private String consultaNaturezaOcorrenciaNome = "select * from natureza_ocorrencia where natureza_ocorrencia.nome like ?";
    private String incluiNaturezaOcorrencia = "insert into natureza_ocorrencia (nome, noapp) values(?, ?)";
    private String alteraNaturezaOcorrencia = "update natureza_ocorrencia set nome = ?, noapp = ? where natureza_ocorrencia.id = ?";
    private String excluiNaturezaOcorrencia = "delete from natureza_ocorrencia where natureza_ocorrencia.id = ?";

    public List<NaturezaOcorrencia> consultaNaturezaOcorrencia() {
        List<NaturezaOcorrencia> listNaturezaOcorrencia = new ArrayList<>();
        NaturezaOcorrencia naturezaOcorrencia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaNaturezaOcorrencia);
            rs = ps.executeQuery();
            while (rs.next()) {
                naturezaOcorrencia = new NaturezaOcorrencia();
                naturezaOcorrencia.setId(rs.getInt("id"));
                naturezaOcorrencia.setNome(rs.getString("nome"));
                naturezaOcorrencia.setNoApp(rs.getString("noapp"));
                listNaturezaOcorrencia.add(naturezaOcorrencia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listNaturezaOcorrencia;
    }

    public List<NaturezaOcorrencia> consultaNaturezaOcorrenciaNome(String nome) {
        List<NaturezaOcorrencia> listaNaturezaOcorrencia = new ArrayList<>();
        NaturezaOcorrencia naturezaOcorrencia;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaNaturezaOcorrenciaNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                naturezaOcorrencia = new NaturezaOcorrencia();
                naturezaOcorrencia.setId(rs.getInt("id"));
                naturezaOcorrencia.setNome(rs.getString("nome"));
                naturezaOcorrencia.setNoApp(rs.getString("noapp"));

                listaNaturezaOcorrencia.add(naturezaOcorrencia);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaNaturezaOcorrencia;
    }

    public boolean incluiNaturezaOcorrencia(NaturezaOcorrencia naturezaOcorrencia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiNaturezaOcorrencia);

            ps.setString(1, naturezaOcorrencia.getNome());
            ps.setString(2, naturezaOcorrencia.getNoApp());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraNaturezaOcorrencia(NaturezaOcorrencia naturezaOcorrencia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraNaturezaOcorrencia);

            ps.setString(1, naturezaOcorrencia.getNome());
            ps.setString(2, naturezaOcorrencia.getNoApp());
            ps.setInt(3, naturezaOcorrencia.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiNaturezaOcorrencia(NaturezaOcorrencia naturezaOcorrencia) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiNaturezaOcorrencia);

            ps.setInt(1, naturezaOcorrencia.getId());

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
