/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.localevento;

import sip.acessobd.AcessoBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author T2Ti
 */
public class LocalEventoBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaLocalEvento = "select * from local_evento order by local_evento.id desc";
    private String consultaLocalEventoNome = "select * from local_evento where local_evento.nome like ?";
    private String incluiLocalEvento = "insert into local_evento (nome) values(?)";
    private String alteraLocalEvento = "update local_evento set nome = ? where local_evento.id = ?";
    private String excluiLocalEvento = "delete from local_evento where local_evento.id = ?";
    


    public List<LocalEvento> consultaLocalEvento() {
        List<LocalEvento> listLocalEvento = new ArrayList<>();
        LocalEvento local_evento;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLocalEvento);
            rs = ps.executeQuery();
            while (rs.next()) {
                local_evento = new LocalEvento();
                local_evento.setId(rs.getInt("id"));
                local_evento.setNome(rs.getString("nome"));
                listLocalEvento.add(local_evento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listLocalEvento;
    }

    public List<LocalEvento> consultaLocalEvento (String nome) {
        List<LocalEvento> listaLocalEvento = new ArrayList<>();
        LocalEvento local_evento;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaLocalEventoNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                local_evento = new LocalEvento();
                local_evento.setId(rs.getInt("id"));
                local_evento.setNome(rs.getString("nome"));
        
                listaLocalEvento.add(local_evento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaLocalEvento;
    }
        
    public boolean incluiLocalEvento(LocalEvento local_evento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiLocalEvento);

            ps.setString(1, local_evento.getNome());
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraLocalEvento(LocalEvento local_evento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraLocalEvento);

            ps.setString(1, local_evento.getNome());
            ps.setInt(2, local_evento.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiLocalEvento(LocalEvento local_evento){
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiLocalEvento);

            ps.setInt(1, local_evento.getId());

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
