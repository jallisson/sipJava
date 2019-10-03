/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.emissaolicenca;

import sip.acessobd.AcessoBD;
import sip.bairro.Bairro;
import sip.processo.Processo;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import sip.distribuicao.Distribuicao;
import sip.juridico.Juridico;

/**
 *
 * @author T2Ti
 */
public class EmissaoLicencaBD {

    private PreparedStatement ps;
    private PreparedStatement ps5;
    private PreparedStatement ps6;
    public Connection con;
    public ResultSet rs;
    public ResultSet rs6;
    public ResultSet rs7;
    private AcessoBD acessoBD = new AcessoBD();
    private String mostraId = "select * from emissao_licenca";
    private String mostraIdCon = "select * from emissao_licenca";
    //private String consultaEmissaoLicenca = "SELECT pro.*, req.*, juridico.*, usu.nome, emi.*, dist.* FROM emissao_licenca emi LEFT JOIN juridico juridico ON emi.id_juridico = juridico.id JOIN distribuicao dist ON juridico.id_distribuicao = dist.id JOIN usuario usu ON emi.id_usuario = usu.id  LEFT JOIN processo pro ON dist.id_processo = pro.id LEFT JOIN pro ON emi.id_processo = pro.id JOIN requerente req ON pro.id_requerente = req.id ORDER BY emi.id";
    private String consultaEmissaoLicenca = "SELECT pro.*, req.*, juridico.*, usu.nome, emi.*, dist.*, ativ.* FROM emissao_licenca emi LEFT JOIN juridico juridico ON emi.id_juridico = juridico.id LEFT JOIN distribuicao dist ON juridico.id_distribuicao = dist.id LEFT JOIN usuario usu ON emi.id_usuario = usu.id LEFT JOIN processo pro ON dist.id_processo = pro.id or emi.id_processo = pro.id LEFT JOIN atividade ativ ON pro.id_atividade = ativ.id  LEFT JOIN requerente req ON pro.id_requerente = req.id order BY emi.id";
    private String consultaEmissaoProcessoNome = "SELECT pro.*, req.*, juridico.*, usu.nome, emi.*, dist.*, ativ.* FROM emissao_licenca emi LEFT JOIN juridico juridico ON emi.id_juridico = juridico.id LEFT JOIN distribuicao dist ON juridico.id_distribuicao = dist.id LEFT JOIN usuario usu ON emi.id_usuario = usu.id LEFT JOIN processo pro ON dist.id_processo = pro.id or emi.id_processo = pro.id LEFT JOIN atividade ativ ON pro.id_atividade = ativ.id  LEFT JOIN requerente req ON pro.id_requerente = req.id where pro.num_processo like ? or req.nome like ? group by emi.id ORDER BY emi.id ";
    private String incluiEmissaoLicenca = "insert into emissao_licenca (id_usuario, id_juridico, data_emissao, mesano, area_terreno, area_construida, hora_inicial, hora_final, emitido, id_processo) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraEmissaoLicenca = "update emissao_licenca set id_usuario = ?,  data_emissao = ?, mesano = ?, area_terreno = ?, area_construida = ?, hora_inicial = ?, hora_final = ? where emissao_licenca.id = ?";
    private String alteraEmitido = "update emissao_licenca set emitido = ?, data_validade = ? where emissao_licenca.id = ?";
    //private String alteraEmTiImpresso = "update emissao_titulo set emissao = ? where emissao_titulo.id = ?";
    private String excluiEmissaoLicenca = "delete from emissao_licenca where emissao_licenca.id = ?";
    private String cont = "select count(*) from emissao_licenca.id";
    public int iD;

    public void mostraId(JFormattedTextField campoFor) {

        try {
            con = (Connection) acessoBD.conectar();
            ps5 = con.prepareStatement(mostraId);
            ps6 = con.prepareStatement(mostraIdCon);
            rs6 = ps5.executeQuery(mostraId);
            rs7 = ps6.executeQuery(mostraIdCon);

            if (rs6.first() == false) {
                campoFor.setValue((long) 1);

            }
            while (rs7.next()) {
                campoFor.setValue((long) rs7.getInt("id") + 1);
                iD = rs7.getInt("id")+1;
            }
            

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Não foi possivel fazer conexao!", "Erro conexao", JOptionPane.ERROR_MESSAGE);
        }
    }

