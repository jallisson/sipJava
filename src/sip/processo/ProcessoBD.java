/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.processo;

import java.sql.SQLException;
import sip.acessobd.AcessoBD;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import sip.atividade.Atividade;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;

/**
 *
 * @author T2Ti
 */
public class ProcessoBD {

    private PreparedStatement ps;
    private PreparedStatement ps1;
    private PreparedStatement ps2;
    public Connection con;
    public ResultSet rs;
    public ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    //consulta referente aos anexos
    private String consultaAnexosProcesso = "SELECT anexosp.*, usu.nome, usu.setor, proc.id FROM processo proc LEFT JOIN anexos_processo anexosp ON  proc.id  = anexosp.id_processo LEFT JOIN usuario usu ON anexosp.id_usuario = usu.id WHERE anexosp.id_processo = ? GROUP BY anexosp.id";   
    //consulta referente aos anexos
   // private String consultaProcesso = "select pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor from processo pro left join requerente req on pro.id_requerente = req.id left join atividade ativ on pro.id_atividade = ativ.id join usuario usu on pro.id_usuario = usu.id order by pro.id desc";
    private String consultaProcesso = "SELECT pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor, pdenunciado.nome, denuncia.* FROM processo pro LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN atividade ativ ON pro.id_atividade = ativ.id JOIN usuario usu ON pro.id_usuario = usu.id ORDER BY pro.id DESC"; 
    private String consultaProcessoId = "select pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor from processo pro left join requerente req on pro.id_requerente = req.id left join atividade ativ on pro.id_atividade = ativ.id join usuario usu on pro.id_usuario = usu.id  where pro.id = ?";
    //private String consultaProcDeOnde = "select pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor from processo pro join requerente req on pro.id_requerente = req.id left join atividade ativ on pro.id_atividade = ativ.id join usuario usu on pro.id_usuario = usu.id where pro.tramitou_dma = 'SIM' order by pro.id desc";
    private String consultaProcDMA = "SELECT pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor, pdenunciado.nome, denuncia.* FROM processo pro LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN atividade ativ ON pro.id_atividade = ativ.id JOIN usuario usu ON pro.id_usuario = usu.id where pro.tramitou_dma = 'SIM' AND pro.lancado_dma = 'NAO' and pro.tipo_licenca = 'DENÚNCIA' order by pro.id desc";
    private String consultaProcNumNome = "SELECT pro.*, req.id, req.nome, ativ.id, ativ.nome, usu.id, usu.nome, usu.setor, pdenunciado.nome, denuncia.* FROM processo pro LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN atividade ativ ON pro.id_atividade = ativ.id JOIN usuario usu ON pro.id_usuario = usu.id where pro.num_processo like ? or req.nome like ? or pdenunciado.nome like ?";
    private String consultaUltimoReq = "select req.*, mreq.*, proc.* from mudanca_req mreq left join requerente req on mreq.id_requerente = req.id join processo proc on mreq.id_processo = proc.id where proc.id like ? ORDER BY mreq.id DESC LIMIT 1;";
    private String incluiAnexosProcesso = "insert into anexos_processo (id_processo, nome_arquivo, caminho_arquivo, id_usuario, descricao_anexo) values(?, ?, ?, ?, ?)";
    private String atualizaAnexoProcesso = "update imovel where imovel.id = ?";
    private String incluiProcesso = "insert into processo (num_processo, id_requerente, id_usuario, data_processo, mesano, tipo_licenca, tramitou_dma, id_atividade, arquivado, controle, lancado_dma, mp, id_denuncia, observacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String incluiMudancaReq = "insert into mudanca_req (id_requerente, id_processo, data_mudanca, id_usuario) values (?, ?, ?, ?)";
    private String alteraProcesso = "update processo set num_processo = ?, id_usuario = ?, data_processo = ?, mesano = ?, tipo_licenca = ?, id_atividade = ?, mp = ?, id_denuncia = ?, observacao = ? where processo.id = ?";
    private String alteraProcTramiDMA= "update processo set tramitou_dma = ? where processo.id = ?";
    private String alteraProcArquivado= "update processo set arquivado = ? where processo.id = ?";
    private String alteraProcLancadoDMA= "update processo set lancado_dma = ? where processo.id = ?";
    private String alteraAnexosProcesso= "update anexos_processo set nome_arquivo = ?, caminho_arquivo = ?, id_usuario = ? where anexos_processo.id = ?";
    private String alteraRequerente = "update processo set id_requerente = ?, id_usuario = ? where processo.id = ?";
    private String excluiProcesso = "delete from processo where processo.id = ?";
    private String excluiAnexosProcesso ="delete from anexos_processo where id = ?";
    private String LastId = "SELECT MAX(ID) AS id FROM processo";
    
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

