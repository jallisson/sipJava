/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.distribuicao;

import sip.acessobd.AcessoBD;
import sip.usuario.Usuario;
import sip.requerente.Requerente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import sip.analista.Analista;
import sip.denuncia.Denuncia;
import sip.pessoa.Pessoa;
import sip.processo.Processo;

/**
 *
 * @author T2Ti
 */
public class DistribuicaoBD {
   
    private PreparedStatement ps;
    private PreparedStatement ps1;
    public Connection con;
    public ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaDistribuicao = "select dist.*, usu.*, pro.id,  pro.num_processo, pro.tipo_licenca, pro.arquivado, ana.id, ana.nome, ana.qtde_entrada, ana.qtde_entrada - ana.qtde_saida AS saldo, req.nome, pdenunciado.nome, denuncia.* from distribuicao dist join usuario usu on dist.id_usuario = usu.id join processo pro on dist.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id left join analista ana on dist.id_analista = ana.id left join requerente req on pro.id_requerente = req.id order by dist.id desc";
    private String consultaDistTramiAnalise = "select dist.*, usu.*, pro.id,  pro.num_processo, pro.tipo_licenca, pro.arquivado, ana.id, ana.nome, ana.qtde_entrada, ana.qtde_entrada - ana.qtde_saida AS saldo, req.nome, pdenunciado.nome, denuncia.* from distribuicao dist join usuario usu on dist.id_usuario = usu.id join processo pro on dist.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id join analista ana on dist.id_analista = ana.id left join requerente req on pro.id_requerente = req.id where dist.tramitou_analise = 'SIM' order by dist.id desc";  
    private String consultaDistTramiJuridico = "select dist.*, usu.*, pro.id,  pro.num_processo, pro.tipo_licenca, pro.arquivado, ana.id, ana.nome, ana.qtde_entrada, ana.qtde_entrada - ana.qtde_saida AS saldo, req.nome, pdenunciado.nome, denuncia.* from distribuicao dist join usuario usu on dist.id_usuario = usu.id join processo pro on dist.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id join analista ana on dist.id_analista = ana.id left join requerente req on pro.id_requerente = req.id where dist.tramitou_juridico = 'SIM' order by dist.id desc";  
    private String consultaDistTramiFiscalizacao = "select dist.*, usu.*, pro.id,  pro.num_processo, pro.tipo_licenca, pro.arquivado, ana.id, ana.id_usuario, ana.nome, ana.qtde_entrada, ana.qtde_entrada - ana.qtde_saida AS saldo, req.nome, pdenunciado.nome, denuncia.* from distribuicao dist join usuario usu on dist.id_usuario = usu.id join processo pro on dist.id_processo = pro.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id join analista ana on dist.id_analista = ana.id left join requerente req on pro.id_requerente = req.id where dist.tramitou_fiscalizacao = 'SIM' and ana.id_usuario = ? order by dist.id desc";  
    private String consultaDistNumNome = "SELECT dist.*, usu.*, pro.id,  pro.num_processo, pro.tipo_licenca, pro.arquivado, ana.id, ana.nome, ana.qtde_entrada, req.nome, pdenunciado.nome, denuncia.* FROM distribuicao dist LEFT JOIN usuario usu ON dist.id_usuario = usu.id LEFT JOIN processo pro ON dist.id_processo = pro.id LEFT JOIN analista ana ON dist.id_analista = ana.id LEFT JOIN requerente req ON pro.id_requerente = req.id LEFT JOIN denuncia denuncia ON pro.id_denuncia = denuncia.id LEFT JOIN pessoa pdenunciado ON denuncia.id_denunciado = pdenunciado.id WHERE pro.num_processo LIKE ? OR req.nome LIKE ? OR pdenunciado.nome LIKE ?";
    private String incluiDistribuicao = "insert into distribuicao (id_usuario, id_analista, id_processo, data_dist, mesano, controle, tramitou_analise, tramitou_juridico, tramitou_fiscalizacao) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraDistribuicao = "update distribuicao set data_dist = ?, mesano = ?, analisado = ? where distribuicao.id = ?";
    private String alteraDistTramiAnalise ="update distribuicao set tramitou_analise = ? where distribuicao.id = ?";
    private String alteraDistTramiJuridico ="update distribuicao set tramitou_juridico = ? where distribuicao.id = ?";
    private String alteraDistTramiFiscalizacao ="update distribuicao set tramitou_fiscalizacao = ? where distribuicao.id = ?";
    private String alteraDistRecebidoFiscalizacao ="update distribuicao set status = ? where distribuicao.id = ?";
    private String excluiDistribuicao = "delete from distribuicao where distribuicao.id = ?";
    private String cont = "select count(*) from cobrador";
    
    
    public List<Distribuicao> consultaDistribuicao(String consultaDeOnde, Integer id){
        List<Distribuicao> listaDistribuicao = new ArrayList<>();
        Distribuicao distribuicao;
        try {
            con = acessoBD.conectar();
            switch (consultaDeOnde) {
                case "DistribuicaoFrame":
                    ps = con.prepareStatement(consultaDistribuicao);
                    break;
                case "AnaliseFrame":
                    ps = con.prepareStatement(consultaDistTramiAnalise);
                    break;
                case "JuridicoFrame":
                    ps = con.prepareStatement(consultaDistTramiJuridico);
                    break;
               case "FiscalizacaoFrame":
                    ps = con.prepareStatement(consultaDistTramiFiscalizacao);
                    ps.setInt(1, id);
                    break;
            }
            
            rs = ps.executeQuery();
            while (rs.next()) {
                distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                distribuicao.setDataDist(rs.getDate("dist.data_dist"));
                distribuicao.setMesAno(rs.getString("dist.mesano"));
                //distribuicao.setAnalisado(rs.getString("dist.analisado"));
                distribuicao.setTramitouAnalise(rs.getString("dist.tramitou_analise"));
                distribuicao.setTramitouJuridico(rs.getString("dist.tramitou_juridico"));
                distribuicao.setTramitouFiscalizacao(rs.getString("dist.tramitou_fiscalizacao"));
                distribuicao.setStatus(rs.getString("dist.status"));
                //processo.setControleDigitador(rs.getString("pro.controle_dig"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                distribuicao.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                distribuicao.setProcesso(processo);
         
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                usuario.setId(rs.getInt("usu.id"));
                usuario.setSetor(rs.getString("usu.setor"));
                distribuicao.setUsuario(usuario);
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));
                analista.setQtdeEntrada(rs.getInt("ana.qtde_entrada"));
        
                //analista.setSaldo(rs.getInt("ana.saldo"));
                
                distribuicao.setAnalista(analista);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                distribuicao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
                denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                denuncia.setStatusApp(rs.getString("denuncia.status_app"));
                
                distribuicao.setDenuncia(denuncia);
                    

                listaDistribuicao.add(distribuicao);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDistribuicao;
    }
    

  
      public List<Distribuicao> consultaProcessoNum(String numero, String nome) {
        List<Distribuicao> listaDistribuicao = new ArrayList<>();
        Distribuicao distribuicao;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaDistNumNome);
            numero = "%" + numero + "%";
            nome = "%" + nome + "%";
            ps.setString(1, numero);
            ps.setString(2, nome);
            ps.setString(3, nome);
                     