    public List<EmissaoLicenca> consultaEmissao() {
        List<EmissaoLicenca> listaEmissao = new ArrayList<>();
        EmissaoLicenca emissao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaEmissaoLicenca);
            rs = ps.executeQuery();
            while (rs.next()) {
                emissao = new EmissaoLicenca();
                
                emissao.setId(rs.getInt("emi.id"));
                emissao.setDataEmissao(rs.getDate("emi.data_emissao"));
                emissao.setMesAno(rs.getString("emi.mesano"));
                emissao.setAreaTerreno(rs.getDouble("emi.area_terreno"));
                emissao.setAreaConstruida(rs.getDouble("emi.area_construida"));
                emissao.setHoraInicial(rs.getString("emi.hora_inicial"));
                emissao.setHoraFinal(rs.getString("emi.hora_final"));
                emissao.setEmitido(rs.getString("emi.emitido"));
                emissao.setDataValidade(rs.getDate("emi.data_validade"));

                //usuario
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                emissao.setProcesso(processo);

                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                emissao.setJuridico(juridico);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                emissao.setRequerente(requerente);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                emissao.setUsuario(usuario);

                listaEmissao.add(emissao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Sem dados do imovel!", "Erro", JOptionPane.ERROR_MESSAGE);

        }
        return listaEmissao;
    }

    public List<EmissaoLicenca> consultaEmissaoPro(String numero, String nome) {
        List<EmissaoLicenca> listaEmissao = new ArrayList<>();
        EmissaoLicenca emissao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaEmissaoProcessoNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);

            rs = ps.executeQuery();
            while (rs.next()) {
                emissao = new EmissaoLicenca();
                emissao.setId(rs.getInt("emi.id"));
                emissao.setDataEmissao(rs.getDate("emi.data_emissao"));
                emissao.setMesAno(rs.getString("emi.mesano"));
                emissao.setAreaTerreno(rs.getDouble("emi.area_terreno"));
                emissao.setAreaConstruida(rs.getDouble("emi.area_construida"));
                emissao.setHoraInicial(rs.getString("emi.hora_inicial"));
                emissao.setHoraFinal(rs.getString("emi.hora_final"));
                emissao.setEmitido(rs.getString("emi.emitido"));
                emissao.setDataValidade(rs.getDate("emi.data_validade"));

                //usuario
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                emissao.setProcesso(processo);

                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                emissao.setJuridico(juridico);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                emissao.setRequerente(requerente);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                emissao.setUsuario(usuario);

                listaEmissao.add(emissao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaEmissao;
    }

    public boolean incluiEmissaoLicenca(EmissaoLicenca emissao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiEmissaoLicenca);
            //id_usuario, id_lotetitulacao, id_processo, data_tramitacao, mesano, statuss, parecer, setor, observacao, controle

            ps.setInt(1, emissao.getUsuario().getId());
            //ps.setInt(2, emissao.getJuridico().getId());
            if (emissao.getJuridico() == null) {
                ps.setNull(2, 1);
            } else {
                ps.setInt(2, emissao.getJuridico().getId());
            }
            ps.setDate(3, emissao.getDataEmissao());
            ps.setString(4, emissao.getMesAno());
            ps.setDouble(5, emissao.getAreaTerreno());
            ps.setDouble(6, emissao.getAreaConstruida());
            ps.setString(7, emissao.getHoraInicial());
            ps.setString(8, emissao.getHoraFinal());
            ps.setString(9, emissao.getEmitido());
            if (emissao.getProcesso() == null) {
                ps.setNull(10, 1);
            } else {
                ps.setInt(10, emissao.getProcesso().getId());
            }

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraEmissaoLicenca(EmissaoLicenca emissao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraEmissaoLicenca);

            ps.setInt(1, emissao.getUsuario().getId());
            //ps.setInt(2, emissao.getJuridico().getId());
            ps.setDate(2, emissao.getDataEmissao());
            ps.setString(3, emissao.getMesAno());
            ps.setDouble(4, emissao.getAreaTerreno());
            ps.setDouble(5, emissao.getAreaConstruida());
            ps.setString(6, emissao.getHoraInicial());
            ps.setString(7, emissao.getHoraFinal());

            ps.setInt(8, emissao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraEmissaoLicencaEmitidoValidade(EmissaoLicenca emissao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraEmitido);

            ps.setString(1, emissao.getEmitido());
            ps.setDate(2, emissao.getDataValidade());

            ps.setInt(3, emissao.getId());


            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiEmissaoTitulo(EmissaoLicenca emissao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiEmissaoLicenca);

            ps.setInt(1, emissao.getId());

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
