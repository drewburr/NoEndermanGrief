package com.github.drewburr.mobgriefcontrol.commands;

public class EndCrystalGriefCommandHandler extends BooleanToggleCommandHandler {

	public EndCrystalGriefCommandHandler() {
		super("do_endcrystal_explode", "endcrystal");
	}

	@Override
	public String getCommandName() {
		return "do_endcrystal_explode";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
