package com.hand;

import java.sql.*;

public class Exam1Main {

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = getConnection();
            connection.setAutoCommit(false);

            int countryId = Integer.parseInt(System.getenv("COUNTRYID"));
            int customerId = Integer.parseInt(System.getenv("CUSTOMERID"));

            if(!"".equals(System.getenv("COUNTRYID")) && !"".equals(System.getenv("CUSTOMERID"))){
                //所有属于这个Country的CityID及名称
                getCity(connection, countryId);
                System.out.println();
                System.out.println("=========================111111分割线111111=========================");
                System.out.println();
                //Customer中租的所有的Film，按租用时间倒排序
                getFilm(connection, customerId);
                connection.commit();
            }else {
                System.out.println("参数不能为空");
            }

        } catch (Exception e) {
            try {
                connection.rollback();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e2) {
            e2.printStackTrace();
        }
    }

    /**
     * 根据Country ID。返回所有属于这个Country的CityID及名称
     *
     * @param connection
     * @param countryId
     */
    public static void getCity(Connection connection, int countryId) throws Exception {
        ResultSet resultSet = null;
        String sql = "SELECT city_id,city FROM city WHERE country_id=?";
        PreparedStatement preparedStatement = connection.prepareCall(sql);

        preparedStatement.setInt(1, countryId);

        resultSet = preparedStatement.executeQuery();

        System.out.println("所有属于这个Country的CityID及名称：");
        while (resultSet.next()) {
            System.out.print(resultSet.getInt("city_id") + "   ");
            System.out.print(resultSet.getString("city"));
            System.out.println();
        }
        if (resultSet != null) {
            resultSet.close();
        }
    }

    /**
     * 根据Customer ID。返回这个Customer中租的所有的Film，按租用时间倒排序
     *
     * @param connection
     * @param customerId
     */
    public static void getFilm(Connection connection, int customerId) throws Exception {
        ResultSet resultSet = null;
        String sql = "SELECT inventory.film_id,film.title,rental.rental_date " +
                "FROM rental,inventory,film " +
                "WHERE rental.inventory_id=inventory.inventory_id " +
                "AND inventory.film_id=film.film_id " +
                "AND rental.customer_id=? " +
                "ORDER BY rental_date DESC ";
        PreparedStatement preparedStatement = connection.prepareCall(sql);

        preparedStatement.setInt(1, customerId);

        resultSet = preparedStatement.executeQuery();

        System.out.println("Customer中租的所有的Film，按租用时间倒排序：");
        while (resultSet.next()) {
            System.out.print(resultSet.getInt("film_id") + "    ");
            System.out.print(resultSet.getString("title") + "    ");
            System.out.print(resultSet.getString("rental_date"));
            System.out.println();
        }

        if (resultSet != null) {
            resultSet.close();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String ip = System.getenv("MYSQL_IP");
            String port = System.getenv("MYSQL_PORT");
            String dbname = System.getenv("DBNAME");
            String username = System.getenv("USERNAME");
            String password = System.getenv("MYSQL_ROOT_PASSWORD");

            System.out.println("1.dbname:" + dbname);
            System.out.println("2.username:" + username);
            System.out.println("3.password:" + password);
            System.out.println("4.ip:" + ip);
            System.out.println("5.port:" + port);
            System.out.println();

            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":" + port + "/" + dbname + "?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true", username, password);
//            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
//                    "root", "1234");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
