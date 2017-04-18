package com.ulfric.spigot.prison.warp;

public class WarpException extends RuntimeException {

    protected WarpException(String message, Exception exception)
    {
        super(message, exception);
    }

    protected WarpException(String message)
    {
        super(message);
    }

    protected WarpException()
    {
        super();
    }

}
