package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.service.ServiceUtils;
import com.ulfric.commons.spigot.text.placeholder.Placeholder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

@Name("WARPS")
public class WarpsPlaceholder implements Placeholder {

    @Override
    public String apply(CommandSender sender)
    {
        WarpService service = ServiceUtils.getService(WarpService.class);

        return StringUtils.join(service.getNames(), ", ");
    }

}