    public List<Processo> consultaProcesso(String consultaDeOnde) {
        List<Processo> listaProcesso = new ArrayList<>();
        Processo processo;
        try {
            con = acessoBD.conectar();
              if(null == consultaDeOnde){
                  ps = con.prepareStatement(consultaProcesso);
              }else switch (consultaDeOnde) {
                case "ProcessoFrame":
                    ps = con.prepareStatement(consultaProcesso);
                    break;
                case "DistribuicaoFrame":
                    ps = con.prepareStatement(consultaProcDMA);
                    break;
                default:
                    ps = con.prepareStatement(consultaProcesso);
                    break;
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setDataProcesso(rs.getDate("pro.data_processo"));
                processo.setMesAno(rs.getString("pro.mesano"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                processo.setControle(rs.getString("pro.controle"));
                processo.setLancadoDma(rs.getString("pro.lancado_dma"));
                processo.setMp(rs.getString("pro.mp"));
                processo.setObservacao(rs.getString("pro.observacao"));
                

                //processo.setControleDigitador(rs.getString("pro.controle_dig"));

                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                processo.setRequerente(requerente);
                
                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("ativ.id"));
                atividade.setNome(rs.getString("ativ.nome"));
                
                processo.setAtividade(atividade);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));
                usuario.setSetor(rs.getString("usu.setor"));

                processo.setUsuario(usuario);

                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                processo.setPessoa(pessoa);
                
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                denuncia.setStatusApp(rs.getString("denuncia.status_app"));
                processo.setDenuncia(denuncia);
                    
                listaProcesso.add(processo);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProcesso;
    }

    public List<Processo> consultaProcessoNum(String numero, String nome) {
        List<Processo> listaProcesso = new ArrayList<>();
        Processo processo;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaProcNumNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
            ps.setString(3, nome);

            rs = ps.executeQuery();
            while (rs.next()) {
                processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setDataProcesso(rs.getDate("pro.data_processo"));
                processo.setMesAno(rs.getString("pro.mesano"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                processo.setControle(rs.getString("pro.controle"));
                processo.setMp(rs.getString("pro.mp"));
                 processo.setObservacao(rs.getString("pro.observacao"));

                //processo.setControleDigitador(rs.getString("pro.controle_dig"));

                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                processo.setRequerente(requerente);
                
                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("ativ.id"));
                atividade.setNome(rs.getString("ativ.nome"));
                
                processo.setAtividade(atividade);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));

                processo.setUsuario(usuario);
                
                  Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                processo.setPessoa(pessoa);
                
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                denuncia.setStatusApp(rs.getString("denuncia.status_app"));
                processo.setDenuncia(denuncia);

                listaProcesso.add(processo);

            }

            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaProcesso;
    }
    
    

    public List<MudancaReq> consultaUltimoRequerente(String id) {
        List<MudancaReq> listMudanca = new ArrayList<>();
        MudancaReq mudancaReq;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaUltimoReq);
            id = "" + id + "%";
            ps.setString(1, id);

