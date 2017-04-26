package com.ulfric.spigot.prison.mines;

public class MineBlock {

	String material;
	int weight;

	public MineBlock(String material, int weight)
	{
		this.material = material;
		this.weight = weight;
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
