/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.tipoevento;

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
public class TipoEventoBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaTipoEvento = "select * from tipo_evento order by tipo_evento.id desc";
    private String consultaTipoEventoNome = "select * from tipo_evento where tipo_evento.nome like ?";
    private String incluiTipoEvento = "insert into tipo_evento (nome) values(?)";
    private String alteraTipoEvento = "update tipo_evento set nome = ? where tipo_evento.id = ?";
    private String excluiTipoEvento = "delete from tipo_evento where tipo_evento.id = ?";
    


    public List<TipoEvento> consultaTipoEvento() {
        List<TipoEvento> listTipoEvento = new ArrayList<>();
        TipoEvento tipo_evento;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTipoEvento);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo_evento = new TipoEvento();
                tipo_evento.setId(rs.getInt("id"));
                tipo_evento.setNome(rs.getString("nome"));
                listTipoEvento.add(tipo_evento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listTipoEvento;
    }

    public List<TipoEvento> consultaTipoEvento (String nome) {
        List<TipoEvento> listaTipoEvento = new ArrayList<>();
        TipoEvento tipo_evento;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTipoEventoNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo_evento = new TipoEvento();
                tipo_evento.setId(rs.getInt("id"));
                tipo_evento.setNome(rs.getString("nome"));
        
                listaTipoEvento.add(tipo_evento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTipoEvento;
    }
        
    public boolean incluiTipoEvento(TipoEvento tipo_evento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiTipoEvento);

            ps.setString(1, tipo_evento.getNome());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraTipoEvento(TipoEvento tipo_evento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTipoEvento);

            ps.setString(1, tipo_evento.getNome());
            ps.setInt(2, tipo_evento.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiTipoEvento(TipoEvento tipo_evento){
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiTipoEvento);

            ps.setInt(1, tipo_evento.getId());

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
