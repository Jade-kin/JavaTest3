package com.hand;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class ConnectionFactory {

    private static String driver = "com.mysql.jdbc.Driver";

    private static String dburl;

    private static String user;

    private static String password;

    private static final ConnectionFactory factory = new ConnectionFactory();

    static Connection connection;

    static {
        Properties properties = new Properties();

        try {
            InputStream is = new FileInputStream("docker-compose.yml");
            properties.load(is);
            is.close();
        }catch (Exception e){
            System.out.println("配置文件读取失败");
        }

        dburl = "jdbc:mysql://"+properties.getProperty("mysql_ip")+":"
                +properties.getProperty("port")+"/"
                +properties.getProperty("mysql_name")+"?useSSL=true";

        user = properties.getProperty("user");
        password = properties.getProperty("MYSQL_ROOT_PASSWORD");
    }

    private ConnectionFactory(){

    }

    public static ConnectionFactory getInstance(){
        return factory;
    }

    public Connection getConnection(){
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(dburl,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }
        return connection;
    }

}
