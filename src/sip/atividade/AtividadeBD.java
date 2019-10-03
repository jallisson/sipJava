/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.atividade;

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
public class AtividadeBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaAtividade = "select * from atividade order by atividade.id desc";
    private String consultaAtividadeNome = "select * from atividade where atividade.nome like ?";
    private String incluiAtividade = "insert into atividade (nome, potencial_poluidor) values(?,?)";
    private String alteraAtividade = "update atividade set nome = ?, potencial_poluidor = ? where atividade.id = ?";
    private String excluiAtividade = "delete from atividade where atividade.id = ?";
    


    public List<Atividade> consultaAtividade() {
        List<Atividade> listAtividade = new ArrayList<>();
        Atividade atividade;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAtividade);
            rs = ps.executeQuery();
            while (rs.next()) {
                atividade = new Atividade();
                atividade.setId(rs.getInt("id"));
                atividade.setNome(rs.getString("nome"));
                atividade.setPotencialPoluidor(rs.getString("potencial_poluidor"));
                listAtividade.add(atividade);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listAtividade;
    }

    public List<Atividade> consultaAtividade (String nome) {
        List<Atividade> listaAtividade = new ArrayList<>();
        Atividade atividade;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAtividadeNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                atividade = new Atividade();
                atividade.setId(rs.getInt("id"));
                atividade.setNome(rs.getString("nome"));
                atividade.setPotencialPoluidor(rs.getString("potencial_poluidor"));
                listaAtividade.add(atividade);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAtividade;
    }
        
    public boolean incluiAtividade(Atividade atividade) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiAtividade);

            ps.setString(1, atividade.getNome());
            ps.setString(2, atividade.getPotencialPoluidor());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraAtividade(Atividade atividade) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraAtividade);

            ps.setString(1, atividade.getNome());
            ps.setString(2, atividade.getPotencialPoluidor());
            
            
            ps.setInt(3, atividade.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiAtividade(Atividade atividade){
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiAtividade);

            ps.setInt(1, atividade.getId());

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
