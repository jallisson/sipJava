/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sip.pessoa;

import sip.acessobd.AcessoBD;
import sip.bairro.Bairro;
import sip.cidade.Cidade;
import sip.logradouro.Logradouro;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author T2Ti
 */
public class PessoaBD {

    private PreparedStatement ps;
    public Connection con;
    public ResultSet rs;
    private AcessoBD acessoBD = new AcessoBD();
    private String consultaPessoa = "select pess.*, logra.*, bai.*, cid.* from pessoa pess left join logradouro logra on pess.id_logradouro = logra.id left join bairro bai on pess.id_bairro = bai.id left join cidade cid on pess.id_cidade = cid.id order by pess.id desc";
    private String consultaPessoaNome = "select pess.*, logra.*, bai.*, cid.* from pessoa pess left join logradouro logra on pess.id_logradouro = logra.id left join bairro bai on pess.id_bairro = bai.id left join cidade cid on pess.id_cidade = cid.id where pess.nome like ?";
    private String incluiPessoa = "insert into pessoa (nome, id_logradouro, numero, tel_fixo, tel_cel, id_bairro, complemento, id_cidade) values(?, ?, ?, ?, ?, ?, ?, ?)";
    private String alteraPessoa = "update pessoa set nome = ?, id_logradouro = ?, numero = ?,  tel_fixo = ?, tel_cel = ?, id_bairro = ?, complemento = ? id_cidade = ? where pessuerente.id = ?";
    private String excluipessuerente = "delete from pessoa where pessoa.id = ?";
    private String cont = "select count(*) from pessuerente";

    public List<Pessoa> consultaPessoa() {
        List<Pessoa> listPessoa = new ArrayList<>();
        Pessoa pessoa;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaPessoa);
            rs = ps.executeQuery();
            while (rs.next()) {
                pessoa = new Pessoa();
                pessoa.setId(rs.getInt("pess.id"));
                pessoa.setNome(rs.getString("pess.nome"));
                pessoa.setNumero(rs.getString("pess.numero"));
                pessoa.setTelFixo(rs.getString("pess.tel_fixo"));
                pessoa.setTelCel(rs.getString("pess.tel_cel"));
                pessoa.setComplemento(rs.getString("pess.complemento"));
                
               
                //monitor.setTelefone(rs.getString("telefone"));

                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));

                pessoa.setBairro(bairro);


                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));

                pessoa.setLogradouro(logradouro);

                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cid.id"));
                cidade.setNome(rs.getString("cid.nome"));

                pessoa.setCidade(cidade);          
               

                listPessoa.add(pessoa);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();

        }
        return listPessoa;
    }

    public List<Pessoa> consultaPessoaNome(String nome) {
        List<Pessoa> listPessoa = new ArrayList<>();
        Pessoa pessoa;
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(consultaPessoaNome);
            nome = "%" + nome + "%";
            ps.setString(1, nome);
            rs = ps.executeQuery();
            while (rs.next()) {
                pessoa = new Pessoa();
                pessoa.setId(rs.getInt("pess.id"));
                pessoa.setNome(rs.getString("pess.nome"));
                pessoa.setNumero(rs.getString("pess.numero"));
                pessoa.setTelFixo(rs.getString("pess.tel_fixo"));
                pessoa.setTelCel(rs.getString("pess.tel_cel"));
                pessoa.setComplemento(rs.getString("pess.complemento"));
                
               
                //monitor.setTelefone(rs.getString("telefone"));

                Bairro bairro = new Bairro();
                bairro.setId(rs.getInt("bai.id"));
                bairro.setNome(rs.getString("bai.nome"));

                pessoa.setBairro(bairro);


                Logradouro logradouro = new Logradouro();
                logradouro.setId(rs.getInt("logra.id"));
                logradouro.setNome(rs.getString("logra.nome"));

                pessoa.setLogradouro(logradouro);

                Cidade cidade = new Cidade();
                cidade.setId(rs.getInt("cid.id"));
                cidade.setNome(rs.getString("cid.nome"));

                pessoa.setCidade(cidade);          
               

                listPessoa.add(pessoa);
            }
            acessoBD.desconectar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listPessoa;
    }

    public boolean incluiPessoa(Pessoa pessoa) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(incluiPessoa);
            ps.setString(1, pessoa.getNome());
            if(pessoa.getLogradouro() == null){
                ps.setNull(2, 1);
            }else{
                ps.setInt(2, pessoa.getLogradouro().getId());
            }
            
            if(pessoa.getNumero() == null){
                ps.setNull(3, 1);
            }else{
                ps.setString(3, pessoa.getNumero());
            }
            
            if(pessoa.getTelFixo() == null){
                ps.setNull(4, 1);
            }else{
                ps.setString(4, pessoa.getTelFixo());
            }
           
            if(pessoa.getTelCel() == null){
                ps.setNull(5, 1);
            }else{
                ps.setString(5, pessoa.getTelCel());
            }
           
            if(pessoa.getBairro() == null){
                ps.setNull(6, 1);
            }else{
                ps.setInt(6, pessoa.getBairro().getId());
            }
            
            if(pessoa.getComplemento() == null){
                ps.setNull(7, 1);
            }else{
                ps.setString(7, pessoa.getComplemento());
            }
            
            if(pessoa.getCidade() == null){
                ps.setNull(8, 1);
            }else{
                ps.setInt(8, pessoa.getCidade().getId());
            }        

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    

    public boolean alteraPessoa(Pessoa pessoa) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(alteraPessoa);

            ps.setString(1, pessoa.getNome());
            if(pessoa.getLogradouro() == null){
                ps.setNull(2, 1);
            }else{
                ps.setInt(2, pessoa.getLogradouro().getId());
            }
            
            if(pessoa.getNumero() == null){
                ps.setNull(3, 1);
            }else{
                ps.setString(3, pessoa.getNumero());
            }
            
            if(pessoa.getTelFixo() == null){
                ps.setNull(4, 1);
            }else{
                ps.setString(4, pessoa.getTelFixo());
            }
           
            if(pessoa.getTelCel() == null){
                ps.setNull(5, 1);
            }else{
                ps.setString(5, pessoa.getTelCel());
            }
           
            if(pessoa.getBairro() == null){
                ps.setNull(6, 1);
            }else{
                ps.setInt(6, pessoa.getBairro().getId());
            }
            
            if(pessoa.getComplemento() == null){
                ps.setNull(7, 1);
            }else{
                ps.setString(7, pessoa.getComplemento());
            }
            
            if(pessoa.getCidade() == null){
                ps.setNull(8, 1);
            }else{
                ps.setInt(8, pessoa.getCidade().getId());
            }        
                

            ps.setInt(9, pessoa.getId());

            ps.executeUpdate();

            acessoBD.desconectar();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean excluiPessoa(Pessoa pessoa) {
        try {
            con = acessoBD.conectar();
            ps = con.prepareStatement(excluipessuerente);

            ps.setInt(1, pessoa.getId());

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
