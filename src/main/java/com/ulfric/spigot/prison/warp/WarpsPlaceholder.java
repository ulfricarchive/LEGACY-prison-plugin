package com.ulfric.spigot.prison.warp;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.text.placeholder.Placeholder;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Name("WARPS")
public class WarpsPlaceholder implements Placeholder {

    @Override
    public String apply(CommandSender sender)
    {
        List<String> names = new ArrayList<>(Warps.getService().getWarps().keySet());

        Collections.sort(names);

        return StringUtils.join(names, ", ");
    }

}
