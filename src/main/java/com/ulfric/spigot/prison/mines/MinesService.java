package com.ulfric.spigot.prison.mines;

import com.ulfric.commons.spigot.data.Data;
import com.ulfric.commons.spigot.data.DataStore;
import com.ulfric.commons.spigot.data.PersistentData;
import com.ulfric.dragoon.container.Container;
import com.ulfric.dragoon.initialize.Initialize;
import com.ulfric.dragoon.inject.Inject;
import java.util.stream.Stream;

public class MinesService {

	@Inject
	private Container owner;

	@Initialize
	private void initialize()
	{
		DataStore dataStore = Data.getDataStore(owner);
		Stream<PersistentData> persistentDataStream = dataStore.loadAllData();
		persistentDataStream.forEach(persistentData ->
		{
			String mineName = persistentData.getName();
			String regioName = persistentData.getString("Region");
		});
	}

}
