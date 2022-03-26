package QuesMeDemo.exeptions;

public class ErrorFieldException extends Exception{
    public ErrorFieldException (String ex)
    { super ("Введенные данные некорректны!\n" + ex);}
}
