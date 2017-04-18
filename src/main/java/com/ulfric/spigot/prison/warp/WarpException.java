package com.ulfric.spigot.prison.warp;

/******************************************************************************
 * Copyright (c) 2017.  Written by Luke, associated with "Studio Ink LTD." *
 * Date: 18/04/2017                                                              *
 ******************************************************************************/
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
