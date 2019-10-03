/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.requerente;

import sip.acessobd.AcessoBD;
import sip.bairro.Bairro;
import sip.cidade.Cidade;
import sip.logradouro.Logradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author T2Ti
 */
public class RequerenteBD {

    private PreparedStatement ps;
    public Connection con;
    public ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaRequerente = "select req.*, reqrep.*, logra.*, bai.*, cid.* from requerente req left join requerente reqrep on req.id_requerente = reqrep.id join logradouro logra on req.id_logradouro = logra.id join bairro bai on req.id_bairro = bai.id join cidade cid on req.id_cidade = cid.id order by req.id desc";
    private String consultaReqNomeCpf = "select req.*, reqrep.*, logra.*, bai.*, cid.* from requerente req left join requerente reqrep on req.id_requerente = reqrep.id join logradouro logra on req.id_logradouro = logra.id join bairro bai on req.id_bairro = bai.id join cidade cid on req.id_cidade = cid.id where req.nome like ? or REPLACE(REPLACE(req.cpf, '.', ''), '-', '') like ?";
    private String incluiRequerente = "insert into requerente (nome, nacionalidade, est_civil, profissao, rg, orgao, cpf, id_logradouro, numero, id_bairro, id_cidade, tel_fixo, tel_cel, nubente, regime, representado, representado_nome, representado_tipo, tipo_pessoa, id_requerente, complemento, cep) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraRequerente = "update requerente set nome = ?, nacionalidade = ?, est_civil = ?, profissao = ?, rg = ?, orgao = ?, cpf = ?, id_logradouro = ?, numero = ?, id_bairro = ?, id_cidade = ?, tel_fixo = ?, tel_cel = ?, nubente = ?, regime = ?, representado = ?, representado_nome = ?, representado_tipo = ?, tipo_pessoa = ?, id_requerente = ?, complemento = ?, cep = ? where requerente.id = ?";
    private String excluiRequerente = "delete from requerente where requerente.id = ?";
    private String cont = "select count(*) from requerente";

    public List<Requerente> consultaRequerente() {
        List<Requerente> listaRequerente = new ArrayList<>();
        Requerente requerente;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaRequerente);
            rs = ps.executeQuery();
            while (rs.next()) {
                requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                requerente.setNacionalidade(rs.getString("req.nacionalidade"));
                requerente.setEstadoCivil(rs.getString("req.est_civil"));
                requerente.setProfissao(rs.getString("req.profissao"));
                requerente.setRg(rs.getString("req.rg"));
                requerente.setOrgao(rs.getString("req.orgao"));
                requerente.setCpf(rs.getString("req.cpf"));
                requerente.setNumero(rs.getString("req.numero"));
                requerente.setTelFixo(rs.getString("req.tel_fixo"));
                requerente.setTelCel(rs.getString("req.tel_cel"));
                requerente.setNubente(rs.getString("req.nubente"));
                requerente.setRegime(rs.getString("req.regime"));
                requerente.setRepresentado(rs.getString("req.representado"));
                requerente.setRepresentadoNome(rs.getString("req.representado_nome"));
                requerente.setRepresentadoTipo(rs.getString("req.representado_tipo"));
                requerente.setTipoPessoa(rs.getString("req.tipo_pessoa"));
                requerente.setComplemento(rs.getString("req.complemento"));
                requerente.setCep(rs.getString("req.cep"));
                
               
                //monitor.setTelefone(rs.getString("telefone"));

                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));

                requerente.setBairro(bairro);


                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));

                requerente.setLogradouro(logradouro);

                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cid.id"));
                cidade.setNome(rs.getString("cid.nome"));

                requerente.setCidade(cidade);
                
                Requerente reqRepresentado = new Requerente();
                reqRepresentado.setId(rs.getInt("reqrep.id"));
                reqRepresentado.setNome(rs.getString("reqrep.nome"));
                requerente.setRequerente(reqRepresentado);
                
               

               

                listaRequerente.add(requerente);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return listaRequerente;
    }

    public List<Requerente> consultaRequerente(String nome, String cpf) {
        List<Requerente> listaRequerente = new ArrayList<>();
        Requerente requerente;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaReqNomeCpf);
            nome = "%" + nome + "%";
            cpf = "%"+ cpf + "%";
            ps.setString(1, nome);
            ps.setString(2, cpf);
            rs = ps.executeQuery();
            while (rs.next()) {
                requerente = new Requerente();
                requerente.setId(rs.getInt("req.id"));
                requerente.setNome(rs.getString("req.nome"));
                requerente.setNacionalidade(rs.getString("req.nacionalidade"));
                requerente.setEstadoCivil(rs.getString("req.est_civil"));
                requerente.setProfissao(rs.getString("req.profissao"));
                requerente.setRg(rs.getString("req.rg"));
                requerente.setOrgao(rs.getString("req.orgao"));
                requerente.setCpf(rs.getString("req.cpf"));
                requerente.setNumero(rs.getString("req.numero"));
                requerente.setTelFixo(rs.getString("req.tel_fixo"));
                requerente.setTelCel(rs.getString("req.tel_cel"));
                requerente.setNubente(rs.getString("req.nubente"));
                requerente.setRegime(rs.getString("req.regime"));
                requerente.setRepresentado(rs.getString("req.representado"));
                requerente.setRepresentadoNome(rs.getString("req.representado_nome"));
                requerente.setRepresentadoTipo(rs.getString("req.representado_tipo"));
                requerente.setTipoPessoa(rs.getString("req.tipo_pessoa"));
                requerente.setComplemento(rs.getString("req.complemento"));
                requerente.setCep(rs.getString("req.cep"));


                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));

                requerente.setBairro(bairro);

                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));

                requerente.setLogradouro(logradouro);

                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cid.id"));
                cidade.setNome(rs.getString("cid.nome"));

                requerente.setCidade(cidade);
                
                Requerente reqRepresentado = new Requerente();
                reqRepresentado.setId(rs.getInt("reqrep.id"));
                reqRepresentado.setNome(rs.getString("reqrep.nome"));
                requerente.setRequerente(reqRepresentado);

                listaRequerente.add(requerente);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listaRequerente;
    }

    public boolean incluiRequerente(Requerente requerente) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiRequerente);
