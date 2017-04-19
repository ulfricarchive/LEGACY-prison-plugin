package com.ulfric.spigot.prison.spawn;

public class SpawnException extends RuntimeException {

	protected SpawnException(String message, Exception exception)
	{
		super(message, exception);
	}

	protected SpawnException(String message)
	{
		super(message);
	}

	protected SpawnException()
	{
		super();
	}

}
