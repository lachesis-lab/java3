package ru.lachesis.lesson2;

import javax.swing.*;
import java.util.function.Function;
import java.util.function.Supplier;


public class SignInWindow extends JFrame {

    private final JLabel errorLabel;

    class ClientLoginResult {
        String login;
        String password;

        public ClientLoginResult(String login, String password) {
            this.login = login;
            this.password = password;
        }
    }

    SignInWindow(Function<ClientLoginResult, Void> onLogin, Supplier<Void> onClickRegButton) {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(500, 300, 320, 170);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        JPanel topPanel = new JPanel();
//        topPanel.setSize (320, 50);

        topPanel.setAlignmentX(1);
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));

        JTextField loginField = new JTextField();
        loginField.setName("Логин");
        JTextField passwordField = new JTextField();
        passwordField.setName("Пароль");
        JButton signInButton = new JButton("Войти");
        signInButton.addActionListener(e -> onLogin.apply(new ClientLoginResult(loginField.getText(), passwordField.getText())));
        loginField.addActionListener(e -> onLogin.apply(new ClientLoginResult(loginField.getText(), passwordField.getText())));
        passwordField.addActionListener(e -> onLogin.apply(new ClientLoginResult(loginField.getText(), passwordField.getText())));
        JButton regButton = new JButton("Регистрация");
        regButton.addActionListener(e -> onClickRegButton.get());
        errorLabel = new JLabel();
        JLabel label = new JLabel("Пожалуйста, авторизуйтесь");
        errorLabel.setText("<html><br></html>");
//        errorLabel.setHorizontalAlignment(JLabel.CENTER);
//        topPanel.setAlignmentX (JLabel.CENTER);
        add(topPanel);
        topPanel.add(errorLabel);
        topPanel.add(label);
        add(Box.createVerticalStrut(20));
        add(loginField);
        add(Box.createVerticalStrut(10));
        add(passwordField);
        bottomPanel.add(signInButton);
        bottomPanel.add(Box.createHorizontalGlue());
//        bottomPanel.add(Box.createHorizontalStrut(1));
        bottomPanel.add(regButton);
//        bottomPanel.add(Box.createVerticalStrut(10));
        add(bottomPanel);
//        pack();
//        setLocationRelativeTo(null);
        setVisible(true);


    }

    public void showError(String errorText) {
        errorLabel.setText(errorText);
    }

}
