package com.example.demo.dataProcess;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class Map {

    // 下面八个为 影视资源的属性ID->entityIDs 的映射。例如:影视资源发布的时代作为一个实体的ID
    public static HashMap<String, String> basicGenre = new HashMap<>();
    public static ArrayList<String> basicGenre_priorityList = new ArrayList<>(); // 同时出现多个标签的时候，标签的优先级，一般越小众越优先

    public static HashMap<String,String> specificGenre = new HashMap<>();
    public static ArrayList<String> specificGenre_priorityList = new ArrayList<>();

    public static HashMap<String, String> specialGenre = new HashMap<>();
    public static ArrayList<String> specialGenre_priorityList = new ArrayList<>();

    public static HashMap<String, String> language = new HashMap<>();
    public static HashMap<String, String> area = new HashMap<>();
    public static HashMap<String, String> rating = new HashMap<>();
    public static HashMap<String, String> type = new HashMap<>();
    public static HashMap<String, String> period = new HashMap<>();

    public static HashMap<String,String> relation_type_id = new HashMap<>(); // 关系本身的relationID
    public static ArrayList<String> relation_names = new ArrayList<>();

    public static int entityBeginID = 0;
    public static int userBeginID = 0;
    public static int relationBeginID = 0;

    // 类初始化
    static {

        for (String s : new String[]{"恐怖","悬疑","犯罪","动作","喜剧","爱情","默认"}) // basicGenre关系连接的entityIDs，默认是没有的关系
        {
            basicGenre.put(s, entityBeginID + "");
            basicGenre_priorityList.add(s);
            entityBeginID++;
        }
        basicGenre_priorityList.remove("默认"); // 默认不作为优先项
        basicGenre.put("惊悚", basicGenre.get("恐怖"));
        basicGenre_priorityList.add(basicGenre_priorityList.indexOf("恐怖"),"惊悚"); // 惊悚和恐怖看作一样

        for(String s:new String[]{"鬼怪","灾难","短片","黑色电影","运动","武侠","古装","历史","战争","家庭","科幻","奇幻","冒险","默认"}){ // specificGenre关系连接的entityIDs，默认是没有的意思
            specificGenre.put(s,entityBeginID+"");
            specificGenre_priorityList.add(s);
            entityBeginID++;
        }
        specificGenre_priorityList.remove("默认");

        for (String s : new String[]{"舞台艺术","成人","戏曲","儿童","同性","歌舞","音乐","传记","默认"}) // 小众genre关系连接的entityIDs,默认是没有的意思
        {
            specialGenre.put(s, entityBeginID + "");
            specialGenre_priorityList.add(s);
            entityBeginID++;
        }
        specialGenre_priorityList.remove("默认");
        specialGenre.put("情色",specialGenre.get("成人"));
        specialGenre_priorityList.add(specialGenre_priorityList.indexOf("成人"),"情色"); // 成人和情色看作相同

        for (String s : new String[]{"英语", "汉语", "法语", "德语", "日语", "俄语", "西班牙语", "意大利语", "韩语", "粤语","未知","默认"}) // 主要语言关系连接的entityIDs，默认代表小众语言
        {
            language.put(s, entityBeginID + "");
            entityBeginID++;
        }
        language.put("普通话", language.get("汉语"));
        language.put("中文", language.get("汉语"));

        for (String s : new String[]{"美国", "中国大陆", "中国台湾", "中国香港", "法国", "德国", "西德", "东德", "日本",
                "俄罗斯", "苏联", "西班牙", "意大利", "西班牙", "韩国", "澳大利亚", "加拿大", "印度","欧洲","东南亚", "中东","未知","默认"}) { // 主要地区关系连接的entityIDs，默认代表小众地区
            area.put(s, entityBeginID + "");
            entityBeginID++;
        }
        area.put("中国",area.get("中国大陆"));

        for(String s:new String[]{"bad","normal","good","brilliant","legendary","默认"}) // 评分关系连接的entityIDs，默认代表没有评分
        {
            rating.put(s,entityBeginID+"");
            entityBeginID++;
        }

        for(String s:new String[]{"movie","animation","documentary","tv","默认"}) // 影视资源的种类关系连接的entityIDs，默认是没有的意思
        {
            type.put(s,entityBeginID+"");
            entityBeginID++;
        }

        for(String s:new String[]{"after16","10To15","00To09","90s","80s","70s","before70s","默认"}) // 时代关系连接的entityIDs，默认是没有的意思
        {
            period.put(s,entityBeginID+"");
            entityBeginID++;
        }

        for(String s:new String[]{"basicGenre","specificGenre","specialGenre","language","area","rating","type","period","actor","director","author"}) // 关系本身的relationID
        {
            relation_type_id.put(s,relationBeginID+"");
            relation_names.add(s);
            relationBeginID++;
        }
    }

    // 初始化
    public static void init(boolean withHead){
        try{
            Connection conn = getConn("db2","root","qazxsw123");
            HashMap<String,String> items = item2entityID(conn);
            HashMap<String,String> persons = person2entityID(conn);
            HashMap<String,String> users = user2newID(conn);
            HashMap<String,String[]> properties = property2entityIDs(conn,false,items);
            HashMap<String,ArrayList<String[]>> ratings = convertRating(conn,users,items,3);
            HashMap<String,ArrayList<String[]>> kg = convertKg(conn,persons,items);

            write("items",items,",",withHead);
            write("users",users,",",withHead);
            write("persons",persons,",",withHead);
            write("properties",properties,",",withHead);
            write("ratings",ratings,",",withHead);
            write("kg",kg,",",withHead);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    // 获取数据库连接
    private static Connection getConn(String dbName,String userName,String password) throws ClassNotFoundException, SQLException { // 返回数据库连接
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url="jdbc:mysql://localhost:3306/"+dbName+"?useSSL=false&serverTimezone=UTC";
        return DriverManager.getConnection(url,userName,password);
    }

    // 影视资源的属性ID转换成的entityIDs  k:v  ->  filmID:[entity1,entity2,...,entity6]
    public static HashMap<String,String[]> property2entityIDs(Connection conn, boolean DEFAULT,HashMap<String,String> items) throws Exception {
        PreparedStatement statement = conn.prepareStatement("select genres,language,area,rating,film.type,releaseDate,filmID from film;");
        ResultSet propertyResultSet = statement.executeQuery();
        HashMap<String,String[]> properties=new HashMap<>(); // 整个数据集的属性集的entityID
        while(propertyResultSet.next())
        {
            String[] nonItemList=new String[6]; // 存储属性的entityID

            nonItemList[0]=propertyResultSet.getString(1);
            String[] tmp=propertyResultSet.getString(2).split("/");
            if(tmp.length==0) nonItemList[1]="";
            else nonItemList[1]=tmp[0];
            tmp=propertyResultSet.getString(3).split("/");
            if(tmp.length==0) nonItemList[2]="";
            else nonItemList[2]=tmp[0];
            nonItemList[3]=propertyResultSet.getString(4);
            nonItemList[4]=propertyResultSet.getString(5);
            nonItemList[5]=propertyResultSet.getString(6);


            boolean found = false;
            String[] entityIDs = new String[8]; // 顺序和上面的定义一样,每行数据的属性实体id

            for(String s:basicGenre_priorityList){ // 返回basicGenre所连接的entity_id nonItemList[0]->entityIDs[0]
                if(nonItemList[0].contains(s)){
                    found = true;
                    entityIDs[0]=basicGenre.get(s);
                    break;
                }
            }
            if(!found) entityIDs[0]=basicGenre.get("默认");

            found=false;
            for(String s:specificGenre_priorityList){ // 返回specificGenre 连接的entity_id nonItemList[0]->entityIDs[1]
                if(nonItemList[0].contains(s)){
                    found=true;
                    entityIDs[1]=specificGenre.get(s);
                    break;
                }
            }
            if(!found) entityIDs[1]=specificGenre.get("默认");

            found=false;
            for(String s:specialGenre_priorityList){ // 返回specialGenre所连接的entity_id nonItemList[0]->entityIDs[2]
                if(nonItemList[0].contains(s)){
                    found=true;
                    entityIDs[2]=specialGenre.get(s);
                    break;
                }
            }
            if(!found) entityIDs[2]=specialGenre.get("默认");

            found=false;
            for(String s:language.keySet()){ // 返回language所连接的entity_id nonItemList[1]->entityIDs[3]
                if(nonItemList[1].contains(s)){
                    found=true;
                    entityIDs[3]=language.get(s);
                    break;
                }
            }
            if(!found) entityIDs[3]=language.get("默认");

            found=false;
            for(String s:area.keySet()){ // 返回area所连接的entity_id nonItemList[2]->entityIDs[4]
                if(nonItemList[2].contains(s)){
                    found=true;
                    entityIDs[4]=area.get(s);
                    break;
                }
            }
            if(!found) entityIDs[4] = area.get("默认");

            if(nonItemList[3]==null){ // 返回rating所连接的entity_id nonItemList[3]->entityIDs[5]
                entityIDs[5]=rating.get("默认");
            }
            else{
                float rating_value = Float.parseFloat(nonItemList[3]); // 评分的具体值
                if(rating_value<=6.0) entityIDs[5]=rating.get("bad");
                else if(rating_value<=7.5) entityIDs[5]=rating.get("normal");
                else if(rating_value<=8.5) entityIDs[5]=rating.get("good");
                else if(rating_value<9.0) entityIDs[5]=rating.get("brilliant");
                else entityIDs[5]=rating.get("legendary");
            }

            found=false;
            for(String s:type.keySet()){ // 返回area所连接的entity_id nonItemList[4]->entityIDs[6]
                if(nonItemList[4].contains(s)){
                    found=true;
                    entityIDs[6]=type.get(s);
                    break;
                }
            }
            if(!found) entityIDs[6] = type.get("默认");

            if(nonItemList[5]==null){ // 返回上映的时代 nonItemList[5]->entityIDs[7]
                entityIDs[7]=period.get("默认");
            } else {
                int year = Integer.parseInt(nonItemList[5].split("-")[0]);

                if(year>=2016) entityIDs[7]=period.get("after16");
                else if(year>=2010) entityIDs[7]=period.get("10To15");
                else if(year>=2000) entityIDs[7]=period.get("00To09");
                else if(year>=1990) entityIDs[7]=period.get("90s");
                else if(year>=1980) entityIDs[7]=period.get("80s");
                else if(year>=1970) entityIDs[7]=period.get("70s");
                else entityIDs[7]=period.get("before70s");
            }

            if(!DEFAULT){ // 不需要default的实体的话
                if(entityIDs[0].equals(basicGenre.get("默认"))) entityIDs[0]="";
                if(entityIDs[1].equals(specificGenre.get("默认"))) entityIDs[1]="";
                if(entityIDs[2].equals(specialGenre.get("默认"))) entityIDs[2]="";
                if(entityIDs[3].equals(language.get("默认"))) entityIDs[3]="";
                if(entityIDs[4].equals(area.get("默认"))) entityIDs[4]="";
                if(entityIDs[5].equals(rating.get("默认"))) entityIDs[5]="";
                if(entityIDs[6].equals(type.get("默认"))) entityIDs[6]="";
                if(entityIDs[7].equals(period.get("默认"))) entityIDs[7]="";
            }

            properties.put(items.get(propertyResultSet.getString(7)),entityIDs);
        }

        return properties;
    }

    // 返回items的entityID
    public static HashMap<String,String> item2entityID(Connection conn) throws SQLException { 
        HashMap<String,String> items = new HashMap<>();

        PreparedStatement statement = conn.prepareStatement("select filmID from film;");
        ResultSet resultSet = statement.executeQuery();
        while(resultSet.next())
        {
            items.put(resultSet.getString(1),entityBeginID+"");
            entityBeginID++;
        }
        System.out.println("num of items : "+items.size());
        return items;
    }

    // 将所有person转化成entityID
    public static HashMap<String,String> person2entityID(Connection conn) throws SQLException { 
        HashMap<String,String> persons = new HashMap<>();
        ResultSet actors = conn.prepareStatement("select actorID from actor;").executeQuery();
        ResultSet authors = conn.prepareStatement("select authorID from author;").executeQuery();
        ResultSet directors = conn.prepareStatement("select directorID from director;").executeQuery(); // 查询

        ArrayList<ResultSet> array = new ArrayList<>();
        array.add(actors);
        array.add(authors);
        array.add(directors);

        HashSet<String> distinctPersons = new HashSet<>(); // 所有person都加入到这个集合里
        array.forEach((result)->{
            try{
                while(result.next()){
                    distinctPersons.add(result.getString(1));
                }
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        distinctPersons.forEach((p)->{ // 生成entityID
            persons.put(p,entityBeginID+"");
            entityBeginID++;
        });
        System.out.println("num of person : "+persons.size());
        return persons;
    }

    // 所有user转化成新的userID，用户不是实体
    public static HashMap<String,String> user2newID(Connection conn) throws SQLException {
        HashMap<String,String> users = new HashMap<>();
        ResultSet resultSet = conn.prepareStatement("select userID from user;").executeQuery();
        while(resultSet.next()){
            users.put(resultSet.getString(1),userBeginID+"");
            userBeginID++;
        }
        System.out.println("num of users : "+users.size());
        return users;
    }

    // 转换rating，最终结构是 userID:[[itemID,"0"/"1"],[]...]
    public static HashMap<String,ArrayList<String[]>> convertRating(Connection conn,HashMap<String,String> users,HashMap<String,String> items,float threshold) throws SQLException{ 
        HashMap<String,ArrayList<String[]>> ratings =new HashMap<>(); // userID:[[itemID,pos/neg]]
        ResultSet resultSet = conn.prepareStatement("select userID,filmID,rating from comment where rating is not null;").executeQuery();
        String userID,itemID;
        String user_entityID,item_entityID; // 转换后的userID
        while(resultSet.next()){
            userID=resultSet.getString(1);
            itemID=resultSet.getString(2);
            user_entityID=users.get(userID);
            item_entityID=items.get(itemID);

            if(resultSet.getFloat(3)>threshold){
                ratings.computeIfAbsent(user_entityID, k -> new ArrayList<>());
                ratings.get(user_entityID).add(new String[]{item_entityID,"1"});
            }  else{
                ratings.computeIfAbsent(user_entityID, k -> new ArrayList<>());
                ratings.get(user_entityID).add(new String[]{item_entityID,"0"});
            }
        }
        return ratings;
    }

    // itemID:[(personID,relationID),()...()]
    public static HashMap<String,ArrayList<String[]>> convertKg(Connection conn,HashMap<String,String> persons,HashMap<String,String> items) throws SQLException{
        HashMap<String,ArrayList<String[]>> kg = new HashMap<>();

        ResultSet actors = conn.prepareStatement("select filmID,actorID from perform;").executeQuery();
        ResultSet directors = conn.prepareStatement("select filmID,directorID from direct;").executeQuery();
        ResultSet authors = conn.prepareStatement("select filmID,authorID from `write`;").executeQuery();

        String[] relationNames = {"actor","director","author"};
        ResultSet[] results = {actors,directors,authors};

        for(int i=0;i<results.length;i++){
            String relationName = relationNames[i];
            String itemID,personID;

            while(results[i].next()){
                itemID=items.get(results[i].getString(1));
                personID=persons.get(results[i].getString(2));
                kg.computeIfAbsent(itemID, k -> new ArrayList<>());
                kg.get(itemID).add(new String[]{personID,relation_type_id.get(relationName)});
            }
        }
        return kg;
    }

    // 将上面这些数据写入文件保存
    public static void write(String mode,Object collection,String sep,boolean withHead) throws Exception{

        String dirPath = "e:/kg";

        switch (mode){
            case "properties":{
                FileWriter fw = new FileWriter(dirPath+"/data/kg.csv",true);
                System.out.println("converting properties...");
                if(withHead) fw.write("itemID"+sep+"relationID"+sep+"entityID"+"\n");
                HashMap<String,String[]> properties = (HashMap<String,String[]>)collection;
                for(String itemID:properties.keySet()){
                    String[] ps = properties.get(itemID);
                    for(int i=0;i<ps.length;i++) if(!ps[i].equals("")) fw.write(itemID+sep+relation_type_id.get(relation_names.get(i))+sep+ps[i]+"\n");
                }
                fw.close();
                break;
            }
            case "items":{
                FileWriter fw = new FileWriter(dirPath+"/data/entities.csv",true);
                System.out.println("converting items...");
                if(withHead) fw.write("itemID"+sep+"entityID\n");
                HashMap<String,String> items = (HashMap<String,String>)collection;
                items.forEach((itemID,entity)->{
                    try {
                        fw.write(itemID+sep+entity+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fw.close();
                break;
            }
            case "persons" :{
                FileWriter fw = new FileWriter(dirPath+"/data/entities.csv",true);
                System.out.println("converting persons...");
                HashMap<String,String> persons = (HashMap<String,String>)collection;
                persons.forEach((personID,entityID)->{
                    try {
                        fw.write(personID+sep+entityID+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fw.close();
                break;
            }
            case "users":{
                FileWriter fw = new FileWriter(dirPath+"/data/users.csv",true);
                System.out.println("converting users...");
                if(withHead) fw.write("user_oldID"+sep+"user_newID\n");
                HashMap<String,String> users = (HashMap<String,String>)collection;
                users.forEach((userID,entityID)->{
                    try {
                        fw.write(userID+sep+entityID+"\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                fw.close();
                break;
            }
            case "ratings":{
                FileWriter fw = new FileWriter(dirPath+"/data/ratings.csv",true);
                System.out.println("converting ratings...");
                if(withHead) fw.write("userID"+sep+"itemID"+sep+"rating\n");
                HashMap<String,ArrayList<String[]>> ratings = (HashMap<String,ArrayList<String[]>>)collection;
                ratings.forEach((userID,rating_list)->{
                    rating_list.forEach((rating)->{
                        try {
                            fw.write(userID+sep+rating[0]+sep+rating[1]+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                fw.close();
                break;
            }
            case "kg":{
                FileWriter fw = new FileWriter(dirPath+"/data/kg.csv",true);
                System.out.println("converting kg...");
                HashMap<String,ArrayList<String[]>> kg = (HashMap<String,ArrayList<String[]>>)collection;
                kg.forEach((itemID,person_relationList)->{
                    person_relationList.forEach((person_relation)->{
                        try {
                            fw.write(itemID+sep+person_relation[1]+sep+person_relation[0]+"\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
                break;
            }
        }
    }

    // 保存映射
    public static void saveMap(){

    }

    public static void main(String[] args){
        init(false);
        System.out.print(entityBeginID);
    }

}
