package com.ulfric.spigot.prison.mines;

public class MineBlock {

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
	
	@Override
	public String toString()
	{
		return new StringBuilder(this.material).append(":").append(this.weight).toString();
	}
	
	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}

		MineBlock mineBlock = (MineBlock) o;

		return material != null ? material.equals(mineBlock.material) : mineBlock.material == null;
	}

	@Override
	public int hashCode()
	{
		return material != null ? material.hashCode() : 0;
	}

}
