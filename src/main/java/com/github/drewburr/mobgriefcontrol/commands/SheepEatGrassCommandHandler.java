package com.github.drewburr.mobgriefcontrol.commands;

public class SheepEatGrassCommandHandler extends BooleanToggleCommandHandler {

	public SheepEatGrassCommandHandler() {
		super("do_sheep_eat_grass", "sheep");
	}

	@Override
	public String getCommandName() {
		return "do_sheep_eat_grass";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