            rs = ps.executeQuery();
            while (rs.next()) {
                mudancaReq = new MudancaReq();
                mudancaReq.setId(rs.getInt("mreq.id"));


                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                mudancaReq.setRequerente(requerente);


                listMudanca.add(mudancaReq);

            }

            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listMudanca;
    }

    public List<Processo> consultaAnexosProcesso() {
        List<Processo> listProcesso = new ArrayList<>();
        Processo processo;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaProcesso);
            rs = ps.executeQuery();
            while (rs.next()) {
                processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setDataProcesso(rs.getDate("pro.data_processo"));
                processo.setMesAno(rs.getString("pro.mesano"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                 processo.setControle(rs.getString("pro.controle"));
                  processo.setMp(rs.getString("pro.mp"));
                   processo.setObservacao(rs.getString("pro.observacao"));
                //processo.setNomeArquivo(rs.getString("pro.nome_arquivo"));
                //processo.setCaminhoArquivo(rs.getString("pro.caminho_arquivo"));

                //processo.setControleDigitador(rs.getString("pro.controle_dig"));

                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                processo.setRequerente(requerente);

                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("ativ.id"));
                atividade.setNome(rs.getString("ativ.nome"));
                
                processo.setAtividade(atividade);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));
                usuario.setSetor(rs.getString("usu.setor"));

                processo.setUsuario(usuario);

    
                
                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, processo.getId());
                rs1 = ps.executeQuery();

                List<AnexosProcesso> listAnexosProcesso = new ArrayList<>();
                while (rs1.next()) {
                    AnexosProcesso anexosProcesso = new AnexosProcesso();

                    anexosProcesso.setId(rs1.getInt("anexosp.id"));
                    anexosProcesso.setDescricaoAnexo(rs1.getString("anexosp.descricao_anexo"));
                    anexosProcesso.setNomeArquivo(rs1.getString("anexosp.nome_arquivo"));
                    anexosProcesso.setCaminhoArquivo(rs1.getString("anexosp.caminho_arquivo"));
                    
                     Usuario usu= new Usuario();
                     usu.setNome(rs1.getString("usu.nome"));
                     usu.setSetor(rs1.getString("usu.setor"));
                                         
                     anexosProcesso.setUsuario(usu);
                    
                    listAnexosProcesso.add(anexosProcesso);

                }
                processo.setAnexosProcesso(listAnexosProcesso);

                listProcesso.add(processo);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProcesso;
    }
    
    public List<Processo> consultaAnexosProcessoId(String idProcesso) {
        List<Processo> listProcesso = new ArrayList<>();
        Processo processo;
        try {
            String id = idProcesso;
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaProcessoId);
            idProcesso = "" + idProcesso + "";
            ps.setString(1, idProcesso);
            rs = ps.executeQuery();
            while (rs.next()) {
                processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setDataProcesso(rs.getDate("pro.data_processo"));
                processo.setMesAno(rs.getString("pro.mesano"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                 processo.setArquivado(rs.getString("pro.arquivado"));
                 processo.setControle(rs.getString("pro.controle"));
                  processo.setMp(rs.getString("pro.mp"));
                   processo.setObservacao(rs.getString("pro.observacao"));
                //processo.setNomeArquivo(rs.getString("pro.nome_arquivo"));
                //processo.setCaminhoArquivo(rs.getString("pro.caminho_arquivo"));

                //processo.setControleDigitador(rs.getString("pro.controle_dig"));

                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                processo.setRequerente(requerente);

                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("ativ.id"));
                atividade.setNome(rs.getString("ativ.nome"));
                
                processo.setAtividade(atividade);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));
                usuario.setSetor(rs.getString("usu.setor"));

                processo.setUsuario(usuario);

    
                
                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, processo.getId());
                rs1 = ps.executeQuery();

                List<AnexosProcesso> listAnexosProcesso = new ArrayList<>();
                while (rs1.next()) {
                    AnexosProcesso anexosProcesso = new AnexosProcesso();

                    anexosProcesso.setId(rs1.getInt("anexosp.id"));
                    anexosProcesso.setDescricaoAnexo(rs1.getString("anexosp.descricao_anexo"));
                    anexosProcesso.setNomeArquivo(rs1.getString("anexosp.nome_arquivo"));
                    anexosProcesso.setCaminhoArquivo(rs1.getString("anexosp.caminho_arquivo"));
                    
                     Usuario usu= new Usuario();
                     usu.setNome(rs1.getString("usu.nome"));
                     usu.setSetor(rs1.getString("usu.setor"));
                                         
                     anexosProcesso.setUsuario(usu);
                    
                    listAnexosProcesso.add(anexosProcesso);

                }
                processo.setAnexosProcesso(listAnexosProcesso);

                listProcesso.add(processo);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProcesso;
    }

    public List<Processo> consultaAnexosProcessoNome(String numero, String nome) {
        List<Processo> listProcesso = new ArrayList<>();
        Processo processo;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaProcNumNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);

            rs = ps.executeQuery();
            while (rs.next()) {
                processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setDataProcesso(rs.getDate("pro.data_processo"));
                processo.setMesAno(rs.getString("pro.mesano"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                processo.setControle(rs.getString("pro.controle"));
                 processo.setMp(rs.getString("pro.mp"));
                 processo.setObservacao(rs.getString("pro.observacao"));

                //processo.setControleDigitador(rs.getString("pro.controle_dig"));

                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));

                processo.setRequerente(requerente);
                
                Atividade atividade = new Atividade();
                atividade.setId(rs.getInt("ativ.id"));
                atividade.setNome(rs.getString("ativ.nome"));
                
                processo.setAtividade(atividade);

                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setNome(rs.getString("usu.nome"));

                processo.setUsuario(usuario);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                processo.setPessoa(pessoa);
                
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setStatusApp(rs.getString("denuncia.status_app"));
                processo.setDenuncia(denuncia);

                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, processo.getId());
                rs1 = ps.executeQuery();

                List<AnexosProcesso> listAnexosProcesso = new ArrayList<>();
                while (rs1.next()) {
                    AnexosProcesso anexosProcesso = new AnexosProcesso();

                    anexosProcesso.setId(rs1.getInt("anexosp.id"));
                    anexosProcesso.setDescricaoAnexo(rs1.getString("anexosp.descricao_anexo"));
                    anexosProcesso.setNomeArquivo(rs1.getString("anexosp.nome_arquivo"));
                    anexosProcesso.setCaminhoArquivo(rs1.getString("anexosp.caminho_arquivo"));
                    
                    Usuario usu= new Usuario();
                    usu.setNome(rs.getString("usu.nome"));
                    usu.setSetor(rs.getString("usu.setor"));
                                         
                    anexosProcesso.setUsuario(usu);

                    listAnexosProcesso.add(anexosProcesso);

                }
                processo.setAnexosProcesso(listAnexosProcesso);

                listProcesso.add(processo);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listProcesso;
    }

    public boolean incluiProcesso(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiProcesso);
            //num_processo, controle_dig, id_requerente, id_digitador, valor, data_processo, mesano  
            ps.setString(1, processo.getNumProcesso());
              if (processo.getRequerente() == null) {
                ps.setNull(2, 1);
            } else {
                ps.setInt(2, processo.getRequerente().getId());
            }
            ps.setInt(3, processo.getUsuario().getId());
            ps.setDate(4, processo.getDataProcesso());
            ps.setString(5, processo.getMesAno());
            ps.setString(6, processo.getTipoLicenca());
            ps.setString(7, processo.getTramitouDMA());
            
            if (processo.getAtividade() == null) {
                ps.setNull(8, 1);
            } else {
                ps.setInt(8, processo.getAtividade().getId());
            }
            ps.setString(9, processo.getArquivado());
            ps.setString(10, processo.getControle());
            ps.setString(11, processo.getLancadoDma());
             if (processo.getMp() == null) {
                ps.setNull(12, 1);
            } else {
                ps.setString(12, processo.getMp());
            }
              if (processo.getDenuncia()== null) {
                ps.setNull(13, 1);
            } else {
                ps.setInt(13, processo.getDenuncia().getId());
            }
            ps.setString(14, processo.getObservacao());
             
            /*
            if (processo.getCaminhoArquivo() == null) {
                ps.setNull(8, 1);
            } else {
                ps.setString(8, processo.getCaminhoArquivo());
            }

            //ps.setString(3, monitor.getTelefone());
             */
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean IncluirAnexosProcesso(Processo processo) {
        try {
            con = acessoBD.conectar();
            for (int i = 0; i < processo.getAnexosProcesso().size(); i++) {
                ps2 = con.prepareStatement(incluiAnexosProcesso);
                ps2.setInt(1, processo.getId());
                ps2.setString(2, processo.getAnexosProcesso().get(i).getNomeArquivo());
                ps2.setString(3, processo.getAnexosProcesso().get(i).getCaminhoArquivo());
                ps2.setInt(4, processo.getUsuario().getId());
                 if ( processo.getAnexosProcesso().get(i).getDescricaoAnexo() == null) {
                ps2.setNull(5, 1);
                     } else {
                ps2.setString(5, processo.getAnexosProcesso().get(i).getDescricaoAnexo());
                    }
                

                ps2.executeUpdate();
            }

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Duplicidade de Cadastro Imobiliario!", "Erro", JOptionPane.ERROR_MESSAGE); 
        }
        return false;
    }
    
    public boolean AlterarAnexosProcesso(Processo processo) {
        try {
            con = acessoBD.conectar();
            for (int i = 0; i < processo.getAnexosProcesso().size(); i++) {
                ps2 = con.prepareStatement(alteraAnexosProcesso);
                //ps2.setInt(1, processo.getId());
                ps2.setString(1, processo.getAnexosProcesso().get(i).getNomeArquivo());
                ps2.setString(2, processo.getAnexosProcesso().get(i).getCaminhoArquivo());
                ps2.setInt(3, processo.getAnexosProcesso().get(i).getUsuario().getId());
                ps2.setInt(4, processo.getAnexosProcesso().get(i).getId());

                ps2.executeUpdate();
            }

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            //JOptionPane.showMessageDialog(null, "Duplicidade de Cadastro Imobiliario!", "Erro", JOptionPane.ERROR_MESSAGE); 
        }
        return false;
    }

    public boolean alteraProcessoRequerente(Processo processo, MudancaReq mudancaReq) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraRequerente);


            ps.setInt(1, processo.getRequerente().getId());
            ps.setInt(2, processo.getUsuario().getId());

            ps.setInt(3, processo.getId());
            ps.executeUpdate();

            ps1 = con.prepareStatement(incluiMudancaReq);

            ps1.setInt(1, mudancaReq.getRequerente().getId());
            ps1.setInt(2, mudancaReq.getProcesso().getId());
            ps1.setDate(3, mudancaReq.getDataMudanca());
            ps1.setInt(4, mudancaReq.getUsuario().getId());



            ps1.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean alteraProcesso(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraProcesso);

            ps.setString(1, processo.getNumProcesso());
            ps.setInt(2, processo.getUsuario().getId());
            ps.setDate(3, processo.getDataProcesso());
            ps.setString(4, processo.getMesAno());
            ps.setString(5, processo.getTipoLicenca());
              if (processo.getAtividade() == null) {
                ps.setNull(6, 1);
            } else {
                ps.setInt(6, processo.getAtividade().getId());
            }
              if (processo.getMp() == null) {
                ps.setNull(7, 1);
            } else {
                ps.setString(7, processo.getMp());
            }
              if (processo.getDenuncia()== null) {
                ps.setNull(8, 1);
            } else {
                ps.setInt(8, processo.getDenuncia().getId());
            }
              ps.setString(9, processo.getObservacao());
           /*
            if (processo.getNomeArquivo() == null) {
                ps.setNull(6, 1);
            } else {
                ps.setString(6, processo.getNomeArquivo());
            }

            if (processo.getCaminhoArquivo() == null) {
                ps.setNull(7, 1);
            } else {
                ps.setString(7, processo.getCaminhoArquivo());
            }
            */
            ps.setInt(10, processo.getId());

            ps.executeUpdate();


            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraProcTramiDMA(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraProcTramiDMA);

            ps.setString(1, processo.getTramitouDMA());
            ps.setInt(2, processo.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraProcArquivado(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraProcArquivado);

            ps.setString(1, processo.getArquivado());
            ps.setInt(2, processo.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraProcLancadoDMA(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraProcLancadoDMA);

            ps.setString(1, processo.getLancadoDma());
            ps.setInt(2, processo.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiProcesso(Processo processo) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiProcesso);

            ps.setInt(1, processo.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
    
    public boolean excluiAnexosProcesso(AnexosProcesso anexosProcesso) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiAnexosProcesso);
     
            ps.setInt(1, anexosProcesso.getId());

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
