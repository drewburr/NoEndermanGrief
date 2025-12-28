package com.github.drewburr.mobgriefcontrol.commands;

/**
 * Handles the ghast grief command
 */
public class GhastGriefCommandHandler extends BooleanToggleCommandHandler {

	public GhastGriefCommandHandler() {
		super("do_ghast_grief", "ghast");
	}

	@Override
	public String getCommandName() {
		return "ghastgrief";
	}

	@Override
	public String[] getAliases() {
		return new String[0];
	}
}
