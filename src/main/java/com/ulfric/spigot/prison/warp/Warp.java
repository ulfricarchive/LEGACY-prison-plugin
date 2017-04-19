package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.bean.Bean;
import org.bukkit.Location;

public final class Warp extends Bean {

	public static Builder build()
	{
		return new Builder();
	}

	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Warp>
	{
		private String name;
		private Location location;

		Builder()
		{

		}

		@Override
		public Warp build()
		{
			if (this.name == null || this.location == null)
			{
				throw new WarpException("Either name or location is null when building a warp");
			}

			return new Warp(this.name, this.location);
		}

		public Builder setName(String name)
		{
			this.name = name;
			return this;
		}

		public Builder setLocation(Location location)
		{
			this.location = location;
			return this;
		}

	}

	private final String name;
	private Location location;

	Warp(String name, Location location)
	{
		this.name = name;
		this.location = location;
	}

	public String getName()
	{
		return this.name;
	}

	public Location getLocation()
	{
		return this.location;
	}

	public void setLocation(Location location)
	{
		this.location = location;
	}

}
