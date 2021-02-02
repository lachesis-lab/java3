package ru.lachesis.chat;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ChatWindow extends JFrame {
    private final int maxMessages = 10;
    private final List<Message> messages = new ArrayList<>();
    private List<String> messageTexts = new ArrayList<>();
    private int sortOrder = 1;
    Client sender;
    JTextArea messageArea;
    JTextField inputField;
    String historyFileName;


    public ChatWindow(Function<Message, Void> sendMessage, Function<Client, Void> createClientRenameWindow, Client sender) {
        this.sender = sender;
        //      JPanel panel = new JPanel(new BorderLayout());
        setTitle("My chat");
        setBounds(100, 100, 400, 500);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        historyFileName = "history_" + sender.login + ".txt";
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(messageArea);
        add(scrollPane, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        JButton refreshButton = new JButton("\u27F3");
        refreshButton.setToolTipText("Refresh");
        refreshButton.addActionListener(e -> {
            sortOrder = 1;
            refresh(messageTexts);
//            refresh(messageArea);
        });
        JButton sortButton = new JButton("\u21C5");
        sortButton.setToolTipText("Sort");
        JButton clearButton = new JButton("\u239A");
        sortButton.addActionListener(e -> {
            sortOrder *= -1;
            refresh(messageTexts);
//            refresh(messageArea);
        });
        clearButton.setToolTipText("Clear");
        clearButton.addActionListener(e -> {
            messageArea.setText("");
            messages.clear();
        });
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Сервис");
        JMenuItem changeName = new JCheckBoxMenuItem("Сменить имя пользователя");
        menuBar.add(menu);
        menu.add(changeName);
        changeName.addActionListener(e -> createClientRenameWindow.apply(sender));
        toolBar.setBorderPainted(true);
        toolBar.add(refreshButton);
        toolBar.add(sortButton);
        toolBar.add(clearButton);
        toolBar.addSeparator();
        menuBar.add(toolBar, BorderLayout.PAGE_START);
        add(menuBar, BorderLayout.PAGE_START);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        add(bottomPanel, BorderLayout.PAGE_END);
        inputField = new JTextField();
        inputField.setBackground(Color.lightGray);
        bottomPanel.add(inputField, BorderLayout.CENTER);

        JButton sendButton = new JButton("Send");
        bottomPanel.add(sendButton, BorderLayout.EAST);
        sendButton.addActionListener(e -> sendMessage.apply(new Message(sender, inputField.getText())));// send(inputField, targetText));
        inputField.addActionListener(e -> sendMessage.apply(new Message(sender, inputField.getText())));
        setTitle(sender.login);
        //       setVisible(true);

    }

    private void refresh(JTextArea targetText) {
        targetText.setText("");
        StringBuilder text = new StringBuilder();
        List<String> messageList = messageTexts.stream().skip(messageTexts.size() - maxMessages).limit(maxMessages).collect(Collectors.toList());
        for (int i = 0; i < messageList.size(); i++) {
            String messageText = messageList.get(sortOrder == -1 ? messageList.size() - i - 1 : i);//messages.get(sortOrder == -1 ? messages.size() - i - 1 : i);
            text.append(messageText).append("\n");
        }
        targetText.setText(text.toString());
    }

    public void refresh(List<String> messageList) {
        if (messageList != null)
            messageTexts = messageList;
        messageArea.setText("");
        if (messageList == null) return;
        StringBuilder text = new StringBuilder();
//        List<String> messageList = messageTexts.stream().skip(messageTexts.size() - maxMessages).limit(maxMessages).collect(Collectors.toList());
        for (int i = 0; i < messageList.size(); i++) {
            String messageText = messageList.get(sortOrder == -1 ? messageList.size() - i - 1 : i);//messages.get(sortOrder == -1 ? messages.size() - i - 1 : i);
            text.append(messageText).append("\n");
        }
        messageArea.setText(text.toString());
    }

    public void onNewMessage(Message message) {
        messages.add(message);
        messageTexts.add(message.text);
        if (sortOrder == 1)
            messageArea.setText(messageArea.getText() + message.text + " \n");
        else if (sortOrder == -1)
            messageArea.setText(message.text + " \n" + messageArea.getText());
        inputField.setText("");

    }


}
