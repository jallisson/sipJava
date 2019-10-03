/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.autorizacaoeventos;

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
import sip.localevento.LocalEvento;
import sip.logradouro.Logradouro;
import sip.tipoevento.TipoEvento;

/**
 *
 * @author T2Ti
 */
public class AutorizacaoEventosBD {

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
    private String consultaAutorizacaoEventos = "SELECT pro.*, req.*, juridico.*, usu.nome, aevt.*, dist.*, bai.*, logra.*, localevt.*, tipoevt.* FROM autorizacao_eventos aevt LEFT JOIN juridico juridico ON aevt.id_juridico = juridico.id LEFT JOIN distribuicao dist ON juridico.id_distribuicao = dist.id LEFT JOIN usuario usu ON aevt.id_usuario = usu.id LEFT JOIN processo pro ON dist.id_processo = pro.id or aevt.id_processo = pro.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN bairro bai ON aevt.id_bairro = bai.id LEFT JOIN logradouro logra ON aevt.id_logradouro = logra.id LEFT JOIN local_evento localevt ON aevt.id_local_evento = localevt.id LEFT JOIN tipo_evento tipoevt ON aevt.id_tipo_evento = tipoevt.id order BY aevt.id";
    private String consultaAutorizacaoProcessoNome = "SELECT pro.*, req.*, juridico.*, usu.nome, aevt.*, dist.*, bai.*, logra.*, localevt.*, tipoevt.* FROM autorizacao_eventos aevt LEFT JOIN juridico juridico ON aevt.id_juridico = juridico.id LEFT JOIN distribuicao dist ON juridico.id_distribuicao = dist.id LEFT JOIN usuario usu ON aevt.id_usuario = usu.id LEFT JOIN processo pro ON dist.id_processo = pro.id or aevt.id_processo = pro.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN bairro bai ON aevt.id_bairro = bai.id LEFT JOIN logradouro logra ON aevt.id_logradouro = logra.id JOIN bairro bai ON aevt.id_bairro = bai.id LEFT JOIN logradouro logra ON aevt.id_logradouro = logra.id LEFT JOIN local_evento localevt ON aevt.id_local_evento = localevt.id LEFT JOIN tipo_evento tipoevt ON aevt.id_tipo_evento = tipoevt.id where pro.num_processo like ? or req.nome like ? group by emi.id ORDER BY emi.id ";
    private String incluiAutorizacaoEventos = "insert into autorizacao_eventos (id_usuario, id_juridico, id_processo, id_tipo_evento, id_logradouro, id_bairro, nome_evento, hora_inicial, hora_final, descricao_area, estimativa, data_evento, mesano, emitido, id_local_evento) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraAutorizacaoEventos = "update autorizacao_eventos set id_usuario = ?, id_tipo_evento = ?, id_logradouro = ?, id_bairro = ?, nome_evento = ?, hora_inicial = ?, hora_final = ?, descricao_area = ?, estimativa = ?, data_evento = ?, mesano = ?, id_local_evento = ? where autorizacao_eventos.id = ?";
    private String alteraEmitido = "update autorizacao_eventos set emitido = ? where autorizacao_eventos.id = ?";
    //private String alteraEmTiImpresso = "update emissao_titulo set emissao = ? where emissao_titulo.id = ?";
    private String excluiAutorizacaoEventos = "delete from autorizacao_eventos where autorizacao_eventos.id = ?";
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

