package ru.lachesis.lesson2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    Client sender;
    Client receiver;
    String text;
    Date dateTime;

    public Message(Client sender, String text) {
        this.sender = sender;
        this.text = text;
        dateTime = new Date();
//        dateTime = date.toString();
    }

    public Message(Client sender, Client receiver, String text) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver;
        dateTime = new Date();
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String name;
        try {
            name = sender.name;
        } catch (NullPointerException e) {
            name = "";
        }
        return name + " [" + dateFormat.format(dateTime) + "]: " + text;

    }

    /*

    public Client getReceiver() {
        return receiver;
    }
*/
}
