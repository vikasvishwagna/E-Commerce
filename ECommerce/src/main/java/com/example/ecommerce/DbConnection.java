package com.example.ecomm;
import java.sql.*;
public class DbConnection {
    private final String dbUrl = "jdbc:mysql://localhost:3306/ecommerce";
    private final String username = "root";
    private final String password = "Saivikas@29";

   private Statement getStatement(){
        try{
            Connection connection = DriverManager.getConnection(dbUrl, username, password);
            return connection.createStatement();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


    public ResultSet getQueryTable(String query){
        try {
            Statement statement = getStatement();
            return statement.executeQuery(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public int updateDataBase(String query){
       try {
            Statement statement = getStatement();
            return statement.executeUpdate(query);
        }catch (Exception e){
            e.printStackTrace();
        }
        return 0;
   }

    public static void main(String[] args) {
        DbConnection conn = new DbConnection();
        ResultSet rs = conn.getQueryTable("select * from customer");
        if(rs!= null){
            System.out.println("Connection Successful");
        }else {
            System.out.println("Connection failed.");
        }
    }
 }