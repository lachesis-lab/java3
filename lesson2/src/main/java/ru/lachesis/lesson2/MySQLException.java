package ru.lachesis.lesson2;

public class MySQLException extends Exception {
    MySQLException(){
        super("Ошибка SQL");
    }
    @Override
    public String getMessage() {
        System.out.println("Ошибка SQL");
        return super.getMessage();
    }

 }
