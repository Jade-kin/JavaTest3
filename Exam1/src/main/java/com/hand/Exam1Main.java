package com.hand;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;


public class Exam1Main {

    public static void main( String[] args ) {
        Connection connection = null;
        try {
            connection = ConnectionFactory.getInstance().getConnection();
            connection.setAutoCommit(false);

            Properties properties = new Properties();

            try {
                InputStream is = new FileInputStream("docker-compose.yml");
                properties.load(is);
                is.close();
            }catch (Exception e){
                System.out.println("配置文件读取失败");
            }

            int countryId = Integer.parseInt(properties.getProperty("countryId"));
            int customerId = Integer.parseInt(properties.getProperty("customerId"));

            //所有属于这个Country的CityID及名称
            getCity(connection,countryId);
            System.out.println();
            System.out.println("=========================分割线=========================");
            System.out.println();
            //Customer中租的所有的Film，按租用时间倒排序
            getFilm(connection,customerId);

            connection.commit();

        }catch (Exception e){
            try {
                connection.rollback();
            }catch (Exception e1){
                e1.printStackTrace();
            }
        }try {
            if(connection != null){
                connection.close();
            }
        }catch (SQLException e2){
            e2.printStackTrace();
        }
    }

    /**
     * 根据Country ID。返回所有属于这个Country的CityID及名称
     * @param connection
     * @param countryId
     * */
    public static void getCity(Connection connection, int countryId) throws Exception{
        ResultSet resultSet = null;
        String sql = "SELECT city_id,city FROM city WHERE country_id=?";
        PreparedStatement preparedStatement = connection.prepareCall(sql);

        preparedStatement.setInt(1,countryId);

        resultSet = preparedStatement.executeQuery();

        System.out.println("所有属于这个Country的CityID及名称：");
        while (resultSet.next()){
            System.out.print(resultSet.getInt("city_id")+"   ");
            System.out.print(resultSet.getString("city"));
            System.out.println();
        }
        if(resultSet != null){
            resultSet.close();
        }
    }

    /**
     * 根据Customer ID。返回这个Customer中租的所有的Film，按租用时间倒排序
     * @param connection
     * @param customerId
     * */
    public static void getFilm(Connection connection,int customerId) throws Exception{
        ResultSet resultSet = null;
        String sql = "SELECT inventory.film_id,film.title,rental.rental_date " +
                "FROM rental,inventory,film " +
                "WHERE rental.inventory_id=inventory.inventory_id " +
                "AND inventory.film_id=film.film_id " +
                "AND rental.customer_id=? " +
                "ORDER BY rental_date DESC ";
        PreparedStatement preparedStatement = connection.prepareCall(sql);

        preparedStatement.setInt(1,customerId);

        resultSet = preparedStatement.executeQuery();

        System.out.println("Customer中租的所有的Film，按租用时间倒排序：");
        while (resultSet.next()){
            System.out.print(resultSet.getInt("film_id")+"    ");
            System.out.print(resultSet.getString("title")+"    ");
            System.out.print(resultSet.getString("rental_date"));
            System.out.println();
        }

        if(resultSet != null){
            resultSet.close();
        }
    }
}
