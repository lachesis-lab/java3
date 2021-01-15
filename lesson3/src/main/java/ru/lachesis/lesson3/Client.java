package ru.lachesis.lesson3;

import java.sql.*;

public class Client {
    int clientId;
    String name;
    String login;
    String password;

    public Client(int clientId, String name, String login, String password) {
        this.name = name;
        this.login = login;
        this.password = password;
        this.clientId = clientId;

    }

    public Client(String name, String login, String password) throws MySQLException {
        this.name = name;
        this.login = login;
        this.password = password;
        if ((this.clientId = addClient(name, login, password)) == -1)
            throw new MySQLException();
    }

    public Client getClient(String login, String psw) {
        Client client = null;
        Connection conn = null;
        PreparedStatement statement = null;
        ResultSet res = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ChatDB.db");
            statement = conn.prepareStatement("SELECT * FROM Client WHERE login = ?");
            statement.setString(1, login);
            res = statement.executeQuery();
            int rowCount = 0;
            while (res.next()) {
                rowCount++;
                if (res.getString("login").equals(login) && res.getString("password").equals(psw))
                    client = new Client(res.getInt("clientID"), res.getString("name"), res.getString("login"), res.getString("password"));
            }
            if (rowCount == 1) return client;

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            close(conn, statement, res);
        }
        return client;
    }

    public int addClient(String name, String login, String psw) {
        Connection conn = null;
        ResultSet res = null;
        PreparedStatement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ChatDB.db");
            statement = conn.prepareStatement("INSERT INTO Client(name,login,password) VALUES (?,?,?);");
            statement.setString(1, name);
            statement.setString(2, login);
            statement.setString(3, psw);
            if (statement.executeUpdate() == 1) {
                statement = conn.prepareStatement("SELECT last_insert_rowid() AS clientId; ");
                res = statement.executeQuery();
                return res.getInt(1);
            } else return -1;
        } catch (ClassNotFoundException | SQLException e) {
            return -1;
        } finally {
            close(conn, statement, res);
        }
    }


    public boolean clientRename(String newName, String login, String psw) {
        Client client = null;
        Connection conn = null;
        ResultSet res = null;
        PreparedStatement statement = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:ChatDB.db");
            statement = conn.prepareStatement("SELECT * FROM Client WHERE login = ?");
            statement.setString(1, login);
//        Statement statement = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
//        ResultSet res = statement.executeQuery(String.format("SELECT * FROM Client WHERE login = \"%s\"",login));
            res = statement.executeQuery();
            int rowCount = 0;
            while (res.next()) {
                rowCount++;
                if (res.getString("login").equals(login) && res.getString("password").equals(psw))
                    client = new Client(res.getInt("clientID"), res.getString("name"), res.getString("login"), res.getString("password"));
            }
            if (rowCount == 1) {
                statement = conn.prepareStatement("UPDATE Client set name = ? WHERE login = ? and password = ?");
                statement.setString(1, newName);
                statement.setString(2, login);
                statement.setString(3, psw);

                if (statement.executeUpdate() == 1) return true;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(conn, statement, res);
        }
        return true;
    }
    public void close(Connection conn, PreparedStatement statement, ResultSet res) {
        try {
            conn.close();
            statement.close();
            res.close();
        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
        }
    }

}
