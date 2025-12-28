package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the enderman grief command
 */
public class EndermanGriefCommandHandler extends BooleanToggleCommandHandler {

	public EndermanGriefCommandHandler() {
		super("do_enderman_pickup", "enderman");
	}

	@Override
	public String getCommandName() {
		return "do_enderman_pickup";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
