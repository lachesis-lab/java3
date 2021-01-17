package ru.lachesis.lesson4;

import java.io.ObjectOutputStream;

public class ClientApp {
    ChatApiHandler api;
    SignInWindow signInWindow;
    ChatWindow chatWindow;
    RenameWindow renameWindow;
    RegWindow regWindow;
    Client sender;
    ObjectOutputStream out;
    HistoryManager historyManager;

    ClientApp() {
        api = new ChatApiHandler("localhost", 3030, this::onAuth, this::onNewMessage, this::onRename, this::onRegistration);

        signInWindow = new SignInWindow(this::onLogin, this::onClickRegButton);
    }

    private String onRegistration(ChatApiHandler.ServerResponse serverResponse) {
        if (serverResponse.isSuccess) {
            hideRegistrationWindow();
            showSignInWindow();
            signInWindow.showError("<html>Регистрация завершена.</html>");
        } else regWindow.showError(serverResponse.serverError);
        return serverResponse.serverError;
    }

    private String onRename(ChatApiHandler.ServerResponse serverResponse) {
        if (serverResponse.isSuccess) {
            hideRenameWindow();
        } else renameWindow.showError(serverResponse.serverError);
        return serverResponse.serverError;
    }

    private Void onClickRegButton() {
        signInWindow.setVisible(false);
        regWindow = new RegWindow(this::onReg);
        return null;
    }

    synchronized Void onLogin(SignInWindow.ClientLoginResult clientLoginResult) {
        api.auth(clientLoginResult);
        return null;
    }

    synchronized Void onReg(Client client) {
        api.registration(client);
        hideSignInWindow();
        return null;
    }

    synchronized String onAuth(ChatApiHandler.ServerResponse serverResponse) {
        System.out.println("login: " + serverResponse.isSuccess);
        if (serverResponse.isSuccess) {
            sender = serverResponse.client;
            signInWindow.showError(serverResponse.serverError);
            chatWindow = new ChatWindow(this::sendMessage, this::createClientRenameWindow, sender);
            hideSignInWindow();
            showChatWindow();
            historyManager = new HistoryManager(sender.login);

            chatWindow.refresh(historyManager.readHistory());

        } else signInWindow.showError(serverResponse.serverError);
        return serverResponse.serverError;
    }

    synchronized Void sendMessage(Message message) {
        api.sendMessage(message);
        return null;
    }

    public Void onNewMessage(Message message) {
        synchronized (chatWindow) {
            chatWindow.onNewMessage(message);
            historyManager.saveHistory(message);
        }
        return null;
    }


    synchronized public Void createClientRenameWindow(Client client) {
        renameWindow = new RenameWindow(this::renameClient, client);
        return null;
    }


    private Void renameClient(RenameWindow.RenameClientClass renameClientClass) {
        api.renameClient(renameClientClass.client, renameClientClass.newName);
        return null;
    }


    private void hideRegistrationWindow() {
        regWindow.setVisible(false);
    }

    private void hideRenameWindow() {
        renameWindow.setVisible(false);
    }

    private void showSignInWindow() {
        signInWindow.setVisible(true);
    }

    private void hideSignInWindow() {
        signInWindow.setVisible(false);
    }

    private void showChatWindow() {
        chatWindow.setVisible(true);
    }

    private void hideChatWindow() {
        chatWindow.setVisible(false);
    }


    public static void main(String[] args) {
        new ClientApp();
    }

}