    public List<AutorizacaoEventos> consultaAutorizacao() {
        List<AutorizacaoEventos> listaAutorizacao = new ArrayList<>();
        AutorizacaoEventos autorizacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAutorizacaoEventos);
            rs = ps.executeQuery();
            while (rs.next()) {
                autorizacao = new AutorizacaoEventos();
                
                autorizacao.setId(rs.getInt("aevt.id"));
                autorizacao.setDataEvento(rs.getDate("aevt.data_evento"));
                autorizacao.setMesAno(rs.getString("aevt.mesano"));
                autorizacao.setHoraInicial(rs.getString("aevt.hora_inicial"));
                autorizacao.setHoraFinal(rs.getString("aevt.hora_final"));
                autorizacao.setEmitido(rs.getString("aevt.emitido"));
                autorizacao.setDescricaoArea(rs.getString("aevt.descricao_area"));
                autorizacao.setEstimativa(rs.getInt("aevt.estimativa"));
                autorizacao.setNomeEvento(rs.getString("aevt.nome_evento"));
                
               
                //usuario
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                autorizacao.setProcesso(processo);

                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                autorizacao.setJuridico(juridico);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                autorizacao.setRequerente(requerente);
                
                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));
                autorizacao.setBairro(bairro);

                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));
                autorizacao.setLogradouro(logradouro);
                
                LocalEvento localEvento = new LocalEvento();
                localEvento.setId(rs.getInt("localevt.id"));
                localEvento.setNome(rs.getString("localevt.nome"));
                autorizacao.setLocalEvento(localEvento);
                
                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(rs.getInt("tipoEvt.id"));
                tipoEvento.setNome(rs.getString("tipoEvt.nome"));
                autorizacao.setTipoEvento(tipoEvento);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                autorizacao.setUsuario(usuario);

                listaAutorizacao.add(autorizacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Sem dados do imovel!", "Erro", JOptionPane.ERROR_MESSAGE);

        }
        return listaAutorizacao;
    }

    public List<AutorizacaoEventos> consultaEmissaoPro(String numero, String nome) {
        List<AutorizacaoEventos> listaAutorizacao = new ArrayList<>();
        AutorizacaoEventos autorizacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaAutorizacaoProcessoNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);

            rs = ps.executeQuery();
            while (rs.next()) {
                  autorizacao = new AutorizacaoEventos();
                autorizacao.setId(rs.getInt("aevt.id"));
                autorizacao.setDataEvento(rs.getDate("aevt.data_evento"));
                autorizacao.setMesAno(rs.getString("aevt.mesano"));
                autorizacao.setHoraInicial(rs.getString("aevt.hora_inicial"));
                autorizacao.setHoraFinal(rs.getString("aevt.hora_final"));
                autorizacao.setEmitido(rs.getString("aevt.emitido"));
                autorizacao.setDescricaoArea(rs.getString("aevt.descricao_area"));
                autorizacao.setEstimativa(rs.getInt("aevt.estimativa"));
                autorizacao.setNomeEvento(rs.getString("aevt.nome_evento"));
                
               
                //usuario
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                autorizacao.setProcesso(processo);

                Juridico juridico = new Juridico();
                juridico.setId(rs.getInt("juridico.id"));
                autorizacao.setJuridico(juridico);

                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                autorizacao.setRequerente(requerente);
                
                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));
                autorizacao.setBairro(bairro);

                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));
                autorizacao.setLogradouro(logradouro);
                
                LocalEvento localEvento = new LocalEvento();
                localEvento.setId(rs.getInt("localevt.id"));
                localEvento.setNome(rs.getString("localevt.nome"));
                autorizacao.setLocalEvento(localEvento);
                
                TipoEvento tipoEvento = new TipoEvento();
                tipoEvento.setId(rs.getInt("tipoEvt.id"));
                tipoEvento.setNome(rs.getString("tipoEvt.nome"));
                autorizacao.setTipoEvento(tipoEvento);

                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                autorizacao.setUsuario(usuario);

                listaAutorizacao.add(autorizacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaAutorizacao;
    }

    public boolean incluiAutorizacaoEventos(AutorizacaoEventos autorizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiAutorizacaoEventos);
            //id_usuario, id_lotetitulacao, id_processo, data_tramitacao, mesano, statuss, parecer, setor, observacao, controle

            ps.setInt(1, autorizacao.getUsuario().getId());
            //ps.setInt(2, emissao.getJuridico().getId());
            if (autorizacao.getJuridico() == null) {
                ps.setNull(2, 1);
            } else {
                ps.setInt(2, autorizacao.getJuridico().getId());
            }
            if (autorizacao.getProcesso() == null) {
                ps.setNull(3, 1);
            } else {
                ps.setInt(3, autorizacao.getProcesso().getId());
            }
            ps.setInt(4, autorizacao.getTipoEvento().getId());           
            if (autorizacao.getLogradouro() == null) {
                ps.setNull(5, 1);
            } else {
                ps.setInt(5, autorizacao.getLogradouro().getId());
            }
            ps.setInt(6, autorizacao.getBairro().getId());
            ps.setString(7, autorizacao.getNomeEvento());
            ps.setString(8, autorizacao.getHoraInicial());
            ps.setString(9, autorizacao.getHoraFinal());
            ps.setString(10, autorizacao.getDescricaoArea());
            ps.setInt(11, autorizacao.getEstimativa());
            ps.setDate(12, autorizacao.getDataEvento());
            ps.setString(13, autorizacao.getMesAno());
            ps.setString(14, autorizacao.getEmitido());
             if (autorizacao.getLocalEvento()== null) {
                ps.setNull(15, 1);
            } else {
                ps.setInt(15, autorizacao.getLocalEvento().getId());
            }
            
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alterarAutorizacaoEventos(AutorizacaoEventos autorizacao) {
        try {
            con = acessoBD.conectar();
            
            ps = con.prepareStatement(alteraAutorizacaoEventos);
           
            ps.setInt(1, autorizacao.getUsuario().getId());           
            ps.setInt(2, autorizacao.getTipoEvento().getId());           
            if (autorizacao.getLogradouro() == null) {
                ps.setNull(3, 1);
            } else {
                ps.setInt(3, autorizacao.getLogradouro().getId());
            }
            ps.setInt(4, autorizacao.getBairro().getId());
            ps.setString(5, autorizacao.getNomeEvento());
            ps.setString(6, autorizacao.getHoraInicial());
            ps.setString(7, autorizacao.getHoraFinal());
            ps.setString(8, autorizacao.getDescricaoArea());
            ps.setInt(9, autorizacao.getEstimativa());
            ps.setDate(10, autorizacao.getDataEvento());
            ps.setString(11, autorizacao.getMesAno());
             if (autorizacao.getLocalEvento()== null) {
                ps.setNull(12, 1);
            } else {
                ps.setInt(12, autorizacao.getLocalEvento().getId());
            }
            ps.setInt(13, autorizacao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraAutorizacaoEventosEmitido(AutorizacaoEventos autorizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraEmitido);

            ps.setString(1, autorizacao.getEmitido());

            ps.setInt(2, autorizacao.getId());


            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiAutorizacaoEventos(AutorizacaoEventos autorizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiAutorizacaoEventos);

            ps.setInt(1, autorizacao.getId());

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
