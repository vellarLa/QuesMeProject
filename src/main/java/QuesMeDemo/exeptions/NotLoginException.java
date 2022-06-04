package QuesMeDemo.exeptions;

public class NotLoginException extends Exception{
    public NotLoginException ()
    { super ("Вы не обладаете правами для доступа к этой странице\n");}
}
