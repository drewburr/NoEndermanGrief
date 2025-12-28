package com.github.drewburr.mobgriefcontrol.commands;

public class RavagerGriefCommandHandler extends BooleanToggleCommandHandler {

	public RavagerGriefCommandHandler() {
		super("do_ravager_destroy_crops", "ravager");
	}

	@Override
	public String getCommandName() {
		return "do_ravager_destroy_crops";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
