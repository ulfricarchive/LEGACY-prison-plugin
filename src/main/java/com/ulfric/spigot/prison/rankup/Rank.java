package com.ulfric.spigot.prison.rankup;

import java.util.Objects;

import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.spigot.economy.CurrencyAmount;

public class Rank extends Bean implements Comparable<Rank> {
	
	public static Builder builder()
	{
		return new Builder();
	}
	
	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Rank>
	{
		private String name;
		private CurrencyAmount currencyAmount;
	
		Builder()
		{
			
		}
		
		@Override
		public Rank build()
		{
			Objects.requireNonNull(this.name);
			Objects.requireNonNull(this.currencyAmount);
			
			return new Rank(this.name, this.currencyAmount);
		}
		
		public Builder setName(String name)
		{
			this.name = name;
			return this;
		}
		
		public Builder setCurrencyAmount(CurrencyAmount currencyAmount)
		{
			this.currencyAmount = currencyAmount;
			return this;
		}
		
	}
	
	private final String name;
	private final CurrencyAmount currencyAmount;
	
	private Rank(String name, CurrencyAmount currencyAmount)
	{
		this.name = name;
		this.currencyAmount = currencyAmount;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public CurrencyAmount getCurrencyAmount()
	{
		return this.currencyAmount;
	}
	
	@Override
	public int compareTo(Rank rank)
	{
		return Long.valueOf(this.currencyAmount.getAmount()).compareTo(rank.getCurrencyAmount().getAmount());
	}
	
}
