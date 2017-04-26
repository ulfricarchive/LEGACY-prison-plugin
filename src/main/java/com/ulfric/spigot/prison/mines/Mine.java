package com.ulfric.spigot.prison.mines;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Mine {

	String region;
	String mine;
	List<MineBlock> mineBlocks = new ArrayList<>();
	Map<MineBlock, Bounds> blockWeights = new HashMap<>();
	int totalWright = 0;

	public Mine(String region, String mine,
			List<MineBlock> mineBlocks)
	{
		this.region = region;
		this.mine = mine;
		this.mineBlocks = mineBlocks;
		updateWrights();
	}

	public void updateWrights()
	{
		Bounds lastBounds = null;
		for (MineBlock mineBlock : mineBlocks)
		{
			Bounds bounds;
			if (lastBounds == null)
			{
				bounds = new Bounds(0, mineBlock.weight);
			} else
			{
				bounds = new Bounds(lastBounds.max, lastBounds.max + mineBlock.weight);
			}
			blockWeights.put(mineBlock,bounds);
			lastBounds = bounds;
		}
		totalWright = lastBounds == null ? 0 : lastBounds.max;
	}

	public MineBlock getNextBLock()
	{
		int roll = new Random().nextInt(totalWright);
		for (Map.Entry<MineBlock, Bounds> entry : blockWeights.entrySet())
		{
			if (roll >= entry.getValue().min && roll <= entry.getValue().max)
			{
				return entry.getKey();
			}
		}
		return null;
	}

	public class Bounds {

		int min;
		int max;

		public Bounds(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
	}

}
