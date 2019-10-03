/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.juridico;

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
public class ParecerJuridicoBD {

    private PreparedStatement ps;
    private Connection con;
    private ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    //private String consultaLaudoImovel = "select *from laudo_imovel";
    private String consultaParecer = "SELECT pro.*, req.*, juridico.*, usu.nome, parecer.*, dist.*, anexos.descricao_anexo, anexos.nome_arquivo FROM parecer_juridico parecer JOIN juridico juridico ON parecer.id_juridico = juridico.id JOIN distribuicao dist ON juridico.id_distribuicao = dist.id JOIN usuario usu ON parecer.id_usuario = usu.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON parecer.id_anexos_processo = anexos.id ORDER BY parecer.id";
    private String consultaParecerProcesso = "SELECT pro.*, req.*, juridico.*, usu.nome, parecer.*, dist.*, anexos.descricao_anexo, anexos.nome_arquivo FROM parecer_juridico parecer JOIN juridico juridico ON parecer.id_juridico = juridico.id JOIN distribuicao dist ON juridico.id_distribuicao = dist.id JOIN usuario usu ON parecer.id_usuario = usu.id  JOIN processo pro ON dist.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON parecer.id_anexos_processo = anexos.id  where pro.num_processo like ? ORDER BY parecer.id";
    private String incluiParecer = "insert into parecer_juridico (id_usuario, id_juridico, data_parecer, parecer, controle, id_anexos_processo, anexo_incluso, tipo) values(?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraParecer = "update parecer_juridico set id_juridico = ?, data_parecer = ?, parecer = ?, controle = ?, id_anexos_processo = ?, anexo_incluso =  ?, tipo = ? where parecer_juridico.id = ?";
    private String excluiParecer = "delete from parecer_juridico where parecer_juridico.id = ?";
    private String consultaUltimoId = "select LAST_INSERT_ID() as id from parecer_juridico";
    private String LastId = "SELECT MAX(ID) AS id FROM parecer_juridico";
    int mostraID;

    public int getLastId() {
        con = acessoBD.conectar();
        int lastId = 0;
        try {
            ps = con.prepareStatement(LastId);
            rs = ps.executeQuery();
            rs.next();
            lastId = rs.getInt("id");
            rs.close();
            ps.close();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return lastId;
        //acessoBD.desconectar();

    }

    public List<ParecerJuridico> consultaParecer() {
        List<ParecerJuridico> listParecer = new ArrayList<>();
        ParecerJuridico parecer;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaParecer);
            rs = ps.executeQuery();
            while (rs.next()) {
                parecer = new ParecerJuridico();
                parecer.setId(rs.getInt("parecer.id"));
                parecer.setDataParecer(rs.getDate("parecer.data_parecer"));
                parecer.setParecer(rs.getString("parecer.parecer"));
                parecer.setTipo(rs.getString("parecer.tipo"));

                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                parecer.setProcesso(processo);

                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                parecer.setJuridico(juridico);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                parecer.setRequerente(requerente);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                parecer.setUsuario(usuario);

                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                parecer.setAnexosProcesso(anexo);


                listParecer.add(parecer);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParecer;
    }

    public List<ParecerJuridico> consultaParecerNome(String Numprocesso) {
        List<ParecerJuridico> listParecer = new ArrayList<>();
        ParecerJuridico parecer;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaParecerProcesso);
            Numprocesso = "%" + Numprocesso + "%";
            ps.setString(1, Numprocesso);
            rs = ps.executeQuery();
            while (rs.next()) {
                parecer = new ParecerJuridico();
                parecer.setId(rs.getInt("parecer.id"));
                parecer.setDataParecer(rs.getDate("parecer.data_parecer"));
                parecer.setParecer(rs.getString("parecer.parecer"));
                parecer.setTipo(rs.getString("parecer.tipo"));

                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                parecer.setProcesso(processo);

                Juridico analise = new Juridico();
                analise.setId(rs.getInt("analise.id"));
                parecer.setJuridico(analise);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));

                parecer.setRequerente(requerente);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                parecer.setUsuario(usuario);

                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                parecer.setAnexosProcesso(anexo);

                listParecer.add(parecer);

            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listParecer;
    }

    public boolean incluiParecer(ParecerJuridico parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiParecer);

            ps.setInt(1, parecer.getUsuario().getId());
            ps.setInt(2, parecer.getJuridico().getId());
            ps.setDate(3, parecer.getDataParecer());
            ps.setString(4, parecer.getParecer());
            ps.setString(5, parecer.getControle());
            if (parecer.getAnexosProcesso() == null) {
                ps.setNull(6, 1);
            } else {
                ps.setInt(6, parecer.getAnexosProcesso().getId());
            }
            ps.setString(7, parecer.getAnexoIncluso());
            ps.setString(8, parecer.getTipo());

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

    public boolean alterarParecer(ParecerJuridico parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraParecer);
            //update parecer_juridico set id_juridico = ?, data_parecer = ?, parecer = ?, controle = ?, id_anexos_processo = ?, anexo_incluso =  ?, tipo = ? where parecer_juridico.id = ?";
            //ps.setInt(1, parecer.getUsuario().getId());
            ps.setInt(1, parecer.getJuridico().getId());
            ps.setDate(2, parecer.getDataParecer());
            ps.setString(3, parecer.getParecer());
            ps.setString(4, parecer.getControle());
            if (parecer.getAnexosProcesso() == null) {
                ps.setNull(5, 1);
            } else {
                ps.setInt(5, parecer.getAnexosProcesso().getId());
            }
            ps.setString(6, parecer.getAnexoIncluso());
            ps.setString(7, parecer.getTipo());            
            ps.setInt(8, parecer.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiParecer(ParecerJuridico parecer) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiParecer);

            ps.setInt(1, parecer.getId());

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
