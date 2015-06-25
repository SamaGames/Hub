package net.samagames.hub.bar;

public class BarMessage implements Cloneable
{
    private String message;
    private int seconds;

    public BarMessage(String message)
    {
        this(message, 5);
    }

    public BarMessage(String message, int seconds)
    {
        this.message = message;
        this.seconds = seconds;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setSeconds(int seconds)
    {
        this.seconds = seconds;
    }

    public String getMessage()
    {
        return this.message;
    }

    public int getSeconds()
    {
        return this.seconds;
    }

    public BarMessage clone() throws CloneNotSupportedException
    {
        return (BarMessage) super.clone();
    }
}
