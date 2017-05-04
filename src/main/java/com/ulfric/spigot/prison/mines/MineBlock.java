package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.bean.Bean;

public class MineBlock extends Bean {

	private final String material;
	private final int weight;

	public MineBlock(String material, int weight)
	{
		this.material = material;
		this.weight = weight;
	}

	public String getMaterial()
	{
		return this.material;
	}

	public int getWeight()
	{
		return this.weight;
	}

}
