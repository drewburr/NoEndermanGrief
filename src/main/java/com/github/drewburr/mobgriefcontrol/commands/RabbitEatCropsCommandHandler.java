package com.github.drewburr.mobgriefcontrol.commands;

public class RabbitEatCropsCommandHandler extends BooleanToggleCommandHandler {

	public RabbitEatCropsCommandHandler() {
		super("do_rabbit_eat_crops", "rabbit");
	}

	@Override
	public String getCommandName() {
		return "do_rabbit_eat_crops";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
