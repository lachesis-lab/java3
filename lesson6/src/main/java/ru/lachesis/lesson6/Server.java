package ru.lachesis.lesson6;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Server {
    Logger logger = Logger.getLogger(Server.class.getName());
    int port;
    //   InputStream in;
    //   OutputStream out;
    List<Message> messages = new ArrayList<>();
    Map<String, List<Message>> chats = new HashMap<>();
    List<ClientHandler> clients = new ArrayList<>();

    public static void main(String[] args) {
        new Server(3030);
    }

    Server(int port) {
        this.port = port;
        ExecutorService executorService = Executors.newCachedThreadPool();
        try {
            ServerSocket server = new ServerSocket(port);
            AuthService authService = new AuthService();
            logger.log(Level.INFO,"Сервер начал работу на порту: "+port);
            while (true) {
                Socket socket = server.accept();
                executorService.execute(new ClientHandler(socket, this, authService));
            }
        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            logger.log(Level.SEVERE,"Сервер завершил работу с ошибкой: "+ e.getMessage());
        }
        executorService.shutdown();
    }

    synchronized void onNewClient(ClientHandler clientHandler) {
        clients.add(clientHandler);
/*
        for (int i = 0; i < messages.size(); i++) {
            if (messages.get(i).receiver == null)
                clientHandler.sendMessage(messages.get(i));
        }
*/
        onNewMessage(new Message(clientHandler.client, "вошел в чат"));
        logger.log(Level.INFO,clientHandler.client + " вошел в чат");
    }

    synchronized void onNewMessage(Message message) {
        messages.add(message);
        String key;
        if (message.receiver != null) {
            if (message.sender.name.compareTo(message.receiver.name) > 0)
                key = message.sender.name + message.receiver.name;
            else
                key = message.receiver.name + message.sender.name;
            if (!chats.containsKey(key)) {
                chats.put(key, new ArrayList<>());
            }
            chats.get(key).add(message);
        }
        List<Client> receiverList = new ArrayList<>();
        for (int i = 0; i < clients.size(); i++) {
//            if (!message.sender.login.equals(clients.get(i).client.login)) {
                ClientHandler clientHandler = clients.get(i);
                if (message.receiver == null || clientHandler.client.login.equals(message.receiver.login)) {
//                    if (message.receiver != null)
//                        receiverList.add(message.receiver);
                    clientHandler.sendMessage(message);
                }
//            }
        }
    }

    synchronized void onClientDisconnected(ClientHandler clientHandler) {
        clients.remove(clientHandler);
        onNewMessage(new Message(clientHandler.client, "покинул чат"));
        logger.log(Level.INFO,clientHandler.client + " покинул в чат");
    }
}
