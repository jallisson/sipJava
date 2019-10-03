/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.lotedma;

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
public class LoteDmaBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaLote = "select * from lote_dma order by lote_dma.id desc";
    private String consultaLoteNome = "select * from lote_dma where lote_dma.lote like ?";
    private String incluiLote = "insert into lote_dma (descricao, data_lote, mesano) values(?, ?, ?)";
    private String alteraLote = "update lote_dma set descricao = ?, data_lote = ?, mesano = ? where lote_dma.id = ?";
    private String excluiLote = "delete from lote_dma where lote_dma.id = ?";
    


    public List<LoteDma> consultaLote() {
        List<LoteDma> listaLote = new ArrayList<LoteDma>();
        LoteDma lote;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLote);
            rs = ps.executeQuery();
            while (rs.next()) {
                lote = new LoteDma();
                lote.setId(rs.getInt("id"));
                //lote.setLote(rs.getString("lote"));
                lote.setDescricao(rs.getString("descricao"));
                lote.setDataLote(rs.getDate("data_lote"));
                lote.setMesAno(rs.getString("mesano"));

                listaLote.add(lote);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaLote;
    }

    public List<LoteDma> consultaLoteNome (String nome) {
        List<LoteDma> listaLote = new ArrayList<LoteDma>();
        LoteDma lote;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLoteNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                lote = new LoteDma();
                lote.setId(rs.getInt("id"));
                //lote.setLote(rs.getString("lote"));
                lote.setDescricao(rs.getString("descricao"));
                lote.setDataLote(rs.getDate("data_lote"));
                lote.setMesAno(rs.getString("mesano"));

                listaLote.add(lote);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaLote;
    }
        
    public boolean incluiLote(LoteDma lote) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiLote);

            //ps.setString(1, lote.getLote());
            ps.setString(1, lote.getDescricao());
            ps.setDate(2, lote.getDataLote());
            ps.setString(3, lote.getMesAno());
            
            
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraLote(LoteDma lote) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraLote);

           // ps.setString(1, lote.getLote());
            ps.setString(1, lote.getDescricao());
            ps.setDate(2, lote.getDataLote());
            ps.setString(3, lote.getMesAno());
            ps.setInt(4, lote.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiLote(LoteDma lote) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiLote);

            ps.setInt(1, lote.getId());

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