            rs = ps.executeQuery();
             while (rs.next()) {
                distribuicao = new Distribuicao();
                distribuicao.setId(rs.getInt("dist.id"));
                distribuicao.setDataDist(rs.getDate("dist.data_dist"));
                distribuicao.setMesAno(rs.getString("dist.mesano"));
                //distribuicao.setAnalisado(rs.getString("dist.analisado"));
                distribuicao.setTramitouAnalise(rs.getString("dist.tramitou_analise"));
                 distribuicao.setTramitouJuridico(rs.getString("dist.tramitou_juridico"));
                  distribuicao.setTramitouFiscalizacao(rs.getString("dist.tramitou_fiscalizacao"));
                 
                
                //processo.setControleDigitador(rs.getString("pro.controle_dig"));
                
                Requerente requerente = new Requerente();
                requerente.setNome(rs.getString("req.nome"));
                
                distribuicao.setRequerente(requerente);
                
                Processo processo = new Processo();
                processo.setId(rs.getInt("pro.id"));
                processo.setNumProcesso(rs.getString("pro.num_processo"));
                processo.setTipoLicenca(rs.getString("pro.tipo_licenca"));
                processo.setArquivado(rs.getString("pro.arquivado"));
                distribuicao.setProcesso(processo);
                
                
                Analista analista = new Analista();
                analista.setId(rs.getInt("ana.id"));
                analista.setNome(rs.getString("ana.nome"));
                analista.setQtdeEntrada(rs.getInt("ana.qtde_entrada"));
                
                distribuicao.setAnalista(analista);
                
                Usuario usuario = new Usuario();
                usuario.setNome(rs.getString("usu.nome"));
                 usuario.setSetor(rs.getString("usu.setor"));
                
                distribuicao.setUsuario(usuario);
                
                Pessoa pessoa = new Pessoa();
                pessoa.setNome(rs.getString("pdenunciado.nome"));
                distribuicao.setPessoa(pessoa);
         
                Denuncia denuncia = new Denuncia();
               denuncia.setId(rs.getInt("denuncia.id"));
                denuncia.setToken(rs.getString("denuncia.token_gcm"));
                denuncia.setOrigem(rs.getString("denuncia.origem"));
                denuncia.setStatusApp(rs.getString("denuncia.status_app"));
                
                distribuicao.setDenuncia(denuncia);
         

                listaDistribuicao.add(distribuicao);
                
            }
                    
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaDistribuicao;
    }
        
   public boolean incluiDistribuicao(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiDistribuicao);
            //num_processo, controle_dig, id_requerente, id_digitador, valor, data_processo, mesano  
            
            
            ps.setInt(1, distribuicao.getUsuario().getId());
            ps.setInt(2, distribuicao.getAnalista().getId());
            ps.setInt(3, distribuicao.getProcesso().getId());
            ps.setDate(4, distribuicao.getDataDist());
            ps.setString(5, distribuicao.getMesAno());
            //ps.setString(6, distribuicao.getAnalisado());
            ps.setString(6, distribuicao.getControle());
            ps.setString(7, distribuicao.getTramitouAnalise());
            ps.setString(8, distribuicao.getTramitouJuridico());
             ps.setString(9, distribuicao.getTramitouFiscalizacao());
            
            /*if (distribuicao.getTramitouAnalise() == null) {
                ps.setNull(8, 1);
            } else {
                ps.setString(8, distribuicao.getTramitouAnalise());
            }*/
         
         
            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

   
    
    public boolean alteraDistribuicao(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDistribuicao);
 
            ps.setDate(1, distribuicao.getDataDist());
            //ps.setString(2, distribuicao.getAnalisado());
            ps.setString(2, distribuicao.getMesAno());
            ps.setString(3, distribuicao.getControle());

            ps.setInt(4, distribuicao.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraDistTramiAnalise(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDistTramiAnalise);
 
            ps.setString(1, distribuicao.getTramitouAnalise());

            ps.setInt(2, distribuicao.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraDistTramiJuridico(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDistTramiJuridico);
 
            ps.setString(1, distribuicao.getTramitouJuridico());

            ps.setInt(2, distribuicao.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public boolean alteraDistTramiFiscalizacao(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDistTramiFiscalizacao);
 
            ps.setString(1, distribuicao.getTramitouFiscalizacao());

            ps.setInt(2, distribuicao.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
     public boolean alteraDistRecebidoFiscalizacao(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraDistRecebidoFiscalizacao);
 
            ps.setString(1, distribuicao.getStatus());

            ps.setInt(2, distribuicao.getId());
            
            ps.executeUpdate();
            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

       
    public boolean excluiProcesso(Distribuicao distribuicao) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiDistribuicao);

            ps.setInt(1, distribuicao.getId());

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