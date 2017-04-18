package com.ulfric.spigot.prison.warp;

public class WarpException extends RuntimeException {

    protected WarpException(String s, Exception e)
    {
        super(s, e);
    }

    protected WarpException(String s)
    {
        super(s);
    }

    protected WarpException()
    {
        super();
    }

}
