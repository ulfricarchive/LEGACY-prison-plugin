package com.ulfric.spigot.prison.cosmetic;

import com.ulfric.commons.naming.Name;
import com.ulfric.commons.spigot.command.Command;
import com.ulfric.commons.spigot.command.Context;
import com.ulfric.commons.spigot.command.argument.Argument;
import com.ulfric.commons.spigot.text.Text;
import com.ulfric.dragoon.Dynamic;
import com.ulfric.spigot.prison.metadata.PrisonMetadataDefaults;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.DynamicType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.annotation.Annotation;

public class CosmeticCommand implements Command {
	
	public static Class<?> createCosmeticCommand(Cosmetic cosmetic)
	{
		DynamicType.Builder<?> command = new ByteBuddy()
				.redefine(CosmeticCommand.class)
				.implement(Dynamic.class)
				.annotateType(CosmeticCommand.createDynamicName(cosmetic.getName()))
				.annotateType(CosmeticCommand.createDynamicCosmeticType(cosmetic.getName()));
		
		return command.make().load(cosmetic.getClass().getClassLoader()).getLoaded();
	}
	
	private static Name createDynamicName(String name)
	{
		return new Name()
		{
			@Override
			public Class<? extends Annotation> annotationType()
			{
				return Name.class;
			}
			
			@Override
			public String value()
			{
				return name;
			}
		};
	}
	
	private static CosmeticType createDynamicCosmeticType(String name)
	{
		return new CosmeticType()
		{
			@Override
			public String value()
			{
				return name;
			}
			
			@Override
			public Class<? extends Annotation> annotationType()
			{
				return CosmeticType.class;
			}
		};
	}
	
	@Argument
	private Player target;
	
	@Argument(optional = true)
	private Integer amount;
	
	@Override
	public void run(Context context)
	{
		Player target = this.target;
		Integer amount = this.amount;
		
		if (amount == null)
		{
			amount = 1;
		}
		
		Cosmetics cosmetics = Cosmetics.getService();
		CosmeticType type = this.getClass().getAnnotation(CosmeticType.class);
		cosmetics.give(target, type.value(), amount);
		
		Text text = Text.getService();
		CommandSender sender = context.getSender();
		
		text.sendMessage(sender, "cosmetic-give",
				PrisonMetadataDefaults.LAST_COSMETIC_GIVE_NAME, type.value(),
				PrisonMetadataDefaults.LAST_COSMETIC_GIVE_AMOUNT, String.valueOf(amount),
				PrisonMetadataDefaults.LAST_COSMETIC_GIVE_USER, target.getName());
		
		text.sendMessage(target, "cosmetic-receive",
				PrisonMetadataDefaults.LAST_COSMETIC_RECEIVE_NAME, type.value(),
				PrisonMetadataDefaults.LAST_COSMETIC_RECEIVE_AMOUNT, String.valueOf(amount));
	}
	
}
