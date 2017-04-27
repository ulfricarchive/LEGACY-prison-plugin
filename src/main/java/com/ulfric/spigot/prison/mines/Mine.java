package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.bean.Bean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

public final class Mine extends Bean {

	public static Builder builder()
	{
		return new Builder();
	}
	
	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Mine>
	{
		
		private String region;
		private String mine;
		private List<MineBlock> mineBlocks;
		
		Builder()
		{
			
		}
		
		@Override
		public Mine build()
		{
			Objects.requireNonNull(this.region);
			Objects.requireNonNull(this.mine);
			
			if (this.mineBlocks == null)
			{
				return new Mine(this.region, this.mine);
			}
			
			return new Mine(this.region, this.mine, this.mineBlocks);
		}
		
		public Builder setRegion(String region)
		{
			this.region = region;
			return this;
		}
		
		public Builder setMine(String mine)
		{
			this.mine = mine;
			return this;
		}
		
		public Builder setMineBlocks(List<MineBlock> mineBlocks)
		{
			this.mineBlocks = mineBlocks;
			return this;
		}
		
	}
	
	private final String region;
	private final String mine;
	private final Map<MineBlock, Bounds> blockWeights = new HashMap<>();
	private final XORShiftRandom xorShiftRandom = new XORShiftRandom();
	
	private final List<MineBlock> mineBlocks;
	private int totalWright = 0;

	Mine(String region, String mine,
			List<MineBlock> mineBlocks)
	{
		this.region = region;
		this.mine = mine;
		this.mineBlocks = new ArrayList<>(mineBlocks);
		updateWrights();
	}
	
	Mine(String region, String mine)
	{
		this(region, mine, new ArrayList<>());
	}
	
	public String getRegion()
	{
		return this.region;
	}
	
	public String getMine()
	{
		return this.mine;
	}
	
	public Stream<MineBlock> getMineBlocks()
	{
		return new ArrayList<>(this.mineBlocks).stream();
	}

	public void updateWrights()
	{
		Bounds lastBounds = null;
		
		for (MineBlock mineBlock : this.mineBlocks)
		{
			Bounds bounds;
			
			if (lastBounds == null)
			{
				bounds = new Bounds(0, mineBlock.getWeight());
			}
			else
			{
				bounds = new Bounds(lastBounds.max, lastBounds.max + mineBlock.getWeight());
			}
			
			this.blockWeights.put(mineBlock, bounds);
			lastBounds = bounds;
		}
		
		this.totalWright = lastBounds == null ? 0 : lastBounds.max;
	}
	
	public MineBlock getNextBLock()
	{
		int roll = this.xorShiftRandom.nextInt(this.totalWright);
		
		for (Map.Entry<MineBlock, Bounds> entry : this.blockWeights.entrySet())
		{
			if (roll >= entry.getValue().min && roll <= entry.getValue().max)
			{
				return entry.getKey();
			}
		}
		
		return null;
	}

	private final class Bounds {

		private final int min;
		private final int max;

		Bounds(int min, int max)
		{
			this.min = min;
			this.max = max;
		}
		
	}

	private final class XORShiftRandom {

		private long last;

		XORShiftRandom()
		{
			this(System.currentTimeMillis());
		}

		XORShiftRandom(long seed)
		{
			this.last = seed;
		}

		private int nextInt(int max)
		{
			this.last ^= (this.last << 21);
			this.last ^= (this.last >>> 35);
			this.last ^= (this.last << 4);
			int out = (int) this.last % max;
			return (out < 0) ? -out : out;
		}

	}

}
