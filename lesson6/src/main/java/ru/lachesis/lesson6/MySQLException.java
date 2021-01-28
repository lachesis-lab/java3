package ru.lachesis.lesson6;

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
