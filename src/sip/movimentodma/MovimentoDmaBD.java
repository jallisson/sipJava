/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.movimentodma;

import sip.acessobd.AcessoBD;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import sip.analista.Analista;
import sip.lotedma.LoteDma;
import sip.processo.Processo;

/**
 *
 * @author T2Ti
 */
public class MovimentoDmaBD {
   
    private PreparedStatement ps;
    private PreparedStatement ps1;
    public Connection con;
    public ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaMovimentoDma = "SELECT movi.*, usu.nome, pro.id, pro.num_processo, pro.tipo_licenca, lote.id, lote.descricao, req.nome FROM movimento_dma movi JOIN usuario usu ON movi.id_usuario = usu.id JOIN processo pro ON movi.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id JOIN lote_dma lote ON movi.id_lote_dma = lote.id ORDER BY movi.id DESC";
    private String consultaMovimentoNumNome = "SELECT movi.*, usu.nome, pro.id, pro.num_processo, pro.tipo_licenca, lote.id, lote.descricao, req.nome FROM movimento_dma movi JOIN usuario usu ON movi.id_usuario = usu.id JOIN processo pro ON movi.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id JOIN lote_dma lote ON movi.id_lote_dma = lote.id where pro.num_processo like ? or req.nome like ?";
    private String incluiMovimentoDma = "insert into movimento_dma (id_usuario, id_processo, id_lote_dma, data_movimento, mesano, situacao, movimento, controle) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraMovimentoDma = "update movimento_dma set data_movimento = ?, mesano = ?, situacao = ?, movimento = ? where .id = ?";
    private String excluiMovimentoDma = "delete from movimento_dma where movimento_dma.id = ?";
    private String cont = "select count(*) from cobrador";
    
    
    public List<MovimentoDma> consultaMovimentoDma(String consultaDeOnde){
        List<MovimentoDma> listaMovimentoDma = new ArrayList<>();
        MovimentoDma movimento;
        try {
            con = acessoBD.conectar();
            switch (consultaDeOnde) {
                case "MovimentoDmaFrame":
                    ps = con.prepareStatement(consultaMovimentoDma);
                    break;
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                movimento = new MovimentoDma();
                movimento.setId(rs.getInt("movi.id"));
                movimento.setDataMovimento(rs.getDate("movi.data_movimento"));
                movimento.setMesAno(rs.getString("movi.mesano"));
                movimento.setSituacao(rs.getString("movi.situacao"));
                movimento.setMovimento(rs.getString("movi.movimento"));
                movimento.setControle(rs.getString("movi.controle"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                movimento.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                movimento.setProcesso(processo);
                
                              
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                movimento.setUsuario(usuario);
                
                LoteDma lote = new LoteDma();
                lote.setId(rs.getInt("lote.id"));
                lote.setDescricao(rs.getString("lote.descricao"));
                movimento.setLote(lote);

                listaMovimentoDma.add(movimento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMovimentoDma;
    }
    

  
      public List<MovimentoDma> consultaProcessoNum(String numero, String nome) {
        List<MovimentoDma> listaMovimentoDma = new ArrayList<>();
        MovimentoDma movimento;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaMovimentoNumNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
                     
            rs = ps.executeQuery();
             while (rs.next()) {
                 movimento = new MovimentoDma();
                movimento.setId(rs.getInt("movi.id"));
                movimento.setDataMovimento(rs.getDate("movi.data_movimento"));
                movimento.setMesAno(rs.getString("movi.mesano"));
                movimento.setSituacao(rs.getString("movi.situacao"));
                movimento.setMovimento(rs.getString("movi.movimento"));
                movimento.setControle(rs.getString("movi.controle"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                movimento.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                movimento.setProcesso(processo);
                
                              
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                movimento.setUsuario(usuario);
                
                LoteDma lote = new LoteDma();
                lote.setId(rs.getInt("lote.id"));
                lote.setDescricao(rs.getString("lote.descricao"));
                movimento.setLote(lote);

                listaMovimentoDma.add(movimento);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaMovimentoDma;
    }
        
   public boolean incluiMovimentoDma(MovimentoDma movimento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiMovimentoDma);
            //insert into movimento_dma (id_usuario, id_processo, id_lote_dma, data_movimento, mesano, situacao, movimento, controle) values (?, ?, ?, ?, ?, ?, ?, ?) 
            
            
            ps.setInt(1, movimento.getUsuario().getId());
            ps.setInt(2, movimento.getProcesso().getId());
            ps.setInt(3, movimento.getLote().getId());
            ps.setDate(4, movimento.getDataMovimento());
            ps.setString(5, movimento.getMesAno());
            ps.setString(6, movimento.getSituacao());
            ps.setString(7, movimento.getMovimento());
            ps.setString(8, movimento.getControle());
            /*if (movimento.getTramitouAnalise() == null) {
                ps.setNull(8, 1);
            } else {
                ps.setString(8, movimento.getTramitouAnalise());
            }*/
         
         
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   
    
    public boolean alteraMovimentoDma(MovimentoDma movimento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraMovimentoDma);
 
            ps.setDate(1, movimento.getDataMovimento());
            //ps.setString(2, movimento.getAnalisado());
            ps.setString(2, movimento.getMesAno());
            ps.setString(3, movimento.getControle());

            ps.setInt(4, movimento.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
       
    public boolean excluiProcesso(MovimentoDma movimento) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiMovimentoDma);

            ps.setInt(1, movimento.getId());

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