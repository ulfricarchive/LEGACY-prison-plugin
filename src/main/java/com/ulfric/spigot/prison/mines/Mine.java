package com.ulfric.spigot.prison.mines;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.ulfric.commons.bean.Bean;
import com.ulfric.commons.naming.Named;
import com.ulfric.commons.spigot.weighted.WeightedTable;
import com.ulfric.commons.spigot.weighted.WeightedValue;

public final class Mine extends Bean implements Named {

	public static Builder builder()
	{
		return new Builder();
	}

	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Mine>
	{
		private String region;
		private String name;
		private List<MineBlock> mineBlocks;

		Builder() { }

		@Override
		public Mine build()
		{
			Objects.requireNonNull(this.region, "region");
			Objects.requireNonNull(this.name, "name");
			Objects.requireNonNull(this.mineBlocks, "mineBlocks");

			return new Mine(this.region, this.name, new ArrayList<>(this.mineBlocks));
		}

		public Builder setRegion(String region)
		{
			this.region = region;
			return this;
		}

		public Builder setName(String name)
		{
			this.name = name;
			return this;
		}

		public Builder setMineBlocks(List<MineBlock> mineBlocks)
		{
			this.mineBlocks = mineBlocks;
			return this;
		}
	}

	private final String region;
	private final String name;
	private final WeightedTable<MineBlock> blocks;

	private Mine(String region, String name,
	             List<MineBlock> mineBlocks)
	{
		this.region = region;
		this.name = name;
		this.blocks = this.createBlocksTable(mineBlocks);
	}

	private WeightedTable<MineBlock> createBlocksTable(List<MineBlock> blocks)
	{
		WeightedTable.Builder<MineBlock> builder = WeightedTable.builder();
		for (MineBlock block : blocks)
		{
			builder.add(new WeightedValue<>(block, block.getWeight()));
		}
		return builder.build();
	}

	public String getRegion()
	{
		return this.region;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	public MineBlock getNextBLock()
	{
		return blocks.nextValue();
	}

}