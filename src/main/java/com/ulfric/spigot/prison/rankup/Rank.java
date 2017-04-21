package com.ulfric.spigot.prison.rankup;

import com.ulfric.commons.bean.Bean;

import java.util.Objects;

public class Rank extends Bean implements Comparable<Rank> {
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Rank>
	{
		private String name;
		private long price;
	
		Builder()
		{
			
		}
		
		@Override
		public Rank build()
		{
			Objects.requireNonNull(this.name);
			
			return new Rank(this.name, this.price);
		}
		
		public Builder setName(String name)
		{
			this.name = name;
			return this;
		}
		
		public Builder setPrice(long price)
		{
			this.price = price;
			return this;
		}
		
	}
	
	private final String name;
	private final long price;
	
	Rank(String name, long price)
	{
		this.name = name;
		this.price = price;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public long getPrice()
	{
		return this.price;
	}
	
	@Override
	public int compareTo(Rank o)
	{
		return Long.valueOf(this.price).compareTo(o.getPrice());
	}
	
}
