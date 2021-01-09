package ru.lachesis.lesson2;

import java.sql.*;
import java.util.ArrayList;

public class AuthService {
    Connection conn;

    //  ArrayList<Client> clients = new ArrayList<>();
    AuthService() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        conn = DriverManager.getConnection("jdbc:sqlite:ChatDB.db");

/*
        clients.add(new Client("Vasily","Vasya","1111"));
        clients.add(new Client("Petr","Petya","1112"));
        clients.add(new Client("Nina","Nina","1113"));
*/
    }

    Client auth(String login, String psw) throws SQLException {
        if (login == null) return null;
        PreparedStatement statement = conn.prepareStatement("SELECT * FROM Client WHERE login = ?");
        statement.setString(1, login);
//        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        ResultSet res = statement.executeQuery(String.format("SELECT * FROM Client WHERE login = \"%s\"",login));
        ResultSet res = statement.executeQuery();
        Client client=null;
        int rowCount = 0;
        while (res.next()) {
            rowCount++;
            if (res.getString("login").equals(login) && res.getString("password").equals(psw))
                client = new Client(res.getInt("clientID"), res.getString("name"), res.getString("login"), res.getString("password"));
        }
        if (rowCount==1) return client;
              /*
        for (Client client:clients){
            if (client.login.equals(login)&& client.password.equals(psw)){
                return client;
            }
        }
*/
        return null;
    }
}
