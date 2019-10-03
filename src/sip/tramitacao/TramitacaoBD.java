/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.tramitacao;

import sip.acessobd.AcessoBD;
import sip.processo.Processo;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import sip.bairro.Bairro;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;
import sip.processo.AnexosProcesso;

/**
 *
 * @author T2Ti
 */
public class TramitacaoBD {

    private PreparedStatement ps;
    public Connection con;
    public ResultSet rs;
    public ResultSet rs1;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaTramitacao = "SELECT tram.*, pro.*, req.*, usu.*, anexos.descricao_anexo, anexos.nome_arquivo,  pdenunciado.nome, denuncia.* FROM tramitacao tram JOIN usuario usu ON tram.id_usuario = usu.id JOIN processo pro ON tram.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON tram.id_anexos_processo = anexos.id GROUP BY tram.id ORDER BY tram.id DESC";
    private String consultaTramitacaoProcessoRequerente = "SELECT tram.*, pro.*, req.*, usu.*, anexos.descricao_anexo, anexos.nome_arquivo,  pdenunciado.nome, denuncia.* FROM tramitacao tram JOIN usuario usu ON tram.id_usuario = usu.id JOIN processo pro ON tram.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON tram.id_anexos_processo = anexos.id where pro.num_processo like ? or req.nome like ? group by tram.id order by tram.id asc";
    private String consultaTramUltimoIdProce = "select tram.*, pro.*, lot.*, usu.* from tramitacao tram left join lote_titulacao lot on tram.id_lotetitulacao = lot.id join usuario usu on tram.id_usuario = usu.id join processo pro on tram.id_processo = pro.id where pro.id like ? ORDER BY tram.id DESC LIMIT 1";
    private String consultaTramUltimoNumProce = "SELECT tram.*, pro.*, req.*, usu.*, anexos.descricao_anexo, anexos.nome_arquivo FROM tramitacao tram JOIN usuario usu ON tram.id_usuario = usu.id JOIN processo pro ON tram.id_processo = pro.id  JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN anexos_processo anexos ON tram.id_anexos_processo = anexos.id where pro.num_processo like ? ORDER BY tram.id DESC LIMIT 1";
    private String consultaAnexosProcesso = "SELECT anexosp.*, usu.nome, usu.setor, trami.id FROM tramitacao trami LEFT JOIN anexos_processo anexosp ON anexosp.id = trami.id_anexos_processo LEFT JOIN usuario usu ON anexosp.id_usuario = usu.id WHERE trami.id = ? GROUP BY anexosp.id";
    private String incluiTramitacao = "insert into tramitacao (id_usuario, id_processo, data_tramitacao, mesano, statuss, parecer, setor, setor_origem, setor_destino, observacao, controle, id_emissao_licenca, id_autorizacao_eventos, id_anexos_processo, id_parecer_analise, id_parecer_juridico, id_parecer_fiscalizacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
      
    private String alteraTramitacao = "update tramitacao set id_usuario = ?, id_processo = ?, data_tramitacao = ?, hora = ?, mesano = ?, statuss = ?, parecer = ?, setor = ?, setor_origem = ?, setor_destino = ?, observacao = ?, controle = ? where tramitacao.id = ?";
    private String alteraTramAnexoParecerAnalise = "UPDATE tramitacao SET id_anexos_processo = ? WHERE tramitacao.id_parecer_analise = ? AND tramitacao.statuss = 'PARECER TECNICO CONCLUIDO' or tramitacao.statuss = 'PARECER CONCLUSIVO CONCLUIDO' or tramitacao.statuss = 'NOTIFICAÇÃO CONCLUIDO'";
    private String alteraTramAnexoParecerJuridico = "UPDATE tramitacao SET id_anexos_processo = ? WHERE tramitacao.id_parecer_juridico = ? AND tramitacao.statuss = 'PARECER JURÍDICO CONCLUIDO' or tramitacao.statuss = 'NOTIFICAÇÃO CONCLUIDO'"; 
    private String alteraTramAnexoParecerFiscalizacao = "UPDATE tramitacao SET id_anexos_processo = ? WHERE tramitacao.id_parecer_fiscalizacao = ? AND tramitacao.statuss = 'LAUDO TÉCNICO CONCLUIDO' or tramitacao.statuss = 'AUTO DE INFRAÇÃO CONCLUIDO'"; 
    private String excluiTramitacao = "delete from tramitacao where tramitacao.id = ?";
    private String cont = "select count(*) from tramitacao";

