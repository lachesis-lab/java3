package ru.lachesis.lesson6;

import javax.swing.*;
import java.util.function.Function;

public class RegWindow extends JFrame {

    private final JLabel errorLabel;

    public void showError(String serverError) {
        errorLabel.setText(serverError);
    }

    RegWindow(Function<Client,Void> onReg) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(300, 300, 300, 200);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        errorLabel = new JLabel("<html><br></html>");
//        errorLabel.setHorizontalAlignment(0);
        JLabel loginLabel = new JLabel("Введите логин");
        JTextField loginField = new JTextField();
        JLabel nameLabel = new JLabel("Введите имя пользователя");
        JTextField nameField = new JTextField();
        JLabel passwordLabel1 = new JLabel("Введите пароль");
        JTextField passwordField1 = new JTextField();
/*
        JLabel passwordLabel2 = new JLabel("Введите пароль еще раз");
        JTextField passwordField2 = new JTextField();
*/
        JButton regButton = new JButton("Зарегистрироваться");
        add(errorLabel);
        add(Box.createVerticalGlue());
        add(loginLabel);
        add(loginField);
        add(nameLabel);
        add(nameField);
        add(passwordLabel1);
        add(passwordField1);
//        add(passwordLabel2);
//        add(passwordField2);
        add(regButton);
        regButton.addActionListener(e->
                onReg.apply(new Client(0,nameField.getText(),loginField.getText(),passwordField1.getText())));
        setVisible(true);
    }
}
