/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.resumotriagem;

import sip.naturezaocorrencia.*;
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
public class ResumoTriagemBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaResumoTriagem = "select * from resumo_triagem order by resumo_triagem.id desc";
    private String consultaResumoTriagemNome = "select * from resumo_triagem where resumo_triagem.nome like ?";
    private String incluiResumoTriagem = "insert into resumo_triagem (nome) values(?)";
    private String alteraResumoTriagem = "update resumo_triagem set nome = ? where resumo_triagem.id = ?";
    private String excluiResumoTriagem = "delete from resumo_triagem where resumo_triagem.id = ?";
    


    public List<ResumoTriagem> consultaResumoTriagem() {
        List<ResumoTriagem> listResumoTriagem = new ArrayList<>();
        ResumoTriagem resumoTriagem;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaResumoTriagem);
            rs = ps.executeQuery();
            while (rs.next()) {
                resumoTriagem = new ResumoTriagem();
                resumoTriagem.setId(rs.getInt("id"));
                resumoTriagem.setNome(rs.getString("nome"));
                listResumoTriagem.add(resumoTriagem);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listResumoTriagem;
    }

    public List<ResumoTriagem> consultaResumoTriagem (String nome) {
        List<ResumoTriagem> listResumoTriagem = new ArrayList<>();
        ResumoTriagem resumoTriagem;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaResumoTriagemNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                resumoTriagem = new ResumoTriagem();
                resumoTriagem.setId(rs.getInt("id"));
                resumoTriagem.setNome(rs.getString("nome"));
        
                listResumoTriagem.add(resumoTriagem);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listResumoTriagem;
    }
        
    public boolean incluiResumoTriagem(ResumoTriagem resumoTriagem) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiResumoTriagem);

            ps.setString(1, resumoTriagem.getNome());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraResumoTriagem(ResumoTriagem resumoTriagem) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraResumoTriagem);

            ps.setString(1, resumoTriagem.getNome());
            ps.setInt(2, resumoTriagem.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiResumoTriagem(ResumoTriagem resumoTriagem){
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiResumoTriagem);

            ps.setInt(1, resumoTriagem.getId());

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