    public List<Tramitacao> consultaTramitacao() {
        List<Tramitacao> listaTramitacao = new ArrayList<>();
        Tramitacao tramitacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTramitacao);
            rs = ps.executeQuery();
            while (rs.next()) {
                tramitacao = new Tramitacao();
                tramitacao.setId(rs.getInt("tram.id"));
                tramitacao.setDataTramitacao(rs.getTimestamp("tram.data_tramitacao"));
                tramitacao.setMesAno(rs.getString("tram.mesano"));
                tramitacao.setStatus(rs.getString("tram.statuss"));
                tramitacao.setParecer(rs.getString("tram.parecer"));
                tramitacao.setSetor(rs.getString("tram.setor"));
                tramitacao.setSetorOrigem(rs.getString("tram.setor_origem"));
                tramitacao.setSetorDestino(rs.getString("tram.setor_destino")); 
                tramitacao.setObservacao(rs.getString("tram.observacao"));
                tramitacao.setControle(rs.getString("tram.controle"));
                //usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setLogin(rs.getString("usu.login"));

                tramitacao.setUsuario(usuario);
  
 
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));

                tramitacao.setProcesso(processo);
                
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                tramitacao.setRequerente(requerente);
                
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                tramitacao.setAnexosProcesso(anexo);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                tramitacao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                tramitacao.setDenuncia(denuncia);
                
                
                listaTramitacao.add(tramitacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTramitacao;
    }

    public List<Tramitacao> consultaTramitacaoPro(String numero, String nome) {
        List<Tramitacao> listaTramitacao = new ArrayList<>();
        Tramitacao tramitacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTramitacaoProcessoRequerente);
            numero = "" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
            
            rs = ps.executeQuery();
            
            while (rs.next()) {
               tramitacao = new Tramitacao();
                tramitacao.setId(rs.getInt("tram.id"));
                tramitacao.setDataTramitacao(rs.getTimestamp("tram.data_tramitacao"));
                tramitacao.setMesAno(rs.getString("tram.mesano"));
                tramitacao.setStatus(rs.getString("tram.statuss"));
                tramitacao.setParecer(rs.getString("tram.parecer"));
                tramitacao.setSetor(rs.getString("tram.setor"));
                tramitacao.setSetorOrigem(rs.getString("tram.setor_origem"));
                tramitacao.setSetorDestino(rs.getString("tram.setor_destino")); 
                tramitacao.setObservacao(rs.getString("tram.observacao"));
                tramitacao.setControle(rs.getString("tram.controle"));
                //usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setLogin(rs.getString("usu.login"));

                tramitacao.setUsuario(usuario);
                
                //processo
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));

                tramitacao.setProcesso(processo);
                
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                tramitacao.setRequerente(requerente);
                
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                tramitacao.setAnexosProcesso(anexo);
                
                
                  Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                tramitacao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                tramitacao.setDenuncia(denuncia);
                
                listaTramitacao.add(tramitacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTramitacao;
    }
    
     public List<Tramitacao> consultaTramUltimoIdProce(String numero) {
        List<Tramitacao> listaTramitacao = new ArrayList<>();
        Tramitacao tramitacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTramUltimoIdProce);
            numero = "%" + numero + "%";
            ps.setString(1, numero);
            rs = ps.executeQuery();
            while (rs.next()) {
                tramitacao = new Tramitacao();
                tramitacao.setId(rs.getInt("tram.id"));
                tramitacao.setDataTramitacao(rs.getTimestamp("tram.data_tramitacao"));
                tramitacao.setMesAno(rs.getString("tram.mesano"));
                tramitacao.setStatus(rs.getString("tram.statuss"));
                tramitacao.setParecer(rs.getString("tram.parecer"));
                tramitacao.setSetor(rs.getString("tram.setor"));
                tramitacao.setSetorOrigem(rs.getString("tram.setor_origem"));
                tramitacao.setSetorDestino(rs.getString("tram.setor_destino")); 
                tramitacao.setObservacao(rs.getString("tram.observacao"));
                tramitacao.setControle(rs.getString("tram.controle"));
                //usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setLogin(rs.getString("usu.login"));

                tramitacao.setUsuario(usuario);
                //lote
                /*LoteTitulacao lote = new LoteTitulacao();
                lote.setId(rs.getInt("lot.id"));
                lote.setLote(rs.getString("lot.lote"));
                lote.setDescricao(rs.getString("lot.descricao"));

                tramitacao.setLoteTitulacao(lote);*/
                //processo
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));

                tramitacao.setProcesso(processo);

                listaTramitacao.add(tramitacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTramitacao;
    }
     
     public List<Tramitacao> consultaTramUltimoNumProce(String numero) {
        List<Tramitacao> listaTramitacao = new ArrayList<>();
        Tramitacao tramitacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTramUltimoNumProce);
            numero = "%" + numero + "%";
            ps.setString(1, numero);
            rs = ps.executeQuery();
            while (rs.next()) {
                tramitacao = new Tramitacao();
                tramitacao.setId(rs.getInt("tram.id"));
                tramitacao.setDataTramitacao(rs.getTimestamp("tram.data_tramitacao"));
                tramitacao.setMesAno(rs.getString("tram.mesano"));
                tramitacao.setStatus(rs.getString("tram.statuss"));
                tramitacao.setParecer(rs.getString("tram.parecer"));
                tramitacao.setSetor(rs.getString("tram.setor"));
                tramitacao.setSetorOrigem(rs.getString("tram.setor_origem"));
                tramitacao.setSetorDestino(rs.getString("tram.setor_destino")); 
                tramitacao.setObservacao(rs.getString("tram.observacao"));
                tramitacao.setControle(rs.getString("tram.controle"));
                //usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setLogin(rs.getString("usu.login"));

                tramitacao.setUsuario(usuario);

                //processo
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));

                tramitacao.setProcesso(processo);
                
                 
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                tramitacao.setRequerente(requerente);
                
                
                AnexosProcesso anexo = new AnexosProcesso();
                anexo.setDescricaoAnexo(rs.getString("anexos.descricao_anexo"));
                anexo.setNomeArquivo(rs.getString("anexos.nome_arquivo"));
                tramitacao.setAnexosProcesso(anexo);
                

                listaTramitacao.add(tramitacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTramitacao;
    }
     /*
     public List<Tramitacao> consultaTramitacaoAnexos() {
        List<Tramitacao> listaTramitacao = new ArrayList<>();
        Tramitacao tramitacao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaTramitacao);
            rs = ps.executeQuery();
            while (rs.next()) {
                tramitacao = new Tramitacao();
                tramitacao.setId(rs.getInt("tram.id"));
                tramitacao.setDataTramitacao(rs.getDate("tram.data_tramitacao"));
                tramitacao.setHora(rs.getString("tram.hora"));
                tramitacao.setMesAno(rs.getString("tram.mesano"));
                tramitacao.setStatus(rs.getString("tram.statuss"));
                tramitacao.setParecer(rs.getString("tram.parecer"));
                tramitacao.setSetor(rs.getString("tram.setor"));
                tramitacao.setSetorOrigem(rs.getString("tram.setor_origem"));
                tramitacao.setSetorDestino(rs.getString("tram.setor_destino")); 
                tramitacao.setObservacao(rs.getString("tram.observacao"));
                tramitacao.setControle(rs.getString("tram.controle"));
                //usuario
                Usuario usuario = new Usuario();
                usuario.setId(rs.getInt("usu.id"));
                usuario.setLogin(rs.getString("usu.login"));

                tramitacao.setUsuario(usuario);
  
 
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));

                tramitacao.setProcesso(processo);
                
                Requerente requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                
                tramitacao.setRequerente(requerente);
                
                ps = con.prepareStatement(consultaAnexosProcesso);
                ps.setInt(1, tramitacao.getId());
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
                tramitacao.setListAnexosProcesso(listAnexosProcesso);
                listaTramitacao.add(tramitacao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaTramitacao;
    }*/

    public boolean incluiTramitacao(Tramitacao tramitacao) {
        
            try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiTramitacao);
            //id_usuario, id_lotetitulacao, id_processo, data_tramitacao, mesano, statuss, parecer, setor, observacao, controle       

            ps.setInt(1, tramitacao.getUsuario().getId());
            ps.setInt(2, tramitacao.getProcesso().getId());
            ps.setTimestamp(3, tramitacao.getDataTramitacao());
            ps.setString(4, tramitacao.getMesAno());
            ps.setString(5, tramitacao.getStatus());
            ps.setString(6, tramitacao.getParecer());
            ps.setString(7, tramitacao.getSetor());
            ps.setString(8, tramitacao.getSetorOrigem());
            ps.setString(9, tramitacao.getSetorDestino());
            ps.setString(10, tramitacao.getObservacao());
            ps.setString(11, tramitacao.getControle());
            if(tramitacao.getEmissaoLicenca() == null){
                ps.setNull(12, 1);
            }else{
                ps.setInt(12, tramitacao.getEmissaoLicenca().getId());
            }
             
             if(tramitacao.getAutorizacaoEventos() == null){
                ps.setNull(13, 1);
            }else{
                ps.setInt(13, tramitacao.getAutorizacaoEventos().getId());
            }
            
             if(tramitacao.getAnexosProcesso() == null){
                ps.setNull(14, 1);
            }else{
                ps.setInt(14, tramitacao.getAnexosProcesso().getId());
            }
             
             if(tramitacao.getParecerAnalise() == null){
                ps.setNull(15, 1);
            }else{
                ps.setInt(15, tramitacao.getParecerAnalise().getId());
            }
             
              if(tramitacao.getParecerJuridico() == null){
                ps.setNull(16, 1);
            }else{
                ps.setInt(16, tramitacao.getParecerJuridico().getId());
            }
              
                if(tramitacao.getParecerFiscalizacao()== null){
                ps.setNull(17, 1);
            }else{
                ps.setInt(17, tramitacao.getParecerFiscalizacao().getId());
            }

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
      
        }  
      
        return false;
    }
    
   

    public boolean alteraTramitacao(Tramitacao tramitacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTramitacao);
            //update tramitacao set id_usuario = ?, id_lotetitulacao = ?, id_processo = ? data_tramitacao = ?, mesano = ?, statuss = ?, parecer = ?, setor = ?, observacao = ?, controle = ? where tramitacao.id = ?
            ps.setInt(1, tramitacao.getUsuario().getId());
            ps.setInt(2, tramitacao.getProcesso().getId());
            ps.setTimestamp(3, tramitacao.getDataTramitacao());
            ps.setString(4, tramitacao.getMesAno());
            ps.setString(5, tramitacao.getStatus());
            ps.setString(6, tramitacao.getParecer());
            ps.setString(7, tramitacao.getSetor());
            ps.setString(8, tramitacao.getSetorOrigem());
            ps.setString(9, tramitacao.getSetorDestino());
            ps.setString(10, tramitacao.getObservacao());
            ps.setString(11, tramitacao.getControle());

            ps.setInt(13, tramitacao.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraTramitacaoAnexoParecerAnalise(Tramitacao tramitacao, int idParecerAnalise) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTramAnexoParecerAnalise);
            //update tramitacao set id_usuario = ?, id_lotetitulacao = ?, id_processo = ? data_tramitacao = ?, mesano = ?, statuss = ?, parecer = ?, setor = ?, observacao = ?, controle = ? where tramitacao.id = ?
            ps.setInt(1, tramitacao.getAnexosProcesso().getId());
            ps.setInt(2, idParecerAnalise);
            //ps.setString(3, "PARECER TECNICO CONCLUIDO");

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraTramitacaoAnexoParecerJuridio(Tramitacao tramitacao, int idParecerJuridico) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTramAnexoParecerJuridico);
            //update tramitacao set id_usuario = ?, id_lotetitulacao = ?, id_processo = ? data_tramitacao = ?, mesano = ?, statuss = ?, parecer = ?, setor = ?, observacao = ?, controle = ? where tramitacao.id = ?
            ps.setInt(1, tramitacao.getAnexosProcesso().getId());
            ps.setInt(2, idParecerJuridico);
            //ps.setString(3, "PARECER TECNICO CONCLUIDO");

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraTramitacaoAnexoParecerFiscalizacao(Tramitacao tramitacao, int idParecerFiscalizacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraTramAnexoParecerJuridico);
            //update tramitacao set id_usuario = ?, id_lotetitulacao = ?, id_processo = ? data_tramitacao = ?, mesano = ?, statuss = ?, parecer = ?, setor = ?, observacao = ?, controle = ? where tramitacao.id = ?
            ps.setInt(1, tramitacao.getAnexosProcesso().getId());
            ps.setInt(2, idParecerFiscalizacao);
            //ps.setString(3, "PARECER TECNICO CONCLUIDO");

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiTramitacao(Tramitacao tramitacao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiTramitacao);

            ps.setInt(1, tramitacao.getId());

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
