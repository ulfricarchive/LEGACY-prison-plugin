package com.ulfric.spigot.prison.util.serializer;

/******************************************************************************
 * Copyright (c) 2017.  Written by Luke, associated with "Studio Ink LTD." *
 * Date: 18/04/2017                                                              *
 ******************************************************************************/
public abstract class Serializer<T> {

    public abstract T from(String context);

    public abstract String to(T object);

}
