package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the enderman grief command
 */
public class EndermanGriefCommandHandler extends BooleanToggleCommandHandler {

	public EndermanGriefCommandHandler() {
		super("do_enderman_grief", "enderman");
	}

	@Override
	public String getCommandName() {
		return "endermangrief";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
