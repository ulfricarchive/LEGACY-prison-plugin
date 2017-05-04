package com.ulfric.spigot.prison.mines;

import java.util.stream.Stream;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.service.Service;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.version.Version;

@Name("Mines")
@Version(1)
public interface Mines extends Service {

	static Mines getService()
	{
		return ServiceUtils.getService(Mines.class);
	}

	void regenerate(Mine mine);

	Stream<Mine> getMines();

}
