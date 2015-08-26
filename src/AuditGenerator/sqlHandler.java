/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AuditGenerator;

import java.sql.Array;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

/**
 *
 * @author dcain
 */
public class sqlHandler {
    static Connection conn = null;
    static Statement stmt = null;
    
    static void setConnection(String db_url, String db_user, String db_pw) throws SQLException, ClassNotFoundException{
//        Properties props = new Properties(); 
//        props.put("user", db_user);         
//        props.put("password", db_pw);
//        props.put("useUnicode", "true");
//        props.put("useServerPrepStmts", "false"); // use client-side prepared statement
//        props.put("characterEncoding", "UTF-8"); // ensure charset is utf8 here
        conn = DriverManager.getConnection(db_url, db_user, db_pw);   
        stmt = conn.createStatement(); 
    }
    
    static void closeConnection() throws SQLException{
        if(stmt != null){
            stmt.close();
        }
        if (conn != null){
        conn.close();
        }
    }
    
    
    
    
    public static int getSQLResponse(String sql){
        ResultSet rs = null;
        int auditId = 0;
        try{
            //System.out.println("inside getSQLResponse");
            Integer newAuditId = stmt.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
            rs = stmt.getGeneratedKeys();
            
            //System.out.println("handler is closed " + rs.isClosed());
            if (rs.next()){
                auditId = rs.getInt(1);
                
            }
            rs.close();
            
            return auditId;
        
        }  catch(Exception e){
            System.out.println("inside sql handler " + e);
        } 
        return 0;
    }
    
    public static String[] getResult(String sql){
        ResultSet rs = null;
        String[] siteIds;
        try{
            //System.out.println("inside getSQLResponse");
            rs = stmt.executeQuery(sql);
            //Array resultArray = rs.getArray(0);
            ArrayList<String> result = new ArrayList<String>();
            while (!rs.isLast()){
                //String thisString = rs.getNString("id");
                //int id = rs.getInt("id");
                //String sId = Integer.toString(id);
                rs.next();
                result.add(Integer.toString(rs.getInt("id")));                
                
            }
             

            rs.close();
            siteIds = result.toArray(new String[result.size()]);
            return siteIds;
        
        }  catch(Exception e){
            System.out.println("sql handler exception" + e);
        } 
       return null;
    }
    public static String[] getResult(String column, String sql){
        ResultSet rs = null;
        String[] siteIds;
        try{
            //System.out.println("inside getSQLResponse");
            rs = stmt.executeQuery(sql);
            //Array resultArray = rs.getArray(0);
            ArrayList<String> result = new ArrayList<String>();
            while (!rs.isAfterLast()){
                //System.out.println("rs.isLast " + rs.isLast());
                
                //String thisString = rs.getNString("id");
                //int id = rs.getInt("id");
                //String sId = Integer.toString(id);
                if(rs.isBeforeFirst()){
                    rs.next();
                }
                    //System.out.println("test");
                    //System.out.println(rs.getInt(column));
                    result.add(Integer.toString(rs.getInt(column)));                
                rs.next();
            }
             

            rs.close();
            siteIds = result.toArray(new String[result.size()]);
            System.out.println("inside sql h siteIds size" + siteIds.length );
            return siteIds;
        
        }  catch(Exception e){
            System.out.println("inside sql handler exception" + e);
        } 
       return null;
    }
    
    
    
    public static void executeStatement(String sql){
        
        try{
            //System.out.println("Inside execStatement " + sql);
            int getResponse = stmt.executeUpdate(sql);
            System.out.println("execute " + getResponse);  
        
        }  catch(Exception e){
           // System.out.println(e);
        } 
        
     }//end try
       
    public static void truncateData() throws SQLException{
        CallableStatement cs = null;        
        cs = conn.prepareCall("{call WipeAuditData()}");
        cs.execute();
    }
    
}
