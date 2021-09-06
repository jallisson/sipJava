/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sip.acessobd;


import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author T2Ti
 */
public class AcessoBD {

    Connection con;
    Connection con1;
    public Statement statement;
    public ResultSet resultset;
     
    
    
//testegit
    public Connection conectar(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
           //con = DriverManager.getConnection("jdbc:mysql://localhost/sip?user=root&password=meioadmtec");//endereço aonde esta localizado o banco de dados   
          con = DriverManager.getConnection("jdbc:mysql://177.185.136.26/sip?user=root&password=meioadmtec");//endereço aonde esta localizado o banco de dados   
          //con = DriverManager.getConnection("jdbc:mysql://177.185.141.212/sip?user=root&password=root");//endereço aonde esta localizado o banco de dados   
           //con = DriverManager.getConnection("jdbc:mysql://contx.ddns.net/sip?user=root&password=root");//endereço aonde esta localizado o banco de dados   
          
         //con = DriverManager.getConnection("jdbc:mysql://localhost/sip?user=root&password=root");//endereço aonde esta localizado o banco de dados  
           //con = DriverManager.getConnection("jdbc:mysql://sipjj.com/sipjj426_sip?user=sipjj426_root&password=rooot");//endereço aonde esta localizado o banco de dados  
            
            //con = DriverManager.getConnection("jdbc:mysql://192.168.2.190/gov?user=root&password=root");//endereço aonde esta localizado o banco de dados      
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(AcessoBD.class.getName()).log(Level.SEVERE, null, ex);
             JOptionPane.showMessageDialog(null,ex);
        }catch(Exception ex){
        JOptionPane.showMessageDialog(null,ex);
    }
        
        return con;
        
    }
    
   public void desconectar(){
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(AcessoBD.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
      public void executeSQL(String sql)
       {
            try 
            {
                statement = con.createStatement(
                        ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
                resultset = statement.executeQuery(sql);
            }
            catch(SQLException sqlex) 
            {
               JOptionPane.showMessageDialog(null,"Não foi possível "+
                       "executar o comando sql,"+sqlex+", o sql passado foi "+sql);
            }
       }
}
