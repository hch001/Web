package com.example.demo.other;
import java.sql.*;

public class Neo4jProcessor {
    private static String username = "neo4j",password="qazxsw123";
    public static Connection conn;
    static {
        try {
            Class.forName("org.neo4j.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:neo4j:http://localhost:7474/",username,password);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getNeo4jConn(){return conn;}

    public static void main(String[] args){

    }
}