//nome, id_nacionalidade, est_civil, profissao, rg, orgao, cpf, id_ruaav, numero, id_bairro, id_cidade

            ps.setString(1, requerente.getNome());
            ps.setString(2, requerente.getNacionalidade());
            ps.setString(3, requerente.getEstadoCivil());
            ps.setString(4, requerente.getProfissao());
            ps.setString(5, requerente.getRg());
            ps.setString(6, requerente.getOrgao());
            ps.setString(7, requerente.getCpf());
            ps.setInt(8, requerente.getLogradouro().getId());
            ps.setString(9, requerente.getNumero());
            ps.setInt(10, requerente.getBairro().getId());
            ps.setInt(11, requerente.getCidade().getId());
            ps.setString(12, requerente.getTelFixo());
            ps.setString(13, requerente.getTelCel());
            ps.setString(14, requerente.getNubente());
            ps.setString(15, requerente.getRegime());
            ps.setString(16, requerente.getRepresentado());
            ps.setString(17, requerente.getRepresentadoNome());
            ps.setString(18, requerente.getRepresentadoTipo());
            ps.setString(19, requerente.getTipoPessoa());
            if(requerente.getRequerente() == null){
                ps.setNull(20, 1);
            }else{
                ps.setInt(20, requerente.getRequerente().getId());
            }
            ps.setString(21, requerente.getComplemento());  
            ps.setString(22, requerente.getCep());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    

    public boolean alteraRequerente(Requerente requerente) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraRequerente);

/*set nome = ?, nacionalidade = ?, est_civil = ?, profissao = ?, rg = ?, orgao = ?, cpf = ?, id_logradouro = ?, numero = ?,
id_bairro = ?, id_cidade = ?, tel_fixo = ?, tel_cel = ?, nubente = ?, regime = ?, representado = ?, representado_nome = ?,
representado_tipo = ?, tipo_pessoa = ?, id_requerente = ?  where requerente.id = ?*/
            ps.setString(1, requerente.getNome());
            ps.setString(2, requerente.getNacionalidade());
            ps.setString(3, requerente.getEstadoCivil());
            ps.setString(4, requerente.getProfissao());
            ps.setString(5, requerente.getRg());
            ps.setString(6, requerente.getOrgao());
            ps.setString(7, requerente.getCpf());
            ps.setInt(8, requerente.getLogradouro().getId());
            ps.setString(9, requerente.getNumero());
            ps.setInt(10, requerente.getBairro().getId());
            ps.setInt(11, requerente.getCidade().getId());
            ps.setString(12, requerente.getTelFixo());
            ps.setString(13, requerente.getTelCel());
            ps.setString(14, requerente.getNubente());
            ps.setString(15, requerente.getRegime());
            ps.setString(16, requerente.getRepresentado());
            ps.setString(17, requerente.getRepresentadoNome());
            ps.setString(18, requerente.getRepresentadoTipo());
            ps.setString(19, requerente.getTipoPessoa());
            if(requerente.getRequerente() == null){
                ps.setNull(20, 1);
            }else{
                ps.setInt(20, requerente.getRequerente().getId());
            }
            ps.setString(21, requerente.getComplemento());  
            ps.setString(22, requerente.getCep());
                

            ps.setInt(23, requerente.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiRequerente(Requerente requerente) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluiRequerente);

            ps.setInt(1, requerente.getId());

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
