/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.analise;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import sip.processo.AnexosProcesso;
import sip.usuario.Usuario;

/**
 *
 * @author T2Ti
 */
public class ParecerAnaliseBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    //private String consultaLaudoImovel = "select *from laudo_imovel";
    private String consultaParecer = "SELECT pro.*, req.*, analise.*, parecer.*, dist.*, anexos.descricao_anexo, anexos.nome_arquivo FROM parecer_analise parecer JOIN analise analise ON parecer.id_analise = analise.id JOIN distribuicao dist ON analise.id_distribuicao = dist.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON parecer.id_anexos_processo = anexos.id ORDER BY parecer.id";
    private String consultaParecerTecnicoProcesso = "SELECT pro.*, req.*, analise.*, parecer.*, dist.*, anexos.descricao_anexo, anexos.nome_arquivo FROM parecer_analise parecer JOIN analise analise ON parecer.id_analise = analise.id JOIN distribuicao dist ON analise.id_distribuicao = dist.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON parecer.id_anexos_processo = anexos.id where pro.num_processo like ?";
    private String incluiParecer = "insert into parecer_analise (id_usuario, id_analise, data_parecer, tipo, controle, id_anexos_processo, data_vistoria, anexo_incluso) values(?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraParecer = "update parecer_analise set id_analise = ?, data_parecer = ?, tipo = ?, controle = ?, id_anexos_processo = ?, data_vistoria = ?, anexo_incluso =  ? where parecer_analise.id = ?";
    private String excluiParecer = "delete from parecer_analise where parecer_analise.id = ?";
    private String consultaUltimoId = "select LAST_INSERT_ID() as id from parecer_analise";
    private String consultaAnexosProcesso = "SELECT anexosp.*, usu.nome, usu.setor, parecer.id FROM parecer_analise parecer LEFT JOIN anexos_processo anexosp ON anexosp.id = parecer.id_anexos_processo LEFT JOIN usuario usu ON anexosp.id_usuario = usu.id WHERE parecer.id = ? GROUP BY anexosp.id";
    int mostraID;
    private String LastId = "SELECT MAX(ID) AS id FROM parecer_analise";

    public int getLastId() throws SQLException{
         con = acessoBD.conectar();
         ps = con.prepareStatement(LastId);
         rs = ps.executeQuery();
         rs.next();
         int lastId = rs.getInt("id");
         rs.close();
         ps.close();
         //acessoBD.desconectar();
         return lastId;        
    }
    
    
    public List<ParecerAnalise> consultaParecer() {
        List<ParecerAnalise> listParecer = new ArrayList<>();
        ParecerAnalise parecer;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaParecer);
            rs = ps.executeQuery();
            while (rs.next()) {
                parecer = new ParecerAnalise();
                parecer.setId(rs.getInt("parecer.id"));              
                parecer.setDataParecer(rs.getDate("parecer.data_parecer"));              
                parecer.setTipo(rs.getString("parecer.tipo"));
                parecer.setDataVistoria(rs.getDate("parecer.data_vistoria"));
                parecer.setAnexoIncluso(rs.getString("parecer.anexo_incluso"));
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                parecer.setProcesso(processo);
                
                Analise analise = new Analise();
                analise.setId(rs.getInt("analise.id"));
                parecer.setAnalise(analise);
                                        
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                parecer.setRequerente(requerente);
                
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                parecer.setAnexosProcesso(anexo);
                
                /*
                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, parecer.getId());
                rs1 = ps.executeQuery();

                List<AnexosProcesso> listAnexosProcesso = new ArrayList<>();
                while (rs1.next()) {
                    AnexosProcesso anexosProcesso = new AnexosProcesso();

                    anexosProcesso.setId(rs1.getInt("anexosp.id"));
                    anexosProcesso.setNomeArquivo(rs1.getString("anexosp.nome_arquivo"));
                    anexosProcesso.setCaminhoArquivo(rs1.getString("anexosp.caminho_arquivo"));
                    
                     Usuario usu= new Usuario();
                     usu.setNome(rs1.getString("usu.nome"));
                     usu.setSetor(rs1.getString("usu.setor"));
                                         
                     anexosProcesso.setUsuario(usu);
                    
                    listAnexosProcesso.add(anexosProcesso);

                }
                parecer.setListAnexosProcesso(listAnexosProcesso);
              */
                listParecer.add(parecer);
               
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParecer;
    }

    public List<ParecerAnalise> consultaParecerNome (String Numprocesso) {
        List<ParecerAnalise> listParecer = new ArrayList<>();
        ParecerAnalise parecer;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaParecerTecnicoProcesso);
            Numprocesso = "%" + Numprocesso + "%";
            ps.setString(1, Numprocesso);
            rs = ps.executeQuery();
            while (rs.next()) {
                parecer = new ParecerAnalise();
                parecer.setId(rs.getInt("parecer.id"));              
                parecer.setDataParecer(rs.getDate("parecer.data_parecer"));              
                parecer.setTipo(rs.getString("parecer.tipo"));
                parecer.setDataVistoria(rs.getDate("parecer.data_vistoria"));
                parecer.setAnexoIncluso(rs.getString("parecer.anexo_incluso"));
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                parecer.setProcesso(processo);
                
                Analise analise = new Analise();
                analise.setId(rs.getInt("analise.id"));
                parecer.setAnalise(analise);
                                        
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                parecer.setRequerente(requerente);
                
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                parecer.setAnexosProcesso(anexo);
                
                /*
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                parecer.setAnexosProcesso(anexo);
               
                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, parecer.getId());
                rs1 = ps.executeQuery();

                List<AnexosProcesso> listAnexosProcesso = new ArrayList<>();
                while (rs1.next()) {
                    AnexosProcesso anexosProcesso = new AnexosProcesso();

                    anexosProcesso.setId(rs1.getInt("anexosp.id"));
                    anexosProcesso.setNomeArquivo(rs1.getString("anexosp.nome_arquivo"));
                    anexosProcesso.setCaminhoArquivo(rs1.getString("anexosp.caminho_arquivo"));
                    
                     Usuario usu= new Usuario();
                     usu.setNome(rs1.getString("usu.nome"));
                     usu.setSetor(rs1.getString("usu.setor"));
                                         
                     anexosProcesso.setUsuario(usu);
                    
                    listAnexosProcesso.add(anexosProcesso);

                }
                parecer.setListAnexosProcesso(listAnexosProcesso);
              */
                listParecer.add(parecer);
                
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParecer;
    }
    
    public List<ParecerAnalise> consultaParecerAnexos() {
        List<ParecerAnalise> listParecer = new ArrayList<>();
        ParecerAnalise parecer;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaParecer);
            rs = ps.executeQuery();
            while (rs.next()) {
                parecer = new ParecerAnalise();
                parecer.setId(rs.getInt("parecer.id"));              
                parecer.setDataParecer(rs.getDate("parecer.data_parecer"));              
                parecer.setTipo(rs.getString("parecer.tipo"));
                parecer.setDataVistoria(rs.getDate("parecer.data_vistoria"));
                parecer.setAnexoIncluso(rs.getString("parecer.anexo_incluso"));
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                parecer.setProcesso(processo);
                
                Analise analise = new Analise();
                analise.setId(rs.getInt("analise.id"));
                parecer.setAnalise(analise);
                                        
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                parecer.setRequerente(requerente);

               
                listParecer.add(parecer);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParecer;
    }
        
    public boolean incluiParecer(ParecerAnalise parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiParecer);
            
            ps.setInt(1, parecer.getUsuario().getId());
            ps.setInt(2, parecer.getAnalise().getId());
            ps.setDate(3, parecer.getDataParecer());
            ps.setString(4, parecer.getTipo());
            ps.setString(5, parecer.getControle());
            if (parecer.getAnexosProcesso() == null) {
                ps.setNull(6, 1);
            } else {
                 ps.setInt(6, parecer.getAnexosProcesso().getId());
            }  
            if (parecer.getDataVistoria() == null) {
                ps.setNull(7, 1);
            } else {
                ps.setDate(7, parecer.getDataVistoria());
            }
            ps.setString(8, parecer.getAnexoIncluso());
            
            ps.executeUpdate();
            
            ps = con.prepareStatement(consultaUltimoId);
            rs = ps.executeQuery();
            rs.next();
            parecer.setId(rs.getInt("id"));
            mostraID = parecer.getId();
            
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alterarParecer(ParecerAnalise parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraParecer);

            ps.setInt(1, parecer.getAnalise().getId());
            ps.setDate(2, parecer.getDataParecer());
            ps.setString(3, parecer.getTipo());
            ps.setString(4, parecer.getControle());
            if (parecer.getAnexosProcesso() == null) {
                ps.setNull(5, 1);
            } else {
                 ps.setInt(5, parecer.getAnexosProcesso().getId());
            }  
            
            if (parecer.getDataVistoria() == null) {
                ps.setNull(6, 1);
            } else {
                ps.setDate(6, parecer.getDataVistoria());
            }
            
            ps.setString(7, parecer.getAnexoIncluso());
            ps.setInt(8, parecer.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean excluiParecer(ParecerAnalise parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiParecer);

            ps.setInt(1, parecer.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        }catch (Exception e) {
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
