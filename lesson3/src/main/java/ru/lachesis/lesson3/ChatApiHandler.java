package ru.lachesis.lesson3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Function;

public class ChatApiHandler {
    int port;
    String host;
    DataInputStream in;
    DataOutputStream out;
    boolean isAlive = true;
    Client sender;

    class ServerResponse {
        boolean isSuccess;
        String serverError;
        Client client;

        ServerResponse(boolean isSuccess, String serverError, Client client) {
            this.isSuccess = isSuccess;
            this.serverError = serverError;
            this.client = client;
        }
    }

    ChatApiHandler(String host, int port, Function<ServerResponse, String> onAuth, Function<Message, Void> onNewMessage, Function<ServerResponse, String> onRename, Function<ServerResponse, String> onRegistration) {
        this.host = host;
        this.port = port;

        try {
            Socket socket = new Socket(host, port);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            new Thread(() -> {
                Thread.currentThread().setName("socketListener");
                try {
                    while (socket.isConnected()) {
                        String messageText = in.readUTF();
                        if (messageText.startsWith("/auth")) {
                            //System.out.println(messageText);
                            if (messageText.startsWith("/auth ok")) {
                                System.out.println("response success");
                                String[] arrClient = messageText.substring(9).split(" ");
                                Client client = new Client(Integer.parseInt(arrClient[0]), arrClient[1], arrClient[2], arrClient[3]);
                                onAuth.apply(new ServerResponse(true, null, client));
                            } else {
                                onAuth.apply(new ServerResponse(false, messageText, null));
                            }
                        } else if (messageText.startsWith("/rename")) {
                            if (messageText.startsWith("/rename ok")) {
                                onRename.apply(new ServerResponse(true, null, null));
                            } else {
                                onRename.apply(new ServerResponse(false, "Ошибка SQL", null));
                            }
                        } else if (messageText.startsWith("/registration")) {
                            if (messageText.startsWith("/registration ok")) {
                                onRegistration.apply(new ServerResponse(true, null, null));
                            } else {
                                onRegistration.apply(new ServerResponse(false, "<html>Некорректные учетные данные,<br> попробуйте еще раз</html>", null));
                            }
                        } else if (messageText.startsWith("/nm ") && messageText.length() > 4) {
                            onNewMessage.apply(new Message(sender, messageText.substring(4)));
                            onAuth.apply(new ServerResponse(false, messageText, null));
                        } else
                            onAuth.apply(new ServerResponse(false, messageText, null));


                    }
                } catch (IOException e) {
                    try {
                        in.close();
                        out.close();
                        socket.close();
                        isAlive = false;
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    e.printStackTrace();
                    System.exit(0);
                }

            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String auth(SignInWindow.ClientLoginResult clientLoginResult) {
        new Thread(() -> {
            try {
                out.writeUTF("/auth " + clientLoginResult.login + " " + clientLoginResult.password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return null;
    }

    String regUser(SignInWindow.ClientLoginResult clientLoginResult) {
        new Thread(() -> {
            try {
                out.writeUTF("/auth " + clientLoginResult.login + " " + clientLoginResult.password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        return null;
    }

    void sendMessage(Message message) {
        new Thread(() -> {
            try {
                out.writeUTF(message.text);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void renameClient(Client client, String newName) {
        new Thread(() -> {
            try {
                out.writeUTF("/rename " + newName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    void registration(Client client) {
        new Thread(() -> {
            try {
                out.writeUTF("/registration " + client.name + " " + client.login + " " + client.password);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }
/*
    public static void main(String[] args) {
        new ChatApiHandler("localhost", 3030);
    }
*/
}

