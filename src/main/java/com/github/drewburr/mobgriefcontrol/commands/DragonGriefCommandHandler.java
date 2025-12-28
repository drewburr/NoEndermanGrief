package com.github.drewburr.mobgriefcontrol.commands;

public class DragonGriefCommandHandler extends BooleanToggleCommandHandler {

	public DragonGriefCommandHandler() {
		super("do_dragon_destroy", "dragon");
	}

	@Override
	public String getCommandName() {
		return "do_dragon_destroy";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
