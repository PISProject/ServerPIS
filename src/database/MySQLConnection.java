/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package database;
import java.sql.*;
import server.MFServer;
        
/**
 *
 * @author kirtash
 */
public class MySQLConnection {
    public static String LOCALHOST = "jdbc:mysql://localhost/";
    public static String DB_NAME = "machango_fightdb";
    public static String USER = "root";
    public static String PASSWORD = "root";
    
    private static Connection db;
    private String query;
    private PreparedStatement preparedStmt;
    private ResultSet resultSet;
    private Statement statement;
    
    public MySQLConnection() throws SQLException, ClassNotFoundException{
        // Cargamos los drivers de MySQL, por si java no lo ha hecho.
        Class.forName("com.mysql.jdbc.Driver");

        //Establecemos la conexion con MySQL
        db = DriverManager.getConnection(LOCALHOST+DB_NAME, USER, PASSWORD);
        query = new String();
    }
    
    // Metodo de inicializacion de la base de datos
    public static MySQLConnection startDatabaseConnection(){
        try {
            return new MySQLConnection(); // Si hay algun
        } catch (SQLException ex) {
            return null;
        } catch (ClassNotFoundException ex){
            return null;
        }
    }
    
    
    //--------------------ADDERS
    
    public boolean addPlayer(String username, String password, String email){
        try{
                query = "insert into players (username, password, email) values ('"+username+"','"+password+"','"+email+"')";
               
                executeUpdate(query);
                
            return true;
        }catch(SQLException ex){
            ex.printStackTrace();
            // No deberia de pasar.
            return false;
        }
    }
    
    public boolean addNewPassword(String username, String newPassword){
        
        try{
                query = "update players set password = '"+newPassword+"' where username = '"+username+"'";

                
                executeUpdate(query);
               /* resultSet.absolute(1);

                resultSet.updateString("password", newPassword);
                resultSet.updateRow();*/
            return true;
        }catch(SQLException ex){
            return false;
        }
    }
        public boolean addNewPassword(int id, String newPassword){
        
        try{
                query = "update players set password = '"+newPassword+"' where id = "+id;

                
                executeUpdate(query);
               /* resultSet.absolute(1);

                resultSet.updateString("password", newPassword);
                resultSet.updateRow();*/
                return true;
        }catch(SQLException ex){
            return false;
        }
    }
    
    //---------------------GETTERS *Retornan null si no existe*
    public String getPassword(int id){
        try{
                query = "select password from players where id = "+id;

                resultSet = executeQuery(query);
                resultSet.first(); //situamos el cursor en el primer resultado
                return resultSet.getString("password"); //devolvemos la password
        }catch(SQLException ex){
            // No deberia de pasar.
            return null;
        }
    }
    
    public String getPassword(String username){
        try{
                query = "select password from players where username = '"+username+"'";
                resultSet = executeQuery(query);

                resultSet.first(); //situamos el cursor en el primer resultado
            return resultSet.getString("password"); //devolvemos la password
            
        }catch(SQLException ex){
            // No deberia de pasar.
            return null;
        }
    }
    public String getUserName(int id){
        try{
                query = "select username from players where id = "+id;
                resultSet = executeQuery(query);

                resultSet.first(); //situamos el cursor en el primer resultado
            return resultSet.getString("username"); //devolvemos la password
            
        }catch(SQLException ex){
            // No deberia de pasar.
            return null;
        }
    }
    public int getUserId(String username){
        try{
                query = "select id from players where username = '"+username+"'";
                resultSet = executeQuery(query);
                resultSet.first(); //situamos el cursor en el primer resultado
            return resultSet.getInt("id"); //devolvemos la password
            
        }catch(SQLException ex){
            ex.printStackTrace();
            return -1;
        }
    }
    
    //---------------------IS *Retornan un boleano*
    public boolean isUsernameInUse(String username){
        try{
                query = "select username from players where username = '"+username+"'";
                resultSet = executeQuery(query);
            return resultSet.first();

            
        }catch(SQLException ex){
            // No deberia de pasar.
            return false;
        }
    }
    
    private ResultSet executeQuery(String query) throws SQLException{
        synchronized(db){
            preparedStmt = db.prepareStatement(query);
            return preparedStmt.executeQuery();
        }
    }
    
    private void executeUpdate(String query) throws SQLException{
        synchronized(db){
            preparedStmt = db.prepareStatement(query);
            preparedStmt.executeUpdate();
        }
    }
}
