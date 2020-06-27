package com.example.demo.dataProcess;

import com.example.demo.dataProcess.Map.*;
import org.yaml.snakeyaml.events.SequenceStartEvent;

import java.io.*;
import java.net.InetAddress;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class dataLoader {
    public static HashMap<String,String> map;

    public static void executeAndStore(PreparedStatement statement,String storePath) throws Exception { //
        ResultSet result = statement.executeQuery();

        FileWriter fw = new FileWriter(storePath);
        int beginID = 0;
        while(result.next())
        {
            fw.write(beginID+"\t"+result.getString(1)+"\n");
            beginID++;
        }
        fw.close();
    }

    public static void personID2ID(Connection conn,String storePath) throws Exception { // 将actor,author,director的id转化为从0开始的ID
        if(storePath.equals("")) storePath="e:/新建文件夹/person_id.txt"; // 默认文件地址

        PreparedStatement statementForActor = conn.prepareStatement("select actorID from actor ;");
        PreparedStatement statementForDirector = conn.prepareStatement("select directorID from director ;");
        PreparedStatement statementForAuthor = conn.prepareStatement("select authorID from author;");

        ResultSet actorSet = statementForActor.executeQuery();
        ResultSet authorSet = statementForAuthor.executeQuery();
        ResultSet directorSet = statementForDirector.executeQuery();

        HashSet<String> personSet = new HashSet<>();

        while(actorSet.next()) personSet.add(actorSet.getString(1));
        while(authorSet.next()) personSet.add(authorSet.getString(1));
        while(directorSet.next()) personSet.add(directorSet.getString(1));

        FileWriter fw = new FileWriter(storePath);
        Iterator<String> it = personSet.iterator();

        int beginID = 0;
        while(it.hasNext())
        {
            fw.write(beginID+"\t"+it.next()+"\n");
            beginID++;
        }
        fw.close();
    }

    public static void filmID2ID(Connection conn,String storePath) throws Exception { // 将FilmID转化为从0开始的ID
        if(storePath.equals("")) storePath="e:/新建文件夹/film_id.txt"; // 默认文件地址
        PreparedStatement statement = conn.prepareStatement("select filmID from film;");
        executeAndStore(statement,storePath);
    }

    public static void userID2ID(Connection conn,String storePath) throws Exception { // 将UserID 转化为从0开始的ID

        if(storePath.equals("")) storePath="e:/新建文件夹/user_id.txt"; // 默认文件地址
        PreparedStatement statement = conn.prepareStatement("select userID from user;");
        executeAndStore(statement,storePath);
    }

    public static void rating2ID(Connection conn,String storePath) throws Exception {
        if(storePath.equals("")) storePath="e:/新建文件夹/rating_id.txt"; // 默认文件地址
        PreparedStatement statement = conn.prepareStatement("");
    }

    public static void writeMapIntoFile(String storePath,Object obj,String name) throws IOException { // 保存map
        System.out.println("writing "+name+" into file...");
        FileOutputStream out = new FileOutputStream(storePath);
        ObjectOutputStream obj_out = new ObjectOutputStream(out);

        obj_out.writeObject(obj);
        out.flush();
        out.close();

        System.out.println("writing "+name+" completed");
    }

    public static <T> T readMapFromFile(String storePath,String name) throws IOException, ClassNotFoundException { // 读取map
        System.out.println("reading "+name+"from file...");

        FileInputStream in = new FileInputStream(storePath);
        ObjectInputStream obj_in = new ObjectInputStream(in);

        T obj = (T)obj_in.readObject();
        in.close();
        System.out.println("reading "+name+" completed");
        return obj;
    }


    public static void getGenres(Connection conn) throws SQLException { // 获取Genres
        PreparedStatement statement = conn.prepareStatement("select film.genres from film;");
        HashSet<String> set = new HashSet<>();
        TreeMap<String,Integer> treeMap = new TreeMap<>();
        ResultSet result = statement.executeQuery();
        String tmp;
        String[] tmp_array;
        while(result.next())
        {
            tmp=result.getString(1);
            tmp_array = tmp.split("/");
            Collections.addAll(set, tmp_array);
            for(String t:tmp_array)
            {
                if(treeMap.containsKey(t)){
                    Integer times = treeMap.get(t);
                    treeMap.put(t,times+1);
                }
                else treeMap.put(t,1);
            }
        }

        System.out.println(set);
        for(String s :treeMap.keySet()){
            System.out.print("\""+s+"\""+":"+treeMap.get(s)+", ");
        }
    }





    private Connection generateConn(String dbName,String userName,String password) throws ClassNotFoundException, SQLException { // 返回数据库连接
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/"+dbName+"?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url,userName,password);
    }

    public static void main(String[] args)
    {
        try
        {

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

}
