package ru.lachesis.lesson4;

import javax.swing.*;
import java.util.function.Function;

class RenameWindow extends JFrame {

    private final JLabel errorLabel;

    public void showError(String serverError) {
        errorLabel.setText(serverError);
    }

    class RenameClientClass {
        Client client;
        String newName;

        public RenameClientClass(Client client, String newName) {
            this.client = client;
            this.newName = newName;
        }
    }

    RenameWindow(Function<RenameClientClass,Void> onRename, Client sender) {
      //  setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel label = new JLabel("Введите новое имя пользователя");
        errorLabel = new JLabel("\n");
        setBounds(300, 500, 300, 130);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.PAGE_AXIS));
        JPanel newNamePanel = new JPanel();
        newNamePanel.setLayout(new BoxLayout(newNamePanel, BoxLayout.Y_AXIS));
        JTextField newNameField = new JTextField();
        newNamePanel.add(Box.createVerticalStrut(10));
        newNamePanel.add(newNameField);
        newNamePanel.add(Box.createHorizontalGlue());
        JButton changeNameButton = new JButton("Изменить");
        add(label);
        add(errorLabel);
        add(newNamePanel);
        add(changeNameButton);
        changeNameButton.addActionListener(e ->onRename.apply(new RenameClientClass(sender,newNameField.getText())) );
        setVisible(true);
    }
}
