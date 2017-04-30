package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.bean.Bean;
import com.ulfric.plugin.platform.droptable.Drop;
import com.ulfric.plugin.platform.droptable.DropTable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Mine extends Bean {

	private final String region;
	private final String mine;
	private final DropTable dropTable = new DropTable();

	Mine(String region, String mine,
			List<MineBlock> mineBlocks)
	{
		this.region = region;
		this.mine = mine;
		for (MineBlock mineBlock : mineBlocks)
		{
			dropTable.add(new Drop(mineBlock, mineBlock.getWeight()));
		}
	}

	Mine(String region, String mine)
	{
		this(region, mine, new ArrayList<>());
	}

	public static Builder builder()
	{
		return new Builder();
	}

	public String getRegion()
	{
		return this.region;
	}

	public String getMine()
	{
		return this.mine;
	}

	public MineBlock getNextBLock()
	{
		return (MineBlock) dropTable.nextDrop().getValue();
	}

	public DropTable getDropTable()
	{
		return dropTable;
	}

	public static final class Builder implements org.apache.commons.lang3.builder.Builder<Mine> {

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

}
