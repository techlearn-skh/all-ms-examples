package com.skh.exceptions;

public class MyCustomException4001100 extends RuntimeException{
    public MyCustomException4001100(String message){
        super(message);
    }

    public MyCustomException4001100(String message, Throwable exception){
        super(message, exception);
    }


}
